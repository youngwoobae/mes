package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash06Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("ddkorDashService")
public class Dash06ServiceImpl implements Dash06Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash06Mapper mapper;
    /*유량계*/
    @Override
    public List<Map<String, Object>> getFlowMeterList(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getFlowMeterList=> ";
        paraMap.put("spotEquipNo",Long.parseLong(env.getProperty("spot.equip.414")));
        log.info(tag + "vsvc.paraMap = " + paraMap.toString());
        return mapper.getFlowMeterList(paraMap);
    }
    /*전력량계*/
    @Override
    public List<Map<String, Object>> getPowerList(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getPowerList=> ";
        paraMap.put("vsvc.spotEquipNo",Long.parseLong(env.getProperty("spot.equip.414")));
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPowerList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getDashSpotTmpr(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getDashSpotTmpr=> ";
        return mapper.getDashSpotTmpr(paraMap);
    }


    @Override
    public List<Map<String, Object>> getDashMainData(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getDashMainData=> ";
        return mapper.getDashMainData(paraMap);
    }

    @Override
    public List<Map<String, Object>> getDashWrapRoomData(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getDashWrapRoomData=> ";
        return mapper.getDashWrapRoomData(paraMap);
    }
}
