package daedan.mes.haccp.project.pre_rec_mgmt.p0060.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0060.mapper.P0060Mapper;
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
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0060.service</li>
 * <li>설 명 : P0060Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */

@Service
@Transactional
public class P0060ServiceImpl implements P0060Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0060Mapper p0060Mapper;

    /**
     * 기본 해충 정보리스트들을 가져온다.
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectDefaultCheckInfo(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> trap01Kinds = p0060Mapper.selectTrap01KindList(param);
        List<Map<String, Object>> trap02Kinds = p0060Mapper.selectTrap02KindList(param);
        List<Map<String, Object>> trap03Kinds = p0060Mapper.selectTrap03KindList(param);

        result.put("trap01Kinds", trap01Kinds);
        result.put("trap02Kinds", trap02Kinds);
        result.put("trap03Kinds", trap03Kinds);

        List<Map<String, Object>> trap01 = p0060Mapper.selectTrap01List(param);
        param.put("keySize", trap01.size());
        List<Map<String, Object>> trap02 = p0060Mapper.selectTrap02List(param);
        param.put("keySize", trap01.size() + trap02.size());
        List<Map<String, Object>> trap03 = p0060Mapper.selectTrap03List(param);

        result.put("trap01", trap01);
        result.put("trap02", trap02);
        result.put("trap03", trap03);

        Map<String, Object> stdPoint = p0060Mapper.selectStdPoint(param);
        result.put("stdPoint", stdPoint);

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
        List<Map<String, Object>> trap01Kinds = p0060Mapper.selectTrap01KindList(param);
        List<Map<String, Object>> trap02Kinds = p0060Mapper.selectTrap02KindList(param);
        List<Map<String, Object>> trap03Kinds = p0060Mapper.selectTrap03KindList(param);

        result.put("trap01Kinds", trap01Kinds);
        result.put("trap02Kinds", trap02Kinds);
        result.put("trap03Kinds", trap03Kinds);

        List<Map<String, Object>> trap01 = p0060Mapper.selectSavedTrap01List(param);
        List<Map<String, Object>> trap02 = p0060Mapper.selectSavedTrap02List(param);
        List<Map<String, Object>> trap03 = p0060Mapper.selectSavedTrap03List(param);

        result.put("trap01", trap01);
        result.put("trap02", trap02);
        result.put("trap03", trap03);

        Map<String, Object> stdPoint = p0060Mapper.selectStdPoint(param);
        result.put("stdPoint", stdPoint);
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
        log.debug("insert param : " + param.toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> fpGrid = (List<Map<String, Object>>) param.get("fpGrid");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> wpGrid = (List<Map<String, Object>>) param.get("wpGrid");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rGrid = (List<Map<String, Object>>) param.get("rGrid");

        for(Map<String, Object> row : fpGrid) {
            log.debug(row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            p0060Mapper.insertFpGridContents(row);
            if(row.get("breakReason") != null) {
                p0060Mapper.insertImprv(row);
            }

        }
        for(Map<String, Object> row : wpGrid) {
            log.debug(row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            p0060Mapper.insertWpGridContents(row);
            if(row.get("breakReason") != null) {
                p0060Mapper.insertImprv(row);
            }
        }
        for(Map<String, Object> row : rGrid) {
            log.debug(row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));
            p0060Mapper.insertRGridContents(row);
            if(row.get("breakReason") != null) {
                p0060Mapper.insertImprv(row);
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
        Map<String, Object> fpGridModifiedRows =  (Map<String, Object>) param.get("fpGridModifiedRows");
        List<Map<String, Object>> updatedRows =  (List<Map<String, Object>>) fpGridModifiedRows.get("updatedRows");
        for(Map<String, Object> row : updatedRows) {
            log.debug("param test1 " +row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));


            p0060Mapper.updateFpInfo(row);
            cleanImprvOne = p0060Mapper.selectImprvOne(row);
            if(cleanImprvOne != null) {
                if(row.get("breakReason") != null) {
                    p0060Mapper.updateImprv(row);
                } else {
                    p0060Mapper.deleteImprvOne(row);
                }
            } else {
                if(row.get("breakReason") != null) {
                    row.put("newRecNo", param.get("recNo"));
                    p0060Mapper.insertImprv(row);
                }
            }
        }

        Map<String, Object> wpGridModifiedRows =  (Map<String, Object>) param.get("wpGridModifiedRows");
        updatedRows =  (List<Map<String, Object>>) wpGridModifiedRows.get("updatedRows");
        for(Map<String, Object> row : updatedRows) {
            log.debug("param test2 " +row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));

            p0060Mapper.updateWpInfo(row);
            cleanImprvOne = p0060Mapper.selectImprvOne(row);
            if(cleanImprvOne != null) {
                if(row.get("breakReason") != null) {
                    p0060Mapper.updateImprv(row);
                } else {
                    p0060Mapper.deleteImprvOne(row);
                }
            } else {
                if(row.get("breakReason") != null) {
                    row.put("newRecNo", param.get("recNo"));
                    p0060Mapper.insertImprv(row);
                }
            }
        }

        Map<String, Object> rGridModifiedRows =  (Map<String, Object>) param.get("rGridModifiedRows");
        updatedRows =  (List<Map<String, Object>>) rGridModifiedRows.get("updatedRows");
        for(Map<String, Object> row : updatedRows) {
            log.debug("param test3 " +row.toString());
            row.put("userId", param.get("userId"));
            row.put("newRecNo", param.get("newRecNo"));

            p0060Mapper.updateRInfo(row);
            cleanImprvOne = p0060Mapper.selectImprvOne(row);
            if(cleanImprvOne != null) {
                if(row.get("breakReason") != null) {
                    p0060Mapper.updateImprv(row);
                } else {
                    p0060Mapper.deleteImprvOne(row);
                }
            } else {
                if(row.get("breakReason") != null) {
                    row.put("newRecNo", param.get("recNo"));
                    p0060Mapper.insertImprv(row);
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
        p0060Mapper.deleteTrapList(param);
        p0060Mapper.deleteTrapImprv(param);
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

        List<Map<String, Object>> selectFinalCheckList = p0060Mapper.selectFinalCheckList(param);
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }
}
