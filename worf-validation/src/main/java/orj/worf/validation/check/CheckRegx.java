package orj.worf.validation.check;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import orj.worf.exception.FastRuntimeException;
import orj.worf.validation.annotation.NotNull;
import orj.worf.validation.annotation.Regex;
import orj.worf.validation.check.base.AbstractCheck;

/**
 * 正则校验
 * @author LiuZhenghua
 * 2016年11月15日 下午9:34:06
 */
public class CheckRegx extends AbstractCheck<Regex> {

	@Override
	protected void checkValue(Object value, Annotation annotation) throws FastRuntimeException {
		final Regex regex = (Regex)annotation;
		if (regex.notnull()) {
			new CheckNotNull().checkValue(value, new NotNull() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return NotNull.class;
				}
				
				@Override
				public String message() {
					return regex.message();
				}
				
				@Override
				public int code() {
					return regex.code();
				}
			});
		}
		if (value != null) {
			Pattern pattern = Pattern.compile(regex.regex());
			Matcher matcher = pattern.matcher(value.toString());
			if (!matcher.matches()) {
				throw new FastRuntimeException(regex.code() + "", regex.message());
			}
		}
	}

}
