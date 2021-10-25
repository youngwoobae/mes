package daedan.mes.pms.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.pms.domain.PmsOrd;
import daedan.mes.pms.domain.PmsWho;
import daedan.mes.pms.mapper.PmsMapper;
import daedan.mes.pms.repository.PmsOrdRepository;
import daedan.mes.pms.repository.PmsWhoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pmsService")
public class PmsServiceImpl implements PmsService{
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private PmsOrdRepository pmsOrdrRepo;

    @Autowired
    private PmsWhoRepository pmsWhoRepo;

    @Autowired
    private PmsMapper mapper;

    @Override
    public Map<String, Object> getPmsOrdInfo(Map<String, Object> paraMap) {
        String tag = "pmsService.getPmsOrdInfo => ";
        log.info(tag + "params = " + paraMap.toString());
        PmsOrd vo = pmsOrdrRepo.findByOrdNoAndUsedYn(Long.parseLong(paraMap.get("ord_no").toString()),"Y");
        Map<String,Object> rmap = new HashMap<String,Object>();
        if (vo != null) {
            rmap = StringUtil.voToMap(vo);
        }
        return rmap;
    }

    @Override
    public List<Map<String, Object>> getComboPmsProcStat(Map<String, Object> paraMap){
        return mapper.getComboPmsProcStat(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPmsOrdrList(Map<String, Object> paraMap) {
        String tag = "vsvc.pmsService.getPmsOrdrList => ";
        log.info(tag + "params = " + paraMap.toString());
        return mapper.getPmsOrdrList(paraMap);
    }

    @Override
    public int getPmsOrdrListCount(Map<String, Object> paraMap) {
        return mapper.getPmsOrdrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPmsOrdInfoList(Map<String, Object> paraMap) {
        return mapper.getPmsOrdInfoList(paraMap);
    }

    @Override
    public void savePmsOrdr(Map<String, Object> paraMap) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PmsOrd vo = new PmsOrd();
        try {
            vo.setIndcDt(sdf.parse(paraMap.get("indc_dt").toString().substring(0,10)));
            vo.setClosDt(sdf.parse(paraMap.get("clos_dt").toString().substring(0,10)));
            try {
                vo.setFileNo(Long.parseLong(paraMap.get("file_no").toString()));
            }
            catch(NullPointerException ne) {
                vo.setFileNo(0L);
            }
            vo.setOrdNm(paraMap.get("ord_nm").toString());
            try {
                vo.setProcStat(Long.parseLong(paraMap.get("proc_stat").toString()));
            }
            catch (NullPointerException ne) {
                vo.setProcStat(Long.parseLong(env.getProperty("code.pmsStat.ing")));
            }

            try{
                vo.setOrdNo(Long.parseLong(paraMap.get("ord_no").toString()));
            }catch(NullPointerException ne){
                vo.setOrdNo(0L);
            }

            vo.setReqId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setPmsCont(paraMap.get("pms_cont").toString());

            vo.setUsedYn("Y");
            try {
                PmsOrd chkvo = pmsOrdrRepo.findByOrdNoAndUsedYn(vo.getOrdNo(),"Y");
                if (chkvo != null) {
                    vo.setOrdNo(chkvo.getOrdNo());
                    vo.setModDt(chkvo.getModDt());
                    vo.setModId(chkvo.getModId());
                    vo.setModIp(chkvo.getModIp());
                }
                else {
                    vo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    vo.setRegIp(paraMap.get("ipaddr").toString());
                }
                pmsOrdrRepo.save(vo);
            }
            catch (NullPointerException ne) {
                ne.printStackTrace();
                throw new RuntimeException(ne.getMessage());	// RuntimeException을 강제로 발생시킨다.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropPmsOrdr(Map<String, Object> paraMap) {
        PmsOrd vo = pmsOrdrRepo.findByOrdNoAndUsedYn(Long.parseLong(paraMap.get("ord_no").toString()),"Y");
        if (vo != null) {
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentBaseDateTime());
            vo.setModId(Long.parseLong(paraMap.get("user_id").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());
            pmsOrdrRepo.save(vo);
        }
    }

    @Override
    public List<Map<String, Object>> getPmsWhoList(Map<String, Object> paraMap){
        return mapper.getPmsWhoList(paraMap);
    }

    @Override
    public void savePmsWhom(Map<String, Object> paraMap){
        PmsWho pwvo = new PmsWho();
        List<Map<String, Object>> pwvoList = (List<Map<String, Object>>) paraMap.get("userList");

        for (Map<String,Object> el : pwvoList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            pwvo.setRecvId(Long.parseLong(el.get("recv_id").toString()));
            pwvo.setUsedYn("Y");

            try{
                pwvo.setPmsWhoNo(Long.parseLong(el.get("pms_who_no").toString()));
            }catch(NullPointerException ne){
                pwvo.setPmsWhoNo(0L);
            }

            try{
                pwvo.setViewDt(sdf.parse(el.get("view_dt").toString().substring(0, 10)));
                pwvo.setCmfmDt(sdf.parse(el.get("cnfm_dt").toString().substring(0, 10)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            pwvo.setOrdNo(Long.parseLong(el.get("ord_no").toString()));
            pwvo.setProcStat(Long.parseLong(el.get("proc_stat").toString()));
            pwvo.setProcCont(el.get("proc_cont").toString());

            PmsWho chkvo = pmsWhoRepo.findByPmsWhoNoAndUsedYn(pwvo.getPmsWhoNo(), pwvo.getUsedYn());
            if(chkvo != null){
                pwvo.setModDt(DateUtils.getCurrentDate());
                pwvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                pwvo.setModIp(paraMap.get("ipaddr").toString());
            }
            else{
                pwvo.setRegDt(DateUtils.getCurrentDate());
                pwvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                pwvo.setRegIp(paraMap.get("ipaddr").toString());
            }

            pmsWhoRepo.save(pwvo);
        }
    }

    @Override
    public void dropPmsWhom(Map<String, Object> paraMap){
        PmsWho pwvo = pmsWhoRepo.findByPmsWhoNoAndUsedYn(Long.parseLong(paraMap.get("pms_who_no").toString()), "Y");
        if(pwvo != null){
            pwvo.setUsedYn("N");
            pwvo.setModDt(DateUtils.getCurrentDate());
            pwvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            pwvo.setModIp(paraMap.get("ipaddr").toString());

            pmsWhoRepo.save(pwvo);
        }
    }
}
