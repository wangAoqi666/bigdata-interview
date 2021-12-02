package com.czxy.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.net.URI;
import java.util.Arrays;


/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/10/27
 */
public class HDFSApp_03 {
    public static final String HDFS_PATH = "hdfs://192.168.100.109:8020";
    Configuration configuration = null;
    FileSystem fileSystem = null;

    /**
     * 测试之前
     */
    @Before
    public void setUp() throws Exception {
        System.out.println("----------执行setUP----------");
        configuration = new Configuration();
        //设置副本的个数
        configuration.set("dfs.replication", "3");
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "root");
    }

    /**
     * 创建目录
     */
    @Test
    public void testMkdir() throws IOException {
        fileSystem.mkdirs(new Path("/tmp/ddd"));
    }

    /**
     * 打开文件
     */
    @Test
    public void testOpen() throws IOException {
        FSDataInputStream inputStream = fileSystem.open(new Path("/tmp/install.log"));
        OutputStream outputStream = new FileOutputStream(new File("123.txt"));
        IOUtils.copyBytes(inputStream, outputStream, 1024);
    }

    /**
     * 创建文件
     */
    @Test
    public void testCreate() throws IOException {
        FSDataOutputStream os = fileSystem.create(new Path("/tmp/124.txt"));
        os.writeUTF("123112312312,大会上感到杀敌数a'");
        os.flush();
        os.close();
    }

    /**
     * 文件重命名
     */
    @Test
    public void testRename() throws IOException {
        boolean result = fileSystem.rename(new Path("/tmp/124.txt"), new Path("/tmp/125.txt"));
        System.out.println(result);
    }

    /**
     * 拷贝本地文件到HDFS文件系统
     */
    @Test
    public void CopyFromLocalFile() throws IOException {
        fileSystem.copyFromLocalFile(new Path("G:\\hadoopCentOS\\hadoop002.zip"), new Path("/tmp"));
    }

    /**
     * 带进度条的上传数据到HDFS系统
     */
    @Test
    public void CopyFromLocalAndProgress() throws Exception {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("D:\\bigDataDevelop\\resource\\01linux\\jdk-8u144-linux-x64.tar.gz")));
        FSDataOutputStream outputStream = fileSystem.create(new Path("/tmp/jdk.tar.gz"), new Progressable() {
            @Override
            public void progress() {
                System.out.print(".");
            }
        });
        IOUtils.copyBytes(inputStream, outputStream, 4096);
    }

    /**
     * 从HDFS文件系统中下载文件到本地
     */
    @Test
    public void copyToLocal() throws Exception {
        InputStream inputStream = fileSystem.open(new Path("/tmp/install.log"));
        OutputStream outputStream = new FileOutputStream("a.txt");
        IOUtils.copyBytes(inputStream, outputStream, 2048);
    }

    /**
     * 查看文件下所有的文件及文件夹的详细信息
     */
    @Test
    public void listFiles() throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/tmp"));
        for (FileStatus file : fileStatuses) {
            //判断是否是文件夹
            String isDir = file.isDirectory() ? "文件夹" : "文件";
            //获取权限
            String permission = file.getPermission().toString();
            //获取副本个数
            short replication = file.getReplication();
            //获取绝对路径
            String path = file.getPath().toString();
            //获取文件大小
            long len = file.getLen();
            System.out.println(isDir + "  " + permission + "  " + replication + "   " + len + "   " + path);
        }
    }

    /**
     * 递归查看目录下的所有文件及文件夹
     */
    @Test
    public void listFilesRecursive() throws IOException {
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(new Path("/tmp"), true);
        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus file = locatedFileStatusRemoteIterator.next();
            //判断是否是文件夹
            String isDir = file.isDirectory() ? "文件夹" : "文件";
            //获取权限
            String permission = file.getPermission().toString();
            //获取副本个数
            short replication = file.getReplication();
            //获取绝对路径
            String path = file.getPath().toString();
            //获取文件大小
            long len = file.getLen();
            System.out.println(isDir + "  " + permission + "  " + replication + "   " + len + "   " + path);
        }
    }

    /**
     * 获取文件的块信息
     */
    @Test
    public void getFileBlockLocations() throws IOException {
        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/tmp/jdk.tar.gz"));
        BlockLocation[] fileBlockLocations = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation blockLocation : fileBlockLocations) {
            String[] names = blockLocation.getNames();
            for (String name : names) {
                System.out.println(name + "--" + blockLocation.getOffset() + "--" + blockLocation.getLength() + "--" + Arrays.toString(blockLocation.getHosts()));
            }
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delFile() throws IOException {
        boolean result = fileSystem.delete(new Path("/tmp/jdk.tar.gz"), false);
        System.out.println(result);
    }


    /**
     * 测试之后
     *
     * @throws Exception
     */
    @After
    public void setDown() throws Exception {
        configuration = null;
        fileSystem = null;
        System.out.println("----------执行setDown----------");
    }
}
