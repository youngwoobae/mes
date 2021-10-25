package daedan.mes.make.domain.sf;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class DailyIndc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="dateIndcNo",nullable = false, columnDefinition = "numeric")
    private Long dateIndcNo;

    /*지시일자*/
    @Column(name="indcDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indcDt;

    @Column(name="indcRmk" , columnDefinition = "TEXT")
    private String indcRmk;

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
