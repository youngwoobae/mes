package daedan.mes.haccp.project.pre_rec_mgmt.p0010.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0010.mapper.P0010Mapper;
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
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0010.service</li>
 * <li>설 명 : P0010Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */

@Service("p0010Service")
@Transactional
public class P0010ServiceImpl implements P0010Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0010Mapper p0010Mapper;

    /**
     * 리스트 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> cntInfo = null;
        String totalCount = null;

        cntInfo = p0010Mapper.gridPagingCnt(param);
        totalCount = cntInfo.get("cnt").toString();
        result.put("totalCount", totalCount);
        result.put("data", p0010Mapper.gridPaging(param));

        return result;
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
        result.put("selectSavedCheckList", p0010Mapper.selectSavedCheckList(param));
        List<Map<String, Object>> cleanImprv = p0010Mapper.selectPreCleanImprv(param);
        if(cleanImprv != null) {
            result.put("cleanImprv", cleanImprv);
        }
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
        String confYn = "";
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> gridData =  (List<Map<String, Object>>) param.get("gridData");
        for(Map<String, Object> row : gridData) {
            log.debug(row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            p0010Mapper.insertPreClean(row);

            confYn = row.get("confYn").toString();
            if(confYn.equals("N")) {
                p0010Mapper.insertPreCleanImprv(row);
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
        Map<String, Object> cleanImprvOne = new HashMap<String, Object>();
        String confYn = "";
        @SuppressWarnings("unchecked")
        Map<String, Object> modifiedRows =  (Map<String, Object>) param.get("modifiedRows");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> updatedRows =  (List<Map<String, Object>>) modifiedRows.get("updatedRows");

        for(Map<String, Object> row : updatedRows) {
            log.debug("param test " +row.toString());
            row.put("userId", param.get("userId"));
            confYn = row.get("confYn").toString();
            cleanImprvOne = p0010Mapper.selectPreCleanImprvOne(row);
            if(confYn.equals("Y")) {
                p0010Mapper.updatePreClean(row);
                if(cleanImprvOne != null) {
                    p0010Mapper.deletePreCleanImprvOne(row);
                }
            } else if(confYn.equals("N")) {
                p0010Mapper.updatePreClean(row);
                if(cleanImprvOne != null) {
                    p0010Mapper.updatePreCleanImprv(row);
                } else {
                    row.put("newRecNo", param.get("recNo"));
                    p0010Mapper.insertPreCleanImprv(row);
                }
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
        p0010Mapper.deletePreClean(param);
        p0010Mapper.deletePreCleanImprv(param);
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

        List<Map<String, Object>> selectFinalCheckList = p0010Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }

    /**
     * 체크 리스트 가져오기
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> selectCheckList(Map<String, Object> param) throws SQLException {
        return p0010Mapper.selectCheckList(param);
    }
}
