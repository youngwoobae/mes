package daedan.mes.haccp.project.pre_rec_mgmt.p0110.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0110.mapper.P0110Mapper;
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
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0110.service</li>
 * <li>설 명 : P0110Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Service
@Transactional
public class P0110ServiceImpl implements P0110Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0110Mapper p0110Mapper;


    /**
     * 입력
     *
     * @param param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> insert(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> gridData = (List<Map<String, Object>>) param.get("gridData");
        for (Map<String, Object> row : gridData) {
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            if (row.get("measHh") != null) { // 오전
                row.put("measHh", row.get("measHh").toString());
                row.put("measMm", row.get("measMm").toString());
                p0110Mapper.insertPreCool(row);
            }
            // 개선사항있으면 저장
            if (row.get("imprv") != null && !"".equals(row.get("imprv"))) {
                p0110Mapper.insertPreImprv(row);
            }
            if (row.get("measHh2") != null) { // 오후
                row.put("measHh", row.get("measHh2").toString());
                row.put("measMm", row.get("measMm2").toString());
                row.put("measVal", row.get("measVal2").toString());
                p0110Mapper.insertPreCool(row);
            }

        }
        return result;
    }

    /**
     * 수정
     *
     * @param param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> update(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> modifiedRows = (Map<String, Object>) param.get("modifiedRows");
        List<Map<String, Object>> updatedRows = (List<Map<String, Object>>) modifiedRows.get("updatedRows");
        for (Map<String, Object> row : updatedRows) {
            log.debug("param test " + row.toString());
//			mybatisWebDAO.update(mapper + "updatePreCool", row);
            if (row.get("measHh") != null) { // 오전
                row.put("ordSeq", "1");
                row.put("measHh", row.get("measHh").toString());
                row.put("measMm", row.get("measMm").toString());
                p0110Mapper.updatePreCool(row);
            }
            if (row.get("measHh2") != null) { // 오후
                int seq2 = (int) row.get("seq");
                row.put("seq", seq2 + 1);
                row.put("ordSeq", "2");
                row.put("measHh2", row.get("measHh2").toString());
                row.put("measMm2", row.get("measMm2").toString());
                row.put("measVal2", row.get("measVal2").toString());
                p0110Mapper.updatePreCool2(row);
                row.put("seq", seq2);
            }
        }
        for (int i = 0; i < updatedRows.size(); i++) {
            if (updatedRows.get(i).get("imprv") != null && !"".equals(updatedRows.get(i).get("imprv"))) {
                Map<String, Object> preImprv = p0110Mapper.selectPreImprv(updatedRows.get(i));
                if (preImprv != null) {
                    p0110Mapper.updatePreImprv(updatedRows.get(i));
                } else {
                    param.put("seq", param.get("seq"));
                    p0110Mapper.insertPreImprv2(updatedRows.get(i));
                }
            }
        }
        return result;
    }

    /**
     * 삭제
     *
     * @param param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> delete(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        p0110Mapper.deletePreCool(param);
        p0110Mapper.deletePreImprv(param);
        return result;
    }

    /**
     * 작업장 리스트
     *
     * @param param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> selectPlaceList(Map<String, Object> param) throws SQLException {
        return p0110Mapper.selectPlaceList(param);
    }
    @Override
    public Map<String, Object> selectList(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> cntInfo = p0110Mapper.gridPagingCnt(param);
        String totalCount = cntInfo.get("cnt").toString();

        result.put("totalCount", totalCount);
        result.put("data", p0110Mapper.gridPaging(param));
        return result;
    }

    /**
     * 추가 정보 조회
     *
     * @param param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("selectSavedCheckList", p0110Mapper.selectSavedCheckList(param));
        return result;
    }

    /**
     * 인증원으로 보낼 데이터들을 Map에 담음
     *
     * @param param 파라미터
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

        List<Map<String, Object>> selectFinalCheckList = p0110Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);
        return result;
    }
}
