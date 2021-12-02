# Flume 

​	Flume 是一种分布式、可靠且可用的服务，用于高效地收集、聚合和移动大量日志数据。它具有基于流数据流的简单灵活的架构。它具有可调整的可靠性机制和许多故障转移和恢复机制，具有健壮性和容错性。它使用一个简单的可扩展数据模型，允许在线分析应用程序。

架构图：

​	![代理组件图](https://gitee.com/wang_ao_qi/images/raw/master/mac_image/202112022034818.png)

# 1.什么是flume?
>Flume是Cloudera提供的一个==高可用的，高可靠的，分布式的海量日志采集、聚合和传输的软件==。
>Flume的核心是把数据从==数据源(source)==收集过来，再将收集到的数据送到指定的==目的地(sink)==。为了保证输送的过程一定成功，在送到目的地(sink)之前，会先==缓存数据(channel)==,待数据真正到达目的地(sink)后，flume在删除自己缓存的数据。
>Flume支持定制各类数据发送方，用于收集各类型数据；同时，Flume支持定制各种数据接受方，用于最终存储数据。一般的采集需求，通过对flume的简单配置即可实现。针对特殊场景也具备良好的自定义扩展能力。因此，flume可以适用于大部分的日常数据采集场景。
>当前Flume有两个版本。Flume 0.9X版本的统称==Flume OG==（original generation），Flume1.X版本的统称Flume NG（next generation）。由于==Flume NG==经过核心组件、核心配置以及代码架构重构，与Flume OG有很大不同，使用时请注意区分。改动的另一原因是将Flume纳入 apache 旗下，Cloudera Flume 改名为 Apache Flume。

# 2.安装教程
- Flume的安装非常简单
	- 上传安装包到数据源所在节点上
	

```
安装包下载:
链接：https://pan.baidu.com/s/11UsR4Y0d305yomJJ3K8ByA 
提取码：0eyd
```

- 然后解压  tar -zxvf apache-flume-1.8.0-bin.tar.gz
- 然后进入flume的目录，修改conf下的flume-env.sh，在里面配置JAVA_HOME
1.看下原本的conf目录
![在这里插入图片描述](https://gitee.com/wang_ao_qi/images/raw/master/mac_image/202112022035511.png)
2.拷贝flume-env.sh.template为flume-env.sh

```
cp flume-env.sh.template flume-env.sh
```
3.编辑文件

```
vim flume-env.sh
```
4.修改JAVA_HOME
![在这里插入图片描述](https://gitee.com/wang_ao_qi/images/raw/master/mac_image/202112022035634.png)
# 3.测试一下
### 1、先在flume的conf目录下新建一个文件

```
vi   netcat-logger.conf
```
添加以下内容到文件

```
# 定义这个agent中各组件的名字
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# 描述和配置source组件：r1
a1.sources.r1.type = netcat
a1.sources.r1.bind = localhost
a1.sources.r1.port = 44444

# 描述和配置sink组件：k1
a1.sinks.k1.type = logger

# 描述和配置channel组件，此处使用是内存缓存的方式
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# 描述和配置source  channel   sink之间的连接关系
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1
```

### 2. 在Flume安装目录下启动agent去采集数据

```
bin/flume-ng agent -c conf -f conf/netcat-logger.conf -n a1  -Dflume.root.logger=INFO,console
```
>-c conf   指定flume自身的配置文件所在目录
>-f conf/netcat-logger.con  指定我们所描述的采集方案
>-n a1  指定我们这个agent的名字
### 3.测试
再开一shell窗口  输入以下命令

```
telnet localhost 44444
```
==注意:== 如出现找不到这个命令  则是没有安装 telnet 服务
就去安装一下 再来执行上面的命令
报错解决: [上一个博客中提到,点我直达-->](https://blog.csdn.net/weixin_43893397/article/details/103394146)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191204215503520.png)

- 连上后会显示一下  
	开启telnet服务的窗口:
	 ![在这里插入图片描述](https://img-blog.csdnimg.cn/2019120421595092.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzg5MzM5Nw==,size_16,color_FFFFFF,t_70)
	 新开的窗口:
	 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20191204220136366.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzg5MzM5Nw==,size_16,color_FFFFFF,t_70)
##### 在新开的窗口随便输入一个字符串  123  回车
![在这里插入图片描述](https://gitee.com/wang_ao_qi/images/raw/master/mac_image/202112022035955.png)
#### 在开启telnet服务的窗口就可以看到
![在这里插入图片描述](https://gitee.com/wang_ao_qi/images/raw/master/mac_image/202112022035177.png)