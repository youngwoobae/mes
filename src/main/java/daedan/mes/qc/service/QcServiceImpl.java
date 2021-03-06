package daedan.mes.qc.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.qc.domain.MatrIwhChk;
import daedan.mes.qc.domain.MatrIwhDoc;
import daedan.mes.qc.domain.ProdOwhChk;
import daedan.mes.qc.domain.ProdOwhDoc;
import daedan.mes.qc.mapper.QcMapper;
import daedan.mes.qc.repository.MatrIwhChkRepository;
import daedan.mes.qc.repository.MatrIwhDocRepository;
import daedan.mes.qc.repository.ProdOwhChkRepository;
import daedan.mes.qc.repository.ProdOwhDocRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service("qcService")
public class QcServiceImpl implements  QcService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private QcMapper mapper;

    @Autowired
    private MatrIwhChkRepository matrIwhChkRepo;

    @Autowired
    private MatrIwhDocRepository matrIwhDocRepo;

    @Autowired
    private ProdOwhChkRepository prodOwhChkRepo;

    @Autowired
    private ProdOwhDocRepository prodOwhDocRepo;

    @Override
    public List<Map<String, Object>> getMatrIwhChkList(Map<String, Object> paraMap) {
        return mapper.getMatrIwhChkList(paraMap);
    }

    @Override
    public int getMatrIwhChkListCount(Map<String, Object> paraMap) {
        return mapper.getMatrIwhChkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwhChkDocList(Map<String, Object> paraMap) {
        return mapper.getMatrIwhChkDocList(paraMap);
    }

    @Override
    public int getMatrIwhChkDocListCount(Map<String, Object> paraMap) {
        return mapper.getMatrIwhDocChkListCount(paraMap);
    }

    @Override
    public Map<String, Object> matrIwhChkInfo(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrIwhChk vo = matrIwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,Long.parseLong(paraMap.get("chkNo").toString()),"Y");
        return StringUtil.voToMap(vo);

    }

    @Override
    @Transactional
    public void saveMatrIwhChk(Map<String, Object> paraMap) {
        String tag = "QcService.saveMatrIwhChk => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        MatrIwhChk vo = new MatrIwhChk();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        vo.setChkTp(Long.parseLong(paraMap.get("chkTp").toString()));
        vo.setChkMth(Long.parseLong(paraMap.get("chkMth").toString()));
        try {
            vo.setChkCont(paraMap.get("chkCont").toString());
        } catch (NullPointerException ne) {

        }
        vo.setUsedYn("Y");
        vo.setModDt(DateUtils.getCurrentDate());
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        try {
            vo.setChkNo(Long.parseLong(paraMap.get("chkNo").toString().toString()));
        }
        catch (NullPointerException ne) {
            vo.setChkNo(0L);
        }
        MatrIwhChk chkvo = matrIwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,vo.getChkNo(), "Y");
        if (chkvo != null) {
            vo.setChkNo(chkvo.getChkNo());
            vo.setRegDt(chkvo.getRegDt());
            vo.setRegId(chkvo.getRegId());
            vo.setRegIp(chkvo.getRegIp());
        } else {
            vo.setChkNo(0L);
            vo.setRegDt(DateUtils.getCurrentDate());
            vo.setRegIp(paraMap.get("ipaddr").toString());
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        vo.setCustNo(custNo);
        matrIwhChkRepo.save(vo);

        mapper.initMatrIwhDoc(); //Doc???????????? ?????????

        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("docList");
        MatrIwhDoc docvo = null;
        for (Map<String, Object> el : ds) {
            docvo = new MatrIwhDoc();
            docvo.setDocNo(Long.parseLong(el.get("docNo").toString()));
            MatrIwhDoc docChkvo = matrIwhDocRepo.findByCustNoAndDocNoAndUsedYn(custNo,docvo.getDocNo(), "Y");
            if (docChkvo != null) {
                docChkvo.setChkYn("Y");
                matrIwhDocRepo.save(docChkvo);
            }
        }
    }
    @Override
    @Transactional
    public void dropMatrIwhChk(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrIwhChk chkvo = matrIwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,Long.parseLong(paraMap.get("chkNo").toString()),"Y");
        if (chkvo != null) {
            chkvo.setChkNo(chkvo.getChkNo());
            chkvo.setUsedYn("N");
            chkvo.setModDt(DateUtils.getCurrentDate());
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            matrIwhChkRepo.save(chkvo);
        }
    }

    @Override
    public List<Map<String, Object>> getProdOwhChkList(Map<String, Object> paraMap) {
        String tag = "QcService.getProdOwhChkList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdOwhChkList(paraMap);
    }

    @Override
    public int getProdOwhChkListCount(Map<String, Object> paraMap) {
        return mapper.getProdOwhChkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdOwhChkDocList(Map<String, Object> paraMap) {
        String tag = "QcService.getProdIwhChkDocList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdOwhChkDocList(paraMap);
    }

    @Override
    public int getProdOwhChkDocListCount(Map<String, Object> paraMap) {
        return mapper.getProdOwhChkDocListCount(paraMap);
    }

    @Override
    public void saveProdOwhChk(Map<String, Object> paraMap) {
        String tag = "QcService.saveProdOwhChk =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long chkNo = 0L;
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        ProdOwhChk vo = new ProdOwhChk();
        vo.setChkTp(Long.parseLong(paraMap.get("chkTp").toString()));
        vo.setChkMth(Long.parseLong(paraMap.get("chkMth").toString()));
        try {
            vo.setChkCont(paraMap.get("chkCont").toString());
        } catch (NullPointerException ne) {

        }
        vo.setUsedYn("Y");
        vo.setModDt(DateUtils.getCurrentDate());
        vo.setModIp(ipaddr);
        vo.setModId(userId);
        try {
            chkNo = Long.parseLong(paraMap.get("chkNo").toString());
        }
        catch (NullPointerException ne) {
            chkNo = 0L;
        }
        ProdOwhChk chkvo = prodOwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,chkNo,"Y");
        if (chkvo != null) {
            vo.setChkNo(chkvo.getChkNo());
            vo.setRegDt(chkvo.getRegDt());
            vo.setRegId(chkvo.getRegId());
            vo.setRegIp(chkvo.getRegIp());
        } else {
            vo.setChkNo(0L);
            vo.setRegDt(DateUtils.getCurrentDate());
            vo.setRegIp(ipaddr);
            vo.setRegId(userId);
        }
        vo.setCustNo(custNo);
        prodOwhChkRepo.save(vo);

        List<ProdOwhDoc> dsOwhDoc = prodOwhDocRepo.findAllByCustNoAndUsedYn(custNo,"Y");
       // update prod_owh_doc set chk_yn = 'N' where used_yn = 'Y' and cust_no = #{custNo}
       // mapper.initProdOwhDoc(); //Doc???????????? ?????????
        for (ProdOwhDoc el : dsOwhDoc) {
            el.setUsedYn("Y");
            el.setChkYn("N");
            el.setModDt(DateUtils.getCurrentBaseDateTime());
            el.setModId(userId);
            el.setModIp(ipaddr);
            prodOwhDocRepo.save(el);
        }
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("docList");
        MatrIwhDoc docvo = null;
        for (Map<String, Object> el : ds) {
            docvo = new MatrIwhDoc();
            docvo.setDocNo(Long.parseLong(el.get("docNo").toString()));
            ProdOwhDoc docChkVo = prodOwhDocRepo.findByCustNoAndDocNoAndUsedYn(custNo,docvo.getDocNo(), "Y");
            if (docChkVo != null) {
                docChkVo.setChkYn("Y");
                prodOwhDocRepo.save(docChkVo);
            }
        }
    }

    @Override
    public void dropProdOwhChk(Map<String, Object> paraMap) {
        String tag = "QcService.dropProdOwhChk =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProdOwhChk chkvo = prodOwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,Long.parseLong(paraMap.get("chkNo").toString()),"Y");
        if (chkvo != null) {
            chkvo.setChkNo(chkvo.getChkNo());
            chkvo.setUsedYn("N");
            chkvo.setModDt(DateUtils.getCurrentDate());
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            prodOwhChkRepo.save(chkvo);
        }
    }

    @Override
    public Map<String, Object> prodOwhChkInfo(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String,Object> rmap = new HashMap<String,Object>();
        ProdOwhChk vo = prodOwhChkRepo.findByCustNoAndChkNoAndUsedYn(custNo,Long.parseLong(paraMap.get("chkNo").toString()),"Y");
        if (vo != null) {
            rmap = StringUtil.voToMap(vo);
        }
        return rmap;
    }
}
