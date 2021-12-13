package daedan.mes.stock.mapper;

import daedan.mes.common.domain.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface StockMapper {
    void dropMatrPos(Map<String, Object> voToMap);
    List<Map<String,Object>> getComboWh(Map<String, Object> voToMap);
    List<Map<String, Object>> getWhList(Map<String, Object> paraMap);
    int getWhListCount(Map<String, Object> paraMap);
    Map<String, Object> getWhInfo(HashMap<String, Object> paraMap);

    //원부자재 적재위치목록
    List<Map<String, Object>> getMatrPosList(HashMap<String, Object> paraMap);
    int getMatrPosListCount(HashMap<String, Object> paraMap);
    //원부자재 적재위치정보
    Map<String, Object> getMatrPosInfo(HashMap<String, Object> paraMap);

    float getMatrCurrentStock(Map<String, Object> paraMap);

    void renewalMatrStock(Map<String, Object> paraMap);

    void addOnStock(Map<String, Object> paraMap);

    void reduceStock(Map<String, Object> paraMap);

    void dropWhInfo(Map<String, Object> paraMap);
    //원료실사재고
    List<Map<String, Object>> getMatrRealStockList(HashMap<String, Object> paraMap);
    int getMatrRealStockListCount(HashMap<String, Object> paraMap);


    List<Map<String, Object>> getMatrProdIoYearList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrStockList(HashMap<String, Object> paraMap);

    void renewalMatrStockPos(Map<String, Object> smap);

    List<Map<String, Object>> getMatrStockListAndLayout(HashMap<String, Object> paraMap);
    int getMatrStockListAndLayoutCount(HashMap<String, Object> paraMap);

    int getCheckMatrIwhListCount(HashMap<String, Object> paraMap);
    void renewalMatrStockByIOwh(Map<String, Object> smap);

    Map<String, Object> getMatrStockByPos(Map<String, Object> smap);
    Map<String, Object> getMatrStockByIo(Map<String, Object> smap);

    List<Map<String, Object>> getMatrStkList(HashMap<String, Object> paraMap);

    int getMatrStkListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrIoList(HashMap<String, Object> paraMap);

    int getMatrIoListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getProdIoList(HashMap<String, Object> paraMap);

    int getProdIoListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getCurrentYear(HashMap<String, Object> paraMap);
    //수불대장/총괄장
    List<Map<String, Object>> getStkClosList(HashMap<String, Object> paraMap);
    int getStkClosListCount(HashMap<String, Object> paraMap);
    //제품 수불대장
    List<Map<String, Object>>  getProdStkClosList(Map<String, Object> paraMap);
    int  getProdStkClosListCount(HashMap<String, Object> paraMap);

    //자재 수불대장
    List<Map<String, Object>>  getMatrStkClosList(Map<String, Object> paraMap);
    int  getMatrStkClosListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getStkClosMatrIwhDetlHstr(Map<String, Object> paraMap);
    int getStkClosMatrIwhDetlHstrCount(HashMap<String, Object> paraMap);
    List<Map<String, Object>> getStkClosMatrOwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosMatrOwhDetlHstrCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getStkClosProdIwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosProdIwhDetlHstrCount(HashMap<String, Object> paraMap);
    List<Map<String, Object>> getStkClosProdOwhDetlHstr(HashMap<String, Object> paraMap);
    int getStkClosProdOwhDetlHstrCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrForRealStkList(Map<String, Object> paraMap);
    int getMatrForRealStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdForRealStkList(Map<String, Object> paraMap);
    int getProdForRealStkListCount(Map<String, Object> paraMap);

    void initMatrClosStk(Map<String, Object> paraMap);
    void initProdClosStk(Map<String, Object> paraMap);
    List<Map<String, Object>> getMatrClosList(Map<String, Object> paraMap);
    List<Map<String, Object>> getProdClosList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrRealStockHstr(HashMap<String, Object> paraMap);
    int getMatrRealStockHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getShdList(Map<String, Object> paraMap);
    List<Map<String, Object>> getShdPlanList(Map<String, Object> paraMap);
}
