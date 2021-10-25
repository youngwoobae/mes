package daedan.mes.qc.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.qc.domain.MatrIwhChk;
import daedan.mes.qc.domain.MatrIwhDoc;
import daedan.mes.qc.mapper.QcMapper;
import daedan.mes.qc.repository.MatrIwhChkRepository;
import daedan.mes.qc.repository.MatrIwhDocRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
        MatrIwhChk chkvo = matrIwhChkRepo.findByCustNoAndChkTpAndChkMthAndUsedYn(custNo,vo.getChkTp(), vo.getChkMth(), "Y");
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
        matrIwhChkRepo.save(vo);

        mapper.initMatrIwhDoc(); //Doc사용여부 초기화

        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("docList");
        MatrIwhDoc docvo = null;
        for (Map<String, Object> el : ds) {
            docvo = new MatrIwhDoc();
            docvo.setDocNo(Long.parseLong(el.get("docNo").toString()));
            MatrIwhDoc docChkvo = matrIwhDocRepo.findByDocNoAndUsedYn(docvo.getDocNo(), "Y");
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
}
