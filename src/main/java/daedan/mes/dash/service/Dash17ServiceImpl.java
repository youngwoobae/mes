package daedan.mes.dash.service;

import daedan.mes.dash.mapper.Dash17Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("dash17Service")
public class Dash17ServiceImpl implements Dash17Service{
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash17Mapper mapper;


    @Override
    public List<Map<String, Object>> getTmpr17List(Map<String, Object> paraMap) {
        String tag = "Dash17Service.getTmpr10List => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getTmpr17List(paraMap);
    }

    @Override
    public Map<String, Object> getFinalProcCnt(Map<String, Object> paraMap) {
        String tag = "Dash17Service.getFinalProcCnt => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getFinalProcCnt(paraMap);
    }
}
