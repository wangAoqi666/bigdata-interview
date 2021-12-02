package com.czxy;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.net.URI;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/7
 *
 * 将本地的多个小文件拼接成大文件
 */
public class Test02 {
    //HDFS文件系统的地址
    private static final String HDFS_PATH = "hdfs://192.168.100.109:8020";
    public static void main(String[] args) throws Exception {
        //实例化对象
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI(HDFS_PATH), configuration,"root");
        //创建文件  返回输出流
        FSDataOutputStream os = fs.create(new Path("/aaaa/1234.txt"));
        //获取本地文件系统对象
        LocalFileSystem local = FileSystem.getLocal(configuration);
        FileStatus[] fileStatuses = local.listStatus(new Path("file:/H:\\网易云音乐"));
        for (FileStatus fileStatus : fileStatuses) {
            //获取文件输入流
            System.out.println(fileStatus.getPath());
            FSDataInputStream is = local.open(fileStatus.getPath());
            IOUtils.copyBytes(is, os, 4096);
            os.flush();
            is.close();
        }
        os.close();
    }
}
