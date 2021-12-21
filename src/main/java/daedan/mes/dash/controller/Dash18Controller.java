package daedan.mes.dash.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.dash.service.Dash18Service;
import daedan.mes.sysmenu.user.domain.AccHstr;
import daedan.mes.sysmenu.user.domain.EvntType;
import daedan.mes.sysmenu.user.domain.UserInfo;
import daedan.mes.sysmenu.user.service.UserService;
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
@RequestMapping("/api/daedan/mes/dash18")
public class Dash18Controller {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;


    @Autowired
    private Dash18Service dash18;

    @Autowired
    private UserService userService;

    /**
     * 온도모니터링 목록 추출
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getTmpr18List")
    public Result getTmpr18List(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(dash18.getTmpr18List(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    /**
     * 찜기 내용 저장
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getFinalHeatStatus")
    public Result getFinalMetalDetect(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(dash18.getFinalHeatStatus(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
}
