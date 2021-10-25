package daedan.mes.sys.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.sys.service.SysService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.sys.service.SysService;
import daedan.mes.sysmenu.service.SysMenuService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/sys")
public class SysController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private SysService sysService;


    /*주문정보 리스트*/
    @PostMapping(value="/ordList")
    public Result getOrdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getOrdList(paraMap));
        return result;
    }

    /*주문제품정보 리스트*/
    @PostMapping(value="/ordProdList")
    public Result getOrdProdList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getOrdProdList(paraMap));
        return result;
    }

    /*지시정보 리스트*/
    @PostMapping(value="/indcList")
    public Result getIndcList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getIndcList(paraMap));
        return result;
    }

    /*지시원료정보 리스트*/
    @PostMapping(value="/indcMatrList")
    public Result getIndcMatrList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getIndcMatrList(paraMap));
        return result;
    }

    /*구매정보 리스트*/
    @PostMapping(value="/pursList")
    public Result getPursList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getPursList(paraMap));
        return result;
    }

    /*구매원료정보 리스트*/
    @PostMapping(value="/pursMatrList")
    public Result getPursMatrList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getPursMatrList(paraMap));
        return result;
    }

    /*원료입고정보 리스트*/
    @PostMapping(value="/matrIwhList")
    public Result getMatrIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getMatrIwhList(paraMap));
        return result;
    }

    /*원료출고정보 리스트*/
    @PostMapping(value="/matrOwhList")
    public Result getMatrOwhList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getMatrOwhList(paraMap));
        return result;
    }

    /*지시결과정보 리스트*/
    @PostMapping(value="/indcRsltList")
    public Result getIndcRsltList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getIndcRsltList(paraMap));
        return result;
    }

    /*제품입고 리스트*/
    @PostMapping(value="/prodIwhList")
    public Result getProdIwhList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getProdIwhList(paraMap));
        return result;
    }

    /*제품출고 리스트*/
    @PostMapping(value="/prodOwhList")
    public Result getProdOwhList(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getProdOwhList(paraMap));
        return result;
    }

    /*RollBack*/
    @PostMapping(value = "/rollBack")
    public Result rollBack(@RequestBody Map<String, Object> paraMap, HttpServletRequest request , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        result.setData(sysService.rollBack(paraMap));
        return result;
    }

    /*작업지시결과 결과*/
    @PostMapping(value = "/resultIndcRslt")
    public Result getResultIndcRslt(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getResultIndcRslt(paraMap));
        return result;
    }

    /*제품입고 결과*/
    @PostMapping(value = "/resultProdIwh")
    public Result getResultProdIwh(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getResultProdIwhList(paraMap));
        return result;
    }

    /*제품출고 결과*/
    @PostMapping(value = "/resultProdOwh")
    public Result getResultProdOwh(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(sysService.getResultProdOwhList(paraMap));
        return result;
    }
}
