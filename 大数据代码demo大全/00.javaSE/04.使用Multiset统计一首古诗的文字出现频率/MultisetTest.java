package com.imooc.zhangxiaoxi.guava;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Chars;
import org.junit.Test;

/**
 * 实现：使用Multiset统计一首古诗的文字出现频率
 */
public class MultisetTest {

    private static final String text =
            "《南陵别儿童入京》" +
            "白酒新熟山中归，黄鸡啄黍秋正肥。" +
            "呼童烹鸡酌白酒，儿女嬉笑牵人衣。" +
            "高歌取醉欲自慰，起舞落日争光辉。" +
            "游说万乘苦不早，著鞭跨马涉远道。" +
            "会稽愚妇轻买臣，余亦辞家西入秦。" +
            "仰天大笑出门去，我辈岂是蓬蒿人。";

    @Test
    public void handle() {
        // multiset创建
        Multiset<Character> multiset =
                HashMultiset.create();

        // string 转换成 char 数组
        char[] chars = text.toCharArray();

        // 遍历数组，添加到multiset中
        Chars.asList(chars)
                .stream()
                .forEach(charItem -> {
                    multiset.add(charItem);
                });

        System.out.println
                ("size : " + multiset.size());

        System.out.println
                ("count : " + multiset.count('人'));

    }
}
