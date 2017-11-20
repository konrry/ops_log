package test;

import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Created by Administrator on 2016/8/9.
 */
public class JedisDemo {


    private static Jedis jedis = new Jedis("10.112.4.95", 10079);
    public static void main(String[] args) {
        
        String uuId = UUID.randomUUID().toString();
        String status = jedis.set(uuId, "123123123");
        System.out.println("status: " + status);
        String value = jedis.get(uuId);
        System.out.println("value: " + value);

    }


}
