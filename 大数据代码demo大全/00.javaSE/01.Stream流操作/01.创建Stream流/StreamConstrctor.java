package com.czxy.stream;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author 550894211@qq.com
 * @version v 1.0
 * @date 2019/12/11
 */
public class StreamConstrctor {

    /**
     * 通过 stream的 of 构造方法  创建流
     */
    @Test
    public void streamFromValue(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        stream.forEach(System.out::println);
    }

    /**
     * 通过传入一个数组创建流
     */
    @Test
    public void streamFormArray(){
        int[] array = {1,2,3,4,5};
        IntStream stream = Arrays.stream(array);
        stream.forEach(System.out :: println);
    }

    /**
     * 通过文件创建一个字符串流
     *  通过调用 files的  lines方法  将path.get(路径)下的参数转换为 字符串流
     */
    @Test
    public void streamFormFile() throws IOException {
        Stream<String> lines = Files.lines(Paths.get(
                "D:\\dev\\workspace03\\day20191207\\src\\" +
                "test\\java\\com\\czxy\\stream\\StreamConstrctor.java"));
        lines.forEach(System.out :: println);
    }

    /**
     * 通过函数 生成一个流
     */
    @Test
    public void streamFromFuncation(){
//        Stream<Long> iterate = Stream.iterate(1L, n -> n + 2L);
        Stream<Double> generate = Stream.generate(Math::random);
        generate.limit(100).forEach(System.out::println);
    }
}
