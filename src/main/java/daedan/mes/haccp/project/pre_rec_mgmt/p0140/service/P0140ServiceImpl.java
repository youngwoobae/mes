package daedan.mes.haccp.project.pre_rec_mgmt.p0140.service;

import daedan.mes.haccp.project.pre_rec_mgmt.p0140.mapper.P0140Mapper;
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
 * <li>서브 업무명 : daedan.mes.haccp.project.pre_rec_mgmt.p0140.service</li>
 * <li>설 명 : P0140Service.java</li>
 * <li>작성일 : 2021. 10. 3.</li>
 * <li>작성자 : KMJ</li>
 * </ul>
 */
@Service
@Transactional
public class P0140ServiceImpl implements P0140Service {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    P0140Mapper p0140Mapper;

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
     */
    @Override
    public Map<String, Object> update(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<String, Object>();

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

        List<Map<String, Object>> selectFinalCheckList = p0140Mapper.selectFinalCheckList(param);
        Map<String, Object> recSeqMap = p0140Mapper.selectCountRecSeq(param);

        for(Map<String, Object> selectFinalCheck : selectFinalCheckList) {
            selectFinalCheck.put("SEQ", Integer.parseInt(recSeqMap.get("cnt").toString()) + 1);
        }

        result.put(param.get("lCode").toString(), selectFinalCheckList);
        return result;
    }
}
