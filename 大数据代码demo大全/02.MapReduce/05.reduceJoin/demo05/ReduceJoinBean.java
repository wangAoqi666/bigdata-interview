package com.czxy.hadoop.mapreduce.demo05;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/23
 */
public class ReduceJoinBean implements Writable {
    private String oid;
    private String oDate;
    private String pid;
    private String pNum;
    private String pName;
    private String oNum;
    private String price;

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(oid+"");
        out.writeUTF(oDate+"");
        out.writeUTF(pid+"");
        out.writeUTF(pNum+"");
        out.writeUTF(pName+"");
        out.writeUTF(oNum+"");
        out.writeUTF(price+"");
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        oid = in.readUTF();
        oDate = in.readUTF();
        pid = in.readUTF();
        pNum = in.readUTF();
        pName = in.readUTF();
        oNum = in.readUTF();
        price = in.readUTF();
    }

    @Override
    public String toString() {
        return "ReduceJoinBean{" +
                "oid='" + oid + '\'' +
                ", oDate='" + oDate + '\'' +
                ", pid='" + pid + '\'' +
                ", pNum='" + pNum + '\'' +
                ", pName='" + pName + '\'' +
                ", oNum='" + oNum + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getoDate() {
        return oDate;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getpNum() {
        return pNum;
    }

    public void setpNum(String pNum) {
        this.pNum = pNum;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getoNum() {
        return oNum;
    }

    public void setoNum(String oNum) {
        this.oNum = oNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ReduceJoinBean() {
    }

    public ReduceJoinBean(String oid, String oDate, String pid, String pNum, String pName, String oNum, String price) {
        this.oid = oid;
        this.oDate = oDate;
        this.pid = pid;
        this.pNum = pNum;
        this.pName = pName;
        this.oNum = oNum;
        this.price = price;
    }
}
