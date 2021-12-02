package com.imooc.zhangxiaoxi.guava;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 不可变集合用法
 */
public class ImmutableTest {

    public static void test(List<Integer> list) {
        list.remove(0);
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();

        list.add(1);
        list.add(2);
        list.add(3);

        List<Integer> newList =
                Collections.unmodifiableList(list);

        test(newList);

        System.out.println(newList);
    }

    public void immutable() {
        List<Integer> list = new ArrayList<Integer>();

        list.add(1);
        list.add(2);
        list.add(3);

        /**
         * 构造不可变集合对象三种方式
         */
        // 通过已经存在的集合创建
        ImmutableSet.copyOf(list);

        // 通过初始值，直接创建不可变集合
        ImmutableSet immutableSet =
                ImmutableSet.of(1, 2, 3);

        // 以builder方式创建
        ImmutableSet.builder()
                .add(1)
                .addAll(Sets.newHashSet(2, 3))
                .add(4)
                .build();

    }

}
