package orj.worf.rocketmq.bean;

import java.util.List;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

/**
 * MQ消费者，同一个topic，同个consumer支持一个MessageListener。此bean用于对代码里这些MessageListener进行调度管理
 * @author LiuZhenghua
 * 2016年12月2日 下午1:36:49
 */
public class MultiConsumerBean implements MessageListener {
	
	private List<MessageListenerWrapper> listeners;

	@Override
	public Action consume(Message message, ConsumeContext context) {
		for (MessageListenerWrapper listener : getListeners()) {
			if (listener.match(message.getTag())) {
				return listener.consume(message, context);
			}
		}
		return Action.ReconsumeLater;
	}

	public List<MessageListenerWrapper> getListeners() {
		return listeners;
	}

	public void setListeners(List<MessageListenerWrapper> listeners) {
		this.listeners = listeners;
	}
}
