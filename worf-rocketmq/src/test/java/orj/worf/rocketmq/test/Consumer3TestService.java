package orj.worf.rocketmq.test;

import java.util.Date;

import orj.worf.rocketmq.annotation.MQOrderConsumer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;

@MQOrderConsumer(tags = "tag_test3", topic = "${mq.shardingTopic}", consumerId = "${mq.shardingConsumerId}")
public class Consumer3TestService implements MessageOrderListener {

	@Override
	public OrderAction consume(Message message, ConsumeOrderContext context) {
		System.out.println(Thread.currentThread() + " - " + "order message(" + new Date().getTime() + "):" + message.getTag() + ":" + new String(message.getBody()));
		return OrderAction.Success;
	}

}
