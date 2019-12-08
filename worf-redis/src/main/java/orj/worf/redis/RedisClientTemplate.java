package orj.worf.redis;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;



/**
 * redis client template
 * @author linruzhou 2015
 * @Reversion V1.0.0
 */
public interface RedisClientTemplate {

	/**
	 * add array value into list 'key' from the head
	 * @param key
	 * @param value
	 * @return number of the array value
	 * @author linruzhou
	 */
	public Long lpush(String key, String... value);
	
	/**
	 * delete string value from the end of list 'key'
	 * @param key
	 * @return deleted value
	 * @author linruzhou
	 */
	public String rpop(String key);
	
	/**
	 * delete string value from the head of list 'key'
	 * @param key
	 * @return deleted value
	 * @author linruzhou
	 */
	public String lpop(String key);

	/**
	 * delete string value from the end of list 'key', 
	 * when no element exist,block until the timeout or at least one element exist,
	 * when set the timeout 0,it means no time limit. 
	 * @param timeout
	 * @param key
	 * @return result list
	 * @author linruzhou
	 */
	public List<String> brpop(int timeout, String key);
	

	
	public Long rpush(String key, String... value) ;

	
	public List<String> lrange(String key, long start, long end) ;
	
	
    /**
     * 保存值
     * 
     * @param key
     * @param value
     *
     */
    public void setVal(String key,String value);
    
    
    /**
     * 保存值
     * 
     * @param key
     * @param value
     *
     */
    public void hsetVal(String key,String field,String value);
    
    /**
     * 设置+过期。操作是原子的。
     * 
     * @param key
     * @param value
     * @param seconds
     *
     */
    public void setValEx(String key,String value,int seconds);
   /**
    *  获得值
    * 
    * @param key
    * @return
    *
    */
    public  String getVal(String key);
    
    /**
     *  获得值
     * 
     * @param key
     * @return
     *
     */
     public  String hgetVal(String key,String field);
     
     public  Boolean exists(String key);
     
     public  Boolean hexists(String key,String field);
    /**
     * 删除
     * 
     * @param key
     * @return
     *
     */
    public String delVal(String key);
    
    
    public String scriptLoad(String script);
    
    
    public Object eval(String script ,int keyCount, String... params);
    
    public Long expire(String key,int seconds);
    
    /**
     * 查询key的过期时间
     * @author LiuZhenghua
     * 2018年7月12日 下午2:41:15
     */
    public Long ttl(String key);
    /**
     * 从key中删除与str匹配的数据，count为0说明全部删除，1：从头开始删除一个相同的数据,2:删除两个，-1：从尾开始删除一个相同的数据
     * @param key
     * @param count
     * @param str
     * @return
     * @author linruzhou
     */
    public Long lrem(String key, long count, String str);
    
    /**
     * 将str插入key对应的队列，pivot之前位置
     * @param key
     * @param pivot
     * @param str
     * @return
     * @author linruzhou
     */
    public Long linsert(String key, String pivot, String str);
    
    /**
     * 返回list中index位置的数据
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key, long index);
    
    public Long hdel(String key, String... fields) ;
    
    public Long hlen(String key) ;
    
    public Set<String> hkeys(String key) ;

    public List<String> hvals(String key) ;

    public Map<String, String> hgetAll(String key) ;
    
    public String hmset(String key, Map<String, String> hash) ;

    public List<String> hmget(String key, String... fields);

    public Long setValNx(String key, String value);
    
    /**
     * 获取shardedjedis中所有jedis节点与 pattern 匹配的key
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public Set<String> keys(String pattern);
    
    /**
     * 有序队列添加值
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public Long zadd(String key, double score, String member);
    
    /**
     * 删除集合中score 在给定区间的元素
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public Long zremrangeByScore(String key, double start, double end);
    
    /**
     * 返回集合中score 在给定区间的数量
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public Long zcount(String key, double min, double max);
    
    /**
     * 返回集合中元素个数
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public Long zcard(String key);
    
    /**
     * sortedset 分页查询
     * @param pattern
     * @return
     * 
     * @author linruzhou
     */
    public LinkedHashSet<String> zrevrange(String key, long start, long end);
    
    
    /**
     * zrevrangeByScore 根据分数返回结果集
     * @param pattern
     * @return
     * @author linhui
     */
    public LinkedHashSet<String> zrevrangeByScore(String key, long start, long end);
    
    /**
     * 获取jedis，默认取第一个
     * @return
     */
    public Jedis getJedis();
    
    public Long incrBy(String key, long num);
    
    public Long incr(String key);
    
    public Long llen(String key);
    
    public Long sadd(String key, String ...members);
    
    public Long scard(String key);
    
    public Set<String> smember(String key);
    
    public Boolean sismember(String key,String member);
    
    public Long expireAt(String key,int time); 
    
    public Set<String> zrange(String key,long start, long end);
    
    /**
     * 自增1， 并设置当天过期
     * @author LiuZhenghua 2017年7月11日 上午11:44:14
     */
    public Long incrAndExpireAfterToday(String key);
    
    /**
     * @param skey 集合的key
     * @param member 成员
     * @author LiuZhenghua
     * 2017年7月29日 下午5:43:16
     */
    public Long srem(String skey, String member);
    
    /**
     * 删除有序集合中的元素
     * @param key
     * @param member
     * @return
     * @author linruzhou
     */
    public Long zrem(String key, String member);
    
    public Long hsetnx(String key, String field, String value);
    
    public Long hincrBy(String key, String field, Long value);
    
    public String ltrim(String key, Long start, Long end);
    
    public String getSet(String key, String value);
    
    /**
     * 返回有序集中指定成员的排名(正序值)
     * @param key
     * @param member
     * @return inde值
     * @author zhangkailei
     */
    public Long zrank(String key, String member);

    /**
     * 获取本地变量的值，如果没有，会从redis读取，更新至本地变量
     * @author LiuZhenghua
     * 2019年7月25日 下午4:43:37
     */
    public String getLocalVal(String key, Long cahedTimeInMillisecond);
    
    public String setWithNxAndPx(String key, String value, long time);
    
}
