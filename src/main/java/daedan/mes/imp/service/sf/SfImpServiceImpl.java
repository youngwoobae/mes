package daedan.mes.imp.service.sf;

import daedan.mes.common.service.util.StringUtil;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.file.service.FileService;
import daedan.mes.matr.domain.MatrInfo;
import daedan.mes.matr.repository.MatrRepository;
import daedan.mes.prod.domain.ProdInfo;
import daedan.mes.prod.mapper.ProdMapper;
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

import java.io.FileInputStream;
import java.util.*;

@Service("sfImpService")
public class SfImpServiceImpl implements SfImpService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private ProdRepository prodRepo;

    @Autowired
    private ProdBomRepository prodBomrepo;

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private MatrRepository matrRepo;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private FileService fileService;


    /*서울식품 제품체크 */
//    @Transactional
    @Override
    public ArrayList<List<Map<String, Object>>> chkProdExist(HashMap<String, Object> paraMap) throws Exception {
        String tag = "ProdService.chkProdExist => ";
        StringBuffer buf = new StringBuffer();
        Long FileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String filePath = fileService.getFileInfo(FileNo);

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        ArrayList<List<Map<String, Object>>> chkList = new ArrayList<>();
        List<Map<String,Object>> bomList = new ArrayList<>();
        List<Map<String, Object>> prodList = new ArrayList<>();

        int prodRowindex = Integer.parseInt(env.getProperty("excel_prod_row_no"));    //품명이 배치된 행
        int ProdFirstindex = Integer.parseInt(env.getProperty("excel_prod_row_first")); // 열의 처음
        int prodSvindex = Integer.parseInt(env.getProperty("excel_Sv_row_no")); // 서브 품명

//        XSSFSheet sheet = workbook.getSheetAt(idx); //시트 번호~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();

        String prodNm = "";
        String mainProdNm = "";
        String sveProdNm = null;
        String svProdNm = "";
        String secProdNm = "";

        XSSFRow rowProdMain = sheet.getRow(prodRowindex); //메인상품명을 가지고 있는 행
        XSSFRow rowProdNm = sheet.getRow(prodSvindex); //서브상품명을 가지고 있는 행
        XSSFRow row = sheet.getRow(prodRowindex); // 행 읽을때 사용

        //업로드용 엑셀의 제품정보와 가지고 있는 제품정보 비교
        for (int columnindex = ProdFirstindex; columnindex <= rows; columnindex++) {

            Map<String, Object> rmap = new HashMap<>();
            Map<String, Object> bomMap = new HashMap<>();

            mainProdNm = String.valueOf(rowProdMain.getCell(columnindex));// 상품명
            try {
                Float.parseFloat(mainProdNm);
                mainProdNm = svProdNm;
            } catch (NumberFormatException e) {
                if (mainProdNm.contains("총 사용량") == true) {
                    break;
                }
            }
            sveProdNm = rowProdNm.getCell(columnindex).getStringCellValue(); // 서브 상품명
            if (!StringUtil.isEmpty(mainProdNm)) {
                svProdNm = mainProdNm;
            }
            if (sveProdNm != null) {
                secProdNm = sveProdNm;
            } else {
                continue;
            }

            row = sheet.getRow(3);

            prodNm = this.getProdNm(sheet, prodRowindex, columnindex, svProdNm, secProdNm).replaceAll("\\p{Z}", "");
            Long prodNo = 0L;
            ProdInfo chkInfo = prodRepo.findByCustNoAndProdNmAndUsedYn(custNo,prodNm, "Y");
            if (chkInfo != null) {
                prodNo = chkInfo.getProdNo();
                //해당 제품의 bom 체크;
                bomMap =  this.chkProdBomExist(prodNo);
                if(bomMap != null){
                    bomList.add(bomMap);
                }
                continue;
            } else {

                rmap.put("prodNm", prodNm);
                rmap.put("lineNo", 3);
                rmap.put("colNo", columnindex);

                prodList.add(rmap);
            }
        }

        chkList.add(prodList);

        //업로드용 엑셀의 원료정보와 가지고 있는 원료정보 비교
        List<Map<String, Object>> matrExistList = this.chkMatrExist(ProdFirstindex, rows, custNo, FileNo);
        chkList.add(matrExistList);
        chkList.add(bomList);

        return chkList;
    }

    public String getProdNm(XSSFSheet sheet, int rowindex, int colindex, String mainProdNm, String sveProdNm) {
        StringBuffer buf = new StringBuffer();

        buf.setLength(0);
        buf.append(mainProdNm);
        buf.append("\n").append(sveProdNm);
        return buf.toString().replaceAll("(\r|\n|\r\n|\n\r)", " ");
    }

    public List<Map<String, Object>> chkMatrExist(int firstIndex, int rows, Long custNo,Long fileNo) throws Exception {
        log.info("sfImpServiceImpl.chkMatrExist => " );

        List<Map<String, Object>> matrChkList = new ArrayList<>();

        StringBuffer buf = new StringBuffer();


        String filePath = fileService.getFileInfo(fileNo);

        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        String MatrNm = null;

        for (int rowIndex = firstIndex; rowIndex <= rows; rowIndex++) {
            Map<String, Object> chkMap = new HashMap<>();
            buf.setLength(0);
            if(rowIndex < 6){
                continue;
            }
            XSSFRow rowMatrMain = sheet.getRow(rowIndex);
            MatrNm = String.valueOf(rowMatrMain.getCell(0));

            if (MatrNm.contains("특이사항") == true) {
                break;
            }

            MatrNm = buf.append(MatrNm).toString().replaceAll("\\p{Z}", "");
            MatrInfo chkmivo = matrRepo.findByCustNoAndMatrNmAndUsedYn(custNo,MatrNm, "Y");
            if(chkmivo != null){
                continue;
            }else{
                chkMap.put("lineNo", rowIndex);
                chkMap.put("colNo", 1);
                chkMap.put("matrNm", MatrNm);

                matrChkList.add(chkMap);
            }

        }
        return matrChkList;

    }

    public Map<String, Object> chkProdBomExist(Long prodNo){
        Map<String, Object> prodMap = new HashMap<>();
        prodMap.put("prodNo", prodNo);

        Map<String, Object> bomchk = prodMapper.getBomConsist(prodMap);
        Float sumConsistRt = 0F;

        Map<String, Object> bomMap = new HashMap<>();

        try{
            sumConsistRt = Float.parseFloat(bomchk.get("sum_consist_rt").toString());
        }catch(NullPointerException ne){
            Map<String, Object> prodError = prodMapper.getProdInfo(prodMap);
            bomMap.put("prodNm", prodError.get("prod_nm"));
            bomMap.put("consistRt", "No Data");
        }

        if(sumConsistRt < 100 || sumConsistRt > 100){
            List<Map<String, Object>> bomError = prodMapper.getProdBom(prodMap);
            for(Map<String, Object> el : bomError){
                try{
                    bomMap.put("prodNo", prodNo);
                    bomMap.put("prodNm", el.get("prod_nm"));
                    bomMap.put("consistRt", sumConsistRt);
                }catch(NullPointerException ne){
                    bomMap.put("noData", "noData");
                }

            }
        }
        else if(sumConsistRt == 0F || sumConsistRt == null){
            bomMap.put("noData", "noData");
        }else{
            bomMap = null;
        }

        return bomMap;
    }

}
