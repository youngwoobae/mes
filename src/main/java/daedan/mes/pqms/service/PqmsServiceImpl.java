package daedan.mes.pqms.service;

import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.pqms.mapper.PqmsMapper;
import daedan.mes.pqms.repository.OrdRecvRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("pqmsService")
public class PqmsServiceImpl implements PqmsService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private OrdRecvRepository ordrecvRepo;

    @Autowired
    private CustInfoRepository custInfoRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private PqmsMapper mapper;



    @Override
    public List<Map<String, Object>> getOrdRecvList(Map<String, Object> paraMap) {
        String tag = "PqmsService.getOrdRecvList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getOrdRecvList(paraMap);
    }

    @Override
    public int getOrdRecvListCount(Map<String, Object> paraMap) {
        return mapper.getOrdRecvListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboOrdSender(Map<String, Object> paraMap) {
        String tag = "PqmsService.getComboOrdSender =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboOrdSender(paraMap);
    }
}