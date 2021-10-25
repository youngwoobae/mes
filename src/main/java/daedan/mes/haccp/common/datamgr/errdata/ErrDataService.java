package daedan.mes.haccp.common.datamgr.errdata;

import daedan.mes.haccp.common.datamgr.DataMgrVO;
import daedan.mes.haccp.common.datamgr.rawdata.RawDataMapper;
import daedan.mes.haccp.common.error_handle.CustomErrorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ErrDataService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RawDataMapper rawDataMapper;

    @Autowired
    ErrDataMapper errDataMapper;

    @Autowired
    DataMgrVO dataMgrVo;

    /**
     * 가열지속 (가열지속 이벤트중일경우 온도, 속도 값을 이탈검증)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     */
    public void heatingContinuing(List<Map<String, Object>> paramList) throws SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                paramList.get(i).put("user_input_yn", "N");
                if ("Y".equals(paramList.get(i).get("eventYn").toString())) { // 가열 지속 이벤트 중 일경우 이탈검증
                    heatingCommonVerf(paramList.get(i)); // 가열 공통 이탈처리
                }
            }
        }
    }

    /**
     * 가열 이벤트 (가열 이벤트중이고, 공정시간 중 일경우 온도값을 이탈검증)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws ParseException
     * throws SQLException
     */

    public void heatingEvent(List<Map<String, Object>> paramList) throws ParseException, SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int idx = 0; idx < paramList.size(); idx++) {
                paramList.get(idx).put("user_input_yn", "N");

                // 가열 이벤트 중 일경우
                if (null != paramList.get(idx).get("eventYn") && "Y".equals(paramList.get(idx).get("eventYn").toString())) {
                    // 마지막으로 입력된 값의 공정시간을 조회
                    //Map<String, Object> lastPrcTimeMap = mybatisWebDAO.selectOne("rawdata.getRecDescPrcTime",paramList.get(i));
                    Map<String, Object> lastPrcTimeMap = rawDataMapper.getRecDescPrcTime(paramList.get(idx));

                    // 공정시간이 null이고 측정온도가 최저점이상일경우 새로운 공정시작
                    if ((null == lastPrcTimeMap
                            || null == lastPrcTimeMap.get("prcTime"))
                            && "01".equals(paramList.get(idx).get("lmtItemCode").toString())
                            && Float.parseFloat(paramList.get(idx).get("temperMeasVal").toString()) >= Float.parseFloat(paramList.get(idx).get("lmtMinVal").toString())) {
                        paramList.get(idx).put("event_yn", "Y");
                        // 설비에 설정된 공정시간을 조회
                        paramList.get(idx).put("update_prc_time",Integer.parseInt(paramList.get(idx).get("prcTime").toString()) * 60);
                        rawDataMapper.updateRawDataPrcTime(paramList.get(idx)); //남은 공정시간을 적용


                        // 가열 공통 이탈 검사 함수 호출
                        heatingCommonVerf(paramList.get(idx));
                        // 공정시간이 NULL이 아니고 0보다 크면 공정중
                    }
                    else if ((null != lastPrcTimeMap
                            && null != lastPrcTimeMap.get("prcTime"))
                            && Integer.parseInt(lastPrcTimeMap.get("prcTime").toString()) > 0) {
                        paramList.get(idx).put("event_yn", "Y");

                        // 마지막 측정시간과 수신된 측정시간을 빼기한 값만큼 마지막 공정시간에서 뺀후 prctime 등록
                        String diffTime = timesDiff(lastPrcTimeMap.get("measTime").toString(),paramList.get(idx).get("measTime").toString());
                        int remainingTime = Integer.parseInt(lastPrcTimeMap.get("prcTime").toString()) - Integer.parseInt(diffTime);

                        // 공정중
                        if (remainingTime > 0) {
                            // 남은 공정시간을 적용
                            paramList.get(idx).put("update_prc_time", remainingTime);
                            rawDataMapper.updateRawDataPrcTime(paramList.get(idx));

                            // 가열 공통 이탈 검사 함수 호출
                            heatingCommonVerf(paramList.get(idx));
                            // 공정종료
                        }
                        else {
                            // 이벤트 종료, 공정시간 종료
                            paramList.get(idx).put("event_yn", "N");
                            paramList.get(idx).put("insert_prc_time", null);
                            rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                            errDataMapper.updateRecCCpEventFlag(paramList.get(idx));
                        }
                        // 공정중이 아님
                    }
                    else {
//						paramList.get(i).put("event_yn", "N");
                        paramList.get(idx).put("update_prc_time", null);
                        rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                        errDataMapper.updateRecCCpEventFlag(paramList.get(idx));
                    }
                    // 공정중이 아님
                } else {
//					paramList.get(i).put("event_yn", "N");
                    paramList.get(idx).put("update_prc_time", null);
                    rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                }
                rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
            }
        }
    }

    /**
     * 가열 품온 (온도값을 이탈검증)
     *
     * param ListMap 조회 파라미터
     * return void
     */
    public void heatingMaterial(List<Map<String, Object>> paramList) {
        try {
            if (null != paramList && paramList.size() > 0) {
                for (int idx = 0; idx < paramList.size(); idx++) {

                    paramList.get(idx).put("user_input_yn", "N");

                    // 가열 이탈검증 공통
                    heatingCommonVerf(paramList.get(idx));
                    rawDataMapper.updateRawDataPrcTime( paramList.get(idx));

                    // 일지 데이터 테이블에 등록
                    if (idx == 0) {
                        errDataMapper.insertShcRecCcpDesc(paramList.get(idx));
                    }
                }
            }
        } catch (SQLException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        }
    }

    /**
     * 가열 압력 이벤트 (가열 압력 이벤트 중이고 공정시간내에 온도, 압력값을 이탈검증)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     * throws ParseException
     */

    public void heatingPressureEvent(List<Map<String, Object>> paramList) throws SQLException, ParseException {
        if (null != paramList && paramList.size() > 0) {
            // 리스트에 압력이 0번 행에 있을경우 0번행을 압력에서 온도로 변환 (온도가 최저점 이상일 경우 공정을 시작하므로 온도값이 먼저 처리되어야 함)
            if (null != paramList && paramList.size() >= 2) {
                if (!"01".equals(paramList.get(0).get("lmtItemCode"))
                        && "03".equals(paramList.get(0).get("lmtItemCode"))) {
                    List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                    tempList.add(0, paramList.get(1));
                    tempList.add(1, paramList.get(0));
                    paramList = tempList;
                }
            }

            for (int idx = 0; idx < paramList.size(); idx++) {

                paramList.get(idx).put("user_input_yn", "N");
                // 가열 이벤트 중 일경우
                if (null != paramList.get(idx).get("eventYn") && "Y".equals(paramList.get(idx).get("eventYn").toString())) {
                    Map<String, Object> lastPrcTimeMap = rawDataMapper.getRecDescPrcTime(paramList.get(idx)); // 마지막으로 입력된 값의 공정시간을 조회
                    if (( null == lastPrcTimeMap || null == lastPrcTimeMap.get("prcTime")) // 공정시간이 null이고 측정온도가 최저점이상일경우 새로운 공정시작
                               && "01".equals(paramList.get(idx).get("lmtItemCode").toString())
                               && Float.parseFloat(paramList.get(idx).get("temperMeasVal").toString()) >= Float.parseFloat(paramList.get(idx).get("lmtMinVal").toString())) {
                        paramList.get(idx).put("event_yn", "Y");

                        // 설비에 설정된 공정시간을 조회
                        paramList.get(idx).put("update_prc_time",Integer.parseInt(paramList.get(idx).get("prcTime").toString()) * 60);

                        // 남은 공정시간을 적용
                        rawDataMapper.updateRawDataPrcTime(paramList.get(idx));

                        // 가열 공통 이탈 검사 함수 호출
                        heatingCommonVerf(paramList.get(idx));
                    }
                    else if ((    null == lastPrcTimeMap || null == lastPrcTimeMap.get("prcTime"))
                               && "03".equals(paramList.get(idx).get("lmtItemCode").toString())
                               && Float.parseFloat(paramList.get(idx).get("temperMeasVal").toString()) >= Float.parseFloat(paramList.get(idx - 1).get("lmtMinVal").toString())) {
                        paramList.get(idx).put("event_yn", "Y");

                        // 설비에 설정된 공정시간을 조회
                        paramList.get(idx).put("update_prc_time",Integer.parseInt(paramList.get(idx).get("prcTime").toString()) * 60);

                        // 남은 공정시간을 적용
                        rawDataMapper.updateRawDataPrcTime(paramList.get(idx));

                        // 가열 공통 이탈 검사 함수 호출
                        heatingCommonVerf(paramList.get(idx));
                        // 공정시간이 NULL이 아니고 0보다 크면 공정중
                    }
                    else if ((     null != lastPrcTimeMap
                                && null != lastPrcTimeMap.get("prcTime"))
                                && Integer.parseInt(lastPrcTimeMap.get("prcTime").toString()) > 0) {
                        paramList.get(idx).put("event_yn", "Y");

                        // 마지막 측정시간과 수신된 측정시간을 빼기한 값만큼 마지막 공정시간에서 뺀후 prctime 등록
                        String diffTime = timesDiff(lastPrcTimeMap.get("measTime").toString(), paramList.get(idx).get("measTime").toString());
                        int remainingTime = Integer.parseInt(lastPrcTimeMap.get("prcTime").toString()) - Integer.parseInt(diffTime);


                        if (remainingTime > 0) { // 공정중
                            paramList.get(idx).put("update_prc_time", remainingTime); // 남은 공정시간을 적용
                            rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                            heatingCommonVerf(paramList.get(idx)); // 이탈검증
                        // 공정종료
                        }
                        else { // 공정중이 아님
                            // 이벤트 종료, 공정시간 종료
                            paramList.get(idx).put("event_yn", "N");
                            paramList.get(idx).put("insert_prc_time", null);
                            rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                            errDataMapper.updateRecCCpEventFlag(paramList.get(idx));
                        }

                    }
                    else { // 공정중이 아님
//							paramList.get(i).put("event_yn", "N");
                        paramList.get(idx).put("update_prc_time", null);
                        rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                        errDataMapper.updateRecCCpEventFlag(paramList.get(idx));
                    }
                }
                else {
//					paramList.get(i).put("event_yn", "N");
                    paramList.get(idx).put("update_prc_time", null);
                    rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
                }
                rawDataMapper.updateRawDataPrcTime(paramList.get(idx));
            }
        }
    }

    /**
     * 가열 이탈처리 공통 함수 (온도, 속도, 압력값이 최저값, 최고값을 사이를 이탈하였는지 검증하는 공통 가열 이탈검증 함수)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     */

    public void heatingCommonVerf(Map<String, Object> paramMap) throws SQLException {
        paramMap.put("event_seq", null);

        // 아이템 코드
        String lmtItemCode = null;
        // 최저점 값
        Float lmtMinVal = null;
        // 최고점 값
        Float lmtMaxVal = null;
        // 온도 값
        Float temperMeasVal = null;
        // 속도 값
        Float speedMeasVal = null;
        // 압력 값
        Float pressureMeasVal = null;
        String breakYn = "N";

        if (null != paramMap.get("lmtItemCode")) {
            lmtItemCode = paramMap.get("lmtItemCode").toString();
        }
        if (null != paramMap.get("lmtMinVal")) {
            lmtMinVal = Float.parseFloat(paramMap.get("lmtMinVal").toString());
        }
        if (null != paramMap.get("lmtMaxVal")) {
            lmtMaxVal = Float.parseFloat(paramMap.get("lmtMaxVal").toString());
        }
        if (null != paramMap.get("temperMeasVal")) {
            temperMeasVal = Float.parseFloat(paramMap.get("temperMeasVal").toString());
        }
        if (null != paramMap.get("speedMeasVal")) {
            speedMeasVal = Float.parseFloat(paramMap.get("speedMeasVal").toString());
        }
        if (null != paramMap.get("pressureMeasVal")) {
            pressureMeasVal = Float.parseFloat(paramMap.get("pressureMeasVal").toString());
        }

        if (null != lmtItemCode && null != lmtMinVal && null != lmtMaxVal) {
            // 온도 이탈 하였을 경우
            if ((null != temperMeasVal && "01".equals(lmtItemCode))
                    && (lmtMinVal > temperMeasVal || lmtMaxVal < temperMeasVal)) {
                paramMap.put("break_reason", "온도이탈");
                breakYn = "Y";
                // 속도 이탈 하였을 경우
            } else if ((null != speedMeasVal && "02".equals(lmtItemCode))
                    && (lmtMinVal > speedMeasVal || lmtMaxVal < speedMeasVal)) {
                paramMap.put("break_reason", "속도이탈");
                breakYn = "Y";
                // 압력 이탈 하였을 경우
            } else if ((null != pressureMeasVal && "03".equals(lmtItemCode))
                    && (lmtMinVal > pressureMeasVal || lmtMaxVal < pressureMeasVal)) {
                paramMap.put("break_reason", "압력이탈");
                breakYn = "Y";
            } else {
            }
        } else {
            log.error("lmtItemCode or lmtMinVal or lmtMaxVal is null");
        }

        // 이탈 되었을 경우 개선조치, 알람 테이블에 등록
        if ("Y".equals(breakYn)) {
            paramMap.put("break_yn", "Y");
            paramMap.put("break_type_code", "01");
            paramMap.put("break_type_nm", "이탈");

            errDataMapper.insertShcRecCcpImprv(paramMap);
            errDataMapper.insertShcRecCcpAlarm(paramMap);
            errDataMapper.updateAlramIdShcRecCcpImprv(paramMap);
        } else {
            paramMap.put("break_yn", "N");
        }

        // 품온일 경우 이탈검사와 함께 수집 테이블에 등록
        if (null != paramMap.get("verfTypeCode") && "H03".equals(paramMap.get("verfTypeCode").toString())) {
            // 일지 상세 데이터 테이블에 등록
            errDataMapper.insertShcSensorData(paramMap);
        }
    }

    /**
     * 냉장냉동 (냉장냉동 온도값이 최저값, 최고값을 사이를 이탈하였는지 검증, 이탈하였을경우 설정된 이탈 회복시간동안 모두 이탈이면 이탈
     * 정상값이 있으면 경고)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     */

    public void coldStorageFrozen(List<Map<String, Object>> paramList) throws SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int idx = 0; idx < paramList.size(); idx++) {

                paramList.get(idx).put("user_input_yn", "N");

                Float lmtMinVal = null;
                Float lmtMaxVal = null;
                Float temperMeasVal = null;

                if (null != paramList.get(idx).get("lmtMinVal")) {
                    lmtMinVal = Float.parseFloat(paramList.get(idx).get("lmtMinVal").toString());
                }

                if (null != paramList.get(idx).get("lmtMaxVal")) {
                    lmtMaxVal = Float.parseFloat(paramList.get(idx).get("lmtMaxVal").toString());
                }

                if (null != paramList.get(idx).get("temperMeasVal")) {
                    temperMeasVal = Float.parseFloat(paramList.get(idx).get("temperMeasVal").toString());
                }

                // 냉장 온도값이 한계 최저, 최고값을 이탈 하였을 경우
                if (lmtMinVal > temperMeasVal || lmtMaxVal < temperMeasVal) {

                    paramList.get(idx).put("break_yn", "Y");
                    paramList.get(idx).put("break_reason", "온도이탈");

                    // 설비에 등록된 이탈 회복시간 동안의 이탈 리스트 조회
                    List<Map<String, Object>> breakYnList = rawDataMapper.ccpcleBreakYn(paramList.get(idx));

                    // 이탈, 경고 처리 변수
                    Integer resultBreakYn = null;

                    if (null != breakYnList && breakYnList.size() > 0) {
                        // 이탈 회복시간동안 이탈여부 판단 (이탈 회복시간 동안 모두 이탈 또는 경고이면 이탈(resultBreakYn=1), 한 건이라도 정상이
                        // 속해 있으면 경고(resultBreakYn=0))
                        for (int j = 0; j < breakYnList.size(); j++) {

                            Integer temp = Integer.parseInt(breakYnList.get(j).get("breakYn").toString());

                            if (null != resultBreakYn) {
                                resultBreakYn = resultBreakYn * temp;
                            } else {
                                resultBreakYn = Integer.parseInt(breakYnList.get(j).get("breakYn").toString());
                            }
                        }

                        if (resultBreakYn != 0) {
                            paramList.get(idx).put("break_type_code", "01");
                            paramList.get(idx).put("break_type_nm", "이탈");
                        } else {
                            paramList.get(idx).put("break_type_code", "02");
                            paramList.get(idx).put("break_type_nm", "경고");
                        }
                    } else {
                        paramList.get(idx).put("break_type_code", "02");
                        paramList.get(idx).put("break_type_nm", "경고");
                    }
                    // 개선조치, 알람 테이블에 등록
                    errDataMapper.insertShcRecCcpImprv(paramList.get(idx));
                    errDataMapper.insertShcRecCcpAlarm(paramList.get(idx));
                    errDataMapper.updateAlramIdShcRecCcpImprv(paramList.get(idx));
                }
            }
        }
    }

    /**
     * 금속검출 (측정값이 0이면 미검출, 1이면 검출, fe,sus,fe+제품,sus+제품 테스트시에는 검출이면 정상, 그외에는 이탈)
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     */

    public void metalDetection(List<Map<String, Object>> paramList) throws SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int idx = 0; idx < paramList.size(); idx++) {

                //mdKind null이 아니면 금속 테스트
                if(paramList.get(idx).get("mdKind") != null) {
                    String eventYn = "Y";
                    String eventStartYn = "Y";
                    int mdKind = Integer.parseInt(paramList.get(idx).get("mdKind").toString());

                    paramList.get(idx).put("md_event_start_flag", eventStartYn);

                    if(idx == 0) {
                        errDataMapper.insertShcRecCcpDesc(paramList.get(idx));
                    }

                    Float metalMeasVal = Float.parseFloat(paramList.get(idx).get("mdYnMeasVal").toString());

                    if(mdKind == 1) {
                        paramList.get(idx).put("lmt_item_code", "11");
                    } else if(mdKind == 2) {
                        paramList.get(idx).put("lmt_item_code", "12");
                    } else if(mdKind == 3) {
                        paramList.get(idx).put("lmt_item_code", "13");
                    } else if(mdKind == 4) {
                        paramList.get(idx).put("lmt_item_code", "14");
                    } else if(mdKind == 5) {
                        paramList.get(idx).put("lmt_item_code", "15");
                    }

                    Map<String, Object> eventSeqMap = errDataMapper.getEventSeqMdTest(paramList.get(idx));

                    paramList.get(idx).put("event_seq", Integer.parseInt(eventSeqMap.get("eventSeq").toString()));

                    // 이탈아닌경우
                    if((3 == mdKind && metalMeasVal == 0) || ((1 == mdKind || 2 == mdKind || 4 == mdKind || 5 == mdKind) && (metalMeasVal == 1))) {

                        paramList.get(idx).put("break_yn", "N");
                        paramList.get(idx).put("event_yn", "Y");

                        errDataMapper.insertShcSensorData(paramList.get(idx));

                        log.info(mdKind + "번 금속검출 테스트 완료");
                        // 이탈인경우
                    } else {
                        paramList.get(idx).put("break_yn", "Y");
                        paramList.get(idx).put("break_reason", "금속이탈");
                        paramList.get(idx).put("break_type_nm", "이탈");
                        paramList.get(idx).put("event_yn", "Y");
                        paramList.get(idx).put("break_type_code", "01");

                        errDataMapper.insertShcSensorData(paramList.get(idx));
                        errDataMapper.insertShcRecCcpImprv(paramList.get(idx));
                        errDataMapper.insertShcRecCcpAlarm(paramList.get(idx));
                        errDataMapper.updateAlramIdShcRecCcpImprv(paramList.get(idx));
                        log.info(mdKind + "번 금속검출 테스트 완료");
                    }
                } else {
                    //DataMgrVO dataMgrVO = (DataMgrVO)  BeanUtils.getBean("dataMgrVO");
                    int mdEventCnt = dataMgrVo.getMdEventCnt();

                    String eventYn = dataMgrVo.getMdEventYn();
                    String eventStartYn = dataMgrVo.getMdEventStartFlag().trim().toUpperCase();

                    if("Y".equals(eventYn)) {
                        mdEventCnt ++;
                    }

                    paramList.get(idx).put("md_event_start_flag", eventStartYn);

                    if(idx == 0) {
                        errDataMapper.insertShcRecCcpDesc(paramList.get(idx));
                    }

                    Float metalMeasVal = Float.parseFloat(paramList.get(idx).get("mdYnMeasVal").toString());

                    // 금속검출 테스트인 경우
                    if("Y".equals(eventYn)) {

                        Map<String, Object> eventSeqMap = errDataMapper.getEventSeq(paramList.get(idx));

                        paramList.get(idx).put("event_seq", Integer.parseInt(eventSeqMap.get("eventSeq").toString()));

                        if(mdEventCnt == 1) {
                            paramList.get(idx).put("lmt_item_code", "11");
                        } else if(mdEventCnt == 2) {
                            paramList.get(idx).put("lmt_item_code", "12");
                        } else if(mdEventCnt == 3) {
                            paramList.get(idx).put("lmt_item_code", "13");
                        } else if(mdEventCnt == 4) {
                            paramList.get(idx).put("lmt_item_code", "14");
                        } else if(mdEventCnt == 5) {
                            paramList.get(idx).put("lmt_item_code", "15");
                        }

                        // 이탈아닌경우
                        if((3 == mdEventCnt && metalMeasVal == 0) || ((1 == mdEventCnt || 2 == mdEventCnt || 4 == mdEventCnt || 5 == mdEventCnt) && (metalMeasVal == 1))) {

                            paramList.get(idx).put("break_yn", "N");
                            paramList.get(idx).put("event_yn", "Y");

                            errDataMapper.insertShcSensorData(paramList.get(idx));

                            log.info(mdEventCnt + "번째 금속검출 테스트 완료");

                            if(mdEventCnt >= 5) {
                                dataMgrVo.setMdEventYn("N");
                                mdEventCnt = 0;
                                log.info("금속검출 테스트 종료");
                            }
                            // 이탈인경우
                        } else {
                            paramList.get(idx).put("break_yn", "Y");
                            paramList.get(idx).put("break_reason", "금속이탈");
                            paramList.get(idx).put("break_type_nm", "이탈");
                            paramList.get(idx).put("event_yn", "Y");
                            paramList.get(idx).put("break_type_code", "01");

                            errDataMapper.insertShcSensorData(paramList.get(idx));
                            errDataMapper.insertShcRecCcpImprv(paramList.get(idx));
                            errDataMapper.insertShcRecCcpAlarm(paramList.get(idx));
                            errDataMapper.updateAlramIdShcRecCcpImprv(paramList.get(idx));

                            log.info(mdEventCnt + "번째 금속검출 테스트 완료");

                            if(mdEventCnt >= 5) {
                                dataMgrVo.setMdEventYn("N");
                                mdEventCnt = 0;
                                log.info("금속검출 테스트 종료");
                            }
                        }
                        // 금속검출 테스트 아닌경우
                    } else {
                        paramList.get(idx).put("event_yn", "N");
                        paramList.get(idx).put("event_seq", null);

                        // 금속검출 이탈하였을경우
                        if(metalMeasVal > 0) {
                            paramList.get(idx).put("break_yn", "Y");
                            paramList.get(idx).put("break_reason", "금속이탈");
                            paramList.get(idx).put("break_type_nm", "이탈");
                            paramList.get(idx).put("break_type_code", "03");

                            errDataMapper.insertShcSensorData(paramList.get(idx));
                            errDataMapper.insertShcRecCcpImprv(paramList.get(idx));
                            errDataMapper.insertShcRecCcpAlarm(paramList.get(idx));
                            errDataMapper.updateAlramIdShcRecCcpImprv(paramList.get(idx));

                            // 금속검출 이탈하지 않았을 경우
                        } else {
                            paramList.get(idx).put("break_yn", "N");
                            errDataMapper.insertShcSensorData(paramList.get(idx));
                        }
                    }
                    dataMgrVo.setMdEventStartFlag("N");
                    dataMgrVo.setMdEventCnt(mdEventCnt);
                }
                rawDataMapper.updateGatherYn(paramList.get(idx));
            }
        }
    }

    /**
     * 수집데이터를 일지상세데이터 및 일지데이터 테이블에 등록
     *
     * param ListMap 조회 파라미터
     * return void
     * throws SQLException
     */

    public void monDataInsert(List<Map<String, Object>> paramList) throws SQLException {
        if (null != paramList && paramList.size() > 0) {
            for (int idx = 0; idx < paramList.size(); idx++) {

                paramList.get(idx).put("user_input_yn", "N");

                // 수집 처리할 데이터의 이탈 정보를 조회
                Map<String, Object> paramMap = errDataMapper.selectShcRecCcpImprv(paramList.get(idx));

                if (null != paramMap) {

                    String breakTypeCode = null;
                    String lmtItemCodeKind = null;

                    if (null != paramMap.get("breakTypeCode")) {
                        breakTypeCode = paramMap.get("breakTypeCode").toString();
                    }

                    if (null != paramMap.get("lmtItemCodeKind")) {
                        lmtItemCodeKind = paramMap.get("lmtItemCodeKind").toString();
                    }

                    // 이탈코드가 1(이탈) 이거나 2(경고)이면 이탈 아니면 정상
                    if ((null != breakTypeCode) && ("01".equals(breakTypeCode) || "02".equals(breakTypeCode))) {
                        paramList.get(idx).put("break_yn", "Y");
                    } else {
                        paramList.get(idx).put("break_yn", "N");
                    }

                    if (null != lmtItemCodeKind) {
                        paramList.get(idx).put("lmt_item_code_kind", paramMap.get("lmtItemCodeKind"));
                    } else {
                        paramList.get(idx).put("lmt_item_code_kind", null);
                    }
                } else {
                    paramList.get(idx).put("break_yn", "N");
                    paramList.get(idx).put("lmt_item_code_kind", null);
                }

                String lmtItemCode = paramList.get(idx).get("lmtItemCode").toString();
                String measVal = paramList.get(idx).get("measVal").toString();

                // 01(온도), 02(속도), 03(압력)
                if (null != lmtItemCode && null != measVal) {
                    switch (lmtItemCode) {
                        case "01":
                            paramList.get(idx).put("temper_meas_val", measVal);
                            break;
                        case "02":
                            paramList.get(idx).put("speed_meas_val", measVal);
                            break;
                        case "03":
                            paramList.get(idx).put("pressure_meas_val", measVal);
                            break;
                    }
                }

                // 일지 데이터에 저장(리스트에 2행일 경우 하나의 행만 저장(예. 온도, 속도일경우 온도행만 저장. 일지 데이터 테이블에는 기본정보만
                // 등록되고, 측정값 이탈여부 등이 등록 되지 않으므로 상관없음))
                if("Y".equals(paramList.get(idx).get("eventYn")) || !"C0010".equals(paramList.get(idx).get("lCode"))) {
                    if (idx == 0) {
                        errDataMapper.insertShcRecCcpDesc(paramList.get(idx));
                    }
                    // 일지 상세데이터에 저장
                    errDataMapper.insertShcSensorData(paramList.get(idx));

                    // 수집 여부 Y로 변경
                    rawDataMapper.updateGatherYn(paramList.get(idx));
                }
            }
        }
    }

    /**
     * 시작 시간 값과, 종료 시간의 값을 비교하여 시간 차이를 구함
     *
     * param String 조회 파라미터(시작시간, 종료시간)
     * return String
     * throws ParseException
     */

    public String timesDiff(String begin, String end) throws ParseException {

        String diffTm = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date FirstDate = format.parse(begin);
        Date SecondDate = format.parse(end);

        long diffTime = FirstDate.getTime() - SecondDate.getTime();
        long diffSecond = diffTime / 1000;

        diffSecond = Math.abs(diffSecond);
        diffTm = String.valueOf(diffSecond);

        return diffTm;
    }

    /**
     * 실시간 데이터를 검증코드별로 분기하여 이탈검증
     *
     * param ListMap 조회 파라미터
     * return void
     */
     public void errDataStartVerify(List<Map<String, Object>> paramList) {
        try {
            if(null != paramList && paramList.size() > 0) {

                switch(paramList.get(0).get("verfTypeCode").toString()){
                    // 가열지속(온도, 속도)
                    case "H01" :
                        this.heatingContinuing(paramList);
                        break;
                    // 가열이벤트(온도, 공정시간)
                    case "H02" :
                        this.heatingEvent(paramList);
                        break;
                    // 가열품온(온도)
                    case "H03" :
                        this.heatingMaterial(paramList);
                        break;
                    // 가열압력이벤트(온도, 압력, 공정시간)
                    case "H04" :
                        this.heatingPressureEvent(paramList);
                        break;
                    // 냉장냉동(온도)
                    case "C01" :
                        this.coldStorageFrozen(paramList);
                        break;
                    // 금속검출(1,2)
                    case "D01" :
                        this.metalDetection(paramList);
                        break;
                }
            }
        } catch (SQLException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        } catch (ParseException e) {
            CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
        }
    }
}
