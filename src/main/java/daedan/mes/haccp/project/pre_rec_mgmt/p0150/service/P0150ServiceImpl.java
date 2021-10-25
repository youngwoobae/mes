package daedan.mes.haccp.project.pre_rec_mgmt.p0150.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0150.mapper.P0150Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <ul>
 * <li>업무 그룹명 : haccp-smart-module</li>
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0150.service</li>
 * <li>설 명 : P0150Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Service
@Transactional
public class P0150ServiceImpl implements P0150Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0150Mapper p0150Mapper;


    /**
     * 입력
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    @Override
    public Map<String, Object> insert(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<String, Object>();

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

        result.put("updateCnt", p0150Mapper.updateRecPreInfo(param));
        result.put("userId", param.get("userId"));

        return result;
    }



    /**
     * 삭제
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     */
    @Override
    public Map<String, Object> delete(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<String, Object>();

        return result;
    }

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

        cntInfo = p0150Mapper.gridPagingCnt(param);
        totalCount = cntInfo.get("cnt").toString();
        result.put("totalCount", totalCount);
        result.put("data", p0150Mapper.gridPaging(param));

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
        result.put("data", p0150Mapper.selectInfo(param));
        return result;
    }

    /**
     * 항목 selectList
     *
     * @param  param 파라미터
     * @return List<Map<String, Object>> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> selectItemList(Map<String, Object> param) throws SQLException {
        return p0150Mapper.selectItemList(param);
    }

    /**
     * 직전 성적서 등록일
     *
     * @param  param 파라미터
     * @return Map<String, Object> 조회 데이터 맵을 반환 한다.
     * @throws SQLException
     */
    @Override
    public Map<String, Object> selectCreateDt(Map<String, Object> param) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", p0150Mapper.selectCreateDt(param));
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

        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern, Locale.KOREA);
        Date today = Calendar.getInstance().getTime();
        String todayString = df.format(today);
        param.put("todayString", todayString);

        List<Map<String, Object>> selectFinalCheckList = p0150Mapper.selectFinalCheckList(param);
        Map<String, Object> recSeqMap = p0150Mapper.selectCountRecSeq(param);

        for(Map<String, Object> selectFinalCheck : selectFinalCheckList) {
            selectFinalCheck.put("SEQ", Integer.parseInt(recSeqMap.get("cnt").toString()) + 1);
        }
        result.put(param.get("lCode").toString(), selectFinalCheckList);

        return result;
    }
}
