package daedan.mes.pumapi.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.pumapi.service.PumApiService;
import daedan.mes.sysmenu.user.domain.AccHstr;
import daedan.mes.sysmenu.user.domain.EvntType;
import daedan.mes.sysmenu.user.domain.UserInfo;
import daedan.mes.sysmenu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/puwapi")
public class PumApiController {

    @Autowired
    PumApiService pumApiService;

    @Autowired
    UserService userService;
    /**
     * 풀무원 제품정보 동기화
     *
     * @param paraMap
     * @param session
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncUser")
    public Result syncUser(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        String sender = paraMap.get("sender").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncUser(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

    /**
     * 풀무원 원자재정보 동기화
     *
     * @param paraMap
     * @param session
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncMatr")
    public Result syncMatr(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        String sender = paraMap.get("sender").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncMatr(paraMap));
        return result;
    }

    /**
     * 풀무원 제품정보 동기화
     *
     * @param paraMap
     * @param session
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncProd")
    public Result syncProd(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        String sender = paraMap.get("sender").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncProd(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    /**
     * 풀무원 주문접수
     *
     * @param paraMap
     * @param session
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncOrdPlan")
    public Result syncOrdPlan(@RequestBody Map<String, Object> paraMap, HttpSession session, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        result.setData(pumApiService.syncOrdPlan(paraMap));

        //SOL AddOn By KMJ AT 21.11.16
        if (uvo.getCustInfo().getActEvtLogYn().equals("Y")) {
            try {
                AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
                userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
            } catch (NullPointerException ne) {
            }
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }

}
