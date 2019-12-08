package orj.worf.validation.check;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.NotNull;
import orj.worf.validation.annotation.Range;
import orj.worf.validation.check.base.AbstractCheck;

/**
 * 值区间
 * @author LiuZhenghua
 * 2016年11月15日 下午10:03:40
 */
public class CheckRange extends AbstractCheck<Range> {

	@Override
	protected void checkValue(Object value, Annotation annotation) throws FastRuntimeException {
		final Range range = (Range)annotation;
		if (range.notnull()) {
			new CheckNotNull().checkValue(value, new NotNull() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return NotNull.class;
				}
				
				@Override
				public String message() {
					return range.message();
				}
				
				@Override
				public int code() {
					return range.code();
				}
			});
		}
		if (value != null) {
			BigDecimal val = new BigDecimal(value.toString());
			String[] splitArr = range.value().split("-");
			if (splitArr.length >= 1) {
				BigDecimal minVal = new BigDecimal(splitArr[0]);
				if (val.compareTo(minVal) < 0) {
					throw new FastRuntimeException(range.code() + "", range.message());
				}
			}
			if (splitArr.length >= 2) {
				BigDecimal maxVal = new BigDecimal(splitArr[1]);
				if (val.compareTo(maxVal) > 0) {
					throw new FastRuntimeException(range.code() + "", range.message());
				}
			}
		}
	}

}
