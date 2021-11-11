package daedan.mes.moniter.service;

import daedan.mes.moniter.mapper.Cust10Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("cust10Service")
public class Cust10ServiceImpl implements Cust10Service{
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private Cust10Mapper mapper;

    @Override
    public List<Map<String,Object>> getMetalDetctHstr(Map<String, Object> paraMap) {
        String tag = "cust10Service.getMetalDetctHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMetalDetctHstr(paraMap);
    }

    @Override
    public int getMetalDetectHstrCount(Map<String, Object> paraMap) {
        return mapper.getMetalDetctHstrCount(paraMap);
    }
}
