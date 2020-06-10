# 准备

``` mysql
CREATE DATABASE big_practice;
USE big_practice;
```

# 导入数据

## 基础数据分析

简单的数据分析，键为分析名，值为分析结果

| key                        | value                      |
| -------------------------- | -------------------------- |
| 活跃的总用户数             | count                      |
| 总体店铺数                 | count                      |
| 总体品牌数                 | count                      |
| 总成交数                   | count                      |
| 双十一那天购买商品的用户数 | count                      |
| 女性购买商品的比率         | count                      |
| 男性购买商品的比率         | count                      |
| 销量top 10的商品           | id-count                   |
| 销量top 10的店铺           | id-count                   |
| 销量top 10的品牌           | id-count                   |
| 各个省份的消费能力         | name_user-count_item-count |



``` sql
create table dws_simple_analysis(
    analy_key varchar(256),
    analy_value text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_simple_analysis;
```



``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_simple_analysis --export-dir /data/bigPractice/dws/dws_simple_analysis/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```



## 用户活跃

### 用户id-活跃日期查询

``` sql
create table dws_user_active_day(
    user_id int,
    dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_active_day;
```



``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_user_active_day --export-dir /data/bigPractice/dws/dws_uv_detail_day/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```



### 用户活跃数查询

- `cnt`：数量

- `type`：

- `certain_dt` ：

  | type-value | `type` description | `certain_dt` description |
  | ---------- | ------------------ | ------------------------ |
  | 0          | 每天活跃           | 日期:`yyyy-MM-dd`        |
  | 1          | 每周活跃           | 第几周                   |
  | 2          | 每月活跃           | 第几月                   |

  

``` sql
create table dws_user_active_count(
    cnt int,
    type SMALLINT,
    certain_dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_active_count;
```

计算每天的活跃，插入数据到活跃数表

``` sql
insert into dws_user_active_count
select count(*),0,dt
from dws_user_active_day
group by dt;
```



每周、每月中间临时表

``` sql
create table tmp_active_count(
    cnt int,
    certain_dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC tmp_active_count;
```

导入每周数据

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table tmp_active_count --export-dir /data/bigPractice/dws/dws_uv_detail_wk/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

插入每周数据到活跃数表

``` sql
insert into dws_user_active_count
select cnt,1,certain_dt
from tmp_active_count;

delete from tmp_active_count;
```



导入每月数据

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table tmp_active_count --export-dir /data/bigPractice/dws/dws_uv_detail_month/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

插入每月数据到活跃数表

``` sql
insert into dws_user_active_count
select cnt,2,certain_dt
from tmp_active_count;

drop table tmp_active_count;
```

## 新增用户

- `cnt`：数量

- `type`：

- `certain_dt` ：

  | type-value | `type` description | `certain_dt` description |
  | ---------- | ------------------ | ------------------------ |
  | 0          | 每天新增           | 日期:`yyyy-MM-dd`        |
  | 1          | 每周新增           | 第几周                   |
  | 2          | 每月新增           | 第几月                   |

``` sql
create table dws_user_new_count(
    cnt int,
    type SMALLINT,
    certain_dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_new_count;
```



每天、每周、每月中间临时表

``` sql
create table tmp_new_count(
    user_id int,
    certain_dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC tmp_new_count;
```

导入每日数据

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table tmp_new_count --export-dir /data/bigPractice/dws/dws_uv_new_day/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

插入每日数据到新增数表

``` sql
insert into dws_user_new_count
select count(*),0,certain_dt
from tmp_new_count
group by certain_dt;

delete from tmp_new_count;
```

每周、每月数据同理

每周每月的数据位置：

- `/data/bigPractice/dws/dws_uv_new_wk/`
- `/data/bigPractice/dws/dws_uv_new_month/`

``` sql
drop table tmp_new_count;
```



## 留存用户

- 前1、2、3、4、7、14天(before_gap)的留存率

``` sql
create table dws_user_keep_rate(
    dt varchar(20),
    before_gap INT,
    rate DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_keep_rate;
```

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_user_keep_rate --export-dir /data/bigPractice/dws/dws_uv_keep_rate/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

## 沉默、流失用户

- `dt`：日期:`yyyy-MM-dd`
- `slience_cnt`：沉默数
- `loss_cnt` ：流失数

``` sql
create table dws_slience_loss_count(
    dt varchar(20),
    slience_cnt int,
    loss_cnt int
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_slience_loss_count;
```



临时表

``` sql
create table dws_slience_count(
    dt varchar(20),
    slience_cnt int
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_slience_count;

create table dws_loss_count(
    dt varchar(20),
    loss_cnt int
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_loss_count;
```

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_slience_count --export-dir /data/bigPractice/dws/dws_uv_slience_count/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'

sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_loss_count --export-dir /data/bigPractice/dws/dws_uv_loss_count/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

``` sql
insert into dws_slience_loss_count
select dws_slience_count.dt,slience_cnt,loss_cnt
from dws_slience_count natural join dws_loss_count;

drop table dws_slience_count;
drop table dws_loss_count;
```

## 最近连续三周

查询某一周的 最近连续三周 活跃的用户

``` sql
CREATE TABLE dws_user_wk_active_continue3(
    user_id INT,
    wk INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_wk_active_continue3;
```

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_user_wk_active_continue3 --export-dir /data/bigPractice/dws/dws_uv_wk_active_count/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

## 最近7天内连续三天

查询某一天的 最近7天内连续三天 活跃的用户

``` sql
CREATE TABLE dws_user_active_7day3continue(
    user_id INT,
    dt varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_user_active_7day3continue;
```

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_user_active_7day3continue --export-dir /data/bigPractice/dws/dws_uv_7day_active_count/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

## 漏斗分析

| type | rate-description       |
| ---- | ---------------------- |
| 0    | 浏览、加入购物车的比率 |
| 1    | 浏览、成单的比率       |



``` sql
CREATE TABLE dws_item_visit_cart_buy_rate(
    item_id INT,
    rate_type INT,
    rate_value DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DESC dws_item_visit_cart_buy_rate;
```

``` shell
sqoop export --connect "jdbc:mysql://node-master:3306/big_practice?useUnicode=true&characterEncoding=utf-8" --username hivetomysql --password admin --table dws_item_visit_cart_buy_rate --export-dir /data/bigPractice/dws/dws_uv_visit_count/* --input-fields-terminated-by '\001' --input-lines-terminated-by '\n'
```

