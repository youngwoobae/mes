package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash10Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
    public List<Map<String, Object>> getTmpr10List(Map<String, Object> paraMap) {
        String tag = "Dash10Service.getTmpr10List => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getTmpr10List(paraMap);
    }
    @Override
    public Map<String, Object> getFinalMetalDetect(Map<String, Object> paraMap) {
        String tag = "Dash10Service.getFinalMetalDetect => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getFinalMetalDetect(paraMap);
    }

}
