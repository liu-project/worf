/*package orj.worf.mybatis.thread;

import java.io.UnsupportedEncodingException;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.Configuration;

import orj.worf.datasource.RJSCipher;
import orj.worf.mybatis.util.SqlHelper;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;

*//**
 * 防篡改数据线程
 * @author LiuZhenghua
 * 2016年12月4日 下午5:50:26
 *//*
public class VerifyThread implements Runnable {

	private BoundSql boundSql;

	private Configuration configuration;
	
	private Producer producer;
	
	private String topic;
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(RJSCipher.decryptData("Sw9HjYyh64g6ytrrkfhq8rkWFGhbpbdBx%2BMqe66Rc4C7%2BPgVjXynbvNgekaVO8aRPa7XZKhE8Xi%2BX%2FE9ma4dZdzwBRwntG0YCDlSjub61iWfuVJXnqzFQ9%2FbSabpJRK7SvIz%2Bg%2BRH7M%3D"));
		System.out.println(RJSCipher.encryptData("jdbc:mysql://172.16.2.6:3306/rjsverifydb?autoReconnect=true&characterEncoding=utf8&allowMultiQueries=true"));
	}

	@Override
	public void run() {
		String sql = SqlHelper.getSql(configuration, boundSql);
		Message message = new Message(topic, "tag_verify_member_money_log", sql.getBytes());
		message.setKey("");
		producer.send(message);
	}
	
	public VerifyThread(BoundSql boundSql, Configuration configuration, Producer producer, String topic) {
		super();
		this.setBoundSql(boundSql);
		this.setConfiguration(configuration);
		this.setProducer(producer);
		this.setTopic(topic);
	}

	public BoundSql getBoundSql() {
		return boundSql;
	}

	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
*/