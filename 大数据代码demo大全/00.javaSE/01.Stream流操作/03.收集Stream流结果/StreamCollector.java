package com.czxy.stream;

import com.alibaba.fastjson.JSON;
import com.czxy.lambda.cart.CartService;
import com.czxy.lambda.cart.Sku;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/11
 */
public class StreamCollector {
    /**
     * 收集流结果  到 list集合
     */
    @Test
    public void toListTest(){
        List<Sku> list = CartService.getCategoryList();
        List<Sku> skuList = list.stream()
                .filter(sku -> sku.getTotalPrice() > 100)
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(skuList,true));
    }

    /**
     * 根据特定的字段进行分组 收集
     */
    @Test
    public void groupTest(){
        List<Sku> list = CartService.getCategoryList();
        Map<Enum, List<Sku>> map = list.stream().collect(Collectors.groupingBy(Sku::getSkuCategory));
        System.out.println(JSON.toJSONString(map,true));
    }

    /**
     * 根据谓词函数进行分区
     */
    @Test
    public void partitionTest() {
        List<Sku> list = CartService.getCategoryList();
        Map<Boolean, List<Sku>> map = list.stream()
                .collect(Collectors.partitioningBy(sku -> sku.getTotalPrice() > 100));
        System.out.println(JSON.toJSONString(map,true));
    }


}
