package daedan.mes.haccp.project.haccp_mgmt.c0030.service;

import daedan.mes.haccp.project.haccp_mgmt.c0030.mapper.C0030Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class C0030ServiceImpl implements C0030Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    C0030Mapper c0030Mapper;


    /**
     * GET : 금속 설비 목록 조회
     *
     * @@param HttpServletRequest
     * @return responseEntity JSON
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getCcpDteList(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();

        result.put("data", c0030Mapper.getCcpDteList(param));

        return result;
    }

    /**
     * 감도, 제품 결과 데이터 조회
     *
     * @param  param 조회 파라미터
     * @return Map<String, Object> 반환 Map
     * @throws SQLException
     */
    @Override
    public Map<String, Object> getMdTestProdList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("mdTestData", c0030Mapper.mdTestGrid(param));
        result.put("prodData", c0030Mapper.prodGrid(param));
        result.put("prodBreakCnt", c0030Mapper.getProdBreakCnt(param));
        result.put("mdTestDataImprv", c0030Mapper.getMdTestDataImprv(param));


        return result;
    }

    /**
     * 금속검출 제품이탈에 대한 개선조치시 일지번호의 seq 0번으로 등록
     *
     * @param  param 조회 파라미터
     * @return Map<String, Object> 반환 Map
     * @throws SQLException
     */
    @Override
    public void insertMdProdImprv(Map<String, Object> param) throws SQLException {

        c0030Mapper.insertMdProdImprv(param);
    }

    /**
     * 하나의 일지에 등록된 금속검출 제품 데이터 총 건수를 조회하여 일지 테이블 DATA_CNT 컬럼에 저장
     *
     * @param  param 조회 파라미터
     * @return Map<String, Object> 반환 Map
     * @throws SQLException
     */
    @Override
    public void updateRecDataCnt(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result = c0030Mapper.getMdProdTotCnt(param);

        param.put("prodTotCnt", result.get("prodTotCnt"));

        c0030Mapper.updateRecDataCnt(param);
    }

    /**
     * 설비별 개선조치 미 처리 건수 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> recImprvChk(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", c0030Mapper.recImprvChk(param));
        return result;
    }

    /**
     * 설비별 코드 및 미처리 건수 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> equipImprvMessage(Map<String, Object> param) throws SQLException {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", c0030Mapper.equipImprvMessage(param));

        return result;
    }

    /**
     * 인증원으로 보낼 데이터들을 Map에 담음
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Map<String, Object> lcnsInfo = (Map<String, Object>) param.get("lcnsInfo");
        @SuppressWarnings("unchecked")
        Map<String, Object> recMainInfo = (Map<String, Object>) param.get("recMainInfo");

        param.put("lcnsNo", lcnsInfo.get("lcnsNo"));
        param.put("companyNm", lcnsInfo.get("companyNm"));
        param.put("lCodeNm", recMainInfo.get("lCodeNm"));

        List<Map<String, Object>> selectFinalCheckList = c0030Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        List<Map<String, Object>> selectFinalCheckListImprv = c0030Mapper.selectFinalCheckListImprv(param);
        result.put(param.get("lCode").toString() + "_prod_imprv", selectFinalCheckListImprv);
        return result;
    }
}
