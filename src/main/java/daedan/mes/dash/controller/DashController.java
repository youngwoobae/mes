package daedan.mes.dash.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.dash.service.DashService;
import daedan.mes.dash.service.Dash08Service;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/dash")
public class DashController {
    private Log log = LogFactory.getLog(this.getClass());


    @Autowired
    private DashService dashService;

    @Autowired

    private Dash08Service yyjgService;


    @PostMapping(value="/graphData")
    public Result getGraphData(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        log.info(tag + "parCodeNo = " + paraMap.get("parCodeNo"));
        List<Map<String,Object>> list = dashService.getGraphData(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value="/getDashTotalMakeIndc")
    public Result getDashTotalMakeIndc(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();
        String tag = "DashController=>";

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        log.info(tag + "parCodeNo = " + paraMap.get("parCodeNo"));
        List<Map<String,Object>> list = dashService.getDashTotalMakeIndc(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value="/getDashTotalMakeIndcRslt")
    public Result getDashTotalMakeIndcRslt(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        log.info(tag + "parCodeNo = " + paraMap.get("parCodeNo"));
        List<Map<String,Object>> list = dashService.getDashTotalMakeIndcRslt(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value="/ToDayQuality")
    public Result getToDayQuality(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        log.info(tag + "parCodeNo = " + paraMap.get("parCodeNo"));
        List<Map<String,Object>> list = dashService.getToDayQuality(paraMap);
        result.setData(list);
        return result;
    }

    @PostMapping(value="/ToDayStoreQuality")
    public Result ToDayStoreQuality(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        List<Map<String,Object>> list = dashService.ToDayStoreQuality(paraMap);
        result.setData(list);
        return result;
    }
    @PostMapping(value="/scadaTmprList")
    public Result scadaTmprList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(dashService.getScadaTmprList(paraMap));
        return result;
    }

    @PostMapping(value="/getUtilsAndTransAndErrorList")
    public Result getUtilsAndTransAndErrorList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "vsvc.DashService.getUtilsAndTransAndErrorList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(dashService.getUtilsAndTransAndErrorList(paraMap));
        return result;
    }

    @PostMapping(value ="/getPhoneDashPreMonthList")
    public Result getPhoneDashPreMonthList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "vsvs.DashService.getPhoneDashPreMonthList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String, Object> passMap = new HashMap<String, Object>();
        passMap.put("procYy", paraMap.get("year").toString());
        passMap.put("procMm", paraMap.get("month").toString());
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(dashService.getPhoneDashPreMonthList(passMap));
        return result;
    }

    @PostMapping(value ="/getPhoneDashMonthList")
    public Result getPhoneDashMonthList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "vsvs.DashService.getPhoneDashMonthList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String, Object> passMap = new HashMap<String, Object>();
        passMap.put("procYy", paraMap.get("year").toString());
        passMap.put("procMm", paraMap.get("month").toString());
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(dashService.getPhoneDashMonthList(passMap));
        return result;
    }

    @PostMapping(value = "/phoneDashRsltInfo")
    public Result getPhoneDashRsltInfo(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "DashController.getPhoneDashRsltInfo => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(dashService.getPhoneDashRsltInfo(paraMap));
        result.setTotalCount(dashService.getPhoneDashRsltInfoCount(paraMap));
        return result;
    }
    /*영양제과/Dashboard 중앙화면*/
    @PostMapping(value = "/getYyjgProdIoList")
    public Result getYyjgProdIoList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "DashController.getYyjgProdIoList => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(dashService.getYyjgProdIoList(paraMap));
        result.setTotalCount(dashService.getYyjgProdIoListCount(paraMap));
        return result;
    }

    /*영양제과/Dashboard 중앙화면*/
    @PostMapping(value = "/getYyjgMatrIoList")
    public Result getYyjgMatrIoList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        String tag = "DashController.getYyjgMatrIoList => ";
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(dashService.getYyjgMatrIoList(paraMap));
        result.setTotalCount(dashService.getYyjgMatrIoListCount(paraMap));
        return result;
    }

    @PostMapping(value="/evtMsgList")
    public Result evtMsgList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        List<Map<String,Object>> list = dashService.getEvtMsgList(paraMap);
        result.setData(list);
        return result;
    }


    @PostMapping(value="/humanList")
    public Result humanList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        log.info(tag + "parCodeNo = " + paraMap.get("parCodeNo"));
        List<Map<String,Object>> list = dashService.gethumanList(paraMap);
        result.setData(list);
        result.setTotalCount(dashService.getToTalhumanCount(paraMap));
        return result;
    }
    @PostMapping(value="/getYyjgPhothCounter")
    public Result getYyjgPhothCounter(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        String tag = "DashController=>";
        result.setData(yyjgService.getYyjgPhothCounter(paraMap));
        return result;
    }

    @PostMapping(value="/getYyjgSpotList")
    public Result getYyjgSpotList(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(yyjgService.getYyjgSpotList(paraMap));
        return result;
    }

    @PostMapping(value="/getMetalLog")
    public Result getMetalLog(@RequestBody Map<String, Object> paraMap  , HttpSession session){
        Result result = Result.successInstance();

        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        result.setData(dashService.getMetalLog(paraMap));
//        result.setTotalCount(dashService.getMetalLogCount(paraMap));
        return result;
    }

}
