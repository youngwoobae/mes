package daedan.mes.moniter.domain.cust18;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class HeaterStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="heatStatNo",nullable = false)
    private Long heatStatNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="spotNo",nullable = false, columnDefinition = "numeric default 0")
    private Long spotNo;

    /*측정일시*/
    @Column(name="unixHms",nullable = false, length = 12)
    private Long unixHms;

    @Column(name="msg", length = 250)
    private String msg;

    @Column(name="operYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String operYn;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
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
