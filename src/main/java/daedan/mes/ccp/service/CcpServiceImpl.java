package daedan.mes.ccp.service;

import daedan.mes.ccp.domain.HeatLmtInfo;
import daedan.mes.ccp.repository.HeatLmtInfoRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.ccp.domain.CcpGuide;
import daedan.mes.ccp.domain.CcpHstr;
import daedan.mes.ccp.mapper.CcpMapper;
import daedan.mes.ccp.repository.CcpGuideRepository;
import daedan.mes.ccp.repository.CcpHstrRepository;
import daedan.mes.proc.repository.ProcInfoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ccpService")
public class CcpServiceImpl implements CcpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CcpMapper mapper;

    @Autowired
    private CcpGuideRepository ccpGuideRepo;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private CcpHstrRepository ccpHstrRepo;

    @Autowired
    private HeatLmtInfoRepository heatLmtInfoRepo;

    @Autowired
    private ProcInfoRepository procInfoRepo;

    @Override
    public List<Map<String, Object>> getGuideList(HashMap<String, Object> paraMap) {
        return mapper.getGuideList(paraMap);
    }

    @Override
    public int getGuideListCount(HashMap<String, Object> paraMap) {
        return mapper.getGuideListCount(paraMap);
    }

    @Override
    public Map<String, Object> getGuideInfo(HashMap<String, Object> paraMap) {
        CcpGuide vo =  ccpGuideRepo.findByCcpNo(Long.parseLong(paraMap.get("ccpNo").toString()));
        return StringUtil.voToMap(vo);
    }

    @Override
    public void saveGuide(HashMap<String, Object> paraMap) {
        CcpGuide hgvo = new CcpGuide();
        hgvo.setCcpCd(paraMap.get("ccp_cd").toString());
        hgvo.setProcCd(Long.valueOf(paraMap.get("proc_cd").toString()));
        hgvo.setEquipSense(paraMap.get("equip_sense").toString());
        hgvo.setProdSense(paraMap.get("prod_sense").toString());
        hgvo.setLmtBase(paraMap.get("lmt_base").toString());
        hgvo.setPassBase(paraMap.get("pass_base").toString());
        hgvo.setMeasTp(Long.valueOf(paraMap.get("meas_tp").toString()));
        hgvo.setUsedYn("Y");

        CcpGuide chkvo = ccpGuideRepo.findByProcCdAndCcpCdAndUsedYn(hgvo.getProcCd(), hgvo.getCcpCd(), "Y");
        if (chkvo != null) {
            hgvo.setCcpNo(chkvo.getCcpNo());
        }
        else {
            hgvo.setCcpNo(0L);
        }
        ccpGuideRepo.save(hgvo);
    }

    @Override
    public void dropGuide(HashMap<String, Object> paraMap) {
        mapper.dropGuide(paraMap);
    }



    @Override
    public void ccpHstrSave(HashMap<String, Object> paraMap) {
        CcpHstr hgvo = new CcpHstr();
        log.info(paraMap + "!QT!@$!#!#!@#");
        hgvo.setCcpNo(Long.parseLong(paraMap.get("ccp_no").toString()));
        CcpHstr chkvo = ccpHstrRepo.findByCcpNo(hgvo.getCcpNo());

        try{
            hgvo.setAbnCtnt(paraMap.get("abn_ctnt").toString());
        }catch (Exception e){}

        try{
            hgvo.setReactCtnt(paraMap.get("react_ctnt").toString());
        }catch (Exception e){}

        if (chkvo != null){
            hgvo.setCcpHstrNo(Long.valueOf(chkvo.getCcpHstrNo()));
        }else{
            hgvo.setCcpHstrNo(0L);
        }
        log.info(hgvo + "!QT!@$!#!#!@#2313213");
        ccpHstrRepo.save(hgvo);
    }

    @Override
    public List<Map<String, Object>> getHeatLmtList(HashMap<String, Object> paraMap) {
        String tag = "vsvc.ccpService.getHeatLmtList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getHeatLmtList(paraMap);
    }

    @Override
    public int getHeatLmtListCount(HashMap<String, Object> paraMap) {
        return mapper.getHeatLmtListCount(paraMap);
    }

    @Override
    public Map<String, Object> getHeatLmtInfo(Map<String, Object> paraMap) {
        //HeatLmtInfo vo = heatLmtInfoRepo.findByLmtNo(Long.parseLong(paraMap.get("lmtNo").toString()));
        //vo.setCodeInfo(codeRepo.findByCodeNo(vo.getLmtCd()));
        //return StringUtil.voToMap(vo);
        return mapper.getHeatLmtInfo(paraMap);
    }

    @Override
    public void dropHeatLmtInfo(HashMap<String, Object> paraMap) {
        HeatLmtInfo vo = new HeatLmtInfo();
        vo.setLmtNo(Long.parseLong(paraMap.get("lmtNo").toString()));
        vo = heatLmtInfoRepo.findByLmtNo(vo.getLmtNo());
        if (vo != null) {
            vo.setUsedYn("N");
            heatLmtInfoRepo.save(vo);
        }
    }
    @Transactional
    @Override
    public void saveHeatLmtInfo(HashMap<String, Object> paraMap) {
        HeatLmtInfo vo = new HeatLmtInfo();
        try {
            vo.setLmtNo(Long.parseLong(paraMap.get("lmtNo").toString()));
        }
        catch (NullPointerException ne) {
            vo.setLmtNo(0L);
        }
        vo.setUsedYn("Y");
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModDt(DateUtils.getCurrentBaseDateTime());

        vo.setMaxHeat(Float.parseFloat(paraMap.get("maxHeat").toString())); //최대가열온도
        vo.setMinHeat(Float.parseFloat(paraMap.get("minHeat").toString())); //최소가열온도
        vo.setMaxHeatTime(Float.parseFloat(paraMap.get("maxHeatTime").toString())); //최대가열시간
        vo.setMinHeatTime(Float.parseFloat(paraMap.get("minHeatTime").toString())); //최소가열시간
        vo.setHeatTp(Long.parseLong(paraMap.get("heatTp").toString()));
        HeatLmtInfo chkvo = heatLmtInfoRepo.findByHeatTpAndUsedYn(vo.getHeatTp(),"Y");
        if (chkvo != null) {
            vo.setLmtNo(chkvo.getLmtNo());
            vo.setRegDt(chkvo.getRegDt());
            vo.setRegIp(chkvo.getRegIp());
            vo.setRegId(chkvo.getRegId());
        }
        else {
            vo.setLmtNo(0L);
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setRegIp(paraMap.get("ipaddr").toString());
            vo.setRegDt(DateUtils.getCurrentBaseDateTime());
        }
        heatLmtInfoRepo.save(vo);
    }
}
