package daedan.mes.ord.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdInfo18;
import daedan.mes.ord.mapper.Ord18Mapper;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.ord.repository.Ord18Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cust18Service")
public class Ord18ServiceImpl implements Ord18Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private Ord18Repository ord18Repo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private Ord18Mapper mapper;
    @Override
    public Map<String, Object> getOrd18Info(Map<String, Object> paraMap) {
        String tag = "Cust18Service.getOrd18Info => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ordMhNo = Long.parseLong(paraMap.get("ordMhNo").toString());
        OrdInfo18 vo = ord18Repo.findByCustNoAndOrdMhNoAndUsedYn(custNo,ordMhNo,"Y");
        OrdInfo ordvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(vo.getCustNo(),vo.getOrdNo(),"Y");
        Map<String,Object> rmap = new HashMap<String,Object>();
        rmap = StringUtil.voToMap(vo);
        rmap.put("ordDt",ordvo.getOrdDt());
        rmap.put("dlvReqDt",ordvo.getDlvReqDt());
        rmap.put("dlvDt",ordvo.getDlvDt());
        rmap.put("ordSts",ordvo.getOrdSts());
        return rmap;
    }

    @Transactional
    @Override
    public void saveOrdInfo18(Map<String, Object> paraMap) {
        String tag = "Cust18ervice.saveOrd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = (String) paraMap.get("ipaddr");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dlvReqDt = paraMap.get("dlvReqDt").toString().substring(0, 10); //??????????????????

        String dlvDt = null;
        try {
            dlvDt = paraMap.get("dlvDt").toString().substring(0, 10); //????????????

        }
        catch (NullPointerException ne) {

        }
        OrdInfo ordvo = new OrdInfo();
        //???????????? ???????????? ??????
        try {
            ordvo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString())); //???????????????-????????? ???????????????????????? ????????? ???????????? ??????.
        } catch (NullPointerException ne) {
            ordvo.setCmpyNo(0L);
        }
        try {
            ordvo.setCmpyNo(Long.parseLong(paraMap.get("plcNo").toString())); //????????????-????????? ??????????????? ???????????? ????????? ???????????? ??????.
        } catch (NullPointerException ne) {
            ordvo.setPlcNo(0L);
        }

        ordvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        ordvo.setModDt(ordvo.getRegDt());
        ordvo.setModIp(paraMap.get("ipaddr").toString());
        try {
            ordvo.setOrdNm(paraMap.get("ordNm").toString());
        }
        catch (NullPointerException ne) {
            ordvo.setOrdNm(paraMap.get("rcvNm").toString());
        }
        try {
            ordvo.setOrdDt(sdf.parse(paraMap.get("ordDt").toString().substring(0, 10)));
        } catch (ParseException e) {
            ordvo.setOrdDt(DateUtils.getCurrentBaseDateTime());
        }
        try {
            ordvo.setDlvReqDt(sdf.parse(dlvReqDt)); //??????????????????
        } catch (ParseException e) {
            ordvo.setDlvReqDt(ordvo.getOrdDt());
        }
        try {
            ordvo.setDlvDt(sdf.parse(dlvDt)); //????????????
        } catch (ParseException e) {

        }

        try {
            ordvo.setOrdTp(Long.parseLong(paraMap.get("ordTp").toString())); //????????????(OEM,ODM)
        }
        catch (NullPointerException ne) {
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord.prjt"))); //????????????(OEM,ODM,PRJT)
        }
        ordvo.setOrdSts(Long.parseLong(paraMap.get("ordSts").toString())); //????????????
        try {
            ordvo.setOrdPath(Long.parseLong(paraMap.get("ordPath").toString())); //????????????
        } catch (NullPointerException ne) {
            ordvo.setOrdPath(0L);
        }
        try {
            Long ordNo = Long.parseLong(paraMap.get("ordNo").toString());
            OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,ordNo,"Y");
            ordvo.setOrdNo(chkvo.getOrdNo());
            ordvo.setRegId(chkvo.getRegId());
            ordvo.setRegDt(chkvo.getRegDt());
            ordvo.setRegIp(chkvo.getRegIp());
        } catch (NullPointerException ne) { //??????????????????
            ordvo.setOrdNo(0L);
        }
        ordvo.setUsedYn("Y");
        ordvo.setCustNo(custNo);
        ordvo = ordRepo.save(ordvo);

        //???????????? ???????????? ??????
        OrdInfo18 ord18vo = new OrdInfo18();
        ord18vo.setOrdNo(ordvo.getOrdNo());
        ord18vo.setModId(userId);
        ord18vo.setModIp(ipaddr);
        ord18vo.setModDt(DateUtils.getCurrentBaseDateTime());
        ord18vo.setOrdCustNm(paraMap.get("ordCustNm").toString()); //????????????
        ord18vo.setOrdCustCellNo(paraMap.get("ordCustCellNo").toString()); //?????????????????????
        ord18vo.setPkgTp(Long.parseLong(paraMap.get("pkgTp").toString())); //????????????
        ord18vo.setRcvTp(Long.parseLong(paraMap.get("rcvTp").toString())); //?????????
        ord18vo.setRcvHr(paraMap.get("rcvHr").toString()); //????????????
        ord18vo.setRcvMi(paraMap.get("rcvMi").toString()); //?????????
        ord18vo.setRcvNm(paraMap.get("rcvNm").toString()); //????????????
        ord18vo.setRcvCellNo(paraMap.get("rcvCellNo").toString()); //????????????????????????
        ord18vo.setRcvAddr(paraMap.get("rcvAddr").toString()); //???????????????
        ord18vo.setFillRmk(paraMap.get("fillRmk").toString()); //??????
        ord18vo.setSheetRmk(paraMap.get("sheetRmk").toString()); //??????
        ord18vo.setTxtRmk(paraMap.get("txtRmk").toString()); //??????
        try {
            ord18vo.setNoteRmk(paraMap.get("noteRmk").toString()); //??????
        }
        catch (NullPointerException ne) {

        }
        try {
            ord18vo.setOrdMhNo(Long.parseLong(paraMap.get("ordMhNo").toString()));
        }
        catch (NullPointerException ne) {
            ord18vo.setOrdMhNo(0L);
        }
        OrdInfo18 chkInfo = ord18Repo.findByCustNoAndOrdMhNoAndUsedYn(custNo,ord18vo.getOrdMhNo(), "Y");
        if (chkInfo != null) {
            ord18vo.setOrdMhNo(chkInfo.getOrdMhNo());
            ord18vo.setRegId(chkInfo.getRegId());
            ord18vo.setRegIp(chkInfo.getRegIp());
            ord18vo.setRegDt(chkInfo.getRegDt());
        } else {
            ord18vo.setRegId(userId);
            ord18vo.setRegIp(ipaddr);
            ord18vo.setRegDt(DateUtils.getCurrentBaseDateTime());
        }
        ord18vo.setCustNo(custNo);
        ord18vo.setUsedYn("Y");
        ord18vo.setCustNo(custNo);
        ord18Repo.save(ord18vo);
     }

    @Override
    public List<Map<String, Object>> getOrd18List(Map<String, Object> paraMap) {
        String tag = "Cust18ervice.saveOrd => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        return mapper.getOrd18List(paraMap);
    }

    @Override
    public int getOrd18ListCount(Map<String, Object> paraMap) {
        return mapper.getOrd18ListCount(paraMap);
    }

    @Override
    public void dropOrdInfo18(Map<String, Object> paraMap) {
        String tag = "Cust18ervice.dropOrdInfo18 => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ordMhNo = Long.parseLong(paraMap.get("ordMhNo").toString());
        OrdInfo18 chk18 = ord18Repo.findByCustNoAndOrdMhNoAndUsedYn(custNo,ordMhNo,"Y");
        if (chk18 != null) {
            chk18.setUsedYn("N");
            chk18.setModDt(DateUtils.getCurrentBaseDateTime());
            chk18.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chk18.setModIp(paraMap.get("ipaddr").toString());
            ord18Repo.save(chk18);

            OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,chk18.getOrdNo(),"Y");
            if (chkvo != null) {
                chkvo.setUsedYn("N");
                chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
                chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                chkvo.setModIp(paraMap.get("ipaddr").toString());
                ordRepo.save(chkvo);
            }
        }
    }
}
