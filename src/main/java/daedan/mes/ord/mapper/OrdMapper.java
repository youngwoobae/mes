package daedan.mes.ord.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface OrdMapper {
    List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap);
    int getOrdProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdProdBomList(Map<String, Object> paraMap);
    int getOrdProdBomListCount(Map<String, Object> paraMap);

    void apndOrdInfo(Map<String, Object> paraMap);
    void saveOrdInfo(Map<String, Object> paraMap);
    void dropOrdInfo(Map<String, Object> paraMap);
    void revivalOrdInfo(Map<String, Object> paraMap);

    void apndOrdProd(Map<String, Object> opMap);
    void saveOrdProd(Map<String, Object> paraMap);
    void deleteOrdProd(Map<String, Object> paraMap);
    void revivalOrdProd(Map<String, Object> paraMap);

    void apndOrdInfoToCart(Map<String, Object> paraMap);
    void apndOrdProdToCart(Map<String, Object> opMap);

    void dropOrdProdCart(Map<String, Object> paraMap);

    Map<String, Object> getOemOrdInfo(Map<String, Object> paraMap);
    Map<String, Object> getPrjOrdInfo(Map<String, Object> paraMap);

        List<Map<String, Object>> getOrdProdCartList(Map<String, Object> paraMap);
    int getOrdProdCartListCount(Map<String, Object> paraMap);
    Long getJustInCreatedOrdNo(Map<String, Object> paraMap);
    void moveOrdProdToCart(Map<String, Object> paraMap);
    void saveOrdProdCart(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboDlvPlc(Map<String, Object> paraMap);

    List<Map<String, Object>> getOwhWaitMatrList(Map<String, Object> el); //주문접수후 출고대기 원부자재목록

    void saveOwhWaitQty(Map<String, Object> matrMap);

    void renewalMatrStk(Map<String, Object> map);
    void addOnMatrStk(Map<String, Object> map);

    void resetPursNo(Map<String, Object> paraMap);

    void dropOrdProd(Map<String, Object> dropmap);

    void renewalOrdProd(Map<String, Object> prodNos);

    void initPursProd(Map<String, Object> dropmap);


    List<Map<String, Object>> getOrdProdCartBomList(Map<String, Object> paraMap);

    int getOrdProdCartBomListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdList(Map<String, Object> paraMap);
    Map<String, Object> getOrdProdInfo(Map<String, Object> paraMap);

    int getOrdListCount(Map<String, Object> paraMap);

    Map<String, Object> getOrdInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getExOrdProdList(Map<String, Object> paraMap);
    Map<String, Object> getExOrdProdInfo(Map<String, Object> paraMap);
    int getExOrdProdListCount(Map<String, Object> paraMap);

    void initOrdProdCart(Map<String, Object> paraMap);
    void createOrdrProdCart(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboDlvPlcList(Map<String, Object> paraMap);

    void updateOrdProdQty(Map<String, Object> voToMap);

    void resetOrdStatus(Map<String, Object> prodMap);

    List<Map<String, Object>> getFirstHalfPerformList(Map<String, Object> paraMap);
    int getFirstHalfPerformListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getSecondHalfPerformList(Map<String, Object> paraMap);
    int getSecondHalfPerformListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcStockList(Map<String, Object> paraMap);
    int getProcStockListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> geOwhDateList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrderBookList(Map<String, Object> paraMap);

    Map<String, Object> findByCustNoAndOrdNoAndUsedYn(Map<String, Object> paraMap);
}
