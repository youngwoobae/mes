package daedan.mes.purs.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.domain.EquipBomCart;
import daedan.mes.make.domain.MakeIndcRslt;
import daedan.mes.matr.service.MatrService;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.ord.service.OrdService;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.mapper.PursMapper;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.stock.service.StockService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("pursService")
public class PursServiceImpl implements  PursService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private PursInfoRepository pir;

    @Autowired
    private PursMatrRepository pmr;

    @Autowired
    private PursMapper mapper;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private PursInfoRepository pursInfoRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private StockService stockService;

    @Override
    public void initTempPursMatr(Map<String, Object> paraMap) {
        mapper.initTempPursMatr(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursList(Map<String, Object> paraMap) {
        return mapper.getPursList(paraMap);
    }

    @Override
    public int getPursListCount(Map<String, Object> paraMap) {
        return mapper.getPursListCount(paraMap);
    }

    @Override
    public Map<String, Object> getPursInfo(Map<String, Object> paraMap) {
        String tag = "pursService.getPursInfo";
        long pursNo = Long.parseLong(paraMap.get("pursNo").toString());
        log.info(tag + "pursNo = " + pursNo);
        return mapper.getPursInfo(paraMap);
    }
    @Override
    public Map<String, Object> getPursMatrInfo(Map<String, Object> paraMap) {
        String tag = "pursService.getPursMatrInfo";
        return mapper.getPursMatrInfo(paraMap);
    }

    @Override
    public Map<String,Object> savePursInfoByVo(PursInfo pivo) {
        String tag = "pursService.savePursInfoByVo => ";
        log.info(tag);

        pivo = pir.save(pivo);
        log.info(tag + "pivo to map = " + StringUtil.voToMap(pivo));
        return StringUtil.voToMap(pivo);
    }
    /*구매정보및 구매자재목록 저장 동시처리*/
    @Transactional
    @Override
    public Map<String, Object> savePursInfo(Map<String, Object> paraMap) {
        String tag = "pursService.savePursInfo => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
//        String fileRoot = paraMap.get("fileRoot").toString();
        log.info(tag + "paraMap = " + paraMap.toString());
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        //구매기본정보처리
        PursInfo pivo = new PursInfo();
        //AddOn by KMJ At 21.09.02 21:00 - 생산계획시 소요자재의 준비를 위해 사용되는 예상수율 (대동고려삼 요구사항 수렴)
        try {
            pivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctl_fill_yield").toString()));
        }
        catch (NullPointerException ne) {
            pivo.setCtlFillYield(100f);
        }
        try {
            pivo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
        }
        catch (NullPointerException ne) {
            pivo.setPursNo(0L);
        }

        try {
            pivo.setPursDt(transFormat.parse((String) paraMap.get("pursDt").toString().substring(0, 10)));
        }
        catch (ParseException e) {
            throw new IllegalStateException(env.getProperty("msg_purs_dt_found"));
        }
        catch(NullPointerException ne){
            pivo.setPursDt(DateUtils.getCurrentDate());
        }

        try {
            Date dt = transFormat.parse(paraMap.get("ordDt").toString().substring(0,10));

            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.DATE, 3);
            Date ordDt = cal.getTime();

            pivo.setDlvReqDt(ordDt);
        }
        catch (ParseException e) {
            throw new IllegalStateException(env.getProperty("msg_dlv_req_dt_found"));
        }



        try {
            pivo.setDlvDt(transFormat.parse(paraMap.get("dlvDt").toString().substring(0,10)));
        }
        catch (ParseException e) {
        }
        catch (NullPointerException ne) {

        }
        Long ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        pivo.setOrdNo(ordNo);

        try {
            pivo.setPursSts(Long.parseLong(paraMap.get("pursSts").toString()));
        }
        catch (NullPointerException ne) {
            pivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.insp"))); //구매의뢰 (162)
        }

        pivo.setUsedYn("Y");
        pivo.setModIp(paraMap.get("ipaddr").toString());
        pivo.setModDt(DateUtils.getCurrentDate());
        pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));

        PursInfo chkvo = pir.findByCustNoAndPursNoAndUsedYn(custNo,pivo.getPursNo(), pivo.getUsedYn());
        if (chkvo != null) {
            pivo.setPursNo(chkvo.getPursNo());
            pivo.setModDt(DateUtils.getCurrentDate());
            pivo.setModIp(paraMap.get("ipaddr").toString());
            pivo.setModId(Long.parseLong((paraMap.get("userId").toString())));
        }
        else {
            pivo.setRegDt(DateUtils.getCurrentDate());
            pivo.setRegIp(paraMap.get("ipaddr").toString());
            pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        pivo.setCustNo(custNo);
        pivo = pir.save(pivo);

        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());

        OrdProd ordprod = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,ordNo,prodNo,"Y");
        if(ordprod != null){
            ordprod.setPursNo(pivo.getPursNo());
            ordprod.setCustNo(custNo);
            ordProdRepo.save(ordprod);
        }

        //구매자재정보 처리
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("matrList");
        for (Map<String, Object> el : ds) {
            PursMatr pmvo = new PursMatr();
            pmvo.setPursNo(pivo.getPursNo());
            try{
                pmvo.setCmpyNo(Long.parseLong(el.get("cmpyNo").toString()));
            }catch(NullPointerException ne){
                pmvo.setCmpyNo(0L);
            }

            pmvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            /* OJT need_qty 소요량으로 구매처리시 소수점자리로 구매 처리됨... 원료가 여러개일때 자재구매필요 중복 */
            pmvo.setPursQty(Float.parseFloat(el.get("pursQty").toString()));

            pmvo.setPursUnit(Long.parseLong(el.get("pursUnit").toString()));
            pmvo.setUsedYn("Y");
            pmvo.setPursSts((long) 162);

            try{
                pmvo.setPursMatrNo(Long.parseLong(el.get("pursMatrNo").toString()));
            }catch(NullPointerException ne){
                pmvo.setPursMatrNo(0L);
            }

            PursMatr chkpmvo = pmr.findByCustNoAndPursMatrNoAndUsedYn(custNo,pmvo.getPursMatrNo(), pmvo.getUsedYn());
            if(chkpmvo != null){
                pmvo.setModIp(paraMap.get("ipaddr").toString());
                pmvo.setModDt(DateUtils.getCurrentDate());
                pmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            }
            else{
                pmvo.setRegDt(DateUtils.getCurrentDate());
                pmvo.setRegIp(paraMap.get("ipaddr").toString());
                pmvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            pmvo.setCustNo(custNo);
            pmvo = pmr.save(pmvo);

            /*창고별 구매자재 위치관리 설정
            if (pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.end"))) {
                Map<String, Object> posmap = StringUtil.voToMap(pmvo);
                posmap.put("whNm", el.get("wh_nm").toString());
                posmap.put("ipaddr",pursMap.get("ipaddr").toString());
                posmap.put("userId",pursMap.get("userId").toString());
                posmap.put("fileRoot",fileRoot);
                stockService.savePursMatrPos(posmap);
            }

            if (pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.end")) ) { //구매완료된경우 재고조정
                stockService.resetMatrStock(paraMap);
            }

            List<Map<String,Object>> ds = mapper.getNeedMatrOrderList(paraMap);
            Map<String,Object> pursMap = new HashMap<String,Object>();
            for(Map<String,Object> el : ds) {
                pursMap.put("ordNo",Long.parseLong(el.get("ordNo").toString()));
                pursMap.put("pursNo",pivo.getPursNo());
                pursMap.put("modIp", paraMap.get("ipaddr"));
                pursMap.put("modId", paraMap.get("userId"));
               ordService.resetPursNo(pursMap);
            }
            */
        }
        /*잠시대기중 이라고 씌어 있어서 2021.10.25 주석 삭제함.*/
        OrdProd opvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,pivo.getOrdNo(),Long.parseLong(paraMap.get("prodNo").toString()) ,"Y");
        if(opvo != null){
            opvo.setPursNo(pivo.getPursNo());
            opvo.setCustNo(custNo);
            ordProdRepo.save(opvo);
        }
        return StringUtil.voToMap(pivo);
    }


    @Transactional
    @Override
    public void dropPursInfo(Map<String, Object> paraMap) {
        String tag = "PursService.dropPursInfo => ";
        log.info(tag + "paraMap = "  +paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        PursInfo chkvo = pursInfoRepo.findByCustNoAndPursNoAndUsedYn(custNo,Long.parseLong(paraMap.get("pursNo").toString()),"Y");
        if (chkvo != null) {
            chkvo.setUsedYn("N");
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            pursInfoRepo.save(chkvo);
        }
        //mapper.dropPursInfo(piMap);
    }
    @Transactional
    @Override
    public void savePursMatrByVo(PursMatr pmvo) {
        Long custNo = pmvo.getCustNo();
        PursMatr ckvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pmvo.getPursNo(),pmvo.getMatrNo(),"Y");
        pmvo.setPursMatrNo( (ckvo != null) ? ckvo.getPursMatrNo() : 0L);
        pmr.save(pmvo);
    }

    @Override
    public List<Map<String, Object>> getPursReqList(Map<String, Object> paraMap) {
        String tag = "vsvc.pursService.getPursReqList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursReqList(paraMap);
    }

    @Override
    public int getPursReqListCount(Map<String, Object> paraMap) {
        String tag = "pursService.getPursReqListCount => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursReqListCount(paraMap);
    }

    @Override
    public Map<String, Object> getPursReqMatrInfo(Map<String, Object> paraMap) {
        String tag = "pursService.getPursReqMatrInfo => ";
        return mapper.getPursReqMatrInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursReqOrdList(Map<String, Object> paraMap) {
        String tag = "pursService.getPursReqOrdList => ";
        return mapper.getPursReqOrdList(paraMap);
    }
    @Override
    public int getPursReqOrdListCount(Map<String, Object> paraMap) {
        String tag = "pursService.getPursReqOrdListCount => ";
        return mapper.getPursReqOrdListCount(paraMap);
    }


    @Transactional
    @Override
    public void savePursReqMatr(HashMap<String, Object> paraMap) {
        String tag = "pursService.savePursReqMatr => ";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        //구매기본정보 저장
        Long pursNo = 0L;
        try {
            pursNo = Long.parseLong(paraMap.get("pursNo").toString());
        }
        catch (NullPointerException ne) {
            pursNo = 0L;
        }
        PursInfo infovo = new PursInfo();
        infovo.setUsedYn("Y");

        infovo.setPursSts(Long.parseLong(paraMap.get("pursSts").toString())); // 검수


        try {
            infovo.setPursDt(sdf.parse(paraMap.get("pursDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            infovo.setPursNo(pursNo);
        }catch(NullPointerException en){
            infovo.setPursNo(0L);
        }

        try{
            infovo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString()));
        }catch (NullPointerException ne){

        }

        try {
            infovo.setDlvReqDt(sdf.parse(paraMap.get("dlvReqDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            infovo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        }
        catch (NullPointerException ne) {
            infovo.setIndcNo(0L);
        }
        infovo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        infovo.setModDt(DateUtils.getCurrentBaseDateTime());
        infovo.setModIp(paraMap.get("ipaddr").toString());

        PursInfo chkpivo = pir.findByCustNoAndPursNoAndUsedYn(custNo,pursNo,"Y") ;
        if (chkpivo != null) {
            infovo.setPursNo(chkpivo.getPursNo());
            infovo.setRegDt(chkpivo.getRegDt());
            infovo.setRegId(chkpivo.getRegId());
            infovo.setRegIp(chkpivo.getRegIp());
        }
        else {
            try {
                infovo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
            }
            catch (NullPointerException ne) {
                infovo.setPursNo(0L);
            }
            infovo.setRegDt(DateUtils.getCurrentBaseDateTime());
            infovo.setRegIp(paraMap.get("ipaddr").toString());
            infovo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        infovo.setCustNo(custNo);
        infovo = pir.save(infovo);

        //구매자재 저장
        PursMatr matrvo = new PursMatr();
        matrvo.setUsedYn("Y");
        matrvo.setPursNo(infovo.getPursNo());
        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        matrvo.setMatrNo(matrNo);
        matrvo.setPursSts(infovo.getPursSts()); //AddOn By KMJ At 21.10.27
        matrvo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
        matrvo.setPursQty(Float.parseFloat(paraMap.get("pursQty").toString()));
        matrvo.setPursUnit(Long.parseLong(paraMap.get("pursUnit").toString()));

        try {
            matrvo.setPursAmt(Long.parseLong(paraMap.get("pursAmt").toString()));
        }
        catch (NullPointerException ne) {

        }
        matrvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        matrvo.setModDt(DateUtils.getCurrentDate());
        matrvo.setModIp(paraMap.get("ipaddr").toString());
        PursMatr pmchkvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pursNo,matrNo,"Y");
        if (pmchkvo != null) {
            matrvo.setPursMatrNo(pmchkvo.getPursMatrNo());
            matrvo.setRegId(pmchkvo.getRegId());
            matrvo.setRegDt(pmchkvo.getRegDt());
            matrvo.setRegIp(pmchkvo.getRegIp());
        }
        else {
            matrvo.setPursMatrNo(0L);
            matrvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            matrvo.setRegDt(DateUtils.getCurrentDate());
            matrvo.setRegIp(paraMap.get("ipaddr").toString());
        }
        matrvo.setCustNo(custNo);
        pmr.save(matrvo);
      //  mapper.resetDlvReqDt(matrInfo);
    }
    @Transactional
    @Override
    public void dropPursReqMatr(HashMap<String, Object> paraMap) {
        String tag = "pursService.dropPursReqMatr => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        mapper.dropPursMatr(paraMap);
        if (mapper.isDeadPurs(paraMap).equals("Y")) {
            mapper.dropPursInfo(paraMap);
        }
    }

    @Transactional
    @Override
    public void savePursMatr(Map<String, Object> paraMap) {
        String tag = "pursService.savePursMatr => ";
        log.info(tag);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        PursMatr pmvo = new PursMatr();
        try {
            pmvo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
        }
        catch (NullPointerException ne) {
            pmvo.setPursNo(0L);
        }
        pmvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        pmvo.setPursQty((Float) paraMap.get("pursQty"));
        pmvo.setPursUnit(Long.parseLong(paraMap.get("pursQnit").toString()));
        pmvo.setUsedYn("Y");
        try {
            pmvo.setPursAmt(Long.parseLong(paraMap.get("pursAmt").toString()));
        }
        catch (NullPointerException ne) {
            pmvo.setPursAmt(0L);
        }
        pmvo.setModIp(paraMap.get("ipaddr").toString());
        pmvo.setModDt(DateUtils.getCurrentDate());
        pmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        if (pmvo.getPursNo() == 0L) {
            pmvo.setRegDt(DateUtils.getCurrentDate());
            pmvo.setRegIp(paraMap.get("ipaddr").toString());
            pmvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        pmvo.setCustNo(custNo);
        pmr.save(pmvo);
    }

    /*구매자재 다중처리*/
    @Transactional
    @Override
    public void savePursMatrList(Map<String, Object> paraMap) {
        String tag = "pursService.savePursMatrList => ";
        log.info(tag);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String,Object>> matrList = ( List<Map<String,Object>>) paraMap.get("matrList");
        PursInfo infovo = new PursInfo();
        infovo.setPursSts(Long.parseLong(env.getProperty("purs.sts.insp"))); // 자재 추기시 검수로
        infovo.setUsedYn("Y");
        Map<String,Object> dmap = new HashMap<String,Object>();
        dmap.put("interval","1 day");
        infovo.setDlvReqDt(cmmnService.getIntervalDate(dmap));
        try {
            infovo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        }
        catch (NullPointerException ne) {

        }
        try {
            infovo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString()));
        }
        catch (NullPointerException ne) {

        }
        infovo.setPursDt(DateUtils.getCurrentDate());

        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        infovo.setModDt(DateUtils.getCurrentDate());
        infovo.setModId(userId);
        infovo.setModIp(ipaddr);
        PursInfo ichkvo = pursInfoRepo.findByCustNoAndPursNoAndUsedYn(custNo,infovo.getPursNo(),"Y");
        if (ichkvo != null) {
            infovo.setPursNo(ichkvo.getPursNo());
            infovo.setRegId(ichkvo.getRegId());
            infovo.setRegId(ichkvo.getRegId());
            infovo.setRegIp(ichkvo.getRegIp());
        }
        else {
            infovo.setPursNo(0L);
            infovo.setRegId(userId);
            infovo.setRegDt(DateUtils.getCurrentDate());
            infovo.setRegIp(ipaddr);
        }
        infovo.setCustNo(custNo);
        infovo = pursInfoRepo.save(infovo);

        for (Map<String, Object> el : matrList) {
            PursMatr matrvo = new PursMatr();
            matrvo.setModId(userId);
            matrvo.setModIp(ipaddr);
            matrvo.setModDt(DateUtils.getCurrentDateTime());
            matrvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            try {
                matrvo.setCmpyNo(Long.parseLong(el.get("cmpyNo").toString()));
            } catch (NullPointerException ne) {

            }

            try {
                matrvo.setPursUnit(Long.parseLong(el.get("pursUnit").toString()));
            } catch (NullPointerException ne) {
                matrvo.setPursUnit(0L);
            }

            matrvo.setUsedYn("Y");
            matrvo.setPursNo(infovo.getPursNo());
            matrvo.setPursQty(1F);
            try {
                matrvo.setPursAmt(Long.parseLong(paraMap.get("pursAmt").toString()));
            } catch (NullPointerException ne) {
                matrvo.setPursAmt(0L);
            }
            matrvo.setModDt(DateUtils.getCurrentDate());
            matrvo.setModIp(paraMap.get("ipaddr").toString());
            matrvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

            PursMatr mchkvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,matrvo.getPursNo(), matrvo.getMatrNo(), matrvo.getUsedYn());
            if (mchkvo != null) {
                matrvo.setPursMatrNo(mchkvo.getPursMatrNo());
                matrvo.setRegDt(mchkvo.getRegDt());
                matrvo.setRegIp(mchkvo.getRegIp());
                matrvo.setRegId(mchkvo.getRegId());
            }
            else {
                matrvo.setRegDt(DateUtils.getCurrentDate());
                matrvo.setRegIp(paraMap.get("ipaddr").toString());
                matrvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            matrvo.setPursSts(infovo.getPursSts());
            matrvo.setCustNo(custNo);
            pmr.save(matrvo);
        }
    }

    /*구매원자재 등록*/
    @Transactional
    @Override
    public void pursMatrSave(HashMap<String, Object> paraMap) {
        String tag = "pursService.pursMatrSave";
        log.info(tag + "paraMap = " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String,Object> pm = (Map<String,Object>) paraMap.get("pursMatr");
        PursMatr pmvo = new PursMatr();
        pmvo.setPursNo(Long.parseLong(pm.get("pursNo").toString()));
        pmvo.setMatrNo(Long.parseLong(pm.get("matrNo").toString()));
        pmvo.setPursQty( (Float) pm.get("pursQty"));
        pmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        pmvo.setModIp(paraMap.get("ipaddr").toString());
        pmvo.setModDt(pmvo.getModDt());
        pmvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        pmvo.setRegIp(paraMap.get("ipaddr").toString());
        pmvo.setRegDt(pmvo.getRegDt());
        PursMatr chkvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,pmvo.getPursNo(), pmvo.getMatrNo(),"Y");
        if (chkvo != null) {
            pmvo.setPursMatrNo(chkvo.getPursMatrNo());
            pmvo.setPursUnit(chkvo.getPursUnit());
        }
        pmvo.setCmpyNo(custNo);
        pmr.save(pmvo);
    }

    @Override
    public List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap) {
        String tag = "pursService.getComboPursCmpy => ";
        paraMap.put("mngrGbnCd",Long.parseLong(env.getProperty("code.mngrgbn.purs")));
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboPursCmpy(paraMap);
    }
    //리스트에서 선택된 쟈재의 취급 구매처 추출
    @Override
    public List<Map<String, Object>> getComboMatrCmpy(Map<String, Object> paraMap) {
        String tag = "pursService.getComboPursCmpy => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getComboMatrCmpy(paraMap);
    }

    @Transactional
    @Override
    public void resetPursStatusEnd(Map<String, Object> paraMap) {
        String tag = "pursService.resetPursStatusEnd => ";
        /*
        List<Map<String,Object>> ds = ( List<Map<String,Object>> ) paraMap.get("matrList");

        List<Long> pursNos = new ArrayList<Long>();
        Long svPursNo = 0L;
        for (Map<String, Object> el : ds) {
            Long pursNo = Long.parseLong(el.get("purs_no").toString());
            if (!svPursNo.equals(pursNo)) {
                pursNos.add(pursNo);
                svPursNo = pursNo;
            }
        }
        Map<String,Object> pursMap = new HashMap<String,Object>();
        pursMap.put("pursNos",pursNos);
        pursMap.put("pursSts",paraMap.get("pursSts"));
        log.info(tag + "pursMap = "  + pursMap.toString() );
         */
        mapper.resetPursStatusEnd(paraMap);
    }

    @Override
    public String isAbleCnfmPurs(Map<String, Object> paraMap) {
        String tag = "pursService.isAbleCnfmPurs => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.isAbleCnfmPurs(paraMap);
    }
    @Transactional
    @Override
    public void resetPursCmpyByList(Map<String, Object> paraMap) {
        String tag = "pursService.resetPursCmpyByList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String,Object> procMap = null;
        List<Map<String,Object>> ds = ( List<Map<String,Object>> ) paraMap.get("matrList");
        for (Map<String, Object> el : ds) {
            procMap = new HashMap<String,Object>();
            procMap.put("pursMatrNo",Long.parseLong(el.get("pursMatrNo").toString()));
            procMap.put("cmpyNo",Long.parseLong(paraMap.get("cmpyNo").toString()));
            procMap.put("ipaddr",paraMap.get("ipaddr"));
            procMap.put("userId",paraMap.get("userId"));
            mapper.resetPursCmpy(procMap);
        }
    }

    @Override
    public List<Map<String, Object>> getPursHstrList(HashMap<String, Object> paraMap) {
        String tag = "pursService.resetPursCmpyQtyByList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursHstrList(paraMap);
    }

    @Override
    public int getPursHstrListCount(HashMap<String, Object> paraMap) {
        String tag = "pursService.resetPursCmpyQtyByList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursHstrListCount(paraMap);
    }


    @Transactional
    @Override
    public void resetPursCmpyQtyByList(Map<String, Object> paraMap) {
        String tag = "pursService.resetPursCmpyQtyByList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String,Object> procMap = null;
        List<Map<String,Object>> ds = ( List<Map<String,Object>> ) paraMap.get("matrList");
        for (Map<String, Object> el : ds) {
            procMap = new HashMap<String,Object>();
            procMap.put("pursMatrNo",Long.parseLong(el.get("pursMatrNo").toString()));
            procMap.put("cmpyNo",Long.parseLong(paraMap.get("cmpyNo").toString()));
            procMap.put("pursQty",Long.parseLong(el.get("pursQty").toString()) );
            procMap.put("ipaddr",paraMap.get("ipaddr"));
            procMap.put("userId",paraMap.get("userId"));
            mapper.resetPursCmpyQty(procMap);
        }
    }


    @Override
    public List<Map<String, Object>> getNeedPursList(Map<String, Object> paraMap) {
        return mapper.getNeedPursList(paraMap);
    }

    @Override
    public int getNeedPursListCount(Map<String, Object> paraMap) {
        return mapper.getNeedPursListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getNeedMatrOrderList(Map<String, Object> paraMap) {
        String tag = "pursService.getNeedMatrOrderList => ";
        return mapper.getNeedMatrOrderList(paraMap);
    }

    @Override
    public int getNeedMatrOrderListCount(Map<String, Object> paraMap) {
        String tag = "pursService.getNeedMatrOrderListCount => ";
        return mapper.getNeedMatrOrderListCount(paraMap);
    }

    @Override
    public List<String> getWhList(HashMap<String, Object> paraMap) {
        List<Map<String,Object>> ds = mapper.getWhList(paraMap);
        List<String> astr = new ArrayList<String>();
        for (Map<String, Object> el : ds) {
            astr.add(el.get("whNm").toString());
        }
        return astr;
    }

    @Override
    public List<Map<String, Object>> getPursMatrList(Map<String, Object> paraMap) {
        String tag = "pursService.getPursMatrList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursMatrList(paraMap);
    }
    @Override
    public int getPursMatrListCount(Map<String, Object> paraMap) {
        return mapper.getPursMatrListCount(paraMap);
    }

    @Override
    public void savePursMatrTemp(HashMap<String, Object> paraMap) {

    }

    @Override
    public void resetPursStatusIng(Map<String, Object> paraMap) {
        mapper.resetPursStatusIng(paraMap);
    }

    @Override
    public void resetPursStatusIngByRetn(Map<String, Object> paraMap) {
        mapper.resetPursStatusIngByRetn(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursMatrIwhList(Map<String, Object> paraMap) {
        String tag = "pursService.getPursMatrIwhList=>";
        log.info(tag + " paraPam = " + paraMap.toString());
        return mapper.getPursMatrIwhList(paraMap);
    }

    @Override
    public int getPursMatrIwhListCount(Map<String, Object> paraMap) {
        return mapper.getPursMatrIwhListCount(paraMap);
    }

    @Override
    public void dropPursMatrList(Map<String, Object> paraMap) {
        String tag = "pursService.dropPursMatrList=>";
        log.info(tag + " paraPam = " + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long pursNo = 0L;
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        //matrList = [{"pursNo":395052,"pursDt":"2021-10-22","pursMatrNo":395055,"whNo":6,"cmpyNm":"구매처미상","whNm":"원료보관창고(냉장)","itemNo":"395055-1","matrNm":"자몽종자추출물","pursQty":1,"stkQty":5,"matrNo":327090,"vgt_id":0,"originalIndex":0,"vgtSelected":true}]
        //225_MatrIwh.vue?3457:991 onMatrRowClick.e = {"pursNo":395052,"pursDt":"2021-10-22","pursMatrNo":395055,"whNo":6,"cmpyNm":"구매처미상","whNm":"원료보관창고(냉장)","itemNo":"395055-1","matrNm":"자몽종자추출물","pursQty":1,"stkQty":5,"matrNo":327090,"vgt_id":0,"originalIndex":0,"vgtSelected":true}

        List<Map<String, Object>> dsMap = (List<Map<String, Object>>) paraMap.get("matrList");
        for (Map<String, Object> el : dsMap) {
            PursMatr pmvo = new PursMatr();
            pmvo.setCustNo(custNo);
            try {
                pmvo.setPursMatrNo(Long.parseLong(el.get("pursMatrNo").toString()));
            }
            catch (NullPointerException ne) {
                continue;
            }
            PursMatr chkpmvo = pmr.findByCustNoAndPursMatrNoAndUsedYn(custNo,pmvo.getPursMatrNo(),"Y");
            if (chkpmvo != null) {
                chkpmvo.setUsedYn("N");
                chkpmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                chkpmvo.setModId(userId);
                chkpmvo.setModIp(ipaddr);
                pmr.save(chkpmvo);
            }
            pursNo = chkpmvo.getPursNo();

            List<PursMatr> dsPursMatr = pmr.findAllByCustNoAndPursNoAndUsedYn(custNo,pursNo,"Y");
            if (dsPursMatr.size() == 0) {
                PursInfo pivo = pir.findByCustNoAndPursNoAndUsedYn(custNo,pursNo,"Y");
                if (pivo != null) {
                    pivo.setUsedYn("N");
                    pivo.setModId(userId);
                    pivo.setModDt(DateUtils.getCurrentBaseDateTime());
                    pivo.setModIp(ipaddr);
                    pir.save(pivo);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> pursInfoList(HashMap<String, Object> paraMap) {
        return mapper.getpursInfoList(paraMap);
    }
    @Override
    public int pursInfoListCount(HashMap<String, Object> paraMap) {
        return mapper.getpursInfoListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> pursInfoMatrList(HashMap<String, Object> paraMap) {
        return mapper.getpursInfoMatrList(paraMap);
    }
    @Override
    public int pursInfoMatrListCount(HashMap<String, Object> paraMap) {
        return mapper.getpursInfoMatrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPursReqIndcList(Map<String, Object> paraMap) {
        return mapper.getPursReqIndcList(paraMap);
    }

    @Override
    public int getPursReqIndcListCount(Map<String, Object> paraMap) {
        return mapper.getPursReqIndcListCount(paraMap);
    }



}
