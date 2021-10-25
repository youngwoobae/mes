package daedan.mes.equip.service;

import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.domain.*;
import daedan.mes.equip.repository.*;
import daedan.mes.make.domain.OperMast;
import daedan.mes.equip.mapper.EquipMapper;
import daedan.mes.equip.domain.EquipReport;
import daedan.mes.spot.service.SpotEquipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*설비관리*/
@Service("equipService")
public class EquipServiceImpl implements EquipService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private EquipInfoRepository eqRepository;

    @Autowired
    private EquipMastRepository eqMastRepo;

    @Autowired
    private EquipMngrItemRepository emiRepository;

    @Autowired
    private SpotEquipService spotEquipService;

    @Autowired
    private EquipMngrHstrRepository equipMngrHstrRepo;

    @Autowired
    private OperMastRepository operMastRepo;

    @Autowired
    private EquipReportRepository equipReportRepo;

    @Autowired
    private EquipMapper mapper;

    @Override
    public List<Map<String,Object>> getEquipList(Map<String, Object> paraMap) {
        return mapper.getEquipList(paraMap);
    }

    @Override
    public int getEquipListCount(Map<String, Object> paraMap) {
        return mapper.getEquipListCount(paraMap);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void saveEquip(Map<String, Object> map) {
        String ipaddr = (String) map.get("ipaddr");
        Long custNo = Long.parseLong(map.get("custNo").toString());
        Long  userId = Long.parseLong(map.get("userId").toString());
        Long madein = 0L;
        EquipInfo em = new EquipInfo();

        try {
            em.setEquipNo(Long.parseLong(map.get("equipNo").toString()));
        }
        catch (NullPointerException ne) {
            em.setEquipNo(0L);
        }
        try {
            em.setFileNo(Long.parseLong(map.get("fileNo").toString()));
        }
        catch (NullPointerException ne) {
            em.setFileNo(0L);
        }
        Date lstChkDt = null;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            lstChkDt = transFormat.parse(map.get("lastChkDt").toString().substring(0,10));
            em.setLstChkDt(lstChkDt);
        } catch (Exception e) {

        }
        try {
            em.setPursUnitPrc(Long.parseLong(map.get("pursUnitPrc").toString()));
        }
        catch (NullPointerException ne) {
            em.setPursUnitPrc(0L);
        }
        try {
            madein = (Long.parseLong(map.get("madein").toString()));
        } catch (NullPointerException ne) {
            madein = Long.parseLong(env.getProperty("madein_default_cd"));
        }
        em.setMadein(madein);

        em.setEquipNm((String) map.get("equipNm"));
        em.setWareCd((String) map.get("wareCd"));
        em.setSz((String) map.get("sz"));
        em.setModlNm((String) map.get("modlNm"));
        em.setMakeCmpy((String) map.get("makeCmpyNm"));
        em.setChkPeriod(Long.parseLong(map.get("chkPeriod").toString()));
        em.setLstChkDt(lstChkDt);
        em.setUsedYn("Y");

        em.setModId(userId);
        em.setModIp(ipaddr);
        em.setModDt(em.getModDt());
        EquipInfo chkvo = eqRepository.findByEquipNo(em.getEquipNo());
        if (chkvo != null) {
            em.setEquipNo(chkvo.getFileNo());
            em.setRegId(chkvo.getRegId());
            em.setRegIp(chkvo.getRegIp());
            em.setRegDt(chkvo.getRegDt());
           }
        else {
            em.setRegId(userId);
            em.setRegIp(ipaddr);
            em.setRegDt(em.getRegDt());
        }
        em.setCustNo(custNo);
        eqRepository.save(em);
    }
    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void saveEquipMast(Map<String, Object> map) {
        String ipaddr = (String) map.get("ipaddr");

        Long custNo = Long.parseLong(map.get("custNo").toString());
        Long  userId = Long.parseLong(map.get("userId").toString());
        Long madein = 0L;
        EquipMast em = new EquipMast();

        try {
            em.setEquipMastNo(Long.parseLong(map.get("equipMastNo").toString()));
        }
        catch (NullPointerException ne) {
            em.setEquipMastNo(0L);
        }
        try {
            em.setFileNo(Long.parseLong(map.get("fileNo").toString()));
        }
        catch (NullPointerException ne) {
            em.setFileNo(0L);
        }
        Date lstChkDt = null;
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            em.setLstChkDt(transFormat.parse(map.get("lastChkDt").toString().substring(0,10)));
        } catch (Exception e) {

        }
        try {
            em.setPursDt(transFormat.parse(map.get("pursDt").toString().substring(0,10)));
        } catch (Exception e) {

        }

        try {
            em.setPursUnitPrc(Long.parseLong(map.get("pursUnitPrc").toString()));
        }
        catch (NullPointerException ne) {
            em.setPursUnitPrc(0L);
        }
        try {
            madein = (Long.parseLong(map.get("madein").toString()));
        } catch (NullPointerException ne) {
            madein = Long.parseLong(env.getProperty("madein_default_cd"));
        }
        em.setMadein(madein);

        em.setEquipNm((String) map.get("equipNm"));
        em.setWareCd((String) map.get("wareCd"));
        try {
            em.setWareSpec(map.get("wareSpec").toString());
        } catch (Exception e) {

        }
        em.setSz((String) map.get("sz"));
        em.setModlNm((String) map.get("modlNm"));
        em.setMakeCmpyNm((String) map.get("makeCmpyNm"));
        em.setChkPeriod(Long.parseLong(map.get("chkPeriod").toString()));
        em.setLstChkDt(lstChkDt);
        em.setUsedYn("Y");

        em.setModId(userId);
        em.setModIp(ipaddr);
        em.setModDt(em.getModDt());
        EquipMast chkvo = eqMastRepo.findByCustNoAndEquipMastNoAndUsedYn(custNo,em.getEquipMastNo(),"Y");
        if (chkvo != null) {
            em.setEquipMastNo(chkvo.getEquipMastNo());
            em.setRegId(chkvo.getRegId());
            em.setRegIp(chkvo.getRegIp());
            em.setRegDt(chkvo.getRegDt());
        }
        else {
            em.setEquipMastNo(0L);
            em.setRegId(userId);
            em.setRegIp(ipaddr);
            em.setRegDt(em.getRegDt());
        }
        em.setCustNo(custNo);
        eqMastRepo.save(em);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void reportSave(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long  userId = Long.parseLong(paraMap.get("userId").toString());
        Long madein = 0L;
        OperMast em = new OperMast();
        log.info("paraMapAS=>"+paraMap);
//        try {
        em.setOperNo(Long.parseLong(paraMap.get("operNo").toString()));
        em.setOperText(paraMap.get("operText").toString());
        em.setShape(paraMap.get("shape").toString());
//
        OperMast chkvo = operMastRepo.findByOperNo(em.getOperNo());
        chkvo.setOperText(paraMap.get("operText").toString());
        chkvo.setShape(paraMap.get("shape").toString());
//        if (chkvo != null) {
//            em.setEquipNo(chkvo.getFileNo());
//        }
//        else {
//
//        }
        chkvo.setCustNo(custNo);
        operMastRepo.save(chkvo);
    }

    @Override
    @Transactional
    public void saveCcpMetalReport(Map<String, Object> paraMap){
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> reportList = (List<Map<String, Object>>) paraMap.get("reportList");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        for(Map<String, Object> el : reportList){
            Long equipReportNo = 0L;
            EquipReport ervo = new EquipReport();
            ervo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
            ervo.setMngrHstrNo(Long.parseLong(el.get("mngrHstrNo").toString()));
            ervo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            ervo.setFeYn(el.get("feYn").toString());
            ervo.setSusYn(el.get("susYn").toString());
            ervo.setProdYn(el.get("prodYn").toString());
            ervo.setProdFeYn(el.get("prodFeYn").toString());
            ervo.setProdSusYn(el.get("prodSusYn").toString());
            ervo.setMeasDt(el.get("measDt").toString());
            ervo.setRmk(el.get("rmk").toString());
            ervo.setTestTp(Integer.parseInt(el.get("testTp").toString()));
            ervo.setUsedYn("Y");
            try{
                equipReportNo = Long.parseLong(el.get("equipReportNo").toString());
            }catch(NullPointerException ne){
                equipReportNo = 0L;
            }


            EquipReport chkervo = equipReportRepo.findByEquipReportNoAndUsedYn(equipReportNo, "Y");
            if(chkervo != null){
                ervo.setEquipReportNo(chkervo.getEquipReportNo());
                ervo.setRegDt(chkervo.getRegDt());
                ervo.setRegId(chkervo.getRegId());
                ervo.setRegIp(chkervo.getRegIp());

                ervo.setModDt(DateUtils.getCurrentDateTime());
                ervo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                ervo.setModIp(paraMap.get("ipaddr").toString());
            }else{
                ervo.setEquipReportNo(0L);
                ervo.setRegDt(DateUtils.getCurrentDateTime());
                ervo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                ervo.setRegIp(paraMap.get("ipaddr").toString());
            }
            ervo.setCustNo(custNo);
            equipReportRepo.save(ervo);
        }

    }

    @Override
    public Map<String, Object> getEquipMastInfo(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        EquipMast vo = eqMastRepo.findByCustNoAndEquipMastNoAndUsedYn(custNo,Long.parseLong(paraMap.get("equipMastNo").toString()),"Y");
        return StringUtil.voToMap(vo);
    }


    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void equipMngrItemSave(Map<String, Object> map) {
        String ipaddr = (String) map.get("ipaddr");
        Long custNo = Long.parseLong(map.get("custNo").toString());
        Long  userId = Long.parseLong(map.get("userId").toString());
        log.info("넘어온값 =>" + map);
        Long madein = 0L;
        EquipMngrItem em = new EquipMngrItem();

        try {
            em.setMngrItemNo(Long.parseLong(map.get("mngrItemNo").toString()));
        }
        catch (NullPointerException ne) {
            em.setMngrItemNo(0L);
        }

        em.setEquipNo(Long.parseLong(map.get("equipNo").toString()));
        em.setMeasPeriod(24);


//        em.setLstChkDt((String) map.get("last_chk_dt"));

        em.setMinLmtVal((float) Long.parseLong(map.get("min").toString()));
        em.setMaxLmtVal((float) Long.parseLong(map.get("max").toString()));
        em.setUsedYn("Y");

        em.setRegId(userId);
        em.setRegIp(ipaddr);
        em.setRegDt(DateUtils.getCurrentDate());

        em.setModId(userId);
        em.setModIp(ipaddr);
        em.setModDt(DateUtils.getCurrentDate());

        EquipMngrItem chkvo = emiRepository.findByEquipNoAndMngrItem(em.getEquipNo(),em.getMngrItem());
        if (chkvo != null) {
            em.setMngrItemNo(chkvo.getMngrItemNo());
        }else{
            em.setMngrItemNo(0L);
        }
        em.setCustNo(custNo);
        emiRepository.save(em);
//        emiRepository.save(em);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void dropEquip(Map<String, Object> map) {
        String ipaddr = (String) map.get("ipaddr");
        Long  userId = Long.parseLong(map.get("userId").toString());
        map.put("modId",userId);
        map.put("modIp",ipaddr);
        mapper.dropEquip(map);
    }
    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void dropEquipMast(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        EquipMast mastvo = eqMastRepo.findByCustNoAndEquipMastNoAndUsedYn(custNo,Long.parseLong(paraMap.get("equip_mast_no").toString()),"Y");
        if (mastvo != null) {
            mastvo.setUsedYn("N");
            mastvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mastvo.setModDt(DateUtils.getCurrentDate());
            mastvo.setModIp(paraMap.get("ipaddr").toString());
            mastvo.setCustNo(custNo);
            eqMastRepo.save(mastvo);
        }
    }
    @Override
    public EquipInfo getEquip(Long equipNo) {
        return eqRepository.findByEquipNo(equipNo);
    }

    @Override
    public Map<String, Object> equipInfo(Map<String, Object> map) {
        return mapper.equipInfo(map);
    }


    @Override
    public List<Map<String, Object>> getEquipRunStatList(Map<String, Object> paraMap) {
        return mapper.getEquipRunStatList(paraMap);
    }

    @Override
    public int getEquipRunStatListCount(Map<String, Object> paraMap) {
        return mapper.getEquipRunStatListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEquipRunHstrList(Map<String, Object> paraMap) {
        return getEquipRunHstrList(paraMap);
    }

    @Override
    public int getEquipRunHstrListCount(Map<String, Object> paraMap) {
        return getEquipRunHstrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEquipDeviceList(Map<String, Object> paraMap) {
        return mapper.getEquipDeviceList(paraMap);
    }

    @Override
    public int getEquipDeviceListCount(Map<String, Object> paraMap) {
        return mapper.getEquipDeviceListCount(paraMap);
    }


    @Override
    public Map<Integer, Map<String, Object>> getErrorItemList(Map<String, Object> paraMap) {
        List<Map<String,Object>> rmap = null;
        rmap = mapper.getOnlyOperList(paraMap);
        Map<Integer, Map<String, Object>> rmaps = new HashMap<>();
        int i = 0;
        for (Map<String, Object> el : rmap) {
            int count = 0;
            count = mapper.getOperCount(el);
            if (count == 0){
                el.put("check","X");
            }else{
                el.put("check","O");
            }

            rmaps.put(Integer.valueOf(i),rmap.get(i));
            i++;
        }


        return rmaps;
    }

    @Override
    public Map<Integer, Map<String, Object>> getCheckItemList(Map<String, Object> paraMap) {
        List<Map<String,Object>> rmap = null;
        rmap = mapper.getCheckItemList(paraMap);
        Map<Integer, Map<String, Object>> rmaps = new HashMap<>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for (int i = 0; i < rmap.toArray().length ; i++) {
            Map<String,Object> item = new HashMap<String, Object>();
            item.put("mngrHstrNo",rmap.get(i).get("mngrHstrNo"));
            item.put("spotEquipNo",rmap.get(i).get("spotEquipNo"));
            item.put("measDt",rmap.get(i).get("measDt"));
            item.put("testTp",rmap.get(i).get("finalTestTp"));
            item.put("feYn",rmap.get(i).get("feYn"));
            item.put("susYn",rmap.get(i).get("susYn"));
            item.put("prodFeYn",rmap.get(i).get("prodFeYn"));
            item.put("prodSusYn",rmap.get(i).get("prodSusYn"));
            item.put("prodNm",rmap.get(i).get("prodNm"));
            item.put("prodNo",rmap.get(i).get("prodNo"));
            item.put("shape",rmap.get(i).get("shape"));
            item.put("prodYn",rmap.get(i).get("prodUYn"));
            item.put("operN",rmap.get(i).get("operNo"));
            item.put("operText",rmap.get(i).get("operText"));

            item.put("equipReportNo",rmap.get(i).get("equipReportNo"));
            item.put("custNo",custNo);
            rmaps.put(i,item);


        }
        log.info("rmaps=>"+rmaps);
        return rmaps;
    }

    @Override
    public List<Map<String,Object>> getOnlyEquipList(Map<String, Object> paraMap) {
        return mapper.getOnlyEquipList(paraMap);
    }

    @Override
    public int getOnlyEquipListCount(Map<String, Object> paraMap) {
        return mapper.getOnlyEquipListCount(paraMap);
    }


    @Override
    public List<Map<String,Object>> getEquipMngrItem(Map<String, Object> paraMap) {
        return mapper.getEquipMngrItem(paraMap);
    }

    @Override
    public List<Map<String,Object>> getEquipMngrHstr(Map<String, Object> paraMap) {
        return mapper.getEquipMngrHstr(paraMap);
    }

    @Override
    public List<Map<String,Object>> getEquipMngrHstrDouble(Map<String, Object> paraMap) {
        return mapper.getEquipMngrHstrDouble(paraMap);
    }

    @Override
    public List<Map<String,Object>> getEquipMngrHstrHdfdVer(Map<String, Object> paraMap) {
        return mapper.getEquipMngrHstrHdfdVer(paraMap);
    }



    /*테블릿 오퍼레이션 시작*/
    @PostMapping(value="/starOper")
    public Result startOper(@RequestBody Map<String, Object> paraMap){
        String tag = "makeController.startEquip => ";
        Result result = Result.successInstance();
        result.setData(mapper.startEquip(paraMap));
        return result;
    }
    /*테블릿 오퍼레이션 종료*/
    @PostMapping(value="/stopOper")
    public Result endOper(@RequestBody Map<String, Object> paraMap){
        String tag = "makeController.stopEquip => ";
        Result result = Result.successInstance();
        mapper.stopEquip(paraMap);
        return result;
    }

    @Override
    @Transactional
    public void saveModbusTmpr(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveModbusTmprHumy ==> ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        paraMap.put("ipaddr",paraMap.get("ipaddr"));
        log.info(tag + "paraMap = " + paraMap.toString());
        EquipMngrHstr emhvo = new EquipMngrHstr();
        emhvo.setUnixHms(System.currentTimeMillis() / 1000);
        Map<String,Object> semap = spotEquipService.getSpotEquipInfo(paraMap);
        if (semap == null) {
            emhvo.setMaxLmtVal(0f);
            emhvo.setMinLmtVal(0f);
        }
        else {
            emhvo.setMaxLmtVal(Float.parseFloat(semap.get("maxLmtVal").toString()));
            emhvo.setMinLmtVal(Float.parseFloat(semap.get("minLmtVal").toString()));
        }
        emhvo.setMeasVal(Float.parseFloat(paraMap.get("measVal").toString()));//온
        emhvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        emhvo.setUsedYn("Y");
        EquipMngrHstr chkvo = equipMngrHstrRepo.findBySpotEquipNoAndUnixHms(emhvo.getSpotEquipNo(), emhvo.getUnixHms());
        if (chkvo != null ) {
            emhvo.setMngrHstrNo(chkvo.getMngrHstrNo());
        }
        else {
            emhvo.setMngrHstrNo(0L);
        }
        log.info(tag + "spotEquipNo = " + emhvo.getSpotEquipNo() + "measval = " + emhvo.getMeasVal());
         emhvo.setCustNo(custNo);
        equipMngrHstrRepo.save(emhvo);

    }
    @Override
    @Transactional
    public void saveModbusHumy(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveModbusTmprHumy ==> ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        paraMap.put("ipaddr", paraMap.get("ipaddr"));
        log.info(tag + "paraMap = " + paraMap.toString());
        EquipMngrHstr emhvo = new EquipMngrHstr();
        emhvo.setUnixHms(System.currentTimeMillis() / 1000);
        Map<String, Object> semap = spotEquipService.getSpotEquipInfo(paraMap);
        if (semap == null) {
            emhvo.setMaxLmtVal(0f);
            emhvo.setMinLmtVal(0f);
        }
        else {
            emhvo.setMaxLmtVal(Float.parseFloat(semap.get("maxLmtVal").toString()));
            emhvo.setMinLmtVal(Float.parseFloat(semap.get("minLmtVal").toString()));
        }
        emhvo.setMeasVal(Float.parseFloat(paraMap.get("measVal").toString()));//온
        emhvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        emhvo.setUsedYn("Y");
        try {
            EquipMngrHstr chkvo = equipMngrHstrRepo.findBySpotEquipNoAndUnixHms(emhvo.getSpotEquipNo(), emhvo.getUnixHms());
            if (chkvo != null) {
                emhvo.setMngrHstrNo(chkvo.getMngrHstrNo());
            } else {
                emhvo.setMngrHstrNo(0L);
            }
            log.info(tag + "spotEquipNo = " + emhvo.getSpotEquipNo() + "measval = " + emhvo.getMeasVal());
            emhvo.setCustNo(custNo);
            equipMngrHstrRepo.save(emhvo);
        }
        catch (NonUniqueResultException ne) {

        }
    }

    @Override
    public List<Map<String, Object>> getComboEquipList(Map<String, Object> paraMap) {
        return mapper.getComboEquipList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEquipReportList(Map<String, Object> paraMap){
        paraMap.put("regDt", paraMap.get("regDt").toString().substring(0,10));
        return mapper.getEquipReportList(paraMap);
    }

    @Override
    public int getEquipReportListCount(Map<String, Object> paraMap){
        paraMap.put("regDt", paraMap.get("regDt").toString().substring(0,10));
        return mapper.getEquipReportListCount(paraMap);
    }

}
