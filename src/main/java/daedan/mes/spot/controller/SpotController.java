package daedan.mes.spot.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.spot.service.SpotService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

@RestController
@RequestMapping("/api/daedan/mes/spot")
public class SpotController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private CodeService codeService;

    @Autowired
    private SpotService spotService;


    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @PostMapping(value="/comboSpotEquip")
    public Result comboSpotEquip(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        result.setData(spotService.getComboSpotEquip(paraMap));
        return result;
    }
    /**
     * 작업장목록 검색조건
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions104")
    public Result conditions104(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("selectStr", "CCP 구분");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ccp_tp")));
        Map<String,Object> rmap = new HashMap<String,Object>();

        rmap.put("comboCcpTp",codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    /**
     * 작업장목록
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/spotList")
    public Result spotList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(spotService.getSpotList(paraMap));
        result.setTotalCount(spotService.getSpotListCount(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26
        return result;
    }
    /*다중선택용-페이징기능없음*/
    @PostMapping(value="/spots")
    public Result spots(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(spotService.getSpots(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    @PostMapping(value="/conditionsEmbSpotInfo")
    public Result conditionsEmbSpotInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        Result result = Result.successInstance();
        Map<String,Object> rmap = new HashMap<String,Object>();

        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.ccp_tp")));
        paraMap.put("selectStr", "CCP 구분");
        rmap.put("comboCcpTp", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }
    @PostMapping(value = "/spotInfo")
    public Result spotInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        result.setData(spotService.getSpotInfo(paraMap));
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.READ, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }

    //삭제 부분
    @PostMapping(value="/dropSpot")
    public Result dropSpot(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("spotNo", paraMap.get("spotNo"));

        spotService.dropSpot(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.DROP, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
    /**
     * 작업장정보 저장
     *
     * @param paraMap
     * @param request
     * @param session
     * @return Result
     */
    @PostMapping(value="/saveSpotInfo")
    public Result saveSpotInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        spotService.saveSpotInfo(paraMap);
        //SOL AddOn By KMJ AT 21.11.16
        try {
            AccHstr acvo = (AccHstr) session.getAttribute("acchstr");
            userService.saveAccLogEvnt(custNo, acvo.getAccNo(), EvntType.SAVE, 1);
        }
        catch(NullPointerException ne) {
        }
        //EOL AddON By KMJ AT 21.11.26

        return result;
    }
}
