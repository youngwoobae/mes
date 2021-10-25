package daedan.mes.ord.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface OrdService {
    List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap);
    int getOrdProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdProdCartList(Map<String, Object> paraMap);
    int getOrdProdCartListCount(Map<String, Object> paraMap);

    void apndOrdProdToCart(Map<String, Object> paraMap);

    void saveOrdProdCart(Map<String, Object> paraMap);

    @Transactional
    void saveOrdProd(Map<String, Object> paraMap);

    void dropOrdProdCarts(Map<String, Object> paraMap); //다중일괄삭제
    void dropOrdProdCart(Map<String, Object> paraMap);  //개별삭제

    void initOrdCart(Map<String, Object> paraMap);

    @Transactional
    void addOnProdToCart(Map<String, Object> paraMap);

    void moveOrdProdToCart(Map<String, Object> paraMap);
    Map<String, Object> getOrdInfo(Map<String, Object> paraMap);

    Map<String, Object> getOemOrdInfo(Map<String, Object> paraMap);
    Map<String, Object> getPrjOrdInfo(Map<String, Object> paraMap);

    void saveOrd(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboDlvPlc(Map<String, Object> paraMap);

    @Transactional
    void updateOrd(Map<String, Object> paraMap) ;

    @Transactional
    void dropOrdInfo(Map<String, Object> paraMap);

    @Transactional
    void resetPursNo(Map<String, Object> pursMap);

    @Transactional
    void initOrdProdCart(Map<String, Object> pursMap);

    List<Map<String, Object>> getOrdProdBomList(Map<String, Object> paraMap);
    int getOrdProdBomListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdProdCartBomList(Map<String, Object> paraMap);
    int getOrdProdCartBomListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getOrdList(Map<String, Object> paraMap);
    int getOrdListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getExOrdProdList(Map<String, Object> paraMap);
    Map<String, Object> getOrdProdInfo(Map<String, Object> paraMap);

    int getExOrdProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboDlvPlcList(Map<String, Object> paraMap);

    void apndOrdProd(Map<String, Object> paraMap);
    void dropOrdProds(Map<String, Object> paraMap);

    void resetOrdStatus(Map<String, Object> ordmap);

    List<Map<String, Object>> getFirstHalfPerformList(Map<String, Object> paraMap);
    int getFirstHalfPerformListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getSecondHalfPerformList(Map<String, Object> paraMap);
    int getSecondHalfPerformListCount(Map<String, Object> paraMap);

    void ordIndfoByExcel(Map<String, Object> paraMap) throws Exception ;

    List<Map<String, Object>> getProcStockList(Map<String, Object> paraMap);

    int getProcStockListCount(Map<String, Object> paraMap);

    void chkSubMatr(Map<String, Object> paraMap);

    void saveMakeAbleYn(Map<String, Object> paraMap);

    void updateDlvReqDt(Map<String, Object> paraMap);

    List<Map<String, Object>> geOwhDateList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrderBookList(Map<String, Object> paraMap);
}

