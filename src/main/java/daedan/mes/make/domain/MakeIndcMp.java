package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeIndcMp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_mp_no",nullable = false, columnDefinition = "numeric")
    private Long indcMpNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*작업지시번호*/
    @Column(name="indc_no", columnDefinition = "numeric")
    private long indcNo;

    /*사용자정보*/
    @Column(name="user_id",nullable = false)
    private long userId;

    /*작업장번호*/
    @Column(name="spot_no",nullable = false)
    private long spotNo;

    /*작업일자*/
    @Column(name="mp_used_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mpUsedDt;

    /*시작시분*/
    @Column(name="fr_hm",nullable = false, length=4)
    private String frHm;

    /*종료시분*/
    @Column(name="to_hm",nullable = false,length=4)
    private String toHm;

    /*정규근무시간*/
    @Column(name="regul_work_hm",nullable = false,columnDefinition = "int default 0")
    private Integer regulWorkHm;

    /*초과시작시분*/
    @Column(name="over_fr_hm", length=4)
    private String overFrHm;

    /*초과종료시간*/
    @Column(name="over_to_hm", length = 4)
    private String overToHm;

    /*초과근무시간*/
    @Column(name="over_work_hm",nullable = false,columnDefinition = "int default 0")
    private Integer overWorkHm;

    /*환산근무시간*/
    @Column(name="exchg_work_hm",nullable = false,columnDefinition = "int default 0")
    private Integer exchgWorkHm;

    /*총근무시간*/
    @Column(name="tot_work_hm",nullable = false,columnDefinition = "int default 0")
    private Integer totWorkHm;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
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
