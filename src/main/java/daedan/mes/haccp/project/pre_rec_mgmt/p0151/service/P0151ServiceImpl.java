package daedan.mes.haccp.project.pre_rec_mgmt.p0151.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0151.mapper.P0151Mapper;
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

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0151.service</li>
 * <li>설 명 : P0151Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Service
@Transactional
public class P0151ServiceImpl implements P0151Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0151Mapper p0151Mapper;


    /**
     * 품목 코드조회
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> selectColList(Map<String, Object> param) throws SQLException {
        return p0151Mapper.selectColList(param);
    }

    /**
     * 추가 정보 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("selectSavedCheckList", p0151Mapper.selectSavedCheckList(param));
        return result;
    }

    /**
     * 입력
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> insert(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> gridData =  (List<Map<String, Object>>) param.get("gridData");
        for(Map<String, Object> row : gridData) {
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            row.put("itemCodeKind", param.get("itemCodeKind"));
            p0151Mapper.insertPreVt(row);
            if("N".equals(row.get("sutYn"))) {
                p0151Mapper.insertPreImprv(row);
            }

        }

        return result;
    }

    /**
     * 수정
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> update(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> modifiedRows =  (Map<String, Object>) param.get("modifiedRows");
        List<Map<String, Object>> updatedRows =  (List<Map<String, Object>>) modifiedRows.get("updatedRows");
        List<Map<String, Object>> createdRows =  (List<Map<String, Object>>) modifiedRows.get("createdRows");
        List<Map<String, Object>> deletedRows =  (List<Map<String, Object>>) modifiedRows.get("deletedRows");

        if(deletedRows.size() > 0) {
            for(Map<String, Object> row : deletedRows) {
                p0151Mapper.deletePreVt(row);
                p0151Mapper.deletePreImprv(row);
            }
        }

        for(Map<String, Object> row : updatedRows) {
            row.put("userId", param.get("userId"));
            row.put("itemCodeKind", param.get("itemCodeKind"));
            p0151Mapper.updatePreVt(row);
            p0151Mapper.updatePreImprv(row);

        }

        for(Map<String, Object> row : createdRows) {
            row.put("newRecNo", param.get("recNo"));
            row.put("userId", param.get("userId"));
            row.put("itemCodeKind", param.get("itemCodeKind"));
            p0151Mapper.insertPreVt(row);
            if("N".equals(row.get("sutYn"))) {
                p0151Mapper.insertPreImprv(row);
            }
        }

        return result;
    }

    /**
     * 삭제
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> delete(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        p0151Mapper.deletePreVtAll(param);
        p0151Mapper.deletePreImprvAll(param);
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

        List<Map<String, Object>> selectFinalCheckList = p0151Mapper.selectFinalCheckList(param);

        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }
}
