package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class EquipMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="equip_mast_no",nullable = false)
    private long equipMastNo;

    //제품코드
    @Column(name = "ware_cd", length = 50)
    private String wareCd;

    //구매단가
    @Column(name = "purs_unit_prc", length = 50)
    private Long pursUnitPrc;

    //구매일
    @Column(name="purs_dt" )
    @Temporal(TemporalType.TIMESTAMP)
    private Date pursDt;

    //설비명
    @Column(name="equip_nm",nullable = false, length = 250)
    private String equipNm;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //모델명
    @Column(name = "modl_nm", length = 100)
    private String modlNm;

    //규격
    @Column(name = "sz", length = 100)
    private String sz;

    //제품사양
    @Column(name = "ware_spec",   length = 100)
    private String wareSpec;

    //설치장소
    @Column(name = "install_plc",  length = 100)
    private String installPlc;

    //원산지
    @Column(name = "madein" )
    private Long madein;

    //제조사명
    @Column(name = "make_cmpy_nm",  length = 100)
    private String makeCmpyNm;

    //점검주기
    @Column(name="chk_period" ,nullable = false )
    private Long chkPeriod;

    //최종점검일
    @Column(name="lst_chk_dt" )
    @Temporal(TemporalType.TIMESTAMP)
    private Date lstChkDt;

    //사진번호
    @Column(name = "file_no")
    private Long fileNo;

    @Column(name="used_yn" ,nullable = false, length = 1)
    private String usedYn;

    @Column(name="reg_id"  )
    private Long regId;

    @Column(name="mod_id" )
    private Long modId;

    @Column(name="reg_ip"  ,length = 20)
    private String regIp;

    @Column(name="mod_ip" ,length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}
