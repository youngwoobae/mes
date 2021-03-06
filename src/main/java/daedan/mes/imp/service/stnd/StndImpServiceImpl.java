package daedan.mes.imp.service.stnd;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.mapper.CodeMapper;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.dept.domain.DeptInfo;
import daedan.mes.dept.repository.DeptRepository;
import daedan.mes.file.service.FileService;
import daedan.mes.imp.domain.stnd.CmpyFormat;
import daedan.mes.imp.domain.stnd.MatrFormat;
import daedan.mes.imp.domain.stnd.ProdFormat;
import daedan.mes.make.mapper.MakeIndcMapper;
import daedan.mes.matr.domain.MatrCmpy;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.mapper.MatrMapper;
import daedan.mes.matr.repository.MatrCmpyRepository;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.proc.repository.ProcBrnchRepository;
import daedan.mes.prod.domain.BomFormat;
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.mapper.ProdMapper;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
import daedan.mes.stock.domain.WhInfo;
import daedan.mes.stock.repository.WhInfoRepository;
import daedan.mes.user.domain.IndsType;
import daedan.mes.user.domain.UserInfo;
import daedan.mes.user.domain.UserType;
import daedan.mes.user.repository.CustInfoRepository;
import daedan.mes.user.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mindrot.jbcrypt.BCrypt;
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
import java.util.*;

@Service("impService")
public class StndImpServiceImpl implements StndImpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private MakeIndcMapper mapper;

    @Autowired
    private CmmnService cmmnService;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private MatrMapper matrMapper;

    @Autowired
    private PursMatrRepository pursMatrRepo;

    @Autowired
    private ProcBrnchRepository procBrnchRepo;

    @Autowired
    private MatrCmpyRepository matrCmpyRepo;

    @Autowired
    private WhInfoRepository whInfoRepo;

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private DeptRepository deptRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustInfoRepository custInfoRepo;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private MatrRepository matrRepo;


    @Autowired
    private ProdRepository prodRepo;


    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private PursInfoRepository pursInfoRepo;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private ProdBomRepository prodBomRepo;



    @Override
    public int getProcRate(Map<String, Object> paraMap) {
        int procRt = 0;
        HttpSession session = (HttpSession) paraMap.get("session");
        int totCount = 0;
        try {
            totCount = (int) session.getAttribute("totCount");
        } catch (NullPointerException ne) {
            totCount = 0;
        }
        String workTp = (String) paraMap.get("workTp");
        paraMap.put("totCount", totCount);
        if (workTp.equals("breif_matr")) {
            procRt = matrMapper.getMadeMatrCount(paraMap);
        }
        if (workTp.equals("normal_matr")) {
            procRt = matrMapper.getMadeMatrCount(paraMap);
        }
        if (workTp.equals("prod_bom")) {
            procRt = prodMapper.getProdBomCount(paraMap);
        }

        return procRt;
    }

    @Override
    @Transactional
    public void makeCodeInfoByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeCodeInfoByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            log.info(tag + "excel???????????? = " + rows);
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("parCodeNo", (long) row.getCell(0).getNumericCellValue());
                map.put("codeNo", (long) row.getCell(1).getNumericCellValue());
                map.put("codeNm", row.getCell(2).getStringCellValue());
                map.put("codeAlais", row.getCell(3).getStringCellValue());
                map.put("codeBrief", row.getCell(4).getStringCellValue());
                map.put("usedYn", row.getCell(5).getStringCellValue());
                map.put("codeSeq", row.getCell(6).getNumericCellValue());
                map.put("modAbleYn", row.getCell(7).getStringCellValue());
                map.put("userId", 0L);
                map.put("custNo",custNo);
                map.put("ipaddr", paraMap.get("ipaddr"));
                codeMapper.appendCodeInfo(map);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void makeCmpyByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeCmpyByExcel => ";
        log.info(tag + "paaMap = " + paraMap.toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        CmpyFormat frm = new CmpyFormat();
        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            log.info(tag + "excel???????????? = " + rows);
            String exData = "";
            int idx = 0;
            String cmpyNm = "";
            Long parMngrGbnCd = Long.parseLong(env.getProperty("prod_brnch_root")); //??????,??????????????????

            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info("process.rowindex = " + rowindex);

                CmpyInfo vo = new CmpyInfo();

                try{
                    vo.setCmpyNo((long)row.getCell(frm.IX_CMPY_NO).getNumericCellValue()  );
                }catch(NullPointerException ne){
                    vo.setCmpyNo(0L);
                }
                try {
                    cmpyNm = row.getCell(frm.IX_CMPY_NM).getStringCellValue(); // ?????? ???
                    cmpyNm = cmpyNm.replaceAll("\\p{Z}", "");
                    if (cmpyNm.length() <= 1) continue;
                }
                catch (NullPointerException ne) {
                    if (rowindex >= rows ) {
                        break;
                    }
                    else {
                        continue;
                    }
                }
                vo.setCmpyNm(cmpyNm);
                try{
                    vo.setCmanNm(row.getCell(frm.IX_CMAN_NM).getStringCellValue()); //????????????
                } catch (NullPointerException en){
                }

                try{
                    vo.setSaupNo(String.valueOf(row.getCell(frm.IX_SAUP_NO).getStringCellValue())); //???????????????
                }catch (NullPointerException en){

                }
                try{
                    vo.setTelNo(row.getCell(frm.IX_TEL_NO).getStringCellValue());//?????? ??????
                }catch (NullPointerException e){

                }

                try{
                    vo.setFaxNo(row.getCell(frm.IX_FAX_NO).getStringCellValue());//?????? Fox
                }catch (NullPointerException e){

                }

                try{
                    vo.setCmanCellNo(row.getCell(frm.IX_CELL_NO).getStringCellValue()); //?????????????????????

                }catch (NullPointerException en){

                }

                exData = row.getCell(frm.IX_CMPY_TP).getStringCellValue(); // ??????/??????/??????
                CodeInfo cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(parMngrGbnCd ,exData.replace(" ",""),"Y");
                if(cdvo != null){
                    vo.setMngrGbnCd(cdvo.getCodeNo()); //?????? ??????
                }

                vo.setCmpyTp(Long.parseLong(env.getProperty("code.cmpytp.cmpy"))); //??????
                try{
                    vo.setReprMailAddr(row.getCell(8).getStringCellValue());//?????????
                }catch (NullPointerException en){
                }

                vo.setCmpyTp(Long.parseLong(env.getProperty("code.cmpytp.cmpy"))); //??????,??????
                vo.setMngrGbnCd(Long.parseLong(paraMap.get("mngrGbnCd").toString())); //??????, ????????????
                vo.setUsedYn("Y");
                vo.setCustNo(custNo);
                vo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                vo.setModIp(paraMap.get("ipaddr").toString());
                vo.setModDt(DateUtils.getCurrentBaseDateTime());
                CmpyInfo chkvo = null;
                if (vo.getCmpyTp() != 0L) {
                    chkvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNoAndUsedYn(custNo,vo.getMngrGbnCd(),vo.getCmpyNo(), "Y");
                }
                else {
                    chkvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,vo.getMngrGbnCd(),vo.getCmpyNm(), "Y");
                }
                if (chkvo != null) {
                    vo.setCmpyNo(chkvo.getCmpyNo());
                    vo.setRegDt(chkvo.getRegDt());
                    vo.setRegIp(chkvo.getRegIp());
                    vo.setRegId(chkvo.getRegId());
                }
                else {
                    vo.setCmpyNo(0L);
                    vo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    vo.setRegIp(paraMap.get("ipaddr").toString());
                    vo.setRegDt(DateUtils.getCurrentBaseDateTime());
                }
                vo.setCustNo(custNo);
                cmpyRepo.save(vo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    @Override
    public void makeMatrByNormalExcel(Map<String, Object> paraMap) {
        String tag = "stnd_ImpService.makeMatrByNormalExcel => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        FileInputStream file = null;
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0); //??????
            int rows = sheet.getPhysicalNumberOfRows();
            MatrFormat format = new MatrFormat();
            Long baseMadeInCd = Long.parseLong(env.getProperty("code.base.madein")); //????????? : 90
            Long mngrgbnPurs = Long.parseLong(env.getProperty("code.mngrgbn.purs")); //??????
            Long cmpyTpCmpy = Long.parseLong(env.getProperty("code.cmpytp.cmpy")); //??????
            Long basePursUnitCd = Long.parseLong(env.getProperty("code.base.unit")); //???????????? (????????? ????????? ?????? ?????????)
            Long baseSaveTmprCd = Long.parseLong(env.getProperty("code.base.save_tmpr_cd")); //????????????
            Map<String,Object> cdmap = new HashMap<String,Object>(); //???????????????

            String strChk = "";
            CodeInfo cdvo = null;
            HttpSession session = (HttpSession) paraMap.get("session");
            UserInfo uvo = (UserInfo) session.getAttribute("userInfo");

            session.getAttribute("userInfo");
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel???????????? = " + rows + " / " + rowindex);

                try {
                    MatrInfo matrvo = new MatrInfo(); // ?????????
                    matrvo.setBrnchNo(1L); //????????????
                    matrvo.setUsedYn("Y");
                    matrvo.setMatrNm(row.getCell(format.IX_MATR_NM).getStringCellValue()); // ?????????

                    strChk = row.getCell(format.IX_PURS_UNIT).getStringCellValue(); //????????????
                    cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(basePursUnitCd,strChk.toUpperCase(Locale.ROOT),"Y");
                    if (cdvo != null) {
                        matrvo.setPursUnit(cdvo.getCodeNo());
                    }
                    try {
                        matrvo.setValidTerm((int) row.getCell(format.IX_VAL_TERM).getNumericCellValue());
                    }
                    catch (NullPointerException ne) {
                        matrvo.setValidTerm(12);
                    }
                    //????????????
                    try {
                        strChk = row.getCell(format.IX_SAVE_TMPR).getStringCellValue();
                    }
                    catch (NullPointerException ne) {
                        strChk = "??????";
                    }
                    cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseSaveTmprCd,strChk.toUpperCase(Locale.ROOT),"Y");
                    if (cdvo != null) {
                        matrvo.setSaveTmpr(cdvo.getCodeNo());
                    }

                    matrvo.setSpga((float) row.getCell(format.IX_SPGA).getNumericCellValue()); //??????

                    matrvo.setMatrTp(Long.parseLong(paraMap.get("matrTp").toString())); //????????????
                    try {
                        matrvo.setVol(Float.parseFloat(String.valueOf(row.getCell(format.IX_VOL).getNumericCellValue())));  //??????
                    }
                    catch (NullPointerException ne) {

                    }

                    try {
                        matrvo.setItemCd(row.getCell(format.IX_MATR_CD).getStringCellValue());
                    }
                    catch(NullPointerException ne) {

                    }
                    try {
                        matrvo.setSz(row.getCell(format.IX_SZ).getStringCellValue()); //??????
                        cdvo = codeRepo.findByCodeNoAndUsedYn(matrvo.getMngrUnit(),"Y");
                        int index = matrvo.getSz().indexOf(cdvo.getCodeNm());
                        Long pursUnitWgt = Long.parseLong(matrvo.getSz().substring(0,index));
                        matrvo.setPursUnitWgt(pursUnitWgt); //??????????????????
                        matrvo.setVol((float) pursUnitWgt);  //??????
                        matrvo.setMess((float) pursUnitWgt); //??????

                        index = matrvo.getSz().indexOf("/");
                        String matrSz = matrvo.getSz();
                        String szPkgUnit = matrSz.substring(index+1);
                        cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(basePursUnitCd,szPkgUnit,"Y");
                        if (cdvo != null) {
                            matrvo.setPkgUnit(cdvo.getCodeNo());
                        }
                        //log.info(tag + " pursUnitWgt = " + pursUnitWgt + "vol = " + matrvo.getVol() + " : floaPursUnitWgt = " + (float)pursUnitWgt);
                        //matrvo.setSpga((float) row.getCell(format.IX_SPGA).getNumericCellValue()); //??????
                        matrvo.setSpga(1f); //??????

                        //????????????
                        cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(basePursUnitCd,"Kg".toUpperCase(Locale.ROOT),"Y");
                        if (cdvo != null) {
                            matrvo.setPursUnit(cdvo.getCodeNo());
                        }
                    }
                    catch(NullPointerException ne) {

                    }
                    //?????????
                    try {
                        strChk = row.getCell(format.IX_MADE_IN).getStringCellValue();
                        cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseMadeInCd, strChk.toUpperCase(Locale.ROOT), "Y");
                        if (cdvo != null) {
                            matrvo.setMadein(cdvo.getCodeNo());
                        } else {
                            cdvo = new CodeInfo();
                            cdvo.setParCodeNo(baseMadeInCd);
                            cdvo.setCodeNo(0L);
                            cdvo.setCodeNm(strChk);
                            cdvo.setCodeAlais(strChk);
                            cdvo.setCodeBrief(strChk);
                            cdvo.setCodeSeq(0);
                            cdvo.setModableYn("N");
                            cdvo.setUsedYn("Y");
                            cdvo.setModDt(DateUtils.getCurrentBaseDateTime());
                            cdvo.setModIp("localhost");
                            cdvo.setModId(uvo.getUserId());
                            cdvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                            cdvo.setRegIp("localhost");

                            cdvo.setRegId(uvo.getUserId());

                            cdmap.put("parCodeNo", baseMadeInCd);
                            cdvo.setCodeSeq(codeMapper.getMaxCodeSeq(cdmap));
                            cdvo = codeRepo.save(cdvo);
                            if (cdvo != null) {
                                matrvo.setMadein(cdvo.getCodeNo());
                            }
                        }
                    }
                    catch (NullPointerException ne) {

                    }
                    matrvo.setCustNo(custNo);

                    matrvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    matrvo.setModIp("localhost");
                    matrvo.setModId(uvo.getUserId());
                    matrvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    matrvo.setRegIp("localhost");
                    matrvo.setRegId(uvo.getUserId());
                    try {
                        matrvo.setMatrNo( (long) row.getCell(format.IX_MATR_NO).getNumericCellValue());
                    }
                    catch (NullPointerException ne) {
                        matrvo.setMatrNo(0L);
                    }
                    log.info(tag + " matrInfo.matrNo = " + matrvo.getMatrNo());

                    MatrInfo chkmivo = null;
                    if (matrvo.getMatrNo() == 0L) {
                        chkmivo = matrRepo.findByCustNoAndMatrNmAndUsedYn(matrvo.getCustNo(), matrvo.getMatrNm(), "Y");
                    }
                    else {
                        chkmivo = matrRepo.findByCustNoAndMatrNoAndUsedYn(matrvo.getCustNo(), matrvo.getMatrNo(), "Y");
                    }

                    if (chkmivo == null) {
                        matrvo.setMatrNo(0L);
                    }
                    else {
                        matrvo.setMatrNo(chkmivo.getMatrNo());
                        matrvo.setMatrNo(chkmivo.getMatrNo());
                        matrvo.setRegDt(chkmivo.getRegDt());
                        matrvo.setRegIp(chkmivo.getRegIp());
                        matrvo.setRegId(chkmivo.getRegId());
                    }
                    //matrvo.setFileNo(0L);

                    matrvo = matrRepo.save(matrvo);
                    try {
                        strChk = row.getCell(format.IX_CMPY_NM).getStringCellValue();
                    }
                    catch(IllegalStateException ie) {
                        strChk = "???????????????";
                    }
                    catch (NullPointerException ne) {
                        strChk = "???????????????";
                    }
                    CmpyInfo cmvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnPurs,strChk,"Y");
                    if (cmvo == null) {
                        cmvo = new CmpyInfo();
                        cmvo.setCmpyNm(strChk);
                        cmvo.setMngrGbnCd(mngrgbnPurs); //???????????????
                        cmvo.setCmpyTp(cmpyTpCmpy); //??????
                        cmvo.setUsedYn("Y");
                        cmvo.setModDt(DateUtils.getCurrentBaseDateTime());
                        cmvo.setModIp("localhost");
                        cmvo.setModId(uvo.getUserId());
                        cmvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                        cmvo.setRegIp("localhost");
                        cmvo.setRegId(uvo.getUserId());
                        cmvo.setCustNo(custNo);
                        cmvo = cmpyRepo.save(cmvo);
                    }

                    MatrCmpy mchkvo = matrCmpyRepo.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,cmvo.getCmpyNo(),matrvo.getMatrNo(),"Y");
                    if (mchkvo == null) {
                        mchkvo = new MatrCmpy();
                        mchkvo.setMatrCmpyNo(0L);
                        mchkvo.setMatrNo(matrvo.getMatrNo());
                        mchkvo.setCmpyNo(cmvo.getCmpyNo());
                        mchkvo.setDefaultYn("N");
                        mchkvo.setUsedYn("Y");
                        mchkvo.setModDt(DateUtils.getCurrentBaseDateTime());
                        mchkvo.setModIp("localhost");

                        mchkvo.setModId(uvo.getUserId());
                        mchkvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                        mchkvo.setRegIp("localhost");
                        mchkvo.setRegId(uvo.getUserId());
                        mchkvo.setCustNo(custNo);
                        matrCmpyRepo.save(mchkvo);

                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                    continue;
                }



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*????????? , ???????????? ?????? ????????? ????????? ??????????????? ??????
     * ?????????????????? ???????????? ???????????? ???????????????.
     * ??????????????? ??????????????? ???????????? ?????? ??????.*/
    @Transactional
    @Override
    public void makeMatrByBriefExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ImptoolService.makeMatrByBriefExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Map<String, Object> Map = new HashMap<String, Object>();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);


        String matrNm = "";
        String cmpy = "";
        Long matrNo = 0L;

        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();

        for (rowindex = 0; rowindex < rows; rowindex++) {
            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;
            try {
                try {
                    if (row.getCell(1).getStringCellValue().equals("")) {
                        continue;
                    }
                } catch (IllegalStateException e) {

                }
                cmpy = row.getCell(1).getStringCellValue();


            } catch (NullPointerException en) {
                continue;
            }

            MatrCmpy mcvo = new MatrCmpy();

            matrNm = row.getCell(2).getStringCellValue();
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // ???????????? ??????
            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                matrNo = chkvo.getMatrNo();
            }
            Long mngrgbnPurs = Long.parseLong(env.getProperty("code.mngrgbn.purs"));
            CmpyInfo chkInfo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnPurs,cmpy, "Y");
            if (chkInfo != null) {
                mcvo.setCmpyNo(chkInfo.getCmpyNo());
            }
            mcvo.setMatrNo(matrNo);
            mcvo.setUsedYn("Y");
            mcvo.setCustNo(custNo);
            mcvo.setDefaultYn("Y");
            mcvo.setRegDt(DateUtils.getCurrentDate());
            mcvo.setRegId(0L);
            mcvo.setRegIp(paraMap.get("ipaddr").toString());
            matrCmpyRepo.save(mcvo);
        }

    }

    @Transactional
    @Override
    public void makePursByExcel(Map<String, Object> paraMap) {
        PursInfo ivo = null;
        PursMatr mvo = null;

        String tag = "ImptoolService.makeMatrByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        Long mngrGbnPurs = Long.parseLong(env.getProperty("code.mngrgbn.purs"));
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();


            CmpyInfo cmpyvo = null;
            MatrInfo matrvo = null;
            CodeInfo codevo = null;
            final SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd");
            int idx = 0;
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip
                //if (rowindex > 3) break; //?????????
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel???????????? = " + rows + " / " + rowindex);

                //????????????????????????
                ivo = new PursInfo();
                ivo.setPursDt(tdf.parse(row.getCell(1).getStringCellValue())); //????????????
                ivo.setDlvReqDt(tdf.parse(row.getCell(1).getStringCellValue())); //??????????????????
                ivo.setDlvDt(tdf.parse(row.getCell(1).getStringCellValue())); //????????????
                ivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.end"))); //????????????(??????)
                ivo.setIndcNo(0L);//??????????????????(???????????????)
                ivo.setOrdNo(0L); //????????????(???????????????)
                ivo.setUsedYn("Y");//????????????(???????????????)
                ivo.setCustNo(custNo);
                ivo.setRegDt(DateUtils.getCurrentDate());
                ivo.setRegId(0L);
                ivo.setRegIp(paraMap.get("ipaddr").toString());
                ivo.setModDt(DateUtils.getCurrentDate());
                ivo.setModId(0L);
                ivo.setModIp(paraMap.get("ipaddr").toString());
                cmpyvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrGbnPurs, row.getCell(2).getStringCellValue(), "Y");
                if (cmpyvo == null) {
                    log.error(tag + "??????????????? ?????? ?????? LineNo = " + rowindex);
                    continue;
                }
                //ivo.setCmpyNo(cmpyvo.getCmpyNo()); //???????????????


                PursInfo ckivo = pursInfoRepo.findByCustNoAndPursDtAndDlvReqDtAndDlvDtAndUsedYn(custNo,ivo.getPursDt(), ivo.getDlvReqDt(), ivo.getDlvDt(), "Y");
                if (ckivo != null) {
                    ivo.setPursNo(ckivo.getPursNo());
                }
                ckivo = pursInfoRepo.save(ivo);

                //????????????????????????
                mvo = new PursMatr();
                mvo.setPursNo(ckivo.getPursNo());
                String itemCd = row.getCell(3).getStringCellValue();
                String matrNm = row.getCell(4).getStringCellValue();
                matrvo = matrRepo.findByCustNoAndItemCdAndMatrNmAndUsedYn(custNo,itemCd, matrNm, "Y");
                if (matrvo == null) {
                    log.error(tag + "???????????? ?????? ?????? ?????? LineNo = " + rowindex);
                    continue;
                }
                mvo.setMatrNo(matrvo.getMatrNo()); //????????????

                //??????????????????
                codevo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(Long.parseLong(env.getProperty("code.base.unit")), row.getCell(12).getStringCellValue(), "Y");
                if (codevo == null) {
                    log.error(tag + "?????????????????? ?????? ?????? LineNo = " + rowindex);
                    continue;
                }
                mvo.setPursQty((float) row.getCell(11).getNumericCellValue()); //????????????
                mvo.setPursUnit(codevo.getCodeNo());
                mvo.setPursAmt(0L);//????????????(???????????????)
                mvo.setUsedYn("Y");//????????????(???????????????)
                mvo.setRegId(ivo.getRegId());
                mvo.setRegIp(ivo.getRegIp());
                mvo.setRegDt(ivo.getRegDt());
                mvo.setModId(ivo.getRegId());
                mvo.setModIp(ivo.getRegIp());
                mvo.setModDt(ivo.getRegDt());
                PursMatr ckmvo = pursMatrRepo.findByCustNoAndPursNoAndMatrNoAndUsedYn(custNo,mvo.getPursNo(), mvo.getMatrNo(), "Y");
                if (ckmvo != null) {
                    mvo.setPursMatrNo(ckmvo.getPursMatrNo());
                    mvo.setPursQty(ckmvo.getPursQty() + mvo.getPursQty());
                }
                mvo.setCustNo(custNo);
                pursMatrRepo.save(mvo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public void makeProdByExcel(Map<String, Object> paraMap) {
        String tag = "stnd_ImpService.makeProdByExcel => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        String filePath = fileService.getFileInfo(fileRoot,fileNo);

        FileInputStream file = null;
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0); //??????
            int rows = sheet.getPhysicalNumberOfRows();
            ProdFormat format = new ProdFormat();
            Long baseProdTp = Long.parseLong(env.getProperty("code.base.prodTp")); //?????????????????? : 34
            Long prodTpOem = Long.parseLong(env.getProperty("ord.oem")); //????????????(OEM) : 35
            Long baseSaveTmpr = Long.parseLong(env.getProperty("code.base.save_tmpr_cd")); //???????????? : 120
            Long baseSaleUnit = Long.parseLong(env.getProperty("code.base.sale_unit")); //???????????? : 80
            Long mngrGbnSale = Long.parseLong(env.getProperty("code.mngrgbn.sale")); //?????????????????????(?????????) : 21
            Long whTpSale = Long.parseLong((env.getProperty("wh_type_prod"))); //????????????(???????????????) :191
            Long brnchNo = Long.parseLong(env.getProperty("code.base.proc_brnch-other")); //????????????(??????) : 1299
            String strChk = "";
            CodeInfo cdvo = null;
            HttpSession session = (HttpSession) paraMap.get("session");

            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel???????????? = " + rows + " / " + rowindex);

                try {
                    ProdInfo prodvo = new ProdInfo();
                    prodvo.setUsedYn("Y");

                    prodvo.setProdNm(row.getCell(format.IX_PROD_NM).getStringCellValue()); //??????
                    log.info(tag +  " ??????= " + prodvo.getProdNm());
                    try {
                        prodvo.setErpProdNm(row.getCell(format.IX_PROD_ERP_NM).getStringCellValue()); //ERP??????
                    }
                    catch (NullPointerException ne) {

                    }
                    try {
                        prodvo.setErpProdNm(row.getCell(format.IX_PROD_CD).getStringCellValue()); //?????????
                    }
                    catch (NullPointerException ne) {

                    }
                    //????????????
                    try {
                        prodvo.setProdCode(row.getCell(format.IX_PROD_CD).getStringCellValue());
                    }
                    catch (NullPointerException ne) {

                    }

                    prodvo.setBrnchNo(brnchNo); //????????????(1299-??????)
                    //??????
                    prodvo.setVol((float) row.getCell(format.IX_WEIGHT).getNumericCellValue());

                    //??????
                    try {
                        prodvo.setSpga((float) row.getCell(format.IX_SPGA).getNumericCellValue());
                    }
                    catch(NullPointerException ne) {
                        prodvo.setSpga(1f);
                    }

                    //????????????(oem.odm)???
                    strChk = row.getCell(format.IX_PROD_TYPE).getStringCellValue();
                    cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseProdTp,strChk,"Y");
                    if (cdvo != null) {
                        prodvo.setProdTp(cdvo.getCodeNo());
                    }
                    else {
                        prodvo.setProdTp(Long.parseLong(env.getProperty("ord.prjt")));
                    }
                    if (prodvo.getProdTp() == prodTpOem ) {
                        strChk = row.getCell(format.IX_OEM_CMPY).getStringCellValue();
                        CmpyInfo civo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo, mngrGbnSale, strChk, "Y");
                        if (civo != null) {
                            prodvo.setCmpyNo(civo.getCmpyNo());
                        }
                        else {
                            civo = new CmpyInfo();
                            civo.setCmpyNm(strChk);
                            civo.setMngrGbnCd(mngrGbnSale);
                            civo.setCmpyTp(Long.parseLong(env.getProperty("code.cmpytp.cmpy")));
                            civo.setCustNo(custNo);
                            civo.setModDt(DateUtils.getCurrentBaseDateTime());
                            civo.setModId(userId);
                            civo.setModIp(ipaddr);
                            civo.setRegDt(DateUtils.getCurrentBaseDateTime());
                            civo.setRegId(userId);
                            civo.setRegIp(ipaddr);
                            civo.setUsedYn("Y");
                            civo = cmpyRepo.save(civo);
                        }
                        if (civo != null) {
                            prodvo.setCmpyNo(civo.getCmpyNo());
                        }
                        else {
                            prodvo.setCmpyNo(0L);
                        }
                    }
                    //??????????????????
                    strChk = row.getCell(format.IX_SAVE_TMPR).getStringCellValue();
                    cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseSaveTmpr,strChk.toUpperCase(Locale.ROOT),"Y");
                    if (cdvo != null) {
                        prodvo.setSaveTmpr(cdvo.getCodeNo());
                    }
                    else {
                        prodvo.setSaveTmpr(0L);
                    }
                    //??????????????????
                    if (prodvo.getSaveTmpr() > 0L) {
                            List<WhInfo> wids = whInfoRepo.findAllByCustNoAndSaveTmprAndWhTpAndUsedYn(custNo, prodvo.getSaveTmpr(), whTpSale, "Y");
                        if (wids != null) {
                            prodvo.setWhNo(wids.get(0).getWhNo());
                        } else {
                            prodvo.setWhNo(0L);
                        }
                    }
                    else {
                        prodvo.setWhNo(0L);
                    }
                    //????????????
                    try {
                        prodvo.setValidTerm((int) row.getCell(format.IX_VAL_TERM).getNumericCellValue());
                    }
                    catch (NullPointerException ne) {
                        prodvo.setValidTerm(12);
                    }
                    //??????????????? ?????? ???????????? ?????? ????????? ????????? ???????????? ???????????? ???.


                    //??????,??????,??????
                    try {
                        strChk = row.getCell(format.IX_PROD_SZ).getStringCellValue();
                        prodvo.setSz(strChk);
                        int index = prodvo.getSz().indexOf("kg");
                        Long pursUnitWgt = Long.parseLong(prodvo.getSz().substring(0,index));
                        prodvo.setVol((float) pursUnitWgt);  //??????
                        prodvo.setMess((float) pursUnitWgt); //??????
                    }
                    catch ( StringIndexOutOfBoundsException ne) {
                        prodvo.setVol(1000f);  //??????
                        prodvo.setMess(1000f); //??????
                    }
                    catch ( NullPointerException ne) {
                        prodvo.setVol(1000f);  //??????
                        prodvo.setMess(1000f); //??????
                    }
                    //????????????
                    strChk = row.getCell(format.IX_SALE_UNIT).getStringCellValue();
                    cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(baseSaleUnit,strChk.toUpperCase(Locale.ROOT),"Y");
                    if (cdvo != null) {
                        prodvo.setSaleUnit(cdvo.getCodeNo());
                    }

                    //?????????
                    try {
                        prodvo.setQtyPerPkg((int)row.getCell(format.IX_QTY_PER_PKG).getNumericCellValue());
                    }
                    catch(NullPointerException ne) {
                        row.getCell(1);
                    }
                    prodvo.setBomLvl(1L);
                    try {
                        prodvo.setSpga((float) row.getCell(format.IX_SPGA).getNumericCellValue()); //??????
                    }
                    catch(NullPointerException ne) {
                        prodvo.setSpga(1f); //??????
                    }
                    prodvo.setCustNo(custNo);
                    prodvo.setUsedYn("Y");
                    prodvo.setModDt(DateUtils.getCurrentBaseDateTime());
                    prodvo.setModIp(ipaddr);
                    prodvo.setModId(userId);
                    prodvo.setRegDt(DateUtils.getCurrentBaseDateTime());
                    prodvo.setRegIp(ipaddr);
                    prodvo.setRegId(userId);
                    try {
                        prodvo.setProdNo( (long) row.getCell(format.IX_PROD_NO).getNumericCellValue());
                    }
                    catch (NullPointerException ne) {
                        prodvo.setProdNo(0L);
                    }
                    log.info(tag + " prodInfo.prodNo = " + prodvo.getProdNo());

                    ProdInfo chkvo = null;
                    if (prodvo.getProdNo() == 0L) {
                        if (prodvo.getProdCode() != null) {
                            chkvo = prodRepo.findByCustNoAndProdCodeAndUsedYn(prodvo.getCustNo(), prodvo.getProdCode(), "Y");
                        }
                        else {
                            chkvo = prodRepo.findByCustNoAndProdNmAndUsedYn(prodvo.getCustNo(), prodvo.getProdNm(), "Y");
                        }
                    }
                    else {
                        chkvo = prodRepo.findByCustNoAndProdNoAndUsedYn(prodvo.getCustNo(), prodvo.getProdNo(), "Y");
                    }

                    if (chkvo == null) {
                        prodvo.setProdNo(0L);
                    }
                    else {
                        prodvo.setProdNo(chkvo.getProdNo());
                        prodvo.setRegDt(chkvo.getRegDt());
                        prodvo.setRegIp(chkvo.getRegIp());
                        prodvo.setRegId(chkvo.getRegId());
                    }
                    prodRepo.save(prodvo);
                    int uploadRate = Math.round ( ( rowindex /  rows) * 100);
                    session.setAttribute("uploadRate", uploadRate);
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public List<Map<String, Object>> makeProdBomByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeProdBomByExcel => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Long userId = Long.parseLong(paraMap.get("userId").toString());
        String ipaddr = paraMap.get("ipaddr").toString();
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        log.info(tag + "filePath = " + filePath);


        ArrayList<Map<String, Object>> errList = new ArrayList<Map<String, Object>>();
        Map<String, Object> errMap = null;
        StringBuffer buf = new StringBuffer();
        FileInputStream file = null;
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            HttpSession session = (HttpSession) paraMap.get("session");
            session.setAttribute("totCount", rows);
            log.info(tag + "excel ???????????? ?????? = " + session.getAttribute("totCount"));

            String prodNm = "";
            String matrNm = "";
            String prodCd = "";
            String matrCd = "";
            String matrTpNm = "";
            Long matrTpCd = 0L;
            ProdBom bomchkvo = null;
            MatrInfo  chkmivo = null;
            ProdInfo chkpivo = null;
            Long rowMatr = Long.parseLong(env.getProperty("rawmatr_cd")); //??????????????????(??????) : 44
            Long subMatr = Long.parseLong(env.getProperty("submatr_cd")); //??????????????????(??????) : 43
            boolean isExist = false;
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex == 0) continue; //???????????? skip
                //if (rowindex > 100) break; //?????????
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel????????? = " + rowindex + " / " + rows);
                //??????????????????

                ProdBom bomvo = new ProdBom();
                matrNm = "";
                //BOM????????????
                try {
                    bomvo.setBomNo((long) row.getCell(BomFormat.BOM_NO).getNumericCellValue());
                }
                catch(NullPointerException ne) {
                    bomvo.setBomNo(0L);
                }

                catch(IllegalStateException ne) {
                    bomvo.setBomNo(0L);
                }
                //??????
                try {
                    bomvo.setProdNo((long) row.getCell(BomFormat.PROD_NO).getNumericCellValue());
                }
                catch(NullPointerException ne) {
                    bomvo.setProdNo(0L);
                }
                catch(IllegalStateException ne) {
                    bomvo.setProdNo(0L);
                }

                //????????????
                try {
                    bomvo.setMatrNo((long) row.getCell(BomFormat.MATR_NO).getNumericCellValue());
                }
                catch(NullPointerException ne) {
                    bomvo.setMatrNo(0L);
                }
                catch(IllegalStateException ne) {
                    bomvo.setMatrNo(0L);
                }
                //????????????
                try {
                    prodCd = row.getCell(BomFormat.PROD_CD).getStringCellValue();
                }
                catch(NullPointerException ne) {
                    prodCd = "";
                }
                //??????
                try {
                    prodNm = row.getCell(BomFormat.PROD_NM).getStringCellValue();
                }
                catch(NullPointerException ne) {
                    prodNm = "";
                }
                //????????????
                try {
                    matrCd = row.getCell(BomFormat.ITEM_CD).getStringCellValue();
                }
                catch(NullPointerException ne) {
                    matrCd = "";
                }
                //????????????
                try {
                    matrNm = row.getCell(BomFormat.MATR_NM).getStringCellValue();
                }
                catch(NullPointerException ne) {

                }
                //BOM LEVEL (0:????????? 1=??????, 2=??????????????? 1???????????? ?????? ?????? ???????????? ????????? ????????????)
                try {
                    bomvo.setBomLvl((long) row.getCell(BomFormat.BOM_LVL).getNumericCellValue());
                }
                catch(NullPointerException ne) {
                    bomvo.setBomLvl(1L);
                }
                catch(IllegalStateException ne) {
                    bomvo.setBomLvl(1L);
                }
                matrTpNm =  row.getCell(BomFormat.MATR_TP).getStringCellValue();
                matrTpCd =  matrTpNm.equals("??????") ?  rowMatr : subMatr;

                //????????????
                try {
                    bomvo.setConsistRt((float) row.getCell(BomFormat.CONSIST_RT).getNumericCellValue());
                }
                catch(NullPointerException ne) {
                    bomvo.setConsistRt(0f);
                }
                catch(IllegalStateException ne) {
                    bomvo.setConsistRt(0f);
                }

                if (bomvo.getProdNo() == 0L) {
                    if (!prodCd.equals("")) {
                        chkpivo = prodRepo.findByCustNoAndProdCodeAndUsedYn(custNo, prodCd, "Y");
                    } else {
                        chkpivo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo, prodNm, "Y");
                    }
                    try {
                        bomvo.setProdNo(chkpivo.getProdNo());
                    }

                    catch (NullPointerException ne) {
                        isExist = false;
                        for (Map<String, Object> el : errList) {
                            if (el.get("contnt").toString().equals(prodNm)) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            errMap = new HashMap<String, Object>();
                            errMap.put("rownum", rowindex);
                            errMap.put("header", "???????????? ??????.");
                            errMap.put("contnt", prodNm);
                            errList.add(errMap);
                            continue;
                        }
                    }
                }
                if (bomvo.getMatrNo() == 0L) {
                    if (!matrCd.equals("")) {
                        chkmivo = matrRepo.findByCustNoAndItemCdAndUsedYn(custNo, matrCd, "Y");
                    } else {
                        chkmivo = matrRepo.findByCustNoAndMatrTpAndMatrNmAndUsedYn(custNo, matrTpCd, matrNm, "Y");
                    }
                    try {
                        bomvo.setMatrNo(chkmivo.getMatrNo());
                    }
                    catch (NullPointerException ne) {
                        isExist = false;
                        for (Map<String, Object> el : errList) {
                            if (el.get("contnt").toString().equals(matrNm)) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            errMap = new HashMap<String, Object>();
                            errMap.put("rownum", rowindex);
                            errMap.put("header", "??????????????? ??????.");
                            errMap.put("contnt", matrNm);
                            errList.add(errMap);
                            continue;
                        }
                    }
                }
                bomvo.setModDt(DateUtils.getCurrentBaseDateTime());
                bomvo.setModId(userId);
                bomvo.setModIp(ipaddr);
                bomvo.setUsedYn("Y");
                bomvo.setPursYn("Y");
                bomvo.setCustNo(custNo);
                if (bomvo.getBomNo() == 0L) {
                    bomchkvo = prodBomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo, bomvo.getProdNo(), bomvo.getMatrNo(), "Y");
                }
                else {
                    bomchkvo = prodBomRepo.findByCustNoAndBomNoAndUsedYn(custNo, bomvo.getBomNo(), "Y");
                }
                if (bomchkvo != null) {
                    bomvo.setBomNo(bomchkvo.getBomNo());
                    bomvo.setRegDt(bomchkvo.getRegDt());
                    bomvo.setRegId(bomchkvo.getRegId());
                    bomvo.setRegIp(bomchkvo.getRegIp());
                }
                else {
                    bomvo.setRegId(userId);
                    bomvo.setRegIp(ipaddr);
                    bomvo.setRegDt(DateUtils.getCurrentDate());
                }
                prodBomRepo.save(bomvo);
                int uploadRate = Math.round ( ( rowindex /  rows) * 100);
                session.setAttribute("uploadRate", uploadRate);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return errList;
    }

    @Override
    public void makeOrdByExcel(Map<String, Object> paraMap) {
        String tag = "StndImpService.makeOrdByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();


            CodeInfo codevo = null;
            OrdInfo ovo = new OrdInfo();
            final SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd");
            int idx = 0;
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel???????????? = " + rows + " / " + rowindex);

                //??????????????????
                ovo = new OrdInfo();
                buf.setLength(0);
                buf.append(row.getCell(1).getStringCellValue())
                        .append("_")
                        .append(row.getCell(2).getStringCellValue())
                        .append("_??????");
                ovo.setOrdNm(buf.toString());
                OrdInfo ckvo = ordRepo.findByCustNoAndOrdNmAndUsedYn(custNo,ovo.getOrdNm(), "Y");
                if (ckvo != null) { //???????????????????????? skip
                    //ovo.setOrdNo(ckvo.getOrdNo());
                    continue;
                }

                ovo.setOrdTp(Long.parseLong(env.getProperty("code.ordtype.project"))); //????????????-?????????
                ovo.setOrdSts(Long.parseLong(env.getProperty("ord_stat.complete"))); //????????????-????????????
                ovo.setOrdDt(tdf.parse(row.getCell(1).getStringCellValue()));//?????????
                ovo.setDlvReqDt(ovo.getOrdDt());//??????????????????
                ovo.setDlvDt(ovo.getOrdDt()); //?????????
                ovo.setUsedYn("Y");


                //????????????????????? ??????
                CmpyInfo cmpyvo = new CmpyInfo();
                cmpyvo.setCmpyNm(row.getCell(2).getStringCellValue());
                cmpyvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgbn.purs"))); //?????????
                CmpyInfo ckcmpyvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,cmpyvo.getMngrGbnCd(), cmpyvo.getCmpyNm(), "Y");
                if (ckcmpyvo == null) {
                    log.info(tag + "????????????????????????????????? : ???????????? = " + rowindex);
                    continue;
                }
                ovo.setCmpyNo(ckcmpyvo.getCmpyNo());
                ovo.setRegId(0L);
                ovo.setRegIp(paraMap.get("ipaddr").toString());
                ovo.setRegDt(DateUtils.getCurrentDate());
                ovo.setModId(ovo.getRegId());
                ovo.setModIp(ovo.getRegIp());
                ovo.setModDt(ovo.getRegDt());
                OrdInfo ckord = ordRepo.findByCustNoAndOrdNmAndUsedYn(custNo,ovo.getOrdNm(), "Y");
                if (ckord != null) {
                    ovo.setOrdNo(ckord.getOrdNo());
                }
                ovo.setCustNo(custNo);
                ordRepo.save(ovo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeOrdProdByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeOrdProdByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        OrdInfo ovo = null;
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            CodeInfo codevo = null;
            ProdInfo prodvo = null;
            OrdInfo ivo = new OrdInfo();
            OrdProd pvo = null;
            final SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd");
            int idx = 0;
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //???????????? skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                int procRt = (rowindex / rows) * 100;
                log.info(tag + "excel????????? ================>>>>>> " + rows + " / " + procRt);
                pvo = new OrdProd();

                //???????????? ??????
                buf.setLength(0);
                buf.append(row.getCell(1).getStringCellValue())
                        .append("_")
                        .append(row.getCell(2).getStringCellValue())
                        .append("_??????");
                ivo = ordRepo.findByCustNoAndOrdNmAndUsedYn(custNo,buf.toString(), "Y");
                if (ivo == null) {
                    log.info(tag + "?????????????????????????????? ????????? = " + rowindex);
                    continue;
                }
                pvo.setOrdNo(ivo.getOrdNo());//????????????
                //??????????????????
                prodvo = prodRepo.findByCustNoAndProdCodeAndProdNmAndUsedYn(custNo,row.getCell(1).getStringCellValue(), row.getCell(4).getStringCellValue(), "Y");
                if (prodvo == null) {
                    log.error(tag + "???????????? ?????? ?????? LineNo = " + rowindex);
                    continue;
                }
                pvo.setProdNo(prodvo.getProdNo());

                pvo.setOrdSz(prodvo.getSz() == null ? prodvo.getProdCode() : prodvo.getSz());

                //??????????????????
                codevo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(Long.parseLong(env.getProperty("code.base.saleunit")), row.getCell(6).getStringCellValue(), "Y");
                if (codevo == null) {
                    log.error(tag + "???????????????????????? ?????? ?????? LineNo = " + rowindex);
                    continue;
                }
                pvo.setSaleUnit(codevo.getCodeNo()); //??????????????????

                pvo.setOrdQty(Float.parseFloat(String.valueOf(row.getCell(5).getNumericCellValue())));
                pvo.setQtyPerPkg(1); //???????????????(???????????????)
                pvo.setUsedYn("Y");//????????????(???????????????)
                pvo.setRegId(0L);
                pvo.setRegIp(paraMap.get("ipaddr").toString());
                pvo.setRegDt(DateUtils.getCurrentDate());
                pvo.setModId(prodvo.getRegId());
                pvo.setModIp(prodvo.getRegIp());
                pvo.setModDt(prodvo.getRegDt());
                OrdProd ckvo = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,pvo.getOrdNo(), pvo.getProdNo(), "Y");
                if (ckvo != null) {
                    pvo.setOrdProdNo(ckvo.getOrdProdNo());
                }
                pvo.setCustNo(custNo);
                ordProdRepo.save(pvo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ??????????????? Excel ?????? ??????
    @Override
    @Transactional
    public void prodIndcExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodIndcExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String matrNm = "";
        String exData = "";
        String prodcd = "";
        Long brnchNo = 0L;
        Float spga = 0F;

        Float mess = 0f;
        double magrbase = 0;

        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        ????????? ??????
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try{
                matrNm = row.getCell(0).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");
                if (prodNm.contains("??????") == true) {
                    break;
                }
            }  catch (NullPointerException ne) {
                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
                continue;
            }

//            MatrInfo matrInfo = new MatrInfo();
////          prodcd = row.getCell(0).getStringCellValue(); // ????????????
////          matrInfo.setItemCd(prodcd);
//            matrInfo.setMatrNm(matrNm);
//            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //????????????
//            matrInfo.setValidTerm(12); //????????????-12??????
//            matrInfo.setPursUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); //????????????(KG)
//            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); // ?????? / ?????????,?????????
//            matrInfo.setBrnchNo(1L);
//            matrInfo.setUsedYn("Y");
//            matrInfo.setVol(0F);
//            matrInfo.setValidTerm(12);
//            matrInfo.setVol(1000F);
//            matrInfo.setMess(1000F);
//            matrInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_vol")));
//            matrInfo.setSpge(1F);
//
//            MatrInfo chkMatr = matrRepo.findByMatrNmAndUsedYn(matrNm, "Y");
//            if(chkMatr != null ){
//                matrInfo.setMatrNo(chkMatr.getMatrNo());
//                matrInfo.setRegIp(chkMatr.getRegIp());
//                matrInfo.setRegId(chkMatr.getRegId());
//                matrInfo.setRegDt(chkMatr.getRegDt());
//            }else{
//                matrInfo.setMatrNo(0L);
//                matrInfo.setModIp("127.0.0.1");
//                matrInfo.setModId(2L);
//                matrInfo.setModDt(DateUtils.getCurrentDate());
//            }
//            matrInfo.setCustNo(custNo);
//            matrRepo.save(matrInfo);

            MatrCmpy cmvo = new MatrCmpy();
            Long mngrgbnCd = Long.parseLong(env.getProperty("code.mngrgbn.purs"));//???????????????
            CmpyInfo cmpyvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndUsedYn(custNo,mngrgbnCd,"???????????????","Y");

            cmvo.setCmpyNo(cmpyvo.getCmpyNo());
            cmvo.setDefaultYn("N");
            cmvo.setUsedYn("Y");

            MatrInfo chkMatr = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if(chkMatr != null ){
                cmvo.setMatrNo(chkMatr.getMatrNo());
                cmvo.setRegIp(chkMatr.getRegIp());
                cmvo.setRegId(chkMatr.getRegId());
                cmvo.setRegDt(chkMatr.getRegDt());
            }else{
                cmvo.setMatrNo(0L);
                cmvo.setModIp("127.0.0.1");
                cmvo.setModId(2L);
                cmvo.setModDt(DateUtils.getCurrentDate());
            }
            cmvo.setCustNo(custNo);
            matrCmpyRepo.save(cmvo);

//            ?????? ??????
//
//            try {
//                prodNm = row.getCell(1).getStringCellValue();
//                prodNm = prodNm.replaceAll("\\p{Z}", "");
//                if (prodNm.contains("??????") == true) {
//                    break;
//                }
//            } catch (NullPointerException ne) {
//                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
//                continue;
//            }
//            ProdInfo prodInfo = new ProdInfo();
//            prodcd = row.getCell(0).getStringCellValue(); // ????????????
//            if(prodcd == null){continue;}
//            prodInfo.setProdCode(prodcd);
//            prodInfo.setErpProdNm(prodNm);
//            double qtypkg = row.getCell(6).getNumericCellValue();
//            prodInfo.setQtyPerPkg((int)qtypkg); //??????????????????(???????????????)
//            prodInfo.setFileNo(0L);//???????????????(???????????????)
//            prodInfo.setUsedYn("Y");//????????????(???????????????)
//            prodInfo.setProdBrnch(1L); //??????(???????????????)
//            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //????????????
//            prodInfo.setBrnchNo(1251L); // ????????? ??????
//            prodInfo.setBrix(0F);
//            prodInfo.setSpga(1F); // ?????? ??????
//            prodInfo.setHeatTp(0L);
//
//            Float vol = (float) row.getCell(4).getNumericCellValue();
//            exData = row.getCell(5).getStringCellValue();
//            Long prodShape = Long.parseLong(env.getProperty("code.base.saleunit"));
//            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(prodShape, exData.replace(" ", ""), "Y");
//            if (mgvo != null) {
//                Long prodUnit = mgvo.getCodeNo();
//                prodInfo.setSaleUnit(prodUnit); // ??????
//
//                if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))){ // KG??? G?????? ??????
//                    prodInfo.setVol(vol*1000);
//                    prodInfo.setMess(vol*1000);
//                }else {
//                    prodInfo.setVol(vol);
//                    prodInfo.setMess(vol);
//                }if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_ml"))){
//                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_vol")));
//                }else {
//                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_imp")));
//                }
//
//            }
//
//            ProdInfo chkInfo = prodRepo.findByProdNmAndUsedYn(prodNm, "Y");
//            if(chkInfo != null ){
//                prodInfo.setProdNo(chkInfo.getProdNo());
//                prodInfo.setRegIp(chkInfo.getRegIp());
//                prodInfo.setRegId(chkInfo.getRegId());
//                prodInfo.setRegDt(chkInfo.getRegDt());
//                prodInfo.setProdNm(chkInfo.getProdNm());
//            }else{
//                prodInfo.setProdNm(prodNm);
//                prodInfo.setModIp("127.0.0.1");
//                prodInfo.setModId(2L);
//                prodInfo.setModDt(DateUtils.getCurrentDate());
//            }
//
//            prodRepo.save(prodInfo);
//
        }
    }




    @Override
    @Transactional
    public void prodBomExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodBomExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> Map = new HashMap<String, Object>();

        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String matrNm = "";
        Float consistRt = 0F;
        Long matrNo = 0L;
        Long prodNo = 0L;


        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();

//        ????????? ??????
        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex < 1) continue; //???????????? skip
            XSSFRow row = sheet.getRow(rowindex);

            ProdBom bomvo = new ProdBom();
            prodNm = row.getCell(0).getStringCellValue();
            prodNm = prodNm.replaceAll("\\p{Z}", "");

            ProdInfo chkInfo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (chkInfo != null) {
                prodNo = chkInfo.getProdNo();
            }
            if (prodNm.contains("??????") == true) {
                break;
            }
            matrNm = row.getCell(2).getStringCellValue();
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // ???????????? ??????
            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                matrNo = chkvo.getMatrNo();
            }


            consistRt = (float) row.getCell(3).getNumericCellValue();
            bomvo.setConsistRt(consistRt);
            bomvo.setProdNo(prodNo);
            bomvo.setMatrNo(matrNo);
            bomvo.setModDt(DateUtils.getCurrentDate());
            bomvo.setModId(0L);
            bomvo.setRegIp((String) paraMap.get("ipaddr"));
            bomvo.setRegDt(bomvo.getModDt());
            bomvo.setRegId(0L);
            bomvo.setUsedYn("Y");
            bomvo.setCustNo(custNo);

            prodBomRepo.save(bomvo);

        }
    }

    @Override
    public void makeUserByExcel(Map<String, Object> paraMap) throws IOException {
        String tag = "ProdService.makeUserByExcel => ";
        StringBuffer buf = new StringBuffer();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

        for (rowindex = 1; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            log.info(tag + "?????? ?????? = " + rowindex);
            UserInfo uservo = new UserInfo();
            //??????
            DeptInfo deptvo = deptRepo.findByCustNoAndDeptNmAndUsedYn(custNo,row.getCell(0).getStringCellValue(), "Y");
            if (deptvo != null) {
                uservo.setDeptNo(deptvo.getDeptNo());
            }

            //??????
            CodeInfo cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(Long.parseLong(env.getProperty("code.base.user_posn")), row.getCell(1).getStringCellValue(), "Y");
            if (cdvo != null) {
                uservo.setUserPosn(cdvo.getCodeNo());
                ;
            }
            uservo.setUserNm(row.getCell(2).getStringCellValue()); //??????
            try {
                int extNo = (int) row.getCell(3).getNumericCellValue();
                uservo.setExtNo(Integer.toString(extNo)); //????????????
            } catch (NullPointerException ne) {

            } catch (IllegalStateException ie) {
                String extNo = row.getCell(3).getStringCellValue();
                uservo.setExtNo(extNo); //????????????

            }
            try {
                uservo.setCellNo(cmmnService.encryptStr(custNo,row.getCell(4).getStringCellValue())); //????????????(???????????????)
            } catch (NullPointerException ne) {
                uservo.setCellNo(cmmnService.encryptStr(custNo,"010-000-0000"));
            }

            //????????????
            try {
                uservo.setMailAddr(row.getCell(5).getStringCellValue());
            } catch (NullPointerException ne) {
                buf.setLength(0);
                buf.append("ex").append(Integer.toString(rowindex)).append("@").append(env.getProperty("mail.domain"));
                uservo.setMailAddr(buf.toString());
            }
            //????????????
            uservo.setSecrtNo(BCrypt.hashpw(env.getProperty("autoword"), BCrypt.gensalt()));

            //????????????(????????????????????? ???????????? ???????????????: ????????????)
            uservo.setIndsTp(IndsType.valueOf(env.getProperty("industry_type")));
            uservo.setUserTp(UserType.USER);
            uservo.setEmplKind((long) row.getCell(6).getNumericCellValue()); //????????????
            uservo.setOcpnKind((long) row.getCell(7).getNumericCellValue()); //????????????
            uservo.setModDt(DateUtils.getCurrentBaseDateTime());
            uservo.setModIp("localhost");
            uservo.setModId(0L);
            uservo.setUsedYn("Y");
            uservo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
            UserInfo chkvo = userRepo.findByUserNmAndUsedYn(uservo.getUserNm(), "Y");
            if (chkvo != null) {
                uservo.setUserId(chkvo.getUserId());
                uservo.setRegDt(chkvo.getRegDt());
                uservo.setRegDt(chkvo.getRegDt());
                uservo.setRegIp(chkvo.getRegIp());
            } else {
                uservo.setUserId(0L);
                uservo.setRegDt(DateUtils.getCurrentBaseDateTime());
                uservo.setRegIp("localhost");
                uservo.setRegId(0L);
            }
            uservo.setCustInfo(custInfoRepo.findByCustNoAndUsedYn(custNo,"Y"));
            userRepo.save(uservo);
        }

    }

    @Override
    @Transactional
    public void makeDefaultMatrCmpy(Map<String, Object> paraMap) {
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        List<Map<String, Object>> ds = matrMapper.getMatrList(paraMap);
        for (Map<String, Object> el : ds) {
            MatrCmpy vo = new MatrCmpy();
            vo.setMatrNo(Long.parseLong(el.get("matrNo").toString()));
            vo.setCmpyNo(Long.parseLong(env.getProperty("default.purs_cmpy")));
            vo.setUsedYn("Y");
            vo.setDefaultYn("N");
            vo.setModDt(DateUtils.getCurrentBaseDateTime());
            vo.setModId(0L);
            vo.setModIp("localhost");
            MatrCmpy chkvo = matrCmpyRepo.findByCustNoAndMatrNoAndCmpyNoAndUsedYn(custNo,vo.getMatrNo(), vo.getCmpyNo(), "Y");
            if (chkvo != null) {
                vo.setMatrCmpyNo(chkvo.getMatrCmpyNo());
                vo.setRegDt(chkvo.getRegDt());
                vo.setRegId(chkvo.getRegId());
                vo.setRegIp(chkvo.getRegIp());
            } else {
                vo.setMatrCmpyNo(0L);
                vo.setRegDt(DateUtils.getCurrentBaseDateTime());
                vo.setRegId(0L);
                vo.setRegIp("localhost");
            }
            vo.setCustNo(custNo);
            matrCmpyRepo.save(vo);
        }
    }

    @Override
    public List<Map<String, Object>> indcExcelList(Map<String, Object> paraMap) {
        List<Map<String, Object>> ds = matrMapper.getindcExcelList(paraMap);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> prodMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<>();
        String svproc = "";
        Float sumMakeQty = 0F;
        Long sumbefQty = 0L;
        Float sumIwhQty = 0f;
        Long sumOwhQty = 0L;
        Float sumStkQty = 0F;

        Float makeQty = 0F;
        Long befQty = 0L;
        Float iwhQty = 0f;
        Long owhQty = 0L;
        Float stkQty = 0F;


        if (ds.size() > 0) {
            prodMap = (Map<String, Object>) ds.get(0);
            svproc = prodMap.get("codeNm").toString();
        }
        for (Map<String, Object> el : ds) {

            prodMap = new HashMap<String, Object>();
            if (!el.get("codeNm").toString().equals(svproc)) {
                prodMap.put("prodSum", "??????");
                sumMakeQty += Float.parseFloat(el.get("makeQty").toString());
                sumbefQty += Long.parseLong(el.get("befStkQty").toString());
                sumIwhQty += Float.parseFloat(el.get("iwhQty").toString());
                sumOwhQty += Long.parseLong(el.get("owhQty").toString());
                sumStkQty += Float.parseFloat(el.get("stkQty").toString());

                prodMap.put("sumQty",sumMakeQty);// ?????? ????????? ???
                prodMap.put("sumBefQty",sumbefQty);//???????????? ???
                prodMap.put("sumIwhQty",sumOwhQty);//???????????? ???
                prodMap.put("sumOwhQty",sumOwhQty);//???????????? ???
                prodMap.put("sumStkQty",sumStkQty);//?????????

            } else{
                prodMap.put("prodSum", "??????");
                makeQty += Float.parseFloat(el.get("makeQty").toString());
                befQty += Long.parseLong(el.get("befStkQty").toString());
                owhQty += Long.parseLong(el.get("owhQty").toString());
                iwhQty += Float.parseFloat(el.get("iwhQty").toString());
                stkQty += Float.parseFloat(el.get("stkQty").toString());
                prodMap.put("sumQty",makeQty);// ?????? ????????? ???
                prodMap.put("befQty",befQty);//???????????? ???
                prodMap.put("owhQty",owhQty);//???????????? ???
                prodMap.put("iwhQty",iwhQty);//???????????? ???
                prodMap.put("stkQty",stkQty);//?????????
            }
                prodMap.put("codeNm",el.get("codeNm"));// ?????? ??????erpUserNo
                prodMap.put("prodNm",el.get("prodNm"));// ?????????
                prodMap.put("qtyPerQkg",el.get("qtyPerQkg"));//??????
                prodMap.put("makeQty",el.get("makeQty"));//???????????????
                prodMap.put("befStkQty",el.get("befStkQty").toString());//????????????
                prodMap.put("iwhQty",el.get("iwhQty").toString());//????????????
                prodMap.put("owhQty",el.get("owhQty").toString());//????????????
                prodMap.put("stkQty",el.get("stkQty").toString());//?????????
                prodMap.put("time", el.get("validTerm").toString()); //????????????
            list.add(prodMap);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> makeIndcExcelList(Map<String, Object> paraMap) {
        return  matrMapper.getmakeIndcExcelList(paraMap);
    }



    //??????????????? BOM Mapper??? ProdNo ????????? BOM ????????????
    @Override
    public void prodCodeBomExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodCodeBomExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        Map<String, Object> Map = new HashMap<String, Object>();

        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String erpProdNm = "";
        String matrNm = "";
        Float consistRt = 0F;
        Long matrNo = 0L;



        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();

//        ????????? ??????
        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex < 1) continue; //???????????? skip
            XSSFRow row = sheet.getRow(rowindex);


            erpProdNm = row.getCell(0).getStringCellValue();
            erpProdNm = erpProdNm.replaceAll("\\p{Z}", "");
            if (erpProdNm.contains("??????") == true) {
                break;
            }

            matrNm = row.getCell(2).getStringCellValue();
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // ???????????? ??????

            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                matrNo = chkvo.getMatrNo();
            }
            consistRt = (float) row.getCell(5).getNumericCellValue();

            List<Map<String, Object>> ds = mapper.getProdNo(erpProdNm);
            for (Map<String, Object> el : ds) {
                ProdBom bomvo = new ProdBom();
                Long prodNo = Long.parseLong(el.get("prod_no").toString());

                if (matrNm.contains("?????????") == true) { //???????????? ??? == ?????? ??????.
                    bomvo.setPursYn("N");
                }else {
                    bomvo.setPursYn("Y");
                }

                bomvo.setConsistRt(consistRt);
                bomvo.setProdNo(prodNo);
                bomvo.setMatrNo(matrNo);
                bomvo.setModDt(DateUtils.getCurrentDate());
                bomvo.setModId(0L);
                bomvo.setRegIp((String) paraMap.get("ipaddr"));
                bomvo.setRegDt(bomvo.getModDt());
                bomvo.setRegId(0L);
                bomvo.setUsedYn("Y");
                bomvo.setCustNo(custNo);
                prodBomRepo.save(bomvo);
            }
        }
    }

    @Override
    public void prodInfoExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.prodIndcExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        String prodNm = "";
        String erProdNm = "";
        String exData = "";
        String prodcd = "";
        Float vol = 0F;
        Float spga = 0F;



        XSSFSheet sheet = workbook.getSheetAt(0); //???????????????
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "?????? excel?????? ?????? = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

        for (rowindex = 0; rowindex <= rows; rowindex++) {
//            if (rowindex < 1) continue; //???????????? skip
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try {
                prodNm = row.getCell(2).getStringCellValue();
                prodNm = prodNm.replaceAll("\\p{Z}", "");
                erProdNm = row.getCell(0).getStringCellValue();
                erProdNm = erProdNm.replaceAll("\\p{Z}", "");
                if (prodNm.contains("??????") == true) {
                    break;
                }

            } catch (NullPointerException ne) {
                log.info(tag + "??????(" + rowindex + ") : ????????? ??????.... skip....");
                continue;
            }
            ProdInfo prodInfo = new ProdInfo();
//            prodcd = row.getCell(1).getStringCellValue(); // ????????????
            prodInfo.setProdCode(prodcd);
            prodInfo.setProdNm(prodNm);
            prodInfo.setErpProdNm(erProdNm);
            double qtypkg = row.getCell(7).getNumericCellValue();
            prodInfo.setQtyPerPkg((int)qtypkg); //??????????????????(???????????????)
            prodInfo.setFileNo(0L);//???????????????(???????????????)
            prodInfo.setUsedYn("Y");//????????????(???????????????)
            prodInfo.setProdBrnch(1L); //??????(???????????????)
            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //????????????
            prodInfo.setBrnchNo((long)row.getCell(1).getNumericCellValue()); // ????????? ??????
            spga = (float)row.getCell(3).getNumericCellValue();
            prodInfo.setSpga(spga); // ?????? ??????
            prodInfo.setHeatTp(0L);

            vol = (float) row.getCell(5).getNumericCellValue();

            exData = row.getCell(6).getStringCellValue();
            Long prodShape = Long.parseLong(env.getProperty("code.base.saleunit"));
            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(prodShape, exData.replace(" ", ""), "Y");
            if (mgvo != null) {
                Long prodUnit = mgvo.getCodeNo();
                prodInfo.setSaleUnit(prodUnit); // ??????

                if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))){ // KG??? G?????? ??????
                    prodInfo.setVol(vol);
                    prodInfo.setMess(vol*spga);
                }else {
                    prodInfo.setVol(vol/1000);
                    prodInfo.setMess(vol*spga);
                }if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_ml"))){
                    prodInfo.setMngrUnit(Long.parseLong(env.getProperty("code.base.mngrbase_vol")));
                }else {
                    prodInfo.setMngrUnit(Long.parseLong(env.getProperty("code.base.mngrbase_imp")));
                }

            }

//            ProdInfo chkInfo = prodRepo.findByProdNmAndUsedYn(prodNm, "Y");
//            if(chkInfo != null ){
//                prodInfo.setProdNo(chkInfo.getProdNo());
//                prodInfo.setRegIp(chkInfo.getRegIp());
//                prodInfo.setRegId(chkInfo.getRegId());
//                prodInfo.setRegDt(chkInfo.getRegDt());
//                prodInfo.setProdNm(chkInfo.getProdNm());
//            }else{
//                prodInfo.setProdNm(prodNm);
//                prodInfo.setModIp("127.0.0.1");
//                prodInfo.setModId(2L);
//                prodInfo.setModDt(DateUtils.getCurrentDate());
//            }
            prodInfo.setCustNo(custNo);
            prodRepo.save(prodInfo);
        }
    }
}