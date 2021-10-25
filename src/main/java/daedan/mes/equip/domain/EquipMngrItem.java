package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor
public class EquipMngrItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mngr_item_no", nullable = false)
    private Long mngrItemNo;

    //관제설비번호
    @Column(name = "equip_no", nullable = false)
    private Long equipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //관리항목코드
    @Column(name = "mngr_item", nullable = false,  columnDefinition = "bigint default 0")
    private Long mngrItem;

    //측정주기
    @Column(name = "meas_period", nullable = false, columnDefinition = "int default 60")
    private Integer measPeriod;

    //최대허용임계값
    @Column(name = "max_lmt_val", nullable = false,  precision=7, scale=3)
    private Float maxLmtVal;

    //최소허용임계값
    @Column(name = "min_lmt_val", nullable = false,  precision=7, scale=3)
    private Float minLmtVal;


    @Column(name = "used_yn", nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name = "reg_id", nullable = false)
    private Long regId;

    @Column(name = "mod_id", nullable = false)
    private Long modId;

    @Column(name = "reg_ip", nullable = false, length = 20)
    private String regIp;

    @Column(name = "mod_ip", nullable = false, length = 20)
    private String modIp;

    @Column(name = "reg_dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name = "mod_dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}
