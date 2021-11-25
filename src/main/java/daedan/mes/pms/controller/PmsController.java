package daedan.mes.pms.controller;

import daedan.mes.user.domain.UserInfo;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.service.EquipBomService;
import daedan.mes.pms.service.PmsService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import daedan.mes.user.service.UserService;
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
@RequestMapping("/api/daedan/mes/pms")
public class PmsController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private PmsService pmsService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;

    @PostMapping(value="/conditionsEmbPmsOrd")
    public Result conditionsEmbPmsOrd(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base.proc_stat")));
        rmap.put("comboProcStat", codeService.getComboCodeList(paraMap));
        result.setData(rmap);

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
    @PostMapping(value="/pmsOrdInfo")
    public Result pmsOrdInfo(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        result.setData(pmsService.getPmsOrdInfo(paraMap));

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

    //pmsProcStat select 박스
    @PostMapping(value = "/comboPmsProcStat")
    public Result getComboPmsProcStat(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "PmsController.getComboPmsProcStat => ";
        log.info(tag + paraMap);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code_base.pmsStat")));
        result.setData(pmsService.getComboPmsProcStat(paraMap));

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

    //Pms 리스트 출력
    @PostMapping(value = "/pmsOrdrList")
    public Result getPmsOrdrList(@RequestBody Map<String, Object> paraMap, HttpSession session) {
        String tag = "PmsController.getPmsOrdList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pmsService.getPmsOrdrList(paraMap));
        result.setTotalCount(pmsService.getPmsOrdrListCount(paraMap));

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

    //pms 상세정보 출력
    @PostMapping(value = "/pmsOrdInfoList")
    public Result getPmsOrdInfoList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "PmsController.getPmsOrdInfoList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        result.setData(pmsService.getPmsOrdInfoList(paraMap));

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

    //pms 정보 저장
    @PostMapping(value = "/savePms")
    public Result savePms(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "PmsController.savePms =>";
        log.info("SavePms : " + tag);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.savePmsOrdr(paraMap);

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

    //pms 정보 drop
    @PostMapping(value = "/dropPms")
    public Result dropPms(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "PmsController.dropPms =>";
        log.info("DropPms : " + tag);
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.dropPmsOrdr(paraMap);

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

    //pmsWho List
    @PostMapping(value = "/pmsWhoList")
    public Result getPmsWhoList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "PmsController.getPmsWhoList => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pmsService.getPmsWhoList(paraMap));

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

    //pmsWho 저장
    @PostMapping(value = "savePmsWhom")
    public Result savePmsWhom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "PmsController.savePmsWhom => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.savePmsWhom(paraMap);

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

    //pmsWho Drop
    @PostMapping(value = "/dropPmsWhom")
    public Result dropPmsWhom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request, HttpSession session){
        String tag = "PmsController.dropPmsWhom => ";
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.dropPmsWhom(paraMap);

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
