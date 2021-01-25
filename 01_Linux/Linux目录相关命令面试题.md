# Linux目录相关命令

## cd

`cd -` ：可以在最近工作的连个目录间切换

![image-20210122140318525](https://i.loli.net/2021/01/22/mH8GbMIPXyC1cUn.png)

## ls

### 1.如何以简单易读的方式显示文件的大小（MB，GB。。。。）

`ls -lh`

list  files

参数详解  ：

- -l 除文件名称外，亦将文件型态、权限、拥有者、文件大小等资讯详细列出

- -h 简单易读的方式显示文件的大小

![image-20210122150853034](https://i.loli.net/2021/01/22/maAg4TPV8dBDnQI.png)

### 2.如何以修改时间升序列出文件

`ls -ltr`

参数详解  ：

- -r 将文件以相反次序显示(原定依英文字母次序)
- -t 将文件依建立时间之先后次序列出

![image-20210122150957158](https://i.loli.net/2021/01/22/8wiHfoEd9u6KLMZ.png)

### 3.如何在文件名后面显示文件类型

`ls -F`

参数详解:

-F 在列出的文件名称后加一符号；例如可执行档则加 "*", 目录则加 "/"

![image-20210122155121637](https://i.loli.net/2021/01/22/j7YNpBAuCRM81ZG.png)

## cat 

### 1.如何快速的将两个文件合并到一起

`cat file1 file2 > file3`

![image-20210122155627415](https://i.loli.net/2021/01/22/K97nwkEPAytuXCx.png)

## mkdir

### 1.如何在一个父级目录都没有的目录下创建子目录

`mkdir -p  父目录/子目录`

参数详解:

- `-p` 选项可以创建一个路径上所有不存在的目录

![image-20210122165847194](https://i.loli.net/2021/01/22/5CW8ysquGVkOJHP.png)

## cp

### 1.如何在拷贝文件后仍保留源文件的权限、时间戳等

`cp -p file1 file2`

![image-20210122172105580](https://i.loli.net/2021/01/22/8rOGlnaN1DYz9b6.png)

### 2.拷贝文件如果目标存在会怎样?

答: 会提示是否覆盖 y是n否

![image-20210122172233043](https://i.loli.net/2021/01/22/29pcYFAbBf8IEtL.png)

## tail&head

### 1.如何只显示文件最后10行

`tail  filename`

![image-20210122173415267](https://i.loli.net/2021/01/22/kFZxzX2jG5JW3w4.png)

![image-20210122173435037](https://i.loli.net/2021/01/22/yEmiRZ3gtkNBO26.png)

### 2.如何之查看指定行数的内容？

`tail -n 数字 fliename`

![image-20210122173613261](https://i.loli.net/2021/01/22/UzyPTlLXHw4pO7B.png)

### 3.如何实时滚动查看文件?

`tail -f filename`

![image-20210122173919054](https://i.loli.net/2021/01/22/iTEpGwWXbvrClLM.png)

参数详解:

- -f 如果有新行添加到文件尾部，它会继续输出新的行
- -F 当文件删除后又重新创建  依然可以

## df

- 使用 `df -h` 选项可以以更符合阅读习惯的方式显示磁盘使用量。

![image-20210122174731263](https://i.loli.net/2021/01/22/kDGKnfv3OyYMrbs.png)

- 使用 `df -T` 选项显示文件系统类型。

![image-20210122174743793](https://i.loli.net/2021/01/22/ALyxWwJc8MvimHC.png)

## mount

### 1.如何挂载目录?

- 先创建目标目录
  - 方式一:使用`mount 文件系统目录 目标目录`
  - 方式二:添加到/etc/fstab文件中

## less

### 1.在不加载整个文件的前提下显示文件内容(查看大兴日志用)

`less  filename`

Ctrl+F:向后翻页

Ctrl+B:向前翻页

## find

### 1.如何查找指定文件名的文件(不区分大小写)

> 在我们接手上一个公司的同事的工作的时候，自己不知道某个服务或者是代码在什么目录中，则可以试试这个方法，就可以减少麻烦别人的次数，从而自给自足。

`find -iname "MyProgram.c"`

参数详解:

```
-iname<范本样式>：此参数的效果和指定“-name”参数类似，但忽略字符大小写的差别；
```

![image-20210125140026859](https://i.loli.net/2021/01/25/8zg6lb1iN9aqcFE.png)

### 2.将30天前的.log文件移动到old目录中

`find . -type f -mtime +30 -name "*.log" -exec cp {} old \;`

注意:old目录需存在

参数详解:

```txt
-type<文件类型>：只寻找符合指定的文件类型的文件；
-mtime<24小时数>：查找在指定时间曾被更改过的文件或目录，单位以24小时计算；
-name<范本样式>：指定字符串作为寻找文件或目录的范本样式；
-exec<执行指令>：假设find指令的回传值为True，就执行该指令；
	{} 用于与-exec选项结合使用来匹配所有文件，然后会被替换为相应的文件名。
```

### 3.查找某个目录下的所有空文件

`find pathname -empty`

参数详解:

```
-empty：寻找文件大小为0 Byte的文件，或目录下没有任何子目录或文件的空目录；
```

### 4.在 /usr 目录下找出大小超过 10MB 的文件

`find /usr -type f -size +10240k` 

ps:另外，du 命令也可以做类似的事情

[查找Linux系统中的占用磁盘空间最大的前10个文件或文件夹](https://my.oschina.net/huxuanhui/blog/58119)

参数详解:

```
-size<文件大小>：查找符合指定的文件大小的文件；
文件大小单元：
b —— 块（512字节）
c —— 字节
w —— 字（2字节）
k —— 千字节
M —— 兆字节
G —— 吉字节
```

### 5.在 /var 目录下找出 90 天之内未被访问过的文件？

`find /var \! -atime -90`

参数详解:

```
-atime<24小时数>：查找在指定时间曾被存取过的文件或目录，单位以24小时计算；
\!  取反
```

### 6.找出当前目录下所有.txt文件并以“File:文件名”的形式打印出来

`find . -type f -name "*.txt" -exec printf "File: %s\n" {} \;`

参数详解:

```
printf "File: %s\n" -- 格式化输出  
%s  字符串    
\n  换行
```

![image-20210125144836751](https://i.loli.net/2021/01/25/O7TSp1k4mBeVuAg.png)