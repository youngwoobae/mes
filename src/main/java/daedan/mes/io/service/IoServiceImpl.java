package daedan.mes.io.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.MatrOwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.mapper.IoMapper;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.MatrOwhRepository;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcMatr;
import daedan.mes.make.domain.MakeIndcRslt;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.make.repository.MakeIndcMatrRepository;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.make.repository.MakeIndcRsltRepository;
import daedan.mes.make.service.MakeIndcService;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.mapper.PursMapper;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.purs.service.PursService;
import daedan.mes.stock.domain.*;
import daedan.mes.stock.repository.*;
import daedan.mes.stock.service.StockService;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("ioService")
public class IoServiceImpl implements IoService {
    private final Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private OrdRepository ordRepository;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private ProdStkLotRepository prodStkLotRepo;

    @Autowired
    private MakeIndcRepository makeIndcRepo;

    @Autowired
    private IoMapper mapper;

    @Autowired
    private MatrStkRepository matrStkRepo;

    @Autowired
    private ProdStkRepository prodStkRepo;


    @Autowired
    private PursInfoRepository pursRepo;

      @Autowired
    private PursMatrRepository pmr;

    @Autowired
    private MakeIndcRepository makePepo;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;

    @Autowired
    private MakeIndcRsltRepository makeIndcRsltRepo;

    @Autowired
    private MatrIwhRepository matrIwhRepo;

    @Autowired
    private WhInfoRepository whr;

    @Autowired
    private MatrIwhRepository imr;

    @Autowired
    private MatrOwhRepository omr;

    @Autowired
    private MatrOwhRepository matrOwhRepo;

    @Autowired
    private MatrPosRepository makeposRepo;

    @Autowired
    private MakeIndcService indcService;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepository;

    @Autowired
    private MakeIndcMapper makeIndcMapper;

    @Autowired
    private ProdOwhRepository prodOwhRepo;

    @Autowired
    private ProdIwhRepository prodIwhRepo;

    @Autowired
    private MakeIndcMatrRepository mimRepo;


    @Autowired
    private PursService pursService;

    @Autowired
    private PursMapper pursMapper;

    @Autowired
    private StockService stockService;

    @PersistenceContext
    private EntityManager em; // ????????? ????????? ???????????? ????????????

    @Override
    public List<Map<String, Object>> getComboWhList(Map<String, Object> paraMap) {
        return mapper.getComboWhList(paraMap);
    }
    /*????????? ????????? ???????????? ????????????*/
    @Override
    public List<Map<String, Object>> getComboMatrWhList(Map<String, Object> paraMap) {
        return mapper.getComboMatrWhList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWhList(Map<String, Object> paraMap) {
        return mapper.getWhList(paraMap);
    }

    @Override
    public int getWhListCount(Map<String, Object> paraMap) {
        return mapper.getWhListCount(paraMap);
    }

    @Override
    public Map<String, Object> getWhInfo(Map<String, Object> paraMap) {
        return mapper.getWhInfo(paraMap);
    }




    @Transactional
    @Override
    public void dropWhInfo(Map<String, Object> paraMap) {
        Map<String, Object> passMap = new HashMap<String, Object>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        passMap.put("modId", paraMap.get("userId"));
        passMap.put("ipaddr", paraMap.get("ipaddr"));
        passMap.put("whNo", paraMap.get("whNo"));
        //SOL AddOn By KMJ AT 21.10.16
        WhInfo chkvo = whr.findByCustNoAndWhNoAndUsedYn(custNo,Long.parseLong(paraMap.get("whNo").toString()),"Y");
        if (chkvo != null) {
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setUsedYn("N");
            whr.save(chkvo);
        }
        //EOL AddOn By KMJ AT 21.10.16
        //mapper.dropWhInfo(passMap); Remarked By KMJ AT 21.10.16
    }

    @Override
    public List<Map<String, Object>> getWaitMatrIwhList(Map<String, Object> paraMap) {
        String tag = "IoController.getWaitMatrIwhList => ";
        List<Map<String, Object>> ds = null;
        paraMap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.end")));//????????????
        log.info(tag + "pursSts =" + paraMap.get("pursSts"));
        ds = mapper.getWaitMatrIwhList(paraMap);
        return ds;
    }

    @Override
    public int getWaitMatrIwhListCount(Map<String, Object> paraMap) {
        String tag = "IoController.getWaitMatrIwhList => ";
        int result = 0;
        paraMap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.end")));//????????????
        log.info(tag + "pursSts =" + paraMap.get("pursSts"));
        result = mapper.getWaitMatrIwhListCount(paraMap);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap) {
        String tag = "IoController.getMatrIwhList => ";
        return mapper.getMatrIwhList(paraMap);
    }

    @Override
    public int getMatrIwhListCount(Map<String, Object> paraMap) {
        String tag = "IoController.getMatrIwhList => ";
        return mapper.getMatrIwhListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMatrIwhInfo(Map<String, Object> paraMap) {
        String tag = "IoController.getMatrIwhInfo => ";
        Map<String, Object> map = new HashMap<String, Object>();
        map = mapper.getMatrIwhInfo(paraMap);
        if (map == null) {
            map = pursService.getPursReqMatrInfo(paraMap);
        }
        return map;
    }

    /*
    @Transactional
    public void saveMatrStkOrg(Map<String, Object> paraMap) {
        String tag = "IoController.saveMatrStk => ";
        String[] iwhNos = paraMap.get("iwh_nos").toString().split(",");
        String[] whNos = paraMap.get("wh_nos").toString().split(",");
        String[] pursNos = paraMap.get("purs_nos").toString().split(",");
        String[] matrNos = paraMap.get("matr_nos").toString().split(",");
        String[] iwhQties = paraMap.get("purs_qties").toString().split(",");

        Long pursNo = 0L;
        Long iwhNo = 0L;
        Map<String,Object> dmap = new HashMap<String,Object>();
        for (int idx =0; idx < matrNos.length; idx++) {
            try {
                pursNo = (Long.parseLong(pursNos[idx]));
            }
            catch (NumberFormatException ne) {
                log.info(tag + "???????????? ?????? ???????????? ?????? ?????? ");
                continue;
            }
            try {
                pursNo = (Long.parseLong(pursNos[idx]));
            }
            catch (NumberFormatException ne) {
                log.info(tag + "???????????? ?????? ???????????? ?????? ??????(iwhNo) =  " + Long.parseLong(iwhNos[idx]));
                continue;
            }
            try {
                if (Long.parseLong(whNos[idx]) == 0L) continue;
            }
            catch (NumberFormatException ne) {
                log.info(tag + "??????????????? ???????????? ???????????? ??????(iwhNo) =  " + Long.parseLong(iwhNos[idx]));
            }
            dmap.put("iwhNo",Long.parseLong(iwhNos[idx]));
            log.info(tag + "?????? ????????? ?????? ?????? ?????? ??????");
            mapper.dropMatrIwh(dmap); //???????????????????????? ????????????
        }
        log.info(tag + "????????? ??????????????? ???????????? ??? ??????, ?????????????????? ??????");
        for (int idx =0; idx < iwhNos.length; idx++) {
            MatrIwh imvo = new MatrIwh();
            imvo.setIwhNo(iwhNo); //??????????????????
            imvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString())); //????????????
            imvo.setMatrNo(Long.valueOf(paraMap.get("matr_no").toString())); //????????????

            imvo.setCmpyNo(Long.valueOf(paraMap.get("cmpy_no").toString())); //???????????????
            imvo.setPursNo((Long.parseLong(pursNos[idx]))); //??????????????????
            imvo.setIwhDt(DateUtils.getCurrentDate()); //????????????
            imvo.setIwhQty(Float.parseFloat(iwhQties[idx])); //??????????????????
            imvo.setRetnQty(Float.valueOf(0));
            imvo.setUsedYn("Y"); //????????????

            imvo.setModDt(DateUtils.getCurrentDateTime());
            imvo.setModId(Long.valueOf(paraMap.get("userId").toString()));
            imvo.setModIp(paraMap.get("ipaddr").toString());
            MatrIwh chkvo = imr.findByPursNoAndMatrNoAndUsedYn(imvo.getPursNo(), imvo.getMatrNo(), imvo.getUsedYn());
            if (chkvo != null) {
                log.info(tag + "?????????????????? ?????????. ????????? ???????????? = " + chkvo.getIwhNo());
                imvo.setIwhNo(chkvo.getIwhNo());
            }
            imr.save(imvo); //????????????????????? ??????????????? ?????? ??????????????? ???????????? ??????.

            log.info(tag + "????????? ?????? ????????? ??????_ + " + idx  + " ??????....");
            paraMap.put("stk_qty", imvo.getIwhQty());
            stockService.addOnStock(paraMap);

            // log.info("???????????? ????????? ????????? ??????");
            // paraMap.put("pursNo",imvo.getPursNo());
            // paraMap.put("pursSts",Long.valueOf(env.getProperty("purs.sts.end")));
            // pursService.resetPursStatusEnd(paraMap);
        }
    }
    */
    /*???????????? ?????? ??????*/
    @Override
    public List<Map<String,Object>> getMatrPosList(Map<String, Object> paraMap) {
        return mapper.getMatrPosList(paraMap);

    }
    @Override
    public int getMatrPosListCount(Map<String, Object> paraMap) {
        return mapper.getMatrPosListCount(paraMap);
    }

    /*?????????????????? - prodStk*/
    @Override
    @Transactional
    public void extrProdStk(Map<String, Object> paraMap) {
        String tag = "IoService.extrProdStk(??????????????????, prodStk)==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("extrProdList");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Float stkQty = 0F;
        for (Map<String,Object> el : ds) {
            ProdStk psvo = new ProdStk();
            psvo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            psvo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
            psvo.setUsedYn("Y");
            psvo.setModDt(DateUtils.getCurrentDateTime());
            psvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            psvo.setModIp(paraMap.get("ipaddr").toString());
            //ProdStk chkvo = prodStkRepo.findByWhNoAndProdNoAndUsedYn(psvo.getWhNo(), psvo.getProdNo(), psvo.getUsedYn());
            ProdStk chkvo = prodStkRepo.findByCustNoAndStkNoAndUsedYn(custNo,psvo.getStkNo(), psvo.getUsedYn());
            if(chkvo != null){
                stkQty = chkvo.getStkQty();
                stkQty -= Float.parseFloat(el.get("ord_qty").toString());
                psvo.setStkQty(stkQty);

                psvo.setStkNo(chkvo.getStkNo());

                psvo.setRegDt(chkvo.getRegDt());
                psvo.setRegId(chkvo.getRegId());
                psvo.setRegIp(chkvo.getRegIp());


                psvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            }
            else {
                psvo.setStkNo(0L);
                psvo.setStkQty(0f);
                psvo.setRegDt(DateUtils.getCurrentDateTime());
                psvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                psvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            psvo.setCustNo(custNo);
            prodStkRepo.save(psvo);
        }
    }

    /*?????????????????? - prodOwh*/
    @Override
//    @Transactional
    public void setProdOwh(Map<String, Object> paraMap) {
        String tag = "IoService.setProdOwh(??????????????????, prodOwh)==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("prodOwhList");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Float qty = 0F;
        for (Map<String,Object> el : ds) {
            ProdOwh povo = new ProdOwh();

            povo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
            povo.setOrdNo(Long.parseLong(el.get("ord_no").toString()));
            povo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            povo.setUsedYn("Y");
            povo.setCmpyNo(Long.parseLong(el.get("cmpy_no").toString()));
            try {
                povo.setOwhReqDt(sdf.parse(el.get("dlv_req_dt").toString().substring(0, 10)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ProdOwh chkvo = prodOwhRepo.findByCustNoAndCmpyNoAndProdNoAndWhNoAndOrdNoAndUsedYn(custNo,povo.getCmpyNo(), povo.getProdNo(), povo.getWhNo(), povo.getOrdNo(), povo.getUsedYn());

            if(chkvo != null){
                qty = chkvo.getOwhQty();
                qty += Integer.parseInt(el.get("ord_qty").toString());
                povo.setOwhQty(qty);
                povo.setOwhNo(chkvo.getOwhNo());
                povo.setOwhDt(DateUtils.getCurrentDateTime());
                povo.setOwhUnit(chkvo.getOwhUnit());
                povo.setOwhReqQty(chkvo.getOwhReqQty());

                povo.setRegDt(chkvo.getRegDt());
                povo.setRegId(chkvo.getRegId());
                povo.setRegIp(chkvo.getRegIp());
                povo.setModDt(DateUtils.getCurrentDateTime());
                povo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                povo.setModIp(paraMap.get("ipaddr").toString());

            }
            else{
                qty = Float.parseFloat(el.get("ord_qty").toString());
                povo.setOwhQty(qty);
                povo.setOwhNo(0L);
                povo.setOwhDt(DateUtils.getCurrentDateTime());
                povo.setOwhReqQty(Float.parseFloat(el.get("ord_qty").toString()));
                povo.setOwhUnit(Long.parseLong(el.get("sale_unit_no").toString()));

                povo.setRegDt(DateUtils.getCurrentDateTime());
                povo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                povo.setRegIp(paraMap.get("ipaddr").toString());
                povo.setModDt(DateUtils.getCurrentDateTime());
                povo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                povo.setModIp(paraMap.get("ipaddr").toString());

            }
            povo.setCustNo(custNo);
            povo = prodOwhRepo.save(povo);
//            this.resetOrdStatusByOwh(povo);
            Map<String, Object> rmap = new HashMap<>();
            rmap.put("custNo",custNo);
            rmap.put("ordNo", povo.getOrdNo());
            rmap.put("ordSts", Long.parseLong(env.getProperty("ord_status.complete")));
            mapper.resetOrdStatusByOwh(rmap);

        }
    }

    /*???????????? ??? ord_info - ord_sts??? ????????? ?????? ????????? ?????? ???????????? - 21.06.30 */
    private void resetOrdStatusByOwh(ProdOwh povo) {
        Map<String,Object> paraMap = new HashMap<String,Object>();
        Long custNo = povo.getCustNo();
        paraMap.put("prodNo", povo.getProdNo());
        paraMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.wait.owh")));

        List<Map<String,Object>> comDs = mapper.getOwhCompleteOrdList(paraMap); //????????????
        List<Map<String,Object>> owhDs = mapper.getTransOwhQty(paraMap); //???????????? ????????????

        int idx = -1;
        Map<String,Object> comDsMap = new HashMap<String,Object>();
        Map<String,Object> OwhDsMap = new HashMap<String,Object>();
        while (++idx < comDs.size()) {
            comDsMap = comDs.get(idx);
            if (!this.chkOwhCompleteCondition(comDsMap)) continue;

            OwhDsMap = owhDs.get(0);
            Float ordQty = Float.parseFloat(comDsMap.get("ord_qty").toString());
            Float owhQty = Float.parseFloat(OwhDsMap.get("owh_qty").toString());

            if (owhQty >= ordQty ) {
                comDsMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.complete")));
                comDsMap.put("ordNo", Long.parseLong(comDsMap.get("ord_no").toString()));
                comDsMap.put("cmpyNo", Integer.parseInt(comDsMap.get("cmpy_no").toString()));
                comDsMap.put("custNo", custNo);
                mapper.resetOrdStatusByOwh(comDsMap);
            }
        }
    }
    private boolean  chkOwhCompleteCondition(Map<String,Object> paraMap) {
        boolean lbResult = true;
        Long prodNo = 0L;
        prodNo = Long.parseLong(paraMap.get("prodNo").toString());


        List<Map<String,Object>> ds = mapper.getOrdProdList(paraMap); //paramap.get("ordNO")
        int count = mapper.getOrdProdListCount(paraMap);
        if(count != 1){
            for (Map<String, Object> el : ds) {
                el.put("ordSts", Long.parseLong(env.getProperty("ord_status.wait.owh")));
                el.put("ordNo", el.get("ordNo"));
                el.put("ordNm",el.get("ordNm"));
                el.put("prodNo",prodNo);
//                ProdNm = el.get("ordNm").toString();
//                ProdInfo chkvo = prRepo.findByProdNmAndUsedYn(ProdNm, "Y");
//                if(chkvo != null){
//                    prodNo = chkvo.getProdNo();
//                }
//                el.put("prodNo", prodNo);
//            el.put("ordNo", paraMap.get("ord_no"));
//            el.put("prodNo", paraMap.get("prod_no"));

                lbResult = mapper.getOwhCompleteOrdCondition(el);
                if (!lbResult) break;
            }
        }else{

            paraMap.put("prodNo",prodNo);

            lbResult = mapper.getOwhCompleteOneOrdCondition(paraMap);
        }

        return lbResult;
    }


    @Override
    public List<Map<String, Object>> getProdIwhList(Map<String, Object> paraMap) {
        return mapper.getProdIwhList(paraMap);
    }

    @Override
    public List<Map<String, Object>> OwhWhStkList(Map<String, Object> paraMap) {
        return mapper.OwhWhStkList(paraMap);
    }

    @Override
    public int getProdIwhListCount(Map<String, Object> paraMap) {
        return mapper.getProdIwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqProdIwhSumList(Map<String, Object> paraMap) {
        return mapper.getReqProdIwhSumList(paraMap);
    }

    @Override
    public int getReqProdIwhSumListCount(Map<String, Object> paraMap) {
        return mapper.getReqProdIwhSumListCount(paraMap);
    }

    /*????????????*/
    @Override
    //@Transactional
    public void saveProdIwh(Map<String, Object> paraMap) {
        String tag = "ioServiceImpl.saveProdIwh = ";
        log.info(tag+"paraMap = "+paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        /*Remarked by kmj At 21.02.01 start.......
        MakeIndcRslt mir = new MakeIndcRslt();
        Map<String,Object> passMap = (Map<String, Object>) paraMap.get("ProdInfo");
        log.info(tag+"passMap = " + passMap);
        try {
            mir.setIndcRsltNo(Long.parseLong(passMap.get("indc_rslt_no").toString()));
        } catch (NullPointerException ne) {
            mir.setIndcRsltNo(0L);
            mir.setRegDt(mir.getRegDt()); // ?????????
            mir.setRegId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
            mir.setRegIp(paraMap.get("ipaddr").toString()); // ????????? IP
        }
        mir.setIndcNo(Long.parseLong(passMap.get("indc_no").toString())); // ????????????

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            String date = passMap.get("make_dt").toString().substring(0,19);
            mir.setMakeDt(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try{
            mir.setMakeQty(Long.parseLong(passMap.get("make_qty").toString())); // ????????????
        } catch (NullPointerException ne) {
            mir.setMakeQty(0L); // ????????????
        }
        try{
            mir.setMetalQty(Long.parseLong(passMap.get("metal_qty").toString())); // ????????????
        } catch (NullPointerException ne) {
            mir.setMetalQty(0L); // ????????????
        }
        try{
            mir.setPackQty(Long.parseLong(passMap.get("pack_qty").toString())); // ????????????
        } catch (NullPointerException ne) {
            mir.setPackQty(0L); // ????????????
        }
        try{
            mir.setSznQty(Long.parseLong(passMap.get("szn_qty").toString())); // ????????????
        } catch (NullPointerException ne) {
            mir.setSznQty(0L); // ????????????
        }
        try{
            mir.setWgtQty(Long.parseLong(passMap.get("wgt_qty").toString())); // ????????????
        } catch (NullPointerException ne) {
            mir.setWgtQty(0L); // ????????????
        }

        mir.setModDt(DateUtils.getCurrentDateTime()); // ????????????
        mir.setModId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
        mir.setModIp(paraMap.get("ipaddr").toString()); // ????????? IP
        mir.setUsedYn("Y"); // ????????????

        MakeIndcRslt chkvo = makeIndcRsltRepo.findByIndcNoAndUsedYn(mir.getIndcNo(),mir.getUsedYn());
        if (chkvo != null) {
            mir.setIndcRsltNo(chkvo.getIndcRsltNo());
        }
        log.info("mir = "+mir);
        makeIndcRsltRepo.save(mir);

        Remarked by kmj At 21.02.01 start....... */
        List<Map<String,Object>> ds =  (List<Map<String,Object>> ) paraMap.get("iwhProds");
        for (Map<String,Object> el : ds) {
            log.info(tag + "1/3 ???????????? ?????? ....");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ProdIwh iwhvo = new ProdIwh();
            iwhvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
            iwhvo.setProdNo(Long.parseLong(el.get("prodNo").toString()));
            ProdInfo prvo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,iwhvo.getProdNo(),"Y");
            try {
                String date = paraMap.get("iwhDt").toString();
                iwhvo.setIwhDt(sdf.parse(date));
                Float iwhQty = Float.parseFloat(el.get("makeQty").toString()); //????????????(????????????)
                iwhvo.setIwhQty(iwhQty * prvo.getQtyPerPkg()); //????????????
                iwhvo.setIndcRsltNo(Long.parseLong(el.get("indcRsltNo").toString()));
                iwhvo.setUsedYn("Y"); // ????????????
                iwhvo.setRegDt(DateUtils.getCurrentDateTime()); // ?????????
                iwhvo.setRegId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
                iwhvo.setRegIp(paraMap.get("ipaddr").toString()); // ????????? IP
                iwhvo.setModDt(DateUtils.getCurrentDateTime()); // ?????????
                iwhvo.setModId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
                iwhvo.setModIp(paraMap.get("ipaddr").toString()); // ????????? IP
                iwhvo.setCustNo(custNo);
                iwhvo = prodIwhRepo.save(iwhvo);

                log.info(tag + "3/2 ???????????? reset ....");
                //JPA Transaction????????? ?????? ???????????? ?????? ?????? ?????????
                //this.renewalProdStk(stkvo);
                ProdStk psvo = new ProdStk();
                psvo.setProdNo(iwhvo.getProdNo());
                psvo.setUsedYn("Y");
                psvo.setWhNo(iwhvo.getWhNo());
                psvo.setModId(iwhvo.getModId());
                psvo.setModIp(iwhvo.getModIp());
                psvo.setModDt(iwhvo.getModDt());
                psvo.setStatTrfDt(DateUtils.getCurrentDateTime());
                psvo.setStkDt(DateUtils.getCurrentDateTime());
                ProdStk pschkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,psvo.getWhNo(),psvo.getProdNo(),"Y");
                if (pschkvo != null) {
                    psvo.setStkNo(pschkvo.getStkNo());
                    Float stkQty = pschkvo.getStkQty();
                    psvo.setStkQty(iwhvo.getIwhQty() + stkQty);
                    psvo.setRegDt(pschkvo.getRegDt()); // ?????????
                    psvo.setRegId(pschkvo.getRegId()); // ?????????
                    psvo.setRegIp(pschkvo.getRegIp()); // ????????? IP

                    psvo.setModDt(DateUtils.getCurrentDateTime()); // ?????????
                    psvo.setModId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
                    psvo.setModIp(paraMap.get("ipaddr").toString()); // ????????? IP
                }
                else {
                    psvo.setStkNo(0L);
                    psvo.setStkQty(iwhvo.getIwhQty());
                    psvo.setRegDt(DateUtils.getCurrentDateTime()); // ?????????
                    psvo.setRegId(Long.parseLong(paraMap.get("userId").toString())); // ?????????
                    psvo.setRegIp(paraMap.get("ipaddr").toString()); // ????????? IP
                }
                psvo.setCustNo(custNo);
                prodStkRepo.save(psvo);

//                //???????????? ????????? ????????? ?????? ???????????? ??????????????? 0??????.
//                Long ordNo = Long.parseLong(el.get("ord_no").toString());
//                if(ordNo == 0L) return;

                log.info(tag + "3/3 ??????????????? ??????????????? reset ....");
                Map<String, Object> rmap = new HashMap<>();
                rmap.put("ordNo", el.get("ordNo"));
                rmap.put("indcRsltNo", el.get("indcRsltNo"));
                rmap.put("ordSts", paraMap.get("ordSts"));
                rmap.put("custNo",custNo);
                mapper.resetOrdStatusByIwh(rmap);

                /*??????????????? ord_info ??? ord_sts??? ????????? ?????? ????????? ?????? ???????????? - 21.06.30*/
//                this.resetOrdStatusByIwh(rmap);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ???????????? ???????????? ??????
     *
     * @param paraMap
     * @return Map<String, Object>
     */
    @Override
    public void dropProdIwh(Map<String, Object> paraMap) {
        Long indcRsltNo = Long.parseLong(paraMap.get("indcRsltNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndcRslt mirvo = makeIndcRsltRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,indcRsltNo, "Y");
        if (mirvo != null) {
            mirvo.setUsedYn("N");
            mirvo.setModDt(DateUtils.getCurrentDateTime());
            mirvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setModIp(paraMap.get("ipaddr").toString());
            makeIndcRsltRepo.save(mirvo);
        }

        Long parIndcNo = 0L;
        Long indcNo = Long.parseLong(paraMap.get("indc_no").toString());
        Long indcSts = Long.parseLong(env.getProperty("code.base.makeEnd"));

        //?????????????????? indcNo??? parIndcNo ????????????.
        MakeIndc chkvo = makePepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo, "Y");
        if(chkvo != null){
            parIndcNo = chkvo.getParIndcNo();
        }

        //indcSts 2404(????????????) -> 2403(????????????) ?????? ??????
        MakeIndc mivo = makePepo.findByCustNoAndIndcNoAndIndcStsAndUsedYn(custNo,parIndcNo, indcSts, "Y");
        if (mivo != null) {
            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makePossible")));
            mirvo.setModDt(DateUtils.getCurrentDateTime());
            mirvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setModIp(paraMap.get("ipaddr").toString());
            mirvo.setCustNo(custNo);
            makePepo.save(mivo);
        }
    }
    /**
     * ?????????????????? ??????
     *
     * @param paraMap
     * @return Map<String, Object> (??????????????????)
     */
    @Override
    public Map<String, Object> getPursMatrInfo(Map<String, Object> paraMap) {
        String tag = "ioService.getPursMatrInfo =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursMatrInfo(paraMap);

    }

    @Override
    public List<Map<String, Object>> getIwhHsrt(Map<String, Object> paraMap) {
        return mapper.getIwhHsrt(paraMap);
    }

    @Override
    public int getIwhHsrtCount(Map<String, Object> paraMap) {
        return mapper.getIwhHsrtCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> comboEmbIwhWhInfo(Map<String, Object> paraMap) {
        return mapper.getComboEmbIwhWhInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwhHistList(Map<String, Object> paraMap) {
        String tag = "ioServiceImpl.getMatrOwhHistList = ";
        log.info(tag + "paraMap = " + paraMap);
        return mapper.getMatrOwhHistList(paraMap);
    }
    @Override
    public int getMatrOwhHistListCount(Map<String, Object> paraMap) {
        String tag = "ioServiceImpl.getMatrOwhHistListCount = ";
        log.info(tag + "paraMap = " + paraMap);
        return mapper.getMatrOwhHistListCount(paraMap);
    }
    /*????????????*/
    @Override
    //@Transactional
    public void saveProdIwhFromIndc(Map<String, Object> paraMap) {
        String tag = "ioServiceImpl.saveProdIwhFromIndc = ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        log.info("passMap??? ????????? paraMap" + paraMap);
        Map<String,Object> indcRsltMap = (Map<String, Object>) paraMap.get("indcRslt");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ProdIwh iwhvo = new ProdIwh();
        Map<String, Object> rmap = new HashMap<String,Object>();
        rmap.put("indcNo", indcRsltMap.get("indc_no"));
        Map<String, Object> dmap = mapper.getIndcRsltNo(rmap);

        iwhvo.setIndcRsltNo(Long.parseLong(dmap.get("indcRsltNo").toString()));
        iwhvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        iwhvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));

        iwhvo.setUsedYn("Y");
        iwhvo.setIwhDt(DateUtils.getCurrentDateTime());

        Float mess = 0f;
        ProdInfo pichkvo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,iwhvo.getProdNo(), iwhvo.getUsedYn());
        if(pichkvo != null){
            mess = pichkvo.getMess();
        }

        Float qty = 0F;
        ProdIwh chkvo = prodIwhRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,iwhvo.getIndcRsltNo(), iwhvo.getUsedYn());
        if (chkvo != null) {
            qty = Float.parseFloat(indcRsltMap.get("make_qty").toString());
            iwhvo.setIwhQty(qty / mess);


            iwhvo.setIwhQty(qty);
            iwhvo.setIwhNo(chkvo.getIwhNo());
            iwhvo.setRegDt(chkvo.getRegDt());
            iwhvo.setRegId(chkvo.getRegId());
            iwhvo.setRegIp(chkvo.getRegIp());

            iwhvo.setModDt(DateUtils.getCurrentDateTime());
            iwhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            iwhvo.setModIp(paraMap.get("ipaddr").toString());
        } else {
            iwhvo.setIwhNo(0L);
            iwhvo.setIwhQty(Float.parseFloat(indcRsltMap.get("make_qty").toString()));
            iwhvo.setRegDt(DateUtils.getCurrentDateTime());
            iwhvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            iwhvo.setRegIp(paraMap.get("ipaddr").toString());
            iwhvo.setModDt(DateUtils.getCurrentDateTime());
            iwhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            iwhvo.setModIp(paraMap.get("ipaddr").toString());
        }
        iwhvo.setCustNo(custNo);
        prodIwhRepo.save(iwhvo);

        log.info(tag + "3/2 ???????????? reset ....");
        ProdStk stkvo = new ProdStk();
        stkvo.setProdNo(iwhvo.getProdNo());
        stkvo.setUsedYn("Y");
        stkvo.setWhNo(iwhvo.getWhNo());
        stkvo.setStkQty(qty / mess);
        stkvo.setModId(iwhvo.getModId());
        stkvo.setModIp(iwhvo.getModIp());
        stkvo.setModDt(iwhvo.getModDt());
        stkvo.setCustNo(custNo);
        this.renewalProdStk(stkvo);

        log.info(tag + "3/3 ??????????????? ??????????????? reset ....");
        this.resetOrdStatusByIwh(stkvo);
    }



    /*???????????? ????????????*/
    private void renewalProdStk(ProdStk psvo) {
        psvo.setModDt(DateUtils.getCurrentDateTime());
        Long custNo = psvo.getCustNo();
        ProdStk pschkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,psvo.getWhNo(),psvo.getProdNo(),"Y");
        if (pschkvo != null) {
            psvo.setStkNo(pschkvo.getStkNo());
            psvo.setStkQty(pschkvo.getStkQty() + psvo.getStkQty());
            psvo.setRegDt(pschkvo.getModDt());
            psvo.setRegId(pschkvo.getModId());
            psvo.setRegIp(pschkvo.getModIp());
        }
        else {
            psvo.setStkNo(0L);
            psvo.setStkQty(psvo.getStkQty());
            psvo.setRegDt(psvo.getModDt());
            psvo.setRegId(psvo.getModId());
            psvo.setRegIp(psvo.getModIp());
        }
        psvo.setCustNo(custNo);
        prodStkRepo.save(psvo);
    }


    /*??????????????? ord_info??? ord_sts??? ????????? ?????? ????????? ?????? ?????? - 21.06.30*/
    private void resetOrdStatusByIwh(ProdStk psvo) {

        Map<String,Object> paraMap = new HashMap<String,Object>();
        Long custNo = psvo.getCustNo();
        paraMap.put("prodNo",psvo.getProdNo() );
        paraMap.put("ordSts", Long.parseLong(env.getProperty("ord_status_acpt"))); //141

        List<Map<String,Object>> ds = mapper.getOwhWaitOrdList(paraMap); //???????????? < ??????????????? ???
        List<Map<String,Object>> stkDs = mapper.getTransStkQty(paraMap); //??????????????????

        int idx = -1;
        Map<String,Object> dsMap = new HashMap<String,Object>();
        Map<String,Object> stkDsMap = new HashMap<String,Object>();
        while (++idx < ds.size()) {
            dsMap = ds.get(idx);
            if (!this.chkOwhCondition(dsMap)) continue; //( ?????????  >= ????????? ) ? true : false;

            stkDsMap = stkDs.get(0);
            Float ordQty = Float.parseFloat(dsMap.get("ord_qty").toString());
            Float stkQty = Float.parseFloat(stkDsMap.get("stk_qty").toString());
            if (stkQty >= ordQty ) {
                dsMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.wait.owh"))); //????????????
//                dsMap.put("prodNo", Long.parseLong(dsMap.get("prod_no").toString()));
                dsMap.put("ordNo", Long.parseLong(dsMap.get("ord_no").toString()));
                dsMap.put("cmpyNo", Integer.parseInt(dsMap.get("cmpy_no").toString()));
                dsMap.put("custNo",custNo);
                mapper.resetOrdStatusByIwh(dsMap);
                psvo.setStkQty(psvo.getStkQty() - Float.parseFloat(dsMap.get("ord_qty").toString()) );

            }
        }
    }

    private boolean  chkOwhCondition(Map<String,Object> paraMap) {
        boolean lbResult = true;
        paraMap.put("prodNo", paraMap.get("prod_no"));
        List<Map<String,Object>> ds = mapper.getOrdProdList(paraMap); //paramap.get("ordNO")
        for (Map<String, Object> el : ds) {
            el.put("ordSts", Long.parseLong(env.getProperty("ord_status_acpt"))); //141
            el.put("ordNo", el.get("ord_no"));
            el.put("prodNo",paraMap.get("prod_no"));
            lbResult = mapper.getOwhWaitOrdCondition(el); //( ?????????  >= ????????? ) ? true : false;
            if (!lbResult) break;
        }
        return lbResult;
    }



    @Override
    public Map<String, Object> getProdIwhInfo(Map<String, Object> paraMap) {
        return mapper.getProdIwhInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqProdOwhSumList(Map<String, Object> paraMap) {
        return mapper.getReqProdOwhSumList(paraMap);
    }

    @Override
    public int getReqProdOwhSumListCount(Map<String, Object> paraMap) {
        return mapper.getReqProdOwhSumListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMatrPosInfo(Map<String, Object> paraMap) {
        return mapper.getMatrPosInfo(paraMap);
    }

    @Override
    public void initTempMatrPos(Map<String, Object> paraMap) {
        mapper.initTempMatrPos(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqProdOwhList(Map<String, Object> paraMap) {
        String tag = "ioService.getReqProdOwhList => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getReqProdOwhList(paraMap);
    }

    @Override
    public int getReqProdOwhListCount(Map<String, Object> paraMap) {
        return mapper.getReqProdOwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcList(paraMap);
    }

    @Override
    public int getMakeIndcListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcListCount(paraMap);
    }

    @Override
    public void saveProdStkQty(Map<String, Object> paraMap) {
        mapper.saveProdStkQty(paraMap);
    }

    /*???????????? ????????????*/
    @Override
    public void saveKioMatrIwh(Map<String, Object> paraMap) {
        String tag = "IoServiceImpl.saveKioMatrIwh = ";
        log.info(tag + paraMap);

        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrIwh mi = new MatrIwh();
        MatrStk stk = new MatrStk();

        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("matrIwhList");

        for (Map<String, Object> el : ds) {
            Long stsCd = Long.parseLong(paraMap.get("purs_sts").toString());
            if (stsCd == 163) {
                mi.setRetnQty(Float.parseFloat(el.get("iwh_qty").toString()));
                mi.setIwhQty((float) 0);
                mi.setRetnResn(Long.parseLong(paraMap.get("retn_resn").toString()));
            }
            else if (stsCd == 164) {
                mi.setRetnQty((float) 0);
                mi.setIwhQty(Float.parseFloat(el.get("iwh_qty").toString()));
                mi.setRetnResn(0L);
            } else {
                return;
            }
            try {
                mi.setIwhNo(Long.parseLong(el.get("iwh_no").toString()));
            } catch (NullPointerException ne) {
                mi.setIwhNo(0L);
            }
//            try {
//                mi.setCmpyNo(Long.parseLong(el.get("cmpy_no").toString()));
//            } catch (NullPointerException ne) {
//                mi.setCmpyNo(0L);
//            }

            mi.setIwhDt((Date) paraMap.get("iwh_dt"));

            mi.setPursNo(Long.parseLong(el.get("purs_no").toString()));

            mi.setUsedYn("Y");
            mi.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
            mi.setPursMatrNo(Long.parseLong(el.get("purs_matr_no").toString()));
            mi.setMatrNo(Long.parseLong(el.get("matr_no").toString()));

            mi.setRegDt(DateUtils.getCurrentDate());
            mi.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mi.setRegIp(paraMap.get("ipaddr").toString());

            mi.setModDt(DateUtils.getCurrentDateTime());
            mi.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mi.setModIp(paraMap.get("ipaddr").toString());

            MatrIwh chkvo = imr.findByCustNoAndIwhNoAndMatrNoAndPursNoAndPursMatrNo(custNo,mi.getIwhNo(), mi.getMatrNo(), mi.getPursNo(), mi.getPursMatrNo());
            if (chkvo != null) {
                mi.setIwhNo(chkvo.getIwhNo());
            }
            mi.setCustNo(custNo);
            imr.save(mi);

            if(stsCd == 164) { // ??????????????? ?????????
                Map<String, Object> map = new HashMap<>();
                map.put("matrNo", Long.parseLong(el.get("matr_no").toString()));
                map.put("stkQty", Float.parseFloat(el.get("iwh_qty").toString()));
                MatrStk ms = new MatrStk();
                MatrStk chk = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,ms.getWhNo(), ms.getMatrNo(), ms.getUsedYn());
                if (chk != null) {
                    mapper.saveKioMatrStk(map);
                } else {
                    try {
                        ms.setMatrStkNo(chk.getMatrStkNo());
                    } catch (NullPointerException ne) {
                        ms.setMatrStkNo(0L);
                        ms.setRegDt(DateUtils.getCurrentDate());
                        ms.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                        ms.setRegIp(paraMap.get("ipaddr").toString());
                    }
                    ms.setModDt(DateUtils.getCurrentDateTime());
                    ms.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    ms.setModIp(paraMap.get("ipaddr").toString());

                    ms.setMatrNo(Long.parseLong(el.get("matr_no").toString()));

                    ms.setStatCd(Long.parseLong(paraMap.get("matrStkIn").toString()));
                    ms.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
                    ms.setUsedYn("Y");

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                        String date = paraMap.get("iwh_dt").toString();
                        ms.setStatTrfDt(sdf.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ms.setCustNo(custNo);
                    matrStkRepo.save(ms);
                }
            } else {
                return;
            }
        }
    }

    @Override
    public void saveKioMatrStk(Map<String, Object> paraMap) {
        mapper.saveKioMatrStk(paraMap);
    }

    @Override
    public List<Map<String, Object>> getKioMatrIwhList(Map<String, Object> paraMap) {
        return mapper.getKioMatrIwhList(paraMap);
    }

    @Override
    public int getKioMatrIwhListCount(Map<String, Object> paraMap) {
        return mapper.getKioMatrIwhListCount(paraMap);
    }


    /*????????????*/
    @Override
    public List<Map<String, Object>> getProdOutPlanList(Map<String, Object> paraMap) {
        return mapper.getProdOutPlanList(paraMap);
    }


    @Transactional
    @Override
    public void dropMatrIwh(Map<String, Object> paraMap) {
        String tag = "ioService.dropMatrIwh =>";
        paraMap.put("midId",paraMap.get("userId"));
        paraMap.put("modIp",paraMap.get("ipaddr"));
        log.info(tag + "org paraMap = " + paraMap.toString());
        Map<String,Object> imap = mapper.getMatrIwhInfo(paraMap);
        if (imap != null) {
            paraMap.put("iwhQty", Float.parseFloat(imap.get("iwhQty").toString()));
            paraMap.put("whNo", Long.parseLong(imap.get("whNo").toString()));
            paraMap.put("matrNo", Long.parseLong(imap.get("matrNo").toString()));
            log.info(tag + "addOn paraMap   = " + paraMap.toString());
            mapper.resetMatrStk(paraMap);
            mapper.dropMatrIwh(paraMap);
        }

    }

     @Override
    public String getCnfmAbleIwh(Map<String, Object> paraMap) {
        return mapper.getCnfmAbleIwh(paraMap);
    }

    @Transactional
    @Override
    public void cnfmIwh(Map<String, Object> paraMap) {
       paraMap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.end")));//????????????
        mapper.cnfmIwh(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkList(Map<String, Object> paraMap) {
        return mapper.getMatrStkList(paraMap);
    }

    @Override
    public int getMatrStkListCount(Map<String, Object> paraMap) {
        return mapper.getMatrStkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkIwhList(Map<String, Object> paraMap) {
        return mapper.getMatrStkIwhList(paraMap);
    }

    @Override
    public int getMatrStkIwhListCount(Map<String, Object> paraMap) {
        return mapper.getMatrStkIwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkOwhList(Map<String, Object> paraMap) {
        return mapper.getMatrStkOwhList(paraMap);
    }

    @Override
    public int getMatrStkOwhListCount(Map<String, Object> paraMap) {
        return mapper.getMatrStkOwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqOwhList(Map<String, Object> paraMap) {
        return mapper.getReqOwhList(paraMap);
    }
    @Override
    public int getReqOwhListCount(Map<String, Object> paraMap) {
        return mapper.getReqOwhListCount(paraMap);
    }

    @Override
    public Map<String, Object> matrOwhInfo(Map<String, Object> paraMap) {
        return mapper.matrOwhInfo(paraMap);
    }

    @Override
    public void saveMatrOwh(Map<String, Object> paraMap) {
        String tag = "IoServiceImpl.saveMatrOwh => ";
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrOwh omvo = null;
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("selectedOwh");

        for (Map<String, Object> el : ds) {

            omvo = new MatrOwh();

            omvo.setModDt(DateUtils.getCurrentDate());
            omvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            omvo.setModIp(paraMap.get("ipaddr").toString());
            omvo.setWhNo(Long.parseLong(el.get("whNo").toString()));
            try {
                omvo.setIndcNo(Long.parseLong(el.get("indcNo").toString()));
            } catch (NullPointerException ne) {
                omvo.setIndcNo(0L);
            }
            omvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            omvo.setOwhQty(Float.parseFloat(el.get("ordQty").toString()));
            try {
                omvo.setOwhReqQty(Float.parseFloat(el.get("ordQty").toString()));
            } catch (NullPointerException ne) {
                omvo.setOwhReqQty(omvo.getOwhQty());
            }
            try {
                omvo.setOwhUnit(Long.parseLong(paraMap.get("owhUnit").toString()));
            }
            catch (NullPointerException ne) {
                omvo.setOwhUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); //kg
            }

            try {
                omvo.setOwhReqDt(transFormat.parse(el.get("owhReqDt").toString().substring(0, 10)));
            } catch (NullPointerException | ParseException ne) {
                omvo.setOwhReqDt(DateUtils.getCurrentDateTime());
            }
            try {
                omvo.setOwhDt(transFormat.parse(paraMap.get("owhDt").toString().substring(0, 10)));
            } catch (NullPointerException | ParseException ne) {
                omvo.setOwhDt(DateUtils.getCurrentDateTime());
            }
            omvo.setUsedYn("Y");
            try {
                omvo.setOwhNo(Long.parseLong(el.get("owhNo").toString()));
            } catch (NullPointerException ne) {
                omvo.setOwhNo(0L);
            }
            MatrOwh chkvo = omr.findByCustNoAndOwhNoAndUsedYn(custNo,omvo.getOwhNo(), "Y");
            if (chkvo != null) {
                omvo.setOwhNo(Long.parseLong(el.get("owhNo").toString()));
                omvo.setRegDt(omvo.getRegDt());
                omvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                omvo.setRegIp(paraMap.get("ipaddr").toString());
            } else {
                omvo.setOwhNo(0L);
                omvo.setRegDt(omvo.getRegDt());
                omvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                omvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            log.info(tag + "1.1 ?????????????????? ?????? ???.....");
            omvo.setCustNo(custNo);
            omvo = omr.save(omvo);
            log.info(tag + "1.2 ?????????????????? ?????? ???.....");

            log.info(tag + "2.1 ????????? ?????? ????????? ??????");
            MatrStk msvo = new MatrStk();
            MatrStk chkstkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,omvo.getWhNo(), omvo.getMatrNo(), "Y");
            if (chkstkvo != null) {
                Float qty = chkstkvo.getStkQty() - omvo.getOwhQty();
                msvo.setStkQty(qty);
                msvo.setMatrStkNo(chkstkvo.getMatrStkNo());
                msvo.setMatrNo(chkstkvo.getMatrNo());
                msvo.setWhNo(chkstkvo.getWhNo());

                msvo.setValidDt(chkstkvo.getValidDt());

                msvo.setStatTrfDt(DateUtils.getCurrentDate());
                msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_owh_stat")));

                msvo.setRegDt(chkstkvo.getRegDt());
                msvo.setRegIp(chkstkvo.getRegIp());
                msvo.setRegId(chkstkvo.getRegId());
                msvo.setUsedYn(chkstkvo.getUsedYn());

                msvo.setModDt(DateUtils.getCurrentDate());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setModIp(paraMap.get("ipaddr").toString());

                msvo.setCustNo(custNo);
                matrStkRepo.save(msvo);

                log.info("2.2 ??????????????? ???????????? ?????? ?????? ?????? ??????");
                Map<String, Object> matrMap = new HashMap<>();
                matrMap.put("matrNo", omvo.getMatrNo());
                matrMap.put("indcNo", omvo.getIndcNo());
                matrMap.put("custNo",custNo);
                makeIndcMapper.updateMatrSts(matrMap);
            }
            /*21.07.07 - ????????? ????????? ?????? ???????????? ?????? ?????? */
//        log.info(tag + "2.2.????????? ?????? ????????? ??????");
//        paraMap.put("matr_no", omvo.getMatrNo());
//        paraMap.put("wh_no", omvo.getWhNo());
//        Map<String,Object> stkmap = stockService.getMatrStockByIo(paraMap);
//        chkstkvo.setStkQty(Float.parseFloat(stkmap.get("stk_qty").toString()));
//        matrStkRepo.save(chkstkvo);
        }

        log.info(tag + "3.1 ???????????? ????????? ??????");
        Map<String, Object> rmap = new HashMap<>();
        rmap.put("indcSts", Long.parseLong(env.getProperty("code.base.makePossible")));
        rmap.put("indcNo", Long.parseLong(omvo.getIndcNo().toString()));
        rmap.put("ioCheck", "owh");
        rmap.put("custNo", custNo);
        indcService.resetIndcSts(rmap);

    }

    //??????????????? matr_iwh??? ??????????????? ???????????? ??????.
    public void updateMatrIwhByOwh(Map<String, Object> paraMap){
        Long matrNo = Long.parseLong(paraMap.get("matrNo").toString());
        Long whNo = Long.parseLong(paraMap.get("whNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Float owhQty = Float.parseFloat(paraMap.get("ordQty").toString());
        Float calcQty = 0F;
        Float remainQty = 0F;
        Float qty = 0F;

        Map<String, Object> rmap = new HashMap<>();
        rmap.put("matrNo", matrNo);
        rmap.put("whNo",whNo);
        List<Map<String,Object>> iwhList = mapper.getMatrIwhListByOwh(rmap);

        for(Map<String, Object> el : iwhList){
            MatrIwh chkvo = matrIwhRepo.findByCustNoAndIwhNoAndUsedYn(custNo,Long.parseLong(el.get("iwhNo").toString()), "Y");
            if(chkvo != null){
                if(chkvo.getIwhQty() <= owhQty ){
                    chkvo.setOwhQty(chkvo.getIwhQty());
                    chkvo.setRemainQty(0F);

                    calcQty = owhQty - chkvo.getIwhQty();
                }
                else{
                    log.info("calcQty : "+calcQty);
                    if(calcQty == 0){
                        qty = chkvo.getOwhQty() + owhQty;
                        chkvo.setOwhQty(qty);
                        remainQty = chkvo.getIwhQty() - qty;
                        chkvo.setRemainQty(remainQty);
                        log.info("2-1. ????????? ???????????? ??????????????? ?????? ??? ?????? ??? ??? owhQty : " + chkvo.getOwhQty());
                        log.info("2-1. ????????? ???????????? ??????????????? ?????? ??? ?????? ??? ??? remainQty : " + chkvo.getRemainQty());
                    }else{
                        qty = chkvo.getOwhQty() + calcQty;
                        chkvo.setOwhQty(qty);
                        remainQty = chkvo.getIwhQty() - qty;
                        chkvo.setRemainQty(remainQty);
                        log.info("2-2. ?????? ??????????????? 0??? ??? owhQty : " + chkvo.getOwhQty());
                        log.info("2-2. ?????? ??????????????? 0??? ??? owhList remainQty : " + chkvo.getRemainQty());
                    }
                }
                chkvo.setCustNo(custNo);
                chkvo = matrIwhRepo.save(chkvo);
            }

            if(chkvo.getRemainQty() > 0) break;
        }
    }

    @Override
    public List<Map<String, Object>> getComboOwhReqDtList(Map<String, Object> paraMap) {
        return mapper.getComboOwhReqDtList(paraMap);
    }

    @Transactional
    @Override
    public void dropMatrOwh(Map<String, Object> paraMap) {
        paraMap.put("modId", paraMap.get("userId"));
        paraMap.put("ipaddr", paraMap.get("ipaddr"));
        paraMap.put("owhNo", paraMap.get("owhNo"));
        log.info(paraMap);
        mapper.dropMatrOwh(paraMap);
    }
    /*?????????????????? ?????????:??????*/
    @Override
    public List<Map<String, Object>> getReqMatrIwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.IoService.getReqMatrIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getReqMatrIwhList(paraMap);
    }

    @Override
    public int getReqMatrIwhListCount(Map<String, Object> paraMap) {
        return mapper.getReqMatrIwhListCount(paraMap);
    }
    /*?????????????????? ?????????:??????*/
    @Override
    public List<Map<String, Object>> getReqMatrIwhSumList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrIwhSumList => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrIwhSumList(paraMap);
    }

    @Override
    public int getReqMatrIwhSumListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrIwhSumListCount => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrIwhSumListCount(paraMap);
    }


    /*?????????????????? ?????????:??????*/
    @Override
    public List<Map<String, Object>> getReqMatrOwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrOwhList => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrOwhList(paraMap);
    }

    @Override
    public int getReqMatrOwhListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrOwhListCount => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrOwhListCount(paraMap);
        }

    @Override
    public List<Map<String, Object>> getReqMatrOwhSumList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrOwhSumList => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrOwhSumList(paraMap);
    }


    /*??????????????? ?????????:??????*/
    @Override
    public int getReqMatrOwhSumListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getReqMatrOwhSumList => ";
        log.info(tag + "params = " + paraMap.toString());//kill
        return mapper.getReqMatrOwhSumListCount(paraMap);
    }
    /*??????????????????*/
    @Override
    public List<Map<String, Object>> getExportProdList(Map<String, Object> paraMap) {
        return mapper.getExportProdList(paraMap);
    }

    @Override
    public Map<String, Object> getExportExecInfo(Map<String, Object> paraMap) {
        String tag = "ioService.getExportExecInfo => ";
        Map<String,Object> rmap = mapper.getExportExecInfo(paraMap);
//        String fileRoot = paraMap.get("fileRoot").toString();
//        if (rmap != null) {
//            StringBuffer buf = new StringBuffer();
//            buf.append(fileRoot).append("ord/").append(rmap.get("ordNo")).append(".png");
//            rmap.put("bar_code_url", buf.toString());
//            log.info(tag + "bar_code_url = " + rmap.get("barCodeUrl"));
//        }
        return rmap;
    }

    /*????????????*/
    @Override
    public void saveExportProdInfo(Map<String, Object> paraMap) {
        ProdOwh owhvo = new ProdOwh();
        Float qty = 0F;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            try{
                owhvo.setOwhDt(sdf.parse(paraMap.get("owhDt").toString()));
            }catch(NullPointerException ne){
                owhvo.setOwhDt(DateUtils.getCurrentDateTime());
            }
            Long ordNo = 0L;
            try{
                ordNo = Long.parseLong(paraMap.get("ordNo").toString());
            }catch(NullPointerException ne){

            }

            owhvo.setOrdNo(ordNo);
            Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
            owhvo.setProdNo(prodNo);
            owhvo.setOwhUnit(Long.parseLong(paraMap.get("ordUnit").toString()));
            owhvo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
            owhvo.setOwhReqQty(Float.parseFloat(paraMap.get("owhReqQty").toString()));
            Date OwhReqDt = sdf.parse(paraMap.get("owhReqDt").toString().substring(0,10));

//            OrdInfo chkoivo = ordRepository.findByOrdNoAndUsedYn(ordNo, "Y");
//            if(chkoivo != null){
//                if(chkoivo.getCmpyNo() == 0){
//                    chkoivo.setCmpyNo(Long.parseLong(paraMap.get("cmpy_no").toString()));
//                    chkoivo.setDlvReqDt(OwhReqDt);
//                    chkoivo.setOrdDt(sdf.parse(paraMap.get("ord_dt").toString().substring(0,10)));
//                }
//                ordRepository.save(chkoivo);
//            }
//
            if (paraMap.get("cmpyTypeNull").equals("N")){
                mapper.ordStkCmpyNoUpdate(paraMap);
            }

//            List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("selectedList");


            // ????????? ??? ?????? ?????? ??????
//            for(Map<String, Object> el : ds){
//                log.info(el+"QWEQQWQEWQE");
//
//                owhvo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
////                owhvo.setOwhQty(Float.parseFloat(el.get("this_owh_qty").toString()));
//                owhvo.setOwhReqDt(OwhReqDt);
//
//                owhvo.setUsedYn("Y");
//                ProdOwh chkvo = prodOwhRepo.findByOrdNoAndWhNoAndProdNoAndUsedYn(owhvo.getOrdNo(), owhvo.getWhNo() ,owhvo.getProdNo(), "Y");
//
//
//                if (chkvo != null) {
//                    qty = chkvo.getOwhQty();
//                    qty += Integer.parseInt(el.get("this_owh_qty").toString());
//                    owhvo.setOwhQty(qty);
//                    owhvo.setOwhNo(chkvo.getOwhNo());
//                    owhvo.setRegDt(chkvo.getRegDt());
//                    owhvo.setRegId(chkvo.getRegId());
//                    owhvo.setRegIp(chkvo.getRegIp());
//                }
//                else {
//                    qty = Float.parseFloat(el.get("this_owh_qty").toString());
//                    owhvo.setOwhQty(qty);
//                    owhvo.setOwhNo(0L);
//                    owhvo.setRegDt(DateUtils.getCurrentDate());
//                    owhvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
//                    owhvo.setRegIp(paraMap.get("ipaddr").toString());
//                }
//
//
//                prodOwhRepo.save(owhvo);
//
//                mapper.renewalProdStockByIOwh(el); // ?????? Stk ??????
//
//                //???????????? ???????????? (??????????????? ????????? ?????? ??????????????? ??????)
//                Map<String,Object> ordMap = new HashMap<String,Object>();
//                log.info(qty +"qeqwqeqew"+ (Integer) el.get("ord_qty"));
//                if (qty >= (Integer) el.get("ord_qty")) {
//                    ordMap.put("ordNo", owhvo.getOrdNo());
//                    ordMap.put("cmpyNo", owhvo.getCmpyNo());
//                    ordMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.complete")));
//                    mapper.resetOrdStatusByOwh(ordMap);
//                }
//            }

                owhvo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
//              owhvo.setOwhQty(Float.parseFloat(el.get("this_owh_qty").toString()));
                owhvo.setOwhReqDt(OwhReqDt);

                owhvo.setUsedYn("Y");
//                ProdOwh chkvo = prodOwhRepo.findByOrdNoAndWhNoAndProdNoAndUsedYn(owhvo.getOrdNo(), owhvo.getWhNo() ,owhvo.getProdNo(), "Y");
//                if (chkvo != null) {
//                    qty = chkvo.getOwhQty();
//                    qty += Integer.parseInt(paraMap.get("owh_qty").toString());
//                    owhvo.setOwhQty(qty);
//                    owhvo.setOwhNo(chkvo.getOwhNo());
//                    owhvo.setRegDt(chkvo.getRegDt());
//                    owhvo.setRegId(chkvo.getRegId());
//                    owhvo.setRegIp(chkvo.getRegIp());
//                }
//                else {
                    ProdInfo vo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
                    log.info(paraMap+"paraMqppkgOwhQtypkgOwhQtypkgOwhQtypkgOwhQty");
                    try{
                        qty = Float.parseFloat(paraMap.get("owhQty").toString());
                    }catch (NullPointerException en){
                        qty = 0F;
                    }
                     Float pkgQty = 0F;
                    try{
                        pkgQty = Float.parseFloat(paraMap.get("pkgOwhQty").toString());
                    }catch (NullPointerException en){
                        pkgQty = 0F;
                    }

                    if (qty == 0) {
                        owhvo.setOwhQty(pkgQty * vo.getQtyPerPkg());
                    }
                    else if (pkgQty == 0) {
                        owhvo.setOwhQty(qty);
                    }
                    else {
                        Float perPkg = pkgQty * vo.getQtyPerPkg();
                        owhvo.setOwhQty(perPkg+qty);
                    }
                    owhvo.setOwhNo(0L);
                    owhvo.setRegDt(DateUtils.getCurrentDateTime());
                    owhvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    owhvo.setRegIp(paraMap.get("ipaddr").toString());
                    owhvo.setModDt(DateUtils.getCurrentDateTime());
                    owhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    owhvo.setModIp(paraMap.get("ipaddr").toString());
                owhvo.setCustNo(custNo);
                prodOwhRepo.save(owhvo);

                Map<String, Object> map = new HashMap<String,Object>();
                map.put("prodNo",prodNo);
                map.put("whNo",owhvo.getWhNo());
                map.put("owhQty",owhvo.getOwhQty());
            // }
            //SOL AddOn by KMJ At 21.08.16
            OrdInfo ordvo = ordRepository.findByCustNoAndOrdNoAndUsedYn(custNo,ordNo,"Y");
            if (paraMap.get("cmpyTypeNull").equals("N")){
                ordvo.setCmpyNo(owhvo.getCmpyNo());
            }else{
                ordvo.setCmpyNo(0L);
            }

            if (paraMap.get("owhChk").equals(true)) {
                ordvo.setOrdSts(Long.parseLong(paraMap.get("ordSts").toString()));
            }
            ordvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            ordvo.setModDt(DateUtils.getCurrentBaseDateTime());
            ordvo.setModIp(paraMap.get("ipaddr").toString());
            ordvo.setCustNo(custNo);
            ordRepository.save(ordvo);
            //EOL AddOn by KMJ At 21.08.16

            mapper.renewalProdStockByIOwh(map); // ?????? Stk ??????

            //???????????? ????????? ?????? ??????, ord_no ??? 0???.
            if(ordNo == 0L) return;
            List<Map<String, Object>> rmap = mapper.getProdOwhCount(owhvo);

            List<Map<String, Object>> ordProdList = mapper.getOrdProdOwhNo(owhvo);

            for (Map<String, Object> el : ordProdList){
                log.info(el.get("ord_prod_no")+"elupdateOrdProdUsedYn");
                mapper.updateOrdProdUsedYn(el);
            }



            /*SOL Remarked by KM At 21.08.16 ??????????????????(owhChk)???  ?????????
            //???????????? ???????????? (??????????????? ????????? ?????? ??????????????? ??????)
            Map<String,Object> ordMap = new HashMap<String,Object>();
            qty = Float.parseFloat(String.valueOf(rmap.get(0).get("owhQty")));
            if (paraMap.get("owhChk").equals(true)) {
                ordMap.put("ordNo", owhvo.getOrdNo());
                ordMap.put("cmpyNo", owhvo.getCmpyNo());
                ordMap.put("ordSts", Long.parseLong(env.getProperty("ord_status.complete")));
                mapper.resetOrdStatusByOwh(ordMap);
            }
            EOL Remarked by KM At 21.08.16 ?????????????????????  ?????????*/
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> getForImportProdList(Map<String, Object> paraMap) {
        return mapper.getForImportProdList(paraMap);
    }

    @Override
    public int getForImportProdListCount(Map<String, Object> paraMap) {
        return mapper.getForImportProdListCount(paraMap);
    }



    @Override
    @Transactional
    public void saveMatrStk(Map<String, Object> paraMap) {
        Long iwhNo = 0L;
                //paraMap : {
        //            pursMatrNo=44684
        //          , mess=null
        //          , restQty=29
        //          , pursNo=44683
        //          , matrNo=697969
        //          , cmpyNo=43248
        //          , iwhQty=29
        //          , whNo=560
        //          , pursQty=29
        //          , iwhDt=2021-07-29T10:04:40.575Z
        //          , dateManufacture=2021-07-29T10:02:38.518Z
        //          , retnResn=null
        //          , validDt=2022-07-29T00:00:00.000Z
        //          , userId=5
        //          , pursSts=162
        //          }
        String tag = "vsvc.IoService.saveMatrStk => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrIwh mivo = new MatrIwh();
        //SOL AddOn By KMJ AT 21.10.11
        try {
            iwhNo = Long.parseLong(paraMap.get("iwhNo").toString());
        }
        catch (NullPointerException ne) {
            iwhNo = 0L;
        }
        //EOL AddOn By KMJ AT 21.10.11

        Long pursNo = Long.parseLong( paraMap.get("pursNo").toString());
        mivo.setPursNo (pursNo);
        mivo.setPursMatrNo (Long.parseLong( paraMap.get("pursMatrNo").toString()));
        mivo.setMatrNo (Long.parseLong( paraMap.get("matrNo").toString()));
        mivo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));
        mivo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
        Long indcNo = 0L;

        PursInfo puvo = pursRepo.findByCustNoAndPursNoAndUsedYn(custNo,pursNo,"Y");
        if (puvo != null){
            indcNo = puvo.getIndcNo();
        }

        MakeIndc mavo = makePepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo,"Y");
        if(mavo != null){
            mavo.setIndcSts(2402L);
            makePepo.save(mavo);
        }

        try {
            mivo.setIwhDt(sdf.parse(paraMap.get("iwhDt").toString().substring(0,16)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            mivo.setValidDt(sdf.parse(paraMap.get("validDt").toString().substring(0,10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mivo.setUsedYn("Y");

        try{
            mivo.setRetnQty(Float.parseFloat(paraMap.get("retnQty").toString()));
        }catch(NullPointerException ne){
            mivo.setRetnQty(0F);
        }

        try{
            mivo.setRetnResn(Long.parseLong(paraMap.get("retnResn").toString()));
        }catch(NullPointerException ne){

        }
        try{
            mivo.setIwhQty(Float.parseFloat(paraMap.get("iwhQty").toString()));
        }catch(NullPointerException ne){
            mivo.setIwhQty(0F);
        }

        try{
            mivo.setDateManufacture(sdf.parse(paraMap.get("dateManufacture").toString()));
        }catch(ParseException e){
            e.printStackTrace();
        }

        mivo.setModDt(DateUtils.getCurrentDateTime());
        mivo.setModId(Long.valueOf(paraMap.get("userId").toString()));
        mivo.setModIp(paraMap.get("ipaddr").toString());
        log.info(tag + "1.???????????? ?????????");
        //mapper.saveMatrIwh(StringUtil.voToMap(mivo));
        //mapper.saveMatrIwh(StringUtil.voToMap(mivo));
        //MatrIwh chkiwh = imr.findByPursNoAndMatrNoAndWhNoAndUsedYn(mivo.getPursNo(),mivo.getMatrNo() , mivo.getWhNo() ,"Y"); //Remarked By KMJ AT 21.10.11
        MatrIwh chkiwh = imr.findByCustNoAndIwhNoAndUsedYn(custNo,mivo.getIwhNo(),"Y"); //AddOn By KMJ AT 21.10.11
        //Float qty = 0F; //Remarked By KMJ At 21.10.11
        if (chkiwh != null) {
            //qty = chkiwh.getIwhQty();
            //qty += mivo.getIwhQty(); //Remarked By KMJ AT 21.10.11
            mivo.setIwhNo(chkiwh.getIwhNo());
            mivo.setIwhQty(mivo.getIwhQty());
            mivo.setIwhNo(chkiwh.getIwhNo());
            mivo.setRegDt(chkiwh.getRegDt());
            mivo.setRegId(chkiwh.getRegId());
            mivo.setRegDt(chkiwh.getRegDt());
        }
        else {
            mivo.setIwhNo(0L);
            //mivo.setRemainQty(0F); Remaked By KMJ AT 21.10.11 --???????????? ?????? ????????? ???..
            mivo.setRegDt(DateUtils.getCurrentDateTime());
            mivo.setRegId(Long.valueOf(paraMap.get("userId").toString()));
            mivo.setRegIp(paraMap.get("ipaddr").toString());
        }
        mivo.setCustNo(custNo);
        mivo = imr.save(mivo); //?????????????????? ????????? ??????????????? ?????? ??????????????? ???????????? ??????.
        log.info(tag + "2.???????????? ?????????");

        if (env.getProperty("matr_pos_yn").equals("Y")) { //?????????????????? ???????????? ?????? : ES ????????????????????? 'Y'??? ?????????
            log.info("3.??? ???????????? ???????????? ??????");
            int mess = Integer.parseInt(paraMap.get("mess").toString()); //????????????
            float restWgt = Long.parseLong(paraMap.get("restQty").toString()); //???????????????
            float iwhWgt = 0f;
            for (int idx = 0; idx < Float.parseFloat( paraMap.get("iwhQty").toString()); idx++) {
                MatrPos mpvo = new MatrPos();
                mpvo.setMatrNo(mivo.getMatrNo());
                mpvo.setWhNo(mivo.getWhNo());
                mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat"))); //??????
                mpvo.setIwhSeq(idx);
                mpvo.setStairIdx(0); //?????????
                mpvo.setRowIdx(0); //?????????
                mpvo.setColIdx(0);//?????????
                mpvo.setUsedYn("Y");
                if (restWgt <= mess) {
                    iwhWgt = restWgt;
                } else {
                    iwhWgt = ((restWgt % mess) != 0) ? mess : (restWgt / mess);
                    restWgt -= iwhWgt;
                }
                mpvo.setMatrQty(iwhWgt);

                mpvo.setMatrPosNo(0L); //???????????? ????????? ??????????????? ????????? ?????? ?????? ???.
                mpvo.setCustNo(custNo);
                makeposRepo.save(mpvo);
            }
        }
        //while(mivo.getIwhNo() == null); //???????????????????????? ?????? Remarked By KMJ AT 21.10.12

        /* SOL Remarked By KMJ AT 21.10.11 --???????????????????????? : ???????????? ???????????? ?????? ??? ?????? (??????????????? ???????????? ???????????? ????????? ???????????? ???)
        //log.info("MatrIwh??? PursMatr??? ?????? ??? ??????");

        if (Float.parseFloat(paraMap.get("pursQty").toString()) <= mivo.getIwhQty()){
            log.info("4.????????? ????????? ??????????????? ?????? ????????? ?????? ???????????? ????????? ????????? ??????");
            paraMap.put("pursNo",mivo.getPursNo());
            paraMap.put("pursSts",Long.valueOf(env.getProperty("purs.sts.ing"))); //165
            pursService.resetPursStatusEnd(paraMap);

            log.info("5.????????? ????????? ??????????????? ?????? ????????? ?????? ???????????? ????????? ??????????????? ??????");
            paraMap.put("pursNo",mivo.getPursNo());
            paraMap.put("pursSts",Long.valueOf(env.getProperty("purs.sts.end"))); //164
            pursService.resetPursStatusIng(paraMap);
        }
        log.info("PursMatr??? ??????????????? MatrIwh??? ???????????? ??????");
        List<Map<String, Object>> pursMatrList = mapper.getPursMatrList(paraMap);
        for(Map<String, Object> el : pursMatrList) {
            Map<String, Object> rmap = new HashMap<>();
            rmap.put("matrNo", el.get("matr_no"));
            rmap.put("pursNo", pursNo);
            rmap.put("pursQty", el.get("purs_qty"));

            Map<String,Object> chkMap = mapper.chkMatrListByIwh(rmap); //Remarked By KMJ At 21.10.11 --??????????????? ???????????? ??????.
            Boolean chk = (Boolean) chkMap.get("chk");
            log.info("chk => " + chk);
            if(chk == false){
                rmap.put("pursSts", Long.parseLong(env.getProperty("purs.sts.end")));
                mapper.resetPursStsIng(rmap);
                break;
            }
        }
        EOL Remarked By KMJ AT 21.10.11 --???????????????????????? : ???????????? ???????????? ?????? ??? ?????? (??????????????? ???????????? ???????????? ????????? ???????????? ???)*/

        mapper.resetPursStsIng(paraMap);  //AddOn By KMJ AT 21.10.12 --?????? ?????? ??????????????? ???????????? ??????????????? ?????????(????????????= ????????????????????? ????????? ??????????????? ???????????? ??????: ???????????? ????????????)
        mapper.resetPursStsSet(paraMap);


        if (env.getProperty("matr_pos_yn").equals("N")) { //?????????????????? ???????????? ?????? ?????? : ES ????????????????????? 'Y'??? ?????????
            log.info(tag + "5.????????? ?????? ????????? ??????");
            paraMap.put("stkQty", mivo.getIwhQty());
            paraMap.put("matrNo", mivo.getMatrNo());
            paraMap.put("whNo", mivo.getWhNo());
            stockService.addOnStock(paraMap);
        }
    }

    @Override
    public Map<String, Object> getReqMatrIwhInfo(Map<String, Object> paraMap) {
        return mapper.getReqMatrIwhInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap) {
        return mapper.getComboPursCmpy(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkPosList(Map<String, Object> paraMap) {
        return mapper.getMatrStkPosList(paraMap);
    }

    @Override
    public int getMatrStkPosListCount(Map<String, Object> paraMap) {
        return mapper.getMatrStkPosListCount(paraMap);
    }

    @Override
    @Transactional
    public void saveMatrStkPosList(Map<String, Object> paraMap) {

        String tag = "IoController.saveMatrStkPosList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrPos mpvo = null;
        List<Map<String,Object>> ds = (List<Map<String, Object>>) paraMap.get("matrStkPosList");
        int idx = 0;

        for (Map<String,Object> el : ds) {
            mpvo = new MatrPos();
            mpvo.setMatrPosNo(Long.parseLong(el.get("matrPosNo").toString())); //
            mpvo.setWhNo(Long.parseLong(el.get("whNo").toString())); //
            mpvo.setMatrNo(Long.parseLong(el.get("matrNo").toString())); //
            mpvo.setMatrQty(Float.parseFloat(el.get("stkQty").toString()));
            mpvo.setMatrStat(Long.parseLong(el.get("matrStat").toString()));
//            mpvo.setPursMatrNo(Long.parseLong(el.get("purs_matr_no").toString())); //
            mpvo.setIwhSeq(Integer.parseInt(el.get("iwhSeq").toString())); //

            mpvo.setStairIdx(Integer.parseInt(el.get("stairIdx").toString())); //???
            mpvo.setRowIdx(Integer.parseInt(el.get("rowIdx").toString())); //???
            mpvo.setColIdx(Integer.parseInt(el.get("colIdx").toString())); //???

//            mpvo.setProcSeq(++idx);

            mpvo.setRegDt(DateUtils.getCurrentDateTime());
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setRegId(Long.valueOf(paraMap.get("userId").toString()));
            mpvo.setUsedYn("Y");

            mpvo.setModDt(DateUtils.getCurrentDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setUsedYn("Y");

            MatrPos chkvo = makeposRepo.findByCustNoAndMatrPosNoAndStairIdxAndRowIdxAndColIdxAndUsedYn(custNo,mpvo.getMatrPosNo(), mpvo.getStairIdx(), mpvo.getRowIdx(), mpvo.getColIdx(), mpvo.getUsedYn());
            if (chkvo != null) {
                mpvo.setMatrPosNo(chkvo.getMatrPosNo());
            }
            else{
                mpvo.setRegDt(DateUtils.getCurrentDate());
                mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            paraMap.put("pursSts",Long.valueOf(env.getProperty("purs.sts.end"))); //Remarked by KMJ AT 21.10.26 -?????? ?????? ???????????? ????????? ?????? ??????.
            mpvo.setCustNo(custNo);
            makeposRepo.save(mpvo);

        }
    }



    @Override
    public List<Map<String, Object>> getMatchPosList(Map<String, Object> paraMap) {
        return mapper.getMatchPosList(paraMap);
    }

    @Override
    public int getMatchPosListCount(Map<String, Object> paraMap) {
        return mapper.getMatchPosListCount(paraMap);
    }

    @Override
    public void renewalMatrPos(Map<String, Object> paraMap) {
        String tag = "IoController.renewalMatrPos => ";
        MatrPos mpvo = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("matrPosList");

        Float qty = 0F;

        for(Map<String, Object> el : ds){
            el.put("ipaddr", paraMap.get("ipaddr"));
            el.put("userId", paraMap.get("userId"));
            mpvo = new MatrPos();
            mpvo.setMatrNo(Long.parseLong(el.get("matr_no").toString()));
            mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
            mpvo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
            try{
                mpvo.setStairIdx(Integer.parseInt(el.get("stair_idx").toString()));
                mpvo.setColIdx(Integer.parseInt(el.get("col_idx").toString()));
                mpvo.setRowIdx(Integer.parseInt(el.get("row_idx").toString()));
                mpvo.setIwhSeq(Integer.parseInt(el.get("iwh_seq").toString()));
            }catch(NullPointerException ne){
                mpvo.setStairIdx(1);
                mpvo.setColIdx(1);
                mpvo.setRowIdx(1);
                mpvo.setIwhSeq(0);
            }

            mpvo.setMatrQty(Float.parseFloat(el.get("iwh_qty").toString()));
            mpvo.setUsedYn("Y");

            MatrPos chkvo = makeposRepo.findByCustNoAndMatrNoAndIwhSeqAndMatrStatAndStairIdxAndRowIdxAndColIdxAndUsedYn(custNo,mpvo.getMatrNo(), mpvo.getIwhSeq(), mpvo.getMatrStat(), mpvo.getStairIdx(), mpvo.getRowIdx(), mpvo.getColIdx(), mpvo.getUsedYn());
            if(chkvo != null){
                qty = chkvo.getMatrQty();
                qty += mpvo.getMatrQty();
                mpvo.setMatrQty(qty);
                mpvo.setMatrPosNo(chkvo.getMatrPosNo());

//                MatrPos chkmp = makeposRepo.findByMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndIwhSeq(mpvo.getMatrNo(), mpvo.getWhNo(), mpvo.getStairIdx(), mpvo.getColIdx(), mpvo.getRowIdx(), mpvo.getIwhSeq());
//                if(chkmp != null){
//                    qty = Float.parseFloat(el.get("iwh_qty").toString());
//                    mpvo.setMatrQty(qty);
//                }
//                else{ //????????? ??????
//                    qty = Float.parseFloat(el.get("iwh_qty").toString());
//                    mpvo.setMatrQty(qty);
//                }
                mpvo.setModDt(DateUtils.getCurrentDateTime());
                mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setModIp(paraMap.get("ipaddr").toString());

                mpvo.setRegDt(chkvo.getRegDt());
                mpvo.setRegId(chkvo.getRegId());
                mpvo.setRegIp(chkvo.getRegIp());
            }
            else{
                mpvo.setModDt(DateUtils.getCurrentDateTime());
                mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setModIp(paraMap.get("ipaddr").toString());

                mpvo.setRegDt(DateUtils.getCurrentDateTime());
                mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            mpvo.setCustNo(custNo);
            makeposRepo.save(mpvo);

            this.renewalMatrStock(el);
        }

    }

    public void renewalMatrStock(Map<String, Object> paraMap) {
        MatrStk msvo = new MatrStk();
        Float stkQty = 0F;

        log.info("paraMap : " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        msvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        msvo.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
        msvo.setUsedYn("Y");

        MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(), msvo.getUsedYn());
        if(chkvo != null){
            stkQty = chkvo.getStkQty();
            stkQty += Float.parseFloat(paraMap.get("iwh_qty").toString());
            msvo.setStkQty(stkQty);
            msvo.setMatrStkNo(chkvo.getMatrStkNo());
            msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
            msvo.setModDt(DateUtils.getCurrentDateTime());
            msvo.setModIp(paraMap.get("ipaddr").toString());
            msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        }
        else{
            msvo.setMatrStkNo(0L);
            msvo.setStkQty(Float.parseFloat(paraMap.get("iwh_qty").toString()));
            msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));

            msvo.setModDt(DateUtils.getCurrentDateTime());
            msvo.setModIp(paraMap.get("ipaddr").toString());
            msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

            msvo.setRegDt(DateUtils.getCurrentDateTime());
            msvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setRegIp(paraMap.get("ipaddr").toString());
        }
        msvo.setCustNo(custNo);
        matrStkRepo.save(msvo);
        this.checkMatrSts(paraMap);
    }

    @Override
    public void renewalMatrIwh(Map<String, Object> paraMap){
        MatrIwh mivo = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("matrIwhList");
        Float qty = 0F;
        for(Map<String, Object> el : ds){
            mivo = new MatrIwh();
            try{
                mivo.setCmpyNo(Long.parseLong(el.get("cmpy_no").toString()));
            }catch(NullPointerException ne){
                mivo.setCmpyNo(0L);
            }

            mivo.setMatrNo(Long.parseLong(el.get("matr_no").toString()));
            mivo.setPursNo(Long.parseLong(el.get("purs_no").toString()));
            mivo.setPursMatrNo(Long.parseLong(el.get("purs_matr_no").toString()));
            mivo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
            mivo.setIwhQty(Float.parseFloat(paraMap.get("iwh_qty").toString()));
            mivo.setUsedYn("Y");

            MatrIwh chkvo = imr.findByCustNoAndMatrNoAndWhNoAndPursNoAndPursMatrNoAndCmpyNoAndUsedYn(custNo,mivo.getMatrNo(), mivo.getWhNo(), mivo.getPursNo(), mivo.getPursMatrNo(), mivo.getCmpyNo(), mivo.getUsedYn());
            if(chkvo != null){
                qty = chkvo.getIwhQty();
                qty += mivo.getIwhQty();
                mivo.setIwhNo(chkvo.getIwhNo());
                mivo.setIwhQty(qty);
                mivo.setRetnQty(chkvo.getRetnQty());
                mivo.setRetnResn(chkvo.getRetnResn());
                mivo.setRegId(chkvo.getRegId());
                mivo.setRegIp(chkvo.getRegIp());
                mivo.setRegDt(chkvo.getRegDt());
                mivo.setIwhDt(DateUtils.getCurrentDateTime());
                mivo.setModDt(DateUtils.getCurrentDateTime());
                mivo.setModIp(paraMap.get("ipaddr").toString());
                mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            }
            else{
                try{
                    mivo.setRetnResn(Long.parseLong(paraMap.get("retn_resn").toString()));
                }catch(NullPointerException ne){
                    mivo.setRetnResn(null);
                }

                try{
                    mivo.setRetnQty(Float.parseFloat(paraMap.get("retn_qty").toString()));
                }catch (NullPointerException ne){
                    mivo.setRetnQty(0F);
                }

                mivo.setIwhNo(0L);
                mivo.setIwhDt(DateUtils.getCurrentDateTime());
                mivo.setRegDt(DateUtils.getCurrentDateTime());
                mivo.setRegIp(paraMap.get("ipaddr").toString());
                mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));

                mivo.setModDt(DateUtils.getCurrentDateTime());
                mivo.setModIp(paraMap.get("ipaddr").toString());
                mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            }
            mivo.setCustNo(custNo);
            imr.save(mivo);
            this.checkMatrSts(el);
        }
    }

    public void checkMatrSts(Map<String, Object> paraMap){
        Map<String,Object> dmap = new HashMap<String,Object>();
        log.info("checkMatrSts - paraMap : " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        dmap.put("pursSts", Integer.parseInt(env.getProperty("purs.sts.end")));
        dmap.put("pursNo", Integer.parseInt(paraMap.get("pursNo").toString()));
        dmap.put("matrNo", Long.parseLong(paraMap.get("whNo").toString()));
        dmap.put("cmpyNo", Long.parseLong(paraMap.get("cmpyNo").toString()));
        dmap.put("custNo",custNo);

        mapper.checkMatrSts(dmap);

        mapper.updatePursMatrWhNo(dmap);
    }


    @Override
    public void renewalPursMatr(Map<String, Object> paraMap) {
        mapper.renewalPursMatr(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrStkImpoList(Map<String, Object> paraMap) {
        return mapper.getMatrStkImpoList(paraMap);
    }

    @Override
    public int getMatrStkImpoListCount(Map<String, Object> paraMap) {
        return mapper.getMatrStkImpoListCount(paraMap);
    }

    @Override
    public Map<String, Object> getExportPlanInfo(Map<String, Object> paraMap) {
        return mapper.getExportPlanInfo(paraMap);
    }

    @Override
    public void outMatrPos(Map<String, Object> paraMap){
        MatrPos mpvo = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("matrPosList");
        Float qty = 0F;

        for(Map<String, Object> el : ds){
            mpvo = new MatrPos();
            mpvo.setWhNo(Long.parseLong(el.get("wh_no").toString())); //560
            mpvo.setMatrNo(Long.parseLong(el.get("matr_no").toString())); //74
            mpvo.setStairIdx(Integer.parseInt(el.get("stair_idx").toString())); //1
            mpvo.setRowIdx(Integer.parseInt(el.get("row_idx").toString())); //1
            mpvo.setColIdx(Integer.parseInt(el.get("col_idx").toString())); //11
            mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
            mpvo.setIwhSeq(Integer.parseInt(el.get("iwh_seq").toString()));
            mpvo.setUsedYn("Y");

            MatrPos chkvo = makeposRepo.findByCustNoAndWhNoAndMatrNoAndStairIdxAndRowIdxAndColIdxAndIwhSeqAndMatrStatAndUsedYn(custNo,mpvo.getWhNo(), mpvo.getMatrNo(), mpvo.getStairIdx(), mpvo.getRowIdx(), mpvo.getColIdx(), mpvo.getIwhSeq(), mpvo.getMatrStat(), mpvo.getUsedYn() );
            if(chkvo != null){
                mpvo.setMatrPosNo(chkvo.getMatrPosNo());
                qty = chkvo.getMatrQty();
                try {
                    qty -= Float.parseFloat(el.get("matr_qty").toString());
                }
                catch (NullPointerException ne) {
                    qty -= 1f; //????????? ???????????? kmj
                }

                if(qty >= 0){
                    mpvo.setMatrQty(0F);
                    mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_owh_stat")));
                }
                else{
                    mpvo.setMatrQty(qty * -1);
                    mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
                }

                mpvo.setModDt(DateUtils.getCurrentDateTime());
                mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mpvo.setModIp(paraMap.get("ipaddr").toString());
            }
            else{
                continue;
            }
            mpvo.setCustNo(custNo);
            makeposRepo.save(mpvo);
        }
    }



    @Override
    public void updateMatrOwhStk(Map<String, Object> paraMap){
        mapper.updateMatrOwhStk(paraMap);
    }

    @Override
    public void updateMatrOwh(Map<String, Object> paraMap) {
        MatrOwh movo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("matrOwhPostList");
        Float qty = 0F;

        for (Map<String, Object> el : ds) {
            movo = new MatrOwh();
            movo.setWhNo(Long.parseLong(el.get("wh_no").toString()));
            movo.setMatrNo(Long.parseLong(el.get("matr_no").toString()));
            movo.setIndcNo(Long.parseLong(el.get("indc_no").toString()));
            movo.setOwhUnit(Long.parseLong(el.get("unit_no").toString()));
            try {
                movo.setOwhReqDt(sdf.parse(el.get("make_to_dt").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                movo.setOwhReqQty(Float.parseFloat(el.get("need_qty").toString()));
            }
            catch (NullPointerException ne) { //????????? ?????? ??????/kmj
                movo.setOwhReqQty(1f);
            }
            movo.setUsedYn("Y");

            MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndWhNoAndUsedYn(custNo,movo.getIndcNo(), movo.getMatrNo(),movo.getWhNo(), movo.getUsedYn());

            if (chkvo != null) {
//                log.info("chkvo.getOwhQty??? ??????? : "+chkvo.getOwhQty());
                qty = chkvo.getOwhQty();
                try {
                    qty += Float.parseFloat(el.get("matr_qty").toString());
                }
                catch (NullPointerException ne) {
                     qty += 1;//????????? ????????????
                }
//                log.info("qty??? ?????? ??????? : "+qty);

                movo.setOwhNo(chkvo.getOwhNo());
                movo.setOwhQty(qty);
                movo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                movo.setModIp(paraMap.get("ipaddr").toString());
                movo.setModDt(DateUtils.getCurrentDate());
                movo.setOwhDt(DateUtils.getCurrentDate());
                movo.setRegDt(chkvo.getRegDt());
                movo.setRegId(chkvo.getRegId());
                movo.setRegIp(chkvo.getRegIp());

            } else {
                movo.setOwhNo(0L);
                try {
                    movo.setOwhQty(Float.parseFloat(el.get("matr_qty").toString()));
                }
                catch (NullPointerException ne) {
                    movo.setOwhQty(1f); //????????? ?????? ?????? : kmj
                }
                movo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                movo.setRegIp(paraMap.get("ipaddr").toString());
                movo.setRegDt(DateUtils.getCurrentDate());
                movo.setOwhDt(DateUtils.getCurrentDate());
            }
            movo.setCustNo(custNo);
            omr.save(movo);
        }

    }

    @Override
    public void updateMakeIndcMatr(Map<String, Object> paraMap){
        MakeIndcMatr mimvo = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("matrOwhPostList");
        Float qty = 0F;


        for(Map<String, Object> el : ds){
            mimvo = new MakeIndcMatr();
            mimvo.setIndcNo(Long.parseLong(el.get("indc_no").toString()));
            mimvo.setMatrNo(Long.parseLong(el.get("matr_no").toString()));
            mimvo.setUsedYn("Y");

            MakeIndcMatr chkvo = mimRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,mimvo.getIndcNo(), mimvo.getMatrNo(), mimvo.getUsedYn());
            mimvo.setIndcMatrNo(chkvo.getIndcMatrNo());
            mimvo.setNeedQty(chkvo.getNeedQty());
            mimvo.setRegDt(chkvo.getRegDt());
            mimvo.setRegId(chkvo.getRegId());
            mimvo.setRegIp(chkvo.getRegIp());

            if(chkvo.getRecvQty() == null){
                try {
                    mimvo.setRecvQty(Float.parseFloat(el.get("matr_qty").toString()));
                }
                catch (NullPointerException ne) {
                    mimvo.setRecvQty(1f);  //????????? ???????????? kmj

                }
            }
            else{
                qty = chkvo.getRecvQty();
                try {
                    qty += Float.parseFloat(el.get("matr_qty").toString());
                }
                catch (NullPointerException ne) {
                    qty += 1f;  //????????? ???????????? kmj
                }
                mimvo.setRecvQty(qty);
            }

            mimvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mimvo.setModIp(paraMap.get("ipaddr").toString());
            mimvo.setModDt(DateUtils.getCurrentDate());
            mimvo.setCustNo(custNo);
            mimRepo.save(mimvo);
        }
    }

    @Override
    public List<Map<String, Object>> getTabletWhNoAndMatrUnit(Map<String, Object> paraMap) {
        return mapper.getTabletWhNoAndMatrUnit(paraMap);
    }

    @Override
    public void tabletMatrStk(Map<String, Object> paraMap){
        MatrStk msvo = new MatrStk();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        msvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        msvo.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
        msvo.setStkQty(Float.parseFloat(paraMap.get("need_qty").toString()));
        msvo.setUsedYn("Y");

        Float qty = 0F;
        MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(), msvo.getUsedYn());
        if(chkvo != null){
            qty = chkvo.getStkQty();
            qty -= msvo.getStkQty();
            msvo.setCustNo(custNo);
            msvo.setStkQty(qty);
            msvo.setMatrStkNo(chkvo.getMatrStkNo());
            msvo.setStatCd(chkvo.getStatCd());
            msvo.setStatTrfDt(DateUtils.getCurrentDateTime());
            msvo.setCustNo(custNo);
            msvo.setRegDt(chkvo.getRegDt());
            msvo.setRegId(chkvo.getRegId());
            msvo.setRegIp(chkvo.getRegIp());

            msvo.setModDt(DateUtils.getCurrentDateTime());
            msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setModIp(paraMap.get("ipaddr").toString());
            msvo.setCustNo(custNo);
            matrStkRepo.save(msvo);

            this.tabletMatrOwh(paraMap);
            this.MatrStatChange(msvo);
        }
    }

    public void tabletMatrOwh(Map<String, Object> paraMap){
        MatrOwh movo = new MatrOwh();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        movo.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
        movo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        movo.setIndcNo(Long.parseLong(paraMap.get("indc_no").toString()));
        movo.setOwhReqQty(Float.parseFloat(paraMap.get("need_qty").toString()));
        movo.setOwhQty(Float.parseFloat(paraMap.get("need_qty").toString()));
        movo.setUsedYn("Y");

        Float qty = 0F;
        MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,movo.getIndcNo(), movo.getMatrNo(), movo.getUsedYn());
        if(chkvo != null){
            qty = chkvo.getOwhQty();
            qty += movo.getOwhQty();
            movo.setOwhQty(qty);
            movo.setOwhDt(DateUtils.getCurrentDateTime());
            movo.setOwhNo(chkvo.getOwhNo());
            movo.setOwhReqQty(chkvo.getOwhReqQty());
            movo.setOwhReqDt(chkvo.getOwhReqDt());
            movo.setOwhUnit(chkvo.getOwhUnit());

            movo.setRegIp(chkvo.getRegIp());
            movo.setRegDt(chkvo.getRegDt());
            movo.setRegId(chkvo.getRegId());

            movo.setModDt(DateUtils.getCurrentDateTime());
            movo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            movo.setModIp(paraMap.get("ipaddr").toString());
        }
        else{
            movo.setOwhNo(0L);
            movo.setOwhDt(DateUtils.getCurrentDateTime());

            try {
                movo.setOwhReqDt(transFormat.parse(paraMap.get("make_to_dt").toString().substring(0, 10)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movo.setOwhUnit(Long.parseLong(paraMap.get("matr_unit").toString()));
            movo.setRegDt(DateUtils.getCurrentDateTime());
            movo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            movo.setRegIp(paraMap.get("ipaddr").toString());

            movo.setModDt(DateUtils.getCurrentDateTime());
            movo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            movo.setModIp(paraMap.get("ipaddr").toString());
        }
        movo.setCustNo(custNo);
        omr.save(movo);
    }

    public void MatrStatChange(MatrStk msvo){
        MatrStk vo = new MatrStk();
        Map<String,Object> dmap = new HashMap<String,Object>();
        Long custNo = msvo.getCustNo();
        vo.setMatrStkNo(msvo.getMatrStkNo());
        MatrStk chkvo = matrStkRepo.findByCustNoAndMatrStkNo(custNo,vo.getMatrStkNo());
        if(chkvo != null){
            if(chkvo.getStkQty() == 0){
                dmap.put("matrStkNo",chkvo.getMatrStkNo());
                dmap.put("statCd", Long.parseLong(env.getProperty("code.stk_owh_stat")));
                dmap.put("custNo",custNo);
                mapper.updateMatrStkStat(dmap);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getTabletReqMatrIwhList(Map<String, Object> paraMap) {
        return mapper.getTabletReqMatrIwhList(paraMap);
    }

    @Override
    public int getTabletReqMatrIwhListCount(Map<String, Object> paraMap) {
        return mapper.getTabletReqMatrIwhListCount(paraMap);
    }

    //tablet ???????????? ??????
    @Override
    public void tabletMatrIwh(Map<String, Object> paraMap){
        MatrIwh mivo = new MatrIwh();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        mivo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        mivo.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
        mivo.setPursMatrNo(Long.parseLong(paraMap.get("purs_matr_no").toString()));
        mivo.setPursNo(Long.parseLong(paraMap.get("purs_no").toString()));
        mivo.setCmpyNo(Long.parseLong(paraMap.get("cmpy_no").toString()));
        mivo.setUsedYn("Y");
        mivo.setIwhQty(Float.parseFloat(paraMap.get("iwh_qty").toString()));

        Float qty = 0F;
        MatrIwh chkvo = imr.findByCustNoAndMatrNoAndWhNoAndPursNoAndPursMatrNoAndCmpyNoAndUsedYn(custNo,mivo.getMatrNo(), mivo.getWhNo(), mivo.getPursNo(), mivo.getPursMatrNo(), mivo.getCmpyNo(), mivo.getUsedYn());
        if(chkvo != null){
            qty = chkvo.getIwhQty();
            qty += mivo.getIwhQty();
            mivo.setIwhQty(qty);
            mivo.setRegDt(chkvo.getRegDt());
            mivo.setRegIp(chkvo.getRegIp());
            mivo.setRegId(chkvo.getRegId());
            mivo.setIwhNo(0L);
            mivo.setIwhDt(DateUtils.getCurrentDateTime());
            mivo.setRetnResn(chkvo.getRetnResn());
            mivo.setRetnQty(chkvo.getRetnQty());

            mivo.setModDt(DateUtils.getCurrentDateTime());
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setModIp(paraMap.get("ipaddr").toString());
        }
        else{
            mivo.setIwhNo(0L);
            mivo.setIwhDt(DateUtils.getCurrentDateTime());
            mivo.setRetnResn(0L);
            mivo.setRetnQty(0F);
            mivo.setRegDt(DateUtils.getCurrentDateTime());
            mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setRegIp(paraMap.get("ipaddr").toString());

            mivo.setModDt(DateUtils.getCurrentDateTime());
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setModIp(paraMap.get("ipaddr").toString());
        }
        mivo.setCustNo(custNo);
        imr.save(mivo);

        this.tabletMatrIwhStk(paraMap); //iwh_stk??? ??????
    }

    public void tabletMatrIwhStk(Map<String, Object> paraMap){
        MatrStk msvo = new MatrStk();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        msvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        msvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        msvo.setStkQty(Float.parseFloat(paraMap.get("iwh_qty").toString()));
        msvo.setUsedYn("Y");

        Float qty = 0F;
        MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(), msvo.getUsedYn());
        if(chkvo != null){
            qty = chkvo.getStkQty();
            qty += msvo.getStkQty();
            msvo.setStkQty(qty);
            msvo.setMatrStkNo(chkvo.getMatrStkNo());
            msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
            msvo.setStatTrfDt(chkvo.getStatTrfDt());

            msvo.setRegDt(chkvo.getRegDt());
            msvo.setRegId(chkvo.getRegId());
            msvo.setRegIp(chkvo.getRegIp());

            msvo.setModDt(DateUtils.getCurrentDateTime());
            msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setModIp(paraMap.get("ipaddr").toString());
        }
        else{
            msvo.setMatrStkNo(0L);
            msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
            msvo.setStatTrfDt(DateUtils.getCurrentDateTime());

            msvo.setRegDt(DateUtils.getCurrentDateTime());
            msvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setRegIp(paraMap.get("ipaddr").toString());

            msvo.setModDt(DateUtils.getCurrentDateTime());
            msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            msvo.setModIp(paraMap.get("ipaddr").toString());
        }
        msvo.setCustNo(custNo);
        matrStkRepo.save(msvo);

        this.tabletPursMatr(paraMap); //purs_matr??? ?????? ??????
        this.tabletMatrPos(paraMap);
    }

    public void tabletPursMatr(Map<String, Object> paraMap){
        Map<String,Object> dmap = new HashMap<String,Object>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        dmap.put("matrNo", paraMap.get("matr_no"));
        dmap.put("cmpyNo", paraMap.get("cmpy_no"));
        dmap.put("pursNo", paraMap.get("purs_no"));
        dmap.put("iwhWgt", paraMap.get("iwh_wgt"));
        dmap.put("custNo", custNo);
        mapper.updateTabletPursMatr(dmap);

        this.updateTabletPursSts(paraMap); //purs_info??? ????????? ????????? ??????
    }

    public void tabletMatrPos(Map<String, Object> paraMap){
        MatrPos mpvo = new MatrPos();
        Float qty = 0F;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        mpvo.setMatrNo(Long.parseLong(paraMap.get("matr_no").toString()));
        mpvo.setWhNo(Long.parseLong(paraMap.get("wh_no").toString()));
        mpvo.setStairIdx(Integer.parseInt(paraMap.get("stair_idx").toString()));
        mpvo.setColIdx(Integer.parseInt(paraMap.get("col_idx").toString()));
        mpvo.setRowIdx(Integer.parseInt(paraMap.get("row_idx").toString()));
        mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
        mpvo.setUsedYn("Y");

        MatrPos chkvo = makeposRepo.findByCustNoAndMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndMatrStatAndUsedYn(custNo,mpvo.getMatrNo(), mpvo.getWhNo(), mpvo.getStairIdx(), mpvo.getColIdx(), mpvo.getRowIdx(), mpvo.getMatrStat(), mpvo.getUsedYn());
        if(chkvo != null){
            qty = chkvo.getMatrQty();
            qty -= Float.parseFloat(paraMap.get("iwh_qty").toString());
            mpvo.setMatrQty(qty);
            mpvo.setMatrPosNo(chkvo.getMatrPosNo());
            mpvo.setBarCodeImg(chkvo.getBarCodeImg());
            mpvo.setIwhSeq(chkvo.getIwhSeq());
            mpvo.setQrCodeImg(chkvo.getQrCodeImg());
            mpvo.setRegDt(chkvo.getRegDt());
            mpvo.setRegId(chkvo.getRegId());
            mpvo.setRegIp(chkvo.getRegIp());

            mpvo.setModDt(DateUtils.getCurrentDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
        }
        else{
            mpvo.setMatrPosNo(0L);
            mpvo.setIwhSeq(0);
            mpvo.setStairIdx(1);
            mpvo.setColIdx(1);
            mpvo.setRowIdx(1);

            mpvo.setMatrQty(Float.parseFloat(paraMap.get("iwh_qty").toString()));
            mpvo.setRegDt(DateUtils.getCurrentDateTime());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setRegIp(paraMap.get("ipaddr").toString());

            mpvo.setModDt(DateUtils.getCurrentDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
        }
        mpvo.setCustNo(custNo);
        makeposRepo.save(mpvo);
    }

    public void updateTabletPursSts(Map<String, Object> paraMap){
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> dmap = new HashMap<String, Object>();
        dmap.put("pursNo", paraMap.get("pursNo"));
        dmap.put("pursSts", Integer.parseInt(env.getProperty("purs.sts.end")));
        dmap.put("custNo", custNo);
        mapper.updateTabletPursSts(dmap);
    }

    //620_makeIndc??? ???????????? ??????
   @Transactional
    public void owhMatrList(Map<String, Object> paraMap){
        String tag = "ioService.owhMatrList => ";
        log.info(tag + "paraMap = " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("matrList");

        for (Map<String,Object> el : ds) {
            //matrStk ?????? ????????? ??????
            MatrStk msvo = new MatrStk();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            Float qty = 0F;
            msvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            msvo.setWhNo(Long.parseLong(el.get("whNo").toString()));
            msvo.setUsedYn("Y");
            MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,msvo.getWhNo(), msvo.getMatrNo(), msvo.getUsedYn());
            if(chkvo != null){
                msvo.setMatrStkNo(chkvo.getMatrStkNo());
                msvo.setRegDt(chkvo.getRegDt());
                msvo.setRegId(chkvo.getRegId());
                msvo.setRegIp(chkvo.getRegIp());
                msvo.setModDt(DateUtils.getCurrentDate());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setModIp(paraMap.get("ipaddr").toString());

                qty = Float.parseFloat(chkvo.getStkQty().toString()) - Float.parseFloat(el.get("need_qty").toString());
                msvo.setStkQty(qty);
                msvo.setStatTrfDt(DateUtils.getCurrentDate());
                msvo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
            }
            msvo.setCustNo(custNo);
            matrStkRepo.save(msvo);

            //matrOwh ???????????? ??????
            MatrOwh movo = new MatrOwh();
            movo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
            movo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            movo.setUsedYn("Y");
            MatrOwh chkmovo = matrOwhRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,movo.getIndcNo(), movo.getMatrNo(), movo.getUsedYn());
            if(chkmovo != null){
                movo.setOwhNo(chkmovo.getOwhNo());
                movo.setOwhQty(Float.parseFloat(el.get("needQty").toString()) + chkmovo.getOwhQty());
                movo.setOwhReqQty(chkmovo.getOwhReqQty());
                movo.setWhNo(chkmovo.getWhNo());
                movo.setOwhUnit(chkmovo.getOwhUnit());
                movo.setRegDt(chkmovo.getRegDt());
                movo.setRegIp(chkmovo.getRegIp());
                movo.setRegId(chkmovo.getRegId());

                movo.setModDt(DateUtils.getCurrentDate());
                movo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                movo.setModIp(paraMap.get("ipaddr").toString());
            }
            else{
                movo.setOwhNo(0L);
                movo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
                movo.setOwhQty(Float.parseFloat(el.get("needQty").toString()));
                movo.setOwhReqQty(Float.parseFloat(el.get("needQty").toString()));

                movo.setWhNo(Long.parseLong(paraMap.get("whNo").toString()));

                movo.setRegDt(DateUtils.getCurrentDate());
                movo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                movo.setRegIp(paraMap.get("ipaddr").toString());
            }


            movo.setOwhDt(DateUtils.getCurrentDate());

            try{
                movo.setOwhReqDt(transFormat.parse(paraMap.get("owhReqDt").toString().substring(0, 10)));
            }catch(NullPointerException | ParseException ne){
                movo.setOwhReqDt(DateUtils.getCurrentDate());
            }
            movo.setCustNo(custNo);
            matrOwhRepo.save(movo);
        }

        //????????????????????? make_indc??? indc_sts ??????
        MakeIndc mivo = new MakeIndc();
        log.info("indc_no ???"+ paraMap.get("indcNo"));
        mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        mivo.setUsedYn("Y");
        MakeIndc chkvo = makePepo.findByCustNoAndIndcNoAndUsedYn(custNo,mivo.getIndcNo(), mivo.getUsedYn());
        if(chkvo != null){
            mivo.setParIndcNo(chkvo.getParIndcNo());
            mivo.setIndcDt(chkvo.getIndcDt());
            mivo.setIndcQty(chkvo.getIndcQty());
            mivo.setMakeFrDt(chkvo.getMakeFrDt());
            mivo.setMakeToDt(chkvo.getMakeToDt());
            mivo.setMakeUnit(chkvo.getMakeUnit());
            mivo.setMatrReqDt(chkvo.getMatrReqDt());
            mivo.setModDt(chkvo.getModDt());
            mivo.setModId(chkvo.getModId());
            mivo.setModIp(chkvo.getModIp());
            mivo.setOrdNo(chkvo.getOrdNo());
            mivo.setProcCd(chkvo.getProcCd());
            mivo.setProdNo(chkvo.getProdNo());
            mivo.setRegIp(chkvo.getRegIp());
            mivo.setRegId(chkvo.getRegId());
            mivo.setRegDt(chkvo.getRegDt());
            mivo.setStatCd(chkvo.getStatCd());
            mivo.setBuf(chkvo.getBuf());
            mivo.setIdxNo(chkvo.getIdxNo());
            mivo.setCtlFillYield(chkvo.getCtlFillYield());
            mivo.setFillYield(chkvo.getFillYield());
            mivo.setIndcWgt(chkvo.getIndcWgt());
            mivo.setMaxMakeQty(chkvo.getMaxMakeQty());
            mivo.setMaxMakeWgt(chkvo.getMaxMakeWgt());
            mivo.setOperRt(chkvo.getOperRt());
            mivo.setFaultRt(chkvo.getFaultRt());
            mivo.setRealYield(chkvo.getRealYield());
            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makePossible").toString()));
        }
        mivo.setCustNo(custNo);
        makePepo.save(mivo);
    }
    //?????? ???????????? ?????? ?????????????????? ?????? : ?????? MES???
    @Override
    public List<Map<String, Object>> getMatrForIwhList(Map<String, Object> paraMap) {
        String tag = "ioService.getMatrIwhList => ";
        log.info(tag + " paraMap = > " + paraMap.toString());
        return mapper.getMatrForIwhList(paraMap);
    }

    @Override
    public int getMatrForIwhListCount(Map<String, Object> paraMap) {
        return mapper.getMatrForIwhListCount(paraMap);
    }


    //?????? ???????????? ?????? ?????????????????? ?????? : ?????? MES???
    @Override
    public List<Map<String, Object>> getMatrForIwhListByT(Map<String, Object> paraMap) {
        String tag = "ioService.getMatrForIwhListByT => ";
        log.info(tag + " paraMap = > " + paraMap.toString());
        return mapper.getMatrForIwhListByT(paraMap);
    }

    @Override
    public int getMatrForIwhListByTCount(Map<String, Object> paraMap) {
        return mapper.getMatrForIwhListByTCount(paraMap);
    }


    @SneakyThrows
    @Override
    public void saveMatrIwhList(Map<String, Object> paraMap) {
        String tag = "ioService.saveMatrIwhList => ";
        log.info(tag + "paraMap = " + paraMap);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("iwhList");
        Long matrNo = 0L;
        Long whNo = 0L;
        Float stkQty = 0F;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String,Object> el : ds) {
            MatrIwh mivo = new MatrIwh();

            matrNo= Long.parseLong(el.get("matrNo").toString());
            mivo.setMatrNo(matrNo);

            whNo = Long.parseLong(el.get("whNo").toString());
            mivo.setWhNo(whNo);

            mivo.setIwhQty(Float.parseFloat(el.get("iwhQty").toString()));
            mivo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
            mivo.setIwhDt(sdf.parse(paraMap.get("iwhDt").toString())); //????????????
            try {
                mivo.setPursMatrNo(Long.parseLong(el.get("pursMatrNo").toString()));
            }
            catch (NullPointerException ne) { //??????????????? ?????????????????? ???????????? ??????
                mivo.setPursMatrNo(0L);
            }
            try {
                mivo.setPursNo(Long.parseLong(el.get("pursNo").toString()));
            }
            catch (NullPointerException ne) {//??????????????? ?????????????????? ???????????? ??????
                mivo.setPursNo(0L);
            }

            mivo.setDateManufacture(sdf.parse(paraMap.get("createDt").toString())); //????????????
            mivo.setModDt(DateUtils.getCurrentDateTime());
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setModIp(paraMap.get("ipaddr").toString());
            mivo.setRegDt(DateUtils.getCurrentDateTime());
            mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setRegIp(paraMap.get("ipaddr").toString());


            try{
                mivo.setInspEr(Long.parseLong(paraMap.get("insp").toString())); //?????????
            }catch(NullPointerException ne){
                mivo.setInspEr(0L);
            }

            try{
                mivo.setPaltCd(Long.parseLong(paraMap.get("paltCd").toString()));
            }catch(NullPointerException ne){
                mivo.setPaltCd(0L);
            }
            try{
                mivo.setPaltQty(Integer.parseInt(paraMap.get("paltQty").toString()));
            }catch(NullPointerException ne){
                mivo.setPaltQty(0);
            }

            try{
                mivo.setRetnQty(Float.parseFloat(paraMap.get("retnQty").toString())); //????????????
            }catch (NullPointerException en) {
                mivo.setRetnQty(0F);
            }try{
                mivo.setRetnResn(Long.parseLong(paraMap.get("retnResn").toString())); //????????????
            }catch (NullPointerException en){
                mivo.setRetnResn(0L);
            }

            log.info(tag + "1.???????????? ??????..");
            mivo.setCustNo(custNo);
            mivo.setUsedYn("Y");
            matrIwhRepo.save(mivo);


            Long pursIng = Long.parseLong(env.getProperty("purs.sts.ing"));
            Long pursEnd = Long.parseLong(env.getProperty("purs.sts.end"));
            if (mivo.getPursNo() > 0L) {

                PursMatr pmvo = pmr.findByCustNoAndPursMatrNoAndUsedYn(custNo, mivo.getPursMatrNo(),"Y" );
                if (pmvo != null) {
                    log.info(tag + "3.???????????? ?????? ??????");
                    pmvo.setCmpyNo(mivo.getCmpyNo());
                    pmvo.setWhNo(mivo.getWhNo());
                    log.info(tag + "3.1.1 ???????????? = " + pmvo.getPursQty());
                    log.info(tag + "3.1.2 ???????????? = " + mivo.getIwhQty());
                    Float pursQtyChk = pmvo.getPursQty();
                    Float IwhQtyChk = mivo.getIwhQty();
                    if (paraMap.get("finishYn").toString().equals("Y")){
                        pmvo.setPursSts(pursEnd);
                    }else{
                        if (pursQtyChk.equals(IwhQtyChk)){
                            pmvo.setPursSts(pursEnd);
                        }else{
                            pmvo.setPursSts(pursIng);
                        }

                    }

                    pmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    pmvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    pmvo.setModIp(paraMap.get("ipaddr").toString());
                    pmr.save(pmvo);
                }

                PursInfo pursvo = pursRepo.findByCustNoAndPursNoAndUsedYn(custNo, mivo.getPursNo(), "Y");

                Map<String, Object> chkItem = pursMapper.getPursChk(mivo);

                if (pursvo != null) {
                    log.info(tag + "2.??????????????? ?????? ??????" + chkItem);
                    pursvo.setCmpyNo(mivo.getCmpyNo());
                    if (chkItem.get("pursType").toString().equals("Y")){
                        pursvo.setPursSts(pursEnd);
                        MakeIndc chkMake = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(pursvo.getCustNo() , pursvo.getIndcNo() ,"Y");
                        if (chkMake != null){
                            chkMake.setIndcSts(2402L);
                            makeIndcRepo.save(chkMake);
                        }

                    }else{
                        pursvo.setPursSts(pursIng);
                    }
                    pursvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    pursvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    pursvo.setModIp(paraMap.get("ipaddr").toString());
                    pursRepo.save(pursvo);
                }
            }

            MatrStk msvo = new MatrStk();
            msvo.setUsedYn("Y");
            MatrStk chkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo,whNo, matrNo,"Y");
            if(chkvo != null){
                stkQty = chkvo.getStkQty();
                stkQty += Float.parseFloat(el.get("iwhQty").toString());
                msvo.setStkQty(stkQty);
                msvo.setMatrNo(chkvo.getMatrNo());
                msvo.setMatrStkNo(chkvo.getMatrStkNo());
                msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
                msvo.setModDt(DateUtils.getCurrentDateTime());
                msvo.setModIp(paraMap.get("ipaddr").toString());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setWhNo(whNo);
            }
            else{
                msvo.setMatrStkNo(0L);
                msvo.setMatrNo(matrNo);
                msvo.setWhNo(whNo);
                msvo.setStkQty(Float.parseFloat(el.get("iwhQty").toString()));
                msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));

                msvo.setModDt(DateUtils.getCurrentDateTime());
                msvo.setModIp(paraMap.get("ipaddr").toString());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

                msvo.setRegDt(DateUtils.getCurrentDateTime());
                msvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setRegIp(paraMap.get("ipaddr").toString());

                msvo.setWhNo(whNo);
            }
            log.info(tag + "4.???????????? ??????.");
            msvo.setUsedYn("Y");
            msvo.setCustNo(custNo);
            matrStkRepo.save(msvo);

            this.makeInspHstr(mivo); //???????????? ??????
        }
    }

    private void makeInspHstr(MatrIwh mivi) {

    }

    @Override
    public List<Map<String, Object>> getMatrForOwhList(Map<String, Object> paraMap) {
        return mapper.getMatrForOwhList(paraMap);
    }

    @Override
    public int getMatrForOwhListCount(Map<String, Object> paraMap) {
        return mapper.getMatrForOwhListCount(paraMap);
    }



    @Transactional
    @Override
    public void saveMatrOwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.IoService.saveMatrOwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("owhList");
        Long matrNo = 0L;
        Float stkQty = 0F;
        Long whNo = 0L;
        Long indcNo = 0L;
        Float owhQty = 0F;
        Float owhReqQty = 0F;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String,Object> el : ds) {
            MatrOwh movo = new MatrOwh();
            matrNo= Long.parseLong(el.get("matrNo").toString());
            whNo = Long.parseLong(el.get("whNo").toString());
            try {
                indcNo = Long.parseLong(el.get("indcNo").toString());
                movo.setIndcNo(indcNo);
            }catch (NullPointerException en) {
                movo.setIndcNo(0L);
            }
            owhQty = Float.parseFloat(el.get("owhQty").toString());
            movo.setMatrNo(matrNo);
            movo.setWhNo(whNo);
            movo.setOwhQty(owhQty);
            try {
                movo.setOwhDt(sdf.parse(paraMap.get("owhDt").toString()));
                log.info(tag + "?????????????????? = " + movo.getOwhDt());//kill
            }
            catch (NullPointerException | ParseException ne) {
                movo.setOwhDt(DateUtils.getCurrentDate());
                log.info(tag + "????????????????????? ?????????(????????????)??? ?????? = " + movo.getOwhDt());//kill
            }
            try {
                movo.setOwhReqDt(sdf.parse(paraMap.get("owhReqDt").toString()));
            }
            catch (NullPointerException | ParseException ne) {
                movo.setOwhReqDt(movo.getOwhDt());
            }
            try {
                owhReqQty = Float.parseFloat(el.get("owhReqQty").toString());
                movo.setOwhReqQty(owhReqQty);
            }
            catch (NullPointerException ne) {
                movo.setOwhReqQty(movo.getOwhQty());
            }

            movo.setUsedYn("Y");
            try {
                movo.setRmk(paraMap.get("rmk").toString());
            }
            catch (NullPointerException ne) {

            }

            movo.setModDt(DateUtils.getCurrentDateTime());
            movo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            movo.setModIp(paraMap.get("ipaddr").toString());
            movo.setRegDt(DateUtils.getCurrentDateTime());
            movo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            movo.setRegIp(paraMap.get("ipaddr").toString());

            try{
                movo.setOwhUnit(Long.parseLong(el.get("pursUnit").toString()));
            }catch(NullPointerException ne){
                movo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_Kg")));
            }
            try{
                movo.setInspEr(Long.parseLong(paraMap.get("insp").toString()));
            }catch(NullPointerException ne){
                movo.setInspEr(0L);
            }
            try{
                movo.setPaltCd(Long.parseLong(paraMap.get("palt").toString()));
            }catch(NullPointerException ne){
                movo.setPaltCd(0L);
            }

            movo.setCustNo(custNo);
            matrOwhRepo.save(movo);

            //?????????????????? ??????----
            MatrStk msvo = new MatrStk();
            msvo.setUsedYn("Y");

            MatrStk chkvo = matrStkRepo.findByCustNoAndMatrNoAndWhNoAndUsedYn(custNo,matrNo, whNo,"Y");
            if(chkvo != null){
                stkQty = chkvo.getStkQty();
                stkQty -= owhQty;
                msvo.setStkQty(stkQty);
                msvo.setMatrNo(chkvo.getMatrNo());
                msvo.setMatrStkNo(chkvo.getMatrStkNo());
                msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
                msvo.setModDt(DateUtils.getCurrentDateTime());
                msvo.setModIp(paraMap.get("ipaddr").toString());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setWhNo(whNo);
            }
            else{
                msvo.setMatrStkNo(0L);
                msvo.setMatrNo(matrNo);
                msvo.setStkQty(0F);
                msvo.setStatCd(Long.parseLong(env.getProperty("code.stk_iwh_stat")));

                msvo.setModDt(DateUtils.getCurrentDateTime());
                msvo.setModIp(paraMap.get("ipaddr").toString());
                msvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

                msvo.setRegDt(DateUtils.getCurrentDateTime());
                msvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                msvo.setRegIp(paraMap.get("ipaddr").toString());

                msvo.setWhNo(whNo);
            }
            msvo.setCustNo(custNo);
            matrStkRepo.save(msvo);

            //????????????(make_indc_matr)??? ????????????(take_yn) ?????? --AddOn By KMJ At 21.11.22 22
            MakeIndcMatr mimvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,movo.getIndcNo(),msvo.getMatrNo(),"Y");
            if (mimvo != null) {
                mimvo.setTakeYn("Y");
                mimvo.setModDt(DateUtils.getCurrentBaseDateTime());
                mimvo.setModIp(paraMap.get("ipaddr").toString());
                mimvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                makeIndcMatrRepo.save(mimvo);
            }
            try { //AddOn By KMJ At 21.12.23 - ??????????????? ???????????? ?????? ???????????? ????????? ???????????? ?????? ??????
                List<MakeIndcMatr> mimdc = makeIndcMatrRepo.findAllByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
                List<MatrOwh>  owhdc = matrOwhRepo.findAllByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
                log.info(tag + "mimdc.size = " + mimdc.size());
                log.info(tag + "owhdc.size = " + owhdc.size());
                if (mimdc.size() == owhdc.size()) { //????????????????????? ??????????????? ????????? ???????????? ????????? ????????? ?????? (???????????????????????? ???)
                    MakeIndc mivo = makePepo.findByCustNoAndIndcNoAndUsedYn(custNo,indcNo,"Y");
                    if (mivo != null && mivo.getIndcSts() == Long.parseLong(env.getProperty("code.indcSts.reqMatrOwh"))) { //???????????????????????????
                        mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makePossible"))); //????????????
                        mivo.setModDt(DateUtils.getCurrentBaseDateTime());
                        mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                        mivo.setModIp(paraMap.get("ipaddr").toString());
                        makePepo.save(mivo);
                    }
                }
            }
            catch (NullPointerException ne) {

            }
        }
    }

    //?????? ???????????? ?????? ?????????????????? ?????? : ?????? MES???
    @Override
    public List<Map<String, Object>> getProdForIwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getProdForIwhList => ";
        log.info(tag + " paraMap = > " + paraMap.toString());
        return mapper.getProdForIwhList(paraMap);
    }

    @Override
    public int getProdForIwhListCount(Map<String, Object> paraMap) {
        return mapper.getProdForIwhListCount(paraMap);
    }

    //@Transactional
    @Override
    public void saveProdIwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.IoService.saveProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("iwhList");
        Long prodNo = 0L;
        Float stkQty = 0F;
        Long whNo = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String,Object> el : ds) {
            whNo = Long.parseLong(el.get("whNo").toString());
            prodNo= Long.parseLong(el.get("prodNo").toString());
            ProdIwh pivo = new ProdIwh();
            pivo.setProdNo(prodNo);
            pivo.setWhNo(whNo);
            pivo.setIwhQty(Float.parseFloat(el.get("iwhQty").toString()));
            try{
                pivo.setIwhDt(sdf.parse(el.get("makeDt").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pivo.setUsedYn("Y");
            pivo.setModDt(DateUtils.getCurrentDateTime());
            pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            pivo.setModIp(paraMap.get("ipaddr").toString());
            pivo.setRegDt(DateUtils.getCurrentDateTime());
            pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            pivo.setRegIp(paraMap.get("ipaddr").toString());
            try{
                pivo.setIndcRsltNo(Long.parseLong(el.get("indcRsltNo").toString()));
            }catch(NullPointerException ne){
                pivo.setIndcRsltNo(0L);
            }

            try{
                pivo.setInspEr(Long.parseLong(paraMap.get("insp").toString()));
            }catch(NullPointerException ne){
                pivo.setInspEr(0L);
            }

            try{
                pivo.setPaltCd(Long.parseLong(paraMap.get("palt").toString()));
            }catch(NullPointerException ne){
                pivo.setPaltCd(0L);
            }

            pivo.setCustNo(custNo);
            prodIwhRepo.save(pivo);

            //SOL Addon By KMJ AT 21.12.26 (???????????? ???????????? ???????????? ????????? ???)
            //JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            //long result = queryFactory.selectFrom(pr.where(person.lastName.eq("???")).fetchCount();
            el.put("custNo",custNo);
            el.put("makeDt",el.get("makeDt").toString());
            Map<String,Object> summap = mapper.getProdLotSummary(el);
            log.info(tag + "summap = " + summap.toString());
            log.info(tag + "elForLot = " + el.toString());
            ProdStkLot pslvo = new ProdStkLot();

            pslvo.setWhNo(pivo.getWhNo());
            pslvo.setProdNo(pivo.getProdNo());
            pslvo.setStkUt(DateUtils.getDateToTimeStamp(el.get("makeDt").toString()));
            pslvo.setStkQty(Float.valueOf(summap.get("lotSumQty").toString()));
            pslvo.setCustNo(custNo);
            pslvo.setUsedYn("Y");
            pslvo.setModDt(DateUtils.getCurrentBaseDateTime());
            pslvo.setModId(userId);
            pslvo.setModIp(ipaddr);


            ProdStkLot chkpslvo = prodStkLotRepo.findByCustNoAndWhNoAndProdNoAndStkUtAndUsedYn(custNo,whNo,prodNo,pslvo.getStkUt(),"Y");
            if (chkpslvo != null) {
                pslvo.setProdLotNo(chkpslvo.getProdLotNo());
                pslvo.setRegDt(chkpslvo.getRegDt());
                pslvo.setRegId(chkpslvo.getRegId());
                pslvo.setRegIp(chkpslvo.getRegIp());
            }
            else {
                pslvo.setProdLotNo(0L);
                pslvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                pslvo.setRegId(userId);
                pslvo.setRegIp(ipaddr);
            }
            prodStkLotRepo.save(pslvo);
            //EOL Addon By KMJ AT 21.12.26 (???????????? ???????????? ???????????? ????????? ???)

            ProdStk psvo = new ProdStk();
            psvo.setUsedYn("Y");
            psvo.setStkDt(DateUtils.getCurrentDate());

            //????????? ?????? ??????
            ProdStk chkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo,prodNo,"Y");
            if(chkvo != null){
                stkQty = chkvo.getStkQty();
                stkQty += Float.parseFloat(el.get("iwhQty").toString());
                psvo.setStkQty(stkQty);
                psvo.setProdNo(chkvo.getProdNo());
                psvo.setStkNo(chkvo.getStkNo());
                psvo.setModDt(DateUtils.getCurrentDateTime());
                psvo.setModIp(paraMap.get("ipaddr").toString());
                psvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                psvo.setWhNo(whNo);
            }
            else{
                psvo.setStkNo(0L);
                psvo.setProdNo(prodNo);
                psvo.setStkQty(Float.parseFloat(el.get("iwhQty").toString()));

                psvo.setModDt(DateUtils.getCurrentDateTime());
                psvo.setModIp(paraMap.get("ipaddr").toString());
                psvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

                psvo.setRegDt(DateUtils.getCurrentDateTime());
                psvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                psvo.setRegIp(paraMap.get("ipaddr").toString());

                psvo.setWhNo(whNo);
            }



            psvo.setCustNo(custNo);
            prodStkRepo.save(psvo);
        }
    }

    //?????? ???????????? ?????? ?????????????????? ?????? : ?????? MES???
    @Override
    public List<Map<String, Object>> getProdForOwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getProdForOwhList => ";
        log.info(tag + " paraMap = > " + paraMap.toString());
        return mapper.getProdForOwhList(paraMap);
    }

    @Override
    public int getProdForOwhListCount(Map<String, Object> paraMap) {
        return mapper.getProdForOwhListCount(paraMap);
    }

    @Override
    public void saveProdOwhList(Map<String, Object> paraMap) {
        String tag = "vsvc.IoService.saveProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Map<String, Object>> ds = (ArrayList<Map<String, Object>>) paraMap.get("owhList");
        Long prodNo = 0L;
        Float stkQty = 0F;
        Long whNo = 0L;
        for (Map<String,Object> el : ds) {
            ProdOwh povo = new ProdOwh();
            prodNo= Long.parseLong(el.get("prodNo").toString());
            whNo = Long.parseLong(el.get("whNo").toString());
            povo.setProdNo(prodNo);
            povo.setWhNo(whNo);
            povo.setOwhQty(Float.parseFloat(el.get("owhQty").toString()));
            povo.setOwhDt(DateUtils.getCurrentDate());
            try{
                povo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
            }catch(NullPointerException ne){
                povo.setCmpyNo(0L);
            }

            try{
                povo.setOwhReqQty(Float.parseFloat(paraMap.get("owhReqQty").toString()));
            }catch(NullPointerException ne){
                povo.setOwhReqQty(Float.parseFloat(el.get("owhQty").toString()));
            }

            try{
                povo.setOwhUnit(Long.parseLong(el.get("owhUnit").toString()));
            }catch(NullPointerException ne){
                povo.setOwhUnit(81L);
            }

            try{
                povo.setOwhReqDt(transFormat.parse(paraMap.get("owhReqDt").toString().substring(0, 10)));
            }catch(NullPointerException | ParseException ne){
                povo.setOwhReqDt(DateUtils.getCurrentDate());
            }

            try {
                povo.setOwhDt(transFormat.parse(paraMap.get("owhDt").toString().substring(0, 10)));
            } catch (NullPointerException | ParseException e) {
                povo.setOwhDt(DateUtils.getCurrentDate());
            }
            try {
                povo.setDateManufacture(transFormat.parse(paraMap.get("crcateDt").toString().substring(0, 10)));
            } catch (NullPointerException | ParseException e) {
                povo.setDateManufacture(povo.getOwhDt());
            }
            try{
                povo.setOrdNo(Long.parseLong(el.get("ordNo").toString()));
            }catch(NullPointerException ne){
                povo.setOrdNo(0L);
            }

            try{
                povo.setInspEr(Long.parseLong(paraMap.get("insp").toString()));
            }catch(NullPointerException ne){
                povo.setInspEr(0L);
            }

            try{
                povo.setPaltCd(Long.parseLong(paraMap.get("palt").toString()));
            }catch(NullPointerException ne){
                povo.setPaltCd(0L);
            }
            try{
                povo.setPaltQty(Integer.parseInt(paraMap.get("paltQty").toString()));
            }catch(NullPointerException ne){
                povo.setPaltQty(0);
            }

            povo.setUsedYn("Y");
            povo.setModDt(DateUtils.getCurrentDateTime());
            povo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            povo.setModIp(paraMap.get("ipaddr").toString());
            povo.setRegDt(DateUtils.getCurrentDateTime());
            povo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            povo.setRegIp(paraMap.get("ipaddr").toString());
            povo.setCustNo(custNo);
            prodOwhRepo.save(povo);

            ProdStk psvo = new ProdStk();
            psvo.setUsedYn("Y");
            psvo.setStkDt(DateUtils.getCurrentDate());

            ProdStk chkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,whNo,prodNo,"Y");
            if(chkvo != null){
                stkQty = chkvo.getStkQty();
                stkQty -= Float.parseFloat(el.get("owhQty").toString());
                psvo.setStkQty(stkQty);
                psvo.setProdNo(chkvo.getProdNo());
                psvo.setStkNo(chkvo.getStkNo());
                psvo.setModDt(DateUtils.getCurrentDateTime());
                psvo.setModIp(paraMap.get("ipaddr").toString());
                psvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                psvo.setWhNo(whNo);
            }
            else{
                psvo.setStkNo(0L);
                psvo.setProdNo(prodNo);
                psvo.setStkQty(Float.parseFloat(el.get("iwhQty").toString()));

                psvo.setModDt(DateUtils.getCurrentDateTime());
                psvo.setModIp(paraMap.get("ipaddr").toString());
                psvo.setModId(Long.parseLong(paraMap.get("userId").toString()));

                psvo.setRegDt(DateUtils.getCurrentDateTime());
                psvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                psvo.setRegIp(paraMap.get("ipaddr").toString());

                psvo.setWhNo(whNo);
            }
            psvo.setCustNo(custNo);
            prodStkRepo.save(psvo);
        }
    }

    //?????? ?????? ??????
    @Override
    public List<Map<String, Object>> stsMatrIwhList(HashMap<String, Object> paraMap) {
        return mapper.getStsMatrIwhList(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public int stsMatrIwhListCount(HashMap<String, Object> paraMap) {
        return mapper.getStsMatrIwhListCount(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public List<Map<String, Object>> stsMatrOwhList(HashMap<String, Object> paraMap) {
        return mapper.getStsMatrOwhList(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public int stsMatrOwhListCount(HashMap<String, Object> paraMap) {
        return mapper.getStsMatrOwhListCount(paraMap);
    }

    //?????? ?????? ??????
    @Override
    public List<Map<String, Object>> stsProdIwhList(HashMap<String, Object> paraMap) {
        return mapper.getStsProdIwhList(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public int stsProdIwhListCount(HashMap<String, Object> paraMap) {
        return mapper.getStsProdIwhListCount(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public List<Map<String, Object>> stsProdOwhList(HashMap<String, Object> paraMap) {
        return mapper.getStsProdOwhList(paraMap);
    }
    //?????? ?????? ??????
    @Override
    public int stsProdOwhListCount(HashMap<String, Object> paraMap) {
        return mapper.getStsProdOwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwhWhNm(HashMap<String, Object> paraMap) {
        String tag = "ioService.getMatrOwhWhNm => ";
        log.info(tag + " paraMap = " + paraMap.toString());
        return mapper.getMatrOwhWhNm(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwhList(HashMap<String, Object> paraMap) {
        return mapper.getMatrOwhList(paraMap);
    }

    @Override
    public int getMatrOwhListCount(HashMap<String, Object> paraMap) {
        return mapper.getMatrOwhListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdOwHstrList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getProdOwHstrList=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getProdOwHstrList(paraMap);

    }

    @Override
    public int getProdOwHstrListCount(Map<String, Object> paraMap) {
        return mapper.getProdOwHstrListCount(paraMap);

    }

    @Override
    public Map<String, Object> getOwhInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getOwhInfo=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getOwhInfo(paraMap);
    }

    @Override
    public void dropOwhInfo(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.dropOwhInfo=>";
        log.info(tag + "paramap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long owhNo = Long.parseLong(paraMap.get("owhNo").toString());
        ProdOwh vo = prodOwhRepo.findByCustNoAndOwhNoAndUsedYn(custNo,owhNo,"Y");
        if (vo != null) {
            //???????????? ??????
            vo.setUsedYn("N");
            vo.setModDt(DateUtils.getCurrentBaseDateTime());
            vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            vo.setModIp(paraMap.get("ipaddr").toString());
            vo.setCustNo(custNo);
            prodOwhRepo.save(vo);

            //????????? ???????????? ???????????? ??????
            ProdStk stkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,vo.getWhNo(),vo.getProdNo(),"Y");
            if (stkvo != null) {
                stkvo.setStatTrfDt(DateUtils.getCurrentDateTime());
                stkvo.setModIp(vo.getModIp());
                stkvo.setModId(vo.getModId());
                stkvo.setModDt(DateUtils.getCurrentDate());
                stkvo.setStkQty(stkvo.getStkQty() + vo.getOwhQty());
                stkvo.setCustNo(custNo);
                prodStkRepo.save(stkvo);
            }
            //??????????????? ??????????????? ??????????????? ??????
            OrdInfo ordvo = ordRepository.findByCustNoAndOrdNoAndUsedYn(custNo,vo.getOrdNo(),"Y");
            if (ordvo != null) {
                ordvo.setModDt(DateUtils.getCurrentBaseDateTime());
                ordvo.setModIp(vo.getModIp());
                ordvo.setModId(vo.getModId());
                ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status_acpt"))); //????????????
            }
        }
    }

    @Override
    public List<Map<String, Object>> matrWhNm(HashMap<String, Object> paraMap) {
        return mapper.getMatrWhNm(paraMap);
    }

    @Override
    public int matrWhNmCount(HashMap<String, Object> paraMap) {
        return mapper.getmatrWhNmCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrWhStkList(Map<String, Object> paraMap) {
        return mapper.getMatrWhStkList(paraMap);
    }

    @Override
    public int getMatrWhStkListCount(Map<String, Object> paraMap) {
        return mapper.getMatrWhStkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getIwhMatrList(HashMap<String, Object> paraMap) {
        return mapper.getIwhMatrList(paraMap);
    }

    @Override
    public int getIwhMatrListCount(HashMap<String, Object> paraMap) {
        return mapper.getIwhMatrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> prodWhNm(HashMap<String, Object> paraMap) {
        return mapper.getProdWhNm(paraMap);
    }

    @Override
    public int prodWhNmCount(HashMap<String, Object> paraMap) {
        return mapper.getProdWhNmCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getProdWhStkList(Map<String, Object> paraMap) {
        return mapper.getProdWhStkList(paraMap);
    }

    @Override
    public int getProdWhStkListCount(Map<String, Object> paraMap) {
        return mapper.getProdWhStkListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getIwhProdList(HashMap<String, Object> paraMap) {
        return mapper.getIwhProdList(paraMap);
    }

    @Override
    public int getIwhProdListCount(HashMap<String, Object> paraMap) {
        return mapper.getIwhProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOwhProdList(HashMap<String, Object> paraMap) {
        return mapper.getOwhProdList(paraMap);
    }

    @Override
    public int getOwhProdListCount(HashMap<String, Object> paraMap) {
        return mapper.getOwhProdListCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getProdIwHstrList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.prodIwHstrList=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getProdIwHstrList(paraMap);
    }

    @Override
    public int getProdIwHstrListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getProdIwHstrListCount=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getProdIwHstrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrIwHstrList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getMatrIwHstrList=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getMatrIwHstrList(paraMap);
    }

    @Override
    public int getMatrIwHstrListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getMatrIwHstrListCount=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getMatrIwHstrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMatrOwHstrList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getMatrOwHstrList=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getMatrOwHstrList(paraMap);
    }

    @Override
    public int getMatrOwHstrListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getMatrOwHstrListCount=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getMatrOwHstrListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getDaedongReqProdIwhSumList(Map<String, Object> paraMap) {
        return mapper.getDaedongReqProdIwhSumList(paraMap);
    }

    @Override
    public int getDaedongReqProdIwhSumListCount(Map<String, Object> paraMap) {
        return mapper.getDaedongReqProdIwhSumListCount(paraMap);
    }

    @Transactional
    @Override
    public void getOwhHstr(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> ds = mapper.getOrdQty(paraMap);
        for (Map<String,Object> el : ds) {
            ProdOwh owvo = new ProdOwh();
            owvo.setOwhQty(Float.parseFloat(el.get("ord_qty").toString())); // ????????????
            try {
                owvo.setOwhDt(sdf.parse(el.get("ord_dt").toString().substring(0, 10))); //????????????
                owvo.setOwhReqDt(sdf.parse(el.get("ord_dt").toString().substring(0, 10)));
                owvo.setModDt(sdf.parse(el.get("ord_dt").toString().substring(0, 10)));
                owvo.setRegDt(sdf.parse(el.get("ord_dt").toString().substring(0, 10)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            owvo.setOwhUnit(84L); //KG
            owvo.setOrdNo(Long.parseLong(el.get("ord_no").toString())); // ????????????
            owvo.setOwhReqQty(Float.parseFloat(el.get("ord_qty").toString()));
            owvo.setProdNo(Long.parseLong(el.get("prod_no").toString()));
            owvo.setWhNo(Long.parseLong(el.get("wh_no").toString()));

            owvo.setModIp(paraMap.get("ipaddr").toString());
            owvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            owvo.setRegIp(paraMap.get("ipaddr").toString());
            owvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));

            owvo.setUsedYn("Y");

            double min = 1;
            double max = 268;
            double random = (int) ((Math.random() * (max - min)) + min);

             Map<String,Object> ran = new HashMap<String,Object>();
             ran.put("random", random);

            Map<String,Object> rmap = mapper.getOrdQtyRandom(ran);

            owvo.setCmpyNo(Long.parseLong(rmap.get("cmpy_no").toString()));
            owvo.setCustNo(custNo);
            prodOwhRepo.save(owvo);
        }
    }

    @Override
    public List<Map<String, Object>> getOwhMatrList(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getOwhMatrList=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getOwhMatrList(paraMap);
    }

    @Override
    public int getOwhMatrListCount(Map<String, Object> paraMap) {
        String tag = "vsvc.ioService.getOwhMatrListCount=>";
        log.info(tag + "paramap = " + paraMap.toString());
        return mapper.getOwhMatrListCount(paraMap);
    }

    @Transactional
    @Override
    public void changeStkData(Map<String, Object> paraMap) {
        String tag = "vsvc.IoService.changeStkData => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        mapper.changeStkData(paraMap);
        mapper.changeTotalStkData(paraMap);
    }


    @Transactional
    @Override
    public void dropStkData(Map<String, Object> paraMap) {
        String pageType = paraMap.get("pageType").toString();
        String type = paraMap.get("type").toString();
        Long  custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long  itemNo = Long.parseLong(paraMap.get("changeNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());

        String ipaddr = paraMap.get("ipaddr").toString();
        MatrStk msvo = null;
        ProdStk psvo = null;

        if (pageType.equals("matr")) {
            if (type.equals("iwh")){
                MatrIwh mivo = matrIwhRepo.findByCustNoAndIwhNoAndUsedYn(custNo, itemNo, "Y");
                if (mivo != null) {
                    //?????????????????? ???????????? ??????????????? ???????????? ?????? adjust
                    msvo = matrStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo, mivo.getMatrNo(), "Y");
                    msvo.setStkQty(msvo.getStkQty() - mivo.getIwhQty());
                    msvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    msvo.setModId(userId);
                    msvo.setModIp(ipaddr);
                    matrStkRepo.save(msvo);
                }
                //????????? ??????????????? ?????? adjust
                mivo.setUsedYn("N");
                matrIwhRepo.save(mivo);
            }
            else{
                MatrOwh movo = matrOwhRepo.findByCustNoAndOwhNoAndUsedYn(custNo, itemNo, "Y");
                if (movo != null) {
                    //?????????????????? ???????????? ??????????????? ???????????? ?????? adjust
                    msvo = matrStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo, movo.getMatrNo(), "Y");
                    msvo.setStkQty(msvo.getStkQty() + movo.getOwhQty());
                    msvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    msvo.setModId(userId);
                    msvo.setModIp(ipaddr);
                    matrStkRepo.save(msvo);
                }
                //????????? ??????????????? ?????? adjust
                movo.setUsedYn("N");
                matrOwhRepo.save(movo);
            }

        }
        else if (pageType.equals("prod")) {
            if (type.equals("iwh")){
                ProdIwh pivo = prodIwhRepo.findByCustNoAndIwhNoAndUsedYn(custNo, itemNo, "Y");
                if (pivo != null) {
                    //?????????????????? ???????????? ??????????????? ???????????? ?????? adjust
                    psvo = prodStkRepo.findByCustNoAndProdNoAndUsedYn(custNo, pivo.getProdNo(), "Y");
                    psvo.setStkQty(psvo.getStkQty() - pivo.getIwhQty());
                    psvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    psvo.setModId(userId);
                    psvo.setModId(userId);
                    prodStkRepo.save(psvo);
                }
                //????????? ??????????????? ?????? adjust
                pivo.setUsedYn("N");
                prodIwhRepo.save(pivo);
            }else{
                ProdOwh povo = prodOwhRepo.findByCustNoAndOwhNoAndUsedYn(custNo, itemNo, "Y");
                if (povo != null) {
                    //?????????????????? ???????????? ??????????????? ???????????? ?????? adjust
                    psvo = prodStkRepo.findByCustNoAndProdNoAndUsedYn(custNo, povo.getProdNo(), "Y");
                    psvo.setStkQty(psvo.getStkQty() + povo.getOwhQty());
                    psvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    psvo.setModId(userId);
                    psvo.setModId(userId);
                    prodStkRepo.save(psvo);
                }
                //????????? ??????????????? ?????? adjust
                povo.setUsedYn("N");
                prodOwhRepo.save(povo);
            }
        }
    }

    @Transactional
    @Override
    public void dropTotalStkData(Map<String, Object> paraMap) {

    }

    @Transactional
    @Override
    public void dropProdIwhList(Map<String, Object> paraMap) {
        String tag = "ioService.dropProdIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        //{prodList=[{indcYn=Y, prodNo=378589, indcRsltNo=413858, whNo=1, whNm=[??????-1]???????????????, makeDt=2021-10-19T15:00:00.000+00:00, makeQty=300, indcNo=394042, stkQty=20, prodNm=6????????????????????????(30???), vgt_id=0, originalIndex=0, vgtSelected=true}, {indcYn=Y, prodNo=355361, indcRsltNo=413854, whNo=2, whNm=[??????-2]???????????????, makeDt=2021-11-23T15:00:00.000+00:00, makeQty=29700, indcNo=404665, stkQty=0, prodNm=6?????????????????????365??????/???????????????, vgt_id=0, originalIndex=1, vgtSelected=true}], userId=2, custNo=6, ipaddr=127.0.0.1, procYn=Y}


        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("prodList");
        for (Map<String, Object> el : ds) {

            MakeIndcRslt chkvo = makeIndcRsltRepo.findByIndcRsltNo(Long.parseLong(el.get("indcRsltNo").toString()));
            chkvo.setUsedYn("N");
            makeIndcRsltRepo.save(chkvo);
//            mirvo.setCustNo(custNo);
//            mirvo.setIndcNo(Long.parseLong(el.get("indcNo").toString()));
//            mirvo.setIndcRsltNo(Long.parseLong(el.get("indcRsltNo").toString()));
//            mirvo.setUsedYn("N");
//            mirvo.setModId(userId);
//            mirvo.setModDt(DateUtils.getCurrentBaseDateTime());
//            mirvo.setModIp(ipaddr);

            /*????????????
            psvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(psvo.getCustNo(), psvo.getWhNo(), psvo.getProdNo(), "Y");
            if (psvo != null) {
                psvo.setStatTrfDt(DateUtils.getCurrentDate());
                psvo.setStkQty(psvo.getStkQty() - stkQty);
                psvo.setModDt(DateUtils.getCurrentDate());
                psvo.setModIp(ipaddr);
                psvo.setModId(userId);
                prodStkRepo.save(psvo);
            }
            */

        }
    }

    @Override
    public List<Map<String, Object>> getMadeProdForIwhList(Map<String, Object> paraMap) {
        String tag = "ioService.getMadeProdForIwhList =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getMadeProdForIwhList(paraMap);
    }

    @Override
    public int getMadeProdForIwhListCount(Map<String, Object> paraMap) {
        return mapper.getMadeProdForIwhListCount(paraMap);
    }

    @Override
    public void dropMatrIwhList(Map<String, Object> paraMap) {
        String tag = "ioSErvice.dropMatrIwhList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        String procYn = paraMap.get("procYn").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        //paraPam = {matrList=[{indcYn=Y, needQty=1, whNo=560, whNm=????????????, takeYn=N, pursUnit=51, makeFrDt=2021-10-12, matrNm=???????????????, indcNo=1008820, stkQty=10, matrNo=697847, vgt_id=0, originalIndex=66, vgtSelected=true}]
        if (procYn.equals("Y")) { //???????????? ???????????? ????????? ?????????.
            List<Map<String, Object>> dsMap = (List<Map<String, Object>>) paraMap.get("matrList");
            for (Map<String, Object> el : dsMap) {
                MakeIndcMatr mimvo = new MakeIndcMatr();
                mimvo.setCustNo(custNo);
                mimvo.setIndcNo(Long.parseLong(el.get("indcNo").toString()));
                mimvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
                MakeIndcMatr vo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo, mimvo.getIndcNo(), mimvo.getMatrNo(),"Y");
                if (vo != null) {
                    vo.setUsedYn("N");
                    vo.setModDt(DateUtils.getCurrentBaseDateTime());
                    vo.setModIp(ipaddr);
                    vo.setModId(userId);
                    makeIndcMatrRepo.save(vo);
                }
                List<MakeIndcMatr> dsvo = makeIndcMatrRepo.findAllByCustNoAndIndcNoAndUsedYn(custNo, mimvo.getIndcNo(), "Y");
                if (dsvo.size() == 0) { //????????? ???????????? ??????????????? ?????? ????????? ?????? ???????????? ????????? ??????
                    MakeIndc mivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,vo.getIndcNo(),"Y");
                    if (mivo != null) {
                        mivo.setUsedYn("N");
                        mivo.setModDt(DateUtils.getCurrentBaseDateTime());
                        mivo.setModIp(ipaddr);
                        mivo.setModId(userId);
                        makeIndcRepo.save(mivo);
                    }
                }
            }
        }
    }


}


