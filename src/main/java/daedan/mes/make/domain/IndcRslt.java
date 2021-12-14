package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class IndcRslt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="indc_rslt_no",nullable = false, columnDefinition = "numeric")
    private Long indcRsltNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*공정코드*/
    @Column(name="proc_cd",nullable = false, columnDefinition = "numeric")
    private Long procCd;

    /*생산일시*/
    @Column(name="make_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date makeDt;

    /*생산수량*/
    @Column(name="make_qty", precision=10, scale=2 , columnDefinition = "numeric default 0")
    private Float makeQty;

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
