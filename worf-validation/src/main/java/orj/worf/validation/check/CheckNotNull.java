package orj.worf.validation.check;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.StringUtils;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.NotNull;
import orj.worf.validation.check.base.AbstractCheck;

/**
 * 非空校验
 * @author LiuZhenghua
 * 2016年11月15日 下午9:21:49
 */
public class CheckNotNull extends AbstractCheck<NotNull> {

	@Override
	protected void checkValue(Object value, Annotation annotation) throws FastRuntimeException {
		if (value != null) {
			if (value instanceof String) {
				String str = (String)value;
				if (StringUtils.isNotBlank(str)) {
					return;
				}
			} else {
				return;
			}
		}
		NotNull notNull = (NotNull)annotation;
		throw new FastRuntimeException(notNull.code() + "", notNull.message());
	}

	

}
