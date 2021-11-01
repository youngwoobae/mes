package daedan.mes.dash.service.mh;

import daedan.mes.dash.mapper.mh.MhDashMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service("mhDashService")
public class MhDashServiceImpl implements MhDashService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private MhDashMapper mapper;


    @Transactional
    @Override
    public List<Map<String, Object>> getMhTmprList(Map<String, Object> paraMap) {
        String tag = "MhDashService.getMhTmprList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return  mapper.getMhTmprList(paraMap);
    }
}
