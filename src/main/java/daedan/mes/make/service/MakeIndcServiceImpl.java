
package daedan.mes.make.service;


import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.MatrOwh;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.MatrOwhRepository;
import daedan.mes.make.domain.*;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.make.repository.*;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.prod.service.ProdService;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.purs.service.PursService;
import daedan.mes.purs.mapper.PursMapper;
import daedan.mes.stock.domain.MatrPos;
import daedan.mes.stock.domain.MatrStk;
import daedan.mes.stock.repository.MatrPosRepository;
import daedan.mes.stock.repository.MatrStkRepository;
import daedan.mes.stock.service.StockService;
import daedan.mes.user.domain.UserInfo;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.*;

@Service("indcService")
public class MakeIndcServiceImpl implements MakeIndcService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private MakeIndcMapper mapper;

    @Autowired
    private MakeIndcRepository makeIndcRepo;

    @Autowired
    private MakeMpRepository makeMpRepo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private MakeWorkPlanRepository makeWorkPlanRepo;

    @Autowired
    private MakeIndcRsltRepository mir;

    @Autowired
    private MatrStkRepository matrStkRepo;

    @Autowired
    private MakeIndcProcRepository makeIndcProcRepo;

    @Autowired
    private PursService pursService;

    @Autowired
    private PursInfoRepository pursRepo;

    @Autowired
    private PursMapper PursMapper;

    @Autowired
    private PursMatrRepository pmr;
    @Autowired
    private PursInfoRepository pir;

    @Autowired
    private MatrOwhRepository omr;

    @Autowired
    private MatrIwhRepository imr;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private MatrPosRepository matrPosRepo;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;


    @Autowired
    private ProdService prodService;

    @Autowired
    private FileRepository fr;

    @Autowired
    private StockService stockService;

    //?????? ?????? -- make_indc ?????? ????????? ?????? ?????? ??????
    private Date frDt;

    //?????? ?????? -- make_indc ?????? ????????? ?????? ?????? ??????
    private Date toDt;

    @Override
    public List<Map<String, Object>> getMakeIndcList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcList(paraMap);
    }

    @Override
    public int getMakeIndcListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMakeIndcInfo(Map<String, Object> paraMap) {

        StringBuffer buf = new StringBuffer();
        Map<String, Object> rmap = null;
        rmap = mapper.getMakeIndcInfo(paraMap);
        try {
            buf.append(env.getProperty("base_file_url")).append("make/").append(rmap.get("indcNo")).append(".png");
            rmap.put("barCodeUrl", buf.toString());
            log.info("getMakeIndcInfo.barCodeUrl = " + rmap.get("barCodeUrl"));
        } catch (NullPointerException ne) {
            rmap = new HashMap<String, Object>();
            buf.setLength(0);
            buf.append(env.getProperty("base_file_url")).append("make/default.png");
            rmap.put("barCodeUrl", buf.toString());
        }
        return rmap;
    }

    @Override
    public Map<String, Object> getMakeIndcRsltInfo(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltInfo(paraMap);
    }

    @Override
    public Map<String, Object> getIndcInfo(Map<String, Object> paraMap) {
        String tag = " MakeIndcService.getIndcInfo => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        StringBuffer buf = new StringBuffer();
        Map<String, Object> rmap = null;
        return mapper.getIndcInfo(paraMap);
    }


    @Override
    public List<Map<String, Object>> getFaultList(Map<String, Object> paraMap) {
        return mapper.getFaultList(paraMap);

    }

    @Override
    public List<Map<String, Object>> getMakePlanList(Map<String, Object> paraMap) {
        return mapper.getMakePlanList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakePlanProdList(Map<String, Object> paraMap) {
        return mapper.getMakePlanProdList(paraMap);
    }

    @Override
    public int getMakePlanProdListCount(Map<String, Object> paraMap) {
        return mapper.getMakePlanProdListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboOrdrCmpyList(Map<String, Object> paraMap) {
        return mapper.getComboOrdrCmpyList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap) {
        return mapper.getComboProdProcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcRsltList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltList(paraMap);
    }

    @Override
    public int getMakeIndcRsltListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcRsltListCount(paraMap);
    }


    /*???????????? ??????????????? ?????????&???????????? ????????? ???????????? ?????? ??????*/
    @Override
    @Transactional
    public void resetMakeTermByDragDrop(Map<String, Object> paraMap) {
        String tag = "makeIndcService.resetMakeTermByDragDrop ==> ";
        paraMap.put("ipaddr", paraMap.get("ipaddr"));
        log.info(tag + "paraMap = " + paraMap.toString());
        mapper.resetMakeTermByDragDrop(paraMap);
    }


    /*
        ??????????????? ????????? ???????????? ????????? ????????? ?????? ?????? ??????
        savemakeIndc ??????????????? ?????????.
            > ?????????????????? ?????? (make_indc)
            > ????????? ????????? ?????? ?????? ????????? bom??? ???????????? ???????????? ?????? (
                - ???????????? ??????(make_indc_matr)
                - ???????????? ?????? (savePursInfo) ??????
                    >  purs_info & purs_matr ??????
                      - ?????? ???????????? ??????????????? ?????? (mapper.chkReqPurs) : (???????????? ???????????? - ????????????)
                      - ?????????????????? ?????? ??????
	                    . ???????????? ??????(purs_mast & purs_matr)
	               > ??????????????????(mess)??? ???????????? ???????????? ???????????? ??????(matr_pos)
	            - ????????????(purs_matr)??? ???????????? ?????????????????? ?????? ?????? (matr_iwh)
     */

    @Transactional
    @Override
    public void saveMakeIndcFullByPlan(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveMakeIndcFullByPlan == > ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());

        Map<String, Object> prodMap = new HashMap<>();
        prodMap.put("ordNo", ordNo);
        prodMap.put("prodNo", prodNo);
        prodMap.put("custNo", custNo);
        List<Map<String, Object>> passMap = mapper.getOrdProdInfo(prodMap);
        for (Map<String, Object> el : passMap) {
            //el.put("ord_no",paraMap.get("ordNo"));
            el.put("indc_dt", paraMap.get("indcDt"));
            el.put("indc_qty", paraMap.get("indcQty"));
            el.put("cust_no", paraMap.get("custNo"));
            el.put("make_fr_dt", paraMap.get("makeFrDt"));
            el.put("make_to_dt", paraMap.get("makeToDt"));
            el.put("indc_sts", paraMap.get("indcSts"));
            el.put("type", paraMap.get("type"));
            el.put("user_id", paraMap.get("userId"));
            el.put("ipaddr", paraMap.get("ipaddr"));
            el.put("indc_tp", paraMap.get("indcTp"));
            this.saveMakeIndcFull(el);
        }

        OrdInfo chkoivo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo, Long.parseLong(paraMap.get("ordNo").toString()), "Y");
        if (chkoivo != null) { //????????????-> ??????????????? ????????? (141-> 142)
            chkoivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_makeIndc")));
            ordRepo.save(chkoivo);
        }

    }

//    @Transactional
    @Override
    public Long saveMakeIndcFull(Map<String, Object> paraMap) { //??????????????? ????????? ???????????? ????????? ??????
        String tag = "makeIndcService.saveMakeIndcFull ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
//        Long oem = Long.parseLong(env.getProperty("code.indcTp.oem"));
//        Long odm = Long.parseLong(env.getProperty("code.indcTp.odm"));

        Map<String, Object> procMap = new HashMap<String, Object>();
        procMap.put("prodNo", prodNo);
        procMap.put("custNo", custNo);


        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;
        Long chkZeroParent = 0L;
        Long ordNo = 0L;
        boolean isCreate = false;

        log.info("???????????? ????????? ?????? ordInfo, ordProd ?????? ??????" + paraMap.get("type"));
        if (paraMap.get("type") == null) {
            paraMap.put("type", "newMake");
        }
        OrdInfo oivo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo, prodNo, "Y");


        if (paraMap.get("type").toString().equals("newMake")) {
            ordNo = this.makeOrderByIndc(paraMap);
            log.info("??????????????? ???????????? : " + ordNo);
        } else {
            ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        }
        try {
            chkZeroParent = Long.parseLong(paraMap.get("parIndcNo").toString());
        } catch (NullPointerException ne) {
            chkZeroParent = 0L; //????????? ????????? ????????? ?????????.
        }
        try {
            if (paraMap.get("type").toString().equals("rowClick")) { //??????????????? ???????????? ?????? ????????? ????????? ??????
                return saveMakeIndc(paraMap);
            }
        } catch (NullPointerException ne) {

        }

        int idx = 0;
        List<Map<String, Object>> ds = mapper.getProdProcList(procMap); //????????????????????? ?????????????????? ??????
        for (Map<String, Object> el : ds) { //????????? ????????? ??????????????? ?????????????????? (???:??????,??????,?????????,????????????,?????????,???????????????)
            paraMap.put("matr_req_able_yn", (idx == 0) ? "Y" : "N");
            paraMap.put("par_indc_no", (idx == 0) ? 0L : svParIndcNo);
            paraMap.put("proc_cd", Long.parseLong(el.get("value").toString())); //value = > ??????????????????(721,732..etc)
            paraMap.put("brnch_no", Long.parseLong(el.get("brnchno").toString())); //????????????????????????(1253... etc)
            //paraMap.put("custNo",paraMap.get("custNo")); Remarked by KMJ AT 21.10.20 --??????????????? ??????.
            /*???????????? ?????? ?????? ?????? ???, ????????? ?????? ????????? ???????????? ??????????????? ??????. --Remarked By KMJ ????????????(21.10.20)
            try{
                paraMap.put("indcWgt", Float.parseFloat(el.get("indcWgt").toString()));
            }catch (NullPointerException ne){
            }
            */
            if (ordNo != null) {
                paraMap.put("ord_no", ordNo); //???????????? ?????? ???????????? ????????? ????????????.
            }

            if (chkZeroParent != 0) { //child??? ????????? ?????? ??????????????? ??????
                if (!isCreate) {
                    this.saveMakeIndc(paraMap);
                    break;
                }
            }
            parIndcNo = this.saveMakeIndc(paraMap);

            if (idx == 0) {
                svParIndcNo = parIndcNo;
            }
            ++idx;
        }

        if (Integer.parseInt(paraMap.get("custNo").toString()) == 8) { //????????????
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
            this.saveIndcRslt(paraMap);
        }

        return svParIndcNo;
    }

    @Override
    public Long saveIndcInfo(Map<String, Object> paraMap) {
        return null;
    }


    @Transactional
    @Override
    public Long saveMakeIndc(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveMakeIndc ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        MakeIndc mivo = this.saveMakeIndcInfo(paraMap); //?????????????????? ??????
        Long cmpyNo = 0L; //AddOn By KMJ AT 21.09.10 09:52
        Long parIndcNo = 0L;

        if (mivo.getParIndcNo() == 0L) {
            parIndcNo = mivo.getIndcNo();
        }
        if (mivo.getProdNo() == 0L) {
            log.info(tag + "???????????? ?????? ????????? ??????(??????????????? ?????? ?????????) ????????? ?????? ???????????? ??????");
            return 0L;
        }
        log.info(tag + "0.????????????????????? = " + paraMap.get("matrReqAbleYn"));
        log.info(tag + "1.???????????????????????? ???????????? ????????? ?????? ????????? ???????????? ?????? ?????? ?????? ????????? ????????? ??????????????? ?????? ???????????? ???");
        if (paraMap.get("matrReqAbleYn").toString().equals("Y")) {
            log.info(tag + "2.BOM????????? ???????????? ???????????? ===> " + mivo.getProdNo());

            Map<String, Object> bomchkmap = new HashMap<String, Object>();
            bomchkmap.put("prodNo", mivo.getProdNo());

            //camel ???????????? ?????? ord_no ?????? ??????, ord_no??? null??? ????????? ???????????? ????????????
            /* SOL Remarked By KMJ AT 21.09.10 08:58
            if(paraMap.get("ord_no") == null){
                bomchkmap.put("ordNo", paraMap.get("ordNo"));
            }else{
                bomchkmap.put("ordNo", paraMap.get("ord_no"));
            }
            EOL Addon By KMJ AT 21.09.10 08:58 */

            /* SOL Addon By KMJ AT 21.09.10 08:58 */
            Long ordNo = 0L;
            try {
                ordNo = Long.parseLong(paraMap.get("ordNo").toString());

                Map<String, Object> tmpMap = new HashMap<String, Object>();
                tmpMap.put("custNo", custNo);
                tmpMap.put("ordNo", ordNo);

                //OrdInfo ordvo = new OrdInfo();
                //tmpMap = ordMapper.findByCustNoAndOrdNoAndUsedYn(tmpMap);
                //ordvo = (OrdInfo) StringUtil.mapToVo(tmpMap,ordvo);
                OrdInfo ordvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo, ordNo, "Y");  //???????????? ???????????? ????????? ?????????
                if (ordvo != null) {
                    cmpyNo = ordvo.getCmpyNo();
                }
            } catch (NullPointerException ne) {
                ordNo = 0L;
            }
            /* EOL Addon By KMJ AT 21.09.10 08:58 */

            bomchkmap.put("cmpyNo", cmpyNo); //AddOn By KMJ AT 21.09.10 09:53 : ???????????? ???????????? ?????? ??????
            bomchkmap.put("ordNo", ordNo);
            bomchkmap.put("custNo", custNo);
            bomchkmap.put("pageNo", 0);
            bomchkmap.put("pageSz", 100);
            bomchkmap.put("checkPursYn", "checking");
            bomchkmap.put("indcQty", mivo.getIndcQty()); //????????? ?????????
            List<Map<String, Object>> ds = null;

            /* SOL Addon By KMJ AT 21.09.10 08:58 */
            ds = prodService.getProdBomListByIndc(bomchkmap);
            /* EOL Addon By KMJ AT 21.09.10 08:58 */

            /* SOL Remarked By KMJ AT 21.09.10 08:58
            if(paraMap.get("cust_no").equals(3) ){ //????????????
                 ds = prodService.getHdfdProdBomListByIndc(bomchkmap);
            }else if(paraMap.get("cust_no").equals(2)){ //????????????
                 ds = prodService.getSfProdBomListByIndc(bomchkmap);
            }else {
                 ds = prodService.getProdBomListByIndc(bomchkmap);
            }
            EOL Remarked By KMJ AT 21.09.10 08:58 */

            log.info(tag + "3.???????????? ???????????? ?????????????????? ?????? (?????????????????? BOM ???????????????)===> " + ds);
            int idx = 0;
            for (Map<String, Object> el : ds) {
                ++idx;
                el.put("indc_no", mivo.getIndcNo());
                el.put("fill_yield", mivo.getFillYield());
                el.put("userId", paraMap.get("userId"));
                el.put("ipaddr", paraMap.get("ipaddr"));
                el.put("cust_no", custNo);
                log.info("??????????????? ???????????? ?????? ?????? : " + el);
                //???????????? ?????? ?????? ?????? ???, ???????????? ?????? ????????? ??????????????? ???.
                this.saveMakeIndcMatr(el);
            }

            boolean chk = true;
            MakeIndc inchk = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, parIndcNo, "Y");
            if (inchk != null) {
                Map<String, Object> datas = new HashMap<String, Object>();
                datas.put("custNo", custNo);
                datas.put("indcNo", parIndcNo);

                List<Map<String, Object>> dl = mapper.chkStkByIndc(datas); // ?????? ??????
                for (Map<String, Object> es : dl) {
                    if (Float.parseFloat(es.get("reqPursQty").toString()) < 0) { //???????????? < ??????????????? ??????
                        chk = false;
                        break;
                    }
                }
                inchk.setIndcSts( (!chk) ?  2401L : 2402L );

//                /*kmjkmj.21.12.03 -- ????????? ?????? ?????? ?????????
//                List<Map<String, Object>> dl = mapper.chkStkByIndc(datas); // ?????? ??????
//                for (Map<String, Object> es : dl) {
//                    if (Float.parseFloat(es.get("reqPursQty").toString()) < 0) { //???????????? < ??????????????? ??????
//                        chk = false;
//                        break;
//                    }
//                }
//                inchk.setIndcSts( (!chk) ?  2401L : 2402L );
//                ????????? ?????? ?????? ?????????*/
//                inchk.setIndcSts(2403L); //kmjkmj - ????????? ?????? ?????? ?????????.
                inchk.setCustNo(custNo);
                makeIndcRepo.save(inchk);
            }

            try {
                log.info("4.?????????????????? ????????????(purs_info) , ????????????(purs_matr) ??? ????????????(matr_pos) ?????? ?????? / ?????????????????? = " + mivo.getIndcNo());
                paraMap.put("indcNo", mivo.getIndcNo());
                paraMap.put("custNo", custNo);
                this.savePursInfo(paraMap);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }

        return mivo.getIndcNo();
    }

    /*???????????? ?????? ????????? ?????? ??? ?????? ?????? ???, ???????????? ??????.*/
    private Long makeOrderByIndc(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());

        log.info("??????????????? ???????????? ?????? => " + paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        OrdInfo oivo = new OrdInfo();
        Long ordNo = 0L;

        try {
            ordNo = Long.parseLong(paraMap.get("ordNo").toString());
        } catch (NullPointerException ne) {
            ordNo = 0L;
        }

        OrdInfo chkvo = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo, ordNo, "Y");
        Map<String, Object> tmpMap = new HashMap<String, Object>();
        tmpMap.put("custNo", custNo);
        tmpMap.put("ordNo", ordNo);
//        OrdInfo chkvo = new OrdInfo();
//        tmpMap = ordMapper.findByCustNoAndOrdNoAndUsedYn(tmpMap);
//        chkvo = (OrdInfo) StringUtil.mapToVo(tmpMap,chkvo);
        if (chkvo != null) {
            oivo.setOrdNo(chkvo.getOrdNo());
        } else {
            oivo.setOrdNo(0L);
        }

        oivo.setOrdNm(paraMap.get("prodNm").toString());
        oivo.setOrdSts(Long.parseLong(env.getProperty("ord_status_acpt")));
        oivo.setOrdTp(0L);
        oivo.setPlcNo(0L);
        oivo.setUsedYn("Y");

        try {
            oivo.setOrdDt(sdf.parse(paraMap.get("makeToDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            oivo.setOrdDt(DateUtils.getCurrentDate());
        }

        try {
            oivo.setDlvReqDt(sdf.parse(paraMap.get("makeFrDt").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            oivo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
        } catch (NullPointerException ne) {
            oivo.setCmpyNo(0L);
        }

        oivo.setRegDt(DateUtils.getCurrentDate());
        oivo.setRegIp(paraMap.get("ipaddr").toString());
        oivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        oivo.setCustNo(custNo);
        oivo = ordRepo.save(oivo);

        OrdProd opvo = new OrdProd();
        OrdProd chkopvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo, oivo.getOrdNo(), Long.parseLong(paraMap.get("prodNo").toString()), "Y");
        if (chkopvo != null) {
            opvo.setOrdNo(chkopvo.getOrdNo());
            opvo.setOrdProdNo(chkopvo.getOrdProdNo());
        } else {
            opvo.setOrdProdNo(0L);
            opvo.setOrdNo(oivo.getOrdNo());
        }

        opvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        opvo.setQtyPerPkg(1);
        opvo.setUsedYn("Y");
        /*SOL AddOn By KMJ At 21.09.04*/
        try {
            opvo.setPursNo(Long.parseLong(paraMap.get("pursNo").toString()));
        } catch (NullPointerException ne) {
            opvo.setPursNo(0L);
        }
        /*EOL AddOn By KMJ At 21.09.04*/

        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        float val = 0f;
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo, prodNo, "Y");
        if (chk != null) {
            //??????????????? ?????? vol??? mess ?????? X
            try {
                if (custNo == 3 || custNo == 2) {
                    val = chk.getVol();
                } else {
                    val = chk.getVol();
                }
            } catch (NullPointerException e) {
                val = chk.getVol();
            }
        }

        Float ordQty = 0F;
        if (custNo == 6) {
            Float indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
            ordQty = ((indcWgt / val));
        } else {
            try {
                Float indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
                ordQty = indcWgt;
            } catch (NullPointerException ne) {
                Float indcQty = Float.parseFloat(paraMap.get("indcQty").toString());
                ordQty = indcQty;
            }
        }

        opvo.setOrdQty(ordQty);

        try {
            opvo.setSaleUnit(Long.parseLong(paraMap.get("makeUnit").toString()));
        } catch (NullPointerException ne) {
            opvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
        }

        opvo.setRegDt(DateUtils.getCurrentDate());
        opvo.setRegIp(paraMap.get("ipaddr").toString());
        opvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        opvo.setCustNo(custNo);
        ordProdRepo.save(opvo);
        return oivo.getOrdNo();
    }

    @Override
    public List<Map<String, Object>> getProdIndcNo(Map<String, Object> paraMap) {
        return mapper.getProdIndcNo(paraMap);
    }

    @Override
    public Float getCurrentProdStock(Map<String, Object> paraMap) {
        return mapper.getCurrentProdStock(paraMap);
    }

    private MakeIndc saveMakeIndcInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveMakeIndcInfo=> ";
        log.info(tag + "paraMap =  " + paraMap.toString());
        Long procCd = 0L;
        Long brnchNo = 0L;
        Float indcQty = 0F;
        Float indcWgt = 0F;
        int needValDt = 0;
        int nextvalDt = 0;
        Date tomkdt = null;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Date frd;

        MakeIndc mivo = new MakeIndc();
        mivo.setUsedYn("Y");

        try {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("par_indc_no").toString()));
        } catch (NullPointerException ne) {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("parIndcNo").toString()));
        }



        try {
            //indcNo??? ???????????? ??????????????? ???????????? ????????? ????????? NullPointer ??????????????? ????????? ??????????????? ?????? ??????????????? ?????? ?????????.(2021.04.26 : KMJ)
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setIndcNo(0L);
            mivo.setRegDt(DateUtils.getCurrentDate());
            mivo.setRegId(mivo.getModId());
            mivo.setRegIp(mivo.getModIp());
        }


        try {
            mivo.setIndcTp(Long.parseLong(paraMap.get("indcTp").toString()));
        } catch (NullPointerException ne) {

        }
        try {
            mivo.setIndcSts(Long.parseLong(paraMap.get("indcSts").toString()));
        } catch (NullPointerException en) {
            mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.indcsts")));
        }

        try{
            procCd = Long.parseLong(paraMap.get("proc_cd").toString()); //?????????????????? ?????? ?????? ?????? ??????
        }catch (NullPointerException ne){
            procCd = Long.parseLong(paraMap.get("procCd").toString()); //?????????????????? ?????? ?????? ?????? ??????
        }

        mivo.setProcCd(procCd);
        if (mivo.getParIndcNo() == 0L) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(custNo, mivo.getParIndcNo(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
            if (chkvo != null) {
                if (chkvo.getParIndcNo() > 0L) {
                    log.info(tag + "???????????? ?????????????????? ?????????.===> ");
                    log.info(tag + "???????????????????????? = " + mivo.getParIndcNo());
                    log.info(tag + "?????????????????? = " + mivo.getIndcNo());
                    log.info(tag + "??????????????? = " + mivo.getProcCd());
                    log.info(tag + "?????? ???????????? ?????????.===> ");
                    return chkvo;
                }
            }
        }
        /*???????????? : ??????????????? ??????????????? ????????????????????? ????????? ???????????? ????????? ???????????? ?????????????????? ?????? ???????????? ?????? ???????????? ?????? (???????????? / ????????????) : ??????????????? 100% ?????? ????????? ?????????.*/
        try {
            mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString()));
        } catch (NullPointerException ne) {
            mivo.setFillYield(0F);
        }
        /*????????? ????????????-??????????????? ?????????
        try{
            mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
        }catch(NullPointerException ne){
            mivo.setCtlFillYield(mivo.getFillYield());
        }
        */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long prodNo = 0L;
        Float mess = 0F;
        float val = 0f;

        try {
            mivo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString())); //??????
        } catch (NullPointerException ne) {
            mivo.setProdNo(0L); //??????
        }

        try {
            mivo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString())); //???????????? ?????? ?????? ???????????? ???????????? ????????????
        } catch (NullPointerException ne) {
            mivo.setOrdNo(0L); //??????????????????
        }

        prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo, prodNo, "Y");

        if (chk != null) {
            //??????????????? ?????? vol??? mess ?????? X
            //try{ //Remarked By KMJ At 21.10.25
            //if(custNo == 3 || custNo == 2){ //Remarked By KMJ At 21.10.25
            //    val = chk.getVol() * 0.001F; //Remarked By KMJ At 21.10.25
            //} //Remarked By KMJ At 21.10.25
            //else{ //Remarked By KMJ At 21.10.25
            val = chk.getVol();
            mess = chk.getMess();
            //} //Remarked By KMJ At 21.10.25
            //}catch(NullPointerException e){ //Remarked By KMJ At 21.10.25
            //   val = chk.getVol(); //Remarked By KMJ At 21.10.25
            //} //Remarked By KMJ At 21.10.25saveMakeIndcFull
        }

        try {
            indcQty = Float.parseFloat(paraMap.get("indcQty").toString());
        } catch (NullPointerException ne) {
            indcQty = 0F;
        }

        try {
            indcWgt = Float.parseFloat(paraMap.get("indcWgt").toString());
        } catch (NullPointerException ne) {
            indcWgt = 0F;
        }

        if (indcQty == 0) {
            mivo.setIndcWgt(indcWgt);
            if (custNo == 2) {
                mivo.setIndcQty((indcWgt));
            } else {
                mivo.setIndcQty((indcWgt * 1000) / (val * chk.getQtyPerPkg()));
            }

        } else {
            mivo.setIndcQty(indcQty);
            mivo.setIndcWgt(indcWgt);//??????????????????
        }

        try {
            mivo.setMakeUnit(Long.parseLong(paraMap.get("makeUnit").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setMakeUnit(0L);
        }
        try {
            mivo.setIndcDt(sdf.parse(paraMap.get("indcDt").toString().substring(0, 10)));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            mivo.setIndcDt(DateUtils.getCurrentDateTime());
        }
        try {
            try {
                brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
            } catch (NullPointerException ne) {
                brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
            }

            ProcBrnch brnvo = procBrnchRepo.findByCustNoAndBrnchNoAndProcCdAndUsedYn(custNo, brnchNo, procCd, "Y");
            if (brnvo != null) {
                needValDt = brnvo.getNeedDtVal();
                nextvalDt = brnvo.getNextStepVal();
            }

            /*????????? ???????????? ??????( ????????? ??? ????????? ????????????)*/
            frd = sdf.parse(paraMap.get("indcDt").toString().substring(0, 10));


            tomkdt = sdf.parse(paraMap.get("makeToDt").toString().substring(0, 10));

            //??????????????? ????????? ??????????????? ?????????????????? ????????? ???????????? ???????????? ??????,  ?????? ????????? ????????? ?????? ????????? ?????? - 21.07.18
            if (procCd == 721) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(frd);
                cal.add(Calendar.DATE, needValDt - 1);
                toDt = cal.getTime();

                mivo.setMakeFrDt(frd);
                mivo.setMakeToDt(tomkdt);
            } else {
                mivo.setMakeFrDt(toDt);

                Calendar cal = Calendar.getInstance();
                cal.setTime(toDt);
                cal.add(Calendar.DATE, needValDt + nextvalDt - 1);
                toDt = cal.getTime();

                mivo.setMakeToDt(toDt);
            }


//            if(procCd == 721) {
//                if (nextvalDt > 0) {
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(frd);
//                    cal.add(Calendar.DATE, needValDt+nextvalDt-1);
//                    toDt = cal.getTime();
//                    mivo.setMakeFrDt(frd);
//                    mivo.setMakeToDt(toDt);
//                } else{
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(frd);
//                    cal.add(Calendar.DATE, needValDt-1);
//                    toDt = cal.getTime();
//                    mivo.setMakeFrDt(frd);
//                    mivo.setMakeToDt(toDt);
//                }
//            }else {
//                if (needValDt == 1) {
//                    Calendar cale = Calendar.getInstance();
//                    cale.setTime(frd);
//                    cale.add(Calendar.DATE, needValDt + nextvalDt-1);
//                    toDt = cale.getTime();
//                    mivo.setMakeFrDt(tomkdt);
//                    mivo.setMakeToDt(toDt);
//                }
//                if(nextvalDt > 0) {
//                    Calendar nextCal = Calendar.getInstance();
//                    nextCal.setTime(tomkdt);
//                    nextCal.add(Calendar.DATE, needValDt+nextvalDt-1);
//                    toDt = nextCal.getTime();
//                    mivo.setMakeFrDt(tomkdt);
//                    mivo.setMakeToDt(toDt);
//                }
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException ne) {
            try {
                mivo.setMakeFrDt(sdf.parse(paraMap.get("makeFrDt").toString()));//???????????????
                mivo.setMakeToDt(sdf.parse(paraMap.get("makeToDt").toString()));//???????????????
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//            log.info(tag + "????????? ??????????????? ???????????? ????????? ??????....");
//            Map<String, Object> chkmap = new HashMap<String,Object>();
//            chkmap.put("procCd",paraMap.get("procCd"));
//            Map<String,Object> procmap = procService.getProcInfo(chkmap);
//            int maxMakeQty = 0;
//            try {
//                maxMakeQty = Integer.parseInt(procmap.get("max_makeQty").toString());
//            }
//            catch (NullPointerException ne) {
//                maxMakeQty = 0;
//            }
//            chkmap.put("procNo", mivo.getProdNo());
//           //Map<String,Object> prodmap = prodService.getProdInfo(chkmap);
//
//            int dateTerm = DateUtils.getProgressDays(paraMap.get("make_fr_dt").toString(), paraMap.get("make_to_dt").toString());
//            dateTerm++;
//            mivo.setMaxMakeQty((float) (maxMakeQty * dateTerm));
//            log.info(tag + " maxMakeQty = " + mivo.getMaxMakeQty());
//            mivo.setMaxMakeWgt(mivo.getMaxMakeQty() * val );
        mivo.setMaxMakeWgt(indcWgt);
        mivo.setMaxMakeQty(indcQty);

        try {
            mivo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
        } catch (NullPointerException ne) {
            Map<String, Object> scm = new HashMap<String, Object>();
            scm.put("makeFrDt", mivo.getMakeFrDt());
            scm.put("makeToDt", mivo.getMakeToDt());
            mivo.setStatCd(mapper.getMakeIndcStatus(scm)); //???????????? ???????????? ???????????? ????????????(??????,??????,?????? ??????)
        }
        try {
            mivo.setFaultRt(Float.parseFloat(paraMap.get("faultRt").toString()));
        } catch (NullPointerException ne) {
            mivo.setFaultRt(0f);
        }
        try {
            mivo.setOperRt(Float.parseFloat(paraMap.get("operRt").toString()));
        } catch (NullPointerException ne) {
            mivo.setOperRt(0f);
        }

        try {
            mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString()));
        } catch (NullPointerException ne) {
            mivo.setFillYield(0f);
        }
        try {
            mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
        } catch (NullPointerException ne) {
            mivo.setCtlFillYield(mivo.getFillYield());
        }
        try {
            mivo.setRealYield(Float.parseFloat(paraMap.get("realYield").toString()));
        } catch (NullPointerException ne) {
            mivo.setRealYield(0f);
        }

        try {
            mivo.setModDt(mivo.getModDt());
        } catch (NullPointerException e) {

        }
        try {
            mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mivo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        try {
            mivo.setIndcCont(paraMap.get("indcCont").toString());
        } catch (NullPointerException ne) {
        }


        MakeIndc chkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo, mivo.getIndcNo(), mivo.getProcCd(), "Y");
        if (chkvo != null) {
            mivo.setIndcNo(chkvo.getIndcNo());
            if (mivo.getCtlFillYield() == 0f) {
                mivo.setCtlFillYield(chkvo.getCtlFillYield());
            }
            if (mivo.getOperRt() == 0f) {
                mivo.setOperRt(chkvo.getOperRt());
            }
            if (mivo.getFaultRt() == 0f) {
                mivo.setFaultRt(chkvo.getFaultRt());
            }
            if (mivo.getRealYield() == 0f) {
                mivo.setRealYield(chkvo.getRealYield());
            }
            mivo.setRegId(chkvo.getRegId());
            mivo.setRegIp(chkvo.getRegIp());
            mivo.setRegDt(chkvo.getRegDt());
        } else {
            mivo.setIndcNo(0L);
            mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mivo.setRegIp(paraMap.get("ipaddr").toString());
            mivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            try {
                mivo.setFaultRt(Float.parseFloat(paraMap.get("faultRt").toString())); //?????????
            } catch (NullPointerException ne) {

            }
            try {
                mivo.setFillYield(Float.parseFloat(paraMap.get("fillYield").toString())); //????????????
            } catch (NullPointerException ne) {

            }
            try {
                mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString())); //?????????????????????
            } catch (NullPointerException ne) {

            }
            try {
                mivo.setRealYield(chkvo.getRealYield()); //????????????
            } catch (NullPointerException ne) {
                MakeIndcRslt rvo = mir.findByCustNoAndIndcNoAndUsedYn(custNo, mivo.getIndcNo(), "Y");
                if (rvo != null) {
                    mivo.setRealYield(rvo.getMakeWgt() / mivo.getIndcQty());
                }
            }
            //SOL AddOn By KMJ At 21.08.06 : ???????????? ?????? ?????? ??????
            try {
                mivo.setClosYn(paraMap.get("closYn").toString());
            } catch (NullPointerException ne) {
                mivo.setClosYn("N");
            }
            //EOL AddOn By KMJ At 21.08.06 : ???????????? ?????? ?????? ??????
        }
        mivo.setCustNo(custNo);
        mivo = makeIndcRepo.save(mivo); //???????????? ????????????
        log.info("???????????? " + mivo + "??????????????? : " + mivo.getIndcWgt());


//        if (chkvo == null) {
//            log.info(tag + "???????????????????????? ????????? ????????? ??????===> ");
//            Map<String, Object> bmap = new HashMap<String, Object>();
//            bmap.put("codeNo", mivo.getIndcNo());
//            bmap.put("savePath", "make/");
//            stockService.makeBarCode(bmap);
//        }
        return mivo;
    }

    private void saveMakeIndcMatr(Map<String, Object> el) {
        String tag = "MakeIndcService.saveMakeIndcMatr => ";
        Long matrNo = Long.parseLong(el.get("matrNo").toString());
        Long custNo = Long.parseLong(el.get("custNo").toString());
        MakeIndcMatr matrvo = new MakeIndcMatr();
        Long indcNo = Long.parseLong(el.get("indcNo").toString());

        matrvo.setIndcNo(indcNo);
        matrvo.setMatrNo(matrNo);
        matrvo.setCustNo(custNo);
        // getProdBomList ?????? needQty??? ???????????? ???????????? ????????? ?????? --21.07.17 KTH --
//        float consistRt = Float.parseFloat(el.get("consist_rt").toString());
//        float indcWgt   = Float.parseFloat(el.get("indc_wgt").toString());
//        log.info(tag + "????????? ??????.???????????? = " + matrvo.getMatrNo() + ": ????????? = " + el.get("matr_nm").toString() );
//        log.info(tag + "????????? ??????.???????????? = " + consistRt + " : ?????????????????? = " + indcWgt );
//        matrvo.setNeedQty ((float) ( ((consistRt * 0.01) * indcWgt )) );


        matrvo.setNeedQty(Float.parseFloat(el.get("needQty").toString()));
        matrvo.setUsedYn("Y");
        matrvo.setMatrSts("N");
        // ?????? ??????????????? ?????????. 21/08/24
        matrvo.setTakeYn("N");

        try {
            matrvo.setModId(Long.parseLong(el.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            matrvo.setModDt(DateUtils.getCurrentBaseDateTime());
        } catch (NullPointerException e) {

        }
        try {
            matrvo.setModIp(el.get("ipaddr").toString());
        } catch (NullPointerException e) {
        }

        MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo, matrvo.getIndcNo(), matrvo.getMatrNo(), "Y");
        if (chkvo != null) {
            matrvo.setIndcMatrNo(chkvo.getIndcMatrNo());
        } else {
            matrvo.setIndcMatrNo(0L);
            matrvo.setRegId(Long.parseLong(el.get("userid").toString()));
            matrvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            matrvo.setRegIp(el.get("ipaddr").toString());
        }
        matrvo.setCustNo(custNo);
        makeIndcMatrRepo.save(matrvo);
        log.info(tag + "???????????? ???????????? ?????????????????? ?????? (?????????????????? BOM ??????) ???.===> ");
        log.info(tag + "???????????? ?????????????????? ?????? ?????????????????? ?????? ?????? ???..===> ");
    }

    @Override
    public void dropMakeIndc(Map<String, Object> paraMap) {
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc vo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
        if (vo != null) {
            log.info("1. ?????? ???????????? ??????");
            vo.setUsedYn("N");
            makeIndcRepo.save(vo);

            log.info("2. ???????????? ????????? ?????? ?????????????????? ?????? ??????");
            mapper.dropMakeIndcMatr(indcNo);

            log.info("3. ?????? ????????? ????????????");
            List<Map<String, Object>> ds = mapper.dropSumMatrOwh(indcNo);
            for (Map<String, Object> el : ds) {
                Long matrNo = Long.parseLong(el.get("matrNo").toString());
                Float stkQty = Float.parseFloat(el.get("owhQty").toString());
                MatrStk skvo = matrStkRepo.findByCustNoAndMatrNoAndUsedYn(custNo, matrNo, "Y");
                if (skvo != null) {
                    skvo.setMatrNo(skvo.getMatrNo());
                    skvo.setMatrStkNo(skvo.getMatrStkNo());
                    skvo.setWhNo(skvo.getWhNo());
                    skvo.setStkQty(skvo.getStkQty() + stkQty);
                    skvo.setCustNo(custNo);
                    matrStkRepo.save(skvo);
                }
            }
            //mapper.dropMatrOwh(indcNo); // usedYn = N

            MakeIndc mivoForDrop = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
            if (mivoForDrop != null) {
                mivoForDrop.setUsedYn("N");
                mivoForDrop.setModDt(DateUtils.getCurrentBaseDateTime());
                mivoForDrop.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mivoForDrop.setModIp(paraMap.get("ipaddr").toString());
                makeIndcRepo.save(mivoForDrop);
            }
            Long pursNo = 0L;

//           ??????????????? ????????? ???????????? ????????? ?????? purs_info ?????? ????????? ???????????? ?????? ????????? ??????


            List<Map<String,Object>> pursList = PursMapper.getMakeIndcPursList(paraMap);
            if (pursList != null) {
                for (Map<String, Object> item : pursList){
                    PursInfo pivo = pursRepo.findByPursNoAndUsedYn(Long.parseLong(item.get("pursNo").toString()), "Y");
                    pursNo = pivo.getPursNo();
                    //????????????(purs_info)??? ????????? ??????????????? ??????????????? ??????, ?????????????????? ?????????????????? ??????.
                    log.info("4. ?????? ????????? ?????? ??????");
                    if (pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.end")) || pivo.getPursSts() == Long.parseLong(env.getProperty("purs.sts.ing"))) {
                        pivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.insp")));
                        pursRepo.save(pivo);

                        List<Map<String, Object>> iwhList = mapper.getDropMatrIwhList(pursNo);

                        Float iwhQty = 0F;
                        Long matrNo = 0L;
                        Long whNo = 0L;
                        for (Map<String, Object> el : iwhList) {
                            iwhQty = Float.parseFloat(el.get("iwhQty").toString());
                            matrNo = Long.parseLong(el.get("matrNo").toString());
                            whNo = Long.parseLong(el.get("whNo").toString());

                            //????????? ?????? ??????.
                            MatrStk msvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo, whNo, matrNo, "Y");
                            if (msvo != null) {
                                Float qty = Float.parseFloat(msvo.getStkQty().toString()) - iwhQty;
                                msvo.setStkQty(qty);
                                msvo.setCustNo(custNo);
                                matrStkRepo.save(msvo);
                            }
                        }

                        //???????????? ??????.
                        mapper.dropIwhList(pursNo);
                    }

                    //??????????????????(purs_info)??? ????????? ????????? ??????, ???????????? N?????? ??????
                    else {
                        log.info("5. ?????? ?????? ?????? ?????? ??????");
                        pivo.setUsedYn("N");
                        pivo.setCustNo(custNo);
                        pivo = pursRepo.save(pivo);

                        //????????????(purs_matr)??? ???????????? N?????? ??????
                        PursInfo pivoForDrop = pursRepo.findByCustNoAndPursNoAndUsedYn(custNo, pivo.getPursNo(), "Y");
                        if (pivoForDrop != null) {
                            pivoForDrop.setUsedYn("N");
                            pivoForDrop.setModDt(DateUtils.getCurrentBaseDateTime());
                            pivoForDrop.setModId(Long.parseLong(paraMap.get("userId").toString()));
                            pivoForDrop.setModIp(paraMap.get("ipaddr").toString());
                            pivo.setCustNo(custNo);
                            pursRepo.save(pivoForDrop);
                        }
                        //mapper.dropPursMatr(pursNo);
                    }
                }
            }
        }
    }


    @Override
    public List<Map<String, Object>> getComboWorkDay(Map<String, Object> paraMap) {

        Map<String, Object> infoMap = mapper.getMakeIndcInfo(paraMap);
        List<Date> ds = null;
        List<Map<String, Object>> comboList = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        try {
            Date fr = sdFormat.parse(infoMap.get("makeFrDt").toString());
            cal.setTime(fr);
            //cal.add(Calendar.DATE ,  +1);

            Date to = sdFormat.parse(infoMap.get("makeToDt").toString());


            cal.setTime(to);
            //cal.add(Calendar.DATE ,  +1);
            ds = DateUtils.getBetweenDates(fr, to);

            for (Date el : ds) {
                Map<String, Object> dateMap = new HashMap<String, Object>();
                dateMap.put("value", sdFormat.format(el));
                dateMap.put("text", sdFormat.format(el));
                comboList.add(dateMap);
            }
        } catch (NullPointerException ne) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return comboList;
    }

    @Override
    public void saveMakeIndcMp(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("users");
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date workDt = null;
        for (Map<String, Object> el : ds) {
            el.put("indcNo", paraMap.get("indcNo"));

            MakeIndcMp mpvo = new MakeIndcMp();
            try {
                workDt = sdFormat.parse(paraMap.get("workDt").toString());
                cal.setTime(workDt);
                cal.add(Calendar.DATE, +1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mpvo.setUserId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setMpUsedDt(workDt);
            MakeIndcMp chkvo = makeMpRepo.findByCustNoAndUserIdAndMpUsedDtAndUsedYn(custNo, mpvo.getUserId(), mpvo.getMpUsedDt(), "Y");
            if (chkvo != null) continue;


            mpvo.setTotWorkHm(0);
            mpvo.setRegulWorkHm(0);//????????????
            mpvo.setOverWorkHm(0); //????????????
            mpvo.setExchgWorkHm(0);
            mpvo.setFrHm("0000");
            mpvo.setToHm("0000");
            mpvo.setSpotNo(0L);
            mpvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
            mpvo.setRegDt(mpvo.getModDt());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setModDt(mpvo.getModDt());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setUsedYn("Y");
            mpvo.setIndcMpNo(0L);
            mpvo.setCustNo(custNo);
            makeMpRepo.save(mpvo);
        }
    }

    @Override
    public List<Map<String, Object>> getMakeIndcMpList(Map<String, Object> paraMap) {
        String tag = "vsvc.MakeIndcService.getMakeIndcMpList => ";
        log.info(tag + "params = " + paraMap.toString());
        return mapper.getMakeIndcMpList(paraMap);
    }

    @Override
    public int getMakeIndcMpListCount(Map<String, Object> paraMap) {
        return mapper.getMakeIndcMpListCount(paraMap);
    }

    @Override
    public Map<String, Object> getMakeIndcMpInfo(Map<String, Object> paraMap) {
        return mapper.getMakeIndcMpInfo(paraMap);
    }

    @Override
    public Map<String, Object> getTimeString(Map<String, Object> paraMap) {

        return mapper.getTimeString(paraMap);
    }

    @Override
    public List<Map<String, Object>> getReqMatrList(Map<String, Object> paraMap) {
        return mapper.getReqMatrList(paraMap);
    }

    @Override
    public int getReqMatrListCount(Map<String, Object> paraMap) {
        return mapper.getReqMatrListCount(paraMap);
    }

    /*??????????????? ????????? ???????????? ????????????*/
    @Override
    public void saveReqMatr(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        /* Remarked At 21.03.05 : ???????????? ????????? matr_pos??????????????? matrNo,whNo??? ??????????????? ???????????? ?????? ???*/
        log.info(tag + "???????????? ????????? BOM??? ????????? ???????????? ??????.????????????(indcNo) = " + paraMap.get("indcNo"));
        List<Map<String, Object>> ds = mapper.getReqMatrList(paraMap); //???????????? ????????? BOM??? ????????? ??????????????? ???????????? ?????? ??????
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        for (Map<String, Object> el : ds) {
            MatrOwh owhvo = new MatrOwh();
            owhvo.setIndcNo(indcNo);
            owhvo.setMatrNo(Long.parseLong(el.get("matrNo").toString())); //??????????????????
            owhvo.setModDt(DateUtils.getCurrentBaseDateTime());
            owhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            owhvo.setModIp(paraMap.get("ipaddr").toString());

            MatrOwh chkvo = omr.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo, owhvo.getIndcNo(), owhvo.getMatrNo(), "Y");
            if (chkvo != null) {
                owhvo.setOwhNo(chkvo.getOwhNo()); //????????????
                owhvo.setWhNo(chkvo.getWhNo());//????????????
            } else {
                owhvo.setWhNo(0L);//???????????? -> ????????????????????? ?????????
                owhvo.setOwhQty(Float.valueOf(0)); //????????????-> ????????????????????? ?????????
                owhvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                owhvo.setRegId(Long.valueOf(paraMap.get("userId").toString()));
                owhvo.setRegIp(paraMap.get("ipaddr").toString());
            }
            owhvo.setOwhReqQty(Float.valueOf(el.get("needQty").toString())); //????????????
            owhvo.setOwhReqDt(DateUtils.getCurrentBaseDateTime()); //??????????????????
            owhvo.setOwhUnit(Long.parseLong(el.get("makeUnit").toString()));//????????????
            owhvo.setUsedYn("Y");
            owhvo.setCustNo(custNo);
            owhvo = omr.save(owhvo);
            /*Remarked By KMJ At 21.10.26
            Map<String,Object> bmap = new HashMap<String,Object>();
            bmap.put("codeNo",owhvo.getOwhNo());
            bmap.put("savePath","matr/owh/");
            stockService.makeBarCode(bmap);
            log.info(tag + "?????????????????? ?????? ???........");
             */
        }
        log.info(tag + "???????????? ???......");
    }

    @Override
    public Map<String, Object> getMakeMainProc(Map<String, Object> paraMap) {
        return mapper.getMakeMainProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatList(Map<String, Object> paraMap) {
        return mapper.getMakeStatList(paraMap);
    }

    @Override
    public int getMakeStatListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatMoreList(Map<String, Object> paraMap) {
        return mapper.getMakeStatMoreList(paraMap);
    }

    @Override
    public int getMakeStatMoreListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatMoreListCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getMakeProcList(Map<String, Object> paraMap) {
        return mapper.getMakeProcList(paraMap);
    }


    /*????????? ???????????? ?????? ?????? ????????? ???????????? ?????? ?????????*/
    @Override
    public List<Map<String, Object>> getMakeExtraProc(Map<String, Object> paraMap) {
        return mapper.getMakeExtraProc(paraMap);
    }

    /*?????????????????? ????????? ????????? ???????????? ????????????*/
    private PursInfo savePursInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveReqMatr==>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long confirmPurs = 0L;
        if (Long.parseLong(paraMap.get("par_indc_no").toString()) != 0L) return null;
        /*?????? ?????? remarked ??? ?????? : ???????????? ????????? bom??? ???????????? ??????????????? ????????? ???????????? ???????????? ?????? ?????? ?????????
        ???????????? ?????? ??????????????? ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????????????? ????????? ?????? ?????? ????????? ?????????.
        ?????? 4??? ?????? ?????? ?????????:21.02.26
        */
        log.info(tag + "1.???????????? ????????? ????????? ????????? ???????????? ??????.?????????????????? ===> " + paraMap.get("indcNo"));
        //mapper.initTempMatrPos(paraMap);    //????????????????????? ???????????? ???????????????????????? ?????????
        //mapper.initPursMatr(paraMap);       //????????????????????? ???????????? ???????????? ?????????
        // mapper.initPursInfo(paraMap);       //????????????????????? ???????????? ???????????? ?????????

        List<Map<String, Object>> ds = mapper.chkReqPurs(paraMap); //?????????????????? ?????? ????????? ??????????????? ??????
        if (ds.size() <= 0) return null;

        log.info(tag + "paraMap = " + paraMap.toString());
        PursInfo pivo = new PursInfo();

        log.info(tag + "2.  ?????????????????? ??????.......");
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
            pmvo.setPursSts(confirmPurs);
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
            /* ??????????????? ??????????????? ???????????? ?????? (2021.03.23)
            log.info(tag + "???????????? ???????????? ?????? ???????????? ??????.???????????? = " + chkmvo.getPursMatrNo() + " : " + chkmvo.getMatrNo()); //????????? ????????? ???????????? ????????? ???.
            int vol = Integer.parseInt(el.get("vol").toString());//????????????
            float pursQty = chkmvo.getPursQty(); //????????????

             Map<String,Object> ckmap = new HashMap<String,Object>();
             ckmap.put("matrNo",pmvo.getMatrNo());
            log.info("aaaaaaaaaaaaa = " + ckmap.toString());
             int posCount = mapper.getPosCount(ckmap);

            int storCnt = (int) (pursQty / vol); //????????????=????????????/????????????
            for (int idx = posCount; idx <= posCount + storCnt; idx++) {
                paraMap.put("pursNo", chkmvo.getPursNo());
                paraMap.put("pursMatrNo", chkmvo.getPursMatrNo());
                paraMap.put("matrNo", chkmvo.getMatrNo());
                paraMap.put("iwhQty", 1f);
                paraMap.put("iwhSeq", idx);
                this.saveMatrPos(paraMap);
                pursQty -= vol;
            }
            if (pursQty > 0) {
                log.info(tag + "???????????? ???????????? ????????????????????? ???????????? ??????????????? ??????.???????????? = " + chkmvo.getPursMatrNo() + " : " + chkmvo.getMatrNo()); //????????? ????????? ???????????? ????????? ???.?????? ???.
                paraMap.put("pursNo", chkmvo.getPursNo());
                paraMap.put("pursMatrNo", chkmvo.getPursMatrNo());
                paraMap.put("matrNo", chkmvo.getMatrNo());
                paraMap.put("iwhQty", pursQty);
                paraMap.put("iwhSeq", storCnt + 1);
                this.saveMatrPos(paraMap);
            }
            */
        }
        log.info(tag + "???????????? ?????? ?????? ???/ ???????????? = " + ds.size());
        log.info(tag + "???????????? ?????????????????? ?????? ??????.????????????(pursNo) = " + pivo.getPursNo());

        /*Remarked By KMJ At 21.10.25
        Map<String,Object> bmap = new HashMap<String,Object>();
        bmap.put("codeNo",pivo.getPursNo());
        bmap.put("savePath","purs/");
        stockService.makeBarCode(bmap);
        log.info(tag + "???????????? ?????????????????? ?????? ???........");
        log.info(tag + "?????????????????? ???.......");
        */

        return pivo;
    }

    private void saveMatrIwh(PursInfo pivo) {
        String tag = "MakeIndcService.svaematrIwh => ";
        log.info(tag + "????????? BOM??? ???????????? ??????????????? ????????? ???????????? ???????????? ????????? ?????? ??????.????????????(pursNo) = " + pivo.getPursNo());
        Long custNo = pivo.getCustNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> pmap = new HashMap<String, Object>();
        pmap.put("pursNo", pivo.getPursNo());
        pmap.put("pageNo", 0);
        pmap.put("custNo", custNo);
        pmap.put("pageSz", 1000);


        List<Map<String, Object>> ds = pursService.getPursMatrList(pmap);
        log.info(tag + "????????????????????? ??? = " + ds.size());
        for (Map<String, Object> el : ds) {
            log.info(tag + "???????????? ??????.pursMatrNo =  " + el.get("pursMatrNo").toString());
            MatrIwh mivo = new MatrIwh();
            mivo.setPursNo(Long.valueOf(el.get("pursNo").toString()));
            mivo.setPursMatrNo(Long.valueOf(el.get("pursMatrNo").toString()));
            mivo.setCmpyNo(0L); //?????????????????? ?????? ?????? ????????? ???????????? ???.
            mivo.setMatrNo(Long.valueOf(el.get("matrNo").toString()));
            try {
                mivo.setIwhDt(sdf.parse(el.get("pursDt").toString())); //??????????????? ??????????????? ?????? ???
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mivo.setIwhQty(Float.valueOf(el.get("pursQty").toString()));
            mivo.setWhNo(0L);//????????????????????? ?????? ?????? ????????? ?????? ?????? ???,
            mivo.setUsedYn("Y");
            MatrIwh chkMatrIwh = imr.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo, mivo.getPursNo(), mivo.getMatrNo(), "Y");
            if (chkMatrIwh != null) {
                mivo.setIwhNo(chkMatrIwh.getIwhNo());
            }
            try {
                mivo.setRetnQty(Float.valueOf(String.valueOf(el.get("retnQty"))));
            } catch (NullPointerException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            } catch (NumberFormatException ne) {
                mivo.setRetnQty(Float.valueOf(0));
            }
            mivo.setCustNo(custNo);
            imr.save(mivo);
            /*Remarked by KMJ At 21.10.25
            log.info(tag + "????????? ????????? ??????....");
            Map<String,Object> ibmap = new HashMap<String,Object>();
            ibmap.put("codeNo",mivo.getIwhNo());
            ibmap.put("savePath","matr/iwh/");
            stockService.makeBarCode(ibmap);
             */
        }
    }


    //?????????????????? ??????.
    private void saveMatrPos(Map<String, Object> paraMap) {

        String tag = "MakeIndcService.saveMatrPos=>";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MatrPos mpvo = new MatrPos();
        mpvo.setMatrStat(Long.parseLong(env.getProperty("code.stk_iwh_stat")));
        mpvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mpvo.setIwhSeq(Integer.parseInt(paraMap.get("iwhSeq").toString()));
        mpvo.setWhNo(0L);
        mpvo.setColIdx(0);
        mpvo.setRowIdx(0);
        mpvo.setStairIdx(0);

        MatrPos chkmp = matrPosRepo.findByCustNoAndMatrNoAndWhNoAndStairIdxAndColIdxAndRowIdxAndIwhSeq(custNo, mpvo.getMatrNo(), mpvo.getWhNo(), mpvo.getStairIdx(), mpvo.getColIdx(), mpvo.getRowIdx(), mpvo.getIwhSeq());
        if (chkmp != null) { //?????? ??????????????? ???????????? ?????? ???
            mpvo.setMatrPosNo(chkmp.getMatrPosNo());
            mpvo.setIwhSeq(chkmp.getIwhSeq());
            mpvo.setWhNo(chkmp.getWhNo());
            mpvo.setStairIdx(chkmp.getStairIdx());
            mpvo.setRowIdx(chkmp.getRowIdx());
            mpvo.setColIdx(chkmp.getColIdx());
            mpvo.setRegIp(chkmp.getRegIp());
            mpvo.setRegId(chkmp.getRegId());
            mpvo.setRegDt(chkmp.getRegDt());

            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());

            mpvo.setMatrQty(chkmp.getMatrQty());
            mpvo.setUsedYn("Y");
        } else {//?????? ??????
            mpvo.setIwhSeq(Integer.parseInt(paraMap.get("iwhSeq").toString()));
            mpvo.setMatrQty(0f);
            mpvo.setUsedYn("Y");
            mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setModIp(paraMap.get("ipaddr").toString());
            mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mpvo.setRegIp(paraMap.get("ipaddr").toString());
            mpvo.setMatrPosNo(0L);
        }
        mpvo.setCustNo(custNo);
        matrPosRepo.save(mpvo);
    }


    @Override
    public List<Map<String, Object>> getExportPlanList(Map<String, Object> paraMap) {
        return mapper.getExportPlanList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeDailyReportList(Map<String, Object> paraMap) {
        return mapper.getMakeDailyReportList(paraMap);
    }

    @Override
    public int getMakeDailyReportListCount(Map<String, Object> paraMap) {
        return mapper.getMakeDailyReportListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getComboProcList(Map<String, Object> paraMap) {
        return mapper.getComboProcList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeStatusList(Map<String, Object> paraMap) {
        return mapper.getMakeStatusList(paraMap);
    }

    @Override
    public int getMakeStatusListCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatusListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap) {
        return mapper.getWorkerList(paraMap);
    }

    @Transactional //AddOn By KMJ AT 21.08.05 19:50
    @Override
    public void saveIndcRslt(Map<String, Object> passMap) {
        String tag = "MakeIndcService.saveIndcRslt => ";
        log.info(tag + " passMap =>" + passMap.toString());
        Long custNo = Long.parseLong(passMap.get("custNo").toString());
        Long userId = Long.parseLong(passMap.get("userId").toString());
        String ipaddr = passMap.get("ipaddr").toString();
        MakeIndcRslt mirvo = new MakeIndcRslt();
        Map<String, Object> paraMap = (Map<String, Object>) passMap.get("indcRslt"); //????????? ?????????
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = paraMap.get("makeDt").toString().substring(0, 10);
            mirvo.setMakeDt(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //?????? ??? ?????? ?????? ?????? ??? indcNo ????????? ???
        mirvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        MakeIndc mivo = null;
        ProdInfo pivo = null;
        Float makeQty = 0F;
        Float mess = 0F;

        try {
            mirvo.setAdjMakeWgt(Float.parseFloat(paraMap.get("adjMakeWgt").toString()));
        } catch (NullPointerException e) {
            mirvo.setAdjMakeWgt(0F);
        }

        mivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, mirvo.getIndcNo(), "Y");
        if (mivo != null) {
            pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo, mivo.getProdNo(), "Y");
            if (pivo != null) {
                mess = pivo.getMess();
            }
            try {
                makeQty = Float.parseFloat(paraMap.get("makeQty").toString());
                mirvo.setMakeQty(makeQty);
            } catch (NullPointerException ne) {
            }
            try {
                //log.info(tag + "???????????? ?????? ??????.proc_unit_nm =  " + paraMap.get("proc_unit_nm"));
                mirvo.setMakeWgt(makeQty);
            } catch (NullPointerException ne) {
            }
            try {
                mirvo.setWgtQty(Long.parseLong(paraMap.get("wgtQty").toString())); //????????????
            } catch (NullPointerException ne) {
                mirvo.setWgtQty(0L);
            }
            try {
                mirvo.setSznQty(Long.parseLong(paraMap.get("sznQty").toString())); //????????????
            } catch (NullPointerException ne) {
                mirvo.setSznQty(0L);
            }
            try {
                mirvo.setMetalQty(Long.parseLong(paraMap.get("metalQty").toString())); //????????????
            } catch (NullPointerException ne) {
                mirvo.setMetalQty(0L);
            }
            try {
                mirvo.setPackQty(Long.parseLong(paraMap.get("packQty").toString())); //???????????????
            } catch (NullPointerException ne) {
                mirvo.setPackQty(0L);
            }

            try {
                mirvo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
            } catch (NullPointerException ne) {

            }

            mirvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mirvo.setModId(Long.parseLong(passMap.get("userId").toString()));
            mirvo.setModIp(passMap.get("ipaddr").toString());
            mirvo.setUsedYn("Y");
            MakeIndcRslt chkvo = mir.findByCustNoAndIndcRsltNoAndUsedYn(custNo, mirvo.getIndcRsltNo(), "Y");
            if (chkvo != null) {
                mirvo.setIndcRsltNo(chkvo.getIndcRsltNo());
                mirvo.setRegId(chkvo.getRegId());
                mirvo.setRegIp(chkvo.getRegIp());
                mirvo.setRegDt(chkvo.getRegDt());
            } else {
                mirvo.setIndcRsltNo(0L);
                mirvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mirvo.setRegId(userId);
                mirvo.setRegIp(ipaddr);
            }
            log.info(tag + "1.1 ???????????? ????????????...");
            mirvo.setCustNo(custNo);
            mirvo = mir.save(mirvo);
            log.info(tag + "1.2 ???????????? ????????????...");


            if (mivo != null) {
                log.info(tag + "??????????????? ?????? ??? ???????????? ????????????");
                mivo.setRealYield(mirvo.getMakeWgt() / mivo.getIndcQty());
                try {
                    mivo.setCtlFillYield(Float.parseFloat(paraMap.get("ctlFillYield").toString()));
                } catch (NullPointerException ne) {
                    mivo.setCtlFillYield(mivo.getCtlFillYield());
                }
                mivo.setRealYield((mirvo.getMakeWgt() / mivo.getIndcQty()) * 100);
                log.info(tag + "??????????????? ?????? ??? ???????????? ????????????.????????????.???????????? = " + mirvo.getMakeWgt());
                log.info(tag + "??????????????? ?????? ??? ???????????? ????????????.????????????.???????????? = " + mivo.getIndcQty());
                log.info(tag + "??????????????? ?????? ??? ???????????? ????????????.???????????? = " + mivo.getRealYield());
                log.info(tag + "??????????????? ?????? ??? ???????????? ????????????.????????????????????? = " + mivo.getCtlFillYield());
                log.info(tag + "????????? ????????????");
                float faultQty = 0f;
                if (mivo != null && pivo != null) {
                    faultQty = (float) (mirvo.getMetalQty() + mirvo.getPackQty() + mirvo.getSznQty() + mirvo.getWgtQty());
                    float indcQty = mivo.getIndcQty();
                    mivo.setFaultRt((faultQty / indcQty) * 100);
                    log.info(tag + "????????? ????????????.????????? = " + mivo.getFaultRt());
                }
                //?????? ??????????????? ??????????????? ?????? ???, ?????? ?????????????????? indc_sts 2404??? ??????
                int procCd = Integer.parseInt(passMap.get("procCd").toString());
                if (procCd == 999) {
                    Long parIndcNo = Long.parseLong(paraMap.get("parIndcNo").toString()); //?????????????????? parIndcNo ????????????.
                    MakeIndc chkmivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, parIndcNo, "Y");
                    if (chkmivo != null) {
                        //SOL AddOn By KMJ At 21.08.05 23:00
                        chkmivo.setModDt(DateUtils.getCurrentBaseDateTime());
                        chkmivo.setModId(userId);
                        chkmivo.setModIp(ipaddr);
                        try {
                            if (paraMap.get("closYn").toString().equals("Y")) { //????????????
                                chkmivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));
                                chkmivo.setClosYn("Y");
                            } else {
                                chkmivo.setClosYn("N");
                            }
                            makeIndcRepo.save(chkmivo);
                        } catch (NullPointerException ne) {

                        }
                        //EOL AddOn By KMJ At 21.08.05 23:00

                    }
                    //SOL AddOn By KMJ At 21.12.01 07:57 --??????????????? ??????????????? closYn -'Y' ??? ?????? ???????????? ??????????????????
                    chkmivo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, mivo.getIndcNo(), "Y");
                    if (chkmivo != null) {
                        try {
                            chkmivo.setModDt(DateUtils.getCurrentBaseDateTime());
                            chkmivo.setModId(userId);
                            chkmivo.setModIp(ipaddr);
                            if (paraMap.get("closYn").toString().equals("Y")) { //????????????
                                chkmivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));
                                chkmivo.setClosYn("Y");
                            } else {
                                chkmivo.setClosYn("N");
                            }
                            makeIndcRepo.save(chkmivo);
                        } catch (NullPointerException ne) {

                        }
                    }
                    //EOL AddOn By KMJ At 21.12.01 07:57

                } else { //AddOn by KMJ At 21.08.05 19:50 --?????????????????? ?????? ????????? ???????????? ??????.
                    mivo.setCustNo(custNo);
                    makeIndcRepo.save(mivo);
                }
            }


            /*Remarked By KMJ AT 21.12.01 - T?????????????????? ??????????????? ?????? ??????????????? ???????????? ??? ??? ??????.
            int procCd = Integer.parseInt(passMap.get("procCd").toString());
            if (procCd != 999) return;

            Long whNo = Long.parseLong(paraMap.get("whNo").toString());
            if (whNo == 0) return; //????????????????????? ??????????????? ???????????? ????????? ??? (???????????? ???????????? ?????? skip)

            log.info(tag + "2.1 ?????? ???????????? ??????...");
            passMap.put("indcRsltNo", mirvo.getIndcRsltNo());
            ProdIwh iwhvo = new ProdIwh();
            Map<String, Object> rmap = new HashMap<String, Object>();

            iwhvo.setIndcRsltNo(mirvo.getIndcRsltNo());
            iwhvo.setProdNo(Long.parseLong(passMap.get("prodNo").toString()));
            iwhvo.setWhNo(Long.parseLong(passMap.get("whNo").toString()));
            iwhvo.setUsedYn("Y");
            iwhvo.setIwhDt(DateUtils.getCurrentBaseDateTime());
            iwhvo.setIwhQty(mirvo.getMakeQty());
            iwhvo.setModDt(DateUtils.getCurrentBaseDateTime());
            iwhvo.setModId(Long.parseLong(passMap.get("userId").toString()));
            iwhvo.setModIp(passMap.get("ipaddr").toString());

            ProdIwh iwhchkvo = prodIwhRepo.findByCustNoAndIndcRsltNoAndUsedYn(custNo,iwhvo.getIndcRsltNo(), iwhvo.getUsedYn());
            if (iwhchkvo != null) {
                iwhvo.setIwhNo(iwhchkvo.getIwhNo());
                iwhvo.setRegDt(iwhchkvo.getRegDt());
                iwhvo.setRegId(iwhchkvo.getRegId());
                iwhvo.setRegIp(iwhchkvo.getRegIp());
            } else {
                iwhvo.setIwhNo(0L);
                iwhvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                iwhvo.setRegId(userId);
                iwhvo.setRegIp(ipaddr);
            }
            iwhvo.setCustNo(custNo);
            prodIwhRepo.save(iwhvo);

            log.info(tag + "3/2 ???????????? reset ....");
            ProdStk stkvo = new ProdStk();
            stkvo.setProdNo(iwhvo.getProdNo());
            stkvo.setUsedYn("Y");
            stkvo.setWhNo(iwhvo.getWhNo());
            Map<String, Object> stkmap = new HashMap<String, Object>();

            stkmap.put("prodNo", iwhvo.getProdNo());
            stkmap.put("whNo", iwhvo.getWhNo());
            stkvo.setStkQty(mapper.getCurrentProdStock(stkmap));
            stkvo.setStatTrfDt(DateUtils.getCurrentBaseDateTime());
            stkvo.setModId(iwhvo.getModId());
            stkvo.setModIp(iwhvo.getModIp());
            stkvo.setModDt(iwhvo.getModDt());
            stkvo.setStkDt(DateUtils.getCurrentBaseDateTime());
            ProdStk chkstk = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,stkvo.getWhNo(), stkvo.getProdNo(), "Y");
            if (chkstk != null) {
                stkvo.setStkNo(chkstk.getStkNo());
                stkvo.setRegDt(chkstk.getRegDt());
                stkvo.setRegId(chkstk.getRegId());
                stkvo.setRegIp(chkstk.getRegIp());
            } else {
                stkvo.setStkNo(0L);
                stkvo.setRegId(iwhvo.getModId());
                stkvo.setRegIp(iwhvo.getModIp());
                stkvo.setRegDt(iwhvo.getModDt());
            }
            stkvo.setUsedYn("Y");
            stkvo.setCustNo(custNo);
            prodStkRepo.save(stkvo);

            Remarked By KMJ AT 21.12.01 - T?????????????????? ??????????????? ?????? ??????????????? ???????????? ??? ???.???*/
        }
    }

    @Transactional
    @Override
    public void dropIndcRslt(Map<String, Object> paraMap) {
        mapper.dropIndcRslt(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMpUsedList(Map<String, Object> paraMap) {
        return mapper.getMpUsedList(paraMap);
    }

    @Override
    public int getMpUsedListCount(Map<String, Object> paraMap) {
        return mapper.getMpUsedListCount(paraMap);
    }

    @Override
    public int getMaxMakeCapacityPerDay(Map<String, Object> paraMap) {
        return mapper.getMaxMakeCapacityPerDay(paraMap);
    }

    @Override
    public void getMpDropInfo(Map<String, Object> paraMap) {
        mapper.getMpDropInfo(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMpUsedDetlList(Map<String, Object> paraMap) {
        return mapper.getMpUsedDetlList(paraMap);
    }

    @Override
    public int getMpUsedDetlListCount(Map<String, Object> paraMap) {
        return mapper.getMpUsedDetlListCount(paraMap);
    }

    //@Transactional
    @Override
    public void saveMpInfo(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.saveMpInfo => ";
        Map<String, Object> passMap = (Map<String, Object>) paraMap.get("mpi");
        //paraMap : {mpi={indcNo=151239, indcMpNo=null, procCd=722, mpUsedDt=2021-04-01T00:00:00.000Z, frHm=0700, toHm=1700, regulWorkHm=600, over_work_hm=0, exchg_work_hm=0, totWorkHm=600},
        //               workerList=[{workerId=28053, worker_nm=?????????}, {workerId=28038, worker_nm=?????????}, {workerId=2, worker_nm=?????????}]
        //               , userId=4} , request : org.apache.catalina.connector.RequestFacade@57bd70c0 ,


        List<Map<String, Object>> dsWorker = (List<Map<String, Object>>) paraMap.get("workerList");
        log.info(tag + "???????????? ????????? = " + dsWorker.size());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        for (Map<String, Object> el : dsWorker) {
            MakeIndcMp mpInfo = new MakeIndcMp();
            mpInfo.setUserId(Long.parseLong(el.get("workerId").toString()));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = passMap.get("mpUsedDt").toString().substring(0, 10);
                mpInfo.setMpUsedDt(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            }

            //mpInfo.setIndcNo(Long.parseLong(passMap.get("indcNo").toString()));

            //chkIndcInfo = ??????????????? ????????? ?????? ?????? ??????????????? ??????????????? ??????????????? ????????? ?????????????????? ????????? ??????????????? ??????.
            passMap.put("userId", paraMap.get("userId"));
            passMap.put("ipaddr", paraMap.get("ipaddr"));

            //log.info(tag + "????????? ?????? ???????????? = " + passMap.get("indcNo").toString());
            //Long rcvIndcNo = Long.parseLong(passMap.get("indcNo").toString());
            try {
                mpInfo.setIndcMpNo(Long.parseLong(passMap.get("indcMpNo").toString()));
            } catch (NullPointerException ne) {
                mpInfo.setIndcMpNo(0L);
            }
            mpInfo.setIndcNo(Long.parseLong(passMap.get("indcNo").toString()));
            //mpInfo.setIndcNo(this.chkIndcInfo(passMap));
            //log.info(tag + "????????? ?????? ???????????? = " + mpInfo.getIndcNo());
            //if (rcvIndcNo != mpInfo.getIndcNo()) {
            //    try {
            //        makeMpRepo.deleteById(indcMpNo);
            //    }
            //   catch(EmptyResultDataAccessException ee) {
            //    }
            //}
            mpInfo.setFrHm(passMap.get("frHm").toString());//????????????
            mpInfo.setToHm(passMap.get("toHm").toString());//????????????
            mpInfo.setRegulWorkHm(Integer.parseInt(passMap.get("regulWorkHm").toString()));//????????????
            mpInfo.setOverWorkHm(Integer.parseInt(passMap.get("over_work_hm").toString())); //????????????
            mpInfo.setExchgWorkHm(Integer.parseInt(passMap.get("exchg_work_hm").toString())); //??????????????????
            mpInfo.setTotWorkHm(Integer.parseInt(passMap.get("totWorkHm").toString())); //???????????????

            mpInfo.setUsedYn("Y");
            mpInfo.setSpotNo(0L);
            //MakeIndcMp chkvo = makeMpRepo.findByIndcNoAndUserIdAndUsedYn(mpInfo.getIndcNo(), mpInfo.getUserId(), "Y");
            MakeIndcMp chkvo = makeMpRepo.findByCustNoAndIndcMpNoAndUsedYn(custNo, mpInfo.getIndcMpNo(), "Y");
            if (chkvo != null) {
                mpInfo.setRegIp(chkvo.getRegIp());
                mpInfo.setRegDt(chkvo.getRegDt());
                mpInfo.setRegDt(chkvo.getRegDt());

                mpInfo.setModDt(mpInfo.getModDt());
                mpInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                mpInfo.setModIp(paraMap.get("ipaddr").toString());
            } else {
                mpInfo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mpInfo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                mpInfo.setRegIp(paraMap.get("ipaddr").toString());
            }
            mpInfo.setCustNo(custNo);
            makeMpRepo.save(mpInfo);
        }
    }

    private Long chkIndcInfo(Map<String, Object> paraMap) {
        Long retVal = 0L;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc indcvo = new MakeIndc();
        indcvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        indcvo.setProcCd(Long.parseLong(paraMap.get("procCd").toString()));
        MakeIndc orgvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, indcvo.getIndcNo(), "Y");
        if (orgvo != null) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo, indcvo.getIndcNo(), indcvo.getProcCd(), "Y");
            if (chkvo != null) {
                retVal = chkvo.getIndcNo();
            } else { //????????? ???????????? ????????? ????????? ????????? ??????????????? ?????? ?????????.
                MakeIndc newIndcVo = new MakeIndc();
                newIndcVo.setIndcNo(0L);
                newIndcVo.setIndcCont(orgvo.getIndcCont());
                newIndcVo.setIndcQty(orgvo.getIndcQty());
                newIndcVo.setProcCd(indcvo.getProcCd());
                newIndcVo.setBuf(orgvo.getBuf());
                newIndcVo.setMakeFrDt(orgvo.getMakeFrDt());
                newIndcVo.setMakeToDt(orgvo.getMakeToDt());
                newIndcVo.setIdxNo(orgvo.getIdxNo());
                newIndcVo.setMakeUnit(orgvo.getMakeUnit());
                newIndcVo.setMaxMakeQty(orgvo.getMaxMakeQty());
                newIndcVo.setOrdNo(orgvo.getOrdNo());
                newIndcVo.setParIndcNo(orgvo.getParIndcNo());
                newIndcVo.setProdNo(orgvo.getProdNo());
                newIndcVo.setStatCd(orgvo.getStatCd());
                newIndcVo.setUsedYn("Y");
                newIndcVo.setModDt(DateUtils.getCurrentBaseDateTime());
                newIndcVo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                newIndcVo.setModIp(paraMap.get("ipaddr").toString());
                newIndcVo.setRegDt(DateUtils.getCurrentBaseDateTime());
                newIndcVo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                newIndcVo.setRegIp(paraMap.get("ipaddr").toString());
                chkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, newIndcVo.getIndcNo(), "Y");
                if (chkvo == null) {
                    newIndcVo.setIndcNo(0L);
                    newIndcVo.setCustNo(custNo);
                    newIndcVo = makeIndcRepo.save(newIndcVo);
                }
                retVal = newIndcVo.getIndcNo();
            }
        }
        return retVal;

    }

    @Transactional
    public void loadMpByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "MatrService.loadRawMatByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        FileInfo fileEntity = fr.findByCustNoAndFileNoAndUsedYn(custNo, fileNo, "Y");

        buf.setLength(0);
        buf.append(fileRoot)
                .append(fileEntity.getRegId()).append(File.separator)
                .append(fileNo).append(File.separator)
                .append(fileEntity.getSaveFileNm());

        String absFilePath = buf.toString();

        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();
        log.info(tag + "excel???????????? = " + rows);

        for (rowindex = 0; rowindex < rows - 1; rowindex++) {
            if (rowindex <= 1) continue; //???????????? skip
            UserInfo userInfo = new UserInfo();
            MakeIndcMp mpInfo = new MakeIndcMp();
            FileInfo fileInfo = new FileInfo();

            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;

            int cells = row.getPhysicalNumberOfCells();
            mpInfo.setUserId(0L);
            log.info(tag + " rowindex = " + rowindex);//kill

            String userNm = row.getCell(2).getStringCellValue();
            //userInfo = ur.findByUserNm(userNm);

            Date Date = row.getCell(3).getDateCellValue();
            String frhm = substring(row.getCell(5).getStringCellValue(), 0, 2) + substring(row.getCell(5).getStringCellValue(), 3, 5);
            String tohm = substring(row.getCell(6).getStringCellValue(), 0, 2) + substring(row.getCell(6).getStringCellValue(), 3, 5);


            Long starthh = Long.valueOf(substring(row.getCell(5).getStringCellValue(), 0, 2));
            Long startmm = Long.valueOf(substring(row.getCell(5).getStringCellValue(), 3, 5));
            Long endhh = Long.valueOf(substring(row.getCell(6).getStringCellValue(), 0, 2));
            Long endmm = Long.valueOf(substring(row.getCell(6).getStringCellValue(), 3, 5));

            Long Starthm = ((starthh * 60) + startmm);
            Long Endhm = ((endhh * 60) + endmm);


            // 7??????????????? 7????????????
            if (Starthm < 420) {
                Starthm = Long.valueOf(420);
            }
            Long Tot = Endhm - Starthm;
            log.info(" ???????????? = " + Tot);

            try {
                userInfo.getUserId();
                mpInfo.setUserId(userInfo.getUserId());
                mpInfo.setMpUsedDt(Date);
                mpInfo.setFrHm(frhm);
                mpInfo.setToHm(String.valueOf(Endhm));

                if (Tot >= 570) { //????????????  9??? 30???
                    mpInfo.setRegulWorkHm(570);
                    log.info("570??? ???????????? ???????????? 570 ?????? ??????" + mpInfo.getRegulWorkHm());
                } else {
                    mpInfo.setRegulWorkHm(Integer.parseInt(String.valueOf(Tot)));
                    log.info("???????????? ????????????" + mpInfo.getRegulWorkHm());
                }
                if (Tot > 570) { //???????????? 570????????????????????????
                    mpInfo.setExchgWorkHm(Integer.parseInt(String.valueOf(((Tot - 570) * 15) / 10)));

                    log.info("570??????????????? ????????????" + mpInfo.getExchgWorkHm());
                } else {         // ???????????? 0???
                    mpInfo.setExchgWorkHm(Integer.parseInt(String.valueOf(0)));

                    log.info("570??????????????? 0??????" + mpInfo.getExchgWorkHm());
                    mpInfo.setTotWorkHm(Integer.parseInt(String.valueOf(Tot)));
                    log.info("?????? ?????? ??????" + mpInfo.getTotWorkHm());
                }

                if (Tot > 570) { //???????????? ????????????
                    mpInfo.setOverWorkHm(Integer.parseInt(String.valueOf(Tot - 570)));
                    log.info("?????? 570??? ???????????? ????????????" + mpInfo.getOverWorkHm());
                } else {
                    mpInfo.setOverWorkHm(Integer.parseInt(String.valueOf(0)));
                    log.info("????????? 0" + mpInfo.getOverWorkHm());
                }
                mpInfo.setUsedYn("Y");

                mpInfo.setTotWorkHm(Integer.parseInt(String.valueOf(570 + Integer.parseInt(String.valueOf(((Tot - 570) * 15) / 10)))));
                log.info("?????? ?????? ??????" + mpInfo.getTotWorkHm());
                log.info("????????????" + mpInfo.getTotWorkHm());
            } catch (NullPointerException e) {
                mpInfo.setSpotNo(0L);
                mpInfo.setIndcMpNo(0L);
                mpInfo.setIndcNo(0L);
            }
            mpInfo.setCustNo(custNo);
            makeMpRepo.save(mpInfo);
        }
    }

    /*????????? ??? ?????? ????????? : ????????? ????????? ?????????*/
    @Override
    public Map<String, Object> getMaxMakeCapacity(Map<String, Object> paraMap) {
        return mapper.getMaxMakeCapacity(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOperProdList(Map<String, Object> paraMap) {
        return mapper.getOperProdList(paraMap);
    }

    @Override
    public Map<String, Object> getStartProc(Map<String, Object> paraMap) {
        return mapper.getStartProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMoniterItemList(Map<String, Object> paraMap) {
        return mapper.getMoniterItemList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getManPowerSummaryList(Map<String, Object> paraMap) {
        return mapper.getManPowerSummaryList(paraMap);
    }

    @Override
    public int getManPowerSummaryListCount(Map<String, Object> paraMap) {
        return mapper.getManPowerSummaryListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getManPowerList(Map<String, Object> paraMap) {
        return mapper.getManPowerList(paraMap);
    }

    @Override
    public int getManPowerListCount(Map<String, Object> paraMap) {
        return mapper.getManPowerListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWgtchkSummaryList(Map<String, Object> paraMap) {
        return mapper.getWgtchkSummaryList(paraMap);
    }

    @Override
    public int getWgtchkSummaryListCount(Map<String, Object> paraMap) {
        return mapper.getWgtchkSummaryListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getWgtchkDiaryList(Map<String, Object> paraMap) {
        return mapper.getWgtchkDiaryList(paraMap);
    }

    @Override
    public int getWgtchkDiaryListCount(Map<String, Object> paraMap) {
        return mapper.getWgtchkDiaryListCount(paraMap);
    }

    /*???????????? ????????????
      ???????????? (??????????????? 999??? indcNo)
     */
    @Override
    public void autoSaveIndcMp(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.autoSaveMp =?";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> procMap = new HashMap<String, Object>();
        procMap.put("indcNo", Long.parseLong(paraMap.get("indcNo").toString()));
        List<Map<String, Object>> ds = mapper.getForMpMakerIndcList(procMap);// Excel??? ???????????? ????????? ??????
        int idx = -1;
        Long procCd = 0L;
        MakeIndcMp mpvo = new MakeIndcMp();
        Map<String, Object> mpmap = new HashMap<String, Object>();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (++idx < ds.size()) {
            try {
                mpmap = ds.get(idx);
                mpvo.setIndcNo(Long.parseLong(mpmap.get("indcNo").toString()));
                mpvo.setUserId(Long.parseLong(mpmap.get("userId").toString()));
                mpvo.setExchgWorkHm(0);
                mpvo.setOverWorkHm(0);
                mpvo.setFrHm(mpmap.get("frHm").toString());
                mpvo.setToHm(mpmap.get("toHm").toString());

                mpvo.setUsedYn("Y");
                mpvo.setRegulWorkHm(Integer.parseInt(mpmap.get("regulWorkHm").toString()));
                mpvo.setTotWorkHm(Integer.parseInt(mpmap.get("totWorkHm").toString()));
                try {
                    mpvo.setMpUsedDt(sdf.parse(mpmap.get("makeDt").toString()));
                } catch (NullPointerException en) {
                    mpvo.setMpUsedDt((Date) paraMap.get("makeDt"));
                }


                mpvo.setModDt(DateUtils.getCurrentBaseDateTime());
                try {
                    mpvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                } catch (NullPointerException en) {
                    mpvo.setModId(2L);
                }
                try {
                    mpvo.setModIp(paraMap.get("ipaddr").toString());
                } catch (NullPointerException en) {
                    mpvo.setModIp("127.0.0.1");
                }

                MakeIndcMp chkvo = makeMpRepo.findByCustNoAndIndcNoAndUserIdAndUsedYn(custNo, mpvo.getIndcNo(), mpvo.getUserId(), "Y");
                if (chkvo != null) {
                    mpvo.setIndcMpNo(chkvo.getIndcMpNo());
                    mpvo.setRegDt(chkvo.getRegDt());
                    mpvo.setRegId(chkvo.getRegId());
                    mpvo.setRegIp(chkvo.getRegIp());

                } else {
                    mpvo.setIndcMpNo(0L);
                    mpvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    try {
                        mpvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    } catch (NullPointerException en) {
                        mpvo.setRegId(2L);
                    }
                    try {
                        mpvo.setRegIp(paraMap.get("ipaddr").toString());
                    } catch (NullPointerException en) {
                        mpvo.setRegIp("127.0.0.1");
                    }
                }
                mpvo.setCustNo(custNo);
                makeMpRepo.save(mpvo);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getMakeStatusReport(Map<String, Object> paraMap) {
        return mapper.getMakeStatusReport(paraMap);
    }

    @Override
    public int getMakeStatusReportCount(Map<String, Object> paraMap) {
        return mapper.getMakeStatusReportCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> makeIndcProc(Map<String, Object> paraMap) {
        return mapper.getmakeIndcProc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcPrintList(Map<String, Object> paraMap) {
        return mapper.getMakeIndcPrintList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMakeIndcBfPrintList(Map<String, Object> paraMap) {
        List<Map<String, Object>> printList = (List<Map<String, Object>>) paraMap.get("bomList");
        List<Map<String, Object>> rList = new ArrayList<>();
        for (Map<String, Object> el : printList) {
            String prodNm = el.get("matrNm").toString();

            Map<String, Object> indcChk = new HashMap<>();
            indcChk.put("prodNm", prodNm);
            indcChk.put("bomLvl", 2);

            rList = mapper.getMakeIndcBfPrintList(indcChk);
            if (rList.size() > 0) {
                return rList;
            }

        }
        return rList;
    }

    @Override
    public List<Map<String, Object>> getProcCtntList(Map<String, Object> paraMap) {
        return mapper.getProcCtntList(paraMap);
    }

    @Override
    public void resetIndcSts(Map<String, Object> paraMap) {
        mapper.resetIndcSts(paraMap);
    }

    @Override
    public List<Map<String, Object>> getNeedProdBomList(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.getNeedProdBomList => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        /*SOL AddOn By KMJ AT 21.09.02 : ??????????????? ????????? ???????????? ?????? ????????? : ??????????????? ?????? ?????????.*/
        float ctlFillYield = Float.parseFloat(paraMap.get("ctlFillYield").toString());
        ctlFillYield *= 0.01; //?????????(%) ????????? 0.1??? ??????
        paraMap.put("ctlFillYield", ctlFillYield);
        /*EOL AddOn By KMJ AT 21.09.02*/
        return mapper.getNeedProdBomList(paraMap);
    }

    //???????????? - ???????????? ???????????? ??????
    @Override
    public List<Map<String, Object>> getMatchIndcList(Map<String, Object> paraMap) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> ds = (List<Map<String, Object>>) paraMap.get("indcList");

        for (Map<String, Object> el : ds) {
            Map<String, Object> passMap = new HashMap<String, Object>();
            passMap.put("indcNo", el.get("indcNo"));

            Map<String, Object> yn = mapper.getMatchIndcList(passMap);
            log.info("yn : " + yn);
            if (yn == null) {
                passMap.put("stkSts", "N");
                passMap.put("matrIwhDt", "");
            } else {
                log.info("yn : " + yn);
                passMap.put("stkSts", yn.get("stkSts"));
                try {
                    passMap.put("matrIwhDt", yn.get("matrIwhDt").toString().substring(0, 10));
                } catch (NullPointerException ne) {
                    passMap.put("matrIwhDt", "");
                }

            }


            list.add(passMap);
        }
        log.info("list??? " + list);
        return list;
    }

    @Override
    public List<Map<String, Object>> getIndcListByProc(Map<String, Object> paraMap) {
        return mapper.getIndcListByProc(paraMap);
    }

    @Override
    public int getFaultListCount(Map<String, Object> paraMap) {
        return mapper.getFaultListCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getIndcSaltList(Map<String, Object> paraMap) {
        return mapper.getIndcSaltList(paraMap);
    }

    @Override
    public int getIndcSaltListCount(Map<String, Object> paraMap) {
        return mapper.getIndcSaltListCount(paraMap);
    }

    @Override
    public void saveIndcSaltList(Map<String, Object> paraMap) {
        Long prodNo = 0L;
        Long indcNo = 0L;
        Float val = 0F;
        MakeIndcRslt mirvo = new MakeIndcRslt();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        paraMap.put("searchTp", "first");
        Map<String, Object> saltMap = mapper.getIndcListBySalt(paraMap);
        if (Float.parseFloat(saltMap.get("parIndcNo").toString()) == 0F) {
            indcNo = Long.parseLong(saltMap.get("indcNo").toString());
        } else {
            indcNo = Long.parseLong(saltMap.get("parIndcNo").toString());
        }


        Long procCd = Long.parseLong(paraMap.get("procCd").toString());

        prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo, prodNo, "Y");
        if (chk != null) {

            //??????????????? ?????? vol??? mess ?????? X
            try {
                if (custNo == 3) {
                    val = chk.getSpga();
                } else {
                    val = chk.getVol();
                }
            } catch (NullPointerException e) {
                val = chk.getVol();
            }
        }

        MakeIndc michkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo, indcNo, procCd, "Y");
        if (michkvo != null) {
            indcNo = michkvo.getIndcNo();
        }

        MakeIndcRslt chkvo = mir.findByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
        if (chkvo != null) {
            chkvo.setModDt(DateUtils.getCurrentBaseDateTime());
            chkvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            chkvo.setModIp(paraMap.get("ipaddr").toString());
            chkvo.setMakeQty(Float.parseFloat(paraMap.get("makeQty").toString()));
            chkvo.setMakeWgt(Float.parseFloat(paraMap.get("makeQty").toString()) * val);//??????????????????
            chkvo.setCustNo(custNo);
            mir.save(chkvo);
        } else {
            mirvo.setIndcRsltNo(0L);
            mirvo.setIndcNo(indcNo);
            mirvo.setAdjMakeQty(0F);
            mirvo.setAdjMakeWgt(0F);

            mirvo.setMakeQty(Float.parseFloat(paraMap.get("makeQty").toString()));
            mirvo.setMakeWgt(Float.parseFloat(paraMap.get("makeQty").toString()) * val);//??????????????????
            mirvo.setMetalQty(0L);
            mirvo.setPackQty(0L);
            mirvo.setSznQty(0L);
            mirvo.setWgtQty(0L);

            mirvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mirvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            mirvo.setRegIp(paraMap.get("ipaddr").toString());
            mirvo.setUsedYn("Y");
            mirvo.setCustNo(custNo);
            mir.save(mirvo);
        }
    }

    @Transactional //AddOn By KMJ AT 21.08.05 19:50
    @Override
    public void saveIndcPrintText(Map<String, Object> paraMap) {
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc michkvo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo, indcNo, "Y");
        try {
            michkvo.setIndcCont(paraMap.get("indcCont").toString());
        } catch (NullPointerException e) {
            michkvo.setIndcCont(null);
        }
        michkvo.setCustNo(custNo);
        makeIndcRepo.save(michkvo);

    }

    @SneakyThrows
    @Transactional
    @Override
    public void planSave(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        MakeWorkPlan mwpvo = new MakeWorkPlan();

        MakeWorkPlan chkvo = makeWorkPlanRepo.findByCustNoAndPlanDtAndBrnchNoAndUsedYn(custNo, sdf.parse(paraMap.get("planDt").toString()), Long.parseLong(paraMap.get("brnchNo").toString()), "Y");

        if (chkvo != null) {
            mwpvo.setBrnchNo(chkvo.getBrnchNo());
            mwpvo.setCustNo(chkvo.getCustNo());
            mwpvo.setPlanDt(chkvo.getPlanDt());
            mwpvo.setPlanNo(chkvo.getPlanNo());
            mwpvo.setUsedYn("Y");

            mwpvo.setTextArea(paraMap.get("textArea").toString());
        } else {
            mwpvo.setBrnchNo(Long.parseLong(paraMap.get("brnchNo").toString()));
            mwpvo.setCustNo(custNo);
            mwpvo.setPlanDt(sdf.parse(paraMap.get("planDt").toString()));
            mwpvo.setPlanNo(0L);
            mwpvo.setUsedYn("Y");

            mwpvo.setTextArea(paraMap.get("textArea").toString());
        }


        makeWorkPlanRepo.save(mwpvo);
    }

    /*????????? ???????????? ???.....*/
    @Transactional
    @Override
    public void saveMakeIndcProc(Map<String, Object> paraMap) {
        String tag = "makeIndcService.saveMakeIndcFullByPlan == > ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long procCd = 0L;
        Long indcNo = Long.parseLong(paraMap.get("indcNo").toString());
        Long prodNo = Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo pivo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo, prodNo, "Y");

        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();

        List<ProcBrnch> dspb = procBrnchRepo.findAllByCustNoAndBrnchNoAndUsedYnOrderByProcSeq(custNo, pivo.getBrnchNo(), "Y");
        for (ProcBrnch el : dspb) {
            procCd = el.getProcCd();

            MakeIndcProc mipvo = new MakeIndcProc();
            mipvo.setIndcNo(indcNo);
            mipvo.setCustNo(custNo);
            mipvo.setProcCd(el.getProcCd());
            mipvo.setMakeQty(el.getMaxMakeQty());
            mipvo.setProcSeq(el.getProcSeq());
            mipvo.setModId(userId);
            mipvo.setModDt(DateUtils.getCurrentBaseDateTime());
            mipvo.setModIp(ipaddr);
            mipvo.setUsedYn("Y");
            MakeIndcProc chkmipvo = makeIndcProcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo, indcNo, procCd, "Y");
            if (chkmipvo == null) {
                mipvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                mipvo.setRegId(userId);
                mipvo.setRegIp(ipaddr);
                makeIndcProcRepo.save(mipvo);
            }
        }
    }


    @Override
    public List<Map<String, Object>> getProductionPlan(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.getProductionPlan =>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return mapper.getProductionPlan(paraMap);
    }



    @Override
    public List<Map<String, Object>> getIndcList(Map<String, Object> paraMap) {
        String tag = "MakeIndcService.getIndcList=> ";
        log.info(tag + "paraMap =  " + paraMap.toString());
        return mapper.getIndcList(paraMap);
    }

    @Override
    public int getIndcListCount(Map<String, Object> paraMap) {
        return mapper.getIndcListCount(paraMap);
    }
}
