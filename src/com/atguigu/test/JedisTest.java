package com.atguigu.test;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.43.129",6379);
        System.out.println(jedis.ping());
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            if (jedis.type(key).equals("string")){
                System.out.println(jedis.get(key));
            }

            if (jedis.type(key).equals("set")){
                Set<String> smembers = jedis.smembers(key);
                for (String smember : smembers) {
                    System.out.println(smember);
                }
            }

            if (jedis.type(key).equals("list")){
                List<String> lrange = jedis.lrange(key, 0, -1);
                for (String s : lrange) {
                    System.out.println(s);
                }
            }
        }
    }

}
