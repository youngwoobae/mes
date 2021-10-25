package daedan.mes.imp.service.daedan;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;

import daedan.mes.imp.domain.yyjg.DaedanFormat;
import daedan.mes.imp.domain.yyjg.MakeFormat;
import daedan.mes.io.domain.MatrIwh;
import daedan.mes.io.domain.MatrOwh;
import daedan.mes.io.domain.ProdIwh;
import daedan.mes.io.domain.ProdOwh;
import daedan.mes.io.repository.MatrIwhRepository;
import daedan.mes.io.repository.MatrOwhRepository;
import daedan.mes.io.repository.ProdIwhRepository;
import daedan.mes.io.repository.ProdOwhRepository;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.stock.domain.MatrStk;
import daedan.mes.stock.domain.MatrStkClos;
import daedan.mes.stock.domain.ProdStk;
import daedan.mes.stock.domain.ProdStkClos;
import daedan.mes.stock.repository.MatrStkClosRepository;
import daedan.mes.stock.repository.MatrStkRepository;
import daedan.mes.stock.repository.ProdStkClosRepository;
import daedan.mes.stock.repository.ProdStkRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("daedanImpService")
public class DaedanImpServiceImpl implements  DaedanImpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private MatrIwhRepository matrIwhRepo;

    @Autowired
    private MatrOwhRepository matrOwhRepo;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private ProdIwhRepository prodIwhRepo;

    @Autowired
    private ProdOwhRepository prodOwhRepo;

    @Autowired
    private ProdStkClosRepository prodStkClosRepo;

    @Autowired
    private MatrStkClosRepository matrStkClosRepo;

    @Autowired
    private ProdStkRepository prodStkRepo;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private MatrStkRepository matrStkRepo;

    @Autowired
    private MatrRepository matrRepo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;


    @Transactional
    @Override
    public ArrayList<Map<String, Object>> makeStkByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "yyjgImpService.makeStkByExcel => ";
        ArrayList<Map<String, Object>> dsr = new ArrayList<Map<String, Object>>();
        HttpSession session = (HttpSession) paraMap.get("session");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> rmap = null;
        StringBuffer buf = new StringBuffer();

//        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long userId = 0L;
        String fileRoot = env.getProperty("file.root.path");
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();

        FileInfo filevo = new FileInfo();
//        filevo.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
//        filevo = fileRepo.findByFileNoAndUsedYn(filevo.getFileNo(),"Y");
//        String fileRoot = env.getProperty("file.root.path");
//        buf.setLength(0);
//        buf.append(fileRoot).append(File.separator).append(filevo.getAccUrl()).append(filevo.getSaveFileNm());
//        String absFilePath = buf.toString();

        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int rowindex = 0;
        for (int i = 0; i < 13; ++i) {
            XSSFSheet sheet = workbook.getSheetAt(i); //첫번째쉬트
            Float iwhQty = 0f;
            Float owhQty = 0f;
            Float stkQty = 0f;
            Float jbQty = 0f;


            ProdInfo prodvo = null;
            String cmpyNm = "";
            Date closDt = null;
//            try {
//                closDt = sdf.parse(paraMap.get("closDt").toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

//            dsr = this.chkProdExist(rows, sheet);
//            if (dsr.size() > 0) { //존재하지 않은 상품이 있는 경우 process stop;
//                return dsr;
//            }

            int rows = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.getRow(rowindex);
            for (rowindex = 0; rowindex < 47; rowindex++) {

                if (rowindex < 7) continue; //헤더정보 skip
                row = sheet.getRow(rowindex);
                if (row == null) continue;


                String prodNm = row.getCell(DaedanFormat.PROD_NM).getStringCellValue();
//                log.info(tag + "행번호(" + rowindex + ")제품명 = " + prodNm);
//                int rate = (rowindex * 100 / rows);
//                session.setAttribute("rate", rate);
//                log.info("rate 는 " + rate + "session은 " + session.getAttribute("rate"));

                if (prodNm.replaceAll("\\p{Z}", "").contains("부품수불일지") == true) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    break;
                }
                if (prodNm.replaceAll("\\p{Z}", "") == null) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    break;
                }
                if (prodNm.replaceAll("\\p{Z}", "").length() <= 0) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    break;
                }
                if (prodNm.replaceAll("\\p{Z}", "").equals("")) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    break;
                }
                prodvo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");

                try {
                    iwhQty = (float) row.getCell(DaedanFormat.JB_IWH).getNumericCellValue(); //입고(생산)
                } catch (NullPointerException ne) {
                    iwhQty = 0f;
                }

                try {
                    owhQty = (float) row.getCell(DaedanFormat.JB_OWH).getNumericCellValue(); //출고(판매)
                } catch (NullPointerException ne) {
                    owhQty = 0f;
                }
                try {
                    stkQty = (float) row.getCell(DaedanFormat.CU_STK).getNumericCellValue(); //현재고
                } catch (NullPointerException ne) {
                    stkQty = 0f;
                }

                row = sheet.getRow(3);
                closDt = row.getCell(2).getDateCellValue();

                CmpyInfo cmvo = new CmpyInfo();
                cmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                cmvo.setModId(userId);
                cmvo.setModIp("localhost");

                //판매거래처 생성
//                try {
//                    cmpyNm = row.getCell(MakeFormat.CMPY_NM).getStringCellValue();
//                    this.makeCmpy(rowindex, cmpyNm, cmvo.getModId(), cmvo.getModIp());
//                } catch (NullPointerException ne) {
//
//                }
                try {
                    jbQty = (float) row.getCell(DaedanFormat.JB_STK).getNumericCellValue(); //전일재고
                } catch (NullPointerException ne) {
                    jbQty = 0f;
                }
                this.makeJbStk(rowindex, custNo,prodvo, closDt, jbQty); //전일재고 생성

                ProdStkClos psvo = this.makeProdStkClos(rowindex, custNo, prodvo, iwhQty, owhQty, stkQty, closDt, userId); //제품마감 테이블에 입출고및 재고 설정
                this.makeFinalProdStk(rowindex, custNo,psvo, stkQty, userId); //제품제고설정

                if (iwhQty > 0) {
                    this.makeProdIwh(rowindex, custNo, prodvo, closDt, iwhQty, userId); //입고이력생성
                }
                if(owhQty > 0 ){
                    this.makeProdOwh(rowindex , custNo, prodvo, closDt, owhQty, userId);
                }
//                this.makeOrdInfo(rowindex, cmpyNm, prodvo, owhQty, psvo.getClosDt(), userId, Long.parseLong(paraMap.get("fileNo").toString())); //주문정보및 출고이력 생성
            }
    }
        return dsr;
}
    private ArrayList<Map<String,Object>> chkProdExist(int rows,Long custNo, XSSFSheet sheet) {
        String tag = "YijgImpService.chkProdExist => ";
        ArrayList<Map<String,Object>> dsr = new ArrayList<Map<String,Object>>();
        Map<String,Object> rmap = null;
        StringBuffer buf = new StringBuffer();
        int rowindex = 0;
        for (rowindex = 0; rowindex < 47; rowindex++) {
            if (rowindex < 7) continue; //헤더정보 skip
            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;

            try{
                String endprodNm = row.getCell(1).getStringCellValue();
                if (endprodNm.replaceAll("\\p{Z}", "").contains("부품수불일지") == true) {
                    break;
                }
            }catch (NullPointerException en){

            }
            String prodNm = row.getCell(3).getStringCellValue();
            if (prodNm.replaceAll("\\p{Z}", "") == null) {
                break;
            }
            if (prodNm.replaceAll("\\p{Z}", "").length() <=  0) {
                break;
            }
            if (prodNm.replaceAll("\\p{Z}", "").equals("") ) {
                break;
            }
            ProdInfo prodvo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (prodvo == null) {
                log.info(tag + prodNm + " 제품정보없음 ");
                rmap = new HashMap<String, Object>();
                rmap.put("lineNo", rowindex);
                rmap.put("prodNm", prodNm);
                rmap.put("errRm", "상품정보가 존재하지 않습니다.");
                dsr.add(rmap);
            }
        }
        return dsr;
    }
    private void makeCmpy(int rowIndex, Long custNo, String cmpyNm, Long userId, String ipaddr) {
        String tag = "ImpService.makeCmpy ==> ";

        Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.sale"));
        CmpyInfo cmvo = new CmpyInfo();
        cmvo.setUsedYn("Y");
        cmvo.setModDt(DateUtils.getCurrentBaseDateTime());
        cmvo.setModId(userId);
        cmvo.setModIp(ipaddr);
        log.info("cmpyNm => " + cmpyNm);
        if (StringUtil.isEmpty(cmpyNm)) {
            log.info(tag + "판매거래처 없음. skip....");
            return;
        }
        try {
            if (cmpyNm.indexOf(",") >= 0) {
                String arCmpy[] = cmpyNm.split(",");
                for (int ix = 0; ix < arCmpy.length; ix++) {
                    int delimiterIx = arCmpy[ix].indexOf("_");
                    String chkCmpyNm = arCmpy[ix].substring(0, delimiterIx);
                    //float saleQty = Float.parseFloat(arCmpy[ix].substring(delimiterIx + 1));
                    CmpyInfo chkCmpyVo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,chkCmpyNm, "Y");
                    if (chkCmpyVo != null) {
                        chkCmpyVo.setModDt(cmvo.getModDt());
                        chkCmpyVo.setModId(cmvo.getModId());
                        chkCmpyVo.setModIp(cmvo.getModIp());
                        cmpyRepo.save(chkCmpyVo);
                    } else {
                        cmvo.setCmpyNo(0L);
                        cmvo.setCmpyNm(chkCmpyNm);
                        cmvo.setCmpyTp(Long.valueOf(env.getProperty("code.cmpytp.sale"))); //매출
                        cmvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgb.cmpy"))); //법인
                        cmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                        cmvo.setRegId(userId);
                        cmvo.setRegIp(ipaddr);
                        cmvo.setCustNo(custNo);
                        cmpyRepo.save(cmvo);
                    }
                } //판매거래처 roop 끝.
            }
            else { //거래처 1개 이하
                log.info(tag + "행번호 ( " + rowIndex + " ) 거래처명 = " + cmpyNm + "  판매거래처 1개 존재");
                CmpyInfo chkCmpyVo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,cmpyNm, "Y");
                if (chkCmpyVo == null) {
                    log.info(tag + "행번호 ( " + rowIndex + " ) 생성거래처  = " + cmpyNm);
                    cmvo.setCmpyNo(0L);
                    cmvo.setCmpyNm(cmpyNm);
                    cmvo.setCmpyTp(Long.valueOf(env.getProperty("code.cmpytp.sale"))); //매출
                    cmvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgb.cmpy"))); //법인
                    cmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    cmvo.setRegId(userId);
                    cmvo.setRegIp(ipaddr);
                    cmvo.setCustNo(custNo);
                    cmpyRepo.save(cmvo);
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            log.info(tag + "행번호 ( " + rowIndex + " ) 거래처명오류 = " + cmpyNm );
        }
    }

    private void makeJbStk(int rowIndex , Long custNo,ProdInfo prodvo, Date closDt ,Float jbQty) { //전일재고 생성
        String tag = "ImpService.makeJbStk(전일재고생성) =>";
        log.info(tag + "시작");
        ProdStkClos psvo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        psvo = new ProdStkClos();
        psvo.setProdNo(prodvo.getProdNo());
        psvo.setClosDt(DateUtils.addDate(closDt, -1));

        psvo.setStkQty(jbQty); //전일재고
        psvo.setWhNo(1024L); //제품창고(기본)
        psvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos")));
        psvo.setModDt(DateUtils.getCurrentBaseDateTime());
        psvo.setModId(0L);
        psvo.setModIp("localhost");
        psvo.setUsedYn("Y");
        ProdStkClos chkpsvo = prodStkClosRepo.findByCustNoAndClosDtAndWhNoAndProdNoAndUsedYn(custNo,psvo.getClosDt(),psvo.getWhNo(), psvo.getProdNo(), "Y");
        if (chkpsvo != null) {
            chkpsvo.setStkQty(psvo.getStkQty());
            chkpsvo.setModDt(psvo.getModDt());
            chkpsvo.setModId(psvo.getModId());
            chkpsvo.setModIp(psvo.getModIp());
            prodStkClosRepo.save(chkpsvo);
        } else {
            psvo.setProdStkClosNo(0L);
            psvo.setIwhQty(0f);
            psvo.setOwhQty(0f);
            psvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            psvo.setRegId(0L);
            psvo.setRegIp("localhost");
            prodStkClosRepo.save(psvo);
            log.info(tag + "성공");
        }
    }

    private void makeProdIwh(int rowIndex , Long custNo, ProdInfo prodvo, Date closDt ,Float iwhQty,Long userId) { //입고이력생성
        String tag = "ImpService.makeProdIwh(입고이력생성) =>";
        log.info(tag + "시작");
        ProdIwh pivo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        pivo = new ProdIwh();
        pivo.setProdNo(prodvo.getProdNo());
        pivo.setIwhDt(closDt);
        pivo.setIwhQty(iwhQty); //전일재고
        pivo.setIndcRsltNo(0L);
        pivo.setUsedYn("Y");
        pivo.setModDt(DateUtils.getCurrentBaseDateTime());
        pivo.setModId(0L);
        pivo.setModIp("localhost");
        pivo.setUsedYn("Y");
        ProdIwh chkvo = prodIwhRepo.findByCustNoAndIwhDtAndProdNoAndUsedYn(custNo, pivo.getIwhDt(), pivo.getProdNo(), "Y");
        if (chkvo != null) {
            chkvo.setIwhQty(pivo.getIwhQty());
            chkvo.setModDt(pivo.getModDt());
            chkvo.setModId(pivo.getModId());
            chkvo.setModIp(pivo.getModIp());
            prodIwhRepo.save(chkvo);
        } else {
            pivo.setIwhNo(0L);
            pivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            pivo.setRegId(userId);
            pivo.setRegIp("localhost");
            pivo.setWhNo(566L);//완제품보관창고(기본)
            prodIwhRepo.save(pivo);
            log.info(tag + "성공");
        }
    }
    private void makeProdOwh(int rowIndex , Long custNo, ProdInfo prodvo, Date closDt , Float owhQty, Long userId) { //입고이력생성
        String tag = "ImpService.makeProdOwh(출고이력생성) =>";
        log.info(tag + "시작.rowIndex = " + rowIndex);

        ProdOwh povo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        povo = new ProdOwh();
        povo.setProdNo(prodvo.getProdNo());
        povo.setCmpyNo(0L);
        povo.setOwhDt(closDt);
        povo.setOwhReqQty(owhQty);
        povo.setOwhQty(owhQty); //출고수량
        povo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));
        povo.setOrdNo(0L);
        povo.setWhNo(1024L);//완제품보관창고(기본));
        povo.setUsedYn("Y");
        povo.setModDt(DateUtils.getCurrentBaseDateTime());
        povo.setModId(0L);

        povo.setModIp("localhost");
        povo.setUsedYn("Y");
        ProdOwh chkvo = prodOwhRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,povo.getOrdNo(), povo.getProdNo(), "Y");
        if (chkvo != null) {
            chkvo.setOwhQty(povo.getOwhQty());
            chkvo.setModDt(povo.getModDt());
            chkvo.setModId(povo.getModId());
            chkvo.setModIp(povo.getModIp());
            prodOwhRepo.save(chkvo);
        } else {
            povo.setOwhNo(0L);
            povo.setRegDt(DateUtils.getCurrentBaseDateTime());
            povo.setRegId(userId);
            povo.setRegIp("localhost");
            prodOwhRepo.save(povo);
            log.info(tag + "성공");
        }
    }

    private ProdStkClos makeProdStkClos(int rowIndex, Long custNo, ProdInfo prodvo, Float iwhQty, Float owhQty, Float stkQty, Date closDt, Long userId) {
        String tag = "ImpService.makeProdStkClos(제품 마감테이블(prod_stk_clos)에 금일 제품입고,출고,재고 설정) =>";
        log.info(tag + "시작.rowIndex = " + rowIndex);
        ProdStkClos psvo = new ProdStkClos();
        psvo.setProdNo(prodvo.getProdNo());
        psvo.setClosDt(closDt); //현재일자
        psvo.setIwhQty(iwhQty); //입고수량(생산수량)
        psvo.setOwhQty(owhQty); //출고수량(판매수량)
        psvo.setStkQty(stkQty); //현재고
        psvo.setModDt(DateUtils.getCurrentBaseDateTime());
        psvo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos")));
        psvo.setWhNo(1024L); //완제품창고1 (기본값을 처리중)
        psvo.setModId(userId);
        psvo.setModIp("localhost");
        psvo.setUsedYn("Y");
        ProdStkClos chkpsvo = prodStkClosRepo.findByCustNoAndClosDtAndWhNoAndProdNoAndUsedYn(custNo,psvo.getClosDt(), psvo.getWhNo(), psvo.getProdNo(), "Y");
        if (chkpsvo != null) {
            chkpsvo.setIwhQty(psvo.getIwhQty());
            chkpsvo.setOwhQty(psvo.getOwhQty());
            chkpsvo.setStkQty(psvo.getStkQty());
            chkpsvo.setModDt(psvo.getModDt());
            chkpsvo.setModId(psvo.getModId());
            chkpsvo.setModIp(psvo.getModIp());
            psvo = prodStkClosRepo.save(chkpsvo);
        } else {
            psvo.setProdStkClosNo(0L);
            psvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            psvo.setRegId(0L);
            psvo.setRegIp("localhost");
            psvo = prodStkClosRepo.save(psvo);
        }
        log.info(tag + "성공");
        return psvo;
    }

    private void makeFinalProdStk(int rowIndex, Long custNo, ProdStkClos psvo, Float stkQty, Long userId) {
        String tag = "ImpService.makeFinalProdStk(제품제고설정) => ";
        log.info(tag + "시작.rowIndex = " + rowIndex + "prodNo =" + psvo.getProdNo());
        ProdStk stkvo = new ProdStk();
        stkvo.setProdNo(psvo.getProdNo());
        stkvo.setStkDt(psvo.getClosDt());
        stkvo.setModDt(psvo.getModDt());
        stkvo.setModId(psvo.getModId());
        stkvo.setModIp(psvo.getModIp());
        stkvo.setWhNo(1024L); //완제품창고1 (기본값을 처리중)
        stkvo.setStatTrfDt(psvo.getClosDt());
        stkvo.setStkQty(stkQty);
        stkvo.setUsedYn("Y");
        ProdStk chkstkvo = prodStkRepo.findByCustNoAndWhNoAndProdNoAndUsedYn(custNo,stkvo.getWhNo(),psvo.getProdNo(), "Y");
        if (chkstkvo != null) {
            chkstkvo.setModDt(stkvo.getModDt());
            chkstkvo.setModId(stkvo.getModId());
            chkstkvo.setModIp(stkvo.getModIp());
            chkstkvo.setStkQty(stkvo.getStkQty());
            prodStkRepo.save(chkstkvo);
        } else {
            stkvo.setStkNo(0L);
            stkvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            stkvo.setRegId(stkvo.getModId());
            stkvo.setRegIp(stkvo.getModIp());
            prodStkRepo.save(stkvo);
        }
        log.info(tag + "성공");
    }
//    private OrdInfo makeOrdInfo(int rowIndex, String cmpyNm, ProdInfo prodvo, Float owhQty, Date closDt, Long userId, Long fileNo ) {
//        String tag = "ImpService.makeOrdInfo(주문정보설정) => ";
//        log.info(tag + "시작");
//        OrdInfo ordvo = null;
//        try {
//            if (cmpyNm.indexOf(",") >= 0) {
//                String arCmpy[] = cmpyNm.split(",");
//
//                for (int ix = 0; ix < arCmpy.length; ix++) {
//                    int delimiterIx = arCmpy[ix].indexOf("_");
//                    String chkCmpyNm = arCmpy[ix].substring(0, delimiterIx);
//                    float saleQty = Float.parseFloat(arCmpy[ix].substring(delimiterIx + 1));
//                    ordvo = this.saveOrd(rowIndex, chkCmpyNm,prodvo.getProdNm(),closDt,saleQty,prodvo, userId, fileNo);
//                }
//            }
//            else {
//                ordvo = this.saveOrd(rowIndex,cmpyNm,prodvo.getProdNm(),closDt,owhQty,prodvo, userId, fileNo);
//            }
//        }
//        catch (ArrayIndexOutOfBoundsException ae) { //거래처 1개 이하
//            log.info(tag + "주문정보생성 시점 거래처 스플릿 싪패!!!!");
//            log.info(tag + "행번호 ( " + rowIndex + " ) 주문정보생성 시점 거래처 스플릿 싪패 =  " + cmpyNm );
//        }
//        return ordvo;
//    }

//    private OrdInfo saveOrd(int rowIndex, String chkCmpyNm,String prodNm,Date closDt, Float saleQty, ProdInfo prodvo, Long userId, Long fileNo) {
//        StringBuffer buf = new StringBuffer();
//        OrdInfo ordvo = null;
//        CmpyInfo chkCmpyVo = cmpyRepo.findByCmpyNmAndUsedYn(chkCmpyNm, "Y");
//        if (chkCmpyVo != null) { //주문거래처가 있음
//            ordvo = new OrdInfo();
//            buf.setLength(0);
//            buf.append(chkCmpyVo.getCmpyNo()).append("[ ").append(prodNm).append(" ] 주문");
//            ordvo.setOrdNm(buf.toString());
//            ordvo.setCmpyNo(chkCmpyVo.getCmpyNo());
//            ordvo.setOrdPath(0L);
//            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.complete"))); //고객인도
//            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord_oem_cd"))); //OEM
//            ordvo.setPlcNo(0L);
//            ordvo.setOrdDt(DateUtils.getCurrentBaseDateTime());
//            ordvo.setDlvReqDt(DateUtils.getCurrentBaseDateTime());
//            ordvo.setDlvDt(DateUtils.getCurrentBaseDateTime());
//            ordvo.setUsedYn("Y");
//            ordvo.setFileNo(fileNo);
//            ordvo.setModIp("localhost");
//            ordvo.setModId(userId);
//            ordvo.setModDt(DateUtils.getCurrentBaseDateTime());
//
//            OrdInfo chkOrdVo = ordRepo.findByCmpyNoAndOrdDtAndUsedYn(ordvo.getCmpyNo(), closDt, "Y");
//            if (chkOrdVo == null) {
//                ordvo.setOrdNo(0L);
//                ordvo.setRegDt(DateUtils.getCurrentBaseDateTime());
//                ordvo.setRegId(ordvo.getModId());
//                ordvo.setRegIp(ordvo.getModIp());
//                ordvo = ordRepo.save(ordvo);
//
//                //주문제품생성
//                OrdProd chkOrdProdVo = ordProdRepo.findByOrdNoAndProdNoAndUsedYn(ordvo.getOrdNo(), prodvo.getProdNo(), "Y");
//                ProdInfo chkProdVo = prodRepo.findByProdNoAndUsedYn(prodvo.getProdNo(), "Y");
//                if (chkProdVo != null) {
//                    OrdProd opvo = new OrdProd();
//                    opvo.setDlvQty(saleQty);
//                    opvo.setDlvDt(DateUtils.getCurrentBaseDateTime());
//                    opvo.setOrdQty(saleQty);
//                    opvo.setOrdNo(ordvo.getOrdNo());
//                    opvo.setProdNo(prodvo.getProdNo());
//                    opvo.setQtyPerPkg(chkProdVo.getQtyPerPkg());
//                    opvo.setSaleUnit(chkProdVo.getSaleUnit());
//                    opvo.setUsedYn("Y");
//                    opvo.setModIp("localhost");
//                    opvo.setModId(userId);
//                    opvo.setModDt(DateUtils.getCurrentBaseDateTime());
//
//                    if (chkOrdProdVo == null) {
//                        opvo.setOrdProdNo(0L);
//                        opvo.setRegIp(opvo.getModIp());
//                        opvo.setRegId(opvo.getModId());
//                        opvo.setRegDt(opvo.getModDt());
//                        ordProdRepo.save(opvo);
//                    } else {
//                        chkOrdProdVo.setOrdQty(opvo.getOrdQty());
//                        ordProdRepo.save(chkOrdProdVo);
//                    }
//                    //출고이력생성
////                    if (opvo.getOrdQty() > 0 && opvo.getOrdNo() != null) {
////                        this.makeProdOwh(rowIndex, prodvo, closDt, ordvo, opvo.getOrdQty(), userId); //출고이력생성
////                    }
//                }
//            }
//        }
//        return ordvo;
//    }


    @Transactional
    @Override
    public ArrayList<Map<String, Object>> makeMatrStkByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "yyjgImpService.makeStkByExcel => ";

        ArrayList<Map<String, Object>> dsr = new ArrayList<Map<String, Object>>();
        HttpSession session = (HttpSession) paraMap.get("session");
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> rmap = null;
        StringBuffer buf = new StringBuffer();

//        Long userId = Long.parseLong(paraMap.get("userId").toString());
        Long userId = 0L;
        String fileRoot = env.getProperty("file.root.path");
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();

        FileInfo filevo = new FileInfo();
//        filevo.setFileNo(Long.parseLong(paraMap.get("fileNo").toString()));
//        filevo = fileRepo.findByFileNoAndUsedYn(filevo.getFileNo(),"Y");
//        String fileRoot = env.getProperty("file.root.path");
//        buf.setLength(0);
//        buf.append(fileRoot).append(File.separator).append(filevo.getAccUrl()).append(filevo.getSaveFileNm());
//        String absFilePath = buf.toString();

        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int rowindex = 0;
        for (int i = 0; i < 13; ++i) {
            XSSFSheet sheet = workbook.getSheetAt(i); //첫번째쉬트
            Float iwhQty = 0f;
            Float owhQty = 0f;
            Float stkQty = 0f;
            Float jbQty = 0f;


            MatrInfo Matrvo = null;
            String cmpyNm = "";
            Date closDt = null;

//            try {
//                closDt = sdf.parse(paraMap.get("closDt").toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

//            dsr = this.chkProdExist(rows, sheet);
//            if (dsr.size() > 0) { //존재하지 않은 상품이 있는 경우 process stop;
//                return dsr;
//            }

            int rows = sheet.getPhysicalNumberOfRows();
            XSSFRow row = sheet.getRow(rowindex);
            for (rowindex = 0; rowindex < 372; rowindex++) {

                if (rowindex < 54) continue; //헤더정보 skip
                row = sheet.getRow(rowindex);
                if (row == null) continue;


                String MatrNm = row.getCell(DaedanFormat.Matr_NM).getStringCellValue();
//                log.info(tag + "행번호(" + rowindex + ")제품명 = " + prodNm);
//                int rate = (rowindex * 100 / rows);
//                session.setAttribute("rate", rate);
//                log.info("rate 는 " + rate + "session은 " + session.getAttribute("rate"));

                String endMatrNm = row.getCell(1).getStringCellValue();
                if (endMatrNm.replaceAll("\\p{Z}", "").contains("부품수불일지(직접입력)") == true) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    break;
                }
                if (MatrNm.replaceAll("\\p{Z}", "") == null) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    continue;
                }
                if (MatrNm.replaceAll("\\p{Z}", "").length() <= 0) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    continue;
                }
                if (MatrNm.replaceAll("\\p{Z}", "").equals("")) {
//                    rate = 100;
//                    session.setAttribute("rate", rate);
                    continue;
                }
                Matrvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,MatrNm,"Y");

                try {
                    iwhQty = (float) row.getCell(DaedanFormat.JB_IWH).getNumericCellValue(); //입고(생산)
                } catch (NullPointerException ne) {
                    iwhQty = 0f;
                }

                try {
                    owhQty = (float) row.getCell(DaedanFormat.JB_OWH).getNumericCellValue(); //출고(판매)
                } catch (NullPointerException ne) {
                    owhQty = 0f;
                }
                try {
                    stkQty = (float) row.getCell(DaedanFormat.CU_STK).getNumericCellValue(); //현재고
                } catch (NullPointerException ne) {
                    stkQty = 0f;
                }

                row = sheet.getRow(3);
                closDt = row.getCell(2).getDateCellValue();

                CmpyInfo cmvo = new CmpyInfo();
                cmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                cmvo.setModId(userId);
                cmvo.setModIp("localhost");

                //판매거래처 생성
//                try {
//                    cmpyNm = row.getCell(MakeFormat.CMPY_NM).getStringCellValue();
//                    this.makeCmpy(rowindex, cmpyNm, cmvo.getModId(), cmvo.getModIp());
//                } catch (NullPointerException ne) {
//
//                }
                try {
                    jbQty = (float) row.getCell(DaedanFormat.JB_STK).getNumericCellValue(); //전일재고
                } catch (NullPointerException ne) {
                    jbQty = 0f;
                }
                this.makeMatrJbStk(rowindex, custNo,Matrvo, closDt, jbQty); //전일재고 생성

                MatrStkClos mavo = this.makeMatrStkClos(rowindex, custNo,Matrvo, iwhQty, owhQty, stkQty, closDt, userId); //제품마감 테이블에 입출고및 재고 설정
                this.makeFinalMatrStk(rowindex, custNo,mavo, stkQty, userId); //제품제고설정

                if (iwhQty > 0) {
                    this.makeMatrIwh(rowindex, custNo, Matrvo, closDt, iwhQty, userId); //입고이력생성
                }
                if(owhQty > 0 ){
                    this.makeMatrOwh(rowindex , custNo,Matrvo, closDt, owhQty, userId);
                }
//                this.makeOrdInfo(rowindex, cmpyNm, prodvo, owhQty, psvo.getClosDt(), userId, Long.parseLong(paraMap.get("fileNo").toString())); //주문정보및 출고이력 생성
            }
        }
        return dsr;
    }
//    private ArrayList<Map<String,Object>> chkProdExist(int rows,XSSFSheet sheet) {
//        String tag = "YijgImpService.chkProdExist => ";
//        ArrayList<Map<String,Object>> dsr = new ArrayList<Map<String,Object>>();
//        Map<String,Object> rmap = null;
//        StringBuffer buf = new StringBuffer();
//        int rowindex = 0;
//        for (rowindex = 0; rowindex < 47; rowindex++) {
//            if (rowindex < 7) continue; //헤더정보 skip
//            XSSFRow row = sheet.getRow(rowindex);
//            if (row == null) continue;
//
//            try{
//                String endprodNm = row.getCell(1).getStringCellValue();
//                if (endprodNm.replaceAll("\\p{Z}", "").contains("부품수불일지") == true) {
//                    break;
//                }
//            }catch (NullPointerException en){
//
//            }
//            String prodNm = row.getCell(3).getStringCellValue();
//            if (prodNm.replaceAll("\\p{Z}", "") == null) {
//                break;
//            }
//            if (prodNm.replaceAll("\\p{Z}", "").length() <=  0) {
//                break;
//            }
//            if (prodNm.replaceAll("\\p{Z}", "").equals("") ) {
//                break;
//            }
//            ProdInfo prodvo = prodRepo.findByProdNmAndUsedYn(prodNm, "Y");
//            if (prodvo == null) {
//                log.info(tag + prodNm + " 제품정보없음 ");
//                rmap = new HashMap<String, Object>();
//                rmap.put("lineNo", rowindex);
//                rmap.put("prodNm", prodNm);
//                rmap.put("errRm", "상품정보가 존재하지 않습니다.");
//                dsr.add(rmap);
//            }
//        }
//        return dsr;
//    }
//    private void makeCmpy(int rowIndex, String cmpyNm, Long userId, String ipaddr) {
//        String tag = "ImpService.makeCmpy ==> ";
//        CmpyInfo cmvo = new CmpyInfo();
//        cmvo.setUsedYn("Y");
//        cmvo.setModDt(DateUtils.getCurrentBaseDateTime());
//        cmvo.setModId(userId);
//        cmvo.setModIp(ipaddr);
//        log.info("cmpyNm => " + cmpyNm);
//        if (StringUtil.isEmpty(cmpyNm)) {
//            log.info(tag + "판매거래처 없음. skip....");
//            return;
//        }
//        try {
//            if (cmpyNm.indexOf(",") >= 0) {
//                String arCmpy[] = cmpyNm.split(",");
//                for (int ix = 0; ix < arCmpy.length; ix++) {
//                    int delimiterIx = arCmpy[ix].indexOf("_");
//                    String chkCmpyNm = arCmpy[ix].substring(0, delimiterIx);
//                    //float saleQty = Float.parseFloat(arCmpy[ix].substring(delimiterIx + 1));
//                    CmpyInfo chkCmpyVo = cmpyRepo.findByCmpyNmAndUsedYn(chkCmpyNm, "Y");
//                    if (chkCmpyVo != null) {
//                        chkCmpyVo.setModDt(cmvo.getModDt());
//                        chkCmpyVo.setModId(cmvo.getModId());
//                        chkCmpyVo.setModIp(cmvo.getModIp());
//                        cmpyRepo.save(chkCmpyVo);
//                    } else {
//                        cmvo.setCmpyNo(0L);
//                        cmvo.setCmpyNm(chkCmpyNm);
//                        cmvo.setCmpyTp(Long.valueOf(env.getProperty("code.cmpytp.sale"))); //매출
//                        cmvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgb.cmpy"))); //법인
//                        cmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
//                        cmvo.setRegId(userId);
//                        cmvo.setRegIp(ipaddr);
//                        cmpyRepo.save(cmvo);
//                    }
//                } //판매거래처 roop 끝.
//            }
//            else { //거래처 1개 이하
//                log.info(tag + "행번호 ( " + rowIndex + " ) 거래처명 = " + cmpyNm + "  판매거래처 1개 존재");
//                CmpyInfo chkCmpyVo = cmpyRepo.findByCmpyNmAndUsedYn(cmpyNm, "Y");
//                if (chkCmpyVo == null) {
//                    log.info(tag + "행번호 ( " + rowIndex + " ) 생성거래처  = " + cmpyNm);
//                    cmvo.setCmpyNo(0L);
//                    cmvo.setCmpyNm(cmpyNm);
//                    cmvo.setCmpyTp(Long.valueOf(env.getProperty("code.cmpytp.sale"))); //매출
//                    cmvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgb.cmpy"))); //법인
//                    cmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
//                    cmvo.setRegId(userId);
//                    cmvo.setRegIp(ipaddr);
//                    cmpyRepo.save(cmvo);
//                }
//            }
//        }
//        catch (ArrayIndexOutOfBoundsException ae) {
//            log.info(tag + "행번호 ( " + rowIndex + " ) 거래처명오류 = " + cmpyNm );
//        }
//    }

    private void makeMatrJbStk(int rowIndex , Long custNo,MatrInfo Matrvo, Date closDt ,Float jbQty) { //전일재고 생성
        String tag = "ImpService.makeJbStk(전일재고생성) =>";
        log.info(tag + "시작");
        MatrStkClos mavo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        mavo = new MatrStkClos();
        mavo.setMatrNo(Matrvo.getMatrNo());
        mavo.setClosDt(DateUtils.addDate(closDt, -1));

        mavo.setStkQty(jbQty); //전일재고
        mavo.setWhNo(1023L); //제품창고(기본)
        mavo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos")));
        mavo.setModDt(DateUtils.getCurrentBaseDateTime());
        mavo.setModId(0L);
        mavo.setModIp("localhost");
        mavo.setUsedYn("Y");
        MatrStkClos chkpsvo = matrStkClosRepo.findByCustNoAndClosDtAndWhNoAndMatrNoAndUsedYn(custNo,mavo.getClosDt(),mavo.getWhNo(), mavo.getMatrNo(), "Y");
        if (chkpsvo != null) {
            chkpsvo.setStkQty(mavo.getStkQty());
            chkpsvo.setModDt(mavo.getModDt());
            chkpsvo.setModId(mavo.getModId());
            chkpsvo.setModIp(mavo.getModIp());
            matrStkClosRepo.save(chkpsvo);
        } else {
            mavo.setMatrStkClosNo(0L);
            mavo.setIwhQty(0f);
            mavo.setOwhQty(0f);
            mavo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mavo.setRegId(0L);
            mavo.setRegIp("localhost");
            matrStkClosRepo.save(mavo);
            log.info(tag + "성공");
        }
    }

    private void makeMatrIwh(int rowIndex ,Long custNo, MatrInfo Matrvo, Date closDt ,Float iwhQty,Long userId) { //입고이력생성
        String tag = "ImpService.makeProdIwh(입고이력생성) =>";
        log.info(tag + "시작");
        MatrIwh mivo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        mivo = new MatrIwh();
        mivo.setMatrNo(Matrvo.getMatrNo());
        mivo.setIwhDt(closDt);
        mivo.setIwhQty(iwhQty); //전일재고
        mivo.setUsedYn("Y");
        mivo.setModDt(DateUtils.getCurrentBaseDateTime());
        mivo.setModId(0L);
        mivo.setModIp("localhost");
        mivo.setUsedYn("Y");
        MatrIwh chkvo = matrIwhRepo.findByCustNoAndIwhDtAndMatrNoAndUsedYn(custNo,mivo.getIwhDt(), mivo.getMatrNo(), "Y");
        if (chkvo != null) {
            chkvo.setIwhQty(mivo.getIwhQty());
            chkvo.setModDt(mivo.getModDt());
            chkvo.setModId(mivo.getModId());
            chkvo.setModIp(mivo.getModIp());
            matrIwhRepo.save(chkvo);
        } else {
            mivo.setIwhNo(0L);
            mivo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mivo.setRegId(userId);
            mivo.setRegIp("localhost");
            mivo.setWhNo(1023L);//완제품보관창고(기본)
            matrIwhRepo.save(mivo);
            log.info(tag + "성공");
        }
    }
    private void makeMatrOwh(int rowIndex , Long custNo , MatrInfo Matrvo, Date closDt , Float owhQty, Long userId) { //입고이력생성
        String tag = "ImpService.makeProdOwh(출고이력생성) =>";
        log.info(tag + "시작.rowIndex = " + rowIndex);
        MatrOwh movo = null;
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");

        movo = new MatrOwh();
        movo.setMatrNo(Matrvo.getMatrNo());
        movo.setIndcNo(0L);
        movo.setOwhDt(closDt);
        movo.setOwhReqDt(closDt);
        movo.setOwhReqQty(owhQty);
        movo.setOwhQty(owhQty); //출고수량
        movo.setOwhUnit(Long.parseLong(env.getProperty("code.base.sale_unit_ea")));
        movo.setWhNo(1024L);//완제품보관창고(기본));
        movo.setUsedYn("Y");
        movo.setModDt(DateUtils.getCurrentBaseDateTime());
        movo.setModId(0L);

        movo.setModIp("localhost");
        movo.setUsedYn("Y");
        MatrOwh chkvo = matrOwhRepo.findByCustNoAndIndcNoAndMatrNoAndUsedYn(custNo,movo.getIndcNo(), movo.getMatrNo(), "Y");
        if (chkvo != null) {
            chkvo.setOwhQty(movo.getOwhQty());
            chkvo.setModDt(movo.getModDt());
            chkvo.setModId(movo.getModId());
            chkvo.setModIp(movo.getModIp());
            matrOwhRepo.save(chkvo);
        } else {
            movo.setOwhNo(0L);
            movo.setRegDt(DateUtils.getCurrentBaseDateTime());
            movo.setRegId(userId);
            movo.setRegIp("localhost");
            matrOwhRepo.save(movo);
            log.info(tag + "성공");
        }
    }

    private MatrStkClos makeMatrStkClos(int rowIndex, Long custNo, MatrInfo Matrvo, Float iwhQty, Float owhQty, Float stkQty, Date closDt, Long userId) {
        String tag = "ImpService.makeProdStkClos(제품 마감테이블(prod_stk_clos)에 금일 제품입고,출고,재고 설정) =>";
        log.info(tag + "시작.rowIndex = " + rowIndex);
        MatrStkClos mavo = new MatrStkClos();
        mavo.setMatrNo(Matrvo.getMatrNo());
        mavo.setClosDt(closDt); //현재일자
        mavo.setIwhQty(iwhQty); //입고수량(생산수량)
        mavo.setOwhQty(owhQty); //출고수량(판매수량)
        mavo.setStkQty(stkQty); //현재고
        mavo.setModDt(DateUtils.getCurrentBaseDateTime());
        mavo.setStatCd(Long.parseLong(env.getProperty("stk_stat_clos")));
        mavo.setWhNo(1023L); //완제품창고1 (기본값을 처리중)
        mavo.setModId(userId);
        mavo.setModIp("localhost");
        mavo.setUsedYn("Y");
        MatrStkClos chkpsvo = matrStkClosRepo.findByCustNoAndClosDtAndWhNoAndMatrNoAndUsedYn(custNo,mavo.getClosDt(), mavo.getWhNo(), mavo.getMatrNo(), "Y");
        if (chkpsvo != null) {
            chkpsvo.setIwhQty(mavo.getIwhQty());
            chkpsvo.setOwhQty(mavo.getOwhQty());
            chkpsvo.setStkQty(mavo.getStkQty());
            chkpsvo.setModDt(mavo.getModDt());
            chkpsvo.setModId(mavo.getModId());
            chkpsvo.setModIp(mavo.getModIp());
            mavo = matrStkClosRepo.save(chkpsvo);
        } else {
            mavo.setMatrStkClosNo(0L);
            mavo.setRegDt(DateUtils.getCurrentBaseDateTime());
            mavo.setRegId(0L);
            mavo.setRegIp("localhost");
            mavo = matrStkClosRepo.save(mavo);
        }
        log.info(tag + "성공");
        return mavo;
    }

    private void makeFinalMatrStk(int rowIndex, Long custNo ,MatrStkClos mavo, Float stkQty, Long userId) {
        String tag = "ImpService.makeFinalProdStk(제품제고설정) => ";
        log.info(tag + "시작.rowIndex = " + rowIndex + "prodNo =" + mavo.getMatrNo());
        MatrStk stkvo = new MatrStk();
        stkvo.setMatrNo(mavo.getMatrNo());
        stkvo.setModDt(mavo.getModDt());
        stkvo.setModId(mavo.getModId());
        stkvo.setModIp(mavo.getModIp());
        stkvo.setWhNo(1023L); //완제품창고1 (기본값을 처리중)
        stkvo.setStatTrfDt(mavo.getClosDt());
        stkvo.setStkQty(stkQty);
        stkvo.setUsedYn("Y");
        MatrStk chkstkvo = matrStkRepo.findByCustNoAndWhNoAndMatrNoAndUsedYn(custNo, stkvo.getWhNo(),mavo.getMatrNo(), "Y");
        if (chkstkvo != null) {
            chkstkvo.setModDt(stkvo.getModDt());
            chkstkvo.setModId(stkvo.getModId());
            chkstkvo.setModIp(stkvo.getModIp());
            chkstkvo.setStkQty(stkvo.getStkQty());
            matrStkRepo.save(chkstkvo);
        } else {
            stkvo.setMatrStkNo(0L);
            stkvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            stkvo.setRegId(stkvo.getModId());
            stkvo.setRegIp(stkvo.getModIp());
            matrStkRepo.save(stkvo);
        }
        log.info(tag + "성공");
    }
    private OrdInfo makeOrdInfo(int rowIndex, Long custNo, String cmpyNm, ProdInfo prodvo, Float owhQty, Date closDt, Long userId, Long fileNo ) {
        String tag = "ImpService.makeOrdInfo(주문정보설정) => ";
        log.info(tag + "시작");
        OrdInfo ordvo = null;
        try {
            if (cmpyNm.indexOf(",") >= 0) {
                String arCmpy[] = cmpyNm.split(",");

                for (int ix = 0; ix < arCmpy.length; ix++) {
                    int delimiterIx = arCmpy[ix].indexOf("_");
                    String chkCmpyNm = arCmpy[ix].substring(0, delimiterIx);
                    float saleQty = Float.parseFloat(arCmpy[ix].substring(delimiterIx + 1));
                    ordvo = this.saveOrd(rowIndex, custNo, chkCmpyNm,prodvo.getProdNm(),closDt,saleQty,prodvo, userId, fileNo);
                }
            }
            else {
                ordvo = this.saveOrd(rowIndex,custNo, cmpyNm,prodvo.getProdNm(),closDt,owhQty,prodvo, userId, fileNo);
            }
        }
        catch (ArrayIndexOutOfBoundsException ae) { //거래처 1개 이하
            log.info(tag + "주문정보생성 시점 거래처 스플릿 싪패!!!!");
            log.info(tag + "행번호 ( " + rowIndex + " ) 주문정보생성 시점 거래처 스플릿 싪패 =  " + cmpyNm );
        }
        return ordvo;
    }

    private OrdInfo saveOrd(int rowIndex, Long custNo, String chkCmpyNm,String prodNm,Date closDt, Float saleQty, ProdInfo prodvo, Long userId, Long fileNo) {
        StringBuffer buf = new StringBuffer();
        OrdInfo ordvo = null;
        Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.sale"));
        CmpyInfo chkCmpyVo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,chkCmpyNm, "Y");
        if (chkCmpyVo != null) { //주문거래처가 있음
            ordvo = new OrdInfo();
            buf.setLength(0);
            buf.append(chkCmpyVo.getCmpyNo()).append("[ ").append(prodNm).append(" ] 주문");
            ordvo.setOrdNm(buf.toString());
            ordvo.setCmpyNo(chkCmpyVo.getCmpyNo());
            ordvo.setOrdPath(0L);
            ordvo.setOrdSts(Long.parseLong(env.getProperty("ord_status.complete"))); //고객인도
            ordvo.setOrdTp(Long.parseLong(env.getProperty("ord_oem_cd"))); //OEM
            ordvo.setPlcNo(0L);
            ordvo.setOrdDt(DateUtils.getCurrentBaseDateTime());
            ordvo.setDlvReqDt(DateUtils.getCurrentBaseDateTime());
            ordvo.setDlvDt(DateUtils.getCurrentBaseDateTime());
            ordvo.setUsedYn("Y");
            ordvo.setCustNo(custNo);
            ordvo.setFileNo(fileNo);
            ordvo.setModIp("localhost");
            ordvo.setModId(userId);
            ordvo.setModDt(DateUtils.getCurrentBaseDateTime());

            OrdInfo chkOrdVo = ordRepo.findByCustNoAndCmpyNoAndOrdDtAndUsedYn(custNo,ordvo.getCmpyNo(), closDt, "Y");
            if (chkOrdVo == null) {
                ordvo.setOrdNo(0L);
                ordvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                ordvo.setRegId(ordvo.getModId());
                ordvo.setRegIp(ordvo.getModIp());
                ordvo = ordRepo.save(ordvo);

                //주문제품생성
                OrdProd chkOrdProdVo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,ordvo.getOrdNo(), prodvo.getProdNo(), "Y");
                ProdInfo chkProdVo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodvo.getProdNo(), "Y");
                if (chkProdVo != null) {
                    OrdProd opvo = new OrdProd();
                    opvo.setDlvQty(saleQty);
                    opvo.setDlvDt(DateUtils.getCurrentBaseDateTime());
                    opvo.setOrdQty(saleQty);
                    opvo.setOrdNo(ordvo.getOrdNo());
                    opvo.setProdNo(prodvo.getProdNo());
                    opvo.setQtyPerPkg(chkProdVo.getQtyPerPkg());
                    opvo.setSaleUnit(chkProdVo.getSaleUnit());
                    opvo.setUsedYn("Y");
                    opvo.setCustNo(custNo);
                    opvo.setModIp("localhost");
                    opvo.setModId(userId);
                    opvo.setModDt(DateUtils.getCurrentBaseDateTime());

                    if (chkOrdProdVo == null) {
                        opvo.setOrdProdNo(0L);
                        opvo.setRegIp(opvo.getModIp());
                        opvo.setRegId(opvo.getModId());
                        opvo.setRegDt(opvo.getModDt());
                        ordProdRepo.save(opvo);
                    } else {
                        chkOrdProdVo.setOrdQty(opvo.getOrdQty());
                        opvo.setCustNo(custNo);
                        ordProdRepo.save(chkOrdProdVo);
                    }
                    //출고이력생성
//                    if (opvo.getOrdQty() > 0 && opvo.getOrdNo() != null) {
//                        this.makeProdOwh(rowIndex, prodvo, closDt, ordvo, opvo.getOrdQty(), userId); //출고이력생성
//                    }
                }
            }
        }
        return ordvo;
    }
}
