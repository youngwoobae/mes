package daedan.mes.moniter.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.matr.service.MatrService;
import daedan.mes.moniter.service.MoniterService;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
    private MoniterService moniterService;

    @PostMapping(value="/conditionRunHist") //
    public Result conditionRunHistory(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot",moniterService.getComboEquip(paraMap));
        result.setData(rmap);
        return result;
    }

    @PostMapping(value="/condition830MakeResultList") //
    public Result condition830MakeResultList(@RequestBody Map<String, Object> paraMap,HttpSession session){
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        paraMap.put("custNo", uvo.getCustInfo().getCustNo());

        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap.put("comboSpot",moniterService.getComboEquip(paraMap));
        result.setData(rmap);
        return result;
    }


    @PostMapping(value="/equipUsedHisr") //설비모니터링
    public Result equipUsedHisr(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.equipUsedHisr => ";
        paraMap.put("custNo", Long.parseLong(env.getProperty("cust_no")));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        List<Map<String, Object>> list = moniterService.getEquipUsedHstr(paraMap);
        result.setData(list);
        result.setTotalCount(moniterService.getEquipUsedHstrCount(paraMap));
        return result;
    }

    @PostMapping(value="/conditionTotalStatusList") //총괄현황
    public Result conditionTotalStatusList(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.conditionTotalStatusList => ";
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("selStr","주문고객선택");
        rmap.put("comboCmpy",moniterService.getComboOrderCmpy(paraMap));

        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code.base.prod_shape")));
        rmap.put("comboShape", codeService.getComboCodeList(paraMap));


        result.setData(rmap);
        return result;
    }


    @PostMapping(value="/totalStatusList") //총괄현황
    public Result totalStatusList(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.totalStatusList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getTotalStatusList(paraMap));
        result.setTotalCount(moniterService.getTotalStatusListCount(paraMap));
        return result;
    }

    @PostMapping(value="/ccpHeatList") //ccp
    public Result ccpHeatList(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.ccpHeatList => ";
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("ccpTp",Long.parseLong(env.getProperty("code.ccp_tp.heat"))); //CCP-살균공정
        Result result = Result.successInstance();
        result.setData(moniterService.getCcpHeatList(paraMap));
        result.setTotalCount(moniterService.getCcpHeatListCount(paraMap));
        return result;
    }

    @PostMapping(value="/equipProdMove") //설비별생산현황
    public Result getEquipProdMove(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.getEquipProdMove => ";
        log.info(tag+paraMap.get("spot_equip_no") );
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getEquipProdMove(paraMap));
        return result;
    }

    @PostMapping(value="/ccpTexts") //설비별생산현황
    public Result getCcpTexts(@RequestBody Map<String, Object> paraMap){
        String tag = "MoniterControlelr.getCcpTexts => ";
        log.info(tag+paraMap.get("spot_equip_no") );
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Result result = Result.successInstance();
        result.setData(moniterService.getCcpTexts(paraMap));
        return result;
    }

    //제품수불관리 - 왼쪽 파일리스트
    @PostMapping(value="/prodIoFileList")
    public Result prodIoFileList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getProdIoFileList(paraMap));
        result.setTotalCount(moniterService.getProdIoFileListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 주문내역 리스트
    @PostMapping(value="/ordHstList")
    public Result ordHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getOrdHstList(paraMap));
        result.setTotalCount(moniterService.getOrdHstListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 원료구매내역 리스트
    @PostMapping(value="/matrPursHstList")
    public Result matrPursHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrPursHstList(paraMap));
        result.setTotalCount(moniterService.getMatrPursHstListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 원료입고내역 리스트
    @PostMapping(value="/matrIwhHstList")
    public Result matrIwhHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrIwhHstList(paraMap));
        result.setTotalCount(moniterService.getMatrIwhHstListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 원료출고내역 리스트
    @PostMapping(value="/matrOwhHstList")
    public Result matrOwhHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));

        result.setData(moniterService.getMatrOwhHstList(paraMap));
        result.setTotalCount(moniterService.getMatrOwhHstListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 제품입고내역 리스트
    @PostMapping(value="/prodIwhHstList")
    public Result prodIwhHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));
        result.setData(moniterService.getProdIwhHstList(paraMap));
        result.setTotalCount(moniterService.getProdIwhHstListCount(paraMap));

        return result;
    }

    //제품수불관리 - 우측 제품출고내역 리스트
    @PostMapping(value="/prodOwhHstList")
    public Result prodOwhHstList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        paraMap.put("date", paraMap.get("closDt").toString().substring(0, 10));
        result.setData(moniterService.getProdOwhHstList(paraMap));
        result.setTotalCount(moniterService.getProdOwhHstListCount(paraMap));

        return result;
    }

    /**
     * 원료입고이력 목록
     *
     * @param paraMap
     * @return Result
     */
    @PostMapping(value="/getMatrIwhHist")
    public Result getMatrIwhHist(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(moniterService.getMatrIwhHist(paraMap));
        result.setTotalCount(moniterService.getMatrIwhHistCount(paraMap));

        return result;
    }

}
