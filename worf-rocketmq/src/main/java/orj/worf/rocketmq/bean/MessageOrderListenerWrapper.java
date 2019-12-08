package orj.worf.rocketmq.bean;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;

public class MessageOrderListenerWrapper implements MessageOrderListener {
	
	private String tags;						//二级分类
	
	private MessageOrderListener messageListener;	//消费者

	public MessageOrderListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageOrderListener messageListener) {
		this.messageListener = messageListener;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	/**
	 * 判断此listener是否支持此类消息
	 * @author LiuZhenghua
	 * 2016年12月2日 下午2:04:44
	 */
	public boolean match(String tag) {
		/*if (tags.equals("*") || tags.indexOf(tag) != -1) {
			return true;
		}
		return false;*/
		//以前的匹配方式， tag_props会匹配tag_props[XXXX]的消费者，新的匹配代码在一千万次的情况下是70ms左右，原始匹配方式大概5ms
		int tagIndex = tags.indexOf(tag);
		if (tagIndex != -1) {
			if (tagIndex + tag.length() == tags.length() 
					|| tags.charAt(tagIndex + tag.length()) == ' '
					|| tags.charAt(tagIndex + tag.length()) == '|') {
				return true;
			}
		} else if (tags.equals("*")) {
			return true;
		}
		return false;
	}

	@Override
	public OrderAction consume(Message message, ConsumeOrderContext context) {
		if (messageListener != null) {
			return messageListener.consume(message, context);
		}
		//If there is no message listener of this shard, suspend this shard message after there is a consumer. 
		return OrderAction.Suspend;
	}
}
