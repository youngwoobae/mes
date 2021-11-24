package daedan.mes.dash.service;

import daedan.mes.common.service.util.StringUtil;
import daedan.mes.dash.mapper.Dash03Mapper;
import daedan.mes.moniter.service.MoniterService;
import daedan.mes.user.domain.AccHstr;
import daedan.mes.user.domain.EvntType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dash10")
public class Dash03ServiceImpl implements  Dash03Service {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private Dash03Mapper mapper;

    @Autowired
    private MoniterService moniterService;

    @Transactional
    @Override
    public Map<String, Object> getTemp03GraphData(Map<String, Object> paraMap) {
        String tag = "Dash03Service.getTemp03List => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        paraMap.put("pageNo", StringUtil.convertPageNo(paraMap));
        Map<String, Object> rmap = new HashMap<String,Object>();
        Map<String, Object> dmap = new HashMap<String,Object>();
        List<Map<String,Object>> ds = mapper.getTmpr03GraphData(paraMap);
        int idx = -1;
        ArrayList<String> rcvTm = new ArrayList<String>();
        ArrayList<Float> rcv1 = new ArrayList<Float>();
        ArrayList<Float> rcv2 = new ArrayList<Float>();
        ArrayList<Float> rcv3 = new ArrayList<Float>();

        //ArrayList<String> spotNm1 = new ArrayList<String>();
        //ArrayList<String> spotNm2 = new ArrayList<String>();
        //ArrayList<String> spotNm3 = new ArrayList<String>();

        while(++idx < ds.size()) {
            dmap = ds.get(idx);
            rcvTm.add(dmap.get("rcvTm").toString());
            rcv1.add(Float.parseFloat(dmap.get("rcv1Val").toString()));
            rcv2.add(Float.parseFloat(dmap.get("rcv2Val").toString()));
            rcv3.add(Float.parseFloat(dmap.get("rcv3Val").toString()));
        }
        rmap.put("griDs",ds);
        rmap.put("rcvTm",rcvTm);
        rmap.put("rcv1", rcv1);
        rmap.put("rcv2", rcv2);
        rmap.put("rcv3", rcv3);
        return rmap;
    }
    @Override
    public List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap) {
        String tag = "dash30Service.getEvtMsgList => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getEvtMsgList(paraMap);
    }

    public List<Map<String, Object>> getMakeRsltDash(Map<String, Object> paraMap) {
        String tag = "dash30Service.getMakeRsltDash => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMakeRsltDash(paraMap);
    }

    public List<Map<String, Object>> getUserGroupDash(Map<String, Object> paraMap) {
        String tag = "dash30Service.getUserGroupDash => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getUserGroupDash(paraMap);
    }

}
