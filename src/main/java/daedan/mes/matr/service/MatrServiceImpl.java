package daedan.mes.matr.service;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CcpType;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.file.service.FileService;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.matr.domain.MatrAttr;
import daedan.mes.matr.domain.MatrCmpy;
import daedan.mes.matr.domain.MatrCmpyCart;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.mapper.MatrMapper;
import daedan.mes.matr.repository.MatrAttrRepository;
import daedan.mes.matr.repository.MatrCmpyCartRepository;
import daedan.mes.matr.repository.MatrCmpyRepository;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.stock.domain.MatrSafeStk;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.domain.WhInfo;
import daedan.mes.stock.repository.MatrSafeStkRepository;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.stock.repository.WhInfoRepository;
import daedan.mes.stock.service.StockService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("matrService")
public class MatrServiceImpl implements MatrService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;
    @Autowired
    private OrdProdRepository ordprRepo;
    @Autowired
    private MatrRepository matrRepository;
    @Autowired
    private MatrRepository matrRepo;
    @Autowired
    private MatrAttrRepository matrAttrRepo;
    @Autowired
    private FileService fileService;
    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private WhInfoRepository whinfoRepo;

    @Autowired
    private CmpyRepository cmpyRepo;


    @Autowired
    private MatrCmpyRepository matrCmpyRepository;

    @Autowired
    private ProdOwhRepository owhRepo;

    @Autowired
    private MatrSafeStkRepository matrSafeStkRepository;
    @Autowired
    private ProdRepository prodRep;
    @Autowired
    private OrdRepository ordRepo;
    @Autowired
    private ProdStkRepository stkRepo;
    @Autowired
    private ProdBomRepository prodBomRepo;

     @Autowired
    private MatrCmpyCartRepository matrCmpyCartRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private MatrMapper matrMapper;


    @Override
    public List<Map<String, Object>> getMatrList(Map<String, Object> paraMap) {
        String tag = "vsvc.MatrService.getMatrList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return matrMapper.getMatrList(paraMap);
    }
    @Override
    public int getMatrListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrExcelList(Map<String, Object> paraMap) {
        return matrMapper.getMatrExcelList(paraMap);
    }



    @Override
    public List<Map<String, Object>> getMatrWithCmpyList(Map<String, Object> paraMap) {
        return  matrMapper.getMatrWithCmpyList(paraMap);
    }

    @Override
    public int getMatrWithCmpyListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrWithCmpyListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap) {
        return matrMapper.getProdBomList(paraMap);
    }
    @Override
    public int getProdBomListCount(Map<String, Object> paraMap) {
        return matrMapper.getProdBomListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMatrInfo(Map<String, Object> paraMap) {
        String tag = " MatrService.getMatrInfo => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String,Object> rmap = matrMapper.getMatrInfo(paraMap);
        StringBuffer buf = new StringBuffer();
        //buf.append(env.getProperty("img.root.path")).append("matr/").append(rmap.get("matr_no")).append(".png");
        buf.append(env.getProperty("base_file_url")).append("matr/").append(rmap.get("matr_no")).append(".png");

        rmap.put("bar_code_url",buf.toString());
        log.info(tag + "bar_code_url => " + rmap.get("bar_code_url"));

        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());

        log.info("what is matrNo => "+matrNo);

        MatrInfo mivo = matrRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");

        log.info("what is mivo => "+mivo);
        if (mivo != null) {
            MatrAttr mavo = matrAttrRepo.findByCustNoAndMatrNoAndUsedYn(custNo,mivo.getMatrNo(), "Y");
            if(mavo != null){
                rmap.put("matrAttr", mavo);
            }
        }
        return rmap;
    }

    @Transactional
    @Override
    public void saveMatr(Map<String, Object> paraMap) {
        String tag = "MatrServiceImpl.saveMatr => ";
        Map<String, Object> mapMatr     = (Map<String, Object>) paraMap.get("matrInfo");
        Map<String, Object> mapSafeStk = (Map<String, Object>) paraMap.get("safeStk");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        log.info(tag + "saveMatr = " + mapMatr.toString());
        MatrInfo matrInfo = new MatrInfo();
        StringBuffer buf = new StringBuffer();
        long userId = (int) paraMap.get("userId");
        String ipaddr = (String) paraMap.get("ipaddr");

        //SOL AddOn by KMJ At 21.05.05 23:00 --?????????????????? ??????????????? ????????? ???????????? ?????? (???????????? ????????? ????????? ???????????? ???????????? ??????))
        try {
            matrInfo.setBaseCalYn(mapMatr.get("baseCalYn").toString());
        } catch (NullPointerException ne) {
            matrInfo.setBaseCalYn("N");
        }
        //EOL AddOn by KMJ At 21.05.05 23:00 --?????????????????? ??????????????? ????????? ???????????? ?????? (???????????? ????????? ????????? ???????????? ???????????? ??????))
        try {
            matrInfo.setSaveTmpr(Long.parseLong(mapMatr.get("saveTmpr").toString()));
        } catch (NullPointerException ne) {
            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room")));
        }
        try {
            matrInfo.setValidTerm(Long.parseLong(mapMatr.get("validTerm").toString()));
            buf.setLength(0);
            buf.append(matrInfo.getValidTerm()).append(" month");
            matrInfo.setStrValidTerm(buf.toString());
        } catch (NullPointerException ne) {
            matrInfo.setValidTerm(0L);
        }
        try {
            matrInfo.setFileNo(Long.parseLong(mapMatr.get("fileNo").toString()));
        } catch (NullPointerException ne) {
            matrInfo.setFileNo(0L);
        }

        try {
            matrInfo.setBrnchNo(Long.parseLong(mapMatr.get("brnchNo").toString()));
        } catch (NullPointerException ne) {
            matrInfo.setBrnchNo(Long.parseLong(env.getProperty("prod_brnch_root")));
        }


        try {
            matrInfo.setMatrNo(Long.parseLong(mapMatr.get("matrNo").toString()));
        } catch (NullPointerException ne) {
            matrInfo.setMatrNo(0L);
        }
        try {
            matrInfo.setVol(Float.parseFloat(mapMatr.get("vol").toString()));
        }
        catch (NullPointerException ne) {
            matrInfo.setVol(0f);
        }
        matrInfo.setMatrTp(Long.parseLong(mapMatr.get("matrTp").toString())); //????????????
        matrInfo.setMatrNm(mapMatr.get("matrNm").toString()); //?????????
        try {
            matrInfo.setItemCd((String) mapMatr.get("itemCd")); //????????????
        }
        catch (NullPointerException ne) {

        }
        try {
            matrInfo.setBaseVolt(mapMatr.get("baseVolt").toString()); //????????????
        }
        catch (NullPointerException ne) {

        }

        try{
            matrInfo.setMade(mapMatr.get("made").toString());
        }catch (NullPointerException en){
        }


        //?????? (??????????????? ????????? ??????????????? ?????? ???????????? ??????.)
        try {
            matrInfo.setVol(Float.parseFloat(mapMatr.get("vol").toString()));
        }
        catch (NullPointerException ne) {
            matrInfo.setVol(1f);
        }
        //?????? (???????????? ????????? ???????????? ??????)
        try{
            matrInfo.setMess(Float.parseFloat(mapMatr.get("mess").toString()));
        }
        catch(NullPointerException ne){
            matrInfo.setMess(matrInfo.getMess());
        }
        //?????? (?????? ?????? 1??? ??????)
        try {
            matrInfo.setSpga(Float.parseFloat(mapMatr.get("spga").toString()));//??????
        }
        catch(NullPointerException ne) {
            matrInfo.setSpga(1f);
        }

        try {
            matrInfo.setMadeby(Long.parseLong(mapMatr.get("madeby").toString())); //??????
        }
        catch (NullPointerException ne) {
            matrInfo.setMadein(0L);
        }
        try {
            matrInfo.setMadein(Long.parseLong(mapMatr.get("madein").toString())); //?????????
        }
        catch (NullPointerException ne) {
            matrInfo.setMadein(0L);
        }
        matrInfo.setSz((String) mapMatr.get("sz"));  //??????
        try {
            matrInfo.setPursUnit(Long.parseLong(mapMatr.get("codeNo").toString())); //????????????
        }catch (NullPointerException en){
            matrInfo.setPursUnit(Long.parseLong(mapMatr.get("pursUnit").toString()));
        }
        try {
            matrInfo.setSaveTmpr(Long.parseLong(mapMatr.get("saveTmpr").toString()));
        }
        catch (NullPointerException ne) {
            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room")));
        }

        //???????????? ??????
        try{
            matrInfo.setBrnchNo(Long.parseLong(mapMatr.get("brnchNo").toString()));//???
        }catch(NullPointerException ne){
            matrInfo.setBrnchNo(10L);//???
        }

        try {
            matrInfo.setValidTerm(Integer.parseInt(mapMatr.get("validTerm").toString()));//????????????
        }
        catch (NullPointerException ne) {
            matrInfo.setValidTerm(0);//????????????
        }


        try {
            matrInfo.setWhNo(Long.parseLong(mapMatr.get("whNo").toString()));//????????????
        }
        catch (NullPointerException ne) {
            matrInfo.setWhNo(0L);//????????????
        }


        try {
            mapMatr.put("file_no", Long.parseLong(mapMatr.get("fileNo").toString()));
        } catch (NullPointerException ne) {
            mapMatr.put("file_no", 0L);
        }
        try {
            matrInfo.setFileNo(Long.parseLong(mapMatr.get("fileno").toString()));
        }
        catch (NullPointerException ne) {
            matrInfo.setFileNo(0L);
        }
        matrInfo.setUsedYn("Y");
        matrInfo.setModId(userId);
        matrInfo.setModIp(ipaddr);
        matrInfo.setModDt(DateUtils.getCurrentBaseDateTime());
        matrInfo.setRegId(userId);
        matrInfo.setRegIp(ipaddr);
        matrInfo.setRegDt(DateUtils.getCurrentBaseDateTime());
        try {
            matrInfo.setMatrNo(Long.parseLong(mapMatr.get("matrNo").toString()));
        }
        catch (NullPointerException ne) {
            MatrInfo chkvo = matrRepository.findByCustNoAndItemCdAndMatrNmAndSzAndUsedYn(custNo,matrInfo.getItemCd(), matrInfo.getMatrNm(),matrInfo.getSz(), "Y");
            if (chkvo != null) {
                matrInfo.setMatrNo(chkvo.getMatrNo());
                matrInfo.setRegId(userId);
                matrInfo.setRegIp(ipaddr);
                matrInfo.setRegDt(DateUtils.getCurrentBaseDateTime());
            }
        }
        matrInfo.setCustNo(custNo);
        matrInfo = matrRepository.save(matrInfo);
        if (custNo == 6) {
            mapMatr.put("matrNo", matrInfo.getMatrNo());
            mapMatr.put("custNo", custNo);
            MatrAttr mavo = this.saveMatrAttr(mapMatr);
            matrInfo.setMatrAttr(mavo);
        }
        matrInfo = matrRepository.save(matrInfo);

        /*????????????????????????
        Map<String,Object> bmap = new HashMap<String,Object>();
        bmap.put("codeNo",matrInfo.getMatrNo());
        bmap.put("savePath","matr/");
        stockService.makeBarCode(bmap);
         */

        /* 2021.03.24 ????????? ????????? ????????? ??????????????? ???
        if (cmpyList != null) {
            for (Map<String, Object> el : cmpyList) {
                el.put("userId", userId);
                el.put("ipaddr", ipaddr);
                MatrCmpy matrCmpy = new MatrCmpy();
                matrCmpy.setCmpyNo(Long.parseLong(el.get("cmpy_no").toString()));
                matrCmpy.setUsedYn("Y");
                try {
                    matrCmpy.setDefaultYn(el.get("defaultYn").toString());
                }
                catch (NullPointerException ne) {
                    matrCmpy.setDefaultYn("N");
                }
                matrCmpy.setMatrNo(matrInfo.getMatrNo());
                matrCmpy.setModId(userId);
                matrCmpy.setModIp(ipaddr);
                matrCmpy.setModDt(DateUtils.getCurrentDate());
                try {
                    matrCmpy.setMatrCmpyNo(Long.parseLong(el.get("matr_cmpy_no").toString()));
                }
                catch (NullPointerException ne) {
                    MatrCmpy ckvo = matrCmpyRepository.findByMatrNoAndCmpyNoAndUsedYn(matrCmpy.getMatrNo(), matrCmpy.getCmpyNo(),"Y");
                    if (ckvo != null) {
                        matrCmpy.setMatrCmpyNo(ckvo.getMatrCmpyNo());
                        matrCmpy.setUsedYn("Y");

                    }
                    else {
                        matrCmpy.setRegId(userId);
                        matrCmpy.setRegIp(ipaddr);
                        matrCmpy.setRegDt(DateUtils.getCurrentDate());
                    }
                }
                matrCmpyRepository.save(matrCmpy);
            }
        }
        */
        if (mapSafeStk != null) {
            mapSafeStk.put("matrNo",matrInfo.getMatrNo());
            mapSafeStk.put("custNo",custNo);
            this.saveSafeStk(mapSafeStk);
        }
    }

    public MatrAttr saveMatrAttr(Map<String, Object> paraMap){
        MatrAttr mavo = new MatrAttr();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        try{
            mavo.setChroma(Float.parseFloat(paraMap.get("chroma").toString()));
        }catch(NullPointerException ne){

        }

        try{
            mavo.setJinseno(Float.parseFloat(paraMap.get("jinseno").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mavo.setMatrType(Long.parseLong(paraMap.get("matrType").toString()));
        }catch(NullPointerException ne){
            mavo.setMatrType(2902L);
        }
        try{
            mavo.setHowOld(Long.parseLong(paraMap.get("howOld").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mavo.setMaxBrix(Float.parseFloat(paraMap.get("maxBrix").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mavo.setMinBrix(Float.parseFloat(paraMap.get("minBrix").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mavo.setMaxVisco(Float.parseFloat(paraMap.get("maxVisco").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mavo.setMinVisco(Float.parseFloat(paraMap.get("minVisco").toString()));
        }catch(NullPointerException ne){

        }

        try{
            mavo.setSolid(Float.parseFloat(paraMap.get("solid").toString()));
        }catch(NullPointerException ne){

        }

        try{
            mavo.setUsedTp(Long.parseLong(paraMap.get("usedTp").toString()));
        }catch(NullPointerException ne){

        }

        Long matrNo = 0L;
        try{
            matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        }catch(NullPointerException ne){
            matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        }

        MatrAttr chkmavo = matrAttrRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
        if(chkmavo != null){
            mavo.setMatrAttrNo(chkmavo.getMatrAttrNo());
            mavo.setMatrNo(chkmavo.getMatrNo());

        }else{
            mavo.setMatrAttrNo(0L);
            mavo.setMatrNo(matrNo);
        }
        mavo.setUsedYn("Y");
        mavo.setCustNo(custNo);
        mavo = matrAttrRepo.save(mavo);

        return mavo;
    }

    /*???????????? ??????*/
    @Override
    public void saveSafeStk(Map<String, Object> mapSafeStk) {
        String tag = "MatrService.saveSafeStk => ";
        Long custNo = Long.parseLong(mapSafeStk.get("custNo").toString());
        //log.info(tag + "mapSafeStk = " + mapSafeStk.toString() );

        try { //1???????????????
            Long.parseLong(mapSafeStk.get("janStkqty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("janStkQty",0L);
        }
        try { //2???????????????
            Long.parseLong(mapSafeStk.get("febStkSty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("febStkQty",0L);
        }
        try {//3???????????????
            Long.parseLong(mapSafeStk.get("marStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("marStkQty",0L);
        }
        try {//4???????????????
            Long.parseLong(mapSafeStk.get("aprStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("aprStkQty",0L);
        }
        try {//5???????????????
            Long.parseLong(mapSafeStk.get("mayStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("mayStkQty",0L);
        }
        try {//6???????????????
            Long.parseLong(mapSafeStk.get("junStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("junStkQty",0L);
        }
        try {//7???????????????
            Long.parseLong(mapSafeStk.get("julStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("julStkQty",0L);
        }
        try {//8???????????????
            Long.parseLong(mapSafeStk.get("augStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("augStkQty",0L);
        }
        try {//9???????????????
            Long.parseLong(mapSafeStk.get("sepStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("sepStkQty",0L);
        }
        try {//10???????????????
            Long.parseLong(mapSafeStk.get("octStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("octStkQty",0L);
        }
        try {//11???????????????
            Long.parseLong(mapSafeStk.get("novStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("novStkQty",0L);
        }
        try {//12???????????????
            Long.parseLong(mapSafeStk.get("decStkQty").toString());
        }
        catch(NullPointerException ne) {
            mapSafeStk.put("decStkQty",0L);
        }

        log.info(tag + "mapSaveStk = " + mapSafeStk.toString());

        MatrSafeStk safeStk = new MatrSafeStk();
        safeStk.setMatrNo(Long.parseLong(mapSafeStk.get("matrNo").toString()));
        safeStk.setFebSafeStk(Long.parseLong(mapSafeStk.get("febStkQty").toString()));
        safeStk.setMarSafeStk(Long.parseLong(mapSafeStk.get("marStkQty").toString()));
        safeStk.setAprSafeStk(Long.parseLong(mapSafeStk.get("aprStkQty").toString()));
        safeStk.setMaySafeStk(Long.parseLong(mapSafeStk.get("mayStkQty").toString()));
        safeStk.setJunSafeStk(Long.parseLong(mapSafeStk.get("junStkQty").toString()));
        safeStk.setJulSafeStk(Long.parseLong(mapSafeStk.get("julStkQty").toString()));
        safeStk.setAugSafeStk(Long.parseLong(mapSafeStk.get("augStkQty").toString()));
        safeStk.setJanSafeStk(Long.parseLong(mapSafeStk.get("janStkQty").toString()));
        safeStk.setSepSafeStk(Long.parseLong(mapSafeStk.get("sepStkQty").toString()));
        safeStk.setOctSafeStk(Long.parseLong(mapSafeStk.get("octStkQty").toString()));
        safeStk.setNovSafeStk(Long.parseLong(mapSafeStk.get("novStkQty").toString()));
        safeStk.setDecSafeStk(Long.parseLong(mapSafeStk.get("decStkQty").toString()));
        safeStk.setUsedYn("Y");
        MatrSafeStk chkvo = matrSafeStkRepository.findByCustNoAndMatrNoAndUsedYn(custNo,safeStk.getMatrNo(),"Y");
        if (chkvo != null) {
            safeStk.setSafeStkNo(chkvo.getSafeStkNo());
        }
        else {
            safeStk.setSafeStkNo(0L);
        }
        safeStk.setCustNo(custNo);
        matrSafeStkRepository.save(safeStk);
    }

    @Override
    public Map<String, Object> getPursUnit(Map<String, Object> paraMap) {
        return matrMapper.getPursUnit(paraMap);
    }


    @Override
    public MatrInfo getMatrInfoByJPA(Long custNo, Long matrNo) {
        return matrRepository.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo,"Y");
    }


    @Override
    @Transactional
    public void saveMatrCmpy(Map<String, Object> paraMap) {
        String tag = "MatrService.saveMatrCmpy => ";
        log.info("paraMap 123: =>" + paraMap.toString());
        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String,Object>> cmpyList  = (List<Map<String,Object>>) paraMap.get("cmpyList");

        for (Map<String, Object> el : cmpyList) {
            MatrCmpy matrCmpy = new MatrCmpy();
            matrCmpy.setMatrNo(matrNo);
            matrCmpy.setUsedYn("Y");
            matrCmpy.setDefaultYn("N");
            matrCmpy.setModDt(DateUtils.getCurrentDate());
            matrCmpy.setModIp(paraMap.get("ipaddr").toString());
            matrCmpy.setModId(Long.parseLong(paraMap.get("userId").toString()));
            matrCmpy.setCmpyNo(Long.parseLong(el.get("cmpyNo").toString()));
            MatrCmpy chkMatrCmpy = matrCmpyRepository.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,matrCmpy.getMatrNo(), matrCmpy.getCmpyNo(),"Y");

            if (chkMatrCmpy != null) {
                matrCmpy.setMatrCmpyNo(chkMatrCmpy.getMatrCmpyNo());
                matrCmpy.setDefaultYn(chkMatrCmpy.getDefaultYn());
                matrCmpy.setRegDt(chkMatrCmpy.getRegDt());
                matrCmpy.setRegId(chkMatrCmpy.getRegId());
                matrCmpy.setRegIp(chkMatrCmpy.getRegIp());
                matrCmpy.setUsedYn("Y");
            }
            else {
                matrCmpy.setMatrCmpyNo(0L);
                matrCmpy.setRegDt(DateUtils.getCurrentDate());
                matrCmpy.setRegIp(paraMap.get("ipaddr").toString());
                matrCmpy.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            matrCmpy.setCustNo(custNo);
            matrCmpyRepository.save(matrCmpy);
        }
    }



    @Override
    public void saveMatrCmpyCart(Map<String, Object> paraMap) {
        String tag = "MatrService.saveMatrCmpyCart => ";

        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        List<Map<String,Object>> cmpyList  = (List<Map<String,Object>>) paraMap.get("cmpyList");
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        for (Map<String, Object> el : cmpyList) {
            MatrCmpyCart matrCmpyCart = new MatrCmpyCart();
            matrCmpyCart.setUserId(userId);
            matrCmpyCart.setMatrNo(matrNo);
            //matrCmpyCart.setMatrInfo(matrRepository.findByMatrNoAndUsedYn(matrNo,"Y"));

            matrCmpyCart.setCmpyNo(Long.parseLong(el.get("cmpyNo").toString()));
            matrCmpyCartRepository.save(matrCmpyCart);
        }
    }

    @Override
    public List<Map<String, Object>> getMatrCmpyList(Map<String, Object> paraMap) {
        return matrMapper.getMatrCmpyList(paraMap);
    }

    @Override
    public int getMatrCmpyListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrCmpyListCount(paraMap);
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void initMatrCmpyCart(Map<String, Object> paraMap) {
        matrCmpyCartRepository.deleteByUserId(Long.parseLong(paraMap.get("userId").toString()));
    }

    @Override
    public List<Map<String, Object>> getMatrCmpyComboList(Map<String, Object> paraMap) {
        return matrMapper.getMatrCmpyComboList(paraMap);
    }


    @Override
    @Transactional
    public void loadRawMatByExcel(Map<String, Object> paraMap)  throws Exception {
        /*
        String tag = "MatrService.loadRawMatByExcel => ";
        StringBuffer buf = new StringBuffer();
        Properties properties = new Properties();
        String resource = "config/mes.properties";  //D:/tmp/upload/right_excel/test.xlsx
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        FileInfo fileEntity = fileRepository.findByFileNoAndUsedYn(fileNo,"Y");

        Reader reader = Resources.getResourceAsReader(resource);
        properties.load(reader);
        String fileRoot = properties.getProperty("file.root.path");

        buf.setLength(0);
        buf.append(fileRoot)
           .append(fileEntity.getRegId()).append(File.separator)
           .append(fileNo).append(File.separator)
           .append(fileEntity.getSaveFileNm());
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex=0;
        int basePubsUnitCd = 0;
        XSSFSheet sheet=workbook.getSheetAt(0);
        int rows=sheet.getPhysicalNumberOfRows();
        log.info(tag  + "excel???????????? = " + rows);
        String cmpyNm = "";
        CmpyInfo cmpyInfo = new CmpyInfo();

        for(rowindex=0;rowindex<rows;rowindex++){
            if ( rowindex <= 2) continue; //???????????? skip
            MatrInfo matrInfo = new MatrInfo();

            XSSFRow row=sheet.getRow(rowindex);
            if(row ==null) continue;

            int cells=row.getPhysicalNumberOfCells();
            //for(columnindex=0; columnindex<=cells; columnindex++) {
            matrInfo.setMatrNo(0L);
            log.info(tag + " rowindex = " + rowindex);//kill
            //XSSFCell cell=row.getCell(0);

            cmpyNm = row.getCell(3).getStringCellValue(); //???????????????-1
            if (cmpyNm != null) {
                cmpyInfo = cmpyRepository.findByCmpyNmAndUsedYn(cmpyNm,"Y");
                if (cmpyInfo == null) {
                    cmpyInfo = new CmpyInfo();
                    cmpyInfo.setCmpyNo(0L);
                    cmpyInfo.setCmpyNm(cmpyNm);
                    cmpyInfo.setMngrGbnCd(Long.parseLong(properties.getProperty("cmpy_purs_cd"))); //???????????????
                    cmpyInfo.setRegIp((String) paraMap.get("ipaddr"));
                    cmpyInfo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    cmpyInfo.setRegDt(matrInfo.getModDt());
                    cmpyInfo.setModIp((String) paraMap.get("ipaddr"));
                    cmpyInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    cmpyInfo.setModDt(matrInfo.getModDt());
                    cmpyInfo.setUsedYn("Y");
                    cmpyRepository.save(cmpyInfo);
                    cmpyInfo = cmpyRepository.findByCmpyNm(cmpyNm);
                }
            }

            //??????????????? ?????????????????? ??????
            String matrNm = row.getCell(1).getStringCellValue();
            matrInfo = matrRepository.findByMatrNm(matrNm);
            if (matrInfo == null) {
                matrInfo.setMatrNo(0L);
            }
            matrInfo.setItemCd(row.getCell(0).getStringCellValue()); //????????????(?????????)
            matrInfo.setMatrNm(matrNm); //?????????

            //???????????? ?????? ??????(??????????????? ?????????)
            matrInfo.setMatrTp(Long.parseLong(properties.getProperty("rawmatr_cd")));
            basePubsUnitCd = Integer.parseInt(properties.getProperty("base_purs_unit_cd"));
            String unitNm = row.getCell(2).getStringCellValue();
            CodeInfo codeInfo = codeRepository.findByParCodeNoAndCodeNm(basePubsUnitCd, unitNm);
            matrInfo.setPursUnit(codeInfo.getCodeNo());
            //???????????? ?????? ???.(??????????????? ?????????)

            matrInfo.setSz(unitNm); //?????????
            matrInfo.setUsedYn("Y");
            matrInfo.setRegIp((String) paraMap.get("ipaddr"));
            matrInfo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            matrInfo.setRegDt(matrInfo.getModDt());
            matrInfo.setModIp((String) paraMap.get("ipaddr"));
            matrInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            matrInfo.setModDt(matrInfo.getModDt());
            matrInfo.setMadein(Long.parseLong(properties.getProperty("madein_default_cd"))); //?????????
            matrInfo.setPursUnitPrc(0L); //????????????
            matrInfo.setFileNo(0L); //?????????????????????
            log.info(tag + "matrInfo = " + matrInfo.toString());
            matrRepository.save(matrInfo);

            if (cmpyNm != null) {
                //??????????????? ?????? ??????
                MatrCmpy matrCmpy = matrCmpyRepository.findByMatrNoAndCmpyNo(matrInfo.getMatrNo(), cmpyInfo.getCmpyNo());
                if (matrCmpy == null) {
                    matrCmpy = new MatrCmpy();
                    matrCmpy.setMatrCmpyNo(0L);
                }
                matrCmpy.setMatrInfo(matrInfo);
                ;
                matrCmpy.setCmpyNo(cmpyInfo.getCmpyNo());
                matrCmpy.setUsedYn("Y");
                log.info(tag + "matrCmpyInfo = " + matrCmpy.toString());
                matrCmpyRepository.save(matrCmpy);
                //??????????????? ?????? ???.
            }

            //?????????????????? ?????? ??????
            MatrSafeStk matrSafeStk = new MatrSafeStk();
            matrSafeStk.setMatrInfo(matrInfo);
            matrSafeStk.setJanSafeStk( (long) row.getCell(18).getNumericCellValue()); //01?????????-??????
            matrSafeStk.setFebSafeStk( (long) row.getCell(18).getNumericCellValue()); //02?????????-??????
            matrSafeStk.setMarSafeStk( (long) row.getCell(19).getNumericCellValue()); //03?????????-????????????
            matrSafeStk.setAprSafeStk( (long) row.getCell(20).getNumericCellValue()); //04?????????-?????????
            matrSafeStk.setMaySafeStk( (long) row.getCell(20).getNumericCellValue()); //05?????????-?????????
            matrSafeStk.setJunSafeStk( (long) row.getCell(18).getNumericCellValue()); //06?????????-??????
            matrSafeStk.setJulSafeStk( (long) row.getCell(18).getNumericCellValue()); //07?????????-??????
            matrSafeStk.setAugSafeStk( (long) row.getCell(19).getNumericCellValue()); //08?????????-????????????
            matrSafeStk.setSepSafeStk( (long) row.getCell(20).getNumericCellValue()); //9?????????-?????????
            matrSafeStk.setOctSafeStk( (long) row.getCell(20).getNumericCellValue()); //10?????????-?????????
            matrSafeStk.setNovSafeStk( (long) row.getCell(18).getNumericCellValue()); //11?????????-??????
            matrSafeStk.setDecSafeStk( (long) row.getCell(18).getNumericCellValue()); //12?????????-??????
            matrSafeStkRepository.save(matrSafeStk);
            //?????????????????? ?????? ???.

        }
        */
    }

    @Override
    public Map<String, Object> getSafeStk(Map<String, Object> paraMap) {
        return matrMapper.getSafeStk(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCmpyMatrCombo(Map<String, Object> paraMap) {
        return matrMapper.getCmpyMatrCombo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrProdList(Map<String, Object> paraMap) {
        String tag = "MatrService.getMatrProdList => ";
        log.info(tag + "param = " + paraMap.toString());
        return matrMapper.getMatrProdList(paraMap);
    }

    @Override
    public int getMatrProdListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCmpyMatrList(Map<String, Object> paraMap) {
        return matrMapper.getCmpyMatrList(paraMap);
    }

    @Override
    public int getCmpyMatrListCount(Map<String, Object> paraMap) {
        return matrMapper.getCmpyMatrListCount(paraMap);
    }

    @Override
    public void dropMatrCmpy(Map<String, Object> paraMap) {
        List<Map<String,Object>> lstCmpy =  (List<Map<String,Object>>) paraMap.get("cmpyList");
        for (Map<String,Object> el : lstCmpy) {
            paraMap.put("cmpyNo",Long.parseLong(el.get("cmpy_no").toString()));
            matrMapper.deleteMatrCmpy(paraMap);
        }
    }

    @Override
    public void dropMatrProd(Map<String, Object> paraMap) {
        List<Map<String,Object>> lstProd =  (List<Map<String,Object>>) paraMap.get("prodList");
        for (Map<String,Object> el : lstProd) {
            paraMap.put("prodNo",Long.parseLong(el.get("prod_no").toString()));
            matrMapper.deleteMatrProd(paraMap);
        }
    }

    @Override
    public List<Map<String, Object>> getProdMatrList(Map<String, Object> paraMap) {
        return matrMapper.getProdMatrList(paraMap);
    }


    @Override
    public List<Map<String, Object>> getStockList(Map<String, Object> paraMap) {
        return matrMapper.getStockList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap) {
        return matrMapper.getMatrIwhList(paraMap);
    }

    @Override
    public int getMatrIwhListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrIwhListCount(paraMap);
    }

    @Override
    public Object saveMatrStkInfo(Map<String, Object> paraMap) {
        return matrMapper.saveMatrStkInfo(paraMap);
    }

    @Override
    public int getProdMatrListCount(Map<String, Object> paraMap) {
        return matrMapper.getProdMatrListCount(paraMap);
    }



    @Override
    public void deleteMatr(Map<String, Object> paraMap) {
        matrMapper.deleteMatr(paraMap);
    }


    @Override
    public void embCalendarSave(Map<String, Object> paraMap) {
        matrMapper.embCalendarSave(paraMap);
    }

//        String dateFr = (String)paraMap.get("dateFr");
//        log.info("summaryList.size() = "+totalStatList.size());

    @Override
    public List<Map<String, Object>> getProcList(Map<String, Object> paraMap) {
        return matrMapper.getProcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrCurrStkList(Map<String, Object> paraMap) {
        return matrMapper.getMatrCurrStkList(paraMap);
    }

    @Override
    public int getMatrCurrStkListCount(Map<String, Object> paraMap) {
        return matrMapper.getMatrCurrStkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getAnaMatrList(Map<String, Object> paraMap) {
        return matrMapper.getAnaMatrList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getCheckRetnList(Map<String, Object> paraMap){
        List<Map<String,Object>> list = new ArrayList<>();
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("matrList");

        for (Map<String,Object> el : ds) {
            Map<String, Object> passMap = new HashMap<String,Object>();
            passMap.put("matrNo", el.get("matr_no"));

            Map<String, Object> yn = matrMapper.getCheckRetnList(passMap);
            if(yn == null){
                passMap.put("retn_yn", "N");
            }
            else{
                passMap.put("retn_yn", yn.get("retn_yn"));
            }

            list.add(passMap);
        }
        log.info("list??? " + list);
        return list;
    }

    //???????????? ?????? ????????????
    @Override
    public void matrStatExcel(Map<String, Object> paraMap, HttpServletResponse response) {
        List<Map<String,Object>> stkList = (List<Map<String,Object>>) paraMap.get("stkList"); // ?????????????????? - ??????

//        String dateFr = (String)paraMap.get("dateFr");
//        log.info("summaryList.size() = "+totalStatList.size());

        XSSFWorkbook wb = new XSSFWorkbook();
        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("????????????");

        //??? ????????? ?????? - ?????????
        CellStyle Style = wb.createCellStyle();
        Style.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
        Style.setVerticalAlignment(VerticalAlignment.CENTER); //?????? ????????? ??????
        Style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex()); //????????? ?????? - 1
        Style.setFillPattern(FillPatternType.SOLID_FOREGROUND); //????????? ?????? - 2

        //?????? ????????? ?????? - ?????????
        XSSFFont Font = wb.createFont();
        Font.setFontHeightInPoints((short)16);
        Font.setBold(true);

        //??? ????????? ?????? - ??????
        CellStyle StyleNum = wb.createCellStyle();
        Style.setAlignment(HorizontalAlignment.CENTER);


        // Row(???) Cell(???) ??????
        Row row = null;
        Cell cell = null;
        int rowCount = 0;
        int cellCount = 0;
        int lstNo = 0;

        rowCount++; //????????? ??????

        // ????????? ??????
        row = sheet.createRow(rowCount++);
        cellCount = 0;
        cell = row.createCell(cellCount++);
        cell.setCellValue("");
        cell = row.createCell(cellCount++);
        cell.setCellValue("1. ????????????");
        cell.setCellStyle(Style);
        Style.setFont(Font);
        sheet.addMergedRegion(new CellRangeAddress(1,1,1,5));

        rowCount++; //????????? ???????????? ??????

        // ????????? Row - ?????????
        row = sheet.createRow(rowCount++);
        cellCount = 0;

        cell = row.createCell(cellCount++);
        cell.setCellValue("");
        cell = row.createCell(cellCount++);
        cell.setCellValue("?????????");
        cell = row.createCell(cellCount++);
        cell.setCellValue("????????????");
        cell = row.createCell(cellCount++);
        cell.setCellValue("????????????");
        cell = row.createCell(cellCount++);
        cell.setCellValue("????????????");
        cell = row.createCell(cellCount++);
        cell.setCellValue("????????????");

        //????????? ROW - ??????, ?????????, ????????????, ????????????, ????????????, ????????????
        cellCount = 0;

        String fieldName1[] = {"","matr_nm", "pqty", "iqty", "oqty", "cqty"};
//        sheet.addMergedRegion(new CellRangeAddress(FiveToSixRow, FiveToSixRow, 0, fieldName1.length-1)); // 1??? ????????? ?????? ?????????
        lstNo = 0;
        for (int i=0; i < stkList.size(); i++) {
            row = sheet.createRow(rowCount++);
//            row = sheet.createRow(FiveToSixRow+i+2);
            cell = row.createCell(0);
            cell.setCellValue(i+1);
            for(int j=1; j<fieldName1.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(String.valueOf(stkList.get(lstNo).get(fieldName1[j])));
            }
            lstNo++;
        }

        String fileName = "????????????";
//        fileName = new String(fileName.getBytes("euc-kr"),"ISO-8859-1");

        FileOutputStream fileOut = null;
        try{
//            fileOut = new FileOutputStream(env.getProperty("img.root.path")+fileName+".xlsx");
            fileOut = new FileOutputStream(env.getProperty("test.root.path")+fileName+".xlsx");
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
        try{
            wb.write(fileOut);
            fileOut.close();
        }catch(IOException e){
            System.out.println(e);
        }
        makeFile(fileName, wb, response);
    }

    @Override
    @Transactional
    public void saveProdBom(Map<String, Object> paraMap) {
        String tag = "MatrService.saveMatrProd => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        List<Map<String,Object>> prodList  = (List<Map<String,Object>>) paraMap.get("prodList");

        for (Map<String, Object> el : prodList) {
            ProdBom bomvo = new ProdBom();
            bomvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            bomvo.setMatrNo(matrNo);
            bomvo.setUsedYn("Y");
            bomvo.setConsistRt(0f);
            bomvo.setModDt(DateUtils.getCurrentDate());
            bomvo.setModIp(paraMap.get("ipaddr").toString());
            bomvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            ProdBom chkvo =  prodBomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo,bomvo.getProdNo(), bomvo.getMatrNo(),"Y");

            if (chkvo != null) {
                bomvo.setBomLvl(chkvo.getBomLvl());
                bomvo.setBomNo(chkvo.getBomNo());
                bomvo.setRegDt(chkvo.getRegDt());
                bomvo.setRegId(chkvo.getRegId());
                bomvo.setRegIp(chkvo.getRegIp());
            }
            else {
                bomvo.setBomNo(0L);
                bomvo.setBomLvl(0L);
                bomvo.setRegDt(DateUtils.getCurrentDate());
                bomvo.setRegIp(paraMap.get("ipaddr").toString());
                bomvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            bomvo.setCustNo(custNo);
            prodBomRepo.save(bomvo);
        }

    }

    //?????? ??????
    public String makeFile(String fileName, XSSFWorkbook xlsxWb, HttpServletResponse res) {
        log.info("fileName = "+fileName);
        log.info("xlsxWb = "+xlsxWb);
        log.info("res = "+res);

        try {
            fileName = new String(fileName.getBytes("euc-kr"),"ISO-8859-1");

            File xlsxFile = File.createTempFile(fileName,".xlsx");
            FileOutputStream fileOut = new FileOutputStream(xlsxFile);
            xlsxWb.write(fileOut);

            res.setHeader("Content-disposition","attachment; filename="+fileName+".xlsx");
            res.setCharacterEncoding("utf-8");
            res.setContentType("text/html; charset=");

//            res.setHeader("Content-Disposition", "attachment; filename=testExcel.xlsx");
//            res.setHeader("Content-Description", "JSP Generated Data");
//            res.setContentType("application/vnd.ms-excel");

            FileInputStream fileIn = new FileInputStream(xlsxFile);
            ServletOutputStream out = res.getOutputStream();

            byte[] outputByte = new byte[4096];
            int readSize = 0;

            while((readSize = fileIn.read(outputByte, 0, 4096)) != -1)
            {
                out.write(outputByte, 0, readSize);
            }
            fileIn.close();
            out.flush();
            out.close();
//            xlsxFile.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(fileName);
        return fileName;
    }

    //???????????? ???????????? ?????? ?????????

    @Override
    @Transactional
    public List<Map<String, Object>> matrExcelIwh(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.DaedongIndcByExcel => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        log.info("FilePath" + fileRoot);
        String filePath = fileService.getFileInfo(fileRoot,fileNo);

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_fr"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_owh_to"));


        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        //???????????? ??????
        String erProdNm = "";
        Float owhQty = 0F;
        Long prodNo = 0L;
        String telNo = "";
        String cmpy = "";
        String owhWhNm = "";
        Long cmpyNo = 0L;
        Long whNo = 0L;
        Date owdate = new Date();
        Float stkQty = 0F;
        Float mistkQty = 0F;
        Long mngrgbnSale = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> Map = new HashMap<String, Object>();
        XSSFRow row = sheet.getRow(rowindex);
        for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {

            row = sheet.getRow(rowindex);
            if (row == null) continue;
            ProdOwh owh = new ProdOwh();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = row.getCell(0).getStringCellValue(); // ????????????
            date = date.replaceAll("\\p{Z}[-][0-9]", "");
            date = date.replaceAll("[/]", "-");
            if (date.contains("???") == true) {
                break;
            }

            erProdNm = row.getCell(1).getStringCellValue();  // ?????? ?????????
            erProdNm = erProdNm.replaceAll("\\p{Z}", ""); // ???????????? ??????
            ProdInfo pochk = prodRep.findByCustNoAndErpProdNmAndUsedYn(custNo,erProdNm, "Y");
            if (pochk != null) {
                prodNo = pochk.getProdNo();
                owh.setProdNo(prodNo);
            } else {
                Map.put("row", row); // ???
                Map.put("idx", rowindex);
            }

            owdate = sdf.parse(date);
            owh.setOwhDt(owdate);
            owh.setOwhReqDt(owdate);

            owhQty = (float) row.getCell(2).getNumericCellValue(); // ?????? ??????
            owh.setOwhQty(owhQty);
            owh.setOwhReqQty(owhQty);
            owh.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));

            owh.setRegDt(DateUtils.getCurrentDateTime());
            owh.setRegId(0L);
            owh.setRegIp("127.0.0.1");
            owh.setModDt(DateUtils.getCurrentDateTime());
            owh.setModId(0L);
            owh.setModIp("127.0.0.1");
            owh.setUsedYn("Y");

            cmpy = row.getCell(4).getStringCellValue(); // ?????? ??????
            cmpy = cmpy.replaceAll("\\p{Z}", "");

            owhWhNm = row.getCell(3).getStringCellValue(); // ?????? ??????
            owhWhNm = owhWhNm.replaceAll("\\p{Z}", "");
            List<WhInfo> whchks = whinfoRepo.findAllByCustNoAndWhNmAndUsedYn(custNo,owhWhNm, "Y");
            if (whchks != null) {
                whNo = whchks.get(0).getWhNo();
                owh.setWhNo(whNo);
            } else {
                Map.put("row", row); // ???
                Map.put("idx", rowindex);
            }

            CmpyInfo cmchk = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnSale,cmpy, "Y");
            if (cmchk != null) {
                if (cmchk.getMngrGbnCd() == 21) {
                    cmpyNo = cmchk.getCmpyNo();
                    owh.setCmpyNo(cmpyNo);
                    telNo = row.getCell(5).getStringCellValue(); // ????????????
                    cmchk.setTelNo(telNo);
                    cmpyRepo.save(cmchk);
                } else {
                    Map.put("row", row); // ???
                    Map.put("idx", rowindex);
                }
            }
            ProdStk stkvo = new ProdStk();
            ProdStk stkchk = stkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo,prodNo, "Y");
            if (stkchk != null) {
                stkQty = stkchk.getStkQty();
                mistkQty = stkQty - owhQty;
                stkchk.setStkQty(mistkQty);
                stkchk.setRegDt(DateUtils.getCurrentDateTime());
                stkchk.setRegId(0L);
                stkchk.setRegIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            } else {
                stkvo.setProdNo(prodNo);
                stkchk.setModDt(DateUtils.getCurrentDateTime());
                stkchk.setModId(0L);
                stkchk.setModIp("127.0.0.1");
                stkvo.setWhNo(whNo);
                stkvo.setStkQty(0F);
                stkvo.setStkDt(owdate);
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);
            stkRepo.save(stkchk);


            OrdInfo ordvo = new OrdInfo();
            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.complete"))); //??????
            ordvo.setOrdGbn(Long.parseLong(env.getProperty("code_byExcel"))); //????????????
            ordvo.setUsedYn("Y");
            ordvo.setOrdDt(owdate);
            ordvo.setDlvDt(owdate);
            ordvo.setDlvReqDt(owdate);
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord.oem")));
            ordvo.setPlcNo(0L);
            ordvo.setModDt(DateUtils.getCurrentDateTime());
            ordvo.setModId(0L);
            ordvo.setModIp("127.0.0.1");
            ordvo.setCmpyNo(cmpyNo);
            ordvo.setOrdNm(erProdNm);
            ordvo.setRegDt(DateUtils.getCurrentDateTime());
            ordvo.setRegId(0L);
            ordvo.setRegIp("127.0.0.1");
            ordvo.setCustNo(custNo);
            ordRepo.save(ordvo);

            OrdProd opvo = new OrdProd();
            opvo.setQtyPerPkg(1);
            opvo.setUsedYn("Y");
            opvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));


            OrdProd orchk = ordprRepo.findByCustNoAndOrdNoAndProdNoAndDlvDtAndUsedYn(custNo,ordvo.getOrdNo(), prodNo, owdate, "Y");
            if (orchk != null) {
                opvo.setOrdNo(orchk.getOrdNo());
                opvo.setProdNo(orchk.getProdNo());
                Float ordQty = orchk.getOrdQty();
                Float sumQty = ordQty + owhQty;
                opvo.setOrdQty(sumQty);
                opvo.setModDt(DateUtils.getCurrentDateTime());
                opvo.setModId(0L);
                opvo.setModIp("127.0.0.1");
            } else {
                opvo.setOrdNo(orchk.getOrdNo());
                opvo.setProdNo(prodNo);
                opvo.setOrdQty(owhQty);
                opvo.setRegDt(DateUtils.getCurrentDateTime());
                opvo.setRegId(0L);
                opvo.setRegIp("127.0.0.1");
                opvo.setDlvDt(owdate);
            }
            opvo.setCustNo(custNo);
            ordprRepo.save(opvo);
            owh.setOrdNo(ordvo.getOrdNo());
            owh.setCustNo(custNo);
            owhRepo.save(owh);
            Map.put("custNo",custNo);
            list.add(Map);


        }
        return list;
    }

    @Override
    public List<Map<String,Object>> getComeDateMatrList(Map<String,Object> paraMap){
        return matrMapper.getComeDateMatrList(paraMap);
    }

    @Override
    public int getComeDateMatrListCount(Map<String, Object> paraMap){
        return matrMapper.getComeDateMatrListCount(paraMap);
    }

    @Override
    @Transactional
    public void matrSaveExcelUpLoad(Map<String, Object> paraMap)throws Exception {
        String tag = "ProdService.matrSaveExcelUpLoad => ";
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        Long matrNo = 0L;
        String matrNm = "";
        String cmpyNm ="";
        String exData = "";
        String tmpr = "";
        Float spga = 1F;
        Float mess = 0F;
        String madein = "";
        Long cmpyNo= 0L;
        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        ????????? ??????
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            if (rowindex < 1) continue; //???????????? skip
            row = sheet.getRow(rowindex);
            if (row == null) break;
            try {
                matrNm = row.getCell(0).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");

            } catch (NullPointerException ne) {
                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
                continue;
            }
            try {
                cmpyNm = row.getCell(8).getStringCellValue(); // ????????? ???
                cmpyNm = cmpyNm.replaceAll("\\p{Z}", "");

            } catch (NullPointerException ne) {
                cmpyNm = "";
            }

            MatrInfo matrInfo = new MatrInfo();
            matrInfo.setMatrNm(matrNm); //?????????
            try {
                matrInfo.setValidTerm((long)row.getCell(2).getNumericCellValue()); //????????????-12??????
            }catch (NullPointerException en){
            matrInfo.setValidTerm(12L);
            }
            try {
                matrInfo.setItemCd(row.getCell(5).getStringCellValue());//????????????
            }catch (NullPointerException en){
                matrInfo.setItemCd("");
            }
            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); // ?????? / ?????????,?????????
            matrInfo.setBrnchNo(1L);
            matrInfo.setUsedYn("Y");
            matrInfo.setSpga(spga);
            try {
                mess = Float.parseFloat(String.valueOf(row.getCell(6).getNumericCellValue())); //??????
            }catch (NullPointerException en){
                matrInfo.setMess(0F);
                matrInfo.setVol(0F);
            }

            try{
                exData = row.getCell(1).getStringCellValue();
            }catch (NullPointerException en){
                exData = "";
            }
            Long matrUnit = Long.parseLong(env.getProperty("code.base.purs_unit"));
            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrUnit, exData.replace(" ", ""), "Y");
            if (mgvo != null) {
                Long Unit = mgvo.getCodeNo();
                matrInfo.setPursUnit(Unit); // ????????????
                if(Unit == Long.parseLong(env.getProperty("code.purs_unit.ml"))){
                    matrInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_imp")));//??????
                }else {
                    matrInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_vol")));//??????
                }

                if(Unit ==Long.parseLong(env.getProperty("code.purs_unit.kg"))){
                    matrInfo.setMess(mess);
                    matrInfo.setVol(mess);
                }else if(Unit ==Long.parseLong(env.getProperty("code.purs_unit.ml"))){
                    matrInfo.setMess(mess);
                    matrInfo.setVol((mess*spga)/1000);
                }else {
                    matrInfo.setMess(mess);
                    matrInfo.setVol(mess);
                }
            }
            try{
                tmpr = row.getCell(3).getStringCellValue();
            }catch (NullPointerException en){
                tmpr = "";
            }
            Long matrtmpr = Long.parseLong(env.getProperty("code.base.save_tmpr_cd"));
            CodeInfo tmvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrtmpr, tmpr.replace(" ", ""), "Y");
            if (tmvo != null) {
                Long savetmpr = tmvo.getCodeNo();
                matrInfo.setSaveTmpr(savetmpr); // ????????????
            }
            try{
                madein = row.getCell(7).getStringCellValue();
            }catch (NullPointerException en){
                madein = "";
            }
            Long matrmade = Long.parseLong(env.getProperty("code.base.madein"));
            CodeInfo mavo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrmade, madein.replace(" ", ""), "Y");
            if (mavo != null) {
                Long matrMadeIn = tmvo.getCodeNo();
                matrInfo.setMadein(matrMadeIn); // ?????????
                matrInfo.setMade(madein);
            }else {
                matrInfo.setMadein(0L);
                matrInfo.setMade(madein);
            }

            try{
                matrNo = (long)row.getCell(9).getNumericCellValue();
            }catch (NullPointerException en){
                matrNo = 0L;
            }

            MatrInfo chkMatr = matrRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (chkMatr != null) {
                matrInfo.setMatrNo(chkMatr.getMatrNo());
                matrInfo.setRegIp(chkMatr.getRegIp());
                matrInfo.setRegId(chkMatr.getRegId());
                matrInfo.setRegDt(chkMatr.getRegDt());
            } else {
                matrInfo.setMatrNo(0L);
                matrInfo.setModIp(paraMap.get("ipaddr").toString());
                matrInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                matrInfo.setModDt(DateUtils.getCurrentDate());
            }
            matrInfo.setCustNo(custNo);
            matrRepo.save(matrInfo);

            /*cmpy_matr ??????????????? ?????? */
            MatrCmpy cmvo = new MatrCmpy();
            CmpyInfo cmpyInfo = new CmpyInfo();
            Long mngrGbnPurs = Long.parseLong(env.getProperty("code.mngrgbn.purs"));
            CmpyInfo cmchk = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrGbnPurs,cmpyNm,"Y");
            if(cmchk != null){
                cmpyNo = cmchk.getCmpyNo();
            }else{
                cmpyInfo.setCmpyNm(cmpyNm);
                cmpyInfo.setUsedYn("Y");
                cmpyInfo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgbn.purs"))); // ????????? ??????????????? ??????
                cmpyInfo.setCustNo(custNo);
                cmpyRepo.save(cmpyInfo);
            }

            cmvo.setUsedYn("Y");
            cmvo.setDefaultYn("N");

            MatrCmpy cmpychk = matrCmpyRepository.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,matrInfo.getMatrNo(),cmpyNo,"Y");
            if(cmpychk != null){
                cmvo.setMatrCmpyNo(cmpychk.getMatrCmpyNo());
                cmvo.setCmpyNo(cmpychk.getCmpyNo());
                cmvo.setMatrNo(cmpychk.getMatrNo());
                cmvo.setRegIp(cmpychk.getRegIp());
                cmvo.setRegId(cmpychk.getRegId());
                cmvo.setRegDt(cmpychk.getRegDt());
            }else{
                cmvo.setMatrCmpyNo(0L);
                cmvo.setMatrNo(matrInfo.getMatrNo());
                cmvo.setCmpyNo(cmpyNo);
                cmvo.setModIp(paraMap.get("ipaddr").toString());
                cmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                cmvo.setModDt(DateUtils.getCurrentDate());
            }
            cmvo.setCustNo(custNo);
            matrCmpyRepository.save(cmvo);

        }

    }

    @Override
    public  List<Map<String, Object>> getmatrPursUnit(Map<String, Object> paraMap) {
        return matrMapper.getmatrPursUnit(paraMap);
    }

    @Override
    public int getmatrPursUnitCount(Map<String, Object> paraMap) {
        return matrMapper.getmatrPursUnitCount(paraMap);
    }

    @Override
    public void saveMatrPursUnit(HashMap<String, Object> paraMap) {
        String tag = "MatrServiceImpl.saveMatrPursUnit => ";
        String codeNm = paraMap.get("codeNm").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        CodeInfo covo = new CodeInfo();
        Long codeNo = 0L;
        try {
            codeNo = Long.parseLong(paraMap.get("codeNo").toString());
        }catch(NullPointerException en){

        }

        CodeInfo codchk = codeRepo.findByCodeNoAndUsedYn(codeNo,"Y");
        if(codchk!=null){
            covo.setParCodeNo(codchk.getParCodeNo());
            covo.setCcpTp(codchk.getCcpTp());
            covo.setCodeNo(codchk.getCodeNo());
            covo.setCodeAlais(codeNm);
            covo.setCodeBrief(codeNm);
            covo.setCodeNm(codeNm);
            covo.setCodeSeq(codchk.getCodeSeq());
            covo.setModableYn(codchk.getModableYn());
            covo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            covo.setRegDt(DateUtils.getCurrentDate());
            covo.setRegIp(paraMap.get("ipaddr").toString());
            covo.setUsedYn("Y");
            covo.setSysCodeYn("Y");
        }else{
            covo.setParCodeNo(Long.parseLong(env.getProperty("code.base.unit")));
            covo.setCodeAlais(codeNm);
            covo.setCodeBrief(codeNm);
            covo.setCodeNm(codeNm);
            covo.setCcpTp(CcpType.valueOf("NONE"));
            covo.setCodeNo(0L);
            covo.setCodeSeq(0);
            covo.setModableYn("N");
            covo.setUsedYn("Y");
            covo.setModIp("127.0.0.1");
            covo.setModId(2L);
            covo.setModDt(DateUtils.getCurrentDate());
        }
        covo.setCustNo(custNo);
        covo = codeRepo.save(covo);
        covo.setCodeSeq(covo.getCodeNo().intValue());
        codeRepo.save(covo);

    }

    @Override
    public void dropPursUnit(Map<String, Object> paraMap) {
        String tag = "MatrServiceImpl.dropPursUnit => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        CodeInfo covo = new CodeInfo();
        Long codeNo = 0L;
        try {
            codeNo = Long.parseLong(paraMap.get("codeNo").toString());
        }catch(NullPointerException en){
        }

        CodeInfo codchk = codeRepo.findByCodeNoAndUsedYn(codeNo,"Y");
        if(codchk!=null) {
            codchk.setUsedYn("N");
            codchk.setModDt(DateUtils.getCurrentBaseDateTime());
            codchk.setModId(Long.parseLong(paraMap.get("userId").toString()));
            codchk.setModIp(paraMap.get("ipaddr").toString());
            codeRepo.save(codchk);
        }

    }

    @Override
    public List<Map<String ,Object>> getSafeStkMatrList(Map<String, Object> paraMap){
        return matrMapper.getSafeStkMatrList(paraMap);
    }

    @Override
    public int getSafeStkMatrListCount(Map<String, Object> paraMap){
        return matrMapper.getSafeStkMatrListCount(paraMap);
    }

    @Override
    @Transactional
    public void svMatrSaveExcelUpLoad(Map<String, Object> paraMap)throws Exception {
        String tag = "ProdService.prodIndcExcel => ";
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        Long matrNo = 0L;
        String matrNm = "";
        String cmpyNm ="";
        String exData = "";
        String tmpr = "";
        Float spga = 1F;
        Float vol = 0F;
        String madein = "";
        Long cmpyNo= 0L;
        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        ????????? ??????
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            if (rowindex < 1) continue; //???????????? skip
            row = sheet.getRow(rowindex);
            if (row == null) break;

            try {
                matrNm = row.getCell(1).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");

            } catch (NullPointerException ne) {
                continue;
            }
            try {
                cmpyNm = row.getCell(9).getStringCellValue(); // ????????? ???
                cmpyNm = cmpyNm.replaceAll("\\p{Z}", "");
            } catch (NullPointerException ne) {
                cmpyNm ="";
            }

            MatrInfo matrInfo = new MatrInfo();
            matrInfo.setMatrNm(matrNm); //?????????
            try{
                matrInfo.setValidTerm((long)row.getCell(3).getNumericCellValue()); //????????????-12??????
            }catch (NullPointerException en){
                matrInfo.setValidTerm(12L);
            }
            try{
                matrInfo.setItemCd(row.getCell(5).getStringCellValue());//????????????
            }catch (NullPointerException en){
                matrInfo.setItemCd("");
            }
            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.submatr"))); // ?????? / ?????????,?????????
            matrInfo.setBrnchNo(1L);
            matrInfo.setUsedYn("Y");
            matrInfo.setSpga(spga);
            try{
                vol = Float.parseFloat(String.valueOf(row.getCell(6).getNumericCellValue())); //??????
            }catch (NullPointerException en){
                matrInfo.setMess(1F);
                matrInfo.setVol(1F);
            }

            try{
                exData = row.getCell(2).getStringCellValue(); //????????????
            }catch(NullPointerException en){
                exData ="";
            }
            Long matrUnit = Long.parseLong(env.getProperty("code.base.purs_unit"));
            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrUnit, exData.replace(" ", ""), "Y");
            if (mgvo != null) {
                Long Unit = mgvo.getCodeNo();
                matrInfo.setPursUnit(Unit); // ????????????
                matrInfo.setMess(vol);
                matrInfo.setVol(vol);
            }
            else{
                matrInfo.setPursUnit(59L); // ????????????
                matrInfo.setMess(1F);
                matrInfo.setVol(1F);
            }

            try{
                tmpr = row.getCell(4).getStringCellValue();
            }catch(NullPointerException en){
                tmpr = "";
            }
            Long matrtmpr = Long.parseLong(env.getProperty("code.base.save_tmpr_cd"));
            CodeInfo tmvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrtmpr, tmpr.replace(" ", ""), "Y");
            if (tmvo != null) {
                Long savetmpr = tmvo.getCodeNo();
                matrInfo.setSaveTmpr(savetmpr); // ????????????
            }

            try{
                madein = row.getCell(7).getStringCellValue();
            }catch(NullPointerException en){
                madein = "";
            }
            Long matrmade = Long.parseLong(env.getProperty("code.base.madein"));
            CodeInfo mavo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(matrmade, madein.replace(" ", ""), "Y");
            if (mavo != null) {
                Long matrMadeIn = tmvo.getCodeNo();
                matrInfo.setMadein(matrMadeIn); // ?????????
                matrInfo.setMade(madein);
            }else {
                matrInfo.setMadein(0L);
                matrInfo.setMade(madein);
            }

            try{
                matrNo = (long)row.getCell(0).getNumericCellValue();
            }catch (NullPointerException en){
                matrNo = 0L;
            }
            MatrInfo chkMatr = matrRepo.findByCustNoAndMatrNoAndUsedYn(custNo,matrNo, "Y");
            if (chkMatr != null) {
                matrInfo.setMatrNo(chkMatr.getMatrNo());
                matrInfo.setRegIp(chkMatr.getRegIp());
                matrInfo.setRegId(chkMatr.getRegId());
                matrInfo.setRegDt(chkMatr.getRegDt());
            } else {
                matrInfo.setMatrNo(0L);
                matrInfo.setModIp(paraMap.get("ipaddr").toString());
                matrInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                matrInfo.setModDt(DateUtils.getCurrentDate());
            }

            matrInfo.setCustNo(custNo);
            matrRepo.save(matrInfo);

            try{
                cmpyNo = (long)row.getCell(8).getNumericCellValue();
            }catch(NullPointerException ne){
                cmpyNo = 0L;
            }


            if(cmpyNo != 0L){
                /*cmpy_matr ??????????????? ??????*/
                MatrCmpy cmvo = new MatrCmpy();
                CmpyInfo cmpyInfo = new CmpyInfo();

                CmpyInfo cmchk = cmpyRepo.findByCustNoAndCmpyNoAndUsedYn(custNo,cmpyNo, "Y");
                if(cmchk != null){
                    cmpyNo = cmchk.getCmpyNo();
                }else{
                    cmpyInfo.setCmpyNm(cmpyNm);
                    cmpyInfo.setUsedYn("Y");
                    cmpyInfo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgbn.purs"))); // ????????? ??????????????? ??????
                    cmpyInfo.setCustNo(custNo);
                    cmpyRepo.save(cmpyInfo);
                }

                cmvo.setUsedYn("Y");
                cmvo.setDefaultYn("N");

                MatrCmpy cmpychk = matrCmpyRepository.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,matrInfo.getMatrNo(),cmpyNo,"Y");
                if(cmpychk != null){
                    cmvo.setMatrCmpyNo(cmpychk.getMatrCmpyNo());
                    cmvo.setCmpyNo(cmpychk.getCmpyNo());
                    cmvo.setMatrNo(cmpychk.getMatrNo());
                    cmvo.setRegIp(cmpychk.getRegIp());
                    cmvo.setRegId(cmpychk.getRegId());
                    cmvo.setRegDt(cmpychk.getRegDt());
                }else{
                    cmvo.setMatrCmpyNo(0L);
                    cmvo.setMatrNo(matrInfo.getMatrNo());
                    cmvo.setCmpyNo(cmpyNo);
                    cmvo.setModIp(paraMap.get("ipaddr").toString());
                    cmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    cmvo.setModDt(DateUtils.getCurrentDate());
                }
                cmvo.setCustNo(custNo);
                matrCmpyRepository.save(cmvo);
            }
        }
    }


    @Override
    public List<Map<String ,Object>> getMatrInspUser(Map<String, Object> paraMap){
        return matrMapper.getMatrInspUser(paraMap);
    }
}
