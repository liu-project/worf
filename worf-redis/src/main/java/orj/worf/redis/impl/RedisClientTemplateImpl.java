package orj.worf.redis.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.redis.RedisClientTemplate;
import orj.worf.redis.RedisDataSource;
import orj.worf.redis.util.TimedValue;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;


/**
 * redis template
 * @author linruzhou 2015年7月23日
 * @Reversion V1.0.0
 */
public class RedisClientTemplateImpl implements RedisClientTemplate{
	
	protected static final Logger logger = LoggerFactory.getLogger(RedisClientTemplateImpl.class);
	private static final Map<String, TimedValue> cachedRedis = new HashMap<String, TimedValue>();

	public Long lpush(String key, String... value) {
		Long result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			result = shardedJedis.lpush(key, value);	
		} catch (Exception e) {
			logger.error("method lpush occur error,key:" + key + ",value:" + value, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	public Long rpush(String key, String... value) {
		Long result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			result = shardedJedis.rpush(key, value);	
		} catch (Exception e) {
			logger.error("method rpush occur error,key:" + key + ",value:" + value, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	public String rpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			 result = shardedJedis.rpop(key);
		} catch (Exception e) {
			logger.error("method rpop occur error,key:" + key, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	@Override
	public String lpop(String key) {
		String result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			 result = shardedJedis.lpop(key);
		} catch (Exception e) {
			logger.error("method lpop occur error,key:" + key, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			 result = shardedJedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.error("method lrange occur error,key:" + key, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	public String lindex(String key, long index) {
		String result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			 result = shardedJedis.lindex(key, index);
		} catch (Exception e) {
			logger.error("method lindex occur error,key:" + key, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	public List<String> brpop(int timeout, String key) {
		List<String> result = null;
		ShardedJedis shardedJedis = dataSource.getShardedJedis();
		if(shardedJedis == null)
			return result;
		try {
			result = shardedJedis.brpop(timeout,key);
		} catch (Exception e) {
			logger.error("method brpop occur error,key:" + key, e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
    /**
     * 保存值
     * 
     * @param key
     * @param value
     *
     */
    public void setVal(String key,String value){
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+key+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        
    }
    
    /**
     * 保存值+设置过期时间+NX<br>
     * NX:存在则不插入
     * @param key
     * @param time : 毫秒
     * @param value
     *
     */
    @Override
    public String setWithNxAndPx(String key, String value, long time){
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String result = null;
        try {
        	result = shardedJedis.set(key, value, "NX", "PX", time);
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+key+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    /**
     * 保存值+设置过期时间+XX<br>
     * XX：存在则插入
     * @param key
     * @param time : 毫秒
     * @param value
     *
     */
    public String setWithXxAndPx(String key, String value, long time){
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String result = null;
        try {
        	result = shardedJedis.set(key, value, "XX", "PX", time);
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+key+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    /**
     * 保存值
     * 
     * @param key
     * @param value
     *
     */
    public void setVal(byte[] key,byte[] value){
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+key+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
    }
    
    /**
     * 自增1， 并设置当天过期
     * @author LiuZhenghua 2017年7月11日 上午11:44:14
     */
    public Long incrAndExpireAfterToday(String key) {
    	Long retVal = null;
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	retVal = shardedJedis.incr(key);
        	Long ttl = shardedJedis.ttl(key);
        	//-1表示无过期， -2表示不存在
        	if (ttl == -1 && ttl != -2) {
        		Calendar calendar = Calendar.getInstance();
        		calendar.set(Calendar.HOUR_OF_DAY, 23);
        		calendar.set(Calendar.MINUTE, 59);
        		calendar.set(Calendar.SECOND, 59);
        		shardedJedis.expireAt(key, calendar.getTimeInMillis() / 1000);
        	}
        } catch (Exception e) {
            logger.error("incrAndExpireAfterToday error !key={}", key, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return retVal;
    }
    
    /**
     * @param skey 集合的key
     * @param member 成员
     * @author LiuZhenghua
     * 2017年7月29日 下午5:43:16
     */
    @Override
    public Long srem(String skey, String member) {
    	Long retVal = null;
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	retVal = shardedJedis.srem(skey, member);
        } catch (Exception e) {
            logger.error("srem error !skey={}, member={}", skey, member, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return retVal;
    }
    
    /**
     * 保存值
     * 
     * @param key
     * @param value
     *
     */
    public void hsetVal(String key,String field,String value){
        ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	shardedJedis.hset(key,field, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hset error!key="+key+",field="+field+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        
    }
    
    /**
     * 设置+过期。操作是原子的。
     * 
     * @param key
     * @param value
     * @param seconds
     *
     */
    public void setValEx(String key,String value,int seconds){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
        try {
        	shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory setValEx error!key="+key+",value="+value, e);
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
    }
   /**
    *  获得值
    * 
    * @param key
    * @return
    *
    */
    public  String getVal(String key){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String value = null;
        try {
            value = shardedJedis.get(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory getVal error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    /**
     *  获得值
     * 
     * @param key
     * @return
     *
     */
     public  byte[] getVal(byte[] key){
     	 ShardedJedis shardedJedis= dataSource.getShardedJedis();
     	 byte[] value = null;
         try {
             value = shardedJedis.get(key);
         } catch (Exception e) {
             logger.error("jedisPoolFactory getVal error!key="+key+",value="+value, e);
             throw e;
         }finally{
         	dataSource.returnShardedJedis(shardedJedis);
         }
         return value;
     }
    
    /**
     *  获得值
     * 
     * @param key
     * @return
     *
     */
     public  String hgetVal(String key,String field){
     	ShardedJedis shardedJedis= dataSource.getShardedJedis();
         String value = null;
         try {
             value = shardedJedis.hget(key,field);
         } catch (Exception e) {
             logger.error("jedisPoolFactory hgetVal error!key="+key+",field="+field+",value="+value, e);
             throw e;
         }finally{
         	dataSource.returnShardedJedis(shardedJedis);
         }
         return value;
     }
     
     public  Boolean exists(String key){
       	ShardedJedis shardedJedis= dataSource.getShardedJedis();
       	Boolean value = false;
           try {
               value = shardedJedis.exists(key);
           } catch (Exception e) {
               logger.error("jedisPoolFactory exists error!key="+key+",value="+value, e);
               throw e;
           }finally{
           	dataSource.returnShardedJedis(shardedJedis);
           }
           return value;
       }
     
     public  Boolean hexists(String key,String field){
      	ShardedJedis shardedJedis= dataSource.getShardedJedis();
      	Boolean value = false;
          try {
              value = shardedJedis.hexists(key, field);
          } catch (Exception e) {
              logger.error("jedisPoolFactory hexists error!key="+key+",field="+field+",value="+value, e);
              throw e;
          }finally{
          	dataSource.returnShardedJedis(shardedJedis);
          }
          return value;
      }
    /**
     * 删除
     * 
     * @param key
     * @return
     *
     */
    public String delVal(String key){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String value = null;
        try {
        	shardedJedis.del(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory delVal error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public String delVal(byte[] key){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String value = null;
        try {
        	shardedJedis.del(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory delVal error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    
    public String scriptLoad(String script){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
        String value = null;
        try {
        	Jedis jedis = shardedJedis.getShard("script");
        	value = jedis.scriptLoad(script);
        } catch (Exception e) {
            logger.error("jedisPoolFactory scriptLoad error!script="+script, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    
    public Object eval(String script ,int keyCount, String... params){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Object value = null;
        try {
        	Jedis jedis = shardedJedis.getShard("script");
        	value = jedis.eval(script, keyCount, params);
        } catch (Exception e) {
            logger.error("jedisPoolFactory eval error!eval="+script, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public Long expire(String key,int seconds){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Long value = null;
        try {
        	value = shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("jedisPoolFactory expire error!key="+key, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
	
    public Long ttl(String key) {
    	Long value = null;
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	try {
    		value = shardedJedis.ttl(key);
    	} catch (Exception e) {
            logger.error("jedisPoolFactory expire error!key="+key, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    /**
     * 从key中删除与str匹配的数据，count为0说明全部删除，1：从头开始删除一个相同的数据,2:删除两个，-1：从尾开始删除一个相同的数据
     * @param key
     * @param count
     * @param str
     * @return
     * @author linruzhou
     */
    public Long lrem(String key, long count, String str){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Long value = null;
        try {
        	value = shardedJedis.lrem(key, count, str);
        } catch (Exception e) {
            logger.error("jedisPoolFactory delVal error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    /**
     * 将str插入key对应的队列，pivot之前位置
     * @param key
     * @param pivot
     * @param str
     * @return
     * @author linruzhou
     */
    public Long linsert(String key, String pivot, String str){
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Long value = null;
        try {
        	value = shardedJedis.linsert(key, LIST_POSITION.BEFORE, pivot, str);
        } catch (Exception e) {
            logger.error("jedisPoolFactory delVal error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public Long hdel(String key, String... fields) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Long value = null;
        try {
        	value = shardedJedis.hdel(key,  fields);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hdel error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public Long hlen(String key) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Long value = null;
        try {
        	value = shardedJedis.hlen(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hlen error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public Set<String> hkeys(String key) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Set<String> value = null;
        try {
        	value = shardedJedis.hkeys(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hkeys error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }

    public List<String> hvals(String key) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	List<String> value = null;
        try {
        	value = shardedJedis.hvals(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hvals error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }

    public Map<String, String> hgetAll(String key) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	Map<String, String> value = null;
        try {
        	value = shardedJedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hgetAll error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
    public String hmset(String key, Map<String, String> hash) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	String value = null;
        try {
        	value = shardedJedis.hmset(key, hash);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hmset error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }

    public List<String> hmget(String key, String... fields) {
    	ShardedJedis shardedJedis= dataSource.getShardedJedis();
    	List<String> value = null;
        try {
        	value = shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hmget error!key="+key+",value="+value, e);
            throw e;
        }finally{
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return value;
    }
    
	/**
     * 设置key的值为value，如果key已经存在返回0，该方法已停用<br>
     * 建议使用{@link orj.worf.redis.impl.RedisClientTemplateImpl#setWithNxAndPx}
     * @param key
     * @param value
     * @return
     * @author linruzhou
     */
    @Deprecated
    public Long setValNx(String key, String value){
    	ShardedJedis shardedJedis = null;
        Long result = 0L;
        try {
        	shardedJedis= dataSource.getShardedJedis();
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+key+",value="+value, e);
            throw e;
        }finally{
            dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }

	@Override
	public Set<String> keys(String pattern) {
    	ShardedJedis shardedJedis = null;
    	Set<String> result = new HashSet<String>();
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	for(Jedis jedis : shardedJedis.getAllShards()) {
        		result.addAll(jedis.keys(pattern));
        	}
        } catch (Exception e) {
            logger.error("jedisPoolFactory set error!key="+pattern, e);
            throw e;
        }finally{
            dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
	}

	
    public Long zadd(String key, double score, String member) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zadd error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long zrem(String key, String member) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zrem(key, member);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zrem error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long zremrangeByScore(String key, double start, double end) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zremrangeByScore error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long zcount(String key, double min, double max) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zcount(key, min, max);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zcount error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long zcard(String key) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zcard(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zcard error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    
    public LinkedHashSet<String> zrevrange(String key, long start, long end) {
    	ShardedJedis shardedJedis = null;
    	LinkedHashSet<String> result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = (LinkedHashSet<String>)shardedJedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zrevrange error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public LinkedHashSet<String> zrevrangeByScore(String key, long start, long end) {
    	ShardedJedis shardedJedis = null;
    	LinkedHashSet<String> result = null;
    	 try {
         	shardedJedis= dataSource.getShardedJedis();
         	result = (LinkedHashSet<String>)shardedJedis.zrevrangeByScore(key, start, end);
         } catch (Exception e) {
             logger.error("jedisPoolFactory zrevrangeByScore error!key="+key , e);
         } finally {
         	dataSource.returnShardedJedis(shardedJedis);
         }
         return result;
    }
    
    public Long incrBy(String key, long num) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.incrBy(key, num);
        } catch (Exception e) {
            logger.error("jedisPoolFactory incrBy error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long incr(String key) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.incr(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory incr error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long llen(String key) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.llen(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory llen error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long sadd(String key, String ...members) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.sadd(key, members);
        } catch (Exception e) {
            logger.error("jedisPoolFactory sadd error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long scard(String key) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.scard(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory scard error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Set<String> smember(String key) {
    	ShardedJedis shardedJedis = null;
    	Set<String> result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.smembers(key);
        } catch (Exception e) {
            logger.error("jedisPoolFactory smember error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long hsetnx(String key, String field, String value) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.hsetnx(key, field, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hsetnx error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Long hincrBy(String key, String field, Long value) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.hincrBy(key, field, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory hincrBy error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public String ltrim(String key, Long start, Long end) {
    	ShardedJedis shardedJedis = null;
    	String result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error("jedisPoolFactory ltrim error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public String getSet(String key, String value) {
    	ShardedJedis shardedJedis = null;
    	String result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            logger.error("jedisPoolFactory ltrim error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Boolean sismember(String key,String member) {
    	ShardedJedis shardedJedis = null;
    	Boolean result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.sismember(key, member);
        } catch (Exception e) {
            logger.error("jedisPoolFactory sismember error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
	
    public Set<String> zrange(String key,long start, long end) {
    	ShardedJedis shardedJedis = null;
    	Set<String> result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zrange error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
    public Set<String> zrangeByScore(String key,double min, double max) {
    	ShardedJedis shardedJedis = null;
    	Set<String> result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("jedisPoolFactory zrangeByScore error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
    }
    
	@Override
	public Long expireAt(String key, int time) {
    	ShardedJedis shardedJedis = null;
    	Long result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	result = shardedJedis.expireAt(key, time);
        } catch (Exception e) {
            logger.error("jedisPoolFactory expireAt error!key="+key , e);
        } finally {
        	dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
	}
    
    
	@Override
	@Deprecated
	public Jedis getJedis() {
    	ShardedJedis shardedJedis = null;
    	Jedis result = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        	for(Jedis jedis : shardedJedis.getAllShards()) {
        		result = jedis;
        		break;
        	}
        } catch (Exception e) {
            logger.error("jedisPoolFactory get jedis error!", e);
            throw e;
        } finally {
            dataSource.returnShardedJedis(shardedJedis);
        }
        return result;
	}
	
	
	public ShardedJedis getshardedJedisUnReturn() {
    	ShardedJedis shardedJedis = null;
        try {
        	shardedJedis= dataSource.getShardedJedis();
        } catch (Exception e) {
            logger.error("jedisPoolFactory get jedis error!", e);
            throw e;
        } 
        return shardedJedis;
	}
	
	public void returnShardedJedis(ShardedJedis shardedJedis) {
		if(shardedJedis != null) {
			dataSource.returnShardedJedis(shardedJedis);
		}
	}
	
	@Override
	public Long zrank(String key, String member) {
		ShardedJedis shardedJedis = null;
		Long result = null;
		try {
			shardedJedis= dataSource.getShardedJedis();
			result = shardedJedis.zrank(key, member);
		} catch (Exception e) {
			logger.error("jedisPoolFactory zrank error!key="+key , e);
		} finally {
			dataSource.returnShardedJedis(shardedJedis);
		}
		return result;
	}
	
	
	private RedisDataSource dataSource;

	public void setDataSource(RedisDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public String getLocalVal(String key, Long cachedInMillisecond) {
		TimedValue timedValue = cachedRedis.get(key);
		long time = System.currentTimeMillis();
		//null的时候再创建变量，避免频繁的垃圾对象产生
		if (timedValue == null) {
			timedValue = new TimedValue();
			cachedRedis.put(key, timedValue);
		}
		if (timedValue.getExpiredTime() < time) {
			timedValue = getLocalValInternal(key, time, cachedInMillisecond);
		}
		return timedValue.getValue();
	}
	
	private synchronized TimedValue getLocalValInternal(String key, Long currentTime, Long cachedInMillisecond) {
		TimedValue timedValue = cachedRedis.get(key);
		if (timedValue.getExpiredTime() < currentTime) {
			timedValue.setValue(getVal(key));
			timedValue.setExpiredTime(currentTime + cachedInMillisecond);
			cachedRedis.put(key, timedValue);
		}
		return timedValue;
	}

		
}
