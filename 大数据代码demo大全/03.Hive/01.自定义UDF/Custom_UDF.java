package com.czxy;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/11/21
 */
public class Custom_UDF extends UDF {

    public Text evaluate(Text name){
        return new Text(name+" 是大帅哥!");
    }

}
