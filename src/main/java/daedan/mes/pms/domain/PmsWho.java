package daedan.mes.pms.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PmsWho {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pms_who_no")
    private Long pmsWhoNo;

    /*지시번호*/
    @Column(name="ord_no")
    private Long ordNo;

    /*수행자id*/
    @Column(name="recv_id")
    private Long recvId;


    /*확인일자*/
    @Column(name="view_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewDt;

    /*완료일자*/
    @Column(name="cnfm_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cmfmDt;

    /*처리상태*/
    @Column(name="proc_stat")
    private Long procStat;

    /*처리내용*/
    @Column(name="proc_cont" )
    private String procCont;

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
