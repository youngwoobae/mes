package daedan.mes.spot.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.spot.service.SpotService;
import daedan.mes.user.domain.UserInfo;
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
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/spot")
public class SpotController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private SpotService spotService;


    @Autowired
    private Environment env;

    @PostMapping(value="/comboSpotEquip")
    public Result comboSpotEquip(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        result.setData(spotService.getComboSpotEquip(paraMap));
        return result;
    }
    @PostMapping(value="/spotList")
    public Result spotList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("custNo",Long.parseLong(env.getProperty("cust_no")));
        result.setData(spotService.getSpotList(paraMap));
        result.setTotalCount(spotService.getSpotListCount(paraMap));

        return result;
    }
    /*다중선택용-페이징기능없음*/
    @PostMapping(value="/spots")
    public Result spots(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(spotService.getSpots(paraMap));
        return result;
    }

    @PostMapping(value = "/spotInfo")
    public Result spotInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(spotService.getSpotInfo(paraMap));
        return result;
    }

    //삭제 부분
    @PostMapping(value="/dropSpot")
    public Result dropSpot(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("spotNo", paraMap.get("spotNo"));

        spotService.dropSpot(paraMap);
        return result;
    }

    @PostMapping(value="/saveSpot")
    public Result saveSpotInfo(@RequestBody Map<String, Object> paraMap , HttpServletRequest request, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        spotService.saveSpot(paraMap);
        return result;
    }
}
