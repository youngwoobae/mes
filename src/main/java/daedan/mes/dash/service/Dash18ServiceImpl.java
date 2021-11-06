package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash18Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service("dash18")
public class Dash18ServiceImpl implements Dash18Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash18Mapper mapper;


    @Transactional
    @Override
    public List<Map<String, Object>> getTmpr18List(Map<String, Object> paraMap) {
        String tag = "Dash18Service.getTmpr18List => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getTmpr18List(paraMap);
    }
}
