package daedan.mes.dash.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dash.service.Dash17Service;
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
@RequestMapping("/api/daedan/mes/dash17")
public class Dash17Controller {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private Dash17Service dash17;


    /**
     * 온도모니터링 내용 저장
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getTmpr17List")
    public Result getTmpr17List(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(dash17.getTmpr17List(paraMap));
        return result;
    }

    /**
     * 카운터 저장
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getFinalProcCnt")
    public Result getFinalCounter(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(dash17.getFinalProcCnt(paraMap));
        return result;
    }
}
