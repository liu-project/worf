package orj.worf.rocketmq.test;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;

import orj.worf.rocketmq.annotation.MQProducer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;

/**
 * 生产者测试类
 * @author LiuZhenghua
 * 2016年11月30日 下午12:59:36
 */
public class ProducerTestService {
	
	@MQProducer
	private Producer producer;
	
	@Value("${mq.defaultTopic}")
	private String topic;
	
	public void testProducer(String text) {
		Message message = new Message(topic, "tag_test1", text.getBytes());
		message.setKey("order_id_" + RandomUtils.nextInt(10000, 99999));
		SendResult sendResult = producer.send(message);
		System.out.println(sendResult);
	}
	
	public void testConsumer2(String text) {
		Message message = new Message(topic, "tag_test2", text.getBytes());
		message.setKey("order_id_" + RandomUtils.nextInt(10000, 99999));
		SendResult sendResult = producer.send(message);
		System.out.println(sendResult);
	}

}
