package orj.worf.rocketmq.bean;

import java.util.List;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;

/**
 * MQ消费者，同一个topic，同个consumer支持一个MessageListener。此bean用于对代码里这些MessageOrderListener进行调度管理
 * @author LiuZhenghua
 * 2016年12月2日 下午1:36:49
 */
public class MultiOrderConsumerBean implements MessageOrderListener {
	
	private List<MessageOrderListenerWrapper> listeners;

	@Override
	public OrderAction consume(Message message, ConsumeOrderContext context) {
		for (MessageOrderListenerWrapper listener : getListeners()) {
			if (listener.match(message.getTag())) {
				return listener.consume(message, context);
			}
		}
		return OrderAction.Suspend;
	}

	public List<MessageOrderListenerWrapper> getListeners() {
		return listeners;
	}

	public void setListeners(List<MessageOrderListenerWrapper> listeners) {
		this.listeners = listeners;
	}
}
