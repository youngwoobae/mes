package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash03Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service("dash10")
public class Dash03ServiceImpl implements  Dash03Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash03Mapper mapper;

    @Transactional
    @Override
    public List<Map<String, Object>> getTmpr03List(Map<String, Object> paraMap) {
        String tag = "Dash03Service.getTmpr03List => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getTmpr03List(paraMap);
    }
}
