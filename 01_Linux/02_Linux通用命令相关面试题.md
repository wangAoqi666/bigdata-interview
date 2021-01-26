# Linux通用相关命令

[TOC]

## grep

### 1.在文件中查找字符串(不区分大小写)

`grep -i "str" xxx.txt`

参数详解:

```
-i 忽略字符大小写的差别。
```

![image-20210126170900565](https://i.loli.net/2021/01/26/HtliAYSpyWRdBuj.png)

### 2.输出成功匹配的行，以及该行之后的三行：

`grep -A 3 -i "example" xxx.txt` 

参数详解:

```
-A<显示列数> 除了显示符合范本样式的那一行之外，并显示该行之后的内容。
```

![image-20210126171247976](https://i.loli.net/2021/01/26/BvhjIOKf2nM8e7X.png)

### 3.在一个文件夹中递归查询包含指定字符串的文件

`grep -r "apache" *`

参数详解:

```
-R/-r 此参数的效果和指定“-d recurse”参数相同。
```

![image-20210126171524045](https://i.loli.net/2021/01/26/zoXbsLiHxf1vRk8.png)

## sed

### 1.替换当前目录中所有文本中的字符串

未改之前

![image-20210126174143746](https://i.loli.net/2021/01/26/OfYeAPmhucFR7lE.png)

执行命令: `sed -i 's/22/33/' *.txt`

![image-20210126174221593](https://i.loli.net/2021/01/26/ixvhRqMrwYdlWc7.png)

参数详解:

```
-i  直接编辑文本
```

### 2.为非空行添加行号

`sed '/./=' filename.txt | sed 'N; s/\n/ /'`

![image-20210126174514724](https://i.loli.net/2021/01/26/ZOGEjBupPS8Yw7t.png)

### 3.打印指定文件的指定行内容

`sed -n '100p'  filename`

参数详解:

```
-n  指定行
```

![image-20210126175108297](https://i.loli.net/2021/01/26/HImAj2vQpChiUc5.png)

### 4.用 sed 命令永久关闭防火墙

`sed -i.bak 's/SELINUX=enforcing/SELINUX=disabled/' /etc/selinux/config `

![image-20210126175220528](https://i.loli.net/2021/01/26/FT7JkOftDvXVRP6.png)

## awk 

### 1.删除重复行(去重)

`$ awk '!($0 in array) { array[$0]; print}' filename`

![image-20210126175505734](https://i.loli.net/2021/01/26/caRPB17Md69JOpw.png)

![image-20210126175526548](https://i.loli.net/2021/01/26/RnePiQgzZWdskjN.png)

### 2.打印 `/etc/passwd` 中所有包含同样的 uid 和 gid 的行

`awk -F ':' '$3=$4' /etc/passwd`

参数详解:

- **-F fs**  fs指定输入分隔符，fs可以是字符串或正则表达式，如-F:

![image-20210126175741881](https://i.loli.net/2021/01/26/i9vqYTazQ5jrWlL.png)

## vim 

### 1.打开文件并跳到第 10 行

`vim +10 filename`

### 2.打开文件跳到第一个匹配的行

![image-20210126180453775](https://i.loli.net/2021/01/26/YapFHTMfZErqJRd.png)

### 3.以只读模式打开文件

`vim -R /etc/passwd`

## diff

### 1.对比两个文件

`diff  文件1 文件2`

![image-20210126181502249](https://i.loli.net/2021/01/26/PvGph8rSy6nEWaD.png)

```
其中，字母"a"、"d"、"c"分别表示添加、删除及修改操作。而"n1"、"n2"表示在文件1中的行号，"n3"、"n4"表示在文件2中的行号。
其中, 以<开始的行属于文件1，以>开始的行属于文件2。
```

## sort 

### 1.以升序对文件内容排序

`sort filename`

### 2.以降序对文件内容进行排序

`sort -r filename`

### 3.以第三个字段对 `/etc/passwd` 的内容排序：

`sort -t: -k 3n /etc/passwd `

```
-t<分隔字符>：指定排序时所用的栏位分隔字符
-k是指定需要爱排序的栏位
```

## xargs

### 1.将所有文件拷贝到指定目录

`ls *.txt | xargs -n1 -i cp {} /tmp`

参数详解:

```
-n选项多行输出
-i 参数或者-I参数配合{}即可进行文件的操作
```

![image-20210126182220635](https://i.loli.net/2021/01/26/yLwORjbTVZq5PXi.png)

### 2.将系统中所有txt 文件压缩打包

`find / -name *.txt -type f -print | xargs tar -cvzf text.tar.gz`

> 注意:不可在有当前文件类型的目录中执行这个命令,否则会报错

### 3.**把当前目录下所有后缀名为 .txt 的文件的权限修改为 777** 

- 方式一，使用 xargs 命令：`find ./ -type f -name "*.txt" |xargs chmod 777` 。
- 方式二，使用 exec 命令：`find ./ -type f -name "*.txt" -exec chmod 777 {}` 。

![image-20210126182625175](https://i.loli.net/2021/01/26/qkOxvw5UjHhcL3t.png)