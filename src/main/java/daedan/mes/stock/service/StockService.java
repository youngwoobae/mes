package daedan.mes.stock.service;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StockService {

    void resetMatrStock(Map<String, Object> paraMap);

     void saveMatrPos(Map<String, Object> paraMap); //단품처리

    /*구매일괄 창고적재 처리*/
    void savePursMatrPos(Map<String, Object> paraMap);

    List<Map<String,Object>> getComboWh(Map<String, Object> paraMap);

    List<Map<String, Object>> getWhList(Map<String, Object> paraMap);
    int getWhListCount(Map<String, Object> paraMap);
    void saveWhInfo(Map<String, Object> paraMap);

    Map<String, Object> getWhInfo(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrPosList(HashMap<String, Object> paraMap);
    int  getMatrPosListCount(HashMap<String, Object> paraMap);

    Map<String, Object> getMatrPosInfo(HashMap<String, Object> paraMap);

    byte[] makeBarCode(Map<String, Object> paraMap);


    @Transactional void addOnStock(Map<String, Object> paraMap) ;
    @Transactional void reduceStock(Map<String, Object> paraMap);

    void dropWhInfo(Map<String, Object> paraMap);

    //원료실사재고
    List<Map<String, Object>>  getMatrRealStockList(HashMap<String, Object> paraMap);
    int getMatrRealStockListCount(HashMap<String, Object> paraMap);

    @Transactional
    void adjustMatrStock(Map<String, Object> paraMap);

    void realStockSave(Map<String, Object> paraMap);

    void saveProdStock(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrProdIoYearList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrStockPosList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrStockListAndLayout(HashMap<String, Object> paraMap);
    int getMatrStockListAndLayoutCount(HashMap<String, Object> paraMap);

    int getCheckMatrIwhListCount(HashMap<String, Object> paraMap);

    Map<String, Object> getMatrStockByIo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrStkList(HashMap<String, Object> paraMap);
    int getMatrStkListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrIoList(HashMap<String, Object> paraMap);
    int getMatrIoListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getProdIoList(HashMap<String, Object> paraMap);
    int getProdIoListCount(HashMap<String, Object> paraMap);
    //수불대장 총괄장
    List<Map<String, Object>>  getStkClosList(HashMap<String, Object> paraMap);
    int  getStkClosListCount(HashMap<String, Object> paraMap);

    //제품 수불대장
    List<Map<String, Object>>  getProdStkClosList(HashMap<String, Object> paraMap);
    int  getProdStkClosListCount(HashMap<String, Object> paraMap);

    //자재 수불대장
    List<Map<String, Object>>  getMatrStkClosList(HashMap<String, Object> paraMap);
    int  getMatrStkClosListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getStkClosMatrIwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosMatrIwhDetlHstrCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getStkClosMatrOwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosMatrOwhDetlHstrCount(HashMap<String, Object> paraMap);


    List<Map<String, Object>> getStkClosProdIwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosProdIwhDetlHstrCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getStkClosProdOwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosProdOwhDetlHstrCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>>  getMatrForRealStkList(Map<String, Object> paraMap);
    int getMatrForRealStkListCount(Map<String, Object> paraMap);

    void saveMatrRealStkList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdForRealStkList(Map<String, Object> paraMap);
    int getProdForRealStkListCount(Map<String, Object> paraMap);

    void saveProdRealStkList(Map<String, Object> paraMap);

    void dropMatrRelStk(Map<String, Object> paraMap);

    void remakeDailyCloseData(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrRealStockHstr(HashMap<String, Object> paraMap);
    int getMatrRealStockHstrCount(Map<String, Object> paraMap);

    Map<String, Object> getShdList(Map<String, Object> paraMap);
}
