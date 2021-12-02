package com.czxy.demo01;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {

    /** pom 依赖:
     <dependency>
     <groupId>org.apache.httpcomponents</groupId>
     <artifactId>httpclient</artifactId>
     <version>4.5.8</version>
     </dependency>
     */
    /**
     * 获取响应信息
     * @return
     */
    public String getResponse() {
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet("http://www.huo1818.com/live-api/v1_2_5/goods/streamer-ranking-list?tag=&type=1&granularity=1&startDate=2020-01-01&endDate=2020-01-01&goodsType=&liveCategoryName=&pageSize=40&pageNo=1");
        request.setHeader("Accept","application/json, text/plain, */*");
        request.setHeader("Accept-Encoding","gzip, deflate");
        request.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        request.setHeader("Connection","keep-alive");
        request.setHeader("Content-Type","application/json;charset=utf-8");
        request.setHeader("Cookie","gr_user_id=2731b971-98b8-46d4-b56b-632496c9924f; grwng_uid=fd268087-dfa7-4491-9b1f-c5d5a1cd4b78; prod_DF_TOKEN=d3ed0cc4f55143b2884493f143b002ff16f59901d47; __tins__20450359=%7B%22sid%22%3A%201577956644022%2C%20%22vd%22%3A%201%2C%20%22expires%22%3A%201577958444022%7D; __51cke__=; __51laig__=1; 8e22d1b16f393571_gr_session_id=c697cdbe-ffa3-425a-9270-adf7e800237d; 8e22d1b16f393571_gr_session_id_c697cdbe-ffa3-425a-9270-adf7e800237d=true; Hm_lvt_a135ef5290d5317a9a4a051b9fee8f92=1577755449,1577852965,1577956644; Hm_lpvt_a135ef5290d5317a9a4a051b9fee8f92=1577956644");
        request.setHeader("Host","www.huo1818.com");
        request.setHeader("prod_DF_TOKEN","d3ed0cc4f55143b2884493f143b002ff16f59901d47");
        request.setHeader("Referer","http://www.huo1818.com/ranking/goods");
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.204 Safari/537.36");
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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
}