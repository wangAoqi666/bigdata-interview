package com.czxy;

import	java.text.SimpleDateFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Date;


/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/7
 */
public class Test01 {
    private static final String HDFS_PATH="hdfs://192.168.100.109:8020";
    Configuration configuration = null;
    FileSystem fs = null;

    /**
     * 实例化配置对象
     * 实例化文件系统对象
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        fs = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
        System.out.println("准备进行HDFS操作");
    }

    /**
     * 创建
     * @throws Exception
     */
    @Test
    public  void mkdir() throws Exception {
        boolean mkdirs = fs.mkdirs(new Path("/dir01"));
        System.out.println(mkdirs?"创建文件夹成功":"创建文件夹失败");
    }

    /**
     * 获取指定路径所有文件
     */
    @Test
    public void listStatus() throws Exception{
        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println(fileStatus.getPath().toString());
        }
    }

    /**
     * 重命名
     */
    @Test
    public void rename() throws IOException {
        boolean result = fs.rename(new Path("/hdfs"), new Path("/hadoop"));
        System.out.println(result ? "重命名成功":"重命名失败!");
    }

    /**
     * 获取文件日期
     */
    @Test
    public void getTime() throws IOException {
        FileStatus fileStatus = fs.getFileStatus(new Path("/install.log"));
        long modificationTime = fileStatus.getModificationTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(modificationTime)));
    }

    /**
     * 删除文件
     */
    @Test
    public void deleteFile() throws IOException {
        boolean result = fs.delete(new Path("/install.log"), false);
        System.out.println(result);
    }

    /**
     * 删除文件
     */
    @Test
    public void addFile() throws IOException {
        FSDataOutputStream fsDataOutputStream = fs.create(new Path("/install.log"));
        fsDataOutputStream.writeUTF("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        fsDataOutputStream.flush();
        fsDataOutputStream.close();
    }

    /**
     * 上传文件到HDFS
     */
    @Test
    public void putFileToHDFS() throws IOException {
        fs.copyFromLocalFile(new Path("D:\\JetbrainsCrack-2.6.2.jar"), new Path("/aaaa"));
    }

    /**
     * 检查目录是否存在
     */
    @Test
    public void check() throws IOException {
        boolean result = fs.exists(new Path("/aaaa"));
        System.out.println(result?"已存在":"不存在");
    }




    /**
     * 使用完后实例对象设空
     */
    @After
    public void finish() {
        configuration = null;
        fs = null;
        System.out.println("操作HDFS完成!");
    }

}
