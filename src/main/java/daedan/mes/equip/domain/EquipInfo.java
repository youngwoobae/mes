package daedan.mes.equip.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//설비정보
@Data
@Entity
@NoArgsConstructor
public class EquipInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="equip_no",nullable = false)
    private long equipNo;

    public EquipInfo(Long equipNo
            , String equipNm
            , String wareCd
            , Long pursUnitPrc
            , String sz
            , String modlNm
            , Long   madein
            , String makeCmpy
            , Long   chkPerid
            , Date   lstChkDt
            , String wareSpec
            , Long fileNo
    )  { this.equipNo = equipNo;
        this.equipNm = equipNm;
        this.wareCd = wareCd;
        this.pursUnitPrc = pursUnitPrc;
        this.sz = sz;
        this.modlNm = modlNm;
        this.madein = madein;
        this.makeCmpy = makeCmpy;
        this.chkPeriod = chkPerid;
        this.lstChkDt = lstChkDt;
        this.wareSpec = wareSpec;
        this.fileNo = fileNo;
    }

    //제품코드
    @Column(name = "ware_cd", length = 50)
    private String wareCd;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //구매단가
    @Column(name = "purs_unit_prc", length = 50)
    private Long pursUnitPrc;

    //제품사양(비고사항)
    @Column(name = "ware_spec", length = 4000)
    private String wareSpec;

    //설비명
    @Column(name="equip_nm",nullable = false, length = 250)
    private String equipNm;

    //모델명
    @Column(name = "modl_nm", length = 100)
    private String modlNm;

    //규격
    @Column(name = "sz", length = 100)
    private String sz;

    //원산지
    @Column(name = "madein" )
    private Long madein;

    //제조사
    @Column(name = "make_cmpy",  length = 100)
    private String makeCmpy;

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
