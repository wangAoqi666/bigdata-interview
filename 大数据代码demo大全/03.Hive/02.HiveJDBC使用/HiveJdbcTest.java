package com.czxy.demo01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcTest {

	public static void main(String[] args) throws Exception {
		try {
			String driverName = "org.apache.hive.jdbc.HiveDriver";
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Connection con = DriverManager.getConnection("jdbc:hive2://node09:10000/telecom", "root", "123456");
		Statement stmt = con.createStatement();
		//----
		 Configuration configuration = new Configuration();
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.100.109:8020"), configuration, "root");
		RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/workspace/01_telecom/data/networkqualityinfo"), true);
		while (files.hasNext()){
			LocatedFileStatus next = files.next();
            String name = next.getPath().getName();
            String yyyy = name.split("\\.")[0].split("-")[0];
            String MM = name.split("\\.")[0].split("-")[1];
            String dd = name.split("\\.")[0].split("-")[2];
            String path = next.getPath().toString();
            System.out.println(path);
            setHIveFromHDFS(path, stmt,yyyy,MM,dd);
        }
	}

	private static void setHIveFromHDFS(String path,Statement stmt,String yyyy,String MM,String dd) throws SQLException {
		execSQL("LOAD DATA inpath '"+path+"' OVERWRITE INTO TABLE networkqualityinfo PARTITION (yyyy='"+yyyy+"',MM='"+MM+"',dd='"+dd+"')",stmt);
	}

	private static void execSQL(String sqlStr,Statement stmt) throws SQLException {
        int i = stmt.executeUpdate(sqlStr);
        System.out.println(i==0?"插入成功!恭喜你!":"失败了");
	}
}
