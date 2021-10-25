package daedan.mes.dash.service.Yyjg;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.dash.mapper.yyjg.YyjgDashMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("YyjgDashService")
public class YyjgDashServiceImpl implements YyjgDashService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private YyjgDashMapper mapper;


    @Override
    public Map<String, Object> getYyjgPhothCounter(Map<String, Object> paraMap) {
        String tag = "DdKorDashService.getYyjgPhothCounter=> ";
        log.info(tag + " in time = " + DateUtils.getCurrentBaseDateString());

        return mapper.getYyjgPhothCounter(paraMap);
    }

    @Override
    public List<Map<String, Object>> getYyjgSpotList(Map<String, Object> paraMap) {
        String tag = "vsvc.DashService.getYyjgMatrIoList=>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getYyjgSpotList(paraMap);
    }

}
