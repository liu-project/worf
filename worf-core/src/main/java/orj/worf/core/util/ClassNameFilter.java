package orj.worf.core.util;

/**
 * 扫描包的时候对类过滤
 * @author LiuZhenghua
 * 2016年11月30日 下午2:12:37
 */
public interface ClassNameFilter {

	public boolean accept(Class<?> clzz);
}
