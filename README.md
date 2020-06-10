# ChildBigData
实训临时工作仓库

node-master上的mysql的账户和密码：

| user         | password |
| ------------ | -------- |
| root         | admin    |
| hivetomysql  | admin    |
| childbigdata | admin    |

放置数据到hdfs

``` shell
hadoop fs -rm -r -f /data
hadoop fs -mkdir -p /data/smallPractice
hadoop fs -put small_user_log.csv /data/smallPractice
```

hive表创建

``` sql
CREATE DATABASE small_practice;
USE small_practice;
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
LOCATION '/data/smallPractice';
DESC user_log; -- 查看表结构

DROP TABLE IF EXISTS dws_uv_detail_month;
CREATE EXTERNAL TABLE dws_uv_detail_month(
    total INT COMMENT '数量',
    month string COMMENT '第几月'
) COMMENT '用户每月活跃'
LOCATION '/data/smallPractice/dws/dws_uv_detail_month/';

-- 导入数据：
INSERT OVERWRITE TABLE dws_uv_detail_month
SELECT COUNT(DISTINCT user_id), month FROM user_log GROUP BY month;
```

mysql表创建

``` sql
CREATE DATABASE small_practice;
USE small_practice;
create table dws_uv_detail_month(
    total int,
    month varchar(3)
);
DESC dws_uv_detail_month;
```

sqoop同步数据

``` shell
bin/sqoop export --connect jdbc:mysql://node-master:3306/small_practice --username hivetomysql --password admin --table dws_uv_detail_month --export-dir /data/smallPractice/dws/dws_uv_detail_month/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

注意：

1. mysql和hive表的字段顺序必须一致，类型必须一致，名称应该一致
2. 



``` sql
-- 1.新增用户专题
-- 第一次使用的用户（点击商品/加入购物车/购买）算作新增用户
-- 1.1每日新增用户
DROP TABLE IF EXISTS dws_uv_new_day;
CREATE EXTERNAL TABLE dws_uv_new_day(
    total INT COMMENT '数量',
    dt string COMMENT '日期'
) COMMENT '每日新增用户'
LOCATION '/data/smallPractice/dws/dws_uv_new_day/';
INSERT OVERWRITE TABLE dws_uv_new_day
SELECT COUNT(user_id) user_count,shopping_date 
FROM 
    (
        SELECT user_id, 
        MIN(
            CONCAT(
                LPAD(month, 2, 0), '-', LPAD(day, 2, 0)
            )
        ) as shopping_date
        FROM user_log
        WHERE action = 0 OR action = 1 OR action = 2
        GROUP BY user_id
    ) as t
GROUP BY shopping_date 
ORDER BY shopping_date;

-- 1.2每周新增用户
DROP TABLE IF EXISTS dws_uv_new_wk;
CREATE EXTERNAL TABLE dws_uv_new_wk(
    total INT COMMENT '数量',
    wk_num string COMMENT '第几周'
) COMMENT '每周新增用户'
LOCATION '/data/smallPractice/dws/dws_uv_new_wk/';
INSERT OVERWRITE TABLE dws_uv_new_wk
SELECT COUNT(user_id) user_count,shopping_date 
FROM 
    (
        SELECT user_id,weekofyear(dt) as shopping_date
        FROM dws_uv_new_day
        GROUP BY weekofyear(dt)
    ) as t
GROUP BY shopping_date 
ORDER BY shopping_date;

-- 1.3每月新增用户
DROP TABLE IF EXISTS dws_uv_new_month;
CREATE EXTERNAL TABLE dws_uv_new_month(
    total INT COMMENT '数量',
    month string COMMENT '第几月'
) COMMENT '每月新增用户'
LOCATION '/data/smallPractice/dws/dws_uv_new_month/';
INSERT OVERWRITE TABLE dws_uv_new_month
SELECT COUNT(user_id) user_count,shopping_date 
FROM 
    (
        SELECT user_id,month(dt) as shopping_date
        FROM dws_uv_new_wk
        GROUP BY month(dt)
    ) as t
GROUP BY shopping_date 
ORDER BY shopping_date;
```

``` sql
- COUNT(DISTINCT)
  “计算连续7天”,可以通过GROUP BY分组和COUNT()来完成。因为一个用户在1天内可能会有多次登录，
  这里需要使用(COUNT DISTINCT）. SQL 语句为：

GROUP BY t.login_time, t.uid
HAVING COUNT(DISTINCT date(t1.login_time))=6
```

