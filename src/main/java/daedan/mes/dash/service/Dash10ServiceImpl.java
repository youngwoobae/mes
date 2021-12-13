package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash10Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dash10Service")
public class Dash10ServiceImpl implements Dash10Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash10Mapper mapper;


    @Override
    public Map<String, Object> getTmpr10List(Map<String, Object> paraMap) {
        String tag = "Dash10Service.getTmpr10List => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Map<String, Object> rmap = new HashMap<String,Object>();
        Map<String, Object> dmap = new HashMap<String,Object>();
        List<Map<String,Object>> ds = (mapper.getTmpr10List(paraMap));
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
        rmap.put("minVal",minval);
        rmap.put("avgVal",avgval);
        rmap.put("maxVal",maxval);
//        log.info(("******rmap=" +rmap));
        return rmap;
    }
    @Override
    public Map<String, Object> getFinalMetalDetect(Map<String, Object> paraMap) {
        String tag = "Dash10Service.getFinalMetalDetect => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getFinalMetalDetect(paraMap);
    }

}
