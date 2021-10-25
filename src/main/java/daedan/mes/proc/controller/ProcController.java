package daedan.mes.proc.controller;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.proc.service.ProcService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/proc")
public class ProcController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private ProcService procService;

    @PostMapping(value = "/conditions")
    public Result getConditions(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String, Object> rmap = new HashMap<String,Object>();

        paraMap.put("selectStr","선택");
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.saleunit")));
        rmap.put("comboUnit", codeService.getComboCodeList(paraMap));

        result.setData(rmap);
        return result;
    }


    @PostMapping(value = "/procList")
    public Result procList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(procService.getProcList(paraMap));
        result.setTotalCount(procService.getProcListCount(paraMap));
        return result;
    }


    @PostMapping(value = "/comboEmbProcWork")
    public Result PostMapping(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Map<String,Object> rmap = new HashMap<String,Object>();
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("selectStr","인력투입그룹선택");
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.proc_grp")));
        rmap.put("comboProcGrp",codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value = "/procInfo")
    public Result procInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(procService.getProcInfo(paraMap));
        return result;
    }

    @PostMapping(value = "/procBrnchList")
    public Result procBrnchList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code_base_proc")));
        result.setData(procService.getProcBrnchList(paraMap));
        result.setTotalCount(procService.getProcBrnchListCount(paraMap));
        return result;
    }
    @PostMapping(value = "/procBrnchInfo")
    public Result procBrnchInfo(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        result.setData(procService.getProcBrnchInfo(paraMap));
        return result;
    }

    @PostMapping(value = "/saveProcBrnchList")
    public Result saveProcBrnchList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        procService.saveProcBrnchList(paraMap);
        return result;
    }

    @PostMapping(value = "/saveProcInfo")
    public Result saveProcInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        procService.saveProcInfo(paraMap);
        return result;
    }
    @PostMapping(value = "/saveProcWork")
    public Result saveProcWork(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        procService.saveProcWork(paraMap);
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        return result;
    }

    @PostMapping(value = "/procStdList")
    public Result getProcStdList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(procService.getProcStdList(paraMap));
        result.setTotalCount(procService.getProcStdListCount(paraMap));
        return result;
    }
    @PostMapping(value = "/saveProcMpList")
    public Result saveProcMp(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        procService.saveProcMpList(paraMap);
        return result;
    }

    @PostMapping(value = "/saveProcStd")
    public Result saveProcStd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        procService.saveProcStd(paraMap);

        return result;
    }

    @PostMapping(value = "/dropProcStd")
    public Result dropProcStd(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        procService.dropProcStd(paraMap);

        return result;
    }

    /*공정별 투입인력 현황 - 투입인력 - 공정추가 버튼 클릭 시*/
    @PostMapping(value = "/popProcList")
    public Result getPopProcList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));

        result.setData(procService.getPopProcList(paraMap));
        result.setTotalCount(procService.getPopProcListCount(paraMap));
        return  result;
    }


    @PostMapping(value = "/procMemberList")
    public Result procMemberList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("ocpnKind",Long.parseLong(env.getProperty("ocpn_kind_blue")));
        result.setData(procService.getProcMemberList(paraMap));
        return result;
    }

    /*공정목록*/
    @PostMapping(value = "/procInfoList")
    public Result procInfoList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "procController.procInfoList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(procService.getProcInfoList(paraMap));
        result.setTotalCount(procService.getProcInfoListCount(paraMap));
        return  result;
    }
}
