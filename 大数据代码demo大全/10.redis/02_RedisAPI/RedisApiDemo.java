import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author WangAoQi
 */
public class RedisApiDemo {
    private JedisPool jedisPool;
    private JedisPoolConfig config;

    @BeforeTest
    public void redisConnectionPool() {
        config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxWaitMillis(3000);
        config.setMaxTotal(50);
        config.setMinIdle(5);
        jedisPool = new JedisPool(config, "node01", 6379);
    }

    @AfterTest
    public void closePool() {
        jedisPool.close();
    }

    /**
     * 添加string类型数据
     */
    @Test
    public void addStr() {
        Jedis resource = jedisPool.getResource();
        //添加
        resource.set("jediskey", "jedisvalue");
        //查询
        String jediskey = resource.get("jediskey");
        System.out.println(jediskey);
        //修改
        resource.set("jediskey", "jedisvalueUpdate");
        //删除
        resource.del("jediskey");
        //实现整型数据的增长操作
        resource.incr("jincr");
        resource.incrBy("jincr", 3);
        String jincr = resource.get("jincr");
        System.out.println(jincr);
        resource.close();
    }

    /**
     * 操作hash类型数据
     */
    @Test
    public void hashOperate() {
        Jedis resource = jedisPool.getResource();
        //添加数据
        resource.hset("jhsetkey", "jmapkey", "jmapvalue");
        resource.hset("jhsetkey", "jmapkey2", "jmapvalue2");
        //获取所有数据
        Map<String, String> jhsetkey = resource.hgetAll("jhsetkey");
        for (String s : jhsetkey.keySet()) {
            System.out.println(s);
        }
        //修改数据
        resource.hset("jhsetkey", "jmapkey2", "jmapvalueupdate2");
        Map<String, String> jhsetkey2 = resource.hgetAll("jhsetkey");
        for (String s : jhsetkey2.keySet()) {
            System.out.println("修改数据打印" + s);
        }
        //删除数据
        resource.del("jhsetkey");

        Set<String> jhsetkey1 = resource.keys("jhsetkey");
        for (String result : jhsetkey1) {
            System.out.println(result);
        }
    }

    /**
     * 操作list类型的数据
     */
    @Test
    public void listOperate() {
        Jedis resource = jedisPool.getResource();
        //从左边插入元素
        resource.lpush("listkey", "listvalue1", "listvalue1", "listvalue2");

        //从右边移除元素
        resource.rpop("listkey");
        //获取所有值
        List<String> listkey = resource.lrange("listkey", 0, -1);
        for (String s : listkey) {
            System.out.println(s);
        }
        resource.close();
    }

    /**
     * set类型数据操作
     */
    @Test
    public void setOperate() {
        Jedis resource = jedisPool.getResource();
        //添加数据
        resource.sadd("setkey", "setvalue1", "setvalue1", "setvalue2", "setvalue3");
        //查询数据
        Set<String> setkey = resource.smembers("setkey");
        for (String s : setkey) {
            System.out.println(s);
        }
        //移除掉一个数据
        resource.srem("setkey", "setvalue3");
        resource.close();
    }


}
