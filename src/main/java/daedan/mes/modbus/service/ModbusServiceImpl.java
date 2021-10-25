package daedan.mes.modbus.service;

import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.equip.domain.EquipMngrHstr;
import daedan.mes.equip.repository.EquipMngrHstrRepository;
import daedan.mes.equip.repository.OperMastRepository;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcRslt;
import daedan.mes.make.domain.OperMast;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.make.repository.MakeIndcRsltRepository;
import daedan.mes.modbus.domain.MobileInfo;
import daedan.mes.modbus.domain.TmprHstr;
import daedan.mes.modbus.mapper.ModbusMapper;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.proc.mapper.ProcMapper;
import daedan.mes.modbus.repository.MobileInfoRepository;
import daedan.mes.modbus.repository.TmprHstrRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.mapper.ProdMapper;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.spot.domain.SpotEquip;
import daedan.mes.spot.repository.SpotEquipRepository;
import daedan.mes.sysmenu.repository.SysMenuRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("modbusService")
public class ModbusServiceImpl implements  ModbusService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    MobileInfoRepository mobileInfoRepo;

    @Autowired
    TmprHstrRepository tmprHstrRepo;

    @Autowired
    MakeIndcRepository makeIndcRepo;

    @Autowired
    ProdRepository prodRepo;

    @Autowired
    MakeIndcRsltRepository makeIndcRsltRepo;

    @Autowired
    EquipMngrHstrRepository  equipMngrHstrRepo;

    @Autowired
    OperMastRepository operMastRepo;

    @Autowired
    SpotEquipRepository spotEquipRepos; ;

    @Autowired
    SysMenuRepository sysmenuRepo;

    @Autowired
    ModbusMapper mapper;

    @Autowired
    MakeIndcMapper makeIndcMapper;

    @Autowired
    ProcMapper procMapper;

    @Autowired
    ProdMapper prodMapper;

    @Override
    public void saveTabletInfo(Map<String, Object> paraMap) {
        MobileInfo vo = new MobileInfo();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        try {
            vo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        }
        catch (NullPointerException ne) {
            Long spotNo = Long.parseLong(paraMap.get("spotNo").toString());
            Long equipNo = Long.parseLong(paraMap.get("equipNo").toString());
            SpotEquip sevo =  spotEquipRepos.findByCustNoAndSpotNoAndEquipNoAndUsedYn(custNo,spotNo,equipNo,"Y");
            if (sevo != null) {
                vo.setSpotEquipNo(sevo.getSpotEquipNo());
            }
        }
        vo.setDeviceIp(paraMap.get("deviceIp").toString());
        vo.setDevicePort(Integer.parseInt(paraMap.get("devicePort").toString()));
        vo.setDeviceNo(Integer.parseInt(paraMap.get("deviceNo").toString()));
        vo.setProtocol(Long.parseLong(paraMap.get("protocol").toString()));
        vo.setSysMenuNo(Long.parseLong(paraMap.get("sysMenuNo").toString()));
        try {
            vo.setFrAddr(Integer.parseInt(paraMap.get("frAddr").toString()));
        }
        catch (NullPointerException ne) {
            vo.setFrAddr(0);
        }
        try {
            vo.setDataLen(Integer.parseInt(paraMap.get("dataLen").toString()));
        }
        catch(NullPointerException ne) {
            vo.setDataLen(0);
        }
        try {
            vo.setDispYn(paraMap.get("dispYn").toString());
        }
        catch (NullPointerException ne) {
            vo.setDispYn("Y");
        }
        vo.setUsedYn("Y");

        vo.setModDt(DateUtils.getCurrentDate());
        vo.setModIp(paraMap.get("ipaddr").toString());
        vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        try {
            vo.setMoblNo(Long.parseLong(paraMap.get("moblNo").toString()));
        }
        catch (NullPointerException ne) {
            vo.setMoblNo(0L);
        }
        MobileInfo chkvo = mobileInfoRepo.findByCustNoAndMoblNoAndUsedYn(custNo,vo.getMoblNo(), "Y");
        //MobileInfo chkvo = mobileInfoRepo.findByDeviceIpAndDevicePortAndDeviceNoAndUsedYn(vo.getDeviceIp(), vo.getDevicePort(), vo.getDeviceNo(), "Y");
        if (chkvo != null) {
            vo.setSpotEquipNo(chkvo.getSpotEquipNo());
            vo.setMoblNo(chkvo.getMoblNo());
            vo.setRegId(chkvo.getRegId());
            vo.setRegIp(chkvo.getRegIp());
            vo.setRegDt(chkvo.getRegDt());
        }
        else {
            vo.setMoblNo(0L);
            vo.setRegDt(DateUtils.getCurrentDate());
            vo.setRegIp(paraMap.get("ipaddr").toString());
            vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        vo.setCustNo(custNo);
        mobileInfoRepo.save(vo);
    }

    @Override
    public void dropTabletInfo(Map<String, Object> paraMap) {
        Long moblNo = Long.parseLong(paraMap.get("moblNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MobileInfo vo = mobileInfoRepo.findByCustNoAndMoblNoAndUsedYn(custNo,moblNo, "Y");
        if (vo != null) {
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentDate());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());
            vo.setCustNo(custNo);
            mobileInfoRepo.save(vo);
        }
    }
    @Override
    public Map<String, Object> getTabletInfo(Map<String, Object> paraMap) {
        return mapper.getTabletInfo(paraMap);
    }

    @Override
    public Map<String, Object> getTabletInfoBySysMenu(Map<String, Object> paraMap) {
        return mapper.getTabletInfoBySysMenu(paraMap);
    }

    @Override
    public List<Map<String, Object>> getTabletList(Map<String, Object> paraMap) {
        return mapper.getTabletList(paraMap);
    }
    @Override
    public int getTabletListCount(Map<String, Object> paraMap) {
        return mapper.getTabletListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboSpot(Map<String, Object> paraMap) {
        return mapper.getComboSpot(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboEquip(Map<String, Object> paraMap) {
        return mapper.getComboEquip(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboTabletMenuList(Map<String, Object> paraMap) {
        return mapper.getComboTabletMenuList(paraMap);
    }

    @Override
    public Map<String, Object> startOper(Map<String, Object> paraMap) {
        String tag = "vsvc.modbusService.startOper => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        OperMast mastvo = new OperMast();
        //MakeIndc indcvo = new MakeIndc();

        //태블릿 indcNo의 경우, 원재료입고의 indcNo임 -> 충전에 해당하는 indc_no를 가져와서 저장해주어야 함.
        Long indcNo = 0L;
        Long procCd = Long.parseLong(paraMap.get("procCd").toString());

        try{
            indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        }catch(NullPointerException ne){
            indcNo = 0L;
        }

        MakeIndc chkmivo = makeIndcRepo.findByCustNoAndParIndcNoAndProcCdAndUsedYn(custNo,indcNo, procCd, "Y");
        if(chkmivo != null){
            mastvo.setIndcNo(chkmivo.getIndcNo());
        }else{
            mastvo.setIndcNo(0L);
        }

        try {
            mastvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        }
        catch (NullPointerException ne) {
            mastvo.setProdNo(0L);
        }

        try{
            mastvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        }catch(NullPointerException ne){
            mastvo.setSpotEquipNo(0L);
        }


        try{
            mastvo.setDeviceNo(Long.parseLong((paraMap.get("deviceNo").toString())));
        }
        catch(NullPointerException ne){
            mastvo.setDeviceNo(0L);
        }

        try{
            mastvo.setEquipNo(Long.parseLong((paraMap.get("equipNo").toString())));
        }
        catch(NullPointerException ne){
            mastvo.setEquipNo(0L);
        }

        try{
            String frHh = paraMap.get("frHh").toString();
            String frMm = paraMap.get("frMm").toString();

            String frDate = DateUtils.getCurrentDateString();
            StringBuffer buf = new StringBuffer();
            String frHm = String.valueOf(buf.append(frDate).append(frHh).append(frMm));

            mastvo.setFrHm(sdf.parse(frHm));
        }catch(NullPointerException ne){

        }catch (ParseException e){
                e.printStackTrace();
        }

        try{
            mastvo.setToHm(sdf.parse(paraMap.get("toHm").toString()));
        }
        catch(ParseException e){

        }
        catch(NullPointerException ne){

        }

        try {
            mastvo.setOperTp(Integer.parseInt(paraMap.get("operTp").toString())); //동작구분 0:정상, 1:(시편)테스트*/
        }
        catch (NullPointerException ne) {
            mastvo.setOperTp(0); //동작구분 0:정상, 1:(시편)테스트*/
        }

        //시편테스트 종류 - 1402 : 시편 , 1403 : 시편 + 제품, 1404 : SUS , 1405 : SUS+제품
        try{
            mastvo.setTestTp(Integer.parseInt(paraMap.get("testTp").toString()));
        }catch(NullPointerException ne){

        }



        mastvo.setFrUnixHms(System.currentTimeMillis() / 1000);
        mastvo.setToUnixHms(0L);
        mastvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
        OperMast mastchkvo = operMastRepo.findByCustNoAndSpotEquipNoAndFrUnixHms(custNo,mastvo.getSpotEquipNo(), mastvo.getFrUnixHms());
        if (mastchkvo != null) {
            mastvo.setOperNo(mastchkvo.getOperNo());
        } else {
            mastvo.setOperNo(0L);
        }
        Map<String,Object> hstrmap  = null;
        mastvo.setCustNo(custNo);
        mastvo = operMastRepo.save(mastvo);
        paraMap.put("operNo",mastvo.getOperNo());


        return StringUtil.voToMap(mastvo);
    }

    @Override
    public void stopOper(Map<String, Object> paraMap) {
        String tag = " ModbusService.stopOper ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        Map<String,Object> rmap = new HashMap<>();
        Long procCd = null;
        Long indcNo = null;
        Float indcQty = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        log.info(tag + "종료계수기 설정.................");

        //mapper.updateOperMastToQty(rmap); //최종수량 설정 Remarked by KMJ AT 21.08.26 17:48
        //SOL AddOn By KMJ AT 21.08.26 17:48
        OperMast omvo = operMastRepo.findByCustNoAndOperNo(custNo,Long.parseLong(paraMap.get("operNo").toString()));
        if (omvo != null) {
            try {
                omvo.setMetalQty(Long.parseLong(paraMap.get("metalQty").toString()));
            }
            catch (NullPointerException ne) {
                omvo.setMetalQty(0L);
            }
            omvo.setToUnixHms(System.currentTimeMillis() / 1000);
            omvo.setFrQty(Float.parseFloat(paraMap.get("frQty").toString()));
            omvo.setToQty(Float.parseFloat(paraMap.get("toQty").toString()));
//            omvo.setPassQty(Float.parseFloat(paraMap.get("passQty").toString()));
            try {
                omvo.setWeightQty(Long.parseLong(paraMap.get("weightQty").toString()));
            }
            catch (NullPointerException ne) {
                omvo.setWeightQty(0L);
            }
            try {
                omvo.setAdjQty(Float.parseFloat(paraMap.get("adjQty").toString()));
            }
            catch (NullPointerException ne) {
                omvo.setAdjQty(0f);
            }

            try{
                omvo.setMetalQty(Long.parseLong(paraMap.get("metalQty").toString()));
            }catch(NullPointerException ne){
                omvo.setMetalQty(0L);
            }

            try{
                String toHh = paraMap.get("toHh").toString();
                String toMm = paraMap.get("toMm").toString();

                String toDate = DateUtils.getCurrentDateString();
                StringBuffer buf = new StringBuffer();
                String toHm = String.valueOf(buf.append(toDate).append(toHh).append(toMm));

                omvo.setToHm(sdf.parse(toHm));
            }catch(NullPointerException ne){

            }catch (ParseException e){
                e.printStackTrace();
            }


            omvo = operMastRepo.save(omvo);
        }
        //EOL AddOn By KMJ AT 21.08.26 17:48

        //Map<String, Object> prodMap = prodMapper.getProdInfo(rmap); Remarked By KMJ AT 21.08.25 17:50
        ProdInfo pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,Long.parseLong(paraMap.get("prodNo").toString()),"Y");
        try{
            procCd = Long.parseLong(paraMap.get("procCd").toString());
        }catch (NullPointerException ne){
            procCd = 0L;
        }
        try{
            indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        }catch(NullPointerException ne){
            indcNo = 0L;
        }

        MakeIndc chkmivo = makeIndcRepo.findByCustNoAndParIndcNoAndProcCdAndUsedYn(custNo,indcNo, procCd, "Y");
        if(chkmivo != null){
            indcQty = chkmivo.getIndcQty();
            indcNo = chkmivo.getIndcNo();
        }
        else{
            indcQty = 1F;
            indcNo = 0L;
        }

        rmap.put("indcNo", indcNo);

        omvo = operMastRepo.findByCustNoAndOperNo(custNo,omvo.getOperNo());
        Float adjPassWgt = null;
        Long IndcRsltNo = null;

        Map<String, Object> sterTimeMap = mapper.getSterTime(rmap); //살균시간 계산산

        MakeIndcRslt mirvo = new MakeIndcRslt();
        try{
            mirvo.setIndcNo(indcNo);
        }catch(NullPointerException ne){
            mirvo.setIndcNo(0L);
        }

        log.info(paraMap.get("adjQty") +"adjPassQty!!!");

        adjPassWgt = omvo.getAdjQty() * pivo.getVol() * 1000;
        mirvo.setAdjMakeWgt(adjPassWgt);
        mirvo.setAdjMakeQty(adjPassWgt);
        mirvo.setUsedYn("Y");

        IndcRsltNo = 0L;

        MakeIndcRslt mirchkvo = makeIndcRsltRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,IndcRsltNo, "Y");
        if(mirchkvo != null) {
            mirvo.setIndcRsltNo(mirchkvo.getIndcRsltNo());
            mirvo.setMakeDt(mirchkvo.getMakeDt());
            try{
                if(omvo.getPassQty() == -999){
                    mirvo.setMakeQty(mirchkvo.getMakeQty());
                    mirvo.setMakeWgt(mirchkvo.getMakeWgt());
                    mirvo.setRealYield(mirchkvo.getRealYield());
                }
                else{
                    mirvo.setMakeQty(omvo.getPassQty());
                    mirvo.setMakeWgt(omvo.getPassQty() * pivo.getVol() * 1000);
                    mirvo.setRealYield(omvo.getPassQty() / indcQty);
                }

            }catch(NullPointerException ne){
                mirvo.setMakeQty(mirchkvo.getMakeQty());
                mirvo.setMakeWgt(mirchkvo.getMakeWgt());
                mirvo.setRealYield(mirchkvo.getRealYield());
            }

            try{
                if(omvo.getWeightQty() == -999){
                    mirvo.setWgtQty(mirchkvo.getWgtQty());
                }else{
                    mirvo.setWgtQty(omvo.getWeightQty());
                }
            }catch(NullPointerException ne){
                mirvo.setWgtQty(mirchkvo.getWgtQty());
            }

            try{
                if(omvo.getMetalQty() == -999){
                    mirvo.setMetalQty(mirchkvo.getMetalQty());
                }else{
                    mirvo.setMetalQty(Long.parseLong(omvo.getMetalQty().toString()));
                }
            }catch(NullPointerException ne){
                mirvo.setMetalQty(mirchkvo.getMetalQty());
            }

            try{
                mirvo.setSterTime(Long.parseLong(sterTimeMap.get("sterTime").toString()));
            }catch(NullPointerException ne){
                mirvo.setSterTime(0L);
            }

            try{
                if(omvo.getSterTmpr() == -999){
                    mirvo.setSterTmpr(mirchkvo.getSterTmpr());
                }else{
                    mirvo.setSterTmpr(omvo.getSterTmpr());
                }
            }catch (NullPointerException ne){
                mirvo.setSterTmpr(mirchkvo.getSterTmpr());
            }

            mirvo.setPackQty(mirchkvo.getPackQty());
            mirvo.setSznQty(mirchkvo.getSznQty());
            mirvo.setAdjMakeQty(Float.parseFloat(paraMap.get("adjPassQty").toString()));

            mirvo.setRegIp(paraMap.get("ipaddr").toString());
            mirvo.setRegDt(DateUtils.getCurrentDateTime());
            mirvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        else { //MakeIndcRslt가 존재하지 않은 경우
            mirvo.setMakeDt(DateUtils.getCurrentDate());
            mirvo.setIndcNo(indcNo);
            try{
                if(omvo.getPassQty() == -999){
                    mirvo.setMakeQty(0F);
                    mirvo.setMakeWgt(0F);
                    mirvo.setRealYield(0F);
                }
                else{
                    log.info(tag +"연동생산결과처리.indcNo = " + omvo.getIndcNo());
                    log.info(tag +"연동생산결과처리.passQty = " + omvo.getPassQty());
                    log.info(tag +"연동생산결과처리.wgtQty = " + omvo.getWeightQty());
                    mirvo.setMakeQty(omvo.getPassQty());
                    mirvo.setMakeWgt(omvo.getPassQty() * pivo.getVol() * 1000);
                    mirvo.setRealYield(omvo.getPassQty() / indcQty);
                }

            }catch(NullPointerException ne){
                mirvo.setMakeQty(0F);
                mirvo.setMakeWgt(0F);
                mirvo.setRealYield(0F);
            }

            try{
                if(omvo.getWeightQty() == -999){
                    mirvo.setWgtQty(0L);
                }else{
                    mirvo.setWgtQty(Long.parseLong(omvo.getWeightQty().toString()));
                }
            }catch(NullPointerException ne){
                mirvo.setWgtQty(0L);
            }

            try{
                if(omvo.getMetalQty() == -999){
                    mirvo.setMetalQty(0L);
                }else{
                    mirvo.setMetalQty(Long.parseLong(omvo.getMetalQty().toString()));
                }
            }catch(NullPointerException ne){
                mirvo.setMetalQty(0L);
            }

            try{
                mirvo.setSterTime(Long.parseLong(sterTimeMap.get("sterTime").toString()));
            }catch(NullPointerException ne){
                mirvo.setSterTime(0L);
            }

            try{
                if(omvo.getSterTmpr() == -999){
                    mirvo.setSterTmpr(0F);
                }else{
                    mirvo.setSterTmpr(omvo.getSterTmpr());
                }
            }catch (NullPointerException ne){
                mirvo.setSterTmpr(0F);
            }

            mirvo.setPackQty(0L);
            mirvo.setSznQty(0L);
            mirvo.setAdjMakeQty(Float.parseFloat(paraMap.get("adjQty").toString()));

            mirvo.setRegIp(paraMap.get("ipaddr").toString());
            mirvo.setRegDt(DateUtils.getCurrentDateTime());
            mirvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        mirvo.setCustNo(custNo);
        makeIndcRsltRepo.save(mirvo);
    }

    @Override
    public Map<String, Object> startMoniter(Map<String, Object> paraMap) {
        OperMast mastvo = new OperMast();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        mastvo.setProdNo(0L);
        mastvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        mastvo.setOperTp(0); /*동작구분 0:정상, 1:(시편)테스트 */
        mastvo.setFrUnixHms(System.currentTimeMillis() / 1000);
        mastvo.setToUnixHms(0L);
        mastvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));

       // mapper.initOperMoniter(paraMap);
        OperMast mastchkvo = operMastRepo.findByCustNoAndSpotEquipNo(custNo,mastvo.getSpotEquipNo());
        if (mastchkvo != null) {
            mastvo.setOperNo(mastchkvo.getOperNo());
        }
        else {
            mastvo.setOperNo(0L);
        }
        mastvo.setCustNo(custNo);
        mastvo = operMastRepo.save(mastvo);
        return StringUtil.voToMap(mastvo);
    }
    /*품명 초성검색*/
    @Override
    public List<Map<String, Object>> getInitialProdList(Map<String, Object> paraMap) {
        return mapper.getInitialProdList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getSoptEquipValHist(Map<String, Object> paraMap) {
        return mapper.getSoptEquipValHist(paraMap);
    }

    @Override
    public int getSoptEquipValHistCount(Map<String, Object> paraMap) {
        return mapper.getSoptEquipValHistCount(paraMap);
    }

    @Override
    public void saveModbusData(Map<String, Object> paraMap) {
        EquipMngrHstr hstrvo = new EquipMngrHstr();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        hstrvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        hstrvo.setOperNo(Long.parseLong(paraMap.get("operNo").toString()));

        try {
            hstrvo.setMeasVal(Float.parseFloat(paraMap.get("measVval").toString()));  //서울식품인경우 on = 0, off= 1로만 들어옴.
            if(hstrvo.getMeasVal() == 0) {
                String operTm = paraMap.get("operTm").toString();
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
                sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
                try {
                    hstrvo.setUnixHms(sdf.parse(operTm).getTime() / 1000);

                } catch (ParseException e) {
                    hstrvo.setUnixHms(System.currentTimeMillis() / 1000);
                }
            }
            else {
                hstrvo.setUnixHms(System.currentTimeMillis() / 1000);
            }
        }
        catch (NullPointerException ne) {
            hstrvo.setMeasVal(0f);
        }

        hstrvo.setMinLmtVal(Float.parseFloat(paraMap.get("maxLmtVal").toString()));
        hstrvo.setMaxLmtVal(Float.parseFloat(paraMap.get("minLmtVal").toString()));

        try {
            hstrvo.setFeYn (paraMap.get("fe_yn").toString().equals("true") ? "Y":"N");
        }
        catch (NullPointerException ne) {
            hstrvo.setFeYn("N");
        }
        try {
            hstrvo.setSusYn (paraMap.get("susYn").toString().equals("true") ? "Y":"N");
         }
        catch (NullPointerException ne) {
            hstrvo.setSusYn("N");
        }
        try {
            hstrvo.setProdYn (paraMap.get("prodYn").toString().equals("true") ? "Y":"N");
        }
        catch (NullPointerException ne) {
            hstrvo.setProdYn("N");
        }
        hstrvo.setUsedYn("Y");

        EquipMngrHstr chkvo = equipMngrHstrRepo.findBySpotEquipNoAndUnixHms(hstrvo.getSpotEquipNo(), hstrvo.getUnixHms());
        if (chkvo != null) {
            hstrvo.setMngrHstrNo(chkvo.getMngrHstrNo());
        }
        else {
            hstrvo.setMngrHstrNo(0L);
        }
        hstrvo.setCustNo(custNo);
        equipMngrHstrRepo.save(hstrvo);
    }

    /*꼬치접이*/
    @Override
    public void saveModbusWrinkle(Map<String, Object> paraMap) {
        String tag = "ModbusService.saveModbusWrinkle => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        EquipMngrHstr hstrvo = new EquipMngrHstr();
        hstrvo.setSpotEquipNo(Long.parseLong(paraMap.get("spotEquipNo").toString()));
        hstrvo.setOperNo(Long.parseLong(paraMap.get("operNo").toString()));
        long measVal = Long.parseLong(paraMap.get("measVal").toString());
        if (measVal < 0) {
            long y = 0xffffffffL & measVal;
        }
        log.info(tag + "converted measval = " + measVal);
        hstrvo.setMeasVal((float)measVal);
        hstrvo.setUnixHms(System.currentTimeMillis() / 1000);
        hstrvo.setMinLmtVal(Float.parseFloat(paraMap.get("maxLmtval").toString()));
        hstrvo.setMaxLmtVal(Float.parseFloat(paraMap.get("minLmtVal").toString()));
        hstrvo.setFeYn ("N");
        hstrvo.setSusYn("N");
        hstrvo.setProdYn("Y");
        hstrvo.setUsedYn("Y");

        EquipMngrHstr chkvo = equipMngrHstrRepo.findBySpotEquipNoAndUnixHms(hstrvo.getSpotEquipNo(), hstrvo.getUnixHms());
        if (chkvo != null) {
            hstrvo.setMngrHstrNo(chkvo.getMngrHstrNo());
        }
        else {
            hstrvo.setMngrHstrNo(0L);
        }
        hstrvo.setCustNo(custNo);
        equipMngrHstrRepo.save(hstrvo);

        OperMast mastvo = operMastRepo.findByCustNoAndOperNo(custNo,hstrvo.getOperNo());
        if (mastvo != null) {
            if (measVal >= 0) {
                mastvo.setFrQty((float) measVal);
            }
        }
    }
    @Override
    public void saveModbusFail(Map<String, Object> paraMap) {
        OperMast mastvo = new OperMast();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        mastvo.setOperNo(Long.parseLong(paraMap.get("operNo").toString()));
        OperMast chkvo = operMastRepo.findByCustNoAndOperNo(custNo,mastvo.getOperNo());
        if (chkvo != null) {
            chkvo.setOperTp(Integer.parseInt(paraMap.get("operTp").toString()));
            chkvo.setToUnixHms(System.currentTimeMillis()/1000);
            chkvo.setMetalQty(0L);
            chkvo.setPassQty(0f);
            operMastRepo.save(chkvo);
        }
    }

    /*자재출고요청 처리용:tablet*/
    @Override
    public List<Map<String, Object>> getTrkList(Map<String, Object> paraMap) {
        return mapper.getTrkList(paraMap);
    }

    @Override
    public int getTrkListCount(Map<String, Object> paraMap) {
        return mapper.getTrkListCount(paraMap);
    }

    @Override
    public void saveTmprAndHumy(Map<String, Object> paraMap){
        TmprHstr thvo = new TmprHstr();
        thvo.setEquipNo(Long.parseLong(paraMap.get("equipNo").toString()));
        thvo.setMeasDt(DateUtils.getCurrentDate());
        thvo.setMeasHumy(Float.parseFloat(paraMap.get("measHumy").toString()));
        thvo.setMeasTmpr(Float.parseFloat(paraMap.get("measTmpr").toString()));
        thvo.setMeasNo(0L);

//        tmprHstrRepo.save(thvo);
    }

    @Override
    public TmprHstr saveTmprHstr(Map<String, Object> paraMap) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        paraMap.put("spotEquipNo",7819);//가공실온도
        List<Map<String,Object>> dst = mapper.getTmprHstrList(paraMap);
        paraMap.put("spotEquipNo",7822);//가공싱습도
        List<Map<String,Object>> hst = mapper.getTmprHstrList(paraMap);
        paraMap.put("maxYn","Y");
        Map<String,Object> tmap = null;
        Map<String,Object> hmap = null;
        int idx = -1;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        TmprHstr rtnvo = null;
        while(++idx < dst.size()) {
            tmap = dst.get(idx);
            Long tUnixHms = Long.parseLong(tmap.get("unixHms").toString());
            Long hUnixHms = Long.parseLong(hmap.get("unixHms").toString());
            TmprHstr thvo = new TmprHstr();
            try {
                thvo.setMeasDt(sdf.parse(tmap.get("chkDt").toString()));
                thvo.setUnixHms(tUnixHms);
                thvo.setMeasTmpr(Float.parseFloat(tmap.get("measVal").toString()));
                thvo.setEquipNo(Long.parseLong(tmap.get("equipNo").toString()));

                paraMap.put("unixHms",hUnixHms);
                List<Map<String,Object>> dsh = mapper.getTmprHstrList(paraMap);

                thvo.setMeasHumy(Float.parseFloat(hmap.get("measVal").toString()));
                TmprHstr chkvo = tmprHstrRepo.findByCustNoAndUnixHms(custNo,thvo.getUnixHms());
                if (chkvo != null) {
                    thvo.setMeasNo(chkvo.getMeasNo());
                }
                else {
                    thvo.setMeasNo(0L);
                }
                rtnvo = tmprHstrRepo.save(thvo);

            } catch (ParseException e) {
                e.printStackTrace();
            }
          }
        return rtnvo;
    }

    @Override
    public List<Map<String, Object>> getTmprHstrList(Map<String, Object> paraMap) {
        return mapper.getTmprHstrList(paraMap);
    }

    @Override
    public int getTmprHstrListCount(Map<String, Object> paraMap) {
        return mapper.getTmprHstrListCount(paraMap);
    }

    //pkgcnt2 저장
//    @Transactional
    @Override
    public void saveOperData(Map<String, Object> paraMap){
        //operMas 저장하기.
//        OperMast omvo = new OperMast();
//
//        omvo.setOperNo(Long.parseLong(paraMap.get("oper_no").toString()));
//        OperMast chkvo = operMastRepo.findByOperNo(omvo.getOperNo());
//        if (chkvo != null) {
//            chkvo.setToUnixHms(System.currentTimeMillis() / 1000);
//            try{
//                chkvo.setMetalQty(Float.parseFloat(paraMap.get("metal_qty").toString()));
//            }
//            catch(NullPointerException ne){
//                chkvo.setMetalQty(0F);
//            }
//            chkvo.setPassQty(Float.parseFloat(paraMap.get("pass_qty").toString()));
//            operMastRepo.save(chkvo);
//        }

        //makeIndc에 저장 - 기존 정보는
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = makeIndcMapper.getProdProcList(paraMap);
        Map<String, Object> procFirst = ds.get(0);
        Map<String, Object> procLast = ds.get(Integer.parseInt(String.valueOf(ds.size())) - 1);

        MakeIndc mivo = new MakeIndc();
        Float wgt = 0F;
        try{
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        }catch(NullPointerException ne){
            mivo.setIndcNo(0L);
        }
        mivo.setUsedYn("Y");

        MakeIndc chkmivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,mivo.getIndcNo(), mivo.getUsedYn());
        if(chkmivo != null){
            mivo.setStatCd(Long.parseLong(env.getProperty("code.work_stat.end")));
            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));

            mivo.setParIndcNo(chkmivo.getParIndcNo());
            mivo.setMaxMakeQty(chkmivo.getMaxMakeQty());
            mivo.setMaxMakeWgt(chkmivo.getMaxMakeWgt());
            mivo.setOperRt(chkmivo.getOperRt());
            mivo.setRealYield(chkmivo.getRealYield());
            mivo.setCtlFillYield(chkmivo.getCtlFillYield());
            mivo.setFaultRt(chkmivo.getFaultRt());
            mivo.setFillYield(chkmivo.getFillYield());
            mivo.setIdxNo(chkmivo.getIdxNo());
            mivo.setIndcDt(chkmivo.getIndcDt());
            mivo.setMakeFrDt(chkmivo.getMakeFrDt());
            mivo.setMakeToDt(chkmivo.getMakeToDt());
            mivo.setMakeUnit(chkmivo.getMakeUnit());
            mivo.setIndcWgt(chkmivo.getIndcWgt());
            mivo.setIndcQty(chkmivo.getIndcQty());

            mivo.setModDt(DateUtils.getCurrentDate());
            mivo.setModIp(paraMap.get("ipaddr").toString());
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));

            makeIndcRepo.save(mivo);
            this.saveMakeIndcRsltFromOperData(paraMap);
        }
        else{
            Long svParIndcNo = 0L;

            for (int i = 0; i < 2; i++) {
                Map<String, Object> chkmap = new HashMap<String, Object>();
                Float maxMakeWgt = 0F;

                MakeIndc mivo2 = new MakeIndc();

                mivo2.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
                mivo2.setUsedYn("Y");
                try{
                    mivo2.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString()));
                }catch(NullPointerException ne){
                    mivo2.setOrdNo(0L);
                }

                if(i == 0){
                    mivo2.setIndcNo(mivo.getIndcNo());
                    mivo2.setProcCd(Long.parseLong(procFirst.get("value").toString()));
                    mivo2.setParIndcNo(0L);
                }
                else{
                    mivo2.setIndcNo(0L);
                    mivo2.setProcCd(Long.parseLong(procLast.get("value").toString()));
                    mivo2.setParIndcNo(svParIndcNo);
                }

                chkmap.put("procCd", mivo2.getProcCd());
                Map<String, Object> procmap = procMapper.getProcInfo(chkmap);
                mivo2.setMaxMakeQty(Float.parseFloat(procmap.get("maxMakeQty").toString()));

                maxMakeWgt = mivo2.getMaxMakeQty() * Float.parseFloat(paraMap.get("mess").toString()) / 1000;
                mivo2.setMaxMakeWgt(maxMakeWgt);

                mivo2.setOperRt(0F);
                mivo2.setRealYield(0F);
                mivo2.setCtlFillYield(0F);
                mivo2.setFaultRt(0F);
                mivo2.setFillYield(0F);
                mivo2.setIdxNo(0);
                mivo2.setIndcDt(DateUtils.getCurrentDate());
                mivo2.setMakeFrDt(DateUtils.getCurrentDate());
                mivo2.setMakeToDt(DateUtils.getCurrentDate());
                mivo2.setMakeUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));

                mivo2.setIndcQty(Float.parseFloat(paraMap.get("passQty").toString()));

                wgt = (mivo2.getIndcQty() * Float.parseFloat(paraMap.get("mess").toString())) / 1000;
                mivo2.setIndcWgt(wgt);

                mivo2.setManualYn("N");
                mivo2.setStatCd(Long.parseLong(env.getProperty("code.work_stat.end")));
                mivo2.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));

                mivo2.setRegDt(DateUtils.getCurrentDate());
                mivo2.setRegIp(paraMap.get("ipaddr").toString());
                mivo2.setRegId(Long.parseLong(paraMap.get("userId").toString()));

                mivo = makeIndcRepo.save(mivo2);

                svParIndcNo = mivo.getIndcNo();
                paraMap.put("indc_no", svParIndcNo);

                this.saveMakeIndcRsltFromOperData(paraMap);
            }
        }


//        else {
//            Map<String, Object> chkmap = new HashMap<String, Object>();
//            Float maxMakeWgt = 0F;
//
//            mivo.setStatCd(Long.parseLong(env.getProperty("code.work_stat.end")));
//            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));
//            mivo.setParIndcNo(0L);
//
//            mivo.setProcCd(Long.parseLong(procLast.get("value").toString()));
//
//            chkmap.put("procCd", mivo.getProcCd());
//            Map<String, Object> procmap = procMapper.getProcInfo(chkmap);
//            mivo.setMaxMakeQty(Float.parseFloat(procmap.get("max_make_qty").toString()));
//
//            maxMakeWgt = mivo.getMaxMakeQty() * Float.parseFloat(paraMap.get("mess").toString()) / 1000;
//            mivo.setMaxMakeWgt(maxMakeWgt);
//
//            mivo.setIndcQty(Float.parseFloat(paraMap.get("pass_qty").toString()));
//            wgt = mivo.getIndcQty() * Float.parseFloat(paraMap.get("mess").toString()) / 1000;
//            mivo.setIndcWgt(wgt);
//
//            mivo.setOperRt(0F);
//            mivo.setRealYield(0F);
//            mivo.setCtlFillYield(0F);
//            mivo.setFaultRt(0F);
//            mivo.setFillYield(0F);
//            mivo.setIdxNo(0);
//            mivo.setIndcDt(DateUtils.getCurrentDate());
//            mivo.setMakeFrDt(DateUtils.getCurrentDate());
//            mivo.setMakeToDt(DateUtils.getCurrentDate());
//            mivo.setMakeUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));
//
//            mivo.setRegDt(DateUtils.getCurrentDate());
//            mivo.setRegIp(paraMap.get("ipaddr").toString());
//            mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
//
//            mivo = makeIndcRepo.save(mivo);
//            paraMap.put("indc_no", mivo.getIndcNo());
//        }

    }

    public void saveMakeIndcRsltFromOperData(Map<String, Object> paraMap){
        MakeIndcRslt mirvo = new MakeIndcRslt();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Float wgt = 0F;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc mivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,Long.parseLong(paraMap.get("indc_no").toString()), "Y");
        if(mivo != null){
            mirvo.setIndcNo(mivo.getIndcNo());
        }

        mirvo.setUsedYn("Y");
        mirvo.setMetalQty(Long.parseLong(paraMap.get("metalQty").toString()));
        mirvo.setMakeQty(Float.parseFloat(paraMap.get("passQty").toString()));
        try{
            mirvo.setMakeDt(sdf.parse(paraMap.get("makeDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            mirvo.setMakeDt(DateUtils.getCurrentDate());
        }


        wgt = Float.parseFloat(paraMap.get("passQty").toString()) * Float.parseFloat(paraMap.get("mess").toString()) / 1000;
        mirvo.setMakeWgt(wgt);

        mirvo.setPackQty(0L);
        mirvo.setWgtQty(0L);
        mirvo.setSznQty(0L);

        MakeIndcRslt chkmir = makeIndcRsltRepo.findByCustNoAndIndcNoAndUsedYn(custNo,mirvo.getIndcNo(), mirvo.getUsedYn());
        if(chkmir != null){
            mirvo.setIndcRsltNo(chkmir.getIndcRsltNo());
            mirvo.setModDt(DateUtils.getCurrentDate());
            mirvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setModIp(paraMap.get("ipaddr").toString());
        }
        else{
            mirvo.setIndcRsltNo(0L);
            mirvo.setMakeDt(DateUtils.getCurrentDate());

            mirvo.setRegDt(DateUtils.getCurrentDate());
            mirvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setRegIp(paraMap.get("ipaddr").toString());
        }

        makeIndcRsltRepo.save(mirvo);
    }

    @Override
    public void matchEquipMngrHstr(Map<String, Object> paraMap){
        mapper.updateEquipMngrHstr(paraMap);
    }

    @Override
    public void stopEquipMngrHstr(Map<String, Object> paraMap){
        mapper.stopEquipMngrHstr(paraMap);
    }

    @Override
    public void setOperMastQty(Map<String, Object> paraMap) {
        String tag = "modbusService.setOperMastQty => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        float passQty = 0f; //통과수량`
        Long wgtQty = 0L; //중량미달수량
        Long metalQty = 0L; //금속검출수량

        log.info(tag + "paraMap = " + paraMap.toString());
        Long operNo = Long.parseLong(paraMap.get("operNo").toString());
        Long spotEquipNo = Long.parseLong(paraMap.get("spotEquipNo").toString());
        passQty = Integer.parseInt(paraMap.get("passQty").toString());
        try {
            wgtQty = Long.parseLong(paraMap.get("wgtQty").toString());
        }
        catch (NullPointerException ne) {
            wgtQty = 0L;
        }
        try {
            metalQty = Long.parseLong(paraMap.get("metalQty").toString());
        }
        catch (NullPointerException ne) {
            metalQty = 0L;
        }

        OperMast omvo = operMastRepo.findByCustNoAndOperNoAndSpotEquipNo(custNo,operNo,spotEquipNo);
        if (omvo != null) {
            omvo.setOperNo(operNo);
            omvo.setEquipNo(spotEquipNo);
            omvo.setPassQty(passQty); //통과수량
            omvo.setWeightQty(wgtQty); //중량미달
            omvo.setMetalQty(metalQty); //금속검출
            omvo.setCustNo(custNo);
            operMastRepo.save(omvo);
        }
    }
}

