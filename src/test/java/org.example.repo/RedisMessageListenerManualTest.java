package org.example.repo;

import org.example.SpringRedisApplication;
import org.example.config.RedisConfig;
import org.example.queue.RedisMessagePublisher;
import org.example.queue.RedisMessageSubscriber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServerBuilder;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringRedisApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = RedisConfig.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RedisMessageListenerManualTest {
    private static redis.embedded.RedisServer redisServer;

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @BeforeClass
    public static void startRedisServer() {
        redisServer = new RedisServerBuilder().port(6379).setting("maxmemory 256M").build();
        redisServer.start();
    }

    @AfterClass
    public static void stopRedisServer() {
        redisServer.stop();
    }

    @Test
    public void testOnMessage() throws Exception{
        String message = "Message " + UUID.randomUUID();
        redisMessagePublisher.publish(message);
        Thread.sleep(1000);
        assertTrue(RedisMessageSubscriber.messageList.get(0).contains(message));
    }

}
