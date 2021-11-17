package daedan.mes.cmmn.io.datamgr.mondata;

import daedan.mes.common.error_handle.CustomErrorHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MonDataController  implements Runnable {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    MonDataMapper mapper;

    @Autowired
    MonDataService monDataService;

    private final String mCode;
    private final boolean startFlag = true;

    public MonDataController(String mCode) {
        this.mCode = mCode;
    }

    /**
     * 설비별로 쓰레드를 동작시켜 지정된 시간마다 DB에 저장되어 있는 데이터를 수집하여 이탈검증 메서드 호출
     *
     * @return void
     */
    @Override
    public void run() {
        // 센서 데이터 수집 쓰레드
        while (startFlag) {
            try {
                // 해당 쓰레드 아이디 값(M_CODE) 값으로 조회하여 설비의 수집 주기 및 검증 코드 조회
                Map<String, Object> monDataMap = mapper.selectStdCycle(mCode);
                Map<String, Object> paramMap = new HashMap<String, Object>();

                if (null != monDataMap) {
                    if (null != monDataMap.get("cycle") && !"".equals(monDataMap.get("cycle"))) { // 수집 주기
                        paramMap.put("cycle", monDataMap.get("cycle"));
                    }
                    /* 검증 코드 (SHC_CCP_LMT_STD.VerfTypeCode)
                          H03 = 가열 품온샘플
                          05 : 냉장샘플한계기준
                          D01 : 금속검출 샘플
                    */
                    if (null != monDataMap.get("verfTypeCode") && !"".equals(monDataMap.get("verfTypeCode"))) {
                        paramMap.put("verfTypeCode", monDataMap.get("verfTypeCode"));
                    }
                    /* 설비 코드 (SHC_CCP_EQUIP.mCode)
                        CH01 : CCP-가열기1
                        CC01 : 냉장창고1
                        CD01 : 금속검출기1
                     */
                    if (null != mCode && !"".equals(mCode)) {
                        paramMap.put("mCode", mCode);
                    }
                    monDataService.readMonData(paramMap); // 데이터 수집하여, 일지데이터, 일지상세데이터 테이블에 등록 함수 호출

                    int sleepTm = Integer.parseInt(monDataMap.get("cycle").toString()) * 1000 * 60; // cycle 컬럼에 등록되어 있는 분단위를 쓰레드 sleep 처리하기 위해 환산

                    if ("C01".equals(monDataMap.get("verfTypeCode"))) { // 설비가 냉장,냉동 검증인 경우
                        if (Integer.parseInt(monDataMap.get("cycle").toString()) > 10) { // 데이터 수집 주기가 10분 보다 클 경우
                            Map<String, Object> lastCCbreakYn = mapper.selectLastCCbreakYn(mCode); // 냉장, 냉동 설비에 마지막으로 수집된 데이터가 이탈인지 아닌지 조회
                            if (null != lastCCbreakYn && null != lastCCbreakYn.get("breakYn")) {
                                if ("정상".equals(lastCCbreakYn.get("breakYn"))) { // 이탈 검증 후 정상이면 DB에 저장된 수집주기 그대로 실행
                                    Thread.sleep(sleepTm);
                                } else {
                                    Thread.sleep(10 * 1000 * 60); // 이탈 검증 후 이탈이면 10분마다 수집 실행
                                }
                            } else {
                                Thread.sleep(sleepTm);
                            }
                        }
                        else { // 데이터 수집 주기가 10분 보다 작을 경우
                            Thread.sleep(sleepTm);
                        }
                    }
                    else {
                        Thread.sleep(sleepTm); // 설비가 가열기이면 설정된 수집주기 그대로 실행
                    }
                }
                else {
                    break;
                }
                // 소스 수정후 프로그램 자동 재시작시 기존에 쓰레드가 종료되지 않아 port 충돌 에러로 인한 추가 (추후 수정 필요)
            } catch (IllegalStateException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
            } catch (InterruptedException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
                break;
            } catch (SQLException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
            } catch (ParserConfigurationException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
            } catch (SAXException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
            } catch (IOException e) {
                CustomErrorHandler.handle(this.getClass().getSimpleName(), e);
            }
        }
    }
}
