package daedan.mes.prod.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ProdMapper {
    List<Map<String, Object>> getProdList(Map<String, Object> paraMap);
    int getProdListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap);
    int getProdBomListCount(Map<String, Object> paraMap);


    Map<String, Object> getProdInfo(Map<String, Object> paraMap);
    void apndProd(Map<String, Object> paraMap);
    void updtProd(Map<String, Object> paraMap);

    List<Map<String, Object>> getAbleOnProdBomList(Map<String, Object> paraMap);
    int getAbleOnProdBomListCount(Map<String, Object> paraMap);

    void resetLineFeedToSpaceToProdNm();
    void dropZeroBom();

    Map<String, Object> getMonthlyOrderQty(Map<String, Object> paraMap);

    void dropProd(Map<String, Object> paraMap);

    void appendProdInfo(Map<String, Object> map);
    void appendProdBom(Map<String, Object> map);

    int getProdBomCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdCmpyList(Map<String, Object> paraMap);
    int getProdCmpyListCount(Map<String, Object> paraMap);

    Map<String, Object> getProdBomInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdOrdList(Map<String, Object> paraMap);
    int getProdOrdListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getProdStkList(Map<String, Object> paraMap);
    int getProdStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdStatList(Map<String, Object> paraMap);
    int getProdStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getAnaprodList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboProcBrnch(Map<String, Object> paraMap);

    void dropOrdProd(Map<String, Object> paraMap);

    List<Map<String, Object>> getKioProdOwhList(Map<String, Object> paraMap);

    int getKioProdOwhListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getProdByPrefixList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboProdProcList(Map<String, Object> procMap);

    List<Map<String, Object>> getProdExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> getBomExcelList(Map<String, Object> paraMap);

    int getFileNo();

    List<Map<String, Object>> getSendInfoToElecticTagUrlList(Map<String, Object> paraMap);

    int getSendInfoToElecticTagUrlListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdBom(Map<String, Object> paraMap);

    int getListProdBomCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdBomListByIndc(Map<String, Object> paraMap);

    List<Map<String, Object>> getHdfdProdBomList(Map<String, Object> paraMap);

    List<Map<String, Object>> getHdfdProdBomListByIndc(Map<String, Object> paraMap);

    Map<String, Object> getProdStkInfo(Map<String, Object> paraMap);

    Map<String, Object> getBomConsist(Map<String, Object> prodMap);

    List<Map<String, Object>> getSfProdBomList(Map<String, Object> bomchkmap);

    List<Map<String, Object>> getSfProdBomListByIndc(Map<String, Object> paraMap);

    int getHdfdProdBomListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdInspUser(Map<String, Object> paraMap);
}
