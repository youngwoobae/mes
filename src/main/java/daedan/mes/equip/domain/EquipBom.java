package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//설비별자재bom
@Data
@Entity
@NoArgsConstructor

public class EquipBom {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="equip_bom_no",nullable = false)
    private long equipBomNo;

    @Column(name="equip_no",nullable = false )
    private Long equipNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="matr_no",nullable = false )
    private Long matrNo;

    @Column(name="need_qty",nullable = false)
    private long needQty;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

   // @ManyToOne( fetch=FetchType.LAZY)
    //@JoinColumn(name="equip_no")
    //private EquipInfo equipInfo;
    //public EquipInfo getEquipInfo(long equip_no) {
    //    return equipInfo;
    //}
    //public void setEquipInfo(EquipInfo equipInfo) {
    //    this.equipInfo = equipInfo;
    //}

    @Column(name="reg_id")
    private long regId;

    @Column(name="mod_id")
    private long modId;

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
