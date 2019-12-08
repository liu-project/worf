package orj.worf.rocketmq.test;

import java.util.Date;

import orj.worf.rocketmq.annotation.MQConsumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

/**
 * 消费者测试类
 * @author LiuZhenghua
 * 2016年11月30日 下午12:59:49
 */
@MQConsumer(topic = "${mq.defaultTopic}", tags = "tag_test1")
public class Consumer1TestService implements MessageListener{
	
	@Override
	public Action consume(Message message, ConsumeContext paramConsumeContext) {
		System.out.println(Thread.currentThread() + " - " + "message(" + new Date().getTime() + "):" + message.getTag() + ":" + new String(message.getBody()));
		return Action.CommitMessage;
	}

}
