package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash19Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("dash19")
public class Dash19ServiceImpl implements Dash19Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash19Mapper mapper;

    @Override
    public List<Map<String, Object>> getTmpr19List(Map<String, Object> paraMap) {
        String tag = "Dash19Service.getTmpr19List => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getTmpr19List(paraMap);
    }
}
