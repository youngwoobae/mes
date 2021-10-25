package daedan.mes.pms.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.service.EquipBomService;
import daedan.mes.pms.service.PmsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value="/conditionsEmbPmsOrd")
    public Result conditionsEmbPmsOrd(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code_base.proc_stat")));
        rmap.put("comboProcStat", codeService.getComboCodeList(paraMap));
        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/pmsOrdInfo")
    public Result pmsOrdInfo(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        result.setData(pmsService.getPmsOrdInfo(paraMap));
        return result;
    }

    //pmsProcStat select 박스
    @PostMapping(value = "/comboPmsProcStat")
    public Result getComboPmsProcStat(@RequestBody Map<String, Object> paraMap){
        String tag = "PmsController.getComboPmsProcStat => ";
        log.info(tag + paraMap);
        Result result = Result.successInstance();
        paraMap.put("parCodeNo", Long.parseLong(env.getProperty("code_base.pmsStat")));
        result.setData(pmsService.getComboPmsProcStat(paraMap));
        return result;
    }

    //Pms 리스트 출력
    @PostMapping(value = "/pmsOrdrList")
    public Result getPmsOrdrList(@RequestBody Map<String, Object> paraMap) {
        String tag = "PmsController.getPmsOrdList => ";
        Result result = Result.successInstance();

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pmsService.getPmsOrdrList(paraMap));
        result.setTotalCount(pmsService.getPmsOrdrListCount(paraMap));
        return result;
    }

    //pms 상세정보 출력
    @PostMapping(value = "/pmsOrdInfoList")
    public Result getPmsOrdInfoList(@RequestBody Map<String, Object> paraMap){
        String tag = "PmsController.getPmsOrdInfoList => ";
        Result result = Result.successInstance();

        result.setData(pmsService.getPmsOrdInfoList(paraMap));

        return result;
    }

    //pms 정보 저장
    @PostMapping(value = "/savePms")
    public Result savePms(@RequestBody Map<String, Object> paraMap, HttpServletRequest request ){
        String tag = "PmsController.savePms =>";
        log.info("SavePms : " + tag);
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.savePmsOrdr(paraMap);

        return result;
    }

    //pms 정보 drop
    @PostMapping(value = "/dropPms")
    public Result dropPms(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        String tag = "PmsController.dropPms =>";
        log.info("DropPms : " + tag);
        Result result = Result.successInstance();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.dropPmsOrdr(paraMap);

        return result;
    }

    //pmsWho List
    @PostMapping(value = "/pmsWhoList")
    public Result getPmsWhoList(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        String tag = "PmsController.getPmsWhoList => ";
        Result result = Result.successInstance();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        result.setData(pmsService.getPmsWhoList(paraMap));

        return result;
    }

    //pmsWho 저장
    @PostMapping(value = "savePmsWhom")
    public Result savePmsWhom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        String tag = "PmsController.savePmsWhom => ";
        Result result = Result.successInstance();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.savePmsWhom(paraMap);

        return result;
    }

    //pmsWho Drop
    @PostMapping(value = "/dropPmsWhom")
    public Result dropPmsWhom(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        String tag = "PmsController.dropPmsWhom => ";
        Result result = Result.successInstance();

        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        pmsService.dropPmsWhom(paraMap);

        return result;
    }


}
