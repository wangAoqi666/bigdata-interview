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

## rm

## cp

## mv

## pwd

## tail

## df

## diff

## mount

## find

## less

