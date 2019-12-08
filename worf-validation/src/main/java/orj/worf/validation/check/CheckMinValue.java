package orj.worf.validation.check;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.MinValue;
import orj.worf.validation.annotation.NotNull;
import orj.worf.validation.check.base.AbstractCheck;

/**
 * 最小值
 * @author LiuZhenghua
 * 2016年11月15日 下午9:23:48
 */
public class CheckMinValue extends AbstractCheck<MinValue> {

	@Override
	protected void checkValue(Object value, Annotation annotation) throws FastRuntimeException {
		final MinValue minValue = (MinValue)annotation;
		if (minValue.notnull()) {
			new CheckNotNull().checkValue(value, new NotNull() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return NotNull.class;
				}
				
				@Override
				public String message() {
					return minValue.message();
				}
				
				@Override
				public int code() {
					return minValue.code();
				}
			});
		}
		if (value != null) {
			BigDecimal val = new BigDecimal(value.toString());
			BigDecimal minVal = new BigDecimal(minValue.value());
			if (val.compareTo(minVal) < 0) {
				throw new FastRuntimeException(minValue.code() + "", minValue.message());
			}
		}
	}

}
