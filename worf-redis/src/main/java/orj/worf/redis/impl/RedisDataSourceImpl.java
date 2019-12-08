package orj.worf.redis.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.redis.RedisDataSource;
import orj.worf.redis.RedisDataSourceHolder;
import orj.worf.redis.constant.RedisDSEnum;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * redis datasource
 * @author linruzhou 2015
 * @Reversion V1.0.0
 */
public class RedisDataSourceImpl implements RedisDataSource {

	private static final Logger logger = LoggerFactory.getLogger(RedisDataSourceImpl.class);
	
	private static final String DEFAULT_REDIS_DATASOURCE = "defaultRedisDataSource";
	
	private static final String ACTIVITY_REDIS_DATASOURCE = "activityRedisDataSource";
	
	private Map<String, ShardedJedisPool> jedisPoolMap;
	
	private Integer minIdle;

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	@Override
	public ShardedJedis getShardedJedis() {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getJedisPool(jedisPoolMap).getResource();
		} catch (Exception e) {
			logger.error("get jedis client from JedisPool occur error",e);
		}
		return shardedJedis;
	}
	
	/**
	 * 获取当前线程的redis数据源
	 * @param jedisPoolMap
	 * @return
	 */
	@Override
	public ShardedJedisPool getJedisPool(Map<String, ShardedJedisPool> jedisPoolMap) {
		//从ThreadLocal中获取当前的datasource
		String dataSourceString = DEFAULT_REDIS_DATASOURCE;
		if(RedisDataSourceHolder.getDataSource() != null
				&& RedisDSEnum.ACTIVITY_DATA_SOURCE == RedisDataSourceHolder.getDataSource()) {
			dataSourceString = ACTIVITY_REDIS_DATASOURCE;
		}
		
		ShardedJedisPool shardedJedisPool = jedisPoolMap.get(dataSourceString);
		if(shardedJedisPool == null) {
			shardedJedisPool = jedisPoolMap.get(DEFAULT_REDIS_DATASOURCE);
		}
		return shardedJedisPool;
	}

	@Override
	public void returnShardedJedis(ShardedJedis shardedJedis) {
		try {
			if(shardedJedis != null) {
				getJedisPool(jedisPoolMap).returnResource(shardedJedis);
			}
		} catch (Exception e) {
			logger.error("return jedis to the pool occur error",e);
		}
	}

	public void setJedisPoolMap(Map<String, ShardedJedisPool> jedisPoolMap) {
		this.jedisPoolMap = jedisPoolMap;
	}
	
	/**
	 * 初始化连接池
	 * @author LiuZhenghua
	 * 2018年12月20日 上午11:02:35
	 */
	public void init() {
		for (String key : jedisPoolMap.keySet()) {
			ShardedJedisPool jedisPool = jedisPoolMap.get(key);
			ShardedJedis[] jedisList = new ShardedJedis[minIdle];
			for (int i = 0; i < minIdle; i++) {
				jedisList[i] = jedisPool.getResource();
			}
			for (int i = 0; i < minIdle; i++) {
				jedisPool.returnResource(jedisList[i]);
			}
		}
	}
}
