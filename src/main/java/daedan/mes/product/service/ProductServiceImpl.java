package daedan.mes.product.service;

import daedan.mes.cmpy.domain.CmpyDlvPlc;
import daedan.mes.cmpy.repository.CmpyDlvPlcRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcMatr;
import daedan.mes.make.repository.MakeIndcMatrRepository;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.domain.ProcInfo;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.proc.repository.ProcInfoRepository;
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.product.domain.ProductIndc;
import daedan.mes.product.domain.ProductPlan;
import daedan.mes.product.mapper.ProductMapper;
import daedan.mes.product.repository.ProductIndcRepository;
import daedan.mes.product.repository.ProductPlanRepositry;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.user.domain.CustInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductPlanRepositry productPlanRepo;

    @Autowired
    private ProductIndcRepository productIndcRepo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private CmpyDlvPlcRepository cmpyDlvPlcRepo;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private ProcInfoRepository procInfoRepo;

    @Autowired
    private MakeIndcRepository makeIndcRepo;
    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;

    @Autowired
    private ProdBomRepository prodBomRepo;



    @Override
    @Transactional
    public void saveProductPlan(Map<String, Object> paraMap) {
        String tag = "ProductService.saveProductPlan => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
          { cmpyNo=495424
           , prodNo=495428
           , userId=14
           , custNo=14
           , ipaddr=127.0.0.1
           , DateList=[2021-12-12, 2021-12-13, 2021-12-14, 2021-12-15, 2021-12-16, 2021-12-17, 2021-12-18, 2021-12-19, 2021-12-20, 2021-12-21, 2021-12-22]
           , ordRecList=[499498, 499499, 0, 0, 0, 0, 0, 0, 0, 0, 0],
           , planQtyList=[300, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0]
         }
         */
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        List<Long> dsOrdRecNo = (List<Long>) paraMap.get("ordRecList");
        List<String> dsDateList = (List<String>) paraMap.get("dateList");
        List<Integer> dsQtyList = (List<Integer>) paraMap.get("planQtyList");

        for (int ix = 0; ix < dsDateList.size(); ix++) {
            int planQty = Integer.parseInt(String.valueOf(dsQtyList.get(ix)));
            if (planQty == 0) continue;
            ProductPlan mpvo = new ProductPlan();

            mpvo.setCustNo(custNo);
            mpvo.setCmpyNo(cmpyNo);
            mpvo.setProdNo(prodNo);
            mpvo.setOrdRecvNo(Long.parseLong(String.valueOf(dsOrdRecNo.get(ix))));
            mpvo.setPlanQty(planQty);
            mpvo.setUsedYn("Y");
            try {
                Date shd = sdf.parse(dsDateList.get(ix));
                mpvo.setPlanUt(shd.getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
            mpvo.setStatCd(Long.parseLong(env.getProperty("code.make_plan_plan")));
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(userId);
            mpvo.setModIp(ipaddr);
            ProductPlan chkvo = productPlanRepo.findByCustNoAndPlanUtAndProdNoAndUsedYn(custNo, mpvo.getPlanUt(), prodNo, "Y");
            if (chkvo != null) {
                mpvo.setProductPlanNo(chkvo.getProductPlanNo());
                mpvo.setOrdRecvNo(chkvo.getOrdRecvNo());
                mpvo.setRegDt(chkvo.getRegDt());
                mpvo.setRegId(chkvo.getRegId());
                mpvo.setRegIp(chkvo.getRegIp());
            } else {
                mpvo.setProductPlanNo(0L);
                mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpvo.setRegId(userId);
                mpvo.setRegIp(ipaddr);
            }
            productPlanRepo.save(mpvo);
        }
    }


    /*???????????? ??????????????? ?????? ???????????? ?????? ??????*/
    @Override
    public void makeProductPlan(Map<String, Object> paraMap) {
        String tag = "ProductService.makeProductPlan=> ";
        log.info(tag + "paraMap =  " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
          { cmpyNo=495424
           , prodNo=495428
           , userId=14
           , custNo=14
           , ipaddr=127.0.0.1
           , DateList=[2021-12-12, 2021-12-13, 2021-12-14, 2021-12-15, 2021-12-16, 2021-12-17, 2021-12-18, 2021-12-19, 2021-12-20, 2021-12-21, 2021-12-22]
           , ordRecList=[499498, 499499, 0, 0, 0, 0, 0, 0, 0, 0, 0],
           , planQtyList=[300, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0]
         }
         */
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        List<Long> dsOrdRecNo = (List<Long>) paraMap.get("ordRecList");
        List<String> dsDateList = (List<String>) paraMap.get("dateList");
        List<Integer> dsQtyList = (List<Integer>) paraMap.get("planQtyList");

        for (int ix = 0; ix < dsDateList.size(); ix++) {
            int planQty = Integer.parseInt(String.valueOf(dsQtyList.get(ix)));
            if (planQty == 0) continue;
            ProductPlan mpvo = new ProductPlan();

            mpvo.setCustNo(custNo);
            mpvo.setCmpyNo(cmpyNo);
            mpvo.setProdNo(prodNo);
            mpvo.setOrdRecvNo(Long.parseLong(String.valueOf(dsOrdRecNo.get(ix))));
            mpvo.setPlanQty(planQty);
            mpvo.setUsedYn("Y");
            try {
                Date shd = sdf.parse(dsDateList.get(ix));
                mpvo.setPlanUt(shd.getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }
            mpvo.setStatCd(Long.parseLong(env.getProperty("code.make_plan_plan")));
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(userId);
            mpvo.setModIp(ipaddr);
            ProductPlan chkvo = productPlanRepo.findByCustNoAndPlanUtAndProdNoAndUsedYn(custNo, mpvo.getPlanUt(), prodNo, "Y");
            if (chkvo != null) {
                mpvo.setProductPlanNo(chkvo.getProductPlanNo());
                mpvo.setOrdRecvNo(chkvo.getOrdRecvNo());
                mpvo.setRegDt(chkvo.getRegDt());
                mpvo.setRegId(chkvo.getRegId());
                mpvo.setRegIp(chkvo.getRegIp());
            } else {
                mpvo.setProductPlanNo(0L);
                mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpvo.setRegId(userId);
                mpvo.setRegIp(ipaddr);
            }
            productPlanRepo.save(mpvo);
        }
    }

    @Override
    public List<Map<String, Object>> getProductPlanList(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductPlanList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProductPlanList(paraMap);
    }

    @Override
    public Map<String, Object> getProductPlanInfo(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductPlanInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long prodcustPlanNo = Long.parseLong(paraMap.get("prodcutPlanN").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        ProductIndc vo = productPlanRepo.findByCustNoAndProductPlanNoAndUsedYn(custNo,prodcustPlanNo,"Y");
        return StringUtil.voToMap(vo);
    }

    @Override
    public List<Map<String, Object>> getProductIndcList(Map<String, Object> paraMap) {
        String tag = " ProductService.getProductIndcList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProductIndcList(paraMap);
    }

    @Override
    public void saveProductIndc(Map<String, Object> paraMap) {
        String tag = "ProductService.saveProductIndc => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        /*
        paraMap = {  cmpyNo=495424
                   , prodNo=495428
                   , indcQty=200
                   , productPlanNo=525107
                   , workFrDt=2021-12-01T15:00:00.000Z
                   , workToDt=2021-12-02T15:00:00.000Z
                   , indcCont=<p>????????? ??? ?????????.</p>
                   , userId=14
                   , custNo=14
                   , ipaddr=127.0.0.1
                   }
         */
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long cmpyNo = Long.parseLong(paraMap.get("cmpyNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        ProductIndc pivo = new ProductIndc();

        pivo.setCustNo(custNo);
        pivo.setProductPlanNo(Long.parseLong(paraMap.get("productPlanNo").toString()));
        pivo.setCmpyNo(cmpyNo);
        pivo.setProdNo(prodNo);
        pivo.setMakeFrUt(DateUtils.getDateToTimeStamp(paraMap.get("workFrDt").toString().substring(0,10)));
        pivo.setMakeToUt(DateUtils.getDateToTimeStamp(paraMap.get("workToDt").toString().substring(0,10)));
        pivo.setUsedYn("Y");
        pivo.setCustNo(custNo);
        pivo.setModDt(DateUtils.getCurrentBaseDateTime());
        pivo.setModIp(ipaddr);
        pivo.setModId(userId);
        pivo.setIndcQty(Float.valueOf(paraMap.get("indcQty").toString()));
        try {
            pivo.setProductPlanNo(Long.parseLong(paraMap.get("productPlanNo").toString()));
        }
        catch(NullPointerException ne) {
            pivo.setProductIndcNo(0L);
        }
        pivo.setStatCd(Long.parseLong(env.getProperty("code.workst.proc")));
        ProductIndc chkvo = productIndcRepo.findByCustNoAndMakeFrUtAndProdNoAndUsedYn(custNo, pivo.getMakeFrUt(), prodNo, "Y");
        if (chkvo != null) {
            pivo.setProductIndcNo(chkvo.getProductIndcNo());
            pivo.setRegDt(chkvo.getRegDt());
            pivo.setRegId(chkvo.getRegId());
            pivo.setRegIp(chkvo.getRegIp());
        } else {
            pivo.setProductIndcNo(0L);
            pivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            pivo.setRegId(userId);
            pivo.setRegIp(ipaddr);
        }
        productIndcRepo.save(pivo);
    }

    @Override
    public List<Map<String, Object>> getPursMatrListByIndc(Map<String, Object> paraMap) {
        String tag = "ProductService.chkStkByIndc => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getPursMatrListByIndc(paraMap);
    }


    @Transactional
    @Override
    public Long expectMakeIndc(Map<String, Object> paraMap) {
        String tag = "hdfdService.expectMakeIndc ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());

      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        CustInfo cuvo = (CustInfo) paraMap.get("custInfo");
        Map<String, Object> procMap = new HashMap<String, Object>();
        procMap.put("prodNo", prodNo);
        procMap.put("custNo", custNo);


        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;
        Long ordNo = 0L;

        ordNo = this.makeOrder(paraMap); //???????????? ??????
        log.info("??????????????? ???????????? : " + ordNo);
        int idx = 0;
        paraMap.put("ordNo", ordNo);
        paraMap.put("indcNo",this.makeIndcList(paraMap));//????????? ???????????? ??????
        paraMap.put("prodNo",prodNo);//????????? ???????????? ??????
        this.makeIndcMatrList(paraMap); //rootIndc??? ??????????????????
        if (cuvo.getPursYn().equals("Y")) {
            this.makePursList(paraMap); //BOM??? ????????? ??????????????? ??????????????? ????????????
        }

        if (cuvo.getCustNo() == 8) { //????????????
            Map<String, Object> indcRslt = new HashMap<>();
            indcRslt.put("ordNo", ordNo);
            indcRslt.put("prodNo", paraMap.get("prodNo"));
            indcRslt.put("indcNo", parIndcNo);
            indcRslt.put("makeDt", paraMap.get("makeDt"));
            indcRslt.put("procCd", 0);
            indcRslt.put("makeWgt", paraMap.get("indcQty"));
            indcRslt.put("procUnitNm", "EA");
            paraMap.put("indcRslt", indcRslt);
            log.info("paraMap = > " + paraMap);
            //this.saveIndcRslt(paraMap); kmjkmj
        }

        return svParIndcNo;
    }

    /*???????????? ?????? ????????? ?????? ??? ?????? ?????? ???, ???????????? ??????.*/
    private Long makeOrder(Map<String, Object> paraMap) {
        String tag = "HdfdService.makeOrder => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        log.info(tag + "???????????? ??????????????? ???????????? ?????? => " + paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        OrdInfo oivo = new OrdInfo();
        Long ordNo = 0L;
        try {
            ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        } catch (NullPointerException ne) {
            ordNo = 0L;
        }
        Long oemNo = Long.parseLong(env.getProperty("ord_oem"));
        Long odmNo = Long.parseLong(env.getProperty("ord_odm"));
        oivo.setOrdNm(paraMap.get("prodNm").toString());
        oivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_acpt"))); //????????????
        ProdInfo pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if (pivo != null) {
            oivo.setOrdTp( (pivo.getCmpyNo() > 0) ? oemNo : odmNo);
            List<CmpyDlvPlc> ds = cmpyDlvPlcRepo.findAllByCustNoAndCmpyNoAndUsedYn(custNo,pivo.getCmpyNo(),"Y");
            if (ds.size() > 0) {
                oivo.setPlcNo(ds.get(0).getPlcNo());
            }
            else {
                oivo.setPlcNo(0L);
            }
        }
        oivo.setUsedYn("Y");

        try {
            oivo.setOrdDt(sdf.parse(paraMap.get("makeToDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            oivo.setOrdDt(DateUtils.getCurrentDate());
        }

        try {
            oivo.setDlvReqDt(sdf.parse(paraMap.get("dlvReqDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            oivo.setDlvReqDt(DateUtils.getLastDateOfMonth(oivo.getOrdDt()));
        }
        if (oivo.getOrdTp() == oemNo) {
            oivo.setCmpyNo(pivo.getCmpyNo());
        }
        else{
            try {
                oivo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
            } catch (NullPointerException ne) {
                oivo.setCmpyNo(0L);
            }
        }
        oivo.setModDt(DateUtils.getCurrentDate());
        oivo.setModIp(paraMap.get("ipaddr").toString());
        oivo.setModId(userId);

        OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo, ordNo, "Y");
        //Map<String, Object> tmpMap = new HashMap<String, Object>();
        //tmpMap.put("custNo", custNo);
        //tmpMap.put("ordNo", ordNo);
        if (chkvo != null) {
            oivo.setOrdNo(chkvo.getOrdNo());
            oivo.setRegDt(chkvo.getRegDt());
            oivo.setRegIp(chkvo.getRegIp());
            oivo.setRegId(chkvo.getRegId());
        }
        else {
            oivo.setOrdNo(0L);
            oivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            oivo.setRegIp(ipaddr);
            oivo.setRegId(userId);
        }
        oivo.setCustNo(custNo);
        oivo.setUsedYn("Y");
        oivo = ordRepo.save(oivo);

        OrdProd opvo = new OrdProd();
        opvo.setOrdNo(oivo.getOrdNo());
        opvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        opvo.setQtyPerPkg(pivo.getQtyPerPkg());
        opvo.setOrdSz(pivo.getSz());
        opvo.setSaleUnit(pivo.getSaleUnit());
        opvo.setOrdModlNm(pivo.getModlNm());
        opvo.setUsedYn("Y");
        opvo.setModDt(DateUtils.getCurrentBaseDateTime());
        opvo.setModId(userId);
        opvo.setModIp(ipaddr);
        try {
            opvo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
        } catch (NullPointerException ne) {
            opvo.setPursNo(0L);
        }
        Float indcWgt = Float.valueOf(paraMap.get("indcWgt").toString());
        opvo.setOrdQty((pivo.getVol() > 0) ? indcWgt / pivo.getVol() : indcWgt);

        try {
            opvo.setSaleUnit(Long.parseLong(paraMap.get("makeUnit").toString()));
        } catch (NullPointerException ne) {
            opvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
        }
        opvo.setRegDt(DateUtils.getCurrentDate());
        opvo.setRegIp(paraMap.get("ipaddr").toString());
        opvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));

        OrdProd chkpvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo, oivo.getOrdNo(), Long.parseLong(paraMap.get("prodNo").toString()), "Y");
        if (chkpvo != null) {
            opvo.setOrdProdNo(chkpvo.getOrdProdNo());
            opvo.setRegDt(chkpvo.getRegDt());
            opvo.setRegId(chkpvo.getRegId());
            opvo.setRegIp(chkpvo.getRegIp());
        }
        else {
            opvo.setOrdProdNo(0L);
            opvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            opvo.setRegId(userId);
            opvo.setRegIp(ipaddr);
        }
        opvo.setCustNo(custNo);
        opvo.setUsedYn("Y");
        ordProdRepo.save(opvo);
        return oivo.getOrdNo();
    }

    private Long makeIndcList(Map<String,Object> paraMap) {
        String tag = "HdfdService.makeIndcList => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long ordNo  = Long.parseLong(paraMap.get("ordNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Date makeFrDt = (Date) paraMap.get("makeFrDt");
        Long indcTpOem = Long.parseLong(env.getProperty("code.indcTp.oem"));
        Long indcTpOdm = Long.parseLong(env.getProperty("code.indcTp.odm"));
        Long rootIndcNo = 0L;

        Date indcDt = (Date) paraMap.get("indcDt");
        String ipaddr = paraMap.get("ipaddr").toString();

        log.info(tag + "???????????? ??????????????? ???????????? ?????? => " + paraMap);
        ProdInfo pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        int idx = 0;
        if (pivo != null) {
            List<ProcBrnch> ds = procBrnchRepo.findAllByCustNoAndBrnchNoAndUsedYnOrderByProcSeq(custNo, pivo.getBrnchNo(), "Y"); //????????????????????? ?????????????????? ??????

            for (ProcBrnch el : ds) { //????????? ????????? ??????????????? ?????????????????? (???:??????,??????,?????????,????????????,?????????,???????????????)
                MakeIndc mivo = new MakeIndc();
                mivo.setParIndcNo((idx == 0) ? 0L : indcNo);
                mivo.setIndcDt(indcDt);
                mivo.setProdNo(prodNo);
                ProcInfo procvo = procInfoRepo.findByCustNoAndProcCdAndUsedYn(custNo, el.getProcCd(), "Y");
                if (procvo != null) {
                    mivo.setMakeFrDt(makeFrDt);
                    mivo.setMakeToDt(DateUtils.addDate(makeFrDt, procvo.getNeedHm()));
                    makeFrDt = mivo.getMakeToDt();
                } else {
                    mivo.setMakeFrDt(makeFrDt);
                    mivo.setMakeToDt(makeFrDt);
                }
                Float indcWgt = Float.valueOf(paraMap.get("indcWgt").toString());
                mivo.setIndcWgt(indcWgt);
                mivo.setIndcQty((pivo.getVol() > 0) ? indcWgt / pivo.getVol() : indcWgt);
                mivo.setMakeUnit(procvo.getProcUnit());
                mivo.setOrdNo(ordNo);
                mivo.setProcCd(el.getProcCd());
                mivo.setClosYn("N");
                mivo.setUsedYn("Y");
                mivo.setCustNo(custNo);
                mivo.setStatCd(501L); //????????????
                mivo.setIndcTp(pivo.getCmpyNo() > 0 ? indcTpOem : indcTpOdm);
                mivo.setModDt(DateUtils.getCurrentBaseDateTime());
                mivo.setModId(userId);
                mivo.setModIp(ipaddr);
                MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndProcCdAndUsedYn(custNo, mivo.getParIndcNo(), mivo.getProcCd(), "Y");
                if (chkvo != null) {
                    mivo.setIndcNo(chkvo.getIndcNo());
                    mivo.setRegDt(chkvo.getRegDt());
                    mivo.setRegId(chkvo.getRegId());
                    mivo.setRegIp(chkvo.getRegIp());
                } else {
                    mivo.setIndcNo(0L);
                    mivo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    mivo.setRegId(userId);
                    mivo.setRegIp(ipaddr);
                }
                mivo = makeIndcRepo.save((mivo));
                if (idx == 0) {
                    rootIndcNo = mivo.getIndcNo();
                }
            }
        }
        return rootIndcNo;
    }
    private void makeIndcMatrList(Map<String,Object> paraMap) {
        String tag = "HdfdService.maekIndcMatrList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long ordNo  = Long.parseLong(paraMap.get("ordNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("custNo").toString());
        Float indcWgt = Float.parseFloat(paraMap.get("indcWeg").toString());

        String ipaddr = paraMap.get("userId").toString();
        Long cmpyNo = 0L;

        ProdInfo pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if (pivo != null) {
            cmpyNo = pivo.getCmpyNo();
            ProdBom bomvo = new ProdBom();
            bomvo.setProdNo(prodNo);
            bomvo.setCustNo(custNo);
            List<ProdBom> ds = prodBomRepo.findAllByCustNoAndProdNoAndUsedYn(custNo, prodNo, "Y");
            log.info(tag + "3.???????????? ???????????? ?????????????????? ?????? (?????????????????? BOM ???????????????)===> " + ds.size());
            int idx = 0;
            for (ProdBom el : ds) {
                ++idx;
                MakeIndcMatr mimvo = new MakeIndcMatr();
                mimvo.setIndcNo(indcNo);
                mimvo.setMatrNo(el.getMatrNo());
                mimvo.setNeedQty(el.getNeedQty() * indcWgt);
                mimvo.setMatrSts("N");
                mimvo.setTakeYn("N");
                mimvo.setUsedYn("Y");
                mimvo.setCustNo(custNo);
                ;
                mimvo.setModDt(DateUtils.getCurrentBaseDateTime());
                mimvo.setModIp(ipaddr);
                mimvo.setModId(userId);
                MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo, mimvo.getIndcNo(), mimvo.getMatrNo(), "Y");
                if (chkvo != null) {
                    mimvo.setIndcMatrNo(chkvo.getIndcMatrNo());
                    mimvo.setRegDt(chkvo.getRegDt());
                    mimvo.setRegIp(chkvo.getRegIp());
                    mimvo.setRegIp(chkvo.getRegIp());
                } else {
                    mimvo.setIndcMatrNo(0L);
                    mimvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    mimvo.setRegIp(ipaddr);
                    mimvo.setRegId(userId);
                }
                makeIndcMatrRepo.save(mimvo);
                log.info("??????????????? ???????????? ?????? ?????? : " + el);
            }
        }
    }
    private void makePursList(Map<String, Object> paraMap) {
        String tag = "HdfdService.makePursList => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        Long rootIndcNo = Long.parseLong(paraMap.get("indcNo").toString());

        boolean chk = true;

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("custNo", custNo);
        datas.put("indcNo", rootIndcNo);
        List<Map<String, Object>> dl = mapper.getPursMatrListByIndc(datas); // ?????? ??????
        for (Map<String, Object> es : dl) {
            if (Float.parseFloat(es.get("reqPursQty").toString()) < 0) { //???????????? < ??????????????? ??????
                chk = false;
                break;
            }
        }
        MakeIndc inchk = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,rootIndcNo,"Y");
        if (inchk != null) {
            inchk.setIndcSts((!chk) ? 2401L : 2402L);
            inchk.setCustNo(custNo);
            inchk.setModDt(DateUtils.getCurrentBaseDateTime());
            inchk.setModId(userId);
            inchk.setModIp(ipaddr);
            makeIndcRepo.save(inchk);
        }
        try {
            log.info(tag + "?????????????????? ????????????(purs_info) , ????????????(purs_matr) ??? ????????????(matr_pos) ?????? ?????? / ?????????????????? = " + rootIndcNo);
            paraMap.put("indcNo", inchk.getIndcNo());
            paraMap.put("custNo", custNo);
            /*
            pivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
            pivo.setPursDt(DateUtils.getCurrentBaseDateTime());
            pivo.setDlvReqDt(DateUtils.getCurrentBaseDateTime());
            if (env.getProperty("purs_proc_yn").equals("Y")) { //??????????????? ???????????? ??????
                confirmPurs = Long.parseLong(env.getProperty("purs.sts.insp")); //????????????
            } else {
                confirmPurs = Long.parseLong(env.getProperty("purs.sts.end")); //????????????
            }
            pivo.setPursSts(confirmPurs);
            try {
                pivo.setOrdNo(Long.parseLong(paraMap.get("ord_no").toString()));
            } catch (NullPointerException en) {
                pivo.setOrdNo(0L);
            }
            pivo.setDlvDt(DateUtils.getCurrentDateTime());
            pivo.setUsedYn("Y");
            pivo.setModIp(paraMap.get("ipaddr").toString());
            pivo.setModDt(DateUtils.getCurrentDateTime());
            pivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            pivo.setRegIp(paraMap.get("ipaddr").toString());
            pivo.setRegDt(pivo.getRegDt());
            pivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            pivo.setCustNo(custNo);
            pivo = pir.save(pivo); //???????????? ???????????? ??????
            log.info(tag + " pirSave complete.........");

            log.info(tag + "???????????? ?????? ?????? ??????.???????????? = " + paraMap.get("indcNo"));
            log.info(tag + "???????????? ?????? ?????? ??????.???????????? = " + pivo.getPursNo());

            PursMatr pmvo = null;
            for (Map<String, Object> el : ds) {
                if (Float.parseFloat(el.get("reqPursQty").toString()) < 0) {
                    continue; //???????????? vs ????????? ??????, ??????????????? ???????????? skip
                    //???????????? : ????????????????????? ??????????????? ???????????? ????????? ?????? ?????? ?????? ?????? ?????????????????? ???????????????????????? ???
                    //        ????????? ?????? ?????? ????????????????????? ????????? ????????? ????????? ?????? ???????????? ???????????? ???????????? ????????? ?????????.
                }
                pmvo = new PursMatr();
                pmvo.setPursNo(pivo.getPursNo());
                pmvo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
                pmvo.setCmpyNo(0L);//?????????????????? ?????? ?????? ????????? MatrIwh.cmptyNo??? ?????? ???????????? ???.
                pmvo.setPursQty(Float.valueOf(el.get("reqPursQty").toString()));
                pmvo.setPursUnit(Long.parseLong(el.get("pursUnit").toString()));
                pmvo.setUsedYn("Y");
                PursMatr chkmvo = pmr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo, pmvo.getPursNo(), pmvo.getMatrNo(), "Y");
                if (chkmvo != null) {
                    pmvo.setPursMatrNo(chkmvo.getPursMatrNo());
                    pmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    pmvo.setModId(pivo.getModId());
                    pmvo.setModIp(pivo.getModIp());
                } else {
                    pmvo.setPursMatrNo(0L);
                    pmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    pmvo.setRegId(pivo.getRegId());
                    pmvo.setRegIp(pivo.getRegIp());
                    pmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    pmvo.setModId(pivo.getModId());
                    pmvo.setModIp(pivo.getModIp());
                }
                pmvo.setCustNo(custNo);
                chkmvo = pmr.save(pmvo);
            }
            log.info(tag + "???????????? ?????? ?????? ???/ ???????????? = " + ds.size());
            log.info(tag + "???????????? ?????????????????? ?????? ??????.????????????(pursNo) = " + pivo.getPursNo());
            return pivo;
            */
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

}
