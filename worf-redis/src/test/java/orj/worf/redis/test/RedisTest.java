package orj.worf.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import orj.worf.redis.RedisClientTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:worf-redis_config_test.xml"})
public class RedisTest {
	
	@Autowired
	private RedisClientTemplate redisService;

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			redisService.getLocalVal("test", 1000L);
		}
		long end = System.currentTimeMillis();
		System.out.println("time used: " + (end - start));
	}
}
