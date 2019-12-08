package orj.worf.rocketmq.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:worf-rocketmq_test_config.xml"})
public class CommonTest {
	
	@Autowired
	ProducerTestService producerTestService;
	
	@Autowired
	OrderProducerService orderProducerService;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void test() throws InterruptedException, IOException {
		for (int i = 0; i <= 100; i++) {
			producerTestService.testProducer("tag_test1 message number is:" + i);
		}
		
		for (int i = 0; i <= 100; i++) {
			producerTestService.testConsumer2("tag_test2 message number is:" + i);
		}
		
		for (int i = 0; i <= 20; i++) {
			orderProducerService.sendMessage("hello, the message number is:" + i);
		}
		for (int i = 0; i <= 20; i++) {
			orderProducerService.sendMessage2("hello, the message number is:" + i);
		}
		System.in.read();
	}

}
