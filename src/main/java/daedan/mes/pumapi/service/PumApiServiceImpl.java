package daedan.mes.pumapi.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.pqms.domain.OrdRecv;
import daedan.mes.pqms.repository.OrdRecvRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.sysmenu.user.domain.CustInfo;
import daedan.mes.sysmenu.user.domain.UserInfo;
import daedan.mes.sysmenu.user.domain.UserType;
import daedan.mes.sysmenu.user.repository.CustInfoRepository;
import daedan.mes.sysmenu.user.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pumApiService")
public class PumApiServiceImpl implements PumApiService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustInfoRepository custInfoRepo;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private OrdRecvRepository ordRecvRepo;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private CmmnService cmmnService;


    @Transactional
    @Override
    public Map<String, Object> syncUser(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnProc =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //전송업체 사업자번호(풀무원)
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<String,Object>();
        int totalSz = 0; //수신데이터에 기록된 리스트 카운터
        int apndQty = 0; //추가 카운터
        int updtQty =0; //수정 카운터
        int readCnt = 0; //리스트 read 카운터
        int rsltCd= 0; //처리결과코드
        StringBuffer buf = new StringBuffer();
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;

        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("totalSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        CustInfo custvo =  custInfoRepo.findBySaupNoAndUsedYn(sender,"Y");
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");

        for(Map<String, Object> el : ds) {
            readCnt++;
            UserInfo uivo = new UserInfo();
            uivo.setCustInfo(custvo);

            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "발송거래처의 사업자번호에 오류가 있습니다.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송거래처의 사업자 번호를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setSendUt(Long.parseLong(el.get("sendDt").toString()));
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송일자를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setErpUserNo(el.get("userId").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "사용자 ID를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setMailAddr(el.get("mailAddr").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "메일주소를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setUserNm(el.get("usertNm").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "사용자명을 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setSecrtNo(BCrypt.hashpw(el.get("passwd").toString(), BCrypt.gensalt()));
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "비밀번호를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                String cellNo = el.get("cellNo").toString();
                uivo.setCellNo(cmmnService.encryptStr(custNo,cellNo)); //이동전화(암호화사용)
            }
            catch (NullPointerException ne) {
            }
            uivo.setUserTp(UserType.valueOf("USER")); /*이용자권한 관리자,사용자,방문객*/

            uivo.setModDt(DateUtils.getCurrentBaseDateTime());
            uivo.setModId(userId);
            uivo.setModIp(ipaddr);
            UserInfo chkvo = userRepo.findByMailAddrAndUsedYn(uivo.getMailAddr(),"Y");
            if (chkvo != null) {
                uivo.setUserId(chkvo.getUserId());
                uivo.setRegDt(chkvo.getRegDt());
                uivo.setRegId(chkvo.getRegId());
                uivo.setRegIp(chkvo.getRegIp());
                updtQty++; //변경 카운터
            }
            else {
                uivo.setUserId(0L);
                uivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                uivo.setRegId(userId);
                uivo.setRegIp(ipaddr);
                apndQty++; //추가 카운터
            }
            uivo.setUsedYn("Y");
            userRepo.save(uivo);
        }
        return rmap;
    }

    @Transactional
    @Override
    public Map<String, Object> syncMatr(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnMatr =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //전송업체 사업자번호(풀무원)
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<String,Object>();
        int totalSz = 0; //수신데이터에 기록된 리스트 카운터
        int apndQty = 0; //추가 카운터
        int updtQty =0; //수정 카운터
        int readCnt = 0; //리스트 read 카운터
        int rsltCd= 0; //처리결과코드
        StringBuffer buf = new StringBuffer();
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;

        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("totalSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        CustInfo custvo =  custInfoRepo.findBySaupNoAndUsedYn(sender,"Y");
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");

        for(Map<String, Object> el : ds) {
            readCnt++;
            MatrInfo mivo = new MatrInfo();
            mivo.setCustNo(custNo);

            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "발송거래처의 사업자번호에 오류가 있습니다.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송거래처의 사업자 번호를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                //mivo.setSendUt(Long.parseLong(el.get("sendDt").toString()));
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송일자를 찾을 수 없습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            /*
            mivo.setModDt(DateUtils.getCurrentBaseDateTime());
            mivo.setModId(userId);
            mivo.setModIp(ipaddr);
            MatrInfo chkvo = matrRepo.findByCustNoAndMatrCdAndUsedYn(custNo, mivo.getMatrCd(),"Y");
            if (chkvo != null) {
                uivo.setUserId(chkvo.getUserId());
                uivo.setRegDt(chkvo.getRegDt());
                uivo.setRegId(chkvo.getRegId());
                uivo.setRegIp(chkvo.getRegIp());
                updtQty++; //변경 카운터
            }
            else {
                uivo.setUserId(0L);
                uivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                uivo.setRegId(userId);
                uivo.setRegIp(ipaddr);
                apndQty++; //추가 카운터
            }
            uivo.setUsedYn("Y");
            userRepo.save(uivo);
             */
        }
        return rmap;
    }

    @Transactional
    @Override
    public Map<String, Object> syncProd(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnProc =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //전송업체 사업자번호(풀무원)
        String ipaddr = paraMap.get("ipaddr").toString();
        Long oem = Long.valueOf(env.getProperty("ord.oem"));
        Long odm = Long.valueOf(env.getProperty("ord.odm"));
        Long baseSaveTmpr = Long.valueOf(env.getProperty("code.base.save_tmpr_cd"));

        Map<String,Object> rmap = new HashMap<String,Object>();

        int totalSz = 0; //수신데이터에 기록된 리스트 카운터
        int apndQty = 0; //추가 카운터
        int updtQty = 0;
        int readCnt = 0; //리스트 read 카운터
        int rsltCd= 0; //처리결과코드
        StringBuffer buf = new StringBuffer();
        Long mngrGbnCd = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CodeInfo cdvo = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("totalSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            ProdInfo pivo = new ProdInfo();
            pivo.setCustNo(custNo);
            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "발송거래처의 사업자번호에 오류가 있습니다.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                pivo.setCmpyNo(civo.getCmpyNo()); //발송처거래처번호 (ex:풀무원)
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
                pivo.setProdCode(el.get("prodCode").toString()); //제품코드
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "제품코드가 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;

            }
            try {
                pivo.setProdNm(el.get("prodNm").toString()); //제품명
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "제품명이 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setMngrUnit(Long.valueOf(el.get("mngrUnit").toString())); //관리단위
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "관리단위가 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setSaleUnit(Long.valueOf(el.get("saleUnit").toString())); //관리단위
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "판매단위가 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setHarfProdYn(el.get("harfProdYn").toString()); //반제품여부
                pivo.setBomLvl(2L);
            }
            catch (NullPointerException ne) {
                pivo.setHarfProdYn("N");
                pivo.setBomLvl(1L);
            }

            try {
                pivo.setQtyPerPkg(Integer.valueOf(el.get("qtyPerPkg").toString())); //입수량
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "입수량이 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setValidTerm(Integer.valueOf(el.get("validTerm").toString())); //유효기간
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "유효기간이 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setVol(Float.valueOf(el.get("vol").toString())/1000); //단위중량(kg단위)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "단위중량이 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setVol(Float.valueOf(el.get("spga").toString())); //비중
            }
            catch (NullPointerException ne) {
                pivo.setSpga(1f);
            }
            try {
                String szSaveTmpr = el.get("saveTmpr").toString();
                cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseSaveTmpr, szSaveTmpr, "Y");
                pivo.setSaveTmpr(cdvo.getCodeNo()); //보관온도
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "보관온도가 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try { //OEM업체
                String cmpyNm = el.get("cmpyNm").toString();
                civo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrGbnCd,cmpyNm,"Y");
                pivo.setCmpyNo(civo.getCmpyNo()); //OEM업체
            }
            catch (NullPointerException ne) {
                pivo.setCmpyNo(0L);
            }
            pivo.setProdTp( (pivo.getCmpyNo() == 0L ) ? odm : oem);

            try {
                pivo.setSz(el.get("sz").toString()); //규격
            }
            catch (NullPointerException ne) {
                buf.setLength(0);
                buf.append(Float.toString(pivo.getVol())).append(" * ").append(Integer.toString(pivo.getQtyPerPkg()));
            }
            try {
                pivo.setSendUt(Long.parseLong(paraMap.get("sendDt").toString()));
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "발송일시가 존재하지 않습니다.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            pivo.setModDt(DateUtils.getCurrentBaseDateTime());
            pivo.setModId(userId);
            pivo.setModIp(ipaddr);
            ProdInfo chkvo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo,(el.get("prodCode").toString()),"Y");
            if (chkvo != null) {
                pivo.setProdNo(chkvo.getProdNo());
                pivo.setRegDt(chkvo.getRegDt());
                pivo.setRegId(chkvo.getRegId());
                pivo.setRegIp(chkvo.getRegIp());
                updtQty++; //변경 카운터
            }
            else {
                pivo.setProdNo(0L);
                pivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                pivo.setRegId(userId);
                pivo.setRegIp(ipaddr);
                apndQty++; //추가 카운터
            }
            pivo.setCustNo(custNo);
            pivo.setUsedYn("Y");
            prodRepo.save(pivo);
        }

        rmap.put("send", totalSz);
        rmap.put("recv", readCnt);
        rmap.put("apnd", apndQty);
        rmap.put("updt", updtQty);
        rmap.put("rsltCd",rsltCd);
        if (rsltCd  == -1) {
            rmap.put("errList", errList);
        }
        return rmap;
    }

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

            OrdRecv chkvo = ordRecvRepo.findByCustNoAndSendUtAndReqUtAndProdNoAndUsedYn(custNo, sendDt, orvo.getReqUt(), orvo.getProdNo(), "Y");
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
            ordRecvRepo.save(orvo);
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

}
