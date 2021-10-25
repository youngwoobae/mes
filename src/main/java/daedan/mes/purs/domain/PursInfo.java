package daedan.mes.purs.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PursInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="purs_no",nullable = false)
    private Long pursNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*작업지시번호*/
    @Column(name="indc_no")
    private Long indcNo;


    /*AddOn By KMJ AT 21.09.03 06:17 : 생산계획에서 주문제품별 BOM단 생산가능 원료점검 여부를 확인하기 위해 사용 */
    @Column(name="prodNo" , columnDefinition = "numeric default '0'")
    private Long prodNo;


    @Column(name="cmpy_no")
    private Long cmpyNo;

    /*주문시번호*/
    @Column(name="ord_no")
    private Long ordNo;
    /*AddOn By KMJ At 21.09.02 21:09 - 소요량 예측을 위한 예비수율  대동고려삼 요청사항 반영*/
    @Column(name="ctlFillYield", precision=5, scale=1,columnDefinition = "numeric default 0")
    private  float ctlFillYield;

    @Column(name="purs_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pursDt;

    @Column(name="dlv_req_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvReqDt;

    @Column(name="dlv_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvDt;

    @Column(name="purs_sts",nullable = false)
    private Long pursSts;

    @Column(name="reg_id")
    private Long regId;

    @Column(name="mod_id")
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="used_yn", nullable = false, length = 1 )
    private String usedYn;


}
