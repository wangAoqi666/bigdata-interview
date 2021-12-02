package com.czxy.mapreduce.pachong;
import	java.util.Calendar;

import java.io.*;
import java.util.*;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

public class HttpClientTest {
    //记录本期总销售额
    private Map<String,User> map = new HashMap<>();
    private int count  = 0;

    @Test
    public void test() {
        //创建时间集合
        List<String> dates = generateDateStringCollection(2019, 12);
        //写入数据
        try (OutputStream os = new FileOutputStream(new File("H:\\dev\\datas\\test.csv"))) {
            //遍历集合
            os.write("name,type,value,date\r\n".getBytes());
            for (String date : dates) {
                count++;
                System.out.println(count+"日");
                String response = getResponse(date);
                Success success = JSON.parseObject(response, Success.class);
                List<User> resultList = success.getResult().getResultList();
                for (User user : resultList) {
                    //判断map是否有值  如果有  则追加  没有则添加进去
                    if (map.get(user.getEid()) == null){
                        user.setSumSellAmount(user.getSellAmount());
                        map.put(user.getEid(), user);
                    }else {
                        User user1 = map.get(user.getEid());
                        user1.setSumSellAmount(user1.getSumSellAmount()+user.getSellAmount());
                    }
                }
                //写入到文件中
                for (Map.Entry<String, User> stringUserEntry : map.entrySet()) {
                    User user = stringUserEntry.getValue();
                    os.write((user.getUserName().replaceAll(",", "") + "," + isEmpty(user.getTag()).replaceAll(",", "&") + "," + user.getSellAmount() + "," + date.replaceAll("-", "/") + "\r\n").getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为空
     * @param str
     */
    public String isEmpty(String str){
        if (str == null){
            return "尚未分类";
        }else {
            return str;
        }
    }

    /** pom 依赖:
     <dependency>
     <groupId>org.apache.httpcomponents</groupId>
     <artifactId>httpclient</artifactId>
     <version>4.5.8</version>
     </dependency>
     */
    /**
     * 获取响应信息
     *
     * @return
     */
    public String getResponse(String date) {
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("http://www.huo1818.com/live-api/v1_2_5/goods/streamer-ranking-list?tag=&type=1&granularity=1&startDate=" + date + "&endDate="+ date +"&goodsType=&liveCategoryName=&pageSize=40&pageNo=1");
        //HttpGet request = new HttpGet("http://www.huo1818.com/live-api/v1_2_5/goods/streamer-ranking-list?tag=&type=1&granularity=1&startDate=2019-12-31&endDate=2019-12-31&goodsType=&liveCategoryName=&pageSize=40&pageNo=1");
        request.setHeader("Accept", "application/json, text/plain, */*");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Content-Type", "application/json;charset=utf-8");
        request.setHeader("Cookie", "gr_user_id=2731b971-98b8-46d4-b56b-632496c9924f; grwng_uid=fd268087-dfa7-4491-9b1f-c5d5a1cd4b78; prod_DF_TOKEN=d3ed0cc4f55143b2884493f143b002ff16f59901d47; __tins__20450359=%7B%22sid%22%3A%201577956644022%2C%20%22vd%22%3A%201%2C%20%22expires%22%3A%201577958444022%7D; __51cke__=; __51laig__=1; 8e22d1b16f393571_gr_session_id=c697cdbe-ffa3-425a-9270-adf7e800237d; 8e22d1b16f393571_gr_session_id_c697cdbe-ffa3-425a-9270-adf7e800237d=true; Hm_lvt_a135ef5290d5317a9a4a051b9fee8f92=1577755449,1577852965,1577956644; Hm_lpvt_a135ef5290d5317a9a4a051b9fee8f92=1577956644");
        request.setHeader("Host", "www.huo1818.com");
        request.setHeader("prod_DF_TOKEN", "d3ed0cc4f55143b2884493f143b002ff16f59901d47");
        request.setHeader("Referer", "http://www.huo1818.com/ranking/goods");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.204 Safari/537.36");
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            //4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity, "utf-8");
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }

    /**
     *
     * @param year 年
     * @param month 月
     */
    public List<String> generateDateStringCollection(Integer year,Integer month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= maxDate; i++) {
            if (i<10){
                list.add(year+"-"+month+"-0"+i);
            }else {
                list.add(year+"-"+month+"-"+i);
            }
        }
        return list;
    }

    @Test
    public void test001(){
//        System.out.println(generateDateStringCollection(2019,12));
//        String response = getResponse("2019-12-01");
//        System.out.println(response);
    }
}