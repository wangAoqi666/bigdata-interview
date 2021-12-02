package com.czxy.mapreduce.pachong;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2020/1/2
 */
@Data
public class Result {
    private Integer  resultCount;
    private Boolean  needCount;
    private Integer  start;
    private Integer  pageSize;
    private List<User> resultList = new ArrayList<>();
    private Integer  currentPageNo;
}
