package daedan.mes.moniter.controller;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.moniter.service.MoniterService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/moniter")
public class MoniterController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private MoniterService moniterService;

    @Autowired
    private UserService userService;


    @PostMapping(value="/conditionRunHist") //
    public Result conditionRunHistory(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot",moniterService.getComboEquip(paraMap));

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

    @PostMapping(value="/condition830MakeResultList") //
    public Result condition830MakeResultList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();

        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot",moniterService.getComboEquip(paraMap));
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


    @PostMapping(value="/equipUsedHisr") //설비모니터링
    public Result equipUsedHisr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "MoniterControlelr.equipUsedHisr => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getEquipUsedHstr(paraMap));
        result.setTotalCount(moniterService.getEquipUsedHstrCount(paraMap));
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

    @PostMapping(value="/conditionTotalStatusList") //총괄현황
    public Result conditionTotalStatusList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "MoniterControlelr.conditionTotalStatusList => ";
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("selStr","주문고객선택");
        rmap.put("comboCmpy",moniterService.getComboOrderCmpy(paraMap));

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.prod_shape")));
        rmap.put("comboShape", codeService.getComboCodeList(paraMap));


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


    @PostMapping(value="/totalStatusList") //총괄현황
    public Result totalStatusList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        String tag = "MoniterControlelr.totalStatusList => ";
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getTotalStatusList(paraMap));
        result.setTotalCount(moniterService.getTotalStatusListCount(paraMap));
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

    @PostMapping(value="/ccpHeatList") //ccp
    public Result ccpHeatList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ccpTp",Long.parseLong(env.getProperty("code.ccp_tp.heat"))); //CCP-살균공정
        Result result = Result.successInstance();
        result.setData(moniterService.getCcpHeatList(paraMap));
        result.setTotalCount(moniterService.getCcpHeatListCount(paraMap));
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

    @PostMapping(value="/equipProdMove") //설비별생산현황
    public Result getEquipProdMove(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getEquipProdMove(paraMap));
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

    @PostMapping(value="/ccpTexts") //설비별생산현황
    public Result getCcpTexts(@RequestBody Map<String, Object> paraMap, HttpSession session){
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getCcpTexts(paraMap));
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

    //제품수불관리 - 왼쪽 파일리스트
    @PostMapping(value="/prodIoFileList")
    public Result prodIoFileList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getProdIoFileList(paraMap));
        result.setTotalCount(moniterService.getProdIoFileListCount(paraMap));

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

    //제품수불관리 - 우측 주문내역 리스트
    @PostMapping(value="/ordHstList")
    public Result ordHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        result.setData(moniterService.getOrdHstList(paraMap));
        result.setTotalCount(moniterService.getOrdHstListCount(paraMap));
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

    //제품수불관리 - 우측 원료구매내역 리스트
    @PostMapping(value="/matrPursHstList")
    public Result matrPursHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrPursHstList(paraMap));
        result.setTotalCount(moniterService.getMatrPursHstListCount(paraMap));
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

    //제품수불관리 - 우측 원료입고내역 리스트
    @PostMapping(value="/matrIwhHstList")
    public Result matrIwhHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrIwhHstList(paraMap));
        result.setTotalCount(moniterService.getMatrIwhHstListCount(paraMap));
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

    //제품수불관리 - 우측 원료출고내역 리스트
    @PostMapping(value="/matrOwhHstList")
    public Result matrOwhHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrOwhHstList(paraMap));
        result.setTotalCount(moniterService.getMatrOwhHstListCount(paraMap));
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

    //제품수불관리 - 우측 제품입고내역 리스트
    @PostMapping(value="/prodIwhHstList")
    public Result prodIwhHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));
        result.setData(moniterService.getProdIwhHstList(paraMap));
        result.setTotalCount(moniterService.getProdIwhHstListCount(paraMap));
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

    //제품수불관리 - 우측 제품출고내역 리스트
    @PostMapping(value="/prodOwhHstList")
    public Result prodOwhHstList(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));
        result.setData(moniterService.getProdOwhHstList(paraMap));
        result.setTotalCount(moniterService.getProdOwhHstListCount(paraMap));
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
     * 원료입고이력 목록
     *
     * @param paraMap
     * @return Result
     */
    @PostMapping(value="/getMatrIwhHstr")
    public Result getMatrIwhHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getMatrIwhHstr(paraMap));
        result.setTotalCount(moniterService.getMatrIwhHstrCount(paraMap));
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


    @PostMapping(value="/getMatrOwhHstr")
    public Result getMatrOwhHstr(@RequestBody Map<String, Object> paraMap, HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getMatrOwhHstr(paraMap));
        result.setTotalCount(moniterService.getMatrOwhHstrCount(paraMap));
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
     * 온도모니터링
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions860")
    public Result conditions860(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot" , cmmnService.getComboSpot(paraMap));
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
    /**
     * 온도모니터링
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/getTmprLogHstr")
    public Result getTmprLogHstr(@RequestBody Map<String, Object> paraMap , HttpSession session){
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();
        Map<String, Object> dmap = new HashMap<String,Object>();
        List<Map<String,Object>> ds = (moniterService.getTmprLogHstr(paraMap));
        int idx = -1;
        ArrayList<String> rcvTm = new ArrayList<String>();
        ArrayList<Float> maxval = new ArrayList<Float>();
        ArrayList<Float> minval = new ArrayList<Float>();
        ArrayList<Float> avgval = new ArrayList<Float>();
        while(++idx < ds.size()) {
            dmap = ds.get(idx);
            rcvTm.add(dmap.get("rcvTm").toString());
            maxval.add(Float.parseFloat(dmap.get("maxVal").toString()));
            minval.add(Float.parseFloat(dmap.get("minVal").toString()));
            avgval.add(Float.parseFloat(dmap.get("avgVal").toString()));
        }
        rmap.put("griDs",ds);
        rmap.put("rcvTm",rcvTm);
        rmap.put("minVal", minval);
        rmap.put("avgVal",avgval);
        rmap.put("maxVal",maxval);
        result.setData(rmap);
//        System.out.println(paraMap.get("*" + "max_val"));

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
     * 금속검출모니터링
     *
     * @param paraMap
     * @param session
     * @return Result
     */
    @PostMapping(value="/conditions861")
    public Result conditions861(@RequestBody Map<String, Object> paraMap , HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        Long custNo = uvo.getCustInfo().getCustNo();
        paraMap.put("custNo", custNo);
        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot" , cmmnService.getComboSpot(paraMap));
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
}
