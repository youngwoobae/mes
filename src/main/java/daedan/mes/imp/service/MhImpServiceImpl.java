package daedan.mes.imp.service;

import daedan.mes.cmpy.domain.CmpyInfo;
import daedan.mes.cmpy.repository.CmpyRepository;
import daedan.mes.code.domain.CodeInfo;
import daedan.mes.code.repository.CodeRepository;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.ord.domain.OrdInfo;
import daedan.mes.ord.domain.OrdProd;
import daedan.mes.ord.repository.OrdProdRepository;
import daedan.mes.ord.repository.OrdRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.repository.ProdRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("MhImpService")
public class MhImpServiceImpl implements MhImpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CodeRepository codeRepo;

    @Autowired
    private CmpyRepository cmpyRepo;

    @Autowired
    private OrdRepository ordRepo;

    @Autowired
    private OrdProdRepository ordProdRepo;

    @Autowired
    private ProdRepository prodRepo;


    @Override
    public void ordInfoByExcel(Map<String, Object> paraMap) throws Exception {
        String tag = "ProdService.ordInfoByExcel => ";
        StringBuffer buf = new StringBuffer();
        String fileRoot = paraMap.get("fileRoot").toString();
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        buf.setLength(0);
        buf.append(fileRoot).append(File.separator).append(paraMap.get("fileNm"));

        FileInputStream file = new FileInputStream(buf.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(file);

//        String filePath = fileService.getFileInfo(Long.parseLong(paraMap.get("fileNo").toString()));

//        FileInputStream file = new FileInputStream(filePath);
//        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex = 0;
        XSSFSheet sheet = workbook.getSheetAt(0); // 시트 읽기
        int rows = sheet.getPhysicalNumberOfRows();
        String cmNm = "";
        String cmMi ="";
        Long ordNo = 0L;
        String ordDt="";
        Long ordProdNo = 0L;

        for (rowindex = 0; rowindex < rows; rowindex++) {
            if (rowindex <= 1) continue; //헤더정보 skip

            XSSFRow row = sheet.getRow(rowindex);
            if (row == null) continue;

            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            OrdInfo ordvo = new OrdInfo();

            ordvo.setOrdNm(row.getCell(6).getStringCellValue());


            String exData = row.getCell(3).getStringCellValue(); // 단위 중량
            Long ordSts = Long.parseLong(env.getProperty("code_base_ord_status"));
            CodeInfo cdchk = codeRepo.findByParCodeNoAndCodeNmAndUsedYn(ordSts, exData.replace(" ", ""), "Y");
            if (cdchk != null) {
                ordvo.setOrdSts(cdchk.getCodeNo());
            }

            CmpyInfo cmpyvo = new CmpyInfo();
            cmNm = row.getCell(9).getStringCellValue();
            cmMi = row.getCell(10).getStringCellValue();
            try{
                ordNo = (long)row.getCell(1).getNumericCellValue();
            }catch (NullPointerException en){

            }

            cmpyvo.setUsedYn("Y");

            cmpyvo.setCmpyTp(Long.parseLong(env.getProperty("code.cmpytp.pers")));
            cmpyvo.setMngrGbnCd(Long.parseLong(env.getProperty("code.mngrgbn.sale")));
            Long mngrgbnSale = cmpyvo.getMngrGbnCd();
            CmpyInfo cmvo = cmpyRepo.findByCustNoAndMngrGbnCdAndCmpyNmAndReprMailAddrAndUsedYn(custNo,mngrgbnSale,cmNm,cmMi,"Y");
            if(cmvo != null){
                cmpyvo.setReprMailAddr(cmvo.getReprMailAddr());
                cmpyvo.setCmpyNo(cmvo.getCmpyNo());
                cmpyvo.setCmpyNm(cmvo.getCmpyNm());
                cmpyvo.setModIp((String) paraMap.get("ipaddr"));
                cmpyvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                cmpyvo.setModDt(DateUtils.getCurrentBaseDateTime());
            }else {
                cmpyvo.setCmpyNo(0L);
                cmpyvo.setCmpyNm(cmNm);
                cmpyvo.setReprMailAddr(cmMi);
                cmpyvo.setRegIp((String) paraMap.get("ipaddr"));
                cmpyvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                cmpyvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            }
            cmpyvo = cmpyRepo.save(cmpyvo);

            ordvo.setCmpyNo(cmpyvo.getCmpyNo());
                ordvo.setOrdTp(Long.parseLong(env.getProperty("ord_oem_cd")));
                ordvo.setPlcNo(0L);
                ordDt = row.getCell(2).getStringCellValue();
                ordvo.setOrdDt(sdf.parse(ordDt));
                ordvo.setDlvReqDt(sdf.parse(ordDt));

                OrdInfo ordchk = ordRepo.findByCustNoAndOrdNoAndUsedYn(custNo,ordNo,"Y");
                if(ordchk != null){
                    ordvo.setOrdNo(ordchk.getOrdNo());
                    ordvo.setOrdNm(ordchk.getOrdNm());
                    ordvo.setModIp((String) paraMap.get("ipaddr"));
                    ordvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                    ordvo.setModDt(DateUtils.getCurrentBaseDateTime());
                }else {
                    ordchk.setOrdNo(ordNo);
                    ordvo.setOrdNm(ordvo.getOrdNm());
                    ordvo.setRegIp((String) paraMap.get("ipaddr"));
                    ordvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                    ordvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            }
            ordvo = ordRepo.save(ordvo);

            OrdProd opvo = new OrdProd();
            try{
                ordProdNo = (long)row.getCell(0).getNumericCellValue();
            }catch (NullPointerException en){

            }
            opvo.setOrdQty((float)row.getCell(8).getNumericCellValue());
            opvo.setUsedYn("Y");
            ProdInfo prvo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,ordvo.getOrdNm(),"Y");
            OrdProd opchk = ordProdRepo.findByCustNoAndOrdNoAndProdNoAndUsedYn(custNo,ordvo.getOrdNo(),prvo.getProdNo(),"Y");
            if(opchk != null){
                opvo.setOrdProdNo(opchk.getOrdProdNo());
                opvo.setModIp((String) paraMap.get("ipaddr"));
                opvo.setModId(Long.parseLong(paraMap.get("userId").toString()));
                opvo.setModDt(DateUtils.getCurrentBaseDateTime());
            }else {
                opvo.setOrdProdNo(ordProdNo);
                opvo.setOrdNo(ordvo.getOrdNo());
                opvo.setProdNo(prvo.getProdNo());
                opvo.setRegIp((String) paraMap.get("ipaddr"));
                opvo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                opvo.setRegDt(DateUtils.getCurrentBaseDateTime());
            }

            ordProdRepo.save(opvo);

        }
    }
}
