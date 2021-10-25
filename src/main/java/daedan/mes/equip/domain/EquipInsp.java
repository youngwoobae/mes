package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//설비점검관리
@Data
@Entity
@NoArgsConstructor
public class EquipInsp {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="insp_no",nullable = false)
    private long InspNo;

    //점검항목코드
    @Column(name = "chk_item", nullable = false)
    private Long chkItem;

    //점검구분코드(정기점검,수시점검,이상점검)
    @Column(name = "chk_gbn", nullable = false)
    private Long chkGbn;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //점검일자
    @Column(name = "chk_dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date chkDt;

    //설비정보 연계
    @ManyToOne( fetch=FetchType.LAZY)
    @JoinColumn(name="equip_no")
    private EquipInfo equipInfo;

    public EquipInfo getEquipInfo() {
        return equipInfo;
    }
    public void  setEquipInfo(EquipInfo equipInfo) {
        this.equipInfo = equipInfo;
    }


    @Column(name="reg_id" ,nullable = false, length = 20)
    private String regId;

    @Column(name="mod_id" ,nullable = false, length = 20)
    private String modId;

    @Column(name="reg_ip" ,nullable = false, length = 20)
    private String regIp;

    @Column(name="mod_ip" ,nullable = false, length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}
