package daedan.mes.make.service;

import daedan.mes.make.domain.MakeIndc;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ExcelMakeService {


    List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcMp(Map<String, Object> paraMap);


    void saveMakeProc(Map<String, Object> paraMap) throws Exception;

    Long saveMake(Map<String, Object> paraMap) throws Exception;

    void saveMakeIndcMatr(Map<String, Object> paraMap) throws Exception;

    void makeBomByExcel(HashMap<String, Object> paraMap) throws Exception;

    // 하담푸드 제조공정 생성
    @Transactional
    abstract void hadamfoodsaveMakeProc(Map<String, Object> paraMap) throws Exception;

    @Transactional
    Long hadamfoodsaveMake(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void DaedongsaveMakeProc(Map<String, Object> paraMap) throws Exception;


    @Transactional
    Long DaedongsaveMake(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void hadamfoodsaveMakeIndcMatr(Map<String, Object> paraMap) throws Exception;
    

    @Transactional
    void makeMakeIndcMpByExcel(Map<String, Object> paraMap) throws Exception;

    String getUseddt(XSSFSheet sheet, int dtRowindex, int columnindex, String svUseDt, String secUseDt);


    @Transactional
    void DaedongsaveMakeIndcMatr(Map<String, Object> paraMap) throws Exception;
}
