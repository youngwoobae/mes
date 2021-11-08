package daedan.mes.dash.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dash.service.Dash10Service;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/dash10")
public class Dash10Controller {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private Dash10Service dashService;


    /**
     * 온도모니터링 내용 저장
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getTmpr10List")
    public Result getTmpr10List(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(dashService.getTmpr10List(paraMap));
        return result;
    }


    /**
     *  금속검출 모니터링 내용 저장
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getFinalMetalDetect")
    public Result getFinalMetalDetect(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(dashService.getFinalMetalDetect(paraMap));
        return result;
    }
}
