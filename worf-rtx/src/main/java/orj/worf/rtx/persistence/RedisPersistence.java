package orj.worf.rtx.persistence;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import orj.worf.redis.annotation.RedisDS;
import orj.worf.redis.constant.RedisDSEnum;
import orj.worf.redis.impl.RedisClientTemplateImpl;
import orj.worf.rtx.constant.TransactionConstant;
import orj.worf.rtx.model.Transaction;
import orj.worf.rtx.serializer.JdkSerializationSerializer;
import orj.worf.rtx.serializer.ObjectSerializer;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

@RedisDS(RedisDSEnum.ACTIVITY_DATA_SOURCE)
public class RedisPersistence implements Persistence {

	private static final String RTX_ROOT_SERVER_NAME = "rtx_root_server_name_list";
	
	public static final String RTX_PREFIX = "rtx_";
	
	@Autowired
	private RedisClientTemplateImpl redisClient;
	
	private String serverName = "";
	
	private ObjectSerializer<Transaction> serializer = new JdkSerializationSerializer<Transaction>();
	
	@PostConstruct
	public void init() {
		serverName = RTX_PREFIX + TransactionConstant.SERVER_NAME;  //项目路径
		redisClient.sadd(RTX_ROOT_SERVER_NAME, serverName);  //各项目的统一记录
	}
	
	@Override
	public void addTrans(Transaction transaction) {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = redisClient.getshardedJedisUnReturn();
			ShardedJedisPipeline pipeline = shardedJedis.pipelined();
			pipeline.zadd(serverName, transaction.getEndTime(), transaction.getId());
			pipeline.set((serverName + transaction.getId()).getBytes(), serializer.serialize(transaction));
			pipeline.sync();
		} finally {
			if(shardedJedis != null) {
				redisClient.returnShardedJedis(shardedJedis);
			}
		}
//		redisClient.zadd(serverName, transaction.getEndTime(), transaction.getId()); //事务超时记录
//		redisClient.setVal((serverName + transaction.getId()).getBytes(), serializer.serialize(transaction));  //事务缓存
	}

	@Override
	public void deleteTrans(Transaction transaction) {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = redisClient.getshardedJedisUnReturn();
			ShardedJedisPipeline pipeline = shardedJedis.pipelined();
			pipeline.del((serverName + transaction.getId()).getBytes());
			pipeline.zrem(serverName, transaction.getId());
			pipeline.sync();
		} finally {
			if(shardedJedis != null) {
				redisClient.returnShardedJedis(shardedJedis);
			}
		}
//		redisClient.delVal((serverName + transaction.getId()).getBytes());  //删除事务
//		redisClient.zrem(serverName, transaction.getId());  //在超时set里面删除，
	}

	@Override
	public void updateTrans(Transaction transaction) {
		redisClient.setVal((serverName + transaction.getId()).getBytes(), serializer.serialize(transaction));  //更新事务
	}

	@Override
	public Set<String> findAllUnCommit(long time) {
		return redisClient.zrangeByScore(serverName, 0, time); 
	}

	@Override
	public Transaction queryTrans(String id) {
		Transaction transaction = null;
		byte[] rtxByte = redisClient.getVal((serverName + id).getBytes());
		if(rtxByte != null && rtxByte.length > 0) {
			transaction = serializer.deserialize(rtxByte);
		}
		return transaction;
	}

}
