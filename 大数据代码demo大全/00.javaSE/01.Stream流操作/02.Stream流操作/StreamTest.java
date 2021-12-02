package com.czxy.stream;

import breeze.linalg.any;
import com.alibaba.fastjson.JSON;
import com.czxy.lambda.cart.CartService;
import com.czxy.lambda.cart.Sku;
import com.czxy.lambda.cart.SkuCategoryEnum;
import kafka.utils.Json;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/11
 * 流的操作
 */
public class StreamTest {
    List<Sku> list;

    /**
     * 初始化
     * 给集合赋值
     */
    @Before
    public void init() {
        list = CartService.getCategoryList();
    }

    /**
     * 使用无状态中间操作  filter方法 进行过滤出  图书类的商品
     * 并使用非短路终端操作 foreach  遍历
     */
    @Test
    public void filterTest() {
        list.stream().filter(sku -> SkuCategoryEnum.BOOKS.equals(sku.getSkuCategory()))
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * map:  将一个对象 转换成另一个对象
     */
    @Test
    public void mapTest() {
        list.stream().map(Sku::getSkuName).forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * flatMap : 将一个对象转换成另一个 流
     */
    @Test
    public void flatMapTest() {
        list.stream()
                .flatMap(sku -> Arrays.stream(sku.getSkuName().split("")))
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * peek:  遍历数据流中的元素
     */
    @Test
    public void peekTest() {
        list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * sorted : 排序
     * 调用Comparator的comparing方法
     * 传入一个参数进行正序排序
     */
    @Test
    public void sorted() {
        list.stream()
                .sorted(Comparator.comparing(Sku::getTotalPrice))
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * 使用 map映射 + distinct 对流中的元素进行去重
     */
    @Test
    public void distinctTest() {
        list.stream()
                .map(Sku::getSkuCategory)
                .distinct()
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * 使用排序 + skip  过滤掉三个最贵的商品
     * skip  跳过
     */
    @Test
    public void skipTest(){
        list.stream()
                .sorted(Comparator.comparing(Sku::getTotalPrice).reversed())
                .skip(3)
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * 打印出 三个最贵的商品
     * limit --  截取
     */
    @Test
    public void limit(){
        list.stream()
                .sorted(Comparator.comparing(Sku::getTotalPrice).reversed())
                .limit(3)
                .forEach(sku -> System.out.println(JSON.toJSONString(sku, true)));
    }

    /**
     * 全部都匹配  结果为true  中间有一个不匹配则 返回false
     * allMatch:  终端操作   短路操作
     *      中间有一个不符合  则中断
     */
    @Test
    public void allMatch(){
        boolean result = list.stream()
                .allMatch(sku -> sku.getTotalPrice() > 10);
        System.out.println(result);
    }

    /**
     * 任意一个匹配 则返回结果是true
     * anyMatch:
     */
    @Test
    public void anyMatch(){
        boolean result = list.stream().anyMatch(sku -> sku.getTotalPrice() > 1_000);
        System.out.println(result);
    }

    /**
     * 所有的都没匹配上  返回结果为 true
     * noneMatch
     */
    @Test
     public void noneMatchTest() {
        boolean result = list.stream().noneMatch(sku -> sku.getTotalPrice() > 10_000 );
        System.out.println(result);
    }

    /**
     * 查找第一个元素
     */
    @Test
    public void findFirstTest(){
        Optional<Sku> first = list.stream()
                .findFirst();
        System.out.println(JSON.toJSONString(first.get(),true));
    }

    /**
     * 查找任意一个对象
     */
    @Test
    public void findAnyTest() {
        Optional<Sku> any1 = list.stream().findAny();
        Optional<Sku> any2 = list.stream().findAny();
        System.out.println(JSON.toJSONString(any1.get(),true));
        System.out.println(JSON.toJSONString(any2.get(),true));
    }

    /**
     * 求出价格最高的 总价
     */
    @Test
    public void maxTest(){
        OptionalDouble max = list.stream()
                .mapToDouble(Sku::getTotalPrice)
                .max();
        if (max.isPresent()){
            System.out.println("最大的单价是: "+max.getAsDouble());
        }
    }

    /**
     * 求出价格最低的 总价
     */
    @Test
    public void minTest(){
        OptionalDouble min = list.stream()
                .mapToDouble(Sku::getTotalPrice)
                .min();
        System.out.println("最小的总价是: "+(min.isPresent()?min.getAsDouble():0.0));
    }

    /**
     * 求出所有数量
     */
    @Test
    public void countTest(){
        long count = list.stream()
                .filter(sku -> sku.getTotalPrice()>100)
                .count();
        System.out.println(count);
    }


}
