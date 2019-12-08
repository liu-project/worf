package orj.worf.rocketmq.test;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;

import orj.worf.rocketmq.annotation.MQOrderProducer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;

public class OrderProducerService {
	
	@MQOrderProducer
	private OrderProducer producer;

	@Value("${mq.shardingTopic}")
	private String topic;
	
	public SendResult sendMessage(String text) {
		String tagName = "tag_test3";
		String shardKey = tagName;
		Message message = new Message(topic, tagName, text.getBytes());
		message.setKey("order_id_" + RandomUtils.nextInt(10000, 99999));
		SendResult sendResult = producer.send(message, shardKey);
		System.out.println(sendResult);
		return sendResult;
	}
	
	public SendResult sendMessage2(String text) {
		String tagName = "tag_test4";
		String shardKey = tagName;
		Message message = new Message(topic, tagName, text.getBytes());
		message.setKey("order_id_" + RandomUtils.nextInt(10000, 99999));
		SendResult sendResult = producer.send(message, shardKey);
		System.out.println(sendResult);
		return sendResult;
	}
}
