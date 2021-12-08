package daedan.mes.common.interceptor;

import daedan.mes.common.error.UnauthorizedException;
import daedan.mes.common.service.jwt.JwtService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor{
	private static final String HEADER_AUTH = "authorization";
	private Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private JwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		final String token = request.getHeader(HEADER_AUTH);

		if(token != null && jwtService.isUsable(token)){
			return true;
		}else{
			throw new UnauthorizedException();
		}
	}



	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
		if (ex != null) {
			log.info("kmjkmjkmkkmkkmj = " + ex.getMessage());
		}

	}
}
