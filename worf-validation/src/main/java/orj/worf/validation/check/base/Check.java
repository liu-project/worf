package orj.worf.validation.check.base;

import orj.worf.exception.FastRuntimeException;

/**
 * 所有校验实现类都应该继承此接口
 * @author LiuZhenghua
 * 2016年11月15日 上午11:37:28
 */
public interface Check {

	/**
	 * 校验数据
	 * @param obj
	 * @throws FastRuntimeException
	 */
	void valid(Object obj) throws FastRuntimeException;
}
