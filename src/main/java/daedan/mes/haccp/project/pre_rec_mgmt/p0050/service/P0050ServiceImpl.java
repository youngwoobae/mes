package daedan.mes.haccp.project.pre_rec_mgmt.p0050.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0050.mapper.P0050Mapper;
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
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0050.service</li>
 * <li>설 명 : P0050Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */

@Service
@Transactional
public class P0050ServiceImpl {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0050Mapper p0050Mapper;


    /**
     * 입력
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public Map<String, Object> insert(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> gridData =  (List<Map<String, Object>>) param.get("gridData");
        for(Map<String, Object> row : gridData) {
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));

            if(row.get("measVal") != null && !"".equals(row.get("measVal"))) {
                row.put("measVal", Float.parseFloat(row.get("measVal").toString()));
            }

            if(row.get("measVal1") != null && !"".equals(row.get("measVal1"))) {
                row.put("measVal1", Float.parseFloat(row.get("measVal1").toString()));
            }

            p0050Mapper.insertPreDegree(row);

            //개선사항있으면 저장
            if(row.get("imprv") != null) {
                p0050Mapper.insertPreImprv(row);
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
    @SuppressWarnings("unchecked")
    public Map<String, Object> update(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> modifiedRows =  (Map<String, Object>) param.get("modifiedRows");
        List<Map<String, Object>> updatedRows =  (List<Map<String, Object>>) modifiedRows.get("updatedRows");
        for(Map<String, Object> row : updatedRows) {

            if(row.get("measVal") != null && !"".equals(row.get("measVal"))) {
                row.put("measVal", Float.parseFloat(row.get("measVal").toString()));
            }

            if(row.get("measVal1") != null && !"".equals(row.get("measVal1"))) {
                row.put("measVal1", Float.parseFloat(row.get("measVal1").toString()));
            }

            p0050Mapper.updatePreDegree(row);
        }
        for(int i=0; i<updatedRows.size(); i++) {
            if(updatedRows.get(i).get("imprv")!= null) {
                Map<String, Object> preImprv = p0050Mapper.selectPreImprv(updatedRows.get(i));
                if(preImprv != null) {
                    p0050Mapper.updatePreImprv(updatedRows.get(i));
                } else {
                    param.put("seq", param.get("seq"));
                    p0050Mapper.insertPreImprv2(updatedRows.get(i));
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
    public Map<String, Object> delete(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        p0050Mapper.deletePreDegree(param);
        p0050Mapper.deletePreImprv(param);
        return result;
    }


    /**
     * 리스트 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public Map<String, Object> selectDegreeList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("col", p0050Mapper.selectColList(param));
        return result;
    }

    public Map<String, Object> selectList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> cntInfo = p0050Mapper.gridPagingCnt(param);
        String totalCount = cntInfo.get("cnt").toString();

        result.put("totalCount", totalCount);
        result.put("data", p0050Mapper.gridPaging(param));
        return result;
    }

    /**
     * 추가 정보 조회
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("cleanImprv", p0050Mapper.selectInfo(param));
        return result;
    }

    /**
     * 작업장 리스트
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public List<Map<String, Object>> selectPlaceList(Map<String, Object> param) throws SQLException {
        return p0050Mapper.electPlaceList(param);
    }

    /**
     * 인증원으로 보낼 데이터들을 Map에 담음
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    public Map<String, Object> sendInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Map<String, Object> lcnsInfo = (Map<String, Object>) param.get("lcnsInfo");
        @SuppressWarnings("unchecked")
        Map<String, Object> recMainInfo = (Map<String, Object>) param.get("recMainInfo");

        param.put("lcnsNo", lcnsInfo.get("lcnsNo"));
        param.put("companyNm", lcnsInfo.get("companyNm"));
        param.put("lCodeNm", recMainInfo.get("lCodeNm"));

        List<Map<String, Object>> selectFinalCheckList = p0050Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }
}
