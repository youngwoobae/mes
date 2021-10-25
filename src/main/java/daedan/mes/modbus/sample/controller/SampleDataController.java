package daedan.mes.modbus.sample.controller;

import daedan.mes.common.domain.Result;
import daedan.mes.modbus.domain.TmprHstr;
import daedan.mes.modbus.sample.service.SampleService;
import daedan.mes.modbus.service.ModbusService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/daedan/mes/sample")
public class SampleDataController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private ModbusService modbusService;

    @PostMapping(value="/tempHumygInfo")
    public Result tempHumygInfo(@RequestBody Map<String, Object> paraMap){
        String tag = "sampleController.tempHumygInfo => ";
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();

        Map<String, Object> tmap = sampleService.getSampleTempData(paraMap);
        rmap.put("tmpr", tmap.get("measVal"));
        Map<String, Object> hmap = sampleService.getSampleHumygData(paraMap);
        rmap.put("humy", hmap.get("measVal"));

        //TmprHstr vo = modbusService.saveTmprHstr(paraMap);
        result.setData(rmap);
        return result;
    }
    @PostMapping(value="/tempHumypInfo")
    public Result tempHumypInfo(@RequestBody Map<String, Object> paraMap){
        String tag = "sampleController.tempHumygInfo => ";
        Result result = Result.successInstance();
        Map<String, Object> rmap = new HashMap<String,Object>();

        Map<String, Object> tmap = sampleService.getSampleTempData(paraMap);
        rmap.put("tmpr", tmap.get("measVal"));
        Map<String, Object> hmap = sampleService.getSampleHumypData(paraMap);
        rmap.put("humy", hmap.get("measVal"));

        //TmprHstr vo = modbusService.saveTmprHstr(paraMap);
        //result.setData(vo);
        result.setData(rmap);
        return result;
    }

}
