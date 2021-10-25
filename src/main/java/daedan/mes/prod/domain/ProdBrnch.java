package daedan.mes.prod.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdBrnch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="brnch_no",nullable = false, columnDefinition = "numeric")
    private Long brnchNo;


    @Column(name="custNo" , columnDefinition = "numeric")
    private Long custNo;

    @Column(name="par_brnch_no", nullable = false, columnDefinition = "numeric" )
    private Long parBrnchNo;

    @Column(name="brnch_nm",nullable = false)
    private String brnchNm;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id",columnDefinition = "numeric")
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
