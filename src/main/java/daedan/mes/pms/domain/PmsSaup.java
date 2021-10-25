package daedan.mes.pms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PmsSaup {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="saup_no"  ,nullable = false ,columnDefinition = "numeric")
    private Long saupNo;

    /*사업명*/
    @Column(name="saup_nm", nullable = false, length = 200 )
    private String saupNm;

    /*착수일자*/
    @Column(name="from_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date indcDt;

    /*종료일자*/
    @Column(name="clos_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date closDt;

    /*처리상태*/
    @Column(name="proc_stat",nullable = false ,columnDefinition = "numeric")
    private Long procStat;

    /*거래처번호*/
    @Column(name="cmpy_no",nullable = false ,columnDefinition = "numeric")
    private Long cmpyNo;

    /*첨부파일번호*/
    @Column(name="fileNo",columnDefinition = "numeric default 0")
    private Long fileNo;

    /*사업개요*/
    @Lob
    @Column(name="saup_cont" )
    private String saupCont;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;


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

}
