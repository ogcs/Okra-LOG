# Okra-LOG说明

# 依赖

 1. JDK 1.8
 2. Netty 4.x
 3. Disruptor 3.x
 4. HikariCP 2.x
 5. MySQL-connector/J
 6. Okra 1.0

# 流程说明

 1. Client上报日志到任务版[MissionBoard]。 任务板将日志分类归理[Struct结构]，当某种类型日志累计一定数量[缺省值:100]触发记录任务
 2. 任务板派发记录任务给Disruptor。异步处理执行数据库写入任务
 3. 写入失败，将数据返回添加到任务版， 等待下一次触发写入任务时，重新尝试写入。
 4. 当程序关闭（除死机，kill -9,jvm崩溃等情况外）将剩余未落地的数据全部写入数据库。

#




