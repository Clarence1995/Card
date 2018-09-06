package jedis;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class index {
    Jedis jedis = null;
    @Before
    public void init() {
        jedis = new Jedis("127.0.0.1");
    }

    @Test
    public void testGet() throws InterruptedException {
        String res = jedis.set("jedis-key", "I am a man!");
        Thread.sleep(5000);
        System.out.println(res);
    }

    @Test
    public void testList() {
        System.out.println(jedis.get("a"));
    }

    @Test
    public void test() {
        JSONObject jsonObject = new JSONObject();

    }
}
