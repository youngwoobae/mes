package daedan.mes.imp.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.mapper.CodeMapper;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.dept.domain.DeptInfo;
import daedan.mes.dept.repository.DeptRepository;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.file.service.FileService;
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
import daedan.mes.prod.domain.ProdBom;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.mapper.ProdMapper;
import daedan.mes.prod.repository.ProdBomRepository;
import daedan.mes.prod.repository.ProdBrnchRepository;
import daedan.mes.prod.repository.ProdRepository;
import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;
import daedan.mes.purs.repository.PursInfoRepository;
import daedan.mes.purs.repository.PursMatrRepository;
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
public class ImpServiceImpl implements ImpService {
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
    private MatrCmpyRepository matrCmpyRepo;

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
            log.info(tag + "excel처리행수 = " + rows);
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //헤더정보 skip

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

        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            log.info(tag + "excel처리행수 = " + rows);
            String exData = "";
            int idx = 0;
            String cmpyNm = "";

            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //헤더정보 skip
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info("process.rowindex = " + rowindex);

                CmpyInfo vo = new CmpyInfo();

                try{
                    Double cmpyNo = row.getCell(0).getNumericCellValue();
                    String sz = Double.toString(cmpyNo);
                    vo.setCmpyNo(Long.parseLong(sz.substring(0,sz.indexOf("."))));
                }catch(NullPointerException ne){
                    vo.setCmpyNo(0L);
                }
                catch (IllegalStateException ie) {
                    vo.setCmpyNo(0L);
                }
                try {
                    cmpyNm = row.getCell(1).getStringCellValue(); // 회사 명
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

                try{
                    vo.setCmanNm(row.getCell(2).getStringCellValue()); //대표자명
                } catch (NullPointerException en){
                }

                try{
                    vo.setSaupNo(String.valueOf(row.getCell(3).getStringCellValue())); //사업자번호
                }catch (NullPointerException en){

                }
                catch (IllegalStateException ie) {

                }
                try{
                    vo.setTelNo(row.getCell(4).getStringCellValue());//대표 전화
                }catch (IllegalStateException en) {

                }catch (NullPointerException e){

                }

                try{
                    vo.setFaxNo(row.getCell(5).getStringCellValue());//대표 Fox
                }catch (IllegalStateException en) {

                }catch (NullPointerException e){

                }

                try{
                    vo.setCmanCellNo(row.getCell(6).getStringCellValue()); //대표자이동전화

                }catch (NullPointerException en){

                }


//                exData = row.getCell(6).getStringCellValue(); // 매입/매출/기타
//                Long lnMngrGbnCd = Long.parseLong(env.getProperty("code.base.cmpytp"));
//                CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(lnMngrGbnCd ,exData.replace(" ",""),"Y");
//                if(mgvo != null){
//                    vo.setMngrGbnCd(mgvo.getCodeNo()); //매입 매출
//                }

                try{
                    vo.setReprMailAddr(row.getCell(8).getStringCellValue());//이메일
                }catch (NullPointerException en){
                }


//                try{
//                    vo.setErpCd(String.valueOf(row.getCell(3).getStringCellValue())); // ERP코드
//                }catch (NullPointerException en){
//                }

//                try{
//                    vo.setAddr(row.getCell(7).getStringCellValue()); // 주소
//                }catch (NullPointerException en){
//                }

                vo.setCmpyTp(Long.parseLong(env.getProperty("code.mngrgb.cmpy"))); //법인,개인
                vo.setMngrGbnCd(Long.parseLong(paraMap.get("mngrGbnCd").toString())); //매입, 매출구분
                vo.setUsedYn("Y");
                vo.setCustNo(custNo);

                CmpyInfo chkvo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNoAndUsedYn(custNo,vo.getCmpyTp(),vo.getCmpyNo(), "Y");
                if (chkvo != null) {
                    vo.setCmpyNo(chkvo.getCmpyNo());
                    vo.setCmpyNm(chkvo.getCmpyNm());
                    vo.setModIp("127.0.0.1");
                    vo.setModId(3L);
                    vo.setModDt(DateUtils.getCurrentDate());
                } else {
                    vo.setCmpyNo(0L);
                    vo.setCmpyNm(cmpyNm);
                    vo.setRegIp("127.0.0.1");
                    vo.setRegId(3L);
                    vo.setRegDt(DateUtils.getCurrentDate());
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
        String tag = "ImptoolService.makeMatrByExcel => ";
        String fileRoot = paraMap.get("fileRoot").toString();
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);

//        StringBuffer buf = new StringBuffer();
//
//        String fileRoot = env.getProperty("file.root.path");
//        buf.setLength(0);
//        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
//        String absFilePath = buf.toString();

        FileInputStream file = null;
        String exData = "";
        String svData = "";
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0); //시트
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //헤더정보 skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel처리행수 = " + rows + " / " + rowindex);

                try {
                    MatrInfo matrvo = new MatrInfo(); // 원자재

                    matrvo.setItemCd(row.getCell(0).getStringCellValue()); // 자재 번호
                    matrvo.setMatrNm(row.getCell(1).getStringCellValue()); //자재명(원자재)
                    matrvo.setSz(row.getCell(2).getStringCellValue()); //규격
                    matrvo.setBrnchNo(1L);
                    matrvo.setUsedYn("Y");
                    matrvo.setRegId(0L);
                    matrvo.setRegIp(paraMap.get("ipaddr").toString());
                    matrvo.setRegDt(DateUtils.getCurrentDate());
                    matrvo.setModDt(DateUtils.getCurrentDate());
                    matrvo.setModIp(paraMap.get("ipaddr").toString());
                    matrvo.setModId(0L);
                    matrvo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); // 자재(원자재) 고정
//                    matrvo.setPursUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); // 단위(Kg) 고정
                    matrvo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.cold"))); // 보관온도(상온) 고정
                    matrvo.setValidTerm(12); //유효기간-12개월

                    MatrInfo chkvo = matrRepo.findByCustNoAndMatrNoAndMatrNmAndUsedYn(custNo,matrvo.getMatrNo(), matrvo.getMatrNm(), "Y");
                    if (chkvo != null) {
                        matrvo.setMatrNm(matrvo.getMatrNm());
                    }
//                  exData = row.getCell(6).getStringCellValue();
//                  Long lnMatrTp = Long.parseLong(env.getProperty("base.matr_type_cd"));
//                  CodeInfo cdvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(lnMatrTp ,exData.replace(" ",""),"Y");
//                  if(cdvo != null){
//                      matrvo.setMatrTp(cdvo.getCodeNo()); //자재구분(원육,시즈닝,원자재,부자재)
//                  }
                    svData = row.getCell(3).getStringCellValue();
                    Long lnUnit = Long.parseLong(env.getProperty("code.base.unit"));
                    CodeInfo unvo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,lnUnit, svData.replace(" ", ""), "Y");
                    if (unvo != null) {
                        matrvo.setPursUnit(unvo.getCodeNo()); //단위(KG, EA 등)
                    }
                    matrvo.setCustNo(custNo);
                    matrRepo.save(matrvo);

                } catch (NullPointerException ne) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*쟈재명 , 쟈재구분 두개 필드만 가지고 자재생성시 사용
     * 자재발주처는 사용자의 소속사로 자동설정함.
     * 생성자재의 보관온도는 냉장으로 자동 서함.*/
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
        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
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
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거
            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                matrNo = chkvo.getMatrNo();
            }
            Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.purs"));
            CmpyInfo chkInfo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,cmpy, "Y");
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
                if (rowindex < 1) continue; //헤더정보 skip
                //if (rowindex > 3) break; //테스트
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel처리행수 = " + rows + " / " + rowindex);

                //구매기본정보설정
                ivo = new PursInfo();
                ivo.setPursDt(tdf.parse(row.getCell(1).getStringCellValue())); //구매일자
                ivo.setDlvReqDt(tdf.parse(row.getCell(1).getStringCellValue())); //납품요청일자
                ivo.setDlvDt(tdf.parse(row.getCell(1).getStringCellValue())); //납품일자
                ivo.setPursSts(Long.parseLong(env.getProperty("purs.sts.complete"))); //구매상태(완료)
                ivo.setIndcNo(0L);//생산지시번호(기본값설정)
                ivo.setOrdNo(0L); //주문번호(기본값설정)
                ivo.setUsedYn("Y");//사용구분(기본값설정)
                ivo.setCustNo(custNo);
                ivo.setRegDt(DateUtils.getCurrentDate());
                ivo.setRegId(0L);
                ivo.setRegIp(paraMap.get("ipaddr").toString());
                ivo.setModDt(DateUtils.getCurrentDate());
                ivo.setModId(0L);
                ivo.setModIp(paraMap.get("ipaddr").toString());
                cmpyvo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,20L, row.getCell(2).getStringCellValue(), "Y");
                if (cmpyvo == null) {
                    log.error(tag + "구매거래처 검색 실패 LineNo = " + rowindex);
                    continue;
                }
                //ivo.setCmpyNo(cmpyvo.getCmpyNo()); //구매처번호


                PursInfo ckivo = pursInfoRepo.findByCustNoAndPursDtAndDlvReqDtAndDlvDtAndUsedYn(custNo,ivo.getPursDt(), ivo.getDlvReqDt(), ivo.getDlvDt(), "Y");
                if (ckivo != null) {
                    ivo.setPursNo(ckivo.getPursNo());
                }
                ckivo = pursInfoRepo.save(ivo);

                //구매자재정보설정
                mvo = new PursMatr();
                mvo.setPursNo(ckivo.getPursNo());
                String itemCd = row.getCell(3).getStringCellValue();
                String matrNm = row.getCell(4).getStringCellValue();
                matrvo = matrRepo.findByCustNoAndItemCdAndMatrNmAndUsedYn(custNo,itemCd, matrNm, "Y");
                if (matrvo == null) {
                    log.error(tag + "구매자재 번호 검색 실패 LineNo = " + rowindex);
                    continue;
                }
                mvo.setMatrNo(matrvo.getMatrNo()); //자재번호

                //구매단위추출
                codevo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,Long.parseLong(env.getProperty("code.base.unit")), row.getCell(12).getStringCellValue(), "Y");
                if (codevo == null) {
                    log.error(tag + "구매단위코드 검색 실패 LineNo = " + rowindex);
                    continue;
                }
                mvo.setPursQty((float) row.getCell(11).getNumericCellValue()); //구매수량
                mvo.setPursUnit(codevo.getCodeNo());
                mvo.setPursAmt(0L);//구매단가(기본값설정)
                mvo.setUsedYn("Y");//사용구분(기본값설정)
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
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            String exData = "";
            XSSFSheet sheet = workbook.getSheetAt(0); //시트
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //헤더정보 skip
                XSSFRow row = sheet.getRow(rowindex);

                ProdInfo prodvo = new ProdInfo();
                prodvo.setProdNm(row.getCell(0).getStringCellValue());


//                exData = row.getCell(0).getStringCellValue();
//                ProdInfo chkvo = prodRepo.findByProdNmAndProdNoAndUsedYn(exData.replace(" ",""),prodvo.getProdNo(),"Y");
//                if (chkvo != null) {
//                    prodvo.setProdNm(chkvo.getProdNm());
//                    prodvo.setProdNo(chkvo.getProdNo());
//                }else {continue;}
                //상품정보생성
                prodvo.setUnitAmt(0);//포장단위금액(기본값설정)
                prodvo.setQtyPerPkg(1); //세트구성수량(기본값설정)
                prodvo.setFileNo(0L);//상품이미지(기본값설정)
                prodvo.setUsedYn("Y");//사용구분(기본값설정)
                prodvo.setProdBrnch(1L); //분류(기본값설정)
                prodvo.setSaleUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); // 단위(Kg) 고정
                prodvo.setRegDt(DateUtils.getCurrentDate());
                prodvo.setRegId(0L);
                prodvo.setRegIp(paraMap.get("ipaddr").toString());
                prodvo.setModDt(DateUtils.getCurrentDate());
                prodvo.setModIp(paraMap.get("ipaddr").toString());
                prodvo.setModId(0L);
                prodvo.setCustNo(custNo);
                prodRepo.save(prodvo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void makeProdBomByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeProdBomByExcel => ";
        StringBuffer buf = new StringBuffer();
         Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String fileRoot = paraMap.get("fileRoot").toString();
        buf.setLength(0);
        buf.append(fileRoot).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = null;
        try {
            file = new FileInputStream(buf.toString());
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            HttpSession session = (HttpSession) paraMap.get("session");
            session.setAttribute("totCount", rows);
            log.info(tag + "excel 전체처리 행수 = " + session.getAttribute("totCount"));

            Long svProdNo = 0L;

            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex == 0) continue; //헤더정보 skip
                //if (rowindex > 100) break; //테스트
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel처리행 = " + rowindex + " / " + rows);
                //상품정보생성

                ProdInfo prodvo = new ProdInfo(); //상품정보

                prodvo.setProdNm(row.getCell(0).getStringCellValue()); //상품명
                prodvo.setProdBrnch(1L);
                ProdInfo chkvo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo, prodvo.getProdNm(), "Y");
                if (chkvo != null) {
                    svProdNo = chkvo.getProdNo(); //상품번호 저장 (BOM 생성시 재사용)
                    prodvo.setProdBrnch(1L);
                    log.info(tag + "상품처리번호 =======> " + svProdNo);
                }

//                    try {
//                        ProdBrnch pbvo = prodBrnchRepo.findByParBrnchNoAndBrnchNmAndUsedYn(Long.valueOf(env.getProperty("prod_start_brnch")), row.getCell(0).getStringCellValue(), "Y");
//                        if (pbvo != null) {
//                            prodvo.setProdBrnch(pbvo.getBrnchNo());//상품분류
//                        } else {
//                            prodvo.setProdBrnch(1L);
//                        }
//                    } catch (NullPointerException ne) {
//                        prodvo.setProdBrnch(1L);
//                    }

                //상품별 BOM 생성

                MatrInfo matrvo = new MatrInfo();
                ProdBom bomvo = new ProdBom(); //상품별bom정보
                bomvo.setProdNo(svProdNo);

                //자재명으로 자재번호 추출
                matrvo.setMatrNm(row.getCell(1).getStringCellValue()); //자재명
                MatrInfo matrchkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrvo.getMatrNm(), "Y");
                if (matrchkvo != null) {
                    bomvo.setMatrNo(matrchkvo.getMatrNo());
                } else {
                    log.error("자재번호검출실패.lineNo = " + rowindex);
                    continue;
                }
                bomvo.setConsistRt((float) row.getCell(2).getNumericCellValue()); // 구성비율 입력
                bomvo.setModId(0L);
                bomvo.setModIp("127.0.0.1");
                bomvo.setModDt(DateUtils.getCurrentDateTime());
                bomvo.setUsedYn("Y");

                ProdBom bomchkvo = prodBomRepo.findByCustNoAndProdNoAndMatrNoAndUsedYn(custNo,bomvo.getProdNo(), bomvo.getMatrNo(), "Y");
                if (bomchkvo != null) {
                    bomvo.setBomNo(bomchkvo.getBomNo());

                } else {
                    bomvo.setRegId(0L);
                    bomvo.setModIp("127.0.0.1");
                    bomvo.setRegDt(DateUtils.getCurrentDate());
                }
                bomvo.setCustNo(custNo);
                prodBomRepo.save(bomvo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeOrdByExcel(Map<String, Object> paraMap) {
        String tag = "ImptoolService.makeOrdByExcel => ";
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
                if (rowindex < 1) continue; //헤더정보 skip
                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel처리행수 = " + rows + " / " + rowindex);

                //주문정보생성
                ovo = new OrdInfo();
                buf.setLength(0);
                buf.append(row.getCell(1).getStringCellValue())
                        .append("_")
                        .append(row.getCell(2).getStringCellValue())
                        .append("_주문");
                ovo.setOrdNm(buf.toString());
                OrdInfo ckvo = ordRepo.findByCustNoAndOrdNmAndUsedYn(custNo,ovo.getOrdNm(), "Y");
                if (ckvo != null) { //기등록주문인경우 skip
                    //ovo.setOrdNo(ckvo.getOrdNo());
                    continue;
                }

                ovo.setOrdTp(Long.parseLong(env.getProperty("code.ordtype.project"))); //주문유형-프로젝
                ovo.setOrdSts(Long.parseLong(env.getProperty("ord_stat.complete"))); //주문상태-고객인도
                ovo.setOrdDt(tdf.parse(row.getCell(1).getStringCellValue()));//주문일
                ovo.setDlvReqDt(ovo.getOrdDt());//납품요청일자
                ovo.setDlvDt(ovo.getOrdDt()); //납품일
                ovo.setUsedYn("Y");


                //주문거래처번호 추출
                CmpyInfo cmpyvo = new CmpyInfo();
                cmpyvo.setCmpyNm(row.getCell(2).getStringCellValue());
                cmpyvo.setCmpyTp(21L); //매입처
                CmpyInfo ckcmpyvo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyvo.getCmpyTp(), cmpyvo.getCmpyNm(), "Y");
                if (ckcmpyvo == null) {
                    log.info(tag + "주문거래처번호추출실패 : 처리행번 = " + rowindex);
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
                if (rowindex < 1) continue; //헤더정보 skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                int procRt = (rowindex / rows) * 100;
                log.info(tag + "excel처리율 ================>>>>>> " + rows + " / " + procRt);
                pvo = new OrdProd();

                //주문번호 추출
                buf.setLength(0);
                buf.append(row.getCell(1).getStringCellValue())
                        .append("_")
                        .append(row.getCell(2).getStringCellValue())
                        .append("_주문");
                ivo = ordRepo.findByCustNoAndOrdNmAndUsedYn(custNo,buf.toString(), "Y");
                if (ivo == null) {
                    log.info(tag + "주문번호추출오류발생 행번호 = " + rowindex);
                    continue;
                }
                pvo.setOrdNo(ivo.getOrdNo());//주문번호
                //상품코드추출
                prodvo = prodRepo.findByCustNoAndProdCodeAndProdNmAndUsedYn(custNo,row.getCell(1).getStringCellValue(), row.getCell(4).getStringCellValue(), "Y");
                if (prodvo == null) {
                    log.error(tag + "상품코드 검색 실패 LineNo = " + rowindex);
                    continue;
                }
                pvo.setProdNo(prodvo.getProdNo());

                pvo.setOrdSz(prodvo.getSz() == null ? prodvo.getProdCode() : prodvo.getSz());

                //포장단위추출
                codevo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,Long.parseLong(env.getProperty("code.base.saleunit")), row.getCell(6).getStringCellValue(), "Y");
                if (codevo == null) {
                    log.error(tag + "상품포장단위코드 검색 실패 LineNo = " + rowindex);
                    continue;
                }
                pvo.setSaleUnit(codevo.getCodeNo()); //포장단위코드

                pvo.setOrdQty(Float.parseFloat(String.valueOf(row.getCell(5).getNumericCellValue())));
                pvo.setQtyPerPkg(1); //포장당수량(기본값설정)
                pvo.setUsedYn("Y");//사용구분(기본값설정)
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

    // 대동고려삼 Excel 상품 등록
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

        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "전체 excel처리 행수 = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

//        원자재 등록
        for (rowindex = 0; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try{
                matrNm = row.getCell(0).getStringCellValue();
                matrNm = matrNm.replaceAll("\\p{Z}", "");
                if (prodNm.contains("합계") == true) {
                    break;
                }
            }  catch (NullPointerException ne) {
                log.info(tag + "행번(" + rowindex + ") : 자재명 없음.... skip....");
                continue;
            }

//            MatrInfo matrInfo = new MatrInfo();
////          prodcd = row.getCell(0).getStringCellValue(); // 코드번호
////          matrInfo.setItemCd(prodcd);
//            matrInfo.setMatrNm(matrNm);
//            matrInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //보관온도
//            matrInfo.setValidTerm(12); //유효기간-12개월
//            matrInfo.setPursUnit(Long.parseLong(env.getProperty("code.purs_unit.kg"))); //구매단위(KG)
//            matrInfo.setMatrTp(Long.parseLong(env.getProperty("code.matrtp.matr"))); // 타입 / 원자재,부자재
//            matrInfo.setBrnchNo(1L);
//            matrInfo.setUsedYn("Y");
//            matrInfo.setVol(0F);
//            matrInfo.setValidTerm(12);
//            matrInfo.setVol(1000F);
//            matrInfo.setMess(1000F);
//            matrInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_mess")));
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
            Long cmpyTp = Long.parseLong(env.getProperty("code.cmpytp.purs"));//매입거래처
            CmpyInfo cmpyvo = cmpyRepo.findByCustNoAndCmpyTpAndCmpyNmAndUsedYn(custNo,cmpyTp,"구매처미상","Y");

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

//            상품 등록
//
//            try {
//                prodNm = row.getCell(1).getStringCellValue();
//                prodNm = prodNm.replaceAll("\\p{Z}", "");
//                if (prodNm.contains("합계") == true) {
//                    break;
//                }
//            } catch (NullPointerException ne) {
//                log.info(tag + "행번(" + rowindex + ") : 제품명 없음.... skip....");
//                continue;
//            }
//            ProdInfo prodInfo = new ProdInfo();
//            prodcd = row.getCell(0).getStringCellValue(); // 코드번호
//            if(prodcd == null){continue;}
//            prodInfo.setProdCode(prodcd);
//            prodInfo.setErpProdNm(prodNm);
//            double qtypkg = row.getCell(6).getNumericCellValue();
//            prodInfo.setQtyPerPkg((int)qtypkg); //세트구성수량(기본값설정)
//            prodInfo.setFileNo(0L);//상품이미지(기본값설정)
//            prodInfo.setUsedYn("Y");//사용구분(기본값설정)
//            prodInfo.setProdBrnch(1L); //분류(기본값설정)
//            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //보관온도
//            prodInfo.setBrnchNo(1251L); // 공정별 분류
//            prodInfo.setBrix(0F);
//            prodInfo.setSpga(1F); // 비중 비율
//            prodInfo.setHeatTp(0L);
//
//            Float vol = (float) row.getCell(4).getNumericCellValue();
//            exData = row.getCell(5).getStringCellValue();
//            Long prodShape = Long.parseLong(env.getProperty("code.base.saleunit"));
//            CodeInfo mgvo = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(prodShape, exData.replace(" ", ""), "Y");
//            if (mgvo != null) {
//                Long prodUnit = mgvo.getCodeNo();
//                prodInfo.setSaleUnit(prodUnit); // 단위
//
//                if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))){ // KG을 G으로 환산
//                    prodInfo.setVol(vol*1000);
//                    prodInfo.setMess(vol*1000);
//                }else {
//                    prodInfo.setVol(vol);
//                    prodInfo.setMess(vol);
//                }if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_ml"))){
//                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_mess")));
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
        String tag = "ProdService.prodIndcExcel => ";
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
        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();

//        원자재 등록
        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex < 1) continue; //헤더정보 skip
            XSSFRow row = sheet.getRow(rowindex);

            ProdBom bomvo = new ProdBom();
            prodNm = row.getCell(0).getStringCellValue();
            prodNm = prodNm.replaceAll("\\p{Z}", "");

            ProdInfo chkInfo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (chkInfo != null) {
                prodNo = chkInfo.getProdNo();
            }
            if (prodNm.contains("합계") == true) {
                break;
            }
            matrNm = row.getCell(2).getStringCellValue();
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거
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
    public void prodExcel(Map<String, Object> paraMap) throws Exception {

        String tag = "ImptoolService.prodExcel => ";
        String fileRoot = paraMap.get("fileRoot").toString();;
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        String filePath = fileService.getFileInfo(fileRoot,fileNo);
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        FileInputStream file = null;
        String svData = "";
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            int rowindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0); //시트
            int rows = sheet.getPhysicalNumberOfRows();
            String prodNm = "";
            for (rowindex = 0; rowindex < rows; rowindex++) {
                if (rowindex < 1) continue; //헤더정보 skip

                XSSFRow row = sheet.getRow(rowindex);
                if (row == null) continue;
                log.info(tag + "excel처리행수 = " + rows + " / " + rowindex);

                try {
                    ProdInfo prodvo = new ProdInfo();

                    Long prodNo = (long)row.getCell(0).getNumericCellValue(); //제품번호

                    prodNm = row.getCell(1).getStringCellValue(); //자재명
                    prodNm = prodNm.replaceAll("\\p{Z}", "");

                    prodvo.setProdCode(row.getCell(3).getStringCellValue()); //자재코드
                    prodvo.setBrnchNo(1L);
                    prodvo.setSaveTmpr(12L);
                    prodvo.setQtyPerPkg((int)row.getCell(7).getNumericCellValue());
                    prodvo.setProdNm(prodNm);
                    prodvo.setErpProdNm(prodNm);
                    prodvo.setUsedYn("Y");
                    prodvo.setHeatTp(0L);
                    prodvo.setFileNo(Long.valueOf(fileNo));
                    log.info(prodvo + "prodvo");
                    Float spga = (float)row.getCell(8).getNumericCellValue();
                    Float mess = (float)row.getCell(5).getNumericCellValue();
                    prodvo.setSpga(spga);

                    if(Long.parseLong(paraMap.get("cust_no").toString()) == 6 ){
                        prodvo.setVol((float) ((mess/spga )* 0.001));
                        prodvo.setMess(mess);
                    }else{
                        prodvo.setVol(mess);
                        prodvo.setMess(mess);
                    }

                    svData = row.getCell(6).getStringCellValue();
                    Long lnUnit = Long.parseLong(env.getProperty("code.base.saleunit"));
                    CodeInfo unvo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,lnUnit, svData.replace(" ", ""), "Y");
                    if (unvo != null) {
                        prodvo.setSaleUnit(unvo.getCodeNo()); //단위(KG, EA 등)
                    } else {
                        prodvo.setSaleUnit(Long.parseLong(env.getProperty("code.saleunit_default")));
                    }

                    ProdInfo prvo = prodRepo.findByCustNoAndProdNoAndUsedYn(custNo,prodNo, "Y");
                    if (prvo != null) {
                        prodvo.setProdNo(prvo.getProdNo());
                        prodvo.setModIp((String) paraMap.get("ipaddr"));
                        prodvo.setModDt(DateUtils.getCurrentDateTime());
                        prodvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    } else {
                        prodvo.setProdNo(0L);
                        prodvo.setRegIp((String) paraMap.get("ipaddr"));
                        prodvo.setRegDt(DateUtils.getCurrentDateTime());
                        prodvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    }
                    prodvo.setCustNo(custNo);
                    prodRepo.save(prodvo);

                } catch (NullPointerException ne) {
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
    public void makeUserByExcel(Map<String, Object> paraMap) throws IOException {
        String tag = "ProdService.makeUserByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));
        String absFilePath = buf.toString();
        log.info(tag + " absFilePath = " + absFilePath);
        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "전체 excel처리 행수 = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

        for (rowindex = 1; rowindex <= rows; rowindex++) {
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            log.info(tag + "처리 행번 = " + rowindex);
            UserInfo uservo = new UserInfo();
            //소속
            DeptInfo deptvo = deptRepo.findByCustNoAndDeptNmAndUsedYn(custNo,row.getCell(0).getStringCellValue(), "Y");
            if (deptvo != null) {
                uservo.setDeptNo(deptvo.getDeptNo());
            }

            //직위
            CodeInfo cdvo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,Long.parseLong(env.getProperty("code.base.user_posn")), row.getCell(1).getStringCellValue(), "Y");
            if (cdvo != null) {
                uservo.setUserPosn(cdvo.getCodeNo());
                ;
            }
            uservo.setUserNm(row.getCell(2).getStringCellValue()); //성명
            try {
                int extNo = (int) row.getCell(3).getNumericCellValue();
                uservo.setExtNo(Integer.toString(extNo)); //내선번호
            } catch (NullPointerException ne) {

            } catch (IllegalStateException ie) {
                String extNo = row.getCell(3).getStringCellValue();
                uservo.setExtNo(extNo); //내선번호

            }
            try {
                uservo.setCellNo(cmmnService.encryptStr(custNo,row.getCell(4).getStringCellValue())); //이동전화(암호화사용)
            } catch (NullPointerException ne) {
                uservo.setCellNo(cmmnService.encryptStr(custNo,"010-000-0000"));
            }

            //메일주소
            try {
                uservo.setMailAddr(row.getCell(5).getStringCellValue());
            } catch (NullPointerException ne) {
                buf.setLength(0);
                buf.append("ex").append(Integer.toString(rowindex)).append("@").append(env.getProperty("mail.domain"));
                uservo.setMailAddr(buf.toString());
            }
            //비밀번호
            uservo.setSecrtNo(BCrypt.hashpw(env.getProperty("autoword"), BCrypt.gensalt()));

            //산업구분(산업구분에따라 표시되는 필드조정됨: 매우중요)
            uservo.setIndsTp(IndsType.valueOf(env.getProperty("industry_type")));
            uservo.setUserTp(UserType.USER);
            uservo.setEmplKind((long) row.getCell(6).getNumericCellValue()); //채용구분
            uservo.setOcpnKind((long) row.getCell(7).getNumericCellValue()); //직종구분
            uservo.setModDt(DateUtils.getCurrentBaseDateTime());
            uservo.setModIp("localhost");
            uservo.setModId(0L);
            uservo.setUsedYn("Y");
            uservo.setCustInfo(custInfoRepo.findByCustNo(Long.parseLong(env.getProperty("cust_no"))));
            UserInfo chkvo = userRepo.findByDeptNoAndUserNmAndUsedYn(uservo.getDeptNo(), uservo.getUserNm(), "Y");
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
            uservo.setCustInfo(custInfoRepo.findByCustNo(custNo));
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
                prodMap.put("prodSum", "소계");
                sumMakeQty += Float.parseFloat(el.get("makeQty").toString());
                sumbefQty += Long.parseLong(el.get("befStkQty").toString());
                sumIwhQty += Float.parseFloat(el.get("iwhQty").toString());
                sumOwhQty += Long.parseLong(el.get("owhQty").toString());
                sumStkQty += Float.parseFloat(el.get("stkQty").toString());

                prodMap.put("sumQty",sumMakeQty);// 일일 생산량 합
                prodMap.put("sumBefQty",sumbefQty);//전일재고 합
                prodMap.put("sumIwhQty",sumOwhQty);//금일입고 합
                prodMap.put("sumOwhQty",sumOwhQty);//금일출고 합
                prodMap.put("sumStkQty",sumStkQty);//현재고

            } else{
                prodMap.put("prodSum", "소계");
                makeQty += Float.parseFloat(el.get("makeQty").toString());
                befQty += Long.parseLong(el.get("befStkQty").toString());
                owhQty += Long.parseLong(el.get("owhQty").toString());
                iwhQty += Float.parseFloat(el.get("iwhQty").toString());
                stkQty += Float.parseFloat(el.get("stkQty").toString());
                prodMap.put("sumQty",makeQty);// 일일 생산량 합
                prodMap.put("befQty",befQty);//전일재고 합
                prodMap.put("owhQty",owhQty);//금일출고 합
                prodMap.put("iwhQty",iwhQty);//금일입고 합
                prodMap.put("stkQty",stkQty);//현재고
            }
                prodMap.put("codeNm",el.get("codeNm"));// 제조 구분erpUserNo
                prodMap.put("prodNm",el.get("prodNm"));// 제품명
                prodMap.put("qtyPerQkg",el.get("qtyPerQkg"));//입수
                prodMap.put("makeQty",el.get("makeQty"));//일일생산량
                prodMap.put("befStkQty",el.get("befStkQty").toString());//전일재고
                prodMap.put("iwhQty",el.get("iwhQty").toString());//금일입고
                prodMap.put("owhQty",el.get("owhQty").toString());//금일출고
                prodMap.put("stkQty",el.get("stkQty").toString());//현재고
                prodMap.put("time", el.get("validTerm").toString()); //유통기한
            list.add(prodMap);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> makeIndcExcelList(Map<String, Object> paraMap) {
        return  matrMapper.getmakeIndcExcelList(paraMap);
    }



    //대동고려삼 BOM Mapper로 ProdNo 찾아서 BOM 등록하기
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
        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();

//        원자재 등록
        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex < 1) continue; //헤더정보 skip
            XSSFRow row = sheet.getRow(rowindex);


            erpProdNm = row.getCell(0).getStringCellValue();
            erpProdNm = erpProdNm.replaceAll("\\p{Z}", "");
            if (erpProdNm.contains("합계") == true) {
                break;
            }

            matrNm = row.getCell(2).getStringCellValue();
            matrNm = matrNm.replaceAll("\\p{Z}", ""); // 띄어쓰기 제거

            MatrInfo chkvo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,matrNm, "Y");
            if (chkvo != null) {
                matrNo = chkvo.getMatrNo();
            }
            consistRt = (float) row.getCell(5).getNumericCellValue();

            List<Map<String, Object>> ds = mapper.getProdNo(erpProdNm);
            for (Map<String, Object> el : ds) {
                ProdBom bomvo = new ProdBom();
                Long prodNo = Long.parseLong(el.get("prod_no").toString());

                if (matrNm.contains("정제수") == true) { //정제수는 물 == 구매 안함.
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
        String erpprodNm = "";
        String exData = "";
        String prodcd = "";
        Float vol = 0F;
        Float spga = 0F;



        XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
        int rows = sheet.getPhysicalNumberOfRows();
        int rowindex = 0;
        log.info(tag + "전체 excel처리 행수 = " + rows);
        XSSFRow row = sheet.getRow(rowindex);

        for (rowindex = 0; rowindex <= rows; rowindex++) {
//            if (rowindex < 1) continue; //헤더정보 skip
            row = sheet.getRow(rowindex);
            if (row == null) continue;
            try {
                prodNm = row.getCell(2).getStringCellValue();
                prodNm = prodNm.replaceAll("\\p{Z}", "");
                erpprodNm = row.getCell(0).getStringCellValue();
                erpprodNm = erpprodNm.replaceAll("\\p{Z}", "");
                if (prodNm.contains("합계") == true) {
                    break;
                }

            } catch (NullPointerException ne) {
                log.info(tag + "행번(" + rowindex + ") : 제품명 없음.... skip....");
                continue;
            }
            ProdInfo prodInfo = new ProdInfo();
//            prodcd = row.getCell(1).getStringCellValue(); // 코드번호
            prodInfo.setProdCode(prodcd);
            prodInfo.setProdNm(prodNm);
            prodInfo.setErpProdNm(erpprodNm);
            double qtypkg = row.getCell(7).getNumericCellValue();
            prodInfo.setQtyPerPkg((int)qtypkg); //세트구성수량(기본값설정)
            prodInfo.setFileNo(0L);//상품이미지(기본값설정)
            prodInfo.setUsedYn("Y");//사용구분(기본값설정)
            prodInfo.setProdBrnch(1L); //분류(기본값설정)
            prodInfo.setSaveTmpr(Long.parseLong(env.getProperty("code.savetmpr.room"))); //보관온도
            prodInfo.setBrnchNo((long)row.getCell(1).getNumericCellValue()); // 공정별 분류
            spga = (float)row.getCell(3).getNumericCellValue();
            prodInfo.setSpga(spga); // 비중 비율
            prodInfo.setHeatTp(0L);

            vol = (float) row.getCell(5).getNumericCellValue();

            exData = row.getCell(6).getStringCellValue();
            Long prodShape = Long.parseLong(env.getProperty("code.base.saleunit"));
            CodeInfo mgvo = codeRepo.findByCustNoAndParCodeNoAndCodeNmAndUsedYn(custNo,prodShape, exData.replace(" ", ""), "Y");
            if (mgvo != null) {
                Long prodUnit = mgvo.getCodeNo();
                prodInfo.setSaleUnit(prodUnit); // 단위

                if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_Kg"))){ // KG을 G으로 환산
                    prodInfo.setVol(vol);
                    prodInfo.setMess(vol*spga);
                }else {
                    prodInfo.setVol(vol/1000);
                    prodInfo.setMess(vol*spga);
                }if(prodUnit == Long.parseLong(env.getProperty("code.base.sale_unit_ml"))){
                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_mess")));
                }else {
                    prodInfo.setMngrBase(Long.parseLong(env.getProperty("code.base.mngrbase_imp")));
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