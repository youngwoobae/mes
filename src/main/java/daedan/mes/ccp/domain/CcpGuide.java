package daedan.mes.ccp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class CcpGuide {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ccp_no")
    private Long ccpNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*CCP관리코드*/
    @Column(name="ccp_cd" ,nullable = false, length = 20)
    private String ccpCd;

    /*공정코드:*/
    @Column(name="proc_cd",nullable = false, columnDefinition = "bigint default 0")
    private Long procCd;

    /*한계기준*/
    @Column(name="lmt_base" ,length = 4000)
    private String lmtBase;

    /*설비감도*/
    @Column(name="equip_sense" , length = 4000)
    private String equipSense;

    /*제품감도*/
    @Column(name="prod_sense" , length = 4000)
    private String prodSense;

    /*통과기준*/
    @Column(name="pass_base" , length = 1000)
    private String passBase;

    @Column(name="meas_tp")
    private Long measTp;

    @Column(name="used_yn" , length = 1)
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
