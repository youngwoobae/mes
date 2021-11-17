package daedan.mes.cmmn.io.datamgr;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class DataMgrVO {
        public String mdEventYn = "N"; //금속검출기 이벤트 중 확인 플래그 변수
        public String mdEventStartFlag = "N"; //금속검출기 이벤트 시작 확인 플래그 변수
        public int mdEventCnt = 0; //금속검출기 테스트 횟수 확인 변수
        public Queue<Object[]> packetDataMgrQ = new LinkedBlockingQueue<Object[]>(1000); //설비센서에서 수신된 패킷을 저장하는 큐

        public String getMdEventYn() {
            return mdEventYn;
        }
        public void setMdEventYn(String mdEventYn) {
            this.mdEventYn = mdEventYn;
        }

        public String getMdEventStartFlag() {
            return mdEventStartFlag;
        }
        public void setMdEventStartFlag(String mdEventStartFlag) {
            this.mdEventStartFlag = mdEventStartFlag;
        }

        public int getMdEventCnt() {
            return mdEventCnt;
        }
        public void setMdEventCnt(int mdEventCnt) {  this.mdEventCnt = mdEventCnt;}
    }
