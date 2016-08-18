
备份查询中常用的SQL

# 某段时间内，根据货币类型分组，统计每种货币的总消耗量
SELECT ioType, SUM(VALUE) AS total FROM log_money WHERE io = 1 AND logDate BETWEEN '2016-08-10' AND '2016-08-12' GROUP BY ioType LIMIT 0,1000;

SELECT ioType, SUM(VALUE) AS total , DATE_FORMAT(logDate, '%Y-%m-%d') AS tDate
FROM log_money WHERE io = 1 AND logDate BETWEEN '2016-08-10' AND '2016-08-12' GROUP BY tDate LIMIT 0,1000;

# 某段时间内，某项货币，获得/消耗 总数变更规律     [x(日期), y(数量)]
SELECT ioType, SUM(VALUE) AS total , DATE_FORMAT(logDate, '%Y-%m-%d') AS tDate
FROM log_money WHERE io = 1  AND ioType = 1 AND logDate BETWEEN '2016-08-10' AND '2016-08-12' GROUP BY tDate LIMIT 0,1000;