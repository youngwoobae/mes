package daedan.mes.stock.service;

import com.google.zxing.WriterException;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.ImageUtils;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.stock.domain.*;
import daedan.mes.stock.mapper.StockMapper;
import daedan.mes.stock.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("stockService")
public class StockServiceImpl implements  StockService{
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private StockMapper mapper;


    @Autowired
    private MatrStkRepository matrStkRepo;


    @Autowired
    private StkClosRepository stkClosRepo;

    @Autowired
    private MatrStkClosRepository matrStkClosRepo;

    @Autowired
    private ProdStkClosRepository prodStkClosRepo;

    @Autowired
    private MatrStkHstrRepository matrStkHstrRepo;

    @Autowired
    private ProdStkRepository prodStkRepo;

    @Autowired
    private ProdIwhRepository prodIwhRepo;

    @Autowired
    MatrStkRepository MatrStkRepos;

    @Autowired
    private ProdStkHstrRepository prodStkHstrRepo;

    @Autowired
    private MatrPosRepository matrPosRepo;

    @Autowired
    private WhInfoRepository whInfoRepo;

    @Autowired
    private MatrIwhRepository matrIwhRepo;

    @Override
    @Transactional
    public void addOnStock(Map<String, Object> paraMap) {
        String tag = "StockServiceImpl.addOnStock => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        MatrStk msvo = new MatrStk();
        msvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        msvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
        msvo.setStatCd(Long.valueOf(env.getProperty("code.stk_iwh_stat")));
        msvo.setStatTrfDt(DateUtils.getCurrentDate());

        msvo.setUsedYn("Y");
        msvo.setModDt(DateUtils.getCurrentDateTime());
        msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        msvo.setModIp(paraMap.get("ipaddr").toString());

        try {
            msvo.setValidDt(sdf.parse(paraMap.get("validDt").toString().substring(0,10)));
        } catch (ParseException e) {

        }

        MatrStk chkvo =  matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(), msvo.getUsedYn());
        if (chkvo == null) {
            log.info(tag + "1. ?????? ???????????? ??????. ?????? ??????");
            msvo.setMatrStkNo(0L);
            msvo.setStkQty(Float.parseFloat(paraMap.get("stkQty").toString()));
            msvo.setRegDt(DateUtils.getCurrentDate());
            msvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setRegIp(paraMap.get("ipaddr").toString());
            msvo.setCustNo(custNo);
            chkvo = matrStkRepo.save(msvo);
        }

        Map<String,Object>   smap = new HashMap<String,Object>();
        smap.put("matr_stk_no", chkvo.getMatrStkNo());
        smap.put("matr_no",paraMap.get("matrNo"));
        smap.put("wh_no",paraMap.get("whNo"));
        smap.put("matr_stat",Long.parseLong(env.getProperty("code.stk_iwh_stat")));
        if (env.getProperty("matr_pos_yn").equals("Y")) { //?????????????????? ???????????? ?????? : ES ????????????????????? 'Y'??? ?????????
            log.info("2.?????????????????? ?????? ?????? ?????? : ????????????????????????(mapr_pos)?????? ????????? ??????(641)??? ????????? ???????????? ???????????? adjus");
            Map<String,Object> stkmap = mapper.getMatrStockByPos(smap);
            chkvo.setStkQty(Float.parseFloat(stkmap.get("stkQty").toString()));
            chkvo.setStatTrfDt(DateUtils.getCurrentDate());
            chkvo.setCustNo(custNo);
            matrStkRepo.save(chkvo);
        }
        else {
            log.info("?????? ????????? ?????? ?????? ?????? ??????");
            Map<String,Object> stkmap = mapper.getMatrStockByIo(smap);
            chkvo.setStkQty(Float.parseFloat(stkmap.get("stkQty").toString()));
            chkvo.setStatTrfDt(DateUtils.getCurrentDate());
            chkvo.setValidDt(msvo.getValidDt());
            chkvo.setCustNo(custNo);
            matrStkRepo.save(chkvo);
        }
    }

    @Override
    public void reduceStock(Map<String, Object> paraMap) {
        mapper.reduceStock(paraMap);
    }

    @Override
    public void dropWhInfo(Map<String, Object> paraMap) {
        mapper.dropWhInfo(paraMap);
    }


    @Override
    public List<Map<String, Object>> getMatrRealStockList(HashMap<String, Object> paraMap) {
        return mapper.getMatrRealStockList(paraMap);
    }

    @Override
    public int getMatrRealStockListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrRealStockListCount(paraMap);
    }

    @Override
    public void resetMatrStock(Map<String, Object> paraMap) {
        MatrInfo matrvo = new MatrInfo();
    }

    @Override
    public void saveMatrPos(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrPos mpvo = new MatrPos();
        mpvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mpvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString())); //????????????

        try{
            mpvo.setIwhSeq(Integer.parseInt(paraMap.get("iwh_seq").toString()));
            mpvo.setStairIdx(Integer.parseInt(paraMap.get("stair_idx").toString())); //???
            mpvo.setRowIdx(Integer.parseInt(paraMap.get("row_idx").toString())); //???
            mpvo.setColIdx(Integer.parseInt(paraMap.get("col_idx").toString())); //???
            mpvo.setMatrStat(Long.parseLong(paraMap.get("matr_stat").toString()));
        }catch(NullPointerException ne){
            mpvo.setIwhSeq(0);
            mpvo.setStairIdx(1); //???
            mpvo.setRowIdx(1); //???
            mpvo.setColIdx(1); //???
            mpvo.setMatrStat(Long.parseLong(env.getProperty("code.matrst.purs")));
        }

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            mpvo.setOwhDt(sdf.parse(paraMap.get("iwh_dt").toString()));
            mpvo.setOwhDt(sdf.parse(paraMap.get("owh_dt").toString()));
        }
        catch (NullPointerException | ParseException ne) {

        }
        mpvo.setModDt(DateUtils.getCurrentDate());
        mpvo.setModIp(paraMap.get("ipaddr").toString());
        mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        mpvo.setMatrQty(Float.parseFloat(paraMap.get("matr_qty").toString()));
        mpvo.setUsedYn("Y");

        try {
            mpvo.setMatrPosNo(Long.parseLong(paraMap.get("matr_pos_no").toString()));
        }
        catch (NullPointerException ne) {
            MatrPos chkvo = matrPosRepo.findByCustNoAndMatrNoAndIwhSeqAndUsedYn(custNo,mpvo.getMatrNo(), mpvo.getIwhSeq(),"Y");
            if (chkvo != null) {
                mpvo.setMatrPosNo(chkvo.getMatrPosNo());
            }
            else {
                mpvo.setRegIp(paraMap.get("ipaddr").toString());
                mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setRegDt(DateUtils.getCurrentDate());
            }
        }
        mpvo.setCustNo(custNo);
        matrPosRepo.save(mpvo);

    }
    /*???????????? ???????????? ??????*/
    @Override
    public void savePursMatrPos(Map<String, Object> paraMap)  {
        String tag = "StockService.savePursMaprPos => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Integer pursQty = Integer.parseInt(paraMap.get("pursQty").toString());
        StringBuffer buf = new StringBuffer();
        for (int idx = 0; idx < pursQty; idx++) {
            MatrPos mpvo = new MatrPos();
//            mpvo.setPursMatrNo(Long.parseLong(paraMap.get("pursMatrNo").toString()));
            mpvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
            Long whNo = Long.parseLong(paraMap.get("whNo").toString());
            WhInfo whvo = whInfoRepo.findByCustNoAndWhNoAndUsedYn(custNo,whNo,"Y");
            mpvo.setWhNo( (whvo != null) ? whvo.getWhNo() : 0L);
            mpvo.setStairIdx(0); //???
            mpvo.setRowIdx(0);   //???
            mpvo.setColIdx(0);   //???
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                mpvo.setOwhDt(sdf.parse(paraMap.get("owhDt").toString()));
            }
            catch (NullPointerException | ParseException ne) {

            }
            try {
                mpvo.setMatrStat(Long.parseLong(paraMap.get("matrStat").toString()));
            }
            catch (NullPointerException ne) {
                mpvo.setMatrStat(Long.parseLong(env.getProperty("code.matrst.purs")));
            }
            mpvo.setUsedYn("Y");
            mpvo.setIwhSeq(idx);
            mpvo.setRegDt(DateUtils.getCurrentDate());
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModDt(DateUtils.getCurrentDate());
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

            MatrPos chkvo = matrPosRepo.findByCustNoAndMatrNoAndIwhSeqAndUsedYn(custNo,mpvo.getMatrNo(),mpvo.getIwhSeq(),"Y");
            if (chkvo != null) {
                mpvo.setMatrPosNo(chkvo.getMatrPosNo());
            }
            mpvo.setCustNo(custNo);
            mpvo = matrPosRepo.save(mpvo);
            int height = Integer.parseInt(env.getProperty("bar_qr_height"));
            int width  = Integer.parseInt(env.getProperty("bar_qr_width"));

            try {
                buf.setLength(0);
                buf.append(fileRoot).append("purs/");
                File file = new File( buf.toString());
                File rootPath = file.getAbsoluteFile();
                log.info(tag + "?????? ??????????????? ?????? : "+rootPath );
                if (! rootPath.exists()){
                    rootPath.mkdirs();
                }

                //String imgUrl = (buf.toString());
                //log.info(tag + "save bar code path = " + buf.toString());//kill
                byte[] barCodeImg = ImageUtils.createBarImage(rootPath.getPath(), Long.toString(mpvo.getMatrPosNo()),height,width);
                mpvo.setBarCodeImg(barCodeImg);
                mpvo.setCustNo(custNo);
                matrPosRepo.save(mpvo);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getComboWh(Map<String, Object> paraMap) {
        return mapper.getComboWh(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWhList(Map<String, Object> paraMap) {
        return mapper.getWhList(paraMap);
    }

    @Override
    public int getWhListCount(Map<String, Object> paraMap) {
        return mapper.getWhListCount(paraMap);
    }



    @Transactional
    @Override
    public void saveWhInfo(Map<String, Object> paraMap) {
        Map<String, Object> passMap = (Map<String, Object>) paraMap.get("whInfo");
        WhInfo vo = new WhInfo();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        Long userId = Long.parseLong(paraMap.get("userId").toString());

        vo.setCustNo(custNo); //AddOn By KMJ At 21.10.21
        try {
            vo.setWhNo(Long.parseLong(passMap.get("whNo").toString()));
        } catch (NullPointerException ne) {
            vo.setWhNo(0L);
        }
        vo.setWhNm(paraMap.get("whNm").toString());
        vo.setSaveTmpr(Long.parseLong(paraMap.get("saveTmpr").toString()));
        vo.setWhTp(Long.parseLong(paraMap.get("whTp").toString()));
        vo.setWhLocNo(Byte.valueOf(paraMap.get("whLocNo").toString()));
        try {
            vo.setMaxRow(Integer.parseInt(paraMap.get("maxRow").toString()));
        } catch (NullPointerException ne) {
            vo.setMaxRow(0);
        } catch (NumberFormatException nfe) {
            vo.setMaxRow(0);
        }
        try {
            vo.setMaxCol(Integer.parseInt(paraMap.get("maxCol").toString()));
        } catch (NullPointerException ne) {
            vo.setMaxCol(0);
        } catch (NumberFormatException nfe) {
            vo.setMaxCol(0);
        }

        try {
            vo.setMaxStair(Integer.parseInt(paraMap.get("maxStair").toString()));
        } catch (NullPointerException ne) {
            vo.setMaxStair(0);
        } catch (NumberFormatException nfe) {
            vo.setMaxStair(0);
        }


        vo.setModDt(DateUtils.getCurrentBaseDateTime());
        vo.setModIp(ipaddr);
        vo.setModId(userId);
        vo.setUsedYn("Y");
        vo.setCustNo(custNo);
        WhInfo chkvo = whInfoRepo.findByCustNoAndWhNoAndUsedYn(custNo, vo.getWhNo(), "Y");
        if (chkvo != null) {
            vo.setRegDt(chkvo.getRegDt());
            vo.setRegIp(chkvo.getRegIp());
            vo.setRegId(chkvo.getRegId());
        } else {
            vo.setRegDt(DateUtils.getCurrentBaseDateTime());
            vo.setRegId(userId);
            vo.setRegIp(ipaddr);
        }
        whInfoRepo.save(vo);
    }

    @Override
    public Map<String, Object> getWhInfo(HashMap<String, Object> paraMap) {
        return mapper.getWhInfo(paraMap);
    }



    @Override
    public List<Map<String, Object>> getMatrPosList(HashMap<String, Object> paraMap) {
        return mapper.getMatrPosList(paraMap);
    }

    @Override
    public int getMatrPosListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrPosListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkList(HashMap<String, Object> paraMap) {
        return mapper.getMatrStkList(paraMap);
    }

    @Override
    public int getMatrStkListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrStkListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMatrPosInfo(HashMap<String, Object> paraMap) {
        Map<String,Object> rmap = mapper.getMatrPosInfo(paraMap);
        StringBuffer buf = new StringBuffer();
        buf.append(env.getProperty("img.root.path")).append(rmap.get("matr_pos_no")).append(".png");
        rmap.put("bar_code_url",buf.toString());
        return rmap;
    }
    @Override
    /*
        paraMap.codeNo
        parap.savePath : ex(prod,matr)
     */
    public byte[] makeBarCode(Map<String, Object> paraMap) {
        StringBuffer buf = new StringBuffer();
        String tag = "StockService.makeBarcode => ";
        log.info(tag + "Params = " + paraMap.toString());
        //buf.append(env.getProperty("file.root.path")).append(paraMap.get("savePath").toString());
        File file = new File( "fileroot/" + paraMap.get("savePath").toString());
        File rootPath = file.getAbsoluteFile();
        log.info(tag + "?????? ??????????????? ?????? : "+rootPath );
        if (! rootPath.exists()){
            rootPath.mkdirs();
        }

        int height = Integer.parseInt(env.getProperty("bar_qr_height"));
        int width = Integer.parseInt(env.getProperty("bar_qr_width"));
        byte[] barCodeImg = new byte[0];
        try {
            String barCodeNo  = paraMap.get("codeNo").toString();
            barCodeImg = ImageUtils.createBarImage(rootPath.getPath(), barCodeNo, height, width);
            //prodInfo.setBarCodeImg(barCodeImg);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barCodeImg;
    }

    @Override
    @Transactional
    public void adjustMatrStock(Map<String, Object> paraMap) {
        String tag = "StockService.adjustMatrStock ==> ";
        log.info(tag + "parms = "  + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrStk msvo = new MatrStk();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        msvo.setWhNo(Long.valueOf(paraMap.get("whNo").toString()));
        msvo.setMatrNo(Long.valueOf(paraMap.get("matrNo").toString()));
        msvo.setStkQty(Float.valueOf(String.valueOf(mapper.getMatrCurrentStock(paraMap)))); //??????????????? = ???????????? - ????????????

        msvo.setStatTrfDt((Date) paraMap.get("statTrfDt"));
        msvo.setUsedYn("Y");
        msvo.setModDt(DateUtils.getCurrentDateTime());
        msvo.setStatCd(Long.valueOf(paraMap.get("statCd").toString()));
        msvo.setModId(Long.valueOf(paraMap.get("userId").toString()));
        msvo.setModIp(paraMap.get("ipaddr").toString());
        msvo.setCustNo(custNo);
        MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(),"Y");
        if (chkvo != null) {
            msvo.setMatrStkNo(chkvo.getMatrStkNo());
        }
        else {
            msvo.setStkQty(msvo.getStkQty() + Float.valueOf(String.valueOf(paraMap.get("iwhQty"))) ); //??????????????? = ???????????? - ???????????? + ???????????????????????? ??????
            msvo.setRegDt(DateUtils.getCurrentDateTime());
            msvo.setRegId(Long.valueOf(paraMap.get("userId").toString()));
            msvo.setRegIp(paraMap.get("ipaddr").toString());
        }
        msvo.setCustNo(custNo);
        msvo = matrStkRepo.save(msvo);
        /*Remarked By KMJ AT 21.10.27 --????????????
        log.info(tag + "???????????????????????? ????????? ????????? ??????===> ");
        Map<String,Object> bmap = new HashMap<String,Object>();
        bmap.put("codeNo",msvo.getMatrStkNo());
        bmap.put("savePath","matr/owh/");
        this.makeBarCode(bmap);
         */

    }

    /*??????????????? ?????? ????????????
      1. ??????????????? ????????????(frWhNo)????????? ????????? ????????????(toWhNo)??? ??????
         1.1 ?????? ????????? ?????? ??????
            - ???????????? ??? ???????????? ??????
         1.2. frWhNo ??? toWhNo??? ????????????
            - frWhNo.realStk??? toWhNo.realStk??? ??????.
            - frWhNo??? ???????????? ???????????? ?????? ?????? (usedYn = 'N')
         1.3 ???????????? (1.1??? 1.2?????? ?????? ??????)
            - ???????????? (matr_iwh ??????) / ???????????? (????????? ????????? ???????????? ???????????? adjust?????? ?????????)
     */
    @Override
    @Transactional
    public void realStockSave(Map<String, Object> paraMap) {
        String tag = "vsvc.StockService.realStockSave ==> ";
        log.info(tag + "parms = "  + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long orgWhNo = 0L;
        Long trkWhNo = 0L;
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        MatrStkHstr mshvo = new MatrStkHstr();
        try{
            mshvo.setChngNo(Long.parseLong(paraMap.get("chngNo").toString())); //????????????????????????
        }catch (NullPointerException en){
            mshvo.setChngNo(0L);
        }

        mshvo.setMatrNo(Long.valueOf(paraMap.get("matrNo").toString())); //????????????
        try {
            orgWhNo = Long.valueOf(paraMap.get("orgWhNo").toString());// ?????????????????????
        }
        catch (NullPointerException ne) {
            orgWhNo = 0L;
        }
        try{
            trkWhNo = Long.valueOf(paraMap.get("trkWhNo").toString());// ?????????????????????
        }
        catch (NullPointerException ne) {
            trkWhNo = orgWhNo;
        }
        mshvo.setWhNo(trkWhNo); //??????????????????

        mshvo.setStkQty(Float.valueOf(paraMap.get("stkQty").toString())); //???????????????

        Long chngResn = Long.valueOf(paraMap.get("chngResn").toString()); //??????ADJUST????????????

        try {
            mshvo.setChngResn(chngResn); //??????ADJUST??????
        }
        catch(NullPointerException ne) {
        }
        mshvo.setChngNo(0L);
        mshvo.setModDt(DateUtils.getCurrentBaseDateTime());
        mshvo.setModId(userId);
        mshvo.setModIp(ipaddr);
        mshvo.setUsedYn("Y");
        mshvo.setChngResn(chngResn);
        mshvo.setRegDt(DateUtils.getCurrentBaseDateTime());
        mshvo.setRegId(userId);
        mshvo.setRegIp(ipaddr);
        mshvo.setCustNo(custNo);
        if (orgWhNo != trkWhNo) { //??????????????? ?????? ??????
            //????????????????????? ????????????????????? ??????
            if (chngResn == Long.parseLong(env.getProperty("code.lose_cd.moveWh"))) {
                log.info("???????????? + ?????? ??? ????????? ?????? ?????? ??????");
                mshvo.setWhNo(trkWhNo);
            } else {
                log.info("???????????? ?????? + ?????? ??? ????????? ?????? ?????? ??????");
                mshvo.setWhNo(orgWhNo);
            }
        }
        mshvo.setCustNo(custNo);
        matrStkHstrRepo.save(mshvo);

        //????????????
        if(chngResn == Long.parseLong(env.getProperty("code.lose_cd.moveWh"))) { //??????????????? ??????
            //???????????? ????????? 0??????
            MatrStk mscvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,orgWhNo,mshvo.getMatrNo(), "Y");
            if (mscvo != null) {
                mscvo.setStkQty(0f);
                mscvo.setStatTrfDt(DateUtils.getCurrentDateTime());
                mscvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_real"))); //??????????????????
                mscvo.setModDt(DateUtils.getCurrentBaseDateTime());
                mscvo.setModId(userId);
                mscvo.setRegIp(ipaddr);
                mscvo.setCustNo(custNo);
                matrStkRepo.save(mscvo);
            }
            //???????????? ???????????? ???????????? ?????????????????? ??????
            List<MatrIwh> mids = matrIwhRepo.findAllByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,orgWhNo,mshvo.getMatrNo(), "Y");
            for (MatrIwh el : mids) {
                el.setModId(userId);
                el.setModIp(ipaddr);
                el.setModDt(DateUtils.getCurrentBaseDateTime());
                el.setRegId(userId);
                el.setRegIp(ipaddr);
                el.setRegDt(DateUtils.getCurrentBaseDateTime());
                el.setWhNo(trkWhNo);
                el.setUsedYn("Y");
                el.setCustNo(custNo);
                MatrIwh mivo = matrIwhRepo.findByCustNoAndWhNoAndMatrNoAndIwhDtAndUsedYn(custNo,el.getWhNo(),el.getMatrNo(),el.getIwhDt(),"Y");
                if (mivo != null) {
                    matrIwhRepo.save(el);
                }
            }
            //???????????? ???????????? ??????
            mids = matrIwhRepo.findAllByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,orgWhNo,mshvo.getMatrNo(), "Y");
            for (MatrIwh el : mids) {
                el.setModId(userId);
                el.setModIp(ipaddr);
                el.setModDt(DateUtils.getCurrentBaseDateTime());
                el.setUsedYn("N");
                el.setCustNo(custNo);
                matrIwhRepo.save(el);
            }
        }
        //????????????
        MatrStk mscvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,trkWhNo,mshvo.getMatrNo(), "Y");
        if (mscvo == null) { //??????????????? ????????????
            mscvo = new MatrStk();
            mscvo.setMatrStkNo(0L);
            mscvo.setRegIp(paraMap.get("ipaddr").toString());
            mscvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mscvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        }
        mscvo.setModIp(ipaddr);
        mscvo.setModDt(DateUtils.getCurrentBaseDateTime());
        mscvo.setModId(userId);
        mscvo.setWhNo(trkWhNo);
        mscvo.setMatrNo(mshvo.getMatrNo());
        mscvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_real"))); //??????????????????
        mscvo.setUsedYn("Y");
        mscvo.setStkQty(mshvo.getStkQty());
        mscvo.setStatTrfDt(DateUtils.getCurrentDateTime());
        mscvo.setCustNo(custNo);
        matrStkRepo.save(mscvo);


    }

    @Override
    @Transactional
    public void saveProdStock(HashMap<String, Object> paraMap) {
        String tag = "StockService.saveProdStock ==> ";
        log.info(tag + "parms = "  + paraMap.toString());

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        Long chngWhNo = 0L; //????????? ????????????
        Long whNo = 0L;  //????????? ????????????
        Long chngResn = Long.parseLong(paraMap.get("chngResn").toString());

        ProdStkHstr prsvo = new ProdStkHstr();
        //????????? ???????????? ??????
        whNo = Long.parseLong(paraMap.get("whNo").toString());
        //?????? ??? ???????????? ??????
        try{
            chngWhNo = Long.parseLong(paraMap.get("chngWhNo").toString());
        }catch(NullPointerException ne){
            chngWhNo = whNo;
        }
        prsvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        prsvo.setWhNo(chngWhNo);
        prsvo.setStkQty(Float.parseFloat(paraMap.get("stkQty").toString()));
        prsvo.setCustNo(custNo);
        prsvo.setUsedYn("Y");
        prsvo.setModDt(DateUtils.getCurrentBaseDateTime());
        prsvo.setModId(userId);
        prsvo.setModIp(ipaddr);
        try{
            prsvo.setChngNo(Long.parseLong(paraMap.get("chngNo").toString()));

        }catch(NullPointerException ne){
            prsvo.setChngNo(0L);
            prsvo.setRegId(userId);
            prsvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            prsvo.setRegIp(ipaddr);
        }
        log.info("1. ???????????? ??????");
        prsvo = prodStkHstrRepo.save(prsvo);

        log.info("2. ???????????? ??????");
        if(chngWhNo != whNo) {
            log.info("2.1. ???????????? ?????? ?????? ????????? ???????????? ??????.");
            ProdStk psvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo, whNo, prodNo, "Y");
            if (psvo != null) {
                psvo.setUsedYn("N");
                psvo.setModId(userId);
                psvo.setModDt(DateUtils.getCurrentBaseDateTime());
                psvo.setModIp(ipaddr);
                prodStkRepo.save(psvo);
            }
        }
        log.info("3. ???????????? ?????????");
        ProdStk psvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,chngWhNo,prodNo,"Y");
        if (psvo != null) {
            psvo.setStatTrfDt(DateUtils.getCurrentBaseDateTime());
            psvo.setStkQty(prsvo.getStkQty());
            psvo.setStkDt(psvo.getStatTrfDt());
            psvo.setModId(userId);
            psvo.setModDt(DateUtils.getCurrentBaseDateTime());
            psvo.setModIp(ipaddr);
        }
        else {
            psvo = new ProdStk();
            psvo.setStkNo(0L);
            psvo.setCustNo(custNo);
            psvo.setProdNo(prsvo.getProdNo());
            psvo.setWhNo(chngWhNo);
            psvo.setStatTrfDt(DateUtils.getCurrentBaseDateTime());
            psvo.setStkQty(prsvo.getStkQty());
            psvo.setStkDt(DateUtils.getCurrentBaseDateTime());
            psvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            psvo.setRegIp(ipaddr);
            psvo.setRegId(userId);
            psvo.setModId(userId);
            psvo.setModDt(DateUtils.getCurrentBaseDateTime());
            psvo.setModIp(ipaddr);
            psvo.setUsedYn("Y");
        }
        prodStkRepo.save(psvo);
    }

    @Override
    public List<Map<String, Object>> getMatrIoList(HashMap<String, Object> paraMap) {
        return mapper.getMatrIoList(paraMap);
    }

    @Override
    public int getMatrIoListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrIoListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdIoList(HashMap<String, Object> paraMap) {
        return mapper.getProdIoList(paraMap);
    }

    @Override
    public int getProdIoListCount(HashMap<String, Object> paraMap) {
        return mapper.getProdIoListCount(paraMap);
    }

    //????????????/?????????
    @Override
    public List<Map<String, Object>> getStkClosList(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getStkClosList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getStkClosList(paraMap);
    }
    @Override
    public int getStkClosListCount(HashMap<String, Object> paraMap) {
        return mapper.getStkClosListCount(paraMap);
    }

    //????????????/??????
    @Override
    public List<Map<String, Object>> getProdStkClosList(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getProdStkClosList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdStkClosList(paraMap);
    }
    @Override
    public int getProdStkClosListCount(HashMap<String, Object> paraMap) {
        return mapper.getProdStkClosListCount(paraMap);
    }
    //????????????/??????
    @Override
    public List<Map<String, Object>> getMatrStkClosList(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getMatrStkClosList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMatrStkClosList(paraMap);
    }
    @Override
    public int getMatrStkClosListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrStkClosListCount(paraMap);
    }
    @Override
    public List<Map<String, Object>> getStkClosMatrIwhDetlHstr(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getStkClosMatrIwhDetlHstr => ";
        log.info(tag + " paramMap = " + paraMap.toString());
        return mapper.getStkClosMatrIwhDetlHstr(paraMap);
    }

    @Override
    public int getStkClosMatrIwhDetlHstrCount(HashMap<String, Object> paraMap) {
        return  mapper.getStkClosMatrIwhDetlHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getStkClosMatrOwhDetlHstr(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getStkClosMatrOwhDetlHstr => ";
        log.info(tag + " paramMap = " + paraMap.toString());
        return mapper.getStkClosMatrOwhDetlHstr(paraMap);
    }

    @Override
    public int getStkClosMatrOwhDetlHstrCount(HashMap<String, Object> paraMap) {
        return mapper.getStkClosMatrOwhDetlHstrCount(paraMap);
    }



    @Override
    public List<Map<String, Object>> getStkClosProdIwhDetlHstr(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getStkClosProdIwhDetlHstr => ";
        log.info(tag + " paramMap = " + paraMap.toString());
        return mapper.getStkClosProdIwhDetlHstr(paraMap);
    }

    @Override
    public int getStkClosProdIwhDetlHstrCount(HashMap<String, Object> paraMap) {
        return mapper.getStkClosProdIwhDetlHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getStkClosProdOwhDetlHstr(HashMap<String, Object> paraMap) {
        String tag = "vsvc.stockService.getStkClosProdOwhDetlHstr => ";
        log.info(tag + " paramMap = " + paraMap.toString());
        return mapper.getStkClosProdOwhDetlHstr(paraMap);
    }

    @Override
    public int getStkClosProdOwhDetlHstrCount(HashMap<String, Object> paraMap) {
        return mapper.getStkClosProdOwhDetlHstrCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getMatrProdIoYearList(HashMap<String, Object> paraMap) {
       List<Map<String,Object>> ds = mapper.getMatrProdIoYearList(paraMap);
       if(ds.size() == 1){
           ds = mapper.getCurrentYear(paraMap);
       }
       return ds;
    }

    @Override
    public List<Map<String, Object>> getMatrStockPosList(HashMap<String, Object> paraMap) {
        List<Map<String, Object>> ds = mapper.getMatrStockList(paraMap);
        int maxCol = Integer.parseInt(paraMap.get("maxCol").toString());
        int maxRow = Integer.parseInt(paraMap.get("maxRow").toString());
        int arSize = maxCol * maxRow;
        //Float arlayoutPos[] = new Float[arSize];
        int idx = -1;
        int layoutPos = 0;
        /*
        for (int row = 0; row < maxRow; row++ ) {
            for (int col = 0; col < maxCol; col++) {
                arlayoutPos[++idx] = 0f;
            }
        }
         */

        List<Map<String,Object>> posList = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                HashMap posmap = new HashMap<String, Object>();
                layoutPos = ((idx + 1) / maxCol) * maxCol;
                try {
                    map = ds.get(layoutPos);
                    try {
                        posmap.put("martCnt", Float.parseFloat(map.get("matrQty").toString()));
                    } catch (NullPointerException ne) {
                        posmap.put("martCnt", 0f);
                    }
                }
                catch (IndexOutOfBoundsException ie) {
                    posmap.put("martCnt", 0f);
                }
                posList.add(posmap);
            }
        }
        return posList;
    }

    /*
    Map<String,Object> bmap = new HashMap<String,Object>();
        bmap.put("codeNo",pivo.getPursNo());
        bmap.put("savePath","purs/");
        stockService.makeBarCode(bmap);
        log.info(tag + "???????????? ?????????????????? ?????? ???........");
        log.info(tag + "?????????????????? ???.......");
    */

    @Override
    public List<Map<String, Object>> getMatrStockListAndLayout(HashMap<String, Object> paraMap) {
        return mapper.getMatrStockListAndLayout(paraMap);
    }

    @Override
    public int getMatrStockListAndLayoutCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrStockListAndLayoutCount(paraMap);
    }

    @Override
    public int getCheckMatrIwhListCount(HashMap<String, Object> paraMap){
        return mapper.getCheckMatrIwhListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMatrStockByIo(Map<String, Object> paraMap) {
        return mapper.getMatrStockByIo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrForRealStkList(Map<String, Object> paraMap){
        return mapper.getMatrForRealStkList(paraMap);
    }

    @Override
    public int getMatrForRealStkListCount(Map<String, Object> paraMap){
        return mapper.getMatrForRealStkListCount(paraMap);
    }

    @Override
    @Transactional
    public void saveMatrRealStkList(Map<String, Object> paraMap){
        String tag = "vsvc.StockService.saveMatrRealStkList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("realList");
        for (Map<String,Object> el : ds) {
            //??????????????????
            MatrStkHstr mshvo = new MatrStkHstr();

            mshvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            mshvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
            mshvo.setStkQty(Float.parseFloat(el.get("realQty").toString()));
            mshvo.setChngResn(Long.parseLong(el.get("chngResn").toString()));
            mshvo.setUsedYn("Y");
            mshvo.setRegDt(DateUtils.getCurrentDate());
            mshvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mshvo.setRegIp(paraMap.get("ipaddr").toString());
            MatrStkHstr chkvo = matrStkHstrRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,mshvo.getWhNo(), mshvo.getMatrNo() ,"Y");
            if (chkvo != null) {
                mshvo.setChngNo(chkvo.getChngNo());
            }
            mshvo.setCustNo(custNo);
            matrStkHstrRepo.save(mshvo);

            //????????????
            MatrStk stkvo = new MatrStk();
            stkvo.setStkQty(Float.parseFloat(el.get("realQty").toString()));
            stkvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
            stkvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            stkvo.setStatTrfDt(DateUtils.getCurrentDate());
            stkvo.setUsedYn("Y");
            stkvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_real")));

            MatrStk chkstkvo = matrStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo,stkvo.getMatrNo(),"Y");
            if (chkstkvo != null) {
                stkvo.setMatrStkNo(chkstkvo.getMatrStkNo());
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                stkvo.setModIp(paraMap.get("ipaddr").toString());
            }
            else {
                stkvo.setMatrStkNo(0L);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                stkvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            stkvo.setCustNo(custNo);
            matrStkRepo.save(stkvo);
        }
    }


    @Override
    public List<Map<String, Object>> getProdForRealStkList(Map<String, Object> paraMap){
        return mapper.getProdForRealStkList(paraMap);
    }

    @Override
    public int getProdForRealStkListCount(Map<String, Object> paraMap){
        return mapper.getProdForRealStkListCount(paraMap);
    }

    @Override
    @Transactional
    public void saveProdRealStkList(Map<String, Object> paraMap){
        String tag = "vsvc.StockService.saveProdRealStkList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("realList");
        for (Map<String,Object> el : ds) {
            //??????????????????
            ProdStkHstr prvo = new ProdStkHstr();

            prvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            prvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
            prvo.setStkQty(Float.parseFloat(el.get("realQty").toString()));
            prvo.setChngResn(Long.parseLong(el.get("chngResn").toString()));
            prvo.setUsedYn("Y");
            prvo.setRegDt(DateUtils.getCurrentDate());
            prvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            prvo.setRegIp(paraMap.get("ipaddr").toString());
            ProdStkHstr chkvo = prodStkHstrRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,prvo.getWhNo(), prvo.getProdNo() ,"Y");
            if (chkvo != null) {
                prvo.setChngNo(chkvo.getChngNo());
            }
            prvo.setCustNo(custNo);
            prodStkHstrRepo.save(prvo);

            //????????????
            ProdStk stkvo = new ProdStk();
            stkvo.setStkQty(Float.parseFloat(el.get("realQty").toString()));
            stkvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
            stkvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            stkvo.setStatTrfDt(DateUtils.getCurrentDate());
            stkvo.setUsedYn("Y");
            stkvo.setStkDt(DateUtils.getCurrentDate());

            ProdStk chkstkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,stkvo.getWhNo(),stkvo.getProdNo(),"Y");
            if (chkstkvo != null) {
                stkvo.setStkNo(chkstkvo.getStkNo());
                stkvo.setModDt(DateUtils.getCurrentDateTime());
                stkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                stkvo.setModIp(paraMap.get("ipaddr").toString());
            }
            else {
                stkvo.setStkNo(0L);
                stkvo.setRegDt(DateUtils.getCurrentDateTime());
                stkvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                stkvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            stkvo.setCustNo(custNo);
            prodStkRepo.save(stkvo);
        }
    }

    @Override
    public void dropMatrRelStk(Map<String, Object> paraMap) {

        Long chngNo = Long.valueOf(paraMap.get("matrRealNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrStkHstr chkvo = matrStkHstrRepo.findByCustNoAndChngNoAndUsedYn(custNo,chngNo ,"Y");
        if (chkvo != null) {
            chkvo.setUsedYn("N");
            matrStkHstrRepo.save(chkvo);
        }

        Long matrStkNo = Long.valueOf(paraMap.get("matrStkNo").toString());
        MatrStk chk = matrStkRepo.findByCustNoAndMatrStkNoAndUsedYn(custNo,matrStkNo,"Y");
        if(chk != null){
            chk.setUsedYn("N");
            matrStkRepo.save(chk);
        }
    }
    @Transactional
    @Override
    public void remakeDailyCloseData(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.remakeDailyCloseData => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        mapper.initMatrClosStk(paraMap); //???????????? ?????????????????? ?????????
        mapper.initProdClosStk(paraMap); //???????????? ?????????????????? ?????????
        this.closingMatr(paraMap); //??????????????????
        this.closingProd(paraMap); //????????????????????????

    }

    @Override
    public List<Map<String, Object>> getMatrRealStockHstr(HashMap<String, Object> paraMap) {
        String tag = "stockService.getRealStackHstr =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMatrRealStockHstr(paraMap);
    }

    @Override
    public int getMatrRealStockHstrCount(Map<String, Object> paraMap) {
        return mapper.getMatrRealStockHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdRealStockHstr(HashMap<String, Object> paraMap) {
        String tag = "stockService.getRealStackHstr =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProdRealStockHstr(paraMap);
    }

    @Override
    public int getProdRealStockHstrCount(Map<String, Object> paraMap) {
        return mapper.getProdRealStockHstrCount(paraMap);
    }

    @Override
    public Map<String, Object> getShdList(Map<String, Object> paraMap) {
        String tag = "stockService.getShdList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        ArrayList<String> dsStkDate = new ArrayList<String>();
        ArrayList<Long> dsOrdRecv = new ArrayList<Long>();
        ArrayList<Long> dsProd = new ArrayList<Long>();
        ArrayList<Long> dsCmpy = new ArrayList<Long>();

        ArrayList<Integer> dsStkQty = new ArrayList<Integer>();
        ArrayList<Integer> dsReqQty = new ArrayList<Integer>();
        ArrayList<Integer> dsPlanQty = new ArrayList<Integer>();

        Map<String,Object> rmap = new HashMap<String,Object>();

        List<Map<String,Object>> ds = mapper.getShdList(paraMap);
        List<Map<String,Object>> dsPlan = mapper.getShdPlanList(paraMap);
        int idx = -1;
        Map<String,Object> planMap = null;
        for (Map<String, Object> el : ds) {
            dsStkDate.add(el.get("date").toString());
            dsOrdRecv.add(Long.parseLong(el.get("ordRecvNo").toString()));
            dsCmpy.add(Long.parseLong(el.get("cmpyNo").toString()));
            dsProd.add(Long.parseLong(el.get("prodNo").toString()));
            dsStkQty.add((int) Math.ceil(Float.parseFloat(el.get("stkQty").toString())));
            dsReqQty.add((int) Math.ceil(Float.parseFloat(el.get("reqQty").toString())));

            idx++;
            planMap = dsPlan.get(idx);
            dsPlanQty.add((int) Math.ceil(Float.parseFloat(planMap.get("planQty").toString())));
        }
        rmap.put("ordRecvNo", dsOrdRecv);
        rmap.put("cmpyNo"   , dsCmpy);
        rmap.put("prodNo"   , dsProd);
        rmap.put("stkDt"    , dsStkDate);
        rmap.put("stkQty"   , dsStkQty);
        rmap.put("reqQty"   , dsReqQty);
        rmap.put("planQty"  , dsPlanQty);
        return rmap;
    }


    private void closingMatr(Map<String,Object> paraMap) {
        List<Map<String, Object>> dsProd = mapper.getMatrClosList(paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for (Map<String, Object> el : dsProd) {
            MatrStkClos matrvo = new MatrStkClos();
            try {
                matrvo.setClosDt(sdf.parse(paraMap.get("procDt").toString()));
                matrvo.setWhNo(Long.parseLong(el.get("whNo").toString()));
                matrvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
                matrvo.setIwhQty(Float.parseFloat(el.get("iwhQty").toString()));
                matrvo.setOwhQty(Float.parseFloat(el.get("owhQty").toString()));
                matrvo.setStkQty(Float.parseFloat(el.get("stkQty").toString()));
                matrvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos"))); //?????????
                matrvo.setModDt(DateUtils.getCurrentDateTime());
                matrvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                matrvo.setModIp(paraMap.get("ipaddr").toString());
                MatrStkClos chkVo = matrStkClosRepo.findByCustNoAndClosDtAndWhNoAndMatrNoAndUsedYn(custNo,matrvo.getClosDt(), matrvo.getWhNo(),matrvo.getMatrNo(),"Y");
                matrvo.setUsedYn("Y");
                if (chkVo != null) {
                    matrvo.setMatrStkClosNo(chkVo.getMatrStkClosNo());
                }
                else {
                    matrvo.setMatrStkClosNo(0L);
                    matrvo.setRegDt(DateUtils.getCurrentDateTime());
                    matrvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    matrvo.setRegIp(paraMap.get("ipaddr").toString());
                }
                matrvo.setCustNo(custNo);
                matrStkClosRepo.save(matrvo);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    private void closingProd(Map<String,Object> paraMap) {
        List<Map<String, Object>> dsProd = mapper.getProdClosList(paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> el : dsProd) {
            ProdStkClos prodvo = new ProdStkClos();
            try {
                prodvo.setClosDt(sdf.parse(paraMap.get("procDt").toString()));
                prodvo.setWhNo(Long.parseLong(el.get("whNo").toString()));
                prodvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
                prodvo.setIwhQty(Float.parseFloat(el.get("iwhQty").toString()));
                prodvo.setOwhQty(Float.parseFloat(el.get("owhQty").toString()));
                prodvo.setStkQty(Float.parseFloat(el.get("stkQty").toString()));
                prodvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos")));
                prodvo.setModDt(DateUtils.getCurrentDateTime());
                prodvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                prodvo.setModIp(paraMap.get("ipaddr").toString());
                prodvo.setUsedYn("Y");
                ProdStkClos chkVo = prodStkClosRepo.findByCustNoAndClosDtAndWhNoAndProdNoAndUsedYn(custNo,prodvo.getClosDt(), prodvo.getWhNo(),prodvo.getProdNo(),"Y");
                if (chkVo != null) {
                    prodvo.setProdStkClosNo(chkVo.getProdStkClosNo());
                }
                else {
                    prodvo.setProdStkClosNo(0L);
                    prodvo.setRegDt(DateUtils.getCurrentDateTime());
                    prodvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    prodvo.setRegIp(paraMap.get("ipaddr").toString());
                }
                prodvo.setCustNo(custNo);
                prodStkClosRepo.save(prodvo);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void closingMatrProd(Map<String,Object> paraMap) {
        String tag = "batchService.closingMatrProd =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = mapper.getMatrStkClosList(paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> el : ds) {
            StkClos vo = new StkClos();
            try {
                vo.setClosDt(sdf.parse(paraMap.get("procDt").toString()));
                try {
                    vo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
                } catch (NullPointerException ne) {
                    break;
                }

                try {
                    vo.setMatrIwhQty(Float.parseFloat(el.get("matrIwh_Qty").toString()));
                } catch (NullPointerException ne) {
                    vo.setMatrIwhQty(0f);
                }
                try {
                    vo.setMatrOwhQty(Float.parseFloat(el.get("matr_owh_qty").toString()));
                } catch (NullPointerException ne) {
                    vo.setMatrOwhQty(0f);
                }
                try {
                    vo.setMatrStkQty(Float.parseFloat(el.get("matrStkQty").toString()));
                } catch (NullPointerException ne) {
                    vo.setMatrOwhQty(0f);
                }
                StkClos chkvo = stkClosRepo.findByCustNoAndClosDtAndMatrNo(custNo,vo.getClosDt(), vo.getMatrNo());
                if (chkvo != null) {
                    vo.setStkClosNo(chkvo.getStkClosNo());
                } else {
                    vo.setStkClosNo(0L);
                }
                vo.setCustNo(custNo);
                stkClosRepo.save(vo);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
                break;
            }
        }

        ds = mapper.getProdStkClosList(paraMap);
        for (Map<String, Object> el : ds) {
            StkClos vo = new StkClos();
            try {
                vo.setClosDt(sdf.parse(paraMap.get("procDt").toString()));
                try {
                    vo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
                } catch (NullPointerException ne) {
                    break;
                }
                try {
                    vo.setProdIwhQty(Float.parseFloat(el.get("prodIwhQty").toString()));
                } catch (NullPointerException ne) {
                    vo.setProdIwhQty(0f);
                }
                try {
                    vo.setProdOwhQty(Float.parseFloat(el.get("prodOwhQty").toString()));
                } catch (NullPointerException ne) {
                    vo.setProdOwhQty(0f);
                }

                StkClos chkvo = stkClosRepo.findByCustNoAndClosDtAndProdNo(custNo,vo.getClosDt(), vo.getProdNo());
                if (chkvo != null) {
                    vo.setStkClosNo(chkvo.getStkClosNo());
                } else {
                    vo.setStkClosNo(0L);
                }
                vo.setCustNo(custNo);
                stkClosRepo.save(vo);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public List<Map<String, Object>> getMakePlanBomStkList(Map<String, Object> paraMap) {
        String tag = "StockService.getMakePlanBomStkList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMakePlanBomStkList(paraMap);
    }
}

