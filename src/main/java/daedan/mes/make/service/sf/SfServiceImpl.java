package daedan.mes.make.service.sf;


import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.file.domain.FileInfo;
import daedan.mes.file.repository.FileRepository;
import daedan.mes.file.service.FileService;

import daedan.mes.make.domain.sf.DailyIndc;
import daedan.mes.make.mapper.sf.SfMapper;
import daedan.mes.make.repository.sf.DailyIndcRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sfService")
public class SfServiceImpl implements  SfService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private DailyIndcRepository dailyindcRepo;

    @Autowired
    private SfMapper mapper;

    @Autowired
    private CmmnService cmmnService;


    @Override
    public Map<String, Object> getXsl2Hmtl(Map<String, Object> paraMap){
        String tag = "SfService.getDailyIndcHtml => ";
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        Long custNo = Long.parseLong(paraMap.get("custNo").toString());
        String filePath = fileService.getFileInfo(fileNo);
        FileInfo fileEntity = fileRepo.findByCustNoAndFileNoAndUsedYn(custNo,fileNo,"Y");
        Map<String, Object> rmap = new HashMap<String, Object>();
        try {
            FileInputStream file = new FileInputStream(filePath);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            ExcelToHtmlConverter excelToHtmlConverter = null;
            excelToHtmlConverter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            excelToHtmlConverter.processWorkbook(workbook);
            List pics = workbook.getAllPictures();
            if (pics != null) {
                for (int i = 0; i < pics.size(); i++) {
                    Picture pic = (Picture) pics.get(i);
                    try {
                        pic.writeImageContent(new FileOutputStream(filePath + pic.suggestFullFileName()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            Document htmlDocument = excelToHtmlConverter.getDocument();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = null;
            try {
                serializer = tf.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            try {
                serializer.transform(domSource, streamResult);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
            outStream.close();
            rmap.put("xls2html", new String(outStream.toByteArray(), "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return rmap;
    }

    @Override
    public void makeDailyIndc(Map<String, Object> paraMap) {
        String tag = "SfService.makeDailyIndc => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        Long fileNo = Long.parseLong(paraMap.get("fileNo").toString());
        //FileInfo fileEntity = fileRepo.findByFileNoAndUsedYn(fileNo,"Y");
        FileInputStream excelFile = null;

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        try {
            excelFile = new FileInputStream(fileService.getFileInfo(fileNo));
            XSSFWorkbook workbook = null;
            workbook = new XSSFWorkbook(excelFile);

            XSSFSheet sheet = workbook.getSheetAt(0); //첫번째쉬트
            int rows = sheet.getPhysicalNumberOfRows();

            int colindex = 2;
            int cols = 20;

            log.info(tag + "전체 excel처리 행수 = " + rows);
            XSSFRow row;
            XSSFCell cell;
            Date dt = new Date();

            for(colindex = 2; colindex < cols; colindex++){
                log.info("colindex" + colindex);
                String rmkTotal = "";
                int rowindex = 1;

                if(rowindex == 1){
                    cell = sheet.getRow(rowindex).getCell(colindex);
                    if(cell == null) break;
                    dt = cell.getDateCellValue();
                }

                for(rowindex = 3; rowindex <= rows; rowindex++){
                    String rmk = "";
                    log.info("rowindex" + rowindex);
                    row = sheet.getRow(rowindex);

                    if(row != null){
                        cell = sheet.getRow(rowindex).getCell(colindex);
                        if(cell == null) break;
                        rmk = cell.getStringCellValue() + "\n";
                        rmkTotal += rmk;
                    }else{
                        continue;
                    }
                }

                if(rmkTotal == "") break;

                DailyIndc divo = new DailyIndc();
                divo.setDateIndcNo(0L);

                divo.setIndcDt(dt);

                divo.setRegIp(paraMap.get("ipaddr").toString());
                divo.setRegId(Long.parseLong(paraMap.get("userId").toString()));
                divo.setRegDt(DateUtils.getCurrentDateTime());
                divo.setUsedYn("Y");

                divo.setIndcRmk(rmkTotal);
                dailyindcRepo.save(divo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> getDailyIndc(Map<String, Object> paraMap) {
        return mapper.getDailyIndc(paraMap);
    }
}
