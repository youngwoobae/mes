package daedan.mes.pumapi.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.pqms.domain.OrdRecv;
import daedan.mes.pqms.repository.OrdRecvRepository;
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.user.domain.CustInfo;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.domain.UserType;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pumApiService")
public class PumApiServiceImpl implements PumApiService {

    private final Log log = LogFactory.getLog(this.getClass());

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
    private MatrRepository matrRepo;

    @Autowired
    private ProdStkRepository prodStkRepo;

    @Autowired
    private ProdBomRepository prodBomRepo;


    @Autowired
    private OrdRecvRepository ordRecvRepo;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private ProdOwhRepository prodOwhRepo;

    @Transactional
    @Override
    public Map<String, Object> syncUser(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnProc =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //???????????? ???????????????(?????????)
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<>();
        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty =0; //?????? ?????????
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        StringBuffer buf = new StringBuffer();
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;

        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
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
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ????????? ????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setSendUt(sendDt);
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
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
                ermap.put("resn", "????????? ID??? ?????? ??? ????????????.");
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
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                uivo.setUserNm(el.get("userNm").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
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
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                String cellNo = el.get("cellNo").toString();
                uivo.setCellNo(cmmnService.encryptStr(custNo,cellNo)); //????????????(???????????????)
            }
            catch (NullPointerException ne) {
            }
            uivo.setUserTp(UserType.valueOf("USER")); /*??????????????? ?????????,?????????,?????????*/
            try {
                uivo.setMatrInspYn(el.get("matrInspYn").toString());
            }
            catch (NullPointerException ne) {
                uivo.setMatrInspYn("N");
            }
            try {
                uivo.setProdInspYn(el.get("prodInspYn").toString());
            }
            catch (NullPointerException ne) {
                uivo.setProdInspYn("N");
            }

            try {
                uivo.setOcpnKind(Long.valueOf(el.get("ocpnKind").toString()));
            }
            catch (NullPointerException ne) {
                uivo.setOcpnKind(Long.valueOf(env.getProperty("ocpn_kind_white")));
            }
            catch (NumberFormatException ne) {
                uivo.setOcpnKind(Long.valueOf(env.getProperty("ocpn_kind_white")));
            }

            try {
                uivo.setEmplKind(Long.valueOf(el.get("emplKind").toString()));
            }
            catch (NullPointerException  ne) {
                uivo.setEmplKind(Long.valueOf(env.getProperty("empl_kind_domestic")));
            }
            catch (NumberFormatException ne) {
                uivo.setEmplKind(Long.valueOf(env.getProperty("empl_kind_domestic")));
            }

            uivo.setModDt(DateUtils.getCurrentBaseDateTime());
            uivo.setModId(userId);
            uivo.setModIp(ipaddr);
            UserInfo chkvo = userRepo.findByMailAddrAndUsedYn(uivo.getMailAddr(),"Y");
            if (chkvo != null) {
                uivo.setUserId(chkvo.getUserId());
                uivo.setMatrInspYn(chkvo.getMatrInspYn());
                uivo.setProdInspYn(chkvo.getMatrInspYn());
                uivo.setRegDt(chkvo.getRegDt());
                uivo.setRegId(chkvo.getRegId());
                uivo.setRegIp(chkvo.getRegIp());
                updtQty++; //?????? ?????????
            }
            else {
                uivo.setUserId(0L);
                uivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                uivo.setRegId(userId);
                uivo.setRegIp(ipaddr);
                apndQty++; //?????? ?????????
            }
            uivo.setUsedYn("Y");
            userRepo.save(uivo);
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
    public Map<String, Object> syncMatr(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnMatr =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //???????????? ???????????????(?????????)
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<String,Object>();
        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty =0; //?????? ?????????
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        StringBuffer buf = new StringBuffer();
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;

        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
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
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ????????? ????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                mivo.setSendUt(sendDt);
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                mivo.setItemCd(el.get("itemCd").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "???????????? ????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                mivo.setMatrNm(el.get("matrNm").toString());
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                 mivo.setMatrTp(Long.parseLong(el.get("matrTp").toString()));
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                mivo.setSaveTmpr(Long.parseLong(el.get("saveTmpr").toString()));
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "???????????? ????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                mivo.setVol(Float.parseFloat(el.get("vol").toString()));
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                mivo.setSz(el.get("sz").toString());
            }
            catch (NullPointerException ne) {

            }
            try {
                mivo.setValidTerm(Long.parseLong(el.get("validTerm").toString()));
            }
            catch (NullPointerException ne) {
                mivo.setValidTerm(12L);
            }
            try {
                mivo.setBrnchNo(Long.parseLong(el.get("brnchNo").toString()));
            }
            catch (NullPointerException ne) {
                mivo.setBrnchNo(0L);
            }
            mivo.setModDt(DateUtils.getCurrentBaseDateTime());
            mivo.setModId(userId);
            mivo.setModIp(ipaddr);
            MatrInfo chkvo = matrRepo.findByCustNoAndItemCdAndUsedYn(custNo, mivo.getItemCd(),"Y");
            if (chkvo != null) {
                mivo.setRegId(chkvo.getRegId());
                mivo.setRegDt(chkvo.getRegDt());
                mivo.setRegIp(chkvo.getRegIp());
                updtQty++; //?????? ?????????
            }
            else {
                mivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mivo.setRegId(userId);
                mivo.setRegIp(ipaddr);
                apndQty++; //?????? ?????????
            }
            mivo.setUsedYn("Y");
            mivo.setCustNo(custNo);
            matrRepo.save(mivo);
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
    public Map<String, Object> syncProd(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnProc =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //???????????? ???????????????(?????????)
        String ipaddr = paraMap.get("ipaddr").toString();
        Long oem = Long.valueOf(env.getProperty("ord.oem"));
        Long odm = Long.valueOf(env.getProperty("ord.odm"));
        Long baseSaveTmpr = Long.valueOf(env.getProperty("code.base.save_tmpr_cd"));

        Map<String,Object> rmap = new HashMap<String,Object>();

        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty = 0;
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        StringBuffer buf = new StringBuffer();
        Long mngrGbnCd = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CodeInfo cdvo = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            ProdInfo pivo = new ProdInfo();
            pivo.setCustNo(custNo);
            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                pivo.setCmpyNo(civo.getCmpyNo()); //???????????????????????? (ex:?????????)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setProdCode(el.get("prodCode").toString()); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;

            }
            try {
                pivo.setProdNm(el.get("prodNm").toString()); //?????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "???????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setMngrUnit(Long.valueOf(el.get("mngrUnit").toString())); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setSaleUnit(Long.valueOf(el.get("saleUnit").toString())); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setHarfProdYn(el.get("harfProdYn").toString()); //???????????????
                pivo.setBomLvl(2L);
            }
            catch (NullPointerException ne) {
                pivo.setHarfProdYn("N");
                pivo.setBomLvl(1L);
            }

            try {
                pivo.setQtyPerPkg(Integer.valueOf(el.get("qtyPerPkg").toString())); //?????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "???????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setValidTerm(Integer.valueOf(el.get("validTerm").toString())); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setVol(Float.valueOf(el.get("vol").toString())/1000); //????????????(kg??????)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                pivo.setVol(Float.valueOf(el.get("spga").toString())); //??????
            }
            catch (NullPointerException ne) {
                pivo.setSpga(1f);
            }
            try {
                String szSaveTmpr = el.get("saveTmpr").toString();
                cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseSaveTmpr, szSaveTmpr, "Y");
                pivo.setSaveTmpr(cdvo.getCodeNo()); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try { //OEM??????
                String cmpyNm = el.get("cmpyNm").toString();
                civo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrGbnCd,cmpyNm,"Y");
                pivo.setCmpyNo(civo.getCmpyNo()); //OEM??????
            }
            catch (NullPointerException ne) {
                pivo.setCmpyNo(0L);
            }
            pivo.setProdTp( (pivo.getCmpyNo() == 0L ) ? odm : oem);

            try {
                pivo.setSz(el.get("sz").toString()); //??????
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
                ermap.put("resn", "??????????????? ???????????? ????????????.");
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
                updtQty++; //?????? ?????????
            }
            else {
                pivo.setProdNo(0L);
                pivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                pivo.setRegId(userId);
                pivo.setRegIp(ipaddr);
                apndQty++; //?????? ?????????
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
    public Map<String, Object> syncBom(Map<String, Object> paraMap) {
        String tag = "PqmsService.sycnProc =>";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String sender = paraMap.get("sender").toString(); //???????????? ???????????????(?????????)
        String ipaddr = paraMap.get("ipaddr").toString();

        Map<String,Object> rmap = new HashMap<String,Object>();

        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty = 0;
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        StringBuffer buf = new StringBuffer();
        Long baseUnitBom = Long.parseLong(env.getProperty("code.base.unit_bom"));
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CodeInfo cdvo = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sender,"Y");
        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        Long matrTp = 0L;

        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            ProdBom bomvo = new ProdBom();
            bomvo.setCustNo(custNo);
            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                //bomvo.setCmpyNo(civo.getCmpyNo()); //???????????????????????? (ex:?????????)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                bomvo.setSendUt(sendDt);
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                String prodCode = el.get("prodCode").toString();
                ProdInfo pivo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo,prodCode,"Y");
                if (pivo != null) {
                    bomvo.setProdNo(pivo.getProdNo());
                }
                else {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "??????????????? ???????????? ???????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                String itemCd = el.get("itemCd").toString();
                MatrInfo mivo = matrRepo.findByCustNoAndItemCdAndUsedYn(custNo,itemCd,"Y");
                if (mivo != null) {
                    bomvo.setMatrNo(mivo.getMatrNo());
                    matrTp = mivo.getMatrTp();
                }
                else {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "?????????????????? ???????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;

            }
            try {
                if (matrTp == Long.parseLong(env.getProperty("submatr_cd"))) {
                    bomvo.setNeedQty(Float.valueOf(el.get("needQty").toString()));
                }
                else {
                    bomvo.setConsistRt(Float.valueOf(el.get("consistRt").toString()));
                }
            }
            catch (NumberFormatException ne) {
                String msg = (matrTp == Long.parseLong(env.getProperty("submatr_cd"))) ? "?????????????????? ????????? ????????????" : "????????????????????? ????????? ????????????";
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", msg);
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            catch (NullPointerException ne) {
                String msg = (matrTp == Long.parseLong(env.getProperty("submatr_cd"))) ? "?????????????????? ?????? ??? ????????????" : "????????????????????? ?????? ??? ????????????.";
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", msg);
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                String szUnitCd = el.get("unitCd").toString();
                CodeInfo cvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseUnitBom,szUnitCd,"Y");
                if (cvo != null) {
                    bomvo.setUnitCd(cvo.getCodeNo());
                }
                else {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "BOM ????????? ???????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NumberFormatException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "BOM ????????? ?????? ??? ????????????..");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                bomvo.setPursYn(el.get("pursYn").toString());
            }
            catch (NumberFormatException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????? ?????? ????????? ?????? ??? ????????????..");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            try {
                bomvo.setBomLvl(Long.valueOf(el.get("bomLvl").toString()));
            }
            catch (NumberFormatException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "BOM Level??? ?????? ??? ????????????..");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "BOM Level??? ?????? ??? ????????????..");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            bomvo.setUsedYn("Y");
            bomvo.setModDt(DateUtils.getCurrentBaseDateTime());
            bomvo.setModId(userId);
            bomvo.setModIp(ipaddr);

            ProdBom chkvo = prodBomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(bomvo.getCustNo(), bomvo.getProdNo(), bomvo.getMatrNo(), "Y");
            if (chkvo != null) {
                bomvo.setBomNo(chkvo.getBomNo());
                bomvo.setRegIp(chkvo.getRegIp());
                bomvo.setRegId(chkvo.getRegId());
                bomvo.setRegDt(chkvo.getRegDt());
                updtQty++; //?????? ?????????
            }
            else {
                bomvo.setBomNo(0L);
                bomvo.setRegIp(ipaddr);
                bomvo.setRegId(userId);
                bomvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                apndQty++; //?????? ?????????
            }
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
    public Map<String, Object> syncProdOut(Map<String, Object> paraMap) {
        String tag = "PqmsService.syncProdOut =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long recvAcpt = Long.parseLong(env.getProperty("code.ord.recv_acpt"));
        String sendCmpyNo = paraMap.get("sendCmpyNo").toString();
        String ipaddr = paraMap.get("ipaddr").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> rmap = new HashMap<String,Object>();

        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty = 0; //?????? ?????????
        int recvQty = 0; //?????? ?????????
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sendCmpyNo,"Y");
        Long prodWhNo = 0L;
        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            ProdOwh povo = new ProdOwh();

            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo())) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                povo.setCmpyNo(civo.getCmpyNo()); //???????????????????????? (ex:?????????)
            } catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                povo.setSendUt(sendDt);
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                String prodCd = el.get("prodCode").toString();
                ProdInfo pivo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo,prodCd,"Y");
                if (pivo != null) {
                    povo.setProdNo(pivo.getProdNo());
                    prodWhNo = pivo.getWhNo();
                }
                else {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "??????????????? ???????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                try {
                    povo.setOwhDt(sdf.parse(DateUtils.getTimestampToDate(Long.valueOf(el.get("owhDt").toString()))));
                }
                catch (ParseException e) {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "???????????? ????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                povo.setOwhQty(Float.valueOf(el.get("owhQty").toString()));
            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "??????????????? ?????? ??? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            povo.setCustNo(custNo);
            povo.setUsedYn("Y");
            ProdOwh chkvo = prodOwhRepo.findByCustNoAndCmpyNoAndProdNoAndAndSendUtAndUsedYn(custNo,civo.getCmpyNo(),povo.getProdNo(),povo.getSendUt(),"Y");
            if (chkvo != null) {
                povo.setOwhNo(chkvo.getOwhNo());
                povo.setRegIp(chkvo.getRegIp());
                povo.setRegId(chkvo.getRegId());
                povo.setRegDt(chkvo.getRegDt());
                updtQty++; //?????? ?????????
            }
            else {
                povo.setOwhNo(0L);
                povo.setRegIp(ipaddr);
                povo.setRegId(userId);
                povo.setRegDt(DateUtils.getCurrentBaseDateTime());
                apndQty++; //?????? ?????????
            }
            povo = prodOwhRepo.save(povo);

            ProdStk psvo = new ProdStk();
            psvo.setModDt(DateUtils.getCurrentBaseDateTime());
            psvo.setModIp(ipaddr);
            psvo.setModIp(ipaddr);

            //???????????? adjust
            ProdStk stkChkVo = prodStkRepo.findByCustNoAndProdNoAndUsedYn(custNo,povo.getProdNo(),"Y");
            if (stkChkVo != null) {
                stkChkVo.setWhNo(prodWhNo);
                stkChkVo.setStkQty(stkChkVo.getStkQty() - povo.getOwhQty() );
                if (stkChkVo.getStkQty() < 0) {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "??????????????? ?????????????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                stkChkVo.setModDt(DateUtils.getCurrentBaseDateTime());
                stkChkVo.setModIp(ipaddr);
                stkChkVo.setModIp(ipaddr);
                prodStkRepo.save(stkChkVo);
            }
            else {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "???????????? ????????? ???????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
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

        int totalSz = 0; //?????????????????? ????????? ????????? ?????????
        int apndQty = 0; //?????? ?????????
        int updtQty = 0; //?????? ?????????
        int recvQty = 0; //?????? ?????????
        int readCnt = 0; //????????? read ?????????
        int rsltCd= 0; //??????????????????
        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> ermap = null;
        CmpyInfo civo = cmpyRepo.findByCustNoAndSaupNoAndUsedYn(custNo,sendCmpyNo,"Y");

        totalSz  = Integer.parseInt(paraMap.get("listSize").toString());
        Long sendDt = Long.parseLong(paraMap.get("sendDt").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("list");
        for(Map<String, Object> el : ds) {
            readCnt++;
            OrdRecv orvo = new OrdRecv();
            orvo.setCustNo(custNo);
            try {
                if (!StringUtil.chkSaupNo(civo.getSaupNo()) ) {
                    ermap.put("rownum", readCnt);
                    ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                orvo.setCmpyNo(civo.getCmpyNo()); //???????????????????????? (ex:?????????)
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rownum", readCnt);
                ermap.put("resn", "?????????????????? ?????????????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            try {
                ProdInfo pivo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo,(el.get("prodCode").toString()),"Y");
                if (pivo == null) {
                    ermap = new HashMap<String, Object>();
                    ermap.put("rowNo", readCnt);
                    ermap.put("resn", "??????????????? ????????????.");
                    errList.add(ermap);
                    rsltCd = -1;
                    continue;
                }
                orvo.setProdNo(pivo.getProdNo());

            }
            catch(NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rowNo", readCnt);
                ermap.put("resn", "???????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }

            orvo.setReqUt(Long.parseLong(el.get("reqDt").toString())); //????????????
            try {
                orvo.setReqQty(Integer.parseInt(el.get("reqQty").toString())); //????????????
            }
            catch (NullPointerException ne) {
                ermap = new HashMap<String, Object>();
                ermap.put("rowNo", readCnt);
                ermap.put("resn", "??????????????? ????????? ????????????.");
                errList.add(ermap);
                rsltCd = -1;
                continue;
            }
            orvo.setSendUt(sendDt); //??????????????? ???????????? ????????? ??????
            orvo.setProcSts(recvAcpt); //????????????
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
            uvo.setCustInfo(custInfoRepo.findBySaupNoAndUsedYn(saupNo,"Y"));
        }
        return uvo;
    }


}
