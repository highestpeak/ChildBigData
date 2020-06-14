# 放置数据到hdfs

``` shell
hadoop fs -rm -r -f /data
hadoop fs -mkdir -p /data/bigPractice/user_log
hadoop fs -put user_log.csv /data/bigPractice/user_log
```

# hive表创建

``` sql
-- big_practice 数据库
CREATE DATABASE big_practice;
USE big_practice;
-- user_log表
CREATE EXTERNAL TABLE user_log(
    user_id INT,
    item_id INT,
    cat_id INT,
    merchant_id INT,
    brand_id INT,
    month STRING,
    day STRING,
    action INT,
    age_range INT,
    gender INT,
    province STRING
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS TEXTFILE 
LOCATION '/data/bigPractice/user_log';
DESC user_log; -- 查看表结构
```

# 分析1

``` sql
DROP TABLE IF EXISTS dws_simple_analysis;
CREATE EXTERNAL TABLE dws_simple_analysis(
    key string COMMENT '分析内容',
    value string COMMENT '分析结果'
) COMMENT '简单的数据分析，键为分析名，值为分析结果'
LOCATION '/data/bigPractice/dws/dws_simple_analysis/';

-- 导入数据:
-- 统计活跃的总用户数
INSERT INTO TABLE dws_simple_analysis 
SELECT "活跃的总用户数",COUNT(DISTINCT user_id) from user_log;
-- 统计总体店铺数
INSERT INTO TABLE dws_simple_analysis 
SELECT "总体店铺数",COUNT(DISTINCT merchant_id) from user_log;
-- 统计总体品牌数
INSERT INTO TABLE dws_simple_analysis 
SELECT "总体品牌数",COUNT(DISTINCT brand_id) from user_log;

-- 统计总成交数 ???
INSERT INTO TABLE dws_simple_analysis 
SELECT "总成交数",COUNT(user_id) from user_log where action = 2;

-- 双十一那天购买商品的用户数
INSERT INTO TABLE dws_simple_analysis 
SELECT "双十一那天购买商品的用户数",COUNT(DISTINCT user_id) from user_log WHERE month = '11' AND day = '11' AND action = 2;
-- 统计男女购买商品的比率
INSERT INTO TABLE dws_simple_analysis 
SELECT "女性购买商品的比率", COUNT(*) FROM user_log where gender=0 AND action = '2'; --女性 1097815
INSERT INTO TABLE dws_simple_analysis 
SELECT "男性购买商品的比率", COUNT(*) FROM user_log where gender=1 AND action = '2'; --男性 1096182

-- top 10 （分析销量top 10的商品/店铺/品牌）??? 拼接成一条？？
INSERT INTO TABLE dws_simple_analysis 
SELECT "销量top 10的商品",CONCAT(x.item_id,"-",x.counts) 
FROM(
    SELECT item_id,count(item_id) counts 
    FROM user_log Group By item_id
    Order By counts DESC LIMIT 10
) as x;

INSERT INTO TABLE dws_simple_analysis 
SELECT "销量top 10的店铺",CONCAT(x.merchant_id,"-",x.counts) 
FROM(
    SELECT merchant_id,count(merchant_id) counts 
    FROM user_log Group By merchant_id 
    Order By counts DESC LIMIT 10
) as x;


INSERT INTO TABLE dws_simple_analysis 
SELECT "销量top 10的品牌",CONCAT(x.brand_id,"-",x.counts) 
FROM(
    SELECT brand_id,count(brand_id) counts
    FROM user_log Group By brand_id 
    Order By counts DESC LIMIT 10
) as x;

-- 分析各个省份的消费能力（用户数/购买商品数）???
INSERT INTO TABLE dws_simple_analysis 
SELECT "各个省份的消费能力", 
    concat(
        province,"_",COUNT(DISTINCT user_id),"_",COUNT(item_id)
    )
FROM user_log where action=2 Group By province ;
```

# 分析2-用户活跃数

``` sql
-- 统计每天的用户活跃数
DROP TABLE IF EXISTS dws_uv_detail_day;
CREATE EXTERNAL TABLE dws_uv_detail_day(
    user_id INT COMMENT '用户id',
    dt string COMMENT '日期'
) COMMENT '用户每天活跃'
LOCATION '/data/bigPractice/dws/dws_uv_detail_day/';
-- 导入数据
INSERT OVERWRITE TABLE dws_uv_detail_day
SELECT
    user_id,
    CONCAT('2020-', LPAD(month, 2, 0), '-', LPAD(day, 2, 0)) dt 
FROM user_log GROUP BY user_id,month,day;

-- 统计每周的用户活跃数
DROP TABLE IF EXISTS dws_uv_detail_wk;
CREATE EXTERNAL TABLE dws_uv_detail_wk(
    total INT COMMENT '数量',
    wk_num string COMMENT '第几周'
) COMMENT '用户每周活跃'
LOCATION '/data/bigPractice/dws/dws_uv_detail_wk/';
-- 导入数据
INSERT OVERWRITE TABLE dws_uv_detail_wk
SELECT 
    COUNT(DISTINCT user_id), weekofyear(dt) 
FROM dws_uv_detail_day GROUP BY weekofyear(dt);

-- 统计每月的用户活跃数
DROP TABLE IF EXISTS dws_uv_detail_month;
CREATE EXTERNAL TABLE dws_uv_detail_month(
    total INT COMMENT '数量',
    month string COMMENT '第几月'
) COMMENT '用户每月活跃'
LOCATION '/data/bigPractice/dws/dws_uv_detail_month/';
-- 导入数据
INSERT OVERWRITE TABLE dws_uv_detail_month
SELECT 
    COUNT(DISTINCT user_id), month
FROM user_log GROUP BY month;
```

# 分析3-新增用户

``` sql
-- 1.新增用户专题
-- 使用的用户,登录的用户: user_use_dt
DROP TABLE IF EXISTS dws_user_use_dt;
CREATE EXTERNAL TABLE dws_user_use_dt(
    user_id INT COMMENT '用户id',
    dt string COMMENT '用户登录/使用日期'
) COMMENT '某天使用的用户,登录的用户'
LOCATION '/data/bigPractice/dws/dws_user_use_dt/';
-- 插入
INSERT OVERWRITE TABLE dws_user_use_dt
select user_id,
CONCAT(
    '2020-',LPAD(month, 2, 0), '-', LPAD(day, 2, 0)
) dt from user_log 
where action = 0 OR action = 1 OR action = 2;

-- 1.1每日新增用户
DROP TABLE IF EXISTS dws_uv_new_day;
CREATE EXTERNAL TABLE dws_uv_new_day(
    user_id INT COMMENT '用户id',
    dt string COMMENT '用户第一次登入日期'
) COMMENT '每日新增用户'
LOCATION '/data/bigPractice/dws/dws_uv_new_day/';
-- 插入
INSERT OVERWRITE TABLE dws_uv_new_day
SELECT 
    user_id,
    MIN(dt) dt
FROM dws_user_use_dt
GROUP BY user_id;

-- 1.2每周新增用户
DROP TABLE IF EXISTS dws_uv_new_wk;
CREATE EXTERNAL TABLE dws_uv_new_wk(
    user_id INT COMMENT '用户id',
    wk string COMMENT '用户第一次登入周数'
) COMMENT '每周新增用户'
LOCATION '/data/bigPractice/dws/dws_uv_new_wk/';
-- 插入
INSERT OVERWRITE TABLE dws_uv_new_wk
SELECT 
    user_id,
    weekofyear(dt) wk
FROM dws_uv_new_day;

-- 1.2每月新增用户
DROP TABLE IF EXISTS dws_uv_new_month;
CREATE EXTERNAL TABLE dws_uv_new_month(
    user_id INT COMMENT '用户id',
    month string COMMENT '用户第一次登入月份'
) COMMENT '每月新增用户'
LOCATION '/data/bigPractice/dws/dws_uv_new_month/';
-- 插入
INSERT OVERWRITE TABLE dws_uv_new_month
SELECT 
    user_id,
    month(dt) month
FROM dws_uv_new_day;
```

# 分析4-留存用户

``` sql
-- 2.用户留存专题
-- 新增用户经过一段时间后，又继续使用被称为留存用户
-- (curr-x天当日新增用户数，该日还登录的用户数)/(curr-x天当日总注册用户数)
-- 每天计算前1、2、3、4、7、14天的留存率
DROP TABLE IF EXISTS dws_uv_keep_rate;
CREATE EXTERNAL TABLE dws_uv_keep_rate(
    dt string COMMENT '日期',
    before_gap INT COMMENT '前before_gap天',
    rate DOUBLE COMMENT '前before_gap天的留存率'
) COMMENT '用户留存率'
LOCATION '/data/bigPractice/dws/dws_uv_keep_rate/';

-- 插入(替换数字为1、2、3、4、7、14)
WITH new_still as (
    select 
        uvuse.dt curr,
        count(uvuse.dt) count_new_still
    from dws_uv_new_day uvnew inner join dws_user_use_dt uvuse 
    on uvnew.user_id = uvuse.user_id
    where datediff(uvuse.dt,uvnew.dt) = 14 
    group by uvuse.dt
),
new_before as (
    select 
        uvnew.dt before,
        count(uvnew.dt) count_new 
    from dws_uv_new_day uvnew
    group by uvnew.dt
)
INSERT INTO TABLE dws_uv_keep_rate
SELECT 
    new_still.curr dt,
    14,
    if(new_still.count_new_still is 
       null,0,new_still.count_new_still)/new_before.count_new
       rate
FROM new_before inner join new_still 
where datediff(new_still.curr,new_before.before) = 14;
```

``` sql
-- 计算留存率的中间步骤
-- 使用的用户,登录的用户: dws_user_use_dt
select user_id,
CONCAT(
    '2020-',LPAD(month, 2, 0), '-', LPAD(day, 2, 0)
) dt from user_log 
where action = 0 OR action = 1 OR action = 2;

-- (curr-x天当日新增用户数，该日还登录的用户数)
    select 
        collect_set(uvuse.dt)[0] curr,
        count(uvuse.dt) count_new_still
    from dws_uv_new_day uvnew, dws_user_use_dt uvuse 
    where datediff(uvuse.dt,uvnew.dt) = 7 and
          uvnew.user_id = uvuse.user_id
    group by uvuse.dt

-- (curr-x天当日总注册用户数)
    select 
        collect_set(uvuse.dt)[0] curr,
        count(uvnew.dt) count_new 
    from dws_uv_new_day uvnew, dws_user_use_dt uvuse 
    where datediff(uvuse.dt,uvnew.dt) = 7
    group by uvnew.dt

-- 留存率
x_before_curr_new_still/x_before_curr_new
```



# 分析5-沉默用户、流失用户

``` sql
DROP TABLE IF EXISTS dws_user_login;
CREATE EXTERNAL TABLE dws_user_login(
    user_id INT COMMENT '用户id',
    dt string COMMENT '日期'
) COMMENT '每天沉默数'
LOCATION '/data/bigPractice/dws/dws_user_login/';
insert into dws_user_login
select
    user_id,
    CONCAT(
        '2020-',LPAD(user_log.month, 2, 0), '-',
        LPAD(user_log.day, 2, 0)
    ) dt
FROM user_log order by dt desc;

-- 3.沉默用户数
-- 只在当天启动过，且启动时间是在一个月前
-- 即除了第一次登录过，在一个月内再也没有登录过？
DROP TABLE IF EXISTS dws_uv_slience_count;
CREATE EXTERNAL TABLE dws_uv_slience_count(
    dt string COMMENT '日期',
    slience_count INT COMMENT '沉默数'
) COMMENT '每天沉默数'
LOCATION '/data/bigPractice/dws/dws_uv_slience_count/';
with first_login as (
    SELECT user_id,collect_set(dt)[0] dt
    FROM dws_uv_new_day
    group by user_id
)
insert into dws_uv_slience_count
SELECT 
    dws_user_login.dt dt,
    count(dws_user_login.dt) slience_count
FROM
    first_login inner join dws_user_login
    on dws_user_login.user_id=first_login.user_id 
where datediff(dws_user_login.dt,first_login.dt)>=30
group by dws_user_login.dt;
```
``` sql
-- 4.流失用户
-- 1个月未登陆的用户
-- 统计每天流失的用户数
DROP TABLE IF EXISTS dws_uv_loss_count;
CREATE EXTERNAL TABLE dws_uv_loss_count(
    dt string COMMENT '日期',
    loss_count INT COMMENT '流失数'
) COMMENT '每天流失的用户数'
LOCATION '/data/bigPractice/dws/dws_uv_loss_count/';

-- 中间步骤：找出某用户登陆时间排序
-- select user_id,CONCAT(
--     '2020-',LPAD(month, 2, 0),
--     '-', LPAD(day, 2, 0)) dt
-- from user_log where user_id=422265 order by dt desc;

with last_login as (
    SELECT user_id,collect_set(dt)[0] dt
    FROM dws_user_login
    group by user_id
)
insert into dws_uv_loss_count
SELECT 
    dws_user_login.dt dt,
    count(dws_user_login.dt) loss_count
FROM 
    last_login inner join dws_user_login
    on dws_user_login.user_id=last_login.user_id 
where datediff(dws_user_login.dt,last_login.dt)>=30
group by dws_user_login.dt;
```

# 分析6-最近连续三周

``` sql
-- 5.最近连续三周活跃的用户数
-- 最近3周连续活跃的用户：通常是周一对前3周的数据做统计，该数据一周计算一次 
-- https://www.zhihu.com/question/56394037
DROP TABLE IF EXISTS dws_uv_wk_active_count;
CREATE EXTERNAL TABLE dws_uv_wk_active_count(
    user_id INT COMMENT '用户id',
    wk INT COMMENT '第几周'
) COMMENT '最近连续三周活跃的用户数'
LOCATION '/data/bigPractice/dws/dws_uv_wk_active_count/';
-- 每周统计一次前三周活跃的用户数
with active_wk as (
    select
        user_id,
        weekofyear(dt) wk_num 
    FROM dws_uv_detail_day 
    GROUP BY weekofyear(dt),user_id
    order by user_id,wk_num desc
), rank_tmp as (
    select 
        user_id,
        wk_num,
        row_number() over(
            partition by user_id order by wk_num
        ) as rank
    from active_wk
), wk_rank_tmp as (
    select user_id,wk_num,wk_num-rank wk_rank
    from rank_tmp
), continue_wk as (
    select user_id,wk_rank,count(*) cnt
    from wk_rank_tmp
    group by wk_rank,user_id
    having cnt>=3
)
insert into dws_uv_wk_active_count
select user_id,wk_rank+3 wk
from continue_wk
where cnt>=3;
-- user_id在第wk_rank周之前的最近连续三周连续活跃

-- 第一版的写法
-- active_wk_list as (
--     select wk_num from active_wk
-- )
-- SELECT 
--     active_wk.user_id user_id,
--     active_wk.wk_num wk_num
-- from active_wk,active_wk_list a,active_wk_list b
-- where 
--  active_wk.wk_num = a.wk_num+1 and
--  active_wk.wk_num = a.wk_num+2 ;
-- active_wk 是 每周活跃的用户；
```

## 最近7天内连续三天活跃的用户数

``` sql
-- 6.最近7天内连续三天活跃的用户数
DROP TABLE IF EXISTS dws_uv_7day_active_count;
CREATE EXTERNAL TABLE dws_uv_7day_active_count(
    user_id INT COMMENT '用户id',
    dt string COMMENT '日期'
) COMMENT '最近连续三周活跃的用户数'
LOCATION '/data/bigPractice/dws/dws_uv_7day_active_count/';

with active_day as (
    select
        user_id,
        dt 
    FROM dws_uv_detail_day 
    GROUP BY dt,user_id
    order by user_id,dt desc
), rank_tmp as (
    select 
        user_id,
        dt,
        row_number() over(
            partition by user_id order by dt
        ) as rank
    from active_day
), day_rank_tmp as (
    select user_id,dt,date_sub(dt,rank) day_rank
    from rank_tmp
), continue_day as (
    select user_id,day_rank,count(*) cnt
    from day_rank_tmp
    group by day_rank,user_id
    having cnt>=3
)
insert into dws_uv_7day_active_count
select 
    continue_day.user_id user_id,
    date_add(dws_uv_detail_day.dt,3) dt
from continue_day inner join dws_uv_detail_day
on continue_day.user_id = dws_uv_detail_day.user_id
where datediff(dws_uv_detail_day.dt,continue_day.day_rank) <=10
 and datediff(dws_uv_detail_day.dt,continue_day.day_rank) >=3
 and cnt>=3;
-- user_id在第 dt_rank 天之后的最近连续三天连续活跃

-- with curr as (
--     SELECT
--         user_id,
--         CONCAT(
--             '2020-',LPAD(month, 2, 0), '-', LPAD(day, 2, 0)
--         ) dt
--     from user_log 
-- ),curr_before_week as (
--     select
--         curr.user_id user_id,
--         curr.dt dt
--     from curr,curr before_week
--     where datediff(curr.dt,before_week.dt) <=7 and     
--           datediff(curr.dt,before_week.dt) >=0
-- ),curr_before_week_list as (
--     select dt from curr_before_week
-- )
-- select 
--     curr_before_week.user_id,
--     curr_before_week.dt
-- from 
--     curr_before_week,
--     curr_before_week_list a,
--     curr_before_week_list b,
--     curr_before_week_list c
-- where 
--     (
--     datediff(curr_before_week.dt,a.dt) = 1 and 
--     datediff(curr_before_week.dt,b.dt) = 2 and 
--     datediff(curr_before_week.dt,c.dt) = 3 
--     ) 
--     or 
--     (
--     datediff(a.dt,curr_before_week.dt) = 1 and 
--     datediff(b.dt,curr_before_week.dt) = 2 and 
--     datediff(c.dt,curr_before_week.dt) = 3
--     );
-- 这种情况是连续三天都在七天内
```
# 分析7-漏斗分析

``` sql
-- 7.漏斗分析
-- 分析浏览、加入购物车、成单的比率
DROP TABLE IF EXISTS dws_uv_visit_count;
CREATE EXTERNAL TABLE dws_uv_visit_count(
    item_id INT COMMENT '商品id',
    type INT COMMENT '0:浏览、加入购物车的比率,1:浏览、成单的比率',
    rate DOUBLE COMMENT '比率'
) COMMENT '分析浏览、加入购物车、成单的比率'
LOCATION '/data/bigPractice/dws/dws_uv_visit_count/';

-- 游览、加入购物车的比率
with visit as(
    select item_id,count(*) visitcount
    from user_log
    where action = 0
    group by item_id
),add_to_cart as (
    select item_id,count(*) cartcount
    from user_log
    where action = 1
    group by item_id
)
insert into dws_uv_visit_count
select visit.item_id item_id, 0,
    add_to_cart.cartcount/visit.visitcount rate
from add_to_cart inner join visit
on visit.item_id = add_to_cart.item_id;

-- 游览、成单的比率
with visit as(
    select item_id,count(*) visitcount
    from user_log
    where action = 0
    group by item_id
),buy as (
    select item_id,count(*) cartcount
    from user_log
    where action = 2
    group by item_id
)
insert into dws_uv_visit_count 
select visit.item_id item_id, 1,
    buy.cartcount/visit.visitcount rate
from visit,buy
where visit.item_id = buy.item_id;
```

