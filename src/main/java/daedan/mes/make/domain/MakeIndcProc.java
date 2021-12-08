package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MakeIndcProc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indcProcNo",nullable = false, columnDefinition = "numeric")
    private Long indcProcNo;
    /*고객번호*/
    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*지시번호*/
    @Column(name="indcNo", columnDefinition = "numeric default 0")
    private Long indcNo;

    /*공정코드*/
    @Column(name="procCd",nullable = false, columnDefinition = "numeric")
    private Long procCd;

    /*공정코드*/
    @Column(name="procSeq",nullable = false, columnDefinition = "numeric")
    private int procSeq;

    /*생산일자*/
    @Column(name="makeDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeDt;

    /*생산량*/
    @Column(name="makeQty",nullable = false, columnDefinition = "numeric default 0")
    private Float makeQty;

    /*작업상태:parcoce=500,default = 501(대기)*/
    @Column(name="procStat",nullable = false, columnDefinition = "numeric default 501")
    private Long procStat;

    /*등록id*/
    @Column(name="reg_id", length = 20)
    private Long regId;
    /*등록ip*/
    @Column(name="reg_ip", length = 20)
    private String regIp;
    /*등록일자*/
    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    /*수정ip*/
    @Column(name="mod_ip", length = 20)
    private String modIp;
    /*수정id*/
    @Column(name="mod_id", length = 20)
    private Long modId;

    /*수정일자*/
    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    /*사용여부*/
    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
