package daedan.mes.make.service;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.common.service.util.DateUtils;

import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.domain.MakeIndcMatr;
import daedan.mes.make.domain.MakeIndcMp;
import daedan.mes.make.domain.MakeIndcRslt;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.make.repository.MakeIndcMatrRepository;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.make.repository.MakeIndcRsltRepository;
import daedan.mes.make.repository.MakeMpRepository;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.proc.domain.ProcBrnch;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.proc.service.ProcService;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.prod.service.ProdService;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.repository.ProdStkRepository;
import daedan.mes.stock.service.StockService;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.repository.UserRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service("excelMakeService")
public class ExcelMakeServiceImpl implements ExcelMakeService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private MakeIndcMapper mapper;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private MatrRepository matrRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private ProdOwhRepository prodOwhRepo;

    @Autowired
    private ProdIwhRepository prodIwh;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private MakeIndcRepository makeIndcRepo;
    @Autowired
    private MakeIndcRsltRepository makerslt;
    @Autowired
    private ProcService procService;
    @Autowired
    private ProdService prodService;
    @Autowired
    private StockService stockService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProdStkRepository prodstk;

    @Autowired
    private MakeMpRepository makeMpRepository;

    @Autowired
    private MakeIndcMatrRepository makeIndcMatrRepo;

    @Autowired
    private OrdProdRepository ordprod;

    @Autowired
    private MakeIndcService indcService;



    @Override
    public List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap) {
        return mapper.getComboProdProcList(paraMap);
    }
    @Override
    public List<Map<String, Object>> getMakeIndcMp(Map<String, Object> paraMap) {
        return mapper.getMakeIndcMp(paraMap);
    }

    @Transactional
    @Override
    public void saveMakeProc(Map<String, Object> paraMap) throws Exception { //??????????????? ????????? ???????????? ????????? ??????
        String tag = "makeIndcService.saveMakeProc ==> ";
        log.info(tag + "paraMap =" + paraMap.toString());

        Map<String, Object> procMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> matrMap = new HashMap<String, Object>();
        procMap.put("prodNo", Long.parseLong(paraMap.get("prodNo").toString()));
        List<Map<String, Object>> ds = mapper.getComboProdProcList(procMap);
        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        int idx = 0;
        for (Map<String, Object> el : ds) {
            ++idx;
            paraMap.put("parIndcNo", (idx == 1) ? 0L : svParIndcNo);
            paraMap.put("procCd", Long.parseLong(el.get("value").toString()));
            parIndcNo = saveMake(paraMap);
            if (idx == 1) {
                svParIndcNo = parIndcNo;
                matrMap.put("indcNo", parIndcNo);
                matrMap.put("prodNo", paraMap.get("prodNo"));
                matrMap.put("userId", paraMap.get("userId"));
                matrMap.put("ipaddr", paraMap.get("ipaddr"));
                matrMap.put("indcWgt", Float.parseFloat(paraMap.get("indcWgt").toString()));

                MakeIndc invo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,parIndcNo,"Y");
                if(invo != null){
                    invo.getOrdNo();
                }
                matrMap.put("ordNo",invo.getOrdNo());
                matrMap.put("date",paraMap.get("date"));
                matrMap.put("custNo",custNo);
                list.add(matrMap);
            }
        }


        prodService.makeExcel(list);
    }



    @Override
    public Long saveMake(Map<String, Object> paraMap) throws Exception {
        String tag = "MakeIndcService.saveMake => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc mivo = new MakeIndc();

        mivo.setUsedYn("Y");
        try {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("parIndcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setParIndcNo(0L);
        }
        try {
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setIndcNo(0L);
            mivo.setRegDt(DateUtils.getCurrentDate());
            mivo.setRegId(mivo.getModId());
            mivo.setRegIp(mivo.getModIp());
        }
        mivo.setProcCd(Long.parseLong(paraMap.get("procCd").toString())); //????????????


        if (mivo.getParIndcNo() == 0L) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getParIndcNo(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
            if (chkvo != null) {
                if (chkvo.getParIndcNo() > 0L) {
                    log.info(tag + "???????????? ?????????????????? ?????????.===> ");
                    log.info(tag + "???????????????????????? = " + mivo.getParIndcNo());
                    log.info(tag + "?????????????????? = " + mivo.getIndcNo());
                    log.info(tag + "??????????????? = " + mivo.getProcCd());
                    log.info(tag + "?????? ???????????? ?????????.===> ");

                }
            }
        }

        try {
            mivo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString())); //??????
        } catch (NullPointerException ne) {
            mivo.setProdNo(0L); //??????
        }
        try {
            mivo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString())); //???????????????
        } catch (NullPointerException ne) {
            mivo.setOrdNo(0L); //??????????????????
        }
        try {
            mivo.setIndcWgt(Float.parseFloat(paraMap.get("indcWgt").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setIndcWgt(0F);
        }
        try {
            mivo.setIndcQty(Float.parseFloat(paraMap.get("indcWgt").toString())); //?????????????????? (vol = 1 )
        } catch (NullPointerException ne) {
            mivo.setIndcWgt(null);
        }


        try {
            mivo.setMakeUnit(Long.parseLong(paraMap.get("makeUnit").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setMakeUnit(0L);
        }

        paraMap.put("date",paraMap.get("makeFrDt"));

        mivo.setMaxMakeQty(Float.parseFloat(paraMap.get("indcWgt").toString()));
        mivo.setMaxMakeWgt(Float.parseFloat(paraMap.get("indcWgt").toString()));

//        Map<String, Object> chkmap = new HashMap<String, Object>();
//        chkmap.put("procCd", paraMap.get("procCd"));
//        Map<String, Object> procmap = procService.getProcInfo(chkmap);
//
//        int maxMakeQty = Integer.parseInt(procmap.get("max_make_qty").toString());
//
//        String frd = sdf.format((Date) paraMap.get("makeFrDt"));
//        String tod = sdf.format((Date) paraMap.get("makeToDt"));
//        int dateTerm = DateUtils.getProgressDays(frd, tod);
//        dateTerm++;
//        mivo.setMaxMakeQty((float) (maxMakeQty * dateTerm));

        try {
            mivo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
        } catch (NullPointerException ne) {
            Map<String, Object> scm = new HashMap<String, Object>();
            scm.put("makeFrDt", mivo.getMakeFrDt());
            scm.put("makeToDt", mivo.getMakeToDt());
            mivo.setStatCd(mapper.getMakeIndcStatus(scm)); //???????????? ???????????? ???????????? ????????????(??????,??????,?????? ??????)
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
            mivo.setIndcDt((Date) paraMap.get("date"));
            mivo.setMakeFrDt((Date) paraMap.get("date"));
            mivo.setMakeToDt((Date) paraMap.get("date"));
        } catch (NullPointerException ne) {
            mivo.setIndcDt(DateUtils.getCurrentDateTime());
        }
        try {
            mivo.setIndcCont(paraMap.get("indcCont").toString());
        } catch (NullPointerException ne) {
        }


//        MakeIndc chkvo = makeIndcRepo.findByIndcNoAndProcCdAndUsedYn(mivo.getIndcNo(), mivo.getProcCd(), "Y");
//        if (chkvo != null) {
//            mivo.setIndcNo(chkvo.getIndcNo());
//        } else {
//            mivo.setIndcNo(0L);
//        }
//        if (chkvo == null) {
//            Map<String, Object> bmap = new HashMap<String, Object>();
//            bmap.put("codeNo", mivo.getIndcNo());
//            bmap.put("savePath", "make/");
//            stockService.makeBarCode(bmap);
//        }



        Long prodNo = 0L;
        if (mivo.getParIndcNo() == 0) {
            OrdInfo ordvo = new OrdInfo();
            prodNo = Long.parseLong(paraMap.get("prodNo").toString());
            ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo, "Y");
            if (chk != null) {
                ordvo.setOrdNm(chk.getErpProdNm());
            }
            ordvo.setOrdDt((Date) paraMap.get("date"));
            ordvo.setDlvReqDt((Date) paraMap.get("date"));
            ordvo.setUsedYn("Y");
            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.wait.owh")));
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord.oem")));
            ordvo.setPlcNo(0L);

            try{
                ordvo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
            }catch(NullPointerException en){
                ordvo.setCmpyNo(699693L);
            }
            OrdInfo chkvo = ordRepo.findByCustNoAndOrdNmAndOrdDtAndUsedYn(custNo,ordvo.getOrdNm(),ordvo.getOrdDt(),"Y");
            if(chkvo != null){
                ordvo.setOrdNo(chkvo.getOrdNo());
                ordvo.setModDt(DateUtils.getCurrentDateTime());
                try {
                    ordvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                }catch (NullPointerException en) {
                    ordvo.setModId(0L);
                }
                ordvo.setModIp((String) paraMap.get("ipaddr"));
            }else {
                ordvo.setOrdNo(0L);
                ordvo.setRegIp((String) paraMap.get("ipaddr"));
                try {
                    ordvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                }catch (NullPointerException en) {
                    ordvo.setRegId(0L);
                }
                ordvo.setRegDt(DateUtils.getCurrentDateTime());
            }
            ordvo.setCustNo(custNo);
            ordvo = ordRepo.save(ordvo);

            mivo.setOrdNo(ordvo.getOrdNo());

            OrdProd orvo = new OrdProd();
            orvo.setOrdNo(ordvo.getOrdNo());
            orvo.setProdNo(prodNo);
            orvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
            orvo.setUsedYn("Y");
            orvo.setQtyPerPkg(0);
            orvo.setOrdQty(Float.parseFloat(paraMap.get("indcWgt").toString())); // EA
            orvo.setOrdProdNo(0L);
            orvo.setModDt(DateUtils.getCurrentDateTime());
            try {
                orvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            }catch (NullPointerException en) {
                orvo.setModId(0L);
            }
            orvo.setModIp((String) paraMap.get("ipaddr"));

            orvo.setRegIp((String) paraMap.get("ipaddr"));
            try {
                orvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }catch (NullPointerException en) {
                orvo.setRegId(0L);
            }
            orvo.setRegDt(DateUtils.getCurrentDateTime());
            orvo.setCustNo(custNo);
            ordprod.save(orvo);
        }

        mivo.setIndcSts(Long.parseLong(env.getProperty("code.base.makeEnd")));
        mivo.setCustNo(custNo);
        mivo = makeIndcRepo.save(mivo);

        Long procCd = Long.parseLong(paraMap.get("procCd").toString());
        if ( procCd == 999) {
            MakeIndcRslt rsvo = new MakeIndcRslt();
            rsvo.setIndcNo(mivo.getIndcNo());
            rsvo.setMakeQty(Float.parseFloat(paraMap.get("indcWgt").toString())); // EA??? ?????? ??????
            rsvo.setMakeDt((Date) paraMap.get("date"));
            rsvo.setUsedYn("Y");
            rsvo.setModId(0L);
            rsvo.setMetalQty(0L);
            rsvo.setPackQty(0L);
            rsvo.setSznQty(0L);
            rsvo.setWgtQty(0L);
            rsvo.setModDt(DateUtils.getCurrentDateTime());
            rsvo.setModIp((String) paraMap.get("ipaddr"));
            try {
                rsvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            } catch (NullPointerException en) {
                rsvo.setModId(0L);
            }
            rsvo.setRegIp((String) paraMap.get("ipaddr"));
            try {
                rsvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            } catch (NullPointerException en) {
                rsvo.setRegId(0L);
            }
            rsvo.setRegDt(DateUtils.getCurrentDateTime());
            rsvo.setRegIp((String) paraMap.get("ipaddr"));
            rsvo.setMakeWgt(0F);
            rsvo.setCustNo(custNo);
            makerslt.save(rsvo);

            ProdIwh iwhvo = new ProdIwh();

            iwhvo.setIwhDt((Date) paraMap.get("date"));
            iwhvo.setProdNo( Long.parseLong(paraMap.get("prodNo").toString()));
            iwhvo.setIndcRsltNo(rsvo.getIndcRsltNo());
            Float iwhQty = Float.parseFloat(paraMap.get("indcWgt").toString());
            iwhvo.setIwhQty(iwhQty);
            iwhvo.setUsedYn("Y");
            iwhvo.setWhNo(102744L);
            iwhvo.setModDt(DateUtils.getCurrentDateTime());
            iwhvo.setModIp((String) paraMap.get("ipaddr"));
            try {
                iwhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            } catch (NullPointerException en) {
                iwhvo.setModId(0L);
            }
            try {
                iwhvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            } catch (NullPointerException en) {
                iwhvo.setRegId(0L);
            }
            iwhvo.setRegDt(DateUtils.getCurrentDateTime());
            iwhvo.setRegIp((String) paraMap.get("ipaddr"));
            iwhvo.setCustNo(custNo);
            prodIwh.save(iwhvo); //?????? ??????

//        ProdOwh owhvo = new ProdOwh();
//        try{
//            owhvo.setCmpyNo(Long.parseLong(paraMap.get("cmpyNo").toString()));
//        }catch(NullPointerException en){
//            owhvo.setCmpyNo(699693L);
//        }
//
//        owhvo.setOwhDt((Date) paraMap.get("date"));
//        owhvo.setOwhReqDt((Date) paraMap.get("date"));
//        owhvo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea").toString()));
//        owhvo.setProdNo(prodNo);
//        Float owhQty = iwhQty;
//        owhvo.setOwhQty(owhQty);
//        owhvo.setOwhReqQty(owhQty);
//        owhvo.setWhNo(2L);
//        owhvo.setUsedYn("Y");
//        owhvo.setModDt(DateUtils.getCurrentDate());
//        owhvo.setModId(0L);
//        owhvo.setModIp("127.0.0.1");
//        owhvo.setRegDt(DateUtils.getCurrentDate());
//        owhvo.setRegId(0L);
//        owhvo.setRegIp("127.0.0.1");
//        owhvo.setOrdNo(parIndcNo);
//
//
//        prodOwhRepo.save(owhvo);


            ProdStk stk = new ProdStk();

            stk.setStkDt((Date) paraMap.get("date"));
            stk.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
            stk.setStatTrfDt((Date) paraMap.get("date"));
            stk.setWhNo(102744L);
            stk.setUsedYn("Y");
            stk.setStkQty(iwhQty);

            ProdStk stkchk = prodstk.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,stk.getWhNo(), stk.getProdNo(), "Y");
            if (stkchk != null) {
                stk.setStkNo(stkchk.getStkNo());
                stk.setStkQty(stkchk.getStkQty() + stk.getStkQty());
                stk.setModDt(DateUtils.getCurrentDateTime());
                stk.setModIp((String) paraMap.get("ipaddr"));
                try {
                    iwhvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                } catch (NullPointerException en) {
                    iwhvo.setModId(0L);
                }
            } else {
                stk.setStkNo(0L);
                stk.setRegDt(DateUtils.getCurrentDateTime());
                stk.setRegIp((String) paraMap.get("ipaddr"));
                try {
                    iwhvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                } catch (NullPointerException en) {
                    iwhvo.setRegId(0L);
                }
            }
            stk.setCustNo(custNo);
            prodstk.save(stk); //????????????
        }
        return mivo.getIndcNo();
    }

    @Override
    public void saveMakeIndcMatr(Map<String, Object> paraMap) throws Exception {
        String tag = "makeIndcService.saveMakeIndcMatr ==> ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndcMatr mimvo = new MakeIndcMatr();
        mimvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mimvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        mimvo.setNeedQty(Float.parseFloat(paraMap.get("needQty").toString()));
        MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,mimvo.getIndcNo(), mimvo.getMatrNo(), "Y");
        if (chkvo != null) {
            mimvo.setIndcMatrNo(chkvo.getIndcMatrNo());
        }
        try {
            mimvo.setUsedYn("Y");
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegDt(DateUtils.getCurrentDateTime());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModDt(mimvo.getModDt());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        mimvo.setCustNo(custNo);
        makeIndcMatrRepo.save(mimvo);
    }

    //???????????? ??????,?????? ?????? // BOM??????
    @Transactional
    @Override
    public void makeBomByExcel(HashMap<String, Object> paraMap) throws Exception {
        String tag = "makeIndcService.makeBomByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();;
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());;
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String matrNm = "";

        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        ????????? ??????
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try {
                matrNm = row.getCell(2).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");
            } catch (NullPointerException ne) {
                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
                continue;
            }
            MatrInfo matrInfo = new MatrInfo();

            matrInfo.setMatrNm(matrNm);
            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); //?????????
            matrInfo.setBrnchNo(1L);
            matrInfo.setModDt(DateUtils.getCurrentDate());
            matrInfo.setModId(0L);
            matrInfo.setRegId(0L);
            matrInfo.setUsedYn("Y");
            matrInfo.setPursUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); // ??????(Kg) ??????
            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.cold"))); // ????????????(??????) ??????
            matrInfo.setValidTerm(12); //????????????-12??????

            MatrInfo chkMatr = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkMatr != null) {
                matrInfo.setMatrNo(chkMatr.getMatrNo());
                matrInfo.setRegIp(chkMatr.getRegIp());
                matrInfo.setRegId(chkMatr.getRegId());
                matrInfo.setRegDt(chkMatr.getRegDt());
            } else {
                matrInfo.setMatrNo(0L);
                matrInfo.setModIp("127.0.0.1");
                matrInfo.setModId(2L);
                matrInfo.setModDt(DateUtils.getCurrentDate());
            }
            matrInfo.setCustNo(custNo);
            matrRepo.save(matrInfo);

            //?????? ??????
            try {
                prodNm = row.getCell(0).getStringCellValue();
                prodNm = prodNm.replaceAll("\\p{Z}", "");
            } catch (NullPointerException ne) {
                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
                continue;
            }
            ProdInfo prodInfo = new ProdInfo();

            prodInfo.setProdNm(prodNm);
            prodInfo.setUnitAmt(0);
            prodInfo.setQtyPerPkg(1); //??????????????????(???????????????)
            prodInfo.setFileNo(0L);//???????????????(???????????????)
            prodInfo.setUsedYn("Y");//????????????(???????????????)
            prodInfo.setProdBrnch(1L); //??????(???????????????)
            prodInfo.setSaleUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); // ??????(Kg) ??????
            prodInfo.setRegDt(DateUtils.getCurrentDate());
            prodInfo.setRegId(0L);
            prodInfo.setModDt(DateUtils.getCurrentDate());
            prodInfo.setModId(0L);
            prodInfo.setMess(1000f);

            ProdInfo chkInfo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (chkInfo != null) {
                prodInfo.setProdNo(chkInfo.getProdNo());
                prodInfo.setRegIp(chkInfo.getRegIp());
                prodInfo.setRegId(chkInfo.getRegId());
                prodInfo.setRegDt(chkInfo.getRegDt());
            } else {
                prodInfo.setProdNo(0L);
                prodInfo.setModIp("127.0.0.1");
                prodInfo.setModId(2L);
                prodInfo.setModDt(DateUtils.getCurrentDate());
            }
            prodInfo.setCustNo(custNo);
            prodRepo.save(prodInfo);
        }
    }




    // ???????????? ???????????? ??????
    @Transactional
    @Override
    public void hadamfoodsaveMakeProc(Map<String, Object> paraMap) throws Exception { //??????????????? ????????? ???????????? ????????? ??????
        String tag = "makeIndcService.hadamfoodsaveMakeProc ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> procMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> matrMap = new HashMap<String, Object>();
        procMap.put("prodNo", Long.parseLong(paraMap.get("prodNo").toString()));
        List<Map<String, Object>> ds = mapper.getProdProcList(procMap);
        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;

        int idx = 0;
        for (Map<String, Object> el : ds) {
            ++idx;
            paraMap.put("parIndcNo", (idx == 1) ? 0L : svParIndcNo);
            paraMap.put("procCd", Long.parseLong(el.get("value").toString()));
            parIndcNo = hadamfoodsaveMake(paraMap);
            if (idx == 1) {
                svParIndcNo = parIndcNo;
                matrMap.put("indcNo", parIndcNo);
                matrMap.put("prodNo", paraMap.get("prodNo"));
                matrMap.put("indcQty", Float.parseFloat(paraMap.get("indcQty").toString()));
                list.add(matrMap);
            }
        }
        if (Long.parseLong(paraMap.get("procCd").toString()) == Long.parseLong(env.getProperty("proc.code.complete"))) {
            MakeIndcRslt rsvo = new MakeIndcRslt();
            rsvo.setIndcNo(parIndcNo);
            rsvo.setMakeQty(Float.parseFloat(paraMap.get("indcQty").toString()));
            rsvo.setMakeDt((Date) paraMap.get("date"));
            rsvo.setUsedYn("Y");
            rsvo.setModId(0L);
            rsvo.setMetalQty(0L);
            rsvo.setPackQty(0L);
            rsvo.setSznQty(0L);
            rsvo.setWgtQty(0L);
            try {
                rsvo.setRegIp(paraMap.get("ipaddr").toString());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setRegId(0L);
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setRegDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModIp(paraMap.get("ipaddr").toString());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModId(0L);
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }
            rsvo.setCustNo(custNo);
            makerslt.save(rsvo);
            paraMap.put("IndcRsltNo", rsvo.getIndcRsltNo());
            paraMap.put("custNo",custNo);

            /*???????????? ?????? ??????*/
            indcService.autoSaveIndcMp(paraMap);




        }

        ProdIwh iwhvo = new ProdIwh();
        iwhvo.setIwhDt((Date) paraMap.get("date"));
        iwhvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        iwhvo.setWhNo(567L);
        iwhvo.setIndcRsltNo(Long.parseLong(paraMap.get("IndcRsltNo").toString()));
        iwhvo.setIwhQty(Float.parseFloat(paraMap.get("indcQty").toString()));
        iwhvo.setUsedYn("Y");
        try {
            iwhvo.setRegIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setRegId(0L);
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setRegDt(DateUtils.getCurrentDate());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModId(0L);
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModDt(DateUtils.getCurrentDate());
        } catch (NullPointerException ne) {
        }
        iwhvo.setCustNo(custNo);
        prodIwh.save(iwhvo); //?????? ??????


        Long prodNo = 0L;
        Float iwhQty = 0F;
        Float stkQty =0F;

        prodNo =  Long.parseLong(paraMap.get("prodNo").toString());
        iwhQty = Float.parseFloat(paraMap.get("indcQty").toString());

        ProdStk stk = new ProdStk();

        ProdStk chk = prodstk.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,iwhvo.getWhNo(),prodNo,"Y");
        if(chk != null){
            stkQty = chk.getStkQty();
            stk.setStkQty(iwhQty+stkQty); // ????????? ??????
            stk.setModIp("127.0.0.1");
            stk.setModId(0L);
            stk.setModDt(DateUtils.getCurrentDate());
        }else{
            stk.setStkQty(iwhQty);
            stk.setRegIp("127.0.0.1");
            stk.setRegId(0L);
            stk.setRegDt(DateUtils.getCurrentDate());
        }

        stk.setProdNo(prodNo);
        stk.setStatTrfDt((Date) paraMap.get("date"));
        stk.setWhNo(567L);
        stk.setUsedYn("Y");
        stk.setCustNo(custNo);
        prodstk.save(stk); //????????????

        prodService.hadamfoodmakeExcel(list);
    }


    @Transactional
    @Override
    public Long hadamfoodsaveMake(Map<String, Object> paraMap) throws Exception {
        String tag = "MakeIndcService.hadamfoodsaveMake => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc mivo = new MakeIndc();
        mivo.setUsedYn("Y");
        try {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("parIndcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setParIndcNo(0L);
        }
        try {
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setIndcNo(0L);
            mivo.setRegDt(DateUtils.getCurrentDate());
            mivo.setRegId(mivo.getModId());
            mivo.setRegIp(mivo.getModIp());
        }
        mivo.setProcCd(Long.parseLong(paraMap.get("procCd").toString())); //????????????


        if (mivo.getParIndcNo() == 0L) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getParIndcNo(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
            if (chkvo != null) {
                if (chkvo.getParIndcNo() > 0L) {
                    log.info(tag + "???????????? ?????????????????? ?????????.===> ");
                    log.info(tag + "???????????????????????? = " + mivo.getParIndcNo());
                    log.info(tag + "?????????????????? = " + mivo.getIndcNo());
                    log.info(tag + "??????????????? = " + mivo.getProcCd());
                    log.info(tag + "?????? ???????????? ?????????.===> ");

                }
            }
        }

        try {
            mivo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString())); //??????
        } catch (NullPointerException ne) {
            mivo.setProdNo(0L); //??????
        }
        try {
            mivo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString())); //???????????????
        } catch (NullPointerException ne) {
            mivo.setOrdNo(0L); //??????????????????
        }
        try {
            mivo.setIndcWgt(Float.parseFloat(paraMap.get("indcWgt").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setIndcWgt(null);
        }

        try {
            mivo.setMakeUnit(Long.parseLong(paraMap.get("makeUnit").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setMakeUnit(0L);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mivo.setMakeFrDt((Date) paraMap.get("date"));//???????????????
        mivo.setMakeToDt((Date) paraMap.get("date")); //???????????????

        log.info(tag + "????????? ??????????????? ???????????? ????????? ??????....");
        Map<String, Object> chkmap = new HashMap<String, Object>();
        chkmap.put("procCd", paraMap.get("procCd"));
        Map<String, Object> procmap = procService.getProcInfo(chkmap);

        int maxMakeQty = Integer.parseInt(procmap.get("max_make_qty").toString());

        log.info(tag + "??????????????????????????? ?????? ????????? ??????....");
        String frd = sdf.format((Date) paraMap.get("date"));
        String tod = sdf.format((Date) paraMap.get("date"));
        int dateTerm = DateUtils.getProgressDays(frd, tod);
        dateTerm++;
        mivo.setMaxMakeQty((float) (maxMakeQty * dateTerm));
        log.info(tag + "after dateTerm = " + dateTerm);

        try {
            mivo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
        } catch (NullPointerException ne) {
            Map<String, Object> scm = new HashMap<String, Object>();
            scm.put("date", mivo.getMakeFrDt());
            scm.put("date", mivo.getMakeToDt());
            mivo.setStatCd(mapper.getMakeIndcStatus(scm)); //???????????? ???????????? ???????????? ????????????(??????,??????,?????? ??????)
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
            mivo.setIndcDt((Date) paraMap.get("date"));
        } catch (NullPointerException ne) {
            mivo.setIndcDt(DateUtils.getCurrentDateTime());
        }
        try {
            mivo.setIndcCont(paraMap.get("indcCont").toString());
        } catch (NullPointerException ne) {
        }


        MakeIndc chkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getIndcNo(), mivo.getProcCd(), "Y");
        if (chkvo != null) {
            mivo.setIndcNo(chkvo.getIndcNo());
        } else {
            mivo.setIndcNo(0L);
        }
        /*Remarked By KMJ At 21.10.24
        if (chkvo == null) {
            log.info(tag + "???????????????????????? ????????? ????????? ??????===> ");
            Map<String, Object> bmap = new HashMap<String, Object>();
            bmap.put("codeNo", mivo.getIndcNo());
            bmap.put("savePath", "make/");
            stockService.makeBarCode(bmap);
        }
        */

        Long prodNo = 0L;

        OrdInfo ordvo = new OrdInfo();

        prodNo =  Long.parseLong(paraMap.get("prodNo").toString());
        ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo,"Y");
        if(chk != null){
            ordvo.setOrdNm(chk.getProdNm());
        }
        ordvo.setOrdDt((Date) paraMap.get("date"));
        ordvo.setDlvReqDt((Date) paraMap.get("date"));
        ordvo.setUsedYn("Y");
        ordvo.setCmpyNo(1L);
        ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.wait.owh")));
        ordvo.setOrdTp(Long.parseLong(env.getProperty("ord.oem")));
        ordvo.setPlcNo(0L);
        ordvo.setCustNo(custNo);
        ordRepo.save(ordvo);

        mivo.setOrdNo(ordvo.getOrdNo());

        OrdProd orvo = new OrdProd();
        orvo.setOrdNo(ordvo.getOrdNo());
        orvo.setProdNo(prodNo);
        orvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
        orvo.setRegIp("127.0.0.1");
        orvo.setRegId(0L);
        orvo.setRegDt(DateUtils.getCurrentDate());
        orvo.setModIp("127.0.0.1");
        orvo.setModId(0L);
        orvo.setModDt(DateUtils.getCurrentDate());
        orvo.setUsedYn("Y");
        orvo.setQtyPerPkg(0);
        orvo.setOrdQty(Float.parseFloat(paraMap.get("iwhQty").toString())); // EA
        orvo.setCustNo(custNo);
        ordprod.save(orvo);

        mivo.setCustNo(custNo);
        mivo = makeIndcRepo.save(mivo);
        return mivo.getIndcNo();

    }


    @Transactional
    @Override
    public void hadamfoodsaveMakeIndcMatr(Map<String, Object> paraMap) throws Exception {
        String tag = "makeIndcService.saveMakeIndcMatr ==> ";
        log.info(tag + "paraMap =" + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndcMatr mimvo = new MakeIndcMatr();
        mimvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mimvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        mimvo.setNeedQty(Float.parseFloat(paraMap.get("needQty").toString()));
        MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,mimvo.getIndcNo(), mimvo.getMatrNo(), "Y");

        if (chkvo != null) {
            mimvo.setIndcMatrNo(chkvo.getIndcMatrNo());
        }
        try {
            mimvo.setUsedYn("Y");
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegDt(DateUtils.getCurrentDateTime());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModDt(mimvo.getModDt());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        mimvo.setCustNo(custNo);
        makeIndcMatrRepo.save(mimvo);
    }


    // ???????????? ????????? ????????? ??????
    @Transactional
    @Override
    public void makeMakeIndcMpByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "MakeIndcService.makeMakeIndcMpByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Map<String,Object> Map = new HashMap<String,Object>();

        int rowindex = 0;

        int dtRowindex = Integer.parseInt(env.getProperty("excel_dt_row_no")); // ????????? ????????? ???

        int startMatrRowindex = Integer.parseInt(env.getProperty("excel_matrmp_fr_row_no"));
        int endMatrRowindex = Integer.parseInt(env.getProperty("excel_matrmp_to_row_no"));

        int dtFirstindex = Integer.parseInt(env.getProperty("excel_mp_row_first")); // ?????? ??????
        int dtLastindex = Integer.parseInt(env.getProperty("excel_prod_row_last")); // ?????? ????????? ????????????

        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        XSSFRow rowProdMain = sheet.getRow(dtRowindex); //?????????????????? ????????? ?????? ???


        int rows = sheet.getPhysicalNumberOfRows();
        //???????????? ??????

        String prodCd = "";
        String useddt = "";
        String svUseDt = "";
        String secUseDt = "";
        Long indcNo = 0L;
        Date usedDt = new Date();
        String userNm = "";
        String matcher ="";
        Long userId = 0L;


        XSSFRow row = sheet.getRow(dtRowindex);
        for (int columnindex = dtFirstindex; columnindex <= dtLastindex; columnindex++) {
            row = sheet.getRow(2);
            svUseDt = row.getCell(1).getStringCellValue().replace("???","-"); // ??? ??????
            secUseDt = rowProdMain.getCell(columnindex).getStringCellValue().replace("???",""); // 1~31???
            if(secUseDt == null){continue;}

            useddt = this.getUseddt(sheet, dtRowindex, columnindex, svUseDt, secUseDt); //?????? ??????...

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            for (rowindex = startMatrRowindex; rowindex <= endMatrRowindex; rowindex++) {
                row = sheet.getRow(rowindex);
                if (row == null) continue;
                prodCd = row.getCell(0).getStringCellValue();
                prodCd = prodCd.replaceAll("\\p{Z}", "");

                userNm = row.getCell(1).getStringCellValue();
                UserInfo chkvo = userRepository.findByUserNmAndUsedYn(userNm,"Y"); // ????????? ????????? ????????????
                if(chkvo != null){
                    userId =  chkvo.getUserId();
                }
                matcher = row.getCell(columnindex).getStringCellValue();
                if (!matcher.equals("O")) {
                    continue;
                }

                Map.put("indcDt",useddt); //?????? ??????
                Map.put("indcCd",prodCd); //?????? ??????

                List<Map<String,Object>> ds = this.getMakeIndcMp(Map);
                for (Map<String, Object> el : ds) {
                    MakeIndcMp vo = new MakeIndcMp();
                    usedDt = sdf.parse((String)el.get("make_fr_dt").toString());
                    indcNo = Long.parseLong(el.get("indc_no").toString());
                    vo.setIndcNo(indcNo);
                    vo.setMpUsedDt(usedDt);
                    vo.setUsedYn("Y");
                    vo.setExchgWorkHm(0);
                    vo.setFrHm("0");
                    vo.setOverWorkHm(0);
                    vo.setToHm("0");
                    vo.setTotWorkHm(0);
                    vo.setRegulWorkHm(0);
                    vo.setUserId(userId);
                    vo.setCustNo(custNo);
                    makeMpRepository.save(vo);
                }
            }
        }
    }

    public String getUseddt(XSSFSheet sheet, int dtRowindex, int columnindex, String svUseDt, String secUseDt) {
        StringBuffer buf = new StringBuffer();

        buf.setLength(0);
        buf.append("2021-");
        buf.append(svUseDt);
        buf.append(secUseDt);
        return buf.toString().replaceAll("(\r|\n|\r\n|\n\r)"," ");
    }






    @Override
    public void DaedongsaveMakeProc(Map<String, Object> paraMap) throws Exception { //??????????????? ????????? ???????????? ????????? ??????
        String tag = "makeIndcService.DaedongsaveMakeProc ==> ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> procMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> matrMap = new HashMap<String, Object>();
        procMap.put("prodNo", Long.parseLong(paraMap.get("prodNo").toString()));
        List<Map<String, Object>> ds = mapper.getProdProcList(procMap);
        matrMap.put("cmpy",paraMap.get("cmpy").toString());

        Long parIndcNo = 0L;
        Long svParIndcNo = 0L;

        int idx = 0;
        for (Map<String, Object> el : ds) {
            ++idx;
            paraMap.put("parIndcNo", (idx == 1) ? 0L : svParIndcNo);
            paraMap.put("procCd", Long.parseLong(el.get("value").toString()));
            parIndcNo = DaedongsaveMake(paraMap);
            if (idx == 1) {
                svParIndcNo = parIndcNo;
                matrMap.put("indcNo", parIndcNo);
                matrMap.put("prodNo", paraMap.get("prodNo"));
                matrMap.put("indcQty", Float.parseFloat(paraMap.get("indcQty").toString()));

                MakeIndc invo = makeIndcRepo.findByCustNoAndIndcNoAndUsedYn(custNo,parIndcNo,"Y");
                if(invo != null){
                    invo.getOrdNo();
                }
                matrMap.put("ordNo",invo.getOrdNo());
                matrMap.put("date",paraMap.get("date"));
                matrMap.put("custNo",custNo);
                list.add(matrMap);


            }
        }
        if (Long.parseLong(paraMap.get("procCd").toString()) == Long.parseLong(env.getProperty("proc.code.complete"))) {
            MakeIndcRslt rsvo = new MakeIndcRslt();
            rsvo.setIndcNo(parIndcNo);
            rsvo.setMakeQty(Float.parseFloat(paraMap.get("indcQty").toString())); // EA??? ?????? ??????
            rsvo.setMakeDt((Date) paraMap.get("date"));
            rsvo.setUsedYn("Y");
            rsvo.setModId(0L);
            rsvo.setMetalQty(0L);
            rsvo.setPackQty(0L);
            rsvo.setSznQty(0L);
            rsvo.setWgtQty(0L);
            try {
                rsvo.setRegIp(paraMap.get("ipaddr").toString());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setRegId(0L);
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setRegDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModIp(paraMap.get("ipaddr").toString());
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModId(0L);
            } catch (NullPointerException ne) {
            }

            try {
                rsvo.setModDt(DateUtils.getCurrentDate());
            } catch (NullPointerException ne) {
            }
            rsvo.setMakeWgt(0F);
            rsvo.setCustNo(custNo);
            makerslt.save(rsvo);
            paraMap.put("IndcRsltNo", rsvo.getIndcRsltNo());
        }




        ProdIwh iwhvo = new ProdIwh();

        iwhvo.setIwhDt((Date) paraMap.get("date"));
        iwhvo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString()));
        iwhvo.setWhNo(2L);
        iwhvo.setIndcRsltNo(Long.parseLong(paraMap.get("IndcRsltNo").toString()));
        Float iwhQty = Float.parseFloat(paraMap.get("indcQty").toString());
        iwhvo.setIwhQty(iwhQty);


        iwhvo.setUsedYn("Y");
        try {
            iwhvo.setRegIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setRegId(0L);
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setRegDt(DateUtils.getCurrentDate());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModId(0L);
        } catch (NullPointerException ne) {
        }

        try {
            iwhvo.setModDt(DateUtils.getCurrentDate());
        } catch (NullPointerException ne) {
        }
        iwhvo.setCustNo(custNo);
        prodIwh.save(iwhvo); //?????? ??????


        Long prodNo =  Long.parseLong(paraMap.get("prodNo").toString());

        ProdOwh owhvo = new ProdOwh();
        String cmpy = paraMap.get("cmpy").toString();
        cmpy = cmpy.replaceAll("\\p{Z}", "");

        Long mngrgbnSale = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
        CmpyInfo vo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnSale,cmpy,"Y");
        if(vo != null){
            owhvo.setCmpyNo(vo.getCmpyNo());
        }
        owhvo.setOwhDt((Date) paraMap.get("date"));
        owhvo.setOwhReqDt((Date) paraMap.get("date"));
        owhvo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea").toString()));
        owhvo.setProdNo(prodNo);
        Float owhQty = iwhQty;
        owhvo.setOwhQty(owhQty);
        owhvo.setOwhReqQty(owhQty);
        owhvo.setWhNo(2L);
        owhvo.setUsedYn("Y");
        owhvo.setModDt(DateUtils.getCurrentDate());
        owhvo.setModId(0L);
        owhvo.setModIp("127.0.0.1");
        owhvo.setRegDt(DateUtils.getCurrentDate());
        owhvo.setRegId(0L);
        owhvo.setRegIp("127.0.0.1");
        owhvo.setOrdNo(parIndcNo);

        owhvo.setCmpyNo(custNo);
        prodOwhRepo.save(owhvo);


        ProdStk stk = new ProdStk();

        Float stkQty =  iwhQty - owhQty ;
        stk.setStkDt((Date) paraMap.get("date"));
        stk.setProdNo(prodNo);
        stk.setStatTrfDt((Date) paraMap.get("date"));
        stk.setWhNo(2L);
        stk.setUsedYn("Y");

        ProdStk chk = prodstk.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,owhvo.getWhNo(),prodNo,"Y");
        if(chk != null){
            Float Qty = chk.getStkQty();
            stk.setStkNo(chk.getStkNo());
            stk.setStkNo(chk.getStkNo());
            stk.setStkQty(Qty+stkQty); // ????????? ??????
            stk.setRegIp("127.0.0.1");
            stk.setRegId(0L);
            stk.setRegDt(DateUtils.getCurrentDate());

        }else{
            stk.setStkQty(stkQty);
            stk.setModDt(DateUtils.getCurrentDate());
            stk.setModId(0L);
            stk.setModIp("127.0.0.1");
        }
        stk.setCustNo(custNo);
        prodstk.save(stk); //????????????

        prodService.DaedongmakeExcel(list);
    }

    //?????? ?????? -- make_indc ?????? ????????? ?????? ?????? ??????
    private Date frDt;

    //?????? ?????? -- make_indc ?????? ????????? ?????? ?????? ??????
    private Date toDt;

    @Override
    public Long DaedongsaveMake(Map<String, Object> paraMap) throws Exception {
        String tag = "MakeIndcService.DaedongsaveMake => ";
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        MakeIndc mivo = new MakeIndc();
        mivo.setUsedYn("Y");
        try {
            mivo.setParIndcNo(Long.parseLong(paraMap.get("parIndcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setParIndcNo(0L);
        }
        try {
            mivo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        } catch (NullPointerException ne) {
            mivo.setIndcNo(0L);
            mivo.setRegDt(DateUtils.getCurrentDate());
            mivo.setRegId(mivo.getModId());
            mivo.setRegIp(mivo.getModIp());
        }
        mivo.setProcCd(Long.parseLong(paraMap.get("procCd").toString())); //????????????
        mivo.setIndcSts(Long.parseLong(env.getProperty("code.indcSts.reqMatrOwh")));

        if (mivo.getParIndcNo() == 0L) {
            MakeIndc chkvo = makeIndcRepo.findByCustNoAndParIndcNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getParIndcNo(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
            if (chkvo != null) {
                if (chkvo.getParIndcNo() > 0L) {
                    log.info(tag + "???????????? ?????????????????? ?????????.===> ");
                    log.info(tag + "???????????????????????? = " + mivo.getParIndcNo());
                    log.info(tag + "?????????????????? = " + mivo.getIndcNo());
                    log.info(tag + "??????????????? = " + mivo.getProcCd());
                    log.info(tag + "?????? ???????????? ?????????.===> ");

                }
            }
        }

        try {
            mivo.setProdNo(Long.parseLong(paraMap.get("prodNo").toString())); //??????
        } catch (NullPointerException ne) {
            mivo.setProdNo(0L); //??????
        }
        try {
            mivo.setOrdNo(Long.parseLong(paraMap.get("ordNo").toString())); //???????????????
        } catch (NullPointerException ne) {
            mivo.setOrdNo(0L); //??????????????????
        }
        try {
            mivo.setIndcQty(Float.parseFloat(paraMap.get("indcQty").toString())); //??????????????????
        } catch (NullPointerException ne) {
            mivo.setIndcQty(0F);
        }
        try {
            mivo.setIndcWgt(Float.parseFloat(paraMap.get("indcWgt").toString()));
        } catch (NullPointerException ne) {
            mivo.setIndcWgt(0F);
        }

        if (Long.parseLong(paraMap.get("procCd").toString()) == Long.parseLong(env.getProperty("proc.code.complete"))) {
            try {
                mivo.setMakeUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea"))); //??????????????????
            } catch (NullPointerException ne) {
                mivo.setMakeUnit(0L);
            }
        }else{
            try {
                mivo.setMakeUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea"))); //??????????????????
            } catch (NullPointerException ne) {
                mivo.setMakeUnit(0L);
            }
        }




        log.info(tag + "????????? ??????????????? ???????????? ????????? ??????....");
        Map<String, Object> chkmap = new HashMap<String, Object>();
        chkmap.put("procCd", paraMap.get("procCd"));
        Map<String, Object> procmap = procService.getProcInfo(chkmap);

        int maxMakeQty = Integer.parseInt(procmap.get("max_make_qty").toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        log.info(tag + "??????????????????????????? ?????? ????????? ??????....");
        String frd = sdf.format((Date) paraMap.get("date"));
        String tod = sdf.format((Date) paraMap.get("date"));
        int dateTerm = DateUtils.getProgressDays(frd, tod);
        dateTerm++;
        mivo.setMaxMakeQty((float) (maxMakeQty * dateTerm));
        log.info(tag + "after dateTerm = " + dateTerm);

        try {
            mivo.setStatCd(Long.parseLong(paraMap.get("statCd").toString()));
        } catch (NullPointerException ne) {
            Map<String, Object> scm = new HashMap<String, Object>();
            scm.put("date", mivo.getMakeFrDt());
            scm.put("date", mivo.getMakeToDt());
            mivo.setStatCd(mapper.getMakeIndcStatus(scm)); //???????????? ???????????? ???????????? ????????????(??????,??????,?????? ??????)
        }
        try {
            mivo.setStatCd(Long.parseLong(env.getProperty("code.base.makeEnd")));
        } catch (NullPointerException ne) {
            mivo.setStatCd(2404L);
        }

         Long procCd = Long.parseLong(paraMap.get("procCd").toString());
         Long brnchNo = Long.parseLong(paraMap.get("brnchNo").toString());
        int needValDt = 0;
        int nextvalDt = 0;
        Long Date;
        Date frdt;


        ProcBrnch brnvo = procBrnchRepo.findByCustNoAndBrnchNoAndProcCdAndUsedYn(custNo,brnchNo, procCd, "Y");
        if (brnvo != null) {
            needValDt = brnvo.getNeedDtVal();
            nextvalDt = brnvo.getNextStepVal();
        }

        if (procCd == Long.parseLong(env.getProperty("proc.code.fr"))) {
            frd = sdf.format((Date) paraMap.get("date"));

            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(frd));
            cal.add(Calendar.DATE, needValDt);
            toDt = cal.getTime();

            mivo.setMakeFrDt(sdf.parse(frd));
            mivo.setMakeToDt(toDt);

        }else{
            Calendar nextCal = Calendar.getInstance();
            nextCal.setTime(toDt);
            nextCal.add(Calendar.DATE, nextvalDt);
            frDt = nextCal.getTime();

            Calendar toCal = Calendar.getInstance();
            toCal.setTime(frDt);
            toCal.add(Calendar.DATE, needValDt);
            toDt = toCal.getTime();

            mivo.setMakeFrDt(frDt);
            mivo.setMakeToDt(toDt);
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
            mivo.setIndcDt((Date) paraMap.get("date"));
        } catch (NullPointerException ne) {
            mivo.setIndcDt(DateUtils.getCurrentDateTime());
        }
        try {
            mivo.setIndcCont(paraMap.get("indcCont").toString());
        } catch (NullPointerException ne) {
        }


        MakeIndc chkvo = makeIndcRepo.findByCustNoAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getIndcNo(), mivo.getProcCd(), "Y");
        if (chkvo != null) {
            mivo.setIndcNo(chkvo.getIndcNo());
        } else {
            mivo.setIndcNo(0L);
        }
        /* Remarked By KMJ At 21.10.24
        if (chkvo == null) {
            log.info(tag + "???????????????????????? ????????? ????????? ??????===> ");
            Map<String, Object> bmap = new HashMap<String, Object>();
            bmap.put("codeNo", mivo.getIndcNo());
            bmap.put("savePath", "make/");
            stockService.makeBarCode(bmap);
        }
        */

        Long prodNo = 0L;
        String cmpy = "";
        if (Long.parseLong(paraMap.get("procCd").toString()) == Long.parseLong(env.getProperty("proc.code.fr"))) {

            OrdInfo ordvo = new OrdInfo();

            prodNo = Long.parseLong(paraMap.get("prodNo").toString());
            ProdInfo chk = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo, "Y");
            if (chk != null) {
                ordvo.setOrdNm(chk.getProdNm());
            }
            ordvo.setOrdDt((Date) paraMap.get("date"));
            ordvo.setDlvReqDt((Date) paraMap.get("date"));
            ordvo.setUsedYn("Y");
            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.complete")));
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord.oem")));
            ordvo.setPlcNo(0L);

            cmpy = paraMap.get("cmpy").toString();
            Long mngrgbnSale = Long.parseLong(env.getProperty("code.mngrgbn.sale"));
            cmpy = cmpy.replaceAll("\\p{Z}", "");
            CmpyInfo vo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnSale,cmpy, "Y");
            if (vo != null) {
                ordvo.setCmpyNo(vo.getCmpyNo());
            }
            ordvo.setCustNo(custNo);
            ordRepo.save(ordvo);
            mivo.setOrdNo(ordvo.getOrdNo());

            OrdProd orvo = new OrdProd();
            orvo.setOrdNo(ordvo.getOrdNo());
            orvo.setProdNo(prodNo);
            orvo.setSaleUnit(Long.parseLong(env.getProperty("code.base.sale_unit_g")));
            orvo.setRegIp("127.0.0.1");
            orvo.setRegId(0L);
            orvo.setRegDt(DateUtils.getCurrentDate());
            orvo.setModIp("127.0.0.1");
            orvo.setModId(0L);
            orvo.setModDt(DateUtils.getCurrentDate());
            orvo.setUsedYn("Y");
            orvo.setQtyPerPkg(0);
            orvo.setOrdQty(Float.parseFloat(paraMap.get("indcQty").toString())); // EA
            orvo.setCustNo(custNo);
            ordprod.save(orvo);
        }
        mivo.setMaxMakeWgt(0F);
        mivo.setCustNo(custNo);
        mivo = makeIndcRepo.save(mivo);



        return mivo.getIndcNo();

    }

    //?????? ????????? ????????????

    @Override
    public void DaedongsaveMakeIndcMatr(Map<String, Object> paraMap) throws Exception {
        String tag = "makeIndcService.DaedongsaveMakeIndcMatr ==> ";
        MakeIndcMatr mimvo = new MakeIndcMatr();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        mimvo.setMatrNo(Long.parseLong(paraMap.get("matrNo").toString()));
        mimvo.setIndcNo(Long.parseLong(paraMap.get("indcNo").toString()));
        mimvo.setNeedQty(Float.parseFloat(paraMap.get("needQty").toString()));
        MakeIndcMatr chkvo = makeIndcMatrRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,mimvo.getIndcNo(), mimvo.getMatrNo(), "Y");

        if (chkvo != null) {
            mimvo.setIndcMatrNo(chkvo.getIndcMatrNo());
        }
        try {
            mimvo.setUsedYn("Y");
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegDt(DateUtils.getCurrentDateTime());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setRegIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModDt(mimvo.getModDt());
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
        } catch (NullPointerException e) {

        }
        try {
            mimvo.setModIp(paraMap.get("ipaddr").toString());
        } catch (NullPointerException e) {

        }
        mimvo.setCustNo(custNo);
        makeIndcMatrRepo.save(mimvo);
    }




}


