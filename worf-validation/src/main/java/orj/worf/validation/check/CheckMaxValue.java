package orj.worf.validation.check;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.MaxValue;
import orj.worf.validation.annotation.NotNull;
import orj.worf.validation.check.base.AbstractCheck;

/**
 * 最大值校验
 * @author LiuZhenghua
 * 2016年11月15日 下午9:38:14
 */
public class CheckMaxValue extends AbstractCheck<MaxValue> {

	@Override
	protected void checkValue(Object value, Annotation annotation) throws FastRuntimeException {
		final MaxValue maxValue = (MaxValue)annotation;
		if (maxValue.notnull()) {
			new CheckNotNull().checkValue(value, new NotNull() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return NotNull.class;
				}
				
				@Override
				public String message() {
					return maxValue.message();
				}
				
				@Override
				public int code() {
					return maxValue.code();
				}
			});
		}
		if (value != null) {
			BigDecimal val = new BigDecimal(value.toString());
			BigDecimal maxVal = new BigDecimal(maxValue.value());
			if (val.compareTo(maxVal) > 0) {
				throw new FastRuntimeException(maxValue.code() + "", maxValue.message());
			}
		}
	}

}
