package daedan.mes.pumapi.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.pumapi.service.PumApiService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log log = LogFactory.getLog(this.getClass());

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
        log.info("syncUser.custNo = " + custNo);
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncUser(paraMap));
        return result;
    }

    /**
     * 풀무원 원자재정보 동기화
     *
     * @param paraMap
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncMatr")
    public Result syncMatr(@RequestBody Map<String, Object> paraMap, HttpServletRequest request) {
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
     * 풀무원 제품별 BOM정보 동기화
     *
     * @param paraMap
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncBom")
    public Result syncBom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        String sender = paraMap.get("sender").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncBom(paraMap));
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
    /**
     * 풀무원 출고정보 수신(From 소신포털) 동기화
     *
     * @param paraMap
     * @param request
     * @return Result
     */
    @PostMapping(value = "/syncProdOut")
    public Result syncProdOut(@RequestBody Map<String, Object> paraMap, HttpServletRequest request) {
        Result result = Result.successInstance();
        String saupNo = paraMap.get("saupNo").toString();
        String sender = paraMap.get("sender").toString();
        UserInfo uvo = pumApiService.getUserInfoBySaupNo(saupNo);
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("userId", uvo.getUserId());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("custNo", custNo);
        paraMap.put("sender", sender);
        result.setData(pumApiService.syncProdOut(paraMap));
        return result;
    }
}
