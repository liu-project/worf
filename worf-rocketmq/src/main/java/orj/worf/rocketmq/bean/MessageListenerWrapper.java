package orj.worf.rocketmq.bean;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

public class MessageListenerWrapper implements MessageListener {
	
	private String tags;						//二级分类
	
	private MessageListener messageListener;	//消费者

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
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
	public Action consume(Message message, ConsumeContext context) {
		if (messageListener != null) {
			return messageListener.consume(message, context);
		}
		return Action.ReconsumeLater;
	}
}
