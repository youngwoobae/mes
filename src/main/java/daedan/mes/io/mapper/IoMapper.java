package daedan.mes.io.mapper;

import daedan.mes.io.domain.ProdOwh;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface IoMapper {
    List<Map<String,Object>> getComboWhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getWhList(Map<String, Object> paraMap);

    int getWhListCount(Map<String, Object> paraMap);

    Map<String, Object> getWhInfo(Map<String, Object> paraMap);

    void saveWhInfo(Map<String, Object> paraMap);
    void dropWhInfo(Map<String, Object> paraMap);

    int getInstkQty(Map<String, Object> passMap);

    List<Map<String, Object>> getWaitMatrIwhList(Map<String, Object> paraMap);
    int getWaitMatrIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap);
    int getMatrIwhListCount(Map<String, Object> paraMap);

    Map<String, Object> getMatrIwhInfo(Map<String, Object> paraMap);

    void dropMatrIwh(Map<String, Object> paraMap);


    String getCnfmAbleIwh(Map<String, Object> paraMap);

    void cnfmIwh(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrStkList(Map<String, Object> paraMap);

    int getMatrStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrStkIwhList(Map<String, Object> paraMap);
    int getMatrStkIwhListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getMatrStkOwhList(Map<String, Object> paraMap);
    int getMatrStkOwhListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getReqOwhList(Map<String, Object> paraMap);
    int getReqOwhListCount(Map<String, Object> paraMap);

    Map<String, Object> matrOwhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboOwhReqDtList(Map<String, Object> paraMap);

    void dropMatrOwh(Map<String, Object> paraMap);

    //자재입고요청 처리용 시작
    List<Map<String, Object>> getReqMatrIwhList(Map<String, Object> paraMap);
    int getReqMatrIwhListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getReqMatrIwhSumList(Map<String, Object> paraMap);
    int getReqMatrIwhSumListCount(Map<String, Object> paraMap);
    //자재입고요청 처리용 끝.

    //자재출고요청 처리용 시작
    List<Map<String, Object>> getReqMatrOwhList(Map<String, Object> paraMap);
    int getReqMatrOwhListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getReqMatrOwhSumList(Map<String, Object> paraMap);
    int getReqMatrOwhSumListCount(Map<String, Object> paraMap);
    //자재출고요청 처리용 끝

    Map<String, Object> getReqMatrIwhInfo(Map<String, Object> paraMap);

    void updatePursStatus(Map<String, Object> paraMap);

    String pursStsCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboMatrWhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIwhList(Map<String, Object> paraMap);
    int getProdIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdIwhSumList(Map<String, Object> paraMap);
    int getReqProdIwhSumListCount(Map<String, Object> paraMap);

    Map<String, Object> getProdIwhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdOwhList(Map<String, Object> paraMap);
    int getReqProdOwhListCount(Map<String, Object> paraMap);

    void addProdStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcList(Map<String, Object> paraMap);
    int getMakeIndcListCount(Map<String, Object> paraMap);

    void saveProdStkQty(Map<String, Object> map);

    void saveKioMatrStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getKioMatrIwhList(Map<String, Object> paraMap);
    int getKioMatrIwhListCount(Map<String, Object> paraMap);

    //출하계획
    List<Map<String, Object>> getProdOutPlanList(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdOwhSumList(Map<String, Object> paraMap);
    int getReqProdOwhSumListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrPosList(Map<String, Object> paraMap);
    int getMatrPosListCount(Map<String, Object> paraMap);

    Map<String, Object> getMatrPosInfo(Map<String, Object> paraMap);

    void initTempMatrPos(Map<String, Object> imap);

    List<Map<String, Object>> getMatrStkPosList(Map<String, Object> paraMap);

    int getMatrStkPosListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatchPosList(Map<String, Object> paraMap);

    int getMatchPosListCount(Map<String, Object> paraMap);

    void renewalMatrPos(Map<String, Object> paraMap);

    void outMatrPos(Map<String, Object> paraMap);

    void updateMatrOwhStk(Map<String, Object> paraMap);

    void updateMatrOwh(Map<String, Object> paraMap);

    void renewalPursMatr(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrStkImpoList(Map<String, Object> paraMap);
    int getMatrStkImpoListCount(Map<String, Object> paraMap);

    Map<String, Object> getExportPlanInfo(Map<String, Object> paraMap);
    Map<String, Object> getExportExecInfo(Map<String, Object> paraMap);
    List<Map<String, Object>> getExportProdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getForImportProdList(Map<String, Object> paraMap);
    int getForImportProdListCount(Map<String, Object> paraMap);

    void resetProdStk(Map<String, Object> ordMap);
    void resetOrdStatusByIwh(Map<String, Object> stsmap);

    List<Map<String, Object>> getOwhWaitOrdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getTransStkQty(Map<String, Object> paraMap);

    boolean getOwhWaitOrdCondition(Map<String, Object> el);

    List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOwhCompleteOrdList(Map<String, Object> paraMap);

    boolean getOwhCompleteOrdCondition(Map<String, Object> paraMap);

    List<Map<String, Object>> getTransOwhQty(Map<String, Object> paraMap);

    void resetOrdStatusByOwh(Map<String, Object> comDsMap);

    void updateMatrStkStat(Map<String, Object> dmap); //테블릿 창고 적재 상태 변경.

    List<Map<String, Object>> getTabletReqMatrIwhList(Map<String, Object> paraMap);
    int getTabletReqMatrIwhListCount(Map<String, Object> paraMap);

    void updateTabletPursMatr(Map<String, Object> dmap);

    void updateTabletPursSts(Map<String, Object> dmap);

    List<Map<String, Object>> getTabletWhNoAndMatrUnit(Map<String, Object> paraMap);

    void checkMatrSts(Map<String, Object> paraMap);

    void updatePursMatrWhNo(Map<String, Object> dmap);

    Map<String, Object> getIndcRsltNo(Object indc_no);

    int getOrdProdListCount(Map<String, Object> paraMap);

    boolean getOwhCompleteOneOrdCondition(Map<String, Object> paraMap);

    void saveMatrIwh(Map<String, Object> voToMap);

    void renewalProdStockByIOwh(Map<String, Object> paraMap);

    List<Map<String, Object>> OwhWhStkList(Map<String, Object> paraMap);

    void ordStkCmpyNoUpdate(Map<String, Object> paraMap);
    //테블릿 자재 입고 목록
    List<Map<String, Object>> getMatrForIwhList(Map<String, Object> paraMap);
    int getMatrForIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrForIwhListByT(Map<String, Object> paraMap); //AddOn By KMJ AT 21.11.22 - getMatrForIwhList 대체용
    int getMatrForIwhListByTCount(Map<String, Object> paraMap); //AddOn By KMJ AT 21.11.22 - getMatrForIwhListCount 대체용

    // 테블릿 자재 출고 목록
    List<Map<String, Object>> getMatrForOwhList(Map<String, Object> paraMap);
    int getMatrForOwhListCount(Map<String, Object> paraMap);

    // 테블릿 제품 입고 목록
    List<Map<String, Object>> getProdForIwhList(Map<String, Object> paraMap);
    int getProdForIwhListCount(Map<String, Object> paraMap);

    // 테블릿 제품 출고 목록
    List<Map<String, Object>> getProdForOwhList(Map<String, Object> paraMap);
    int getProdForOwhListCount(Map<String, Object> paraMap);

    //제품 입고 현황
    List<Map<String, Object>> getStsProdIwhList(HashMap<String, Object> paraMap);
    int getStsProdIwhListCount(HashMap<String, Object> paraMap);
    //제품 출고 현황
    List<Map<String, Object>> getStsProdOwhList(HashMap<String, Object> paraMap);
    int getStsProdOwhListCount(HashMap<String, Object> paraMap);
    //자제 입고 현황
    List<Map<String, Object>> getStsMatrIwhList(HashMap<String, Object> paraMap);
    int getStsMatrIwhListCount(HashMap<String, Object> paraMap);
    //자제 출고 현황
    List<Map<String, Object>> getStsMatrOwhList(HashMap<String, Object> paraMap);
    int getStsMatrOwhListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwhWhNm(HashMap<String, Object> paraMap);



    List<Map<String, Object>> getProdOwhCount(ProdOwh owhvo);

    List<Map<String, Object>> getProdOwHstrList(Map<String, Object> paraMap);
    int getProdOwHstrListCount(Map<String, Object> paraMap);

    Map<String, Object> getOwhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrWhNm(HashMap<String, Object> paraMap);

    int getmatrWhNmCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getMatrWhStkList(Map<String, Object> paraMap);

    int getMatrWhStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getIwhMatrList(HashMap<String, Object> paraMap);

    int getIwhMatrListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getProdWhNm(HashMap<String, Object> paraMap);

    int getProdWhNmCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getProdWhStkList(Map<String, Object> paraMap);

    int getProdWhStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getIwhProdList(HashMap<String, Object> paraMap);

    int getIwhProdListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getOwhProdList(HashMap<String, Object> paraMap);

    int getOwhProdListCount(HashMap<String, Object> paraMap);

    void setOrdProdOwhChk(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhListByOwh(Map<String, Object> rmap);

    List<Map<String, Object>> getOrdProdOwhNo(ProdOwh owhvo);

    void updateOrdProdUsedYn(Map<String, Object> el);

    List<Map<String, Object>> getProdIwHstrList(Map<String, Object> paraMap);

    int getProdIwHstrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwHstrList(Map<String, Object> paraMap);

    int getMatrIwHstrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwHstrList(Map<String, Object> paraMap);

    int getMatrOwHstrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursMatrList(Map<String, Object> paraMap);

    Map<String ,Object> chkMatrListByIwh(Map<String, Object> paraMap);

    void resetPursStsIng(Map<String, Object> rmap);

    List<Map<String, Object>> getDaedongReqProdIwhSumList(Map<String, Object> paraMap);

    int getDaedongReqProdIwhSumListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdQty(Map<String, Object> paraMap);

    Map<String, Object> getOrdQtyRandom(Map<String, Object> ran);

    Map<String, Object> getPursMatrInfo(Map<String, Object> paraMap);

    void resetMatrStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getIwhHsrt(Map<String, Object> paraMap);

    int getIwhHsrtCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboEmbIwhWhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwhHistList(Map<String, Object> paraMap);

    int getMatrOwhHistListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwhList(HashMap<String, Object> paraMap);
    int getMatrOwhListCount(HashMap<String, Object> paraMap);

    void resetPursStsSet(Map<String, Object> paraMap);

    List<Map<String, Object>> getOwhMatrList(Map<String, Object> paraMap);

    int getOwhMatrListCount(Map<String, Object> paraMap);

    void changeStkData(Map<String, Object> paraMap);

    void changeTotalStkData(Map<String, Object> paraMap);

    List<Map<String, Object>> getMadeProdForIwhList(Map<String, Object> paraMap);
    int getMadeProdForIwhListCount(Map<String, Object> paraMap);
    Map<String, Object> getProdLotSummary(Map<String, Object> paraMap);
}

