package daedan.mes.cmpy.service;

import daedan.mes.cmpy.domain.CmpyDlvPlc;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.mapper.CmpyMapper;
import daedan.mes.cmpy.repository.CmpyDlvPlcRepository;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.matr.domain.MatrCmpy;
import daedan.mes.matr.repository.MatrCmpyRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cmpyService")
public class CmpyServiceImpl implements  CmpyService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private CmpyRepository cmpyRepository;

    @Autowired
    private MatrCmpyRepository matrCmpyRepo;

    @Autowired
    private CmpyDlvPlcRepository cmpyDlvPlcRepository;

    @Autowired
    private CmpyMapper cmpyMapper;

    @Autowired
    FileRepository fileRepo;

    @Override
    public List<Map<String, Object>> getCmpyList(Map<String, Object> map) {
        String tag = "cmpyService.getCmpyList => ";
        /*
        log.info(tag + "mngrGbnCd = " + map.get("mngrGbnCd"));
        log.info(tag + "matrNo = " + map.get("matrNo"));
        log.info(tag + "findTp = " + map.get("findTp"));
        log.info(tag + "findSz = " + map.get("findSz"));
        log.info(tag + "pageSz = " + map.get("pageSz"));
        log.info(tag + "pageNo = " + map.get("pageNo"));
         */
        return  cmpyMapper.getCmpyList(map);
    }

    @Override
    public List<Map<String, Object>> getCmpyExcelList(Map<String, Object> paraMap){
        return cmpyMapper.getCmpyExcelList(paraMap);
    }

    @Override
    public int getCmpyListCount(HashMap<String, Object> map) {
        String tag = "cmpyService.getCmpyList => ";
        log.info(tag + "mngrGbnCd = " + map.get("mngrGbnCd"));
        log.info(tag + "findTp = " + map.get("findTp"));
        log.info(tag + "findSz = " + map.get("findSz"));
        return  cmpyMapper.getCmpyListCount(map);
    }

    @Override
    public Map<String, Object> getCmpyInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.cmpyService.getCmpyInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long cmpyTp = Long.parseLong(paraMap.get("cmpyTp").toString());
        CmpyInfo cmpyvo = cmpyRepository.findByCustNoAndCmpyTpAndCmpyNoAndUsedYn(custNo,cmpyTp,cmpyNo,"Y");
        return  (cmpyvo == null) ? null :  StringUtil.voToMap(cmpyvo);
    }

    @Override
    public List<Map<String, Object>> getCmpyOrdrList(Map<String, Object> map) {
        String tag = "cmpyService.getCmpyOrdrList => ";
        log.info(tag + "cmpyNo = " + map.get("cmpyNo"));
        log.info(tag + "ordTp = " + map.get("ordTp"));
        log.info(tag + "findSz = " + map.get("findSz"));
        log.info(tag + "pageSz = " + map.get("pageSz"));
        log.info(tag + "pageNo = " + map.get("pageNo"));
        return  cmpyMapper.getCmpyOrdrList(map);
    }

    @Override
    public int getCmpyOrdrListCount(HashMap<String, Object> map) {
        String tag = "cmpyService.getCmpyOrdrListCount => ";
        log.info(tag + "cmpyNo = " + map.get("cmpyNo"));
        log.info(tag + "ordTp = " + map.get("ordTp"));
        log.info(tag + "findSz = " + map.get("findSz"));
        return cmpyMapper.getCmpyOrdrListCount(map);
    }

    @Override
    public List<Map<String, Object>> getCmpyPursList(Map<String, Object> map) {
        String tag = "cmpyService.getCmpyPursList => ";
        log.info(tag + "cmpyNo = " + map.get("cmpyNo"));
        log.info(tag + "findSz = " + map.get("findSz"));
        log.info(tag + "pageSz = " + map.get("pageSz"));
        log.info(tag + "pageNo = " + map.get("pageNo"));
        return  cmpyMapper.getCmpyPursList(map);
    }

    @Override
    public int getCmpyPursListCount(HashMap<String, Object> map) {
        String tag = "cmpyService.getCmpyPursList => ";
        log.info(tag + "cmpyNo = " + map.get("cmpyNo"));
        log.info(tag + "findSz = " + map.get("findSz"));
        return  cmpyMapper.getCmpyPursListCount(map);
    }

    @Override
    public void saveCmpy(Map<String, Object> paraMap) {
        String tag = " CmpyService.saveCmpy => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        CmpyInfo cmpyvo = new CmpyInfo();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        cmpyvo.setModDt(DateUtils.getCurrentBaseDateTime());
        cmpyvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        cmpyvo.setModIp((String)paraMap.get("ipaddr"));
        Long cmpyNo = 0L;
         try {
             cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
             CmpyInfo chkvo = cmpyRepository.findByCustNoAndCmpyNoAndUsedYn(custNo,cmpyNo,"Y");
             if (cmpyvo != null) {
                 cmpyvo.setCmpyNo(chkvo.getCmpyNo());
                 cmpyvo.setRegDt(chkvo.getRegDt());
                 cmpyvo.setRegId(chkvo.getRegId());
                 cmpyvo.setRegIp(chkvo.getRegIp());
             }
             else {
                 cmpyvo.setCmpyNo(0L);
                 cmpyvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                 cmpyvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                 cmpyvo.setRegIp((String)paraMap.get("ipaddr"));
             }
        }
        catch (NullPointerException ne) {
            cmpyvo.setCmpyNo(0L);
            cmpyvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            cmpyvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            cmpyvo.setRegIp((String)paraMap.get("ipaddr"));
        }
        try {
            cmpyvo.setFileInfo(fileRepo.findByCustNoAndFileNoAndUsedYn(custNo,Long.parseLong(paraMap.get("fileNo").toString()),"Y"));
            log.info(tag + "fileInfo = " + cmpyvo.getFileInfo().getOrgFileNm());
        } catch (NullPointerException ne) {


        }
        cmpyvo.setMngrGbnCd(Long.parseLong(paraMap.get("mngrGbnCd").toString()));
        cmpyvo.setCmpyNm((String)paraMap.get("cmpyNm"));
        cmpyvo.setCmanNm((String)paraMap.get("cmanNm"));
        cmpyvo.setReprMailAddr((String)paraMap.get("mailAddr"));
        cmpyvo.setSaupNo((String)paraMap.get("saupNo"));
        cmpyvo.setCmpyTp(Long.parseLong(paraMap.get("cmpyTp").toString()));
        cmpyvo.setTelNo((String)paraMap.get("telNo"));
        cmpyvo.setCmanCellNo((String)paraMap.get("cmanCellNo"));
        cmpyvo.setFaxNo((String)paraMap.get("faxNo"));


        cmpyvo.setHp((String)paraMap.get("hp"));
        cmpyvo.setUsedYn("Y");
        cmpyvo.setModDt(cmpyvo.getRegDt());
        cmpyvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        cmpyvo.setModIp((String)paraMap.get("ipaddr"));
        cmpyvo.setCustNo(custNo);
        cmpyRepository.save(cmpyvo);
    }

    @Override
    public void setUnusedCmpy(Map<String, Object> cmpyMap) {
        cmpyMapper.setUnusedCmpy(cmpyMap);
    }

    @Override
    public List<Map<String, Object>> getCmpyDlvPlcList(Map<String, Object> paraMap) {
        return cmpyMapper.getCmpyDlvPlcList(paraMap);
    }

    @Override
    public int getCmpyDlvPlcListCount(Map<String, Object> paraMap) {
        return cmpyMapper.getCmpyDlvPlcListCount(paraMap);
    }
    @Transactional
    @Override
    public void saveDlvPlc(Map<String, Object> passMap) {
        String tag = "cmpyService.saveDlvPlc => ";
        Long custNo = Long.parseLong(passMap.get("custNo").toString());
        CmpyDlvPlc vo = new CmpyDlvPlc();
        try {
            vo.setPlcNo(Long.parseLong(passMap.get("plcNo").toString()));
        }
        catch (NullPointerException ne) {
            vo.setPlcNo(0L);
        }
        log.info(tag + "passMap = " + passMap.toString());
        vo.setCmpyNo(Long.parseLong(passMap.get("cmpyNo").toString()));
        vo.setPlcNm(passMap.get("plcNm").toString());
        vo.setPlcAddr(passMap.get("plcAddr").toString());

        vo.setBasePlcYn(passMap.get("basePlcYn").toString());
        vo.setCustNo(custNo);
        vo.setUsedYn("Y");

        if (vo.getBasePlcYn().equals("Y")) {
            cmpyMapper.initBasePlcYn(passMap);
        }
        cmpyDlvPlcRepository.save(vo);
    }
    @Transactional
    @Override
    public void deleteDlvPlc(Map<String, Object> paraMap) {
        String tag = "cmpyService.dropDlvPlc => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> passMap = new HashMap<String,Object>();
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("dlvPlc");
        for (Map<String,Object> el : ds) {
            passMap.put("modId", paraMap.get("userId"));
            passMap.put("ipaddr", paraMap.get("ipaddr"));
            passMap.put("plcNo", el.get("plcNo"));
            passMap.put("custNo",custNo);
            cmpyMapper.deleteCmpyDlvPlc(passMap);
        }
    }
    @Transactional
    @Override
    public void deleteDlvPlcInfo(Map<String, Object> paraMap) {
        String tag = "cmpyService.dropDlvPlcInfo => ";
        Long plcNo = Long.parseLong(paraMap.get("plcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        //SOL Add By KMJ At 21.10.1
        CmpyDlvPlc vo = cmpyDlvPlcRepository.findByCustNoAndPlcNoAndUsedYn(custNo,plcNo,"Y");
        if (vo != null) {
            vo.setUsedYn("N");
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModDt(DateUtils.getCurrentBaseDateTime());
            vo.setModIp(paraMap.get("ipaddr").toString());
            vo.setCustNo(custNo);
            cmpyDlvPlcRepository.save(vo);
        }
        //EOL Add By KMJ At 21.10.1
        //cmpyMapper.deleteCmpyDlvPlc(paraMap); Remarked By KMJ At 21.10.19

    }

    @Override
    public Map<String, Object> getCmpyPursInfo(Map<String, Object> paraMap) {
        String tag = "cmpyService.getCmpyPursInfo => ";
        return  cmpyMapper.getCmpyPursInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCmpyPursMatrList(Map<String, Object> paraMap) {
        String tag = "cmpyService.getCmpyPursMatrList => ";
        return  cmpyMapper.getCmpyPursMatrList(paraMap);

    }

    @Override
    public List<Map<String, Object>> getCmpyIpList(Map<String, Object> paraMap) {
        return cmpyMapper.getCmpyIpList(paraMap);
    }


    @Override
    public int getCmpyIpListCount(HashMap<String, Object> map) {
        return  cmpyMapper.getCmpyIpListCount(map);
    }
    @Override
    public List<Map<String, Object>> getEqOption(Map<String, Object> paraMap) {
        return cmpyMapper.getEqOption(paraMap);
    }
    @Override
    public List<Map<String, Object>> getSysOption(Map<String, Object> paraMap) {
        return cmpyMapper.getSysOption(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrCmpyList(Map<String, Object> paraMap){
        return cmpyMapper.getMatrCmpyList(paraMap);
    }

    @Override
    public void dropMatrCmpy(Map<String, Object> paraMap){
        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrCmpy chkvo = matrCmpyRepo.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,matrNo, cmpyNo, "Y");
        if(chkvo != null){
            chkvo.setUsedYn("N");
            matrCmpyRepo.save(chkvo);
        }
    }

    @Override
    public Map<String, Object> getDlvPlcInfo(Map<String, Object> paraMap) { //AddOn By KMJ At 21.10.19
        String tag = "cmpyService.getDlvPlcInfo =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String,Object> rmap = new HashMap<String,Object>();
        Long plcNo = Long.parseLong(paraMap.get("plcNo").toString());
        CmpyDlvPlc vo = cmpyDlvPlcRepository.findByCustNoAndPlcNoAndUsedYn(custNo,plcNo,"Y");
        try {
             rmap = StringUtil.voToMap(vo);
        }
        catch (NullPointerException ne) {
        }
        return rmap;
    }
}
