package orj.worf.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason="Invalid Request(非法请求)")
public class ForbbidenException extends RuntimeException {

	private static final long serialVersionUID = -4771027603173755767L;

}
