package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class EquipMatr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "matr_no", nullable = false)
    private Long matrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //자재명
    @Column(name = "matr_nm", nullable = true, length = 100)
    private String matrNm;

    //구매단위코드
    @Column(name = "purs_unit", nullable = false, columnDefinition = "bigint default 0")
    private Long pursUnit;

    //구매단가
    @Column(name = "purs_unit_prc", nullable = false, columnDefinition = "bigint default 0")
    private Long pursUnitPrc;

    //안전고재량
    @Column(name = "safe_stk_qty", nullable = true, columnDefinition = "bigint default 0")
    private Long safeStkQty;

    //부품번호
    @Column(name = "part_no", nullable = false, columnDefinition = "bigint default 0")
    private Long partNo;

    //부품코드
    @Column(name = "item_cd", nullable = true, length = 100)
    private String itemCd;

    //규격
    @Column(name = "sz", nullable = true, length = 100)
    private String sz;

    //원산지
    @Column(name = "madein", nullable = true, length = 100)
    private Long madein;

    //제조사
    @Column(name = "make_cmpy", nullable = true, length = 100)
    private String makeCmpy;

    //모델명
    @Column(name = "modl_nm", nullable = true, length = 100)
    private String modlNm;

    //사진번호
    @Column(name = "file_no",  columnDefinition = "numeric default 0")
    private Long fileNo;

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
