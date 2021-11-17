package daedan.mes.cmmn.io.datamgr.rawdata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class RawDataController implements Runnable {
    private Log log = LogFactory.getLog(this.getClass());
    private final boolean startFlag = true;

    @Override
    public void run() {
        /**
         * 설비에서 수신된 데이터가 담긴 공통 Queue를 처리하는 쓰레드 생성
         *
         * @return void
         */
        while(startFlag) {
            RawDataService rawDataService = new RawDataService() ;
            rawDataService.packetProcess();
        }
        // 소스 수정후 프로그램 자동 재시작시 기존에 쓰레드가 종료되지 않아 port 충돌 에러로 인한 추가 (추후 수정 필요)
    }
}
