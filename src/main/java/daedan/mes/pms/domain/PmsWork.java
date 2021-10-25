package daedan.mes.pms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PmsWork {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pms_work_no")
    private Long pmsWorkNo;

    /*PMS 대상자 번호*/
    @Column(name="pms_who_no")
    private Long pmsWhoNo;

    /*처리일자*/
    @Column(name="proc_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date procDt;


    /*첨부파일번호*/
    @Column(name="fileNo",columnDefinition = "numeric default 0")
    private Long fileNo;

    /*처리내용*/
    @Lob
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
