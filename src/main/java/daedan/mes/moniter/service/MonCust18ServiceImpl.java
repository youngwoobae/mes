package daedan.mes.moniter.service;

import daedan.mes.moniter.mapper.Cust18Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("monCust18Service")
public class MonCust18ServiceImpl implements MonCust18Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private Cust18Mapper mapper;

    @Override
    public List<Map<String,Object>> getHeatLogHstr(Map<String, Object> paraMap) {
        String tag = "cust18Service.getHeatLogHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getHeatLogHstr(paraMap);
    }

}
