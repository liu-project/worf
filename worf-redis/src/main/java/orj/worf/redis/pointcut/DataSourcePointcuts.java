package orj.worf.redis.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class DataSourcePointcuts {

//    @Pointcut("@within(orj.worf.redis.annotation.RedisDS)")
    @Pointcut("@within(orj.worf.redis.annotation.RedisDS) || @annotation(orj.worf.redis.annotation.RedisDS)") 
    public void dsMarkPointcut() {}
}