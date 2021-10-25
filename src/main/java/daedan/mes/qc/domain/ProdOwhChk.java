package daedan.mes.qc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdOwhChk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="chk_no",nullable = false, columnDefinition = "numeric")
    private Long chkNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*점검유형*/
    @Column(name="chk_tp",nullable = false, columnDefinition = "numeric")
    private Long chkTp;

    /*점검방법*/
    @Column(name="chk_mth",nullable = false, columnDefinition = "numeric")
    private Long chkMth;

    /*점검내용*/
    @Column(name="chk_cont", length = 1000 )
    private String chkCont;

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

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;
}
