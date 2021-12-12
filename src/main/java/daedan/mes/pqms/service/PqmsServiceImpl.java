package daedan.mes.pqms.service;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.pqms.domain.OrdRecv;
import daedan.mes.pqms.mapper.PqmsMapper;
import daedan.mes.pqms.repository.OrdRecvRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.user.domain.CustInfo;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
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

    @Transactional
    @Override
    public Map<String, Object> syncOrdPlan(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnOrdPlan =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long recvAcpt = Long.parseLong(env.getProperty("code.ord.recv_acpt"));
        String sendCmpyNo = paraMap.get("sendCmpyNo").toString();
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<String,Object>();

        int totalSz = 0; //수신데이터에 기록된 리스트 카운터
        int apndQty = 0; //추가 카운터
        int updtQty = 0; //변경 카운터
        int recvQty = 0; //수신 카운터
        int readCnt = 0; //리스트 read 카운터
        int rsltCd= 0; //처리결과코드
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sendCmpyNo,"Y");

        totalSz  = Integer.parseInt(paraMap.get("totalSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            OrdRecv orvo = new OrdRecv();
            orvo.setCustNo(custNo);
            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "발송거래처의 사업자번호에 오류가 있습니다.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                orvo.setCmpyNo(civo.getCmpyNo()); //발송처거래처번호 (ex:풀무원)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송거래처의 사업자번호에 오류가 있습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                ProdInfo pivo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo,(el.get("prodCode").toString()),"Y");
                if (pivo == null) {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rowNo", readCnt);
                    ermap.put("resn", "제품정보가 없습니다.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                orvo.setProdNo(pivo.getProdNo());

            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rowNo", readCnt);
                ermap.put("resn", "제품보에 오류가 있습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            orvo.setReqUt(Long.parseLong(el.get("reqDt").toString())); //요청일시
            try {
                orvo.setReqQty(Integer.parseInt(el.get("reqQty").toString())); //요청수량
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rowNo", readCnt);
                ermap.put("resn", "수량정보에 오류가 있습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            orvo.setSendUt(sendDt); //발송처에서 데이터를 발송한 일자
            orvo.setProcSts(recvAcpt); //수신상태
            orvo.setUsedYn("Y");
            orvo.setModDt(DateUtils.getCurrentBaseDateTime());
            orvo.setModIp(ipaddr);
            orvo.setModId(userId);

            OrdRecv chkvo = ordrecvRepo.findByCustNoAndSendUtAndReqUtAndProdNoAndUsedYn(custNo, sendDt, orvo.getReqUt(), orvo.getProdNo(), "Y");
            if (chkvo != null) {
                orvo.setOrdRecvNo(chkvo.getOrdRecvNo());
                orvo.setRegDt(chkvo.getRegDt());
                orvo.setRegId(chkvo.getRegId());
                orvo.setRegIp(chkvo.getRegIp());
                updtQty++;
            }
            else {
                orvo.setOrdRecvNo(0L);
                orvo.setRegDt(orvo.getModDt());
                orvo.setRegId(orvo.getModId());
                orvo.setRegIp(orvo.getModIp());
                apndQty++;
            }
            ordrecvRepo.save(orvo);
            recvQty++;
        }
        rmap.put("send", totalSz);
        rmap.put("recv", recvQty);
        rmap.put("apnd", apndQty);
        rmap.put("updt", updtQty);
        rmap.put("rsltCd",rsltCd);
        if (rsltCd  == -1) {
            rmap.put("errList", errList);
        }
        return rmap;
    }

    @Override
    public UserInfo getUserInfoBySaupNo(String saupNo) {
        UserInfo uvo = null;
        CustInfo cvo = custInfoRepo.findBySaupNoAndUsedYn(saupNo,"Y");
        if (cvo != null) {
            uvo = userRepo.findByMailAddrAndUsedYn(cvo.getAutoSignId(),"Y");
        }
        return uvo;
    }

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