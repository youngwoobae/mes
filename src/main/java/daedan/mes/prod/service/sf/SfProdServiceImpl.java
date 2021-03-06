package daedan.mes.prod.service.sf;

import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.service.FileService;
import daedan.mes.make.domain.MakeIndc;
import daedan.mes.make.repository.MakeIndcRepository;
import daedan.mes.make.service.ExcelMakeService;
import daedan.mes.matr.domain.MatrFormat;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdBomFormat;
import daedan.mes.prod.domain.sf.ProdFormat;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdRepository;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("SfProdService")
public class SfProdServiceImpl implements SfProdService {

    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private MakeIndcRepository makeIndcRepo;

    @Autowired
    private ExcelMakeService excelMakeService;

    @Autowired
    private MatrRepository matrRepo;

    @Autowired
    private ProdRepository prodRepository;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProdBomRepository bomRepo;

    @Override
    public String makeSeoulFoodMakeIndcByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "sf.ProdService.makeSeoulFoodMakeIndcByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        log.info(tag + "paraMap = " + paraMap.toString());

        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));

        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int prodRowindex = Integer.parseInt(env.getProperty("excel_prod_row_no"));    //????????? ????????? ???
        int MatrRowindex = Integer.parseInt(env.getProperty("excel_matr_row_no"));// ?????? ?????????
        int ProdFirstindex = Integer.parseInt(env.getProperty("excel_prod_row_first")); // ?????? ??????
        int prodSvindex = Integer.parseInt(env.getProperty("excel_Sv_row_no")); // ?????? ??????
        String chk = "";
        for (int idx = 0; idx <= 6; ++idx) {
            XSSFSheet sheet = workbook.getSheetAt(idx); //?????? ??????~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            int rows = sheet.getPhysicalNumberOfRows();

            HttpSession session = (HttpSession) paraMap.get("session");

            String prodNm = "";
            String mainProdNm = "";
            String sveProdNm = null;
            String svProdNm = "";
            String secProdNm = "";
            Float indcWgt = 0F;
            int rate = 0;

            XSSFRow rowProdMain = sheet.getRow(prodRowindex); //?????????????????? ????????? ?????? ???
            XSSFRow rowIndcQty = sheet.getRow(MatrRowindex); //?????????????????? ????????? ?????? ???
            XSSFRow rowProdNm = sheet.getRow(prodSvindex); //?????????????????? ????????? ?????? ???
            XSSFRow row = sheet.getRow(prodRowindex); // ??? ????????? ??????

            row = sheet.getRow(1);
            Date date = row.getCell(0).getDateCellValue();
            //???????????? ??????
            for (int columnindex = ProdFirstindex; columnindex <= rows; columnindex++) {
                rate = (columnindex * 100 / rows);
                session.setAttribute("indcRate", rate);
                mainProdNm = String.valueOf(rowProdMain.getCell(columnindex));// ?????????
                if (mainProdNm.contains("??? ?????????") == true) {
                    rate = 100;
                    session.setAttribute("indcRate", rate);
                    break;
                }
                indcWgt = (float) rowIndcQty.getCell(columnindex).getNumericCellValue(); //???????????? ?????? ?????? ?????????
                if (indcWgt != 0F || indcWgt == null) {
                } else continue;

                MakeIndc mivo = new MakeIndc();
                try {
                    Float.parseFloat(mainProdNm);
                    mainProdNm = svProdNm;
                } catch (NumberFormatException e) {
                }

                sveProdNm = rowProdNm.getCell(columnindex).getStringCellValue(); // ?????? ?????????

                if (!StringUtil.isEmpty(mainProdNm)) {
                    svProdNm = mainProdNm;
                }
                if (sveProdNm != null) {
                    secProdNm = sveProdNm;
                }


                mivo.setMakeUnit(Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))); //?????? ??????(Kg??????)
                mivo.setStatCd(Long.parseLong(env.getProperty("code.matrst.purs"))); //?????? ?????? (??????)
                mivo.setIndcWgt(indcWgt);
                mivo.setMakeFrDt(date);
                mivo.setMakeToDt(date);
                mivo.setIndcDt(date);
                mivo.setCustNo(Long.parseLong(paraMap.get("custNo").toString())); //AddOn By KMJ At 21.10.21
                mivo.setUsedYn("Y");

                MakeIndc machk = makeIndcRepo.findByCustNoAndIndcQtyAndIndcNoAndProcCdAndUsedYn(custNo,mivo.getIndcWgt(), mivo.getIndcNo(), mivo.getProcCd(), "Y");
                if (machk != null) {
                    mivo.setIndcNo(machk.getIndcNo());
                    mivo.setRegDt(DateUtils.getCurrentDateTime());
                    mivo.setRegIp((String) paraMap.get("ipaddr"));
                    try {
                        mivo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    } catch (NullPointerException en) {
                        mivo.setRegId(0L);
                    }

                } else {
                    mivo.setIndcNo(0L);
                    mivo.setModDt(DateUtils.getCurrentDateTime());
                    mivo.setModIp((String) paraMap.get("ipaddr"));
                    try {
                        mivo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    } catch (NullPointerException en) {
                        mivo.setModId(0L);
                    }
                }
                prodNm = this.getProdNm(sheet, prodRowindex, columnindex, svProdNm, secProdNm).replaceAll("\\p{Z}", "");
                ProdInfo chkInfo = prodRepository.findByCustNoAndProdNmAndUsedYn(custNo, prodNm, "Y");
                if (chkInfo != null) {
                    mivo.setProdNo(chkInfo.getProdNo());
                }

                MakeIndc indcchk = makeIndcRepo.findByCustNoAndProdNoAndIndcWgtAndIndcDtAndUsedYn(custNo,mivo.getProdNo(), mivo.getIndcWgt(), mivo.getIndcDt(), "Y");
                if (indcchk != null) {
                    chk = "chk";
                    break;
                }
                excelMakeService.saveMakeProc(StringUtil.voToMap(mivo));

                rate = 100;
                session.setAttribute("indcRate", rate);

            }
        }
        return chk;
    }

    @Transactional
    @Override
    public void seoulProdBomByExcel(HashMap<String, Object> paraMap) throws Exception {
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String erpProdNm = "";
        String matrNm = "";
        Long prodNo = 0L;
        Long matrNo = 0L;
        Float mess = 0F;
        Float spga = 0F;
        int rate = 0;

        XSSFSheet sheet = workbook.getSheetAt(1); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        HttpSession session = (HttpSession) paraMap.get("session");
        int rowindex = 0;
        XSSFRow row = sheet.getRow(rowindex);

        for (rowindex = 0; rowindex <= rows; rowindex++) {
            if (rowindex < 1) continue; //???????????? skip
            row = sheet.getRow(rowindex);
            rate = (rowindex * 100 / rows);
            session.setAttribute("rate", rate);
            if (row == null) continue;
            custNo = Long.parseLong(paraMap.get("custNo").toString());

            ProdInfo prodInfo = new ProdInfo();
            try {
                prodNo = (long) row.getCell(ProdFormat.PROD_NO).getNumericCellValue(); //????????????
            } catch (NullPointerException ne) {
                prodNo = 0L;
            }

            prodNm = row.getCell(ProdFormat.PROD_NM).getStringCellValue().replaceAll("\\p{Z}", ""); // ?????????

            if (prodNm.replaceAll("\\p{Z}", "") == null) {
                rate = 100;
                session.setAttribute("rate", rate);
                break;
            }
            if (prodNm.replaceAll("\\p{Z}", "").length() <= 0) {
                rate = 100;
                session.setAttribute("rate", rate);
                break;
            }
            if (prodNm.replaceAll("\\p{Z}", "").equals("")) {
                rate = 100;
                session.setAttribute("rate", rate);
                break;
            }

            erpProdNm = row.getCell(ProdFormat.ERP_PROD_NM).getStringCellValue().replaceAll("\\p{Z}", ""); // ?????????

            try {
                prodInfo.setProdCode(row.getCell(ProdFormat.PROD_CODE).getStringCellValue()); // ??????
            } catch (NullPointerException ne) {

            }


            if (custNo == 3) {
                String brnchNo = row.getCell(ProdFormat.BRNCH_NO).getStringCellValue(); // ????????????
                prodInfo.setBrnchNo(brnchNo.equals("???(??????)") ? Long.parseLong(env.getProperty("code.base.proc_brnch-Thr"))
                        : Long.parseLong(env.getProperty("code.base.proc_brnch_tow")));
            } else if (custNo == 6) {
                String data = row.getCell(ProdFormat.BRNCH_NO).getStringCellValue();
                Long brnchNo = Long.parseLong(env.getProperty("code.base.proc_brnch"));
                CodeInfo cdchk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(brnchNo, data.replace(" ", ""), "Y");
                if (cdchk != null) {
                    prodInfo.setBrnchNo(cdchk.getCodeNo());
                }
            } else {
                prodInfo.setBrnchNo(1251L);
            }
            try {
                prodInfo.setHeatTp((long) row.getCell(ProdFormat.HEAT_TP).getNumericCellValue()); // ????????????
            } catch (NullPointerException en) {
                prodInfo.setHeatTp(0L);
            }

            prodInfo.setMess((float) row.getCell(ProdFormat.MESS).getNumericCellValue()); // ????????????

            mess = (float) row.getCell(ProdFormat.MESS).getNumericCellValue();
            spga = (float) row.getCell(ProdFormat.SPGA).getNumericCellValue();

            /* (??????)?????? ??????????????? ????????? ... ????????? ???????????? MESS * SPGA * 0.001 ?????? ????????? ???????????? ????????? ?????? ???????????? ?????? ???????????? */
            if (custNo == 3) {
                prodInfo.setMess(mess);
                prodInfo.setVol(mess);
            } else if (custNo == 6) {
                prodInfo.setMess(mess);
                prodInfo.setVol((float) (mess * spga * 0.001));

            }else{
                prodInfo.setMess(mess);
                prodInfo.setVol(mess);
            }

            try {
                prodInfo.setValidTerm((int) row.getCell(ProdFormat.VALID_TERM).getNumericCellValue()); //????????????
            } catch (NullPointerException en) {
                prodInfo.setValidTerm(12);
            }

            try {
                prodInfo.setQtyPerPkg((int) row.getCell(ProdFormat.QTY_PER_PKG).getNumericCellValue()); //??????????????????
            } catch (NullPointerException en) {
                prodInfo.setQtyPerPkg(1);
            }

            try {
                prodInfo.setSpga(spga); // ??????
            } catch (NullPointerException en) {
                prodInfo.setSpga(1F);
            }

            try {
                prodInfo.setBomLvl((long) row.getCell(ProdFormat.BOM_LVL).getNumericCellValue()); // bom??????
            } catch (NullPointerException en) {
                prodInfo.setBomLvl(1L);
            }
            String mngrBase = row.getCell(ProdFormat.MNGR_BASE).getStringCellValue(); // ??????,?????? ??????
            prodInfo.setMngrUnit(mngrBase.equals("??????") ? Long.parseLong(env.getProperty("code.base.mngrbase_vol"))
                    : Long.parseLong(env.getProperty("code.base.mngrbase_imp")));

            prodInfo.setProdBrnch(1L); //??????(???????????????)
            prodInfo.setFileNo(0L);//???????????????(???????????????)
            prodInfo.setUsedYn("Y");//????????????(???????????????)

            String exData = row.getCell(ProdFormat.SALE_UNIT).getStringCellValue(); // ?????? ??????
            Long seleUnit = Long.parseLong(env.getProperty("code.base.saleunit"));
            CodeInfo cdchk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(seleUnit, exData.replace(" ", ""), "Y");
            if (cdchk != null) {
                prodInfo.setSaleUnit(cdchk.getCodeNo());
            }

            String tmData = row.getCell(ProdFormat.SAVE_TMPR).getStringCellValue(); // ????????????
            Long saveTmpr = Long.parseLong(env.getProperty("code.base.save_tmpr_cd"));
            CodeInfo cochk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(saveTmpr, tmData.replace(" ", ""), "Y");
            if (cochk != null) {
                prodInfo.setSaveTmpr(cochk.getCodeNo());
            }

            String shData = row.getCell(ProdFormat.PROD_SHAPE).getStringCellValue(); // ????????????
            Long prodShape = Long.parseLong(env.getProperty("code.base.prod_shape"));
            CodeInfo shChk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(prodShape, shData.replace(" ", ""), "Y");
            if (shChk != null) {
                prodInfo.setProdShape(shChk.getCodeNo());
            }

            ProdInfo chkInfo = prodRepository.findByCustNoAndProdNmAndErpProdNmAndUsedYn(custNo,prodNm, erpProdNm, "Y");
            if (chkInfo != null) {
                prodInfo.setProdNo(chkInfo.getProdNo());
                prodInfo.setProdNm(chkInfo.getProdNm());
                prodInfo.setErpProdNm(erpProdNm);
                prodInfo.setModDt(DateUtils.getCurrentDateTime());
                prodInfo.setModIp(paraMap.get("ipaddr").toString());
                prodInfo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            } else {
                prodInfo.setProdNo(0L);
                prodInfo.setProdNm(prodNm);
                prodInfo.setErpProdNm(erpProdNm);
                prodInfo.setRegDt(DateUtils.getCurrentDateTime());
                prodInfo.setRegIp(paraMap.get("ipaddr").toString());
                prodInfo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            prodInfo = prodRepository.save(prodInfo);


            // ????????? ??????
            MatrInfo mtvo = new MatrInfo();

            matrNm = row.getCell(MatrFormat.MATR_NM).getStringCellValue().replaceAll("\\p{Z}", ""); // ?????????

            try {
                matrNo = (long) row.getCell(MatrFormat.MATR_NO).getNumericCellValue(); //????????????
            } catch (NullPointerException ne) {
                matrNo = 0L;
            }


            try {
                mtvo.setItemCd(row.getCell(MatrFormat.ITEM_CD).getStringCellValue()); // ????????????
            } catch (NullPointerException en) {

            }

            String matrTp = row.getCell(MatrFormat.MATR_TP).getStringCellValue(); // ???,??? ?????? ??????
            mtvo.setMatrTp(matrTp.equals("?????????") ? Long.parseLong(env.getProperty("code.matrtp.matr"))
                    : Long.parseLong(env.getProperty("code.matrtp.submatr")));

            try {
                mtvo.setValidTerm((int) row.getCell(MatrFormat.VALID_TERM).getNumericCellValue()); // ????????????
            } catch (NullPointerException en) {
                mtvo.setValidTerm(12);
            }

            try {
                mtvo.setMess((float) row.getCell(MatrFormat.MESS).getNumericCellValue()); // ????????????
            } catch (NullPointerException en) {
                mtvo.setMess(1000F);
            }
            mtvo.setVol(mtvo.getMess());

            try {
                mtvo.setSpga((float) row.getCell(MatrFormat.SPGE).getNumericCellValue()); // ??????
            } catch (NullPointerException en) {
                mtvo.setSpga(1F);
            }
            try {
                mtvo.setMade(row.getCell(MatrFormat.MADE).getStringCellValue()); // ?????????
            } catch (NullPointerException en) {

            }


            String matrMngrBase = row.getCell(MatrFormat.MNGR_BASE).getStringCellValue(); // ??????,?????? ??????
            mtvo.setMngrBase(matrMngrBase.equals("??????") ? Long.parseLong(env.getProperty("code.base.mngrbase_vol"))
                    : Long.parseLong(env.getProperty("code.base.mngrbase_imp")));

            String saData = row.getCell(MatrFormat.PURS_UNIT).getStringCellValue(); // ?????? ??????
            Long pursUnit = Long.parseLong(env.getProperty("code.base.unit"));
            CodeInfo codechk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(pursUnit, saData.replace(" ", ""), "Y");
            if (codechk != null) {
                mtvo.setPursUnit(codechk.getCodeNo());
            }

            String tmprData = row.getCell(MatrFormat.SAVE_TMPR).getStringCellValue(); // ????????????
            Long maSaveTmpr = Long.parseLong(env.getProperty("code.base.save_tmpr_cd"));
            CodeInfo chk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(maSaveTmpr, tmprData.replace(" ", ""), "Y");
            if (chk != null) {
                mtvo.setSaveTmpr(chk.getCodeNo());
            }
            mtvo.setBrnchNo(1L);
            mtvo.setUsedYn("Y");
            mtvo.setFileNo(0L);

            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                mtvo.setMatrNo(chkvo.getMatrNo());
                mtvo.setMatrNm(chkvo.getMatrNm());
                mtvo.setModDt(DateUtils.getCurrentDateTime());
                mtvo.setModIp(paraMap.get("ipaddr").toString());
                mtvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            } else {
                mtvo.setMatrNo(0L);
                mtvo.setMatrNm(matrNm);
                mtvo.setRegDt(DateUtils.getCurrentDateTime());
                mtvo.setRegIp(paraMap.get("ipaddr").toString());
                mtvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            mtvo = matrRepo.save(mtvo);

            // BOM ??????

            ProdBom bovo = new ProdBom();
            if(custNo == 2){
                bovo.setConsistRt((float) row.getCell(ProdBomFormat.CONSIST_RT).getNumericCellValue() * 100 );
            }else {
                bovo.setConsistRt((float) row.getCell(ProdBomFormat.CONSIST_RT).getNumericCellValue());
            }


            try {
                bovo.setBomLvl((long) row.getCell(ProdBomFormat.BOM_LVL).getNumericCellValue());
            } catch (NullPointerException en) {
                bovo.setBomLvl(0L);
            }

            bovo.setMatrNo(mtvo.getMatrNo());
            bovo.setProdNo(prodInfo.getProdNo());
            // ????????? ?????? purs_yn = N ?????? ???????????? ?????? ??????
            bovo.setPursYn(!matrNm.equals("?????????") ? "Y" : "N");
            bovo.setUsedYn("Y");


            ProdBom bochk = bomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo,prodInfo.getProdNo(), mtvo.getMatrNo(), "Y");
            if (bochk != null) {
                bovo.setBomNo(bochk.getBomNo());
                bovo.setModDt(DateUtils.getCurrentDateTime());
                bovo.setModIp(paraMap.get("ipaddr").toString());
                bovo.setModId(Long.parseLong(paraMap.get("userId").toString()));
            } else {
                bovo.setBomNo(0L);
                bovo.setRegDt(DateUtils.getCurrentDateTime());
                bovo.setRegIp(paraMap.get("ipaddr").toString());
                bovo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
            }
            bomRepo.save(bovo);
        }
        rate = 100;
        session.setAttribute("rate", rate);
    }



    public String getProdNm(XSSFSheet sheet, int rowindex, int colindex, String mainProdNm, String sveProdNm) {
        StringBuffer buf = new StringBuffer();

        buf.setLength(0);
        buf.append(mainProdNm);
        buf.append("\n").append(sveProdNm);
        return buf.toString().replaceAll("(\r|\n|\r\n|\n\r)", " ");
    }

}
