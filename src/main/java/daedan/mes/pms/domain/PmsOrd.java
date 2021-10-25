package daedan.mes.pms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PmsOrd {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ord_no"  ,nullable = false ,columnDefinition = "numeric")
    private Long ordNo;

    /*관련사업번호*/
    @Column(name="saup_no" ,columnDefinition = "numeric default 0")
    private Long saupNo;

    /*요청자id*/
    @Column(name="req_id", nullable = false, columnDefinition = "numeric")
    private Long reqId;

    /*제목*/
    @Column(name="ord_nm", nullable = false, length = 200 )
    private String ordNm;

    /*요청일자*/
    @Column(name="indc_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date indcDt;

    /*마감일자*/
    @Column(name="clos_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date closDt;

    /*처리상태*/
    @Column(name="proc_stat",nullable = false ,columnDefinition = "numeric")
    private Long procStat;

    /*첨부파일번호*/
    @Column(name="fileNo",columnDefinition = "numeric default 0")
    private Long fileNo;

    /*내용*/
    @Column(name="pms_cont", length = 4000)
    private String pmsCont;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id" ,columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id",columnDefinition = "numeric")
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
}
