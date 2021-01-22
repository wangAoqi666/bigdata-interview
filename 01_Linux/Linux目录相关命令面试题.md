# Linux目录相关命令

## cd

`cd -` ：可以在最近工作的连个目录间切换

![image-20210122140318525](https://i.loli.net/2021/01/22/mH8GbMIPXyC1cUn.png)

## ls

1.如何以简单易读的方式显示文件的大小（MB，GB。。。。）

`ls -lh`

list  files

参数详解  ：

- -l 除文件名称外，亦将文件型态、权限、拥有者、文件大小等资讯详细列出

- -h 简单易读的方式显示文件的大小

![image-20210122150853034](https://i.loli.net/2021/01/22/maAg4TPV8dBDnQI.png)

2.如何以修改时间升序列出文件

`ls -ltr`

参数详解  ：

- -r 将文件以相反次序显示(原定依英文字母次序)
- -t 将文件依建立时间之先后次序列出

![image-20210122150957158](https://i.loli.net/2021/01/22/8wiHfoEd9u6KLMZ.png)

3.如何在文件名后面显示文件类型

`ls -F`

参数详解:

-F 在列出的文件名称后加一符号；例如可执行档则加 "*", 目录则加 "/"

![image-20210122155121637](https://i.loli.net/2021/01/22/j7YNpBAuCRM81ZG.png)

## cat 

1.如何快速的将两个文件合并到一起

`cat file1 file2 > file3`

![image-20210122155627415](https://i.loli.net/2021/01/22/K97nwkEPAytuXCx.png)

## mkdir

1.如何在一个父级目录都没有的目录下创建子目录

`mkdir -p  父目录/子目录`

参数详解:

- `-p` 选项可以创建一个路径上所有不存在的目录

![image-20210122165847194](https://i.loli.net/2021/01/22/5CW8ysquGVkOJHP.png)

## cp

1.如何在拷贝文件后仍保留源文件的权限、时间戳等

`cp -p file1 file2`

![image-20210122172105580](https://i.loli.net/2021/01/22/8rOGlnaN1DYz9b6.png)

2.拷贝文件如果目标存在会怎样?

答: 会提示是否覆盖 y是n否

![image-20210122172233043](https://i.loli.net/2021/01/22/29pcYFAbBf8IEtL.png)

## tail&head

1.如何只显示文件最后10行

`tail  filename`

![image-20210122173415267](https://i.loli.net/2021/01/22/kFZxzX2jG5JW3w4.png)

![image-20210122173435037](https://i.loli.net/2021/01/22/yEmiRZ3gtkNBO26.png)

2.如何之查看指定行数的内容？

`tail -n 数字 fliename`

![image-20210122173613261](https://i.loli.net/2021/01/22/UzyPTlLXHw4pO7B.png)

3.如何实时滚动查看文件?

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

1.如何挂载目录?

- 先创建目标目录
  - 方式一:使用`mount 文件系统目录 目标目录`
  - 方式二:添加到/etc/fstab文件中

## less

1.在不加载整个文件的前提下显示文件内容(查看大兴日志用)

`less  filename`

Ctrl+F:向后翻页

Ctrl+B:向前翻页

## find



