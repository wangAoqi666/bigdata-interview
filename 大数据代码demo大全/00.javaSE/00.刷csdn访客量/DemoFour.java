package show01;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class DemoFour extends Thread {

    //博客链接集
    private static ArrayList<String> urls = new ArrayList<>();

    public static void init() {
        try {

            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("src\\a.txt"))));
            String lin = "";
            while ((lin = bf.readLine()) != null) {
                urls.add(lin);
            }
            bf.close();
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        init();
        try {
            for (int i = 0; i < 60000; ++i) {
                //随机选择博客 [0,11]
                int url = i % urls.size();
                System.out.println(url);
                //选定博客uri
                URI uri = new URI(urls.get(url));
                System.err.println(i + "次运行 | 浏览器:chrome.exe | 博客链接:mmm" + url + " | 时间:" + System.currentTimeMillis());
                //Runtime类在浏览器中打开指定链接
                Runtime.getRuntime().exec("cmd /c start chrome.exe " + uri);
                //等待5秒
                TimeUnit.MILLISECONDS.sleep(5000);

                //关闭浏览器
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
                //等待1秒
                TimeUnit.MILLISECONDS.sleep(1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建线程，开始执行
     *
     * @param args
     */
    public static void main(String[] args) {
        new DemoFour().start();
        new DemoThree().start();
    }

}
