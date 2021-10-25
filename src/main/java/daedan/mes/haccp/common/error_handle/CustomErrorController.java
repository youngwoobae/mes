package daedan.mes.haccp.common.error_handle;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : kr.or.haccp.fresh.common.error_handle</li>
 * <li>설 명 : CustomErrorController.java</li>
 * <li>작성일 : 2020. 11. 18.</li>
 * <li>작성자 : 헨리</li>
 * </ul>
 */
@Controller
public class CustomErrorController implements ErrorController{

    private static final String VIEW_PATH = "/errors/";

    @RequestMapping(value = "/error")
    public String error(HttpServletRequest request, Model model) {
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	
    	if(status != null) {
    		int statusCode = Integer.valueOf(status.toString());
    		model.addAttribute("statusCode", statusCode);
    		if(statusCode == HttpStatus.NOT_FOUND.value()) {
    			return VIEW_PATH + "404";
    		} else if(statusCode == HttpStatus.FORBIDDEN.value()) {
    			return VIEW_PATH + "500";
    		} else {
    			return VIEW_PATH + "error";
    		}
    	}
    	
        return VIEW_PATH + "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}