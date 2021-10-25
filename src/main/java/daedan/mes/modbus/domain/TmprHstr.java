package daedan.mes.modbus.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class TmprHstr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meas_no", nullable = false, columnDefinition = "numeric")
    private Long measNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*측정일시*/
    @Column(name = "meas_dt" ,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date measDt;
    /*설비번*/
    @Column(name="equip_no" ,nullable = false, columnDefinition = "numeric")
    private Long equipNo;

    /*측정온도*/
    @Column(name="meas_tmpr",nullable = false, precision=5, scale=2)
    private Float measTmpr;

    /*측정습도*/
    @Column(name="meas_humy",nullable = false, precision=5, scale=2)
    private Float measHumy;

    /*발송일자*/
    @Column(name="unix_hms",nullable = false, columnDefinition = "numeric default 0")
    private Long unixHms;
}
