package com.czxy.mapreduce.pachong;

import lombok.Data;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2020/1/2
 */
@Data
public class User {
    private String eid;
    private Integer gender;
    private String city;
    private Boolean collectFlag;
    private String tag;
    private Double maxWatchNum;
    private Double liveDays;
    private String kwaiId;
    private String fansNum;
    private String avatar;
    private Double productNum;
    private String userName;
    private Double sellAmount;
    private Double sellCount;
    private Double streamerId;

    private Double sumSellAmount;
}
