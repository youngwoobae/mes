package daedan.mes.spot.controller;

import daedan.mes.code.service.CodeService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.NetworkUtil;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.service.EquipService;
import daedan.mes.spot.service.SpotEquipService;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/api/daedan/mes/spot/equip")

public class SpotEquipController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private SpotEquipService esService;
    @Autowired
    private EquipService equipService;
    @Autowired
    private Environment env;

    @Autowired
    private CodeService codeService;

    @PostMapping(value="/conditionEmbSpotInfo")
    public Result conditionEmbSpotInfo(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        Result result = Result.successInstance();
        Map<String,Object> rmap = new HashMap<String,Object>();
        paraMap.put("parCodeNo",Long.parseLong(env.getProperty("code.base.meas_unit")));
        rmap.put("measUnits", codeService.getComboCodeList(paraMap));
        rmap.put("equips", equipService.getComboEquipList(paraMap));
        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/spotEquipList")
    public Result equipList(@RequestBody Map<String, Object> paraMap){
        Result result = Result.successInstance();
        String tag = "SpotEquipController.spotEquipList =>";
        log.info(tag + paraMap);
        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));

        result.setData(esService.getSpotEquipList(paraMap));
        result.setTotalCount(esService.getSpotEquipListCount(paraMap));
        return result;
    }

    @PostMapping(value="/addSpotEquip")
    public Result addSpotEquip(@RequestBody Map<String, Object> paraMap , HttpServletRequest request){
            Result result = Result.successInstance();

           paraMap.put("spot_no", StringUtil.convertPageNo(paraMap));
            paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
            log.info("spotInfo = " + paraMap.get("spotInfo"));
            log.info("userId = " + paraMap.get("userId"));

            String resource = "config/mes.properties";
            Properties properties = new Properties();

            try {
                Reader reader = Resources.getResourceAsReader(resource);
                properties.load(reader);
                esService.addSpotEquip(paraMap);

            } catch (IOException e) {
                e.printStackTrace();
                Objects.requireNonNull(null, properties.getProperty("property_file_not_found"));

            }

            return result;
    }
    @PostMapping(value="/spotEquipInfo")
    public Result getSpotEquipInfo(@RequestBody Map<String, Object> paraMap) {
        Result result = Result.successInstance();
        String tag = "SpotEquipController.equipSpotInfo =>";
        Map<String,Object> rmap = esService.getSpotEquipInfo(paraMap);
        result.setData(rmap);
        log.info("resultMap = " + rmap);
        return result;
    }
    @Transactional
    @PostMapping(value="/saveSpotEquip")
    public Result saveSpotEquip(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        Result result = Result.successInstance();
        String tag = "SpotEquipController.saveSpotEquip =>";
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        log.info(tag + "params = " + paraMap.toString());
        esService.saveSpotEquip(paraMap);
        return result;
    }
    @Transactional
    @PostMapping(value="/dropSpotEquip")
    public Result dropSpotEquip(@RequestBody Map<String, Object> paraMap, HttpServletRequest request){
        String tag = "SpotEquipController.dropSpotEquip =>";
        Result result = Result.successInstance();
        paraMap.put("ipaddr", NetworkUtil.getClientIp(request));
        paraMap.put("userId", paraMap.get("userId"));
        log.info(tag + "params = " + paraMap.toString());
        esService.dropSpotEquip(paraMap);
        return result;
    }

}
