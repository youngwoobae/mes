package daedan.mes.prod.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProdService {
    List<Map<String, Object>> getProdList(Map<String, Object> paraMap);
    int getProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdByPrefixList(Map<String, Object> paraMap);

    void dropProdInfo(Map<String, Object> paraMap);
    List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap);
    int getProdBomListCount(Map<String, Object> paraMap);
    Map<String, Object> getProdInfo(Map<String, Object> paraMap);
    Map<String, Object>  saveProd(Map<String, Object> paraMap);

    @Transactional
    void apndProdBom(Map<String, Object> paraMap);

    @Transactional
    void saveProdBom(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void dropProdBom(Map<String, Object> paraMap);

    List<Map<String, Object>> getAbleOnProdBomList(Map<String, Object> paraMap);
    int getAbleOnProdBomListCount(Map<String, Object> paraMap);



    void hadamfoodmakeExcel(List<Map<String, Object>> paraMap) throws Exception;

    Map<String, Object> getMonthlyOrderQty(Map<String, Object> paraMap);

    void dropProd(Map<String, Object> paraMap);

    @Transactional void makeProdByExcel(HashMap<String, Object> paraMap) throws Exception;

    Map<String, Object> getProdBomInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdCmpyList(Map<String, Object> paraMap);
    int getProdCmpyListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdStkList(Map<String, Object> paraMap);

    int getProdStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdOrdList(Map<String, Object> paraMap);


    int getProdOrdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdStatList(Map<String, Object> paraMap);
    int getProdStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getAnaprodList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboProcBrnch(Map<String, Object> paraMap);

    void dropOrdProd(Map<String, Object> paraMap);

    Object getKioProdOwhList(Map<String, Object> paraMap);

    int getKioProdOwhListCount(Map<String, Object> paraMap);

    String getProdNm(XSSFSheet sheet, int prodRowindex, int columnindex, String svProdNm, String secProdNm);


    Float getIndcQty(XSSFSheet sheet, int MatrRowindex, int columnindex, Float svIndcQty);




    void makeExcel(List<Map<String, Object>> paraMap ) throws Exception;

    @Transactional
    void ProdIndcByExcel(HashMap<String, Object> paraMap) throws Exception;

    @Transactional
    default void saveProdBomByInput(Map<String, Object> paraMap) {

    }

    List<Map<String, Object>> getProdExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> getBomExcelList(Map<String, Object> paraMap);

    void matchBrnch(Map<String, Object> paraMap);

    @Transactional
    void DaedongIndcByExcel(HashMap<String, Object> paraMap)  throws Exception;
    @Transactional
    void DaedongmakeExcel(List<Map<String, Object>> paraMap) throws Exception;

    void saveReqMatr(Map<String, Object> paraMap);



    List<Map<String, Object>> getSendInfoToElecticTagUrlList(Map<String, Object> paraMap);

    int getSendInfoToElecticTagUrlListCount(Map<String, Object> paraMap);

    /*제품 입고 처리 (Excel) */
    List<Map<String, Object>> stsProdExcelIwh(HashMap<String, Object> paraMap)throws Exception;
    /*제품 출고 처리 (Excel) */
    List<Map<String, Object>> prodExcelOwh(HashMap<String, Object> paraMap) throws Exception;
    /*자재 입고 처리 (Excel) */
    List<Map<String, Object>> stsMatrExcelIwh(HashMap<String, Object> paraMap)throws Exception;
    /*자재 입고 처리 (Excel) */
    List<Map<String, Object>> stsMatrExcelOwh(HashMap<String, Object> paraMap)throws Exception;

    List<Map<String, Object>> getProdBom(Map<String, Object> paraMap);
    int getProdBomCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdBomListByIndc(Map<String, Object> bomchkmap);

    void copyProdBom(Map<String, Object> paraMap);

    List<Map<String, Object>>  getHdfdProdBomList(Map<String, Object> paraMap);

    List<Map<String, Object>> getHdfdProdBomListByIndc(Map<String, Object> bomchkmap);

    Map<String, Object> getProdStkInfo(Map<String, Object> paraMap);


    List<Map<String, Object>> getSfProdBomListByIndc(Map<String, Object> bomchkmap);

    int getHdfdProdBomListCount(Map<String, Object> paraMap);

//    void updateBomPursYn(Map<String, Object> paraMap);
}
