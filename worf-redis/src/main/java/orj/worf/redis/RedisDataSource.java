package orj.worf.redis;

import java.util.Map;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * redis datasource
 * @author linruzhou 2015
 * @Reversion V1.0.0
 */
public interface RedisDataSource {

	/**
	 * get a ShardedJedis connection from ShardedJedisPool
	 * @return ShardedJedis object
	 * @author linruzhou
	 */
	public ShardedJedis getShardedJedis();
	
	/**
	 * return a ShardedJedis connection to ShardedJedisPool
	 * @param ShardedJedis
	 * @author linruzhou
	 */
	public void returnShardedJedis(ShardedJedis jedis);
	
	/**
	 * get ShardedJedis pool from jedisPoolMap
	 * @param jedisPoolMap
	 * @return
	 */
	public ShardedJedisPool getJedisPool(Map<String, ShardedJedisPool> jedisPoolMap);
}
