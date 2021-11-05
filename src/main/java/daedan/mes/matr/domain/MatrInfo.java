package daedan.mes.matr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrInfo {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="matr_no")
    private Long matrNo;

    /*분류번호*/
    @Column(name="brnch_no",nullable = false )
    private Long brnchNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*자재유형*/
    @Column(name="matr_tp")
    private Long matrTp;

    /*자재명*/
    @Column(name="matr_nm",nullable = false, length = 200)
    private String matrNm;

    //품목코드
    @Column(name="item_cd", length = 20)
    private String itemCd;

    //보관온도코드
    @Column(name="save_tmpr" )
    private Long saveTmpr;

    //
    @Column(name="brix", columnDefinition = "smallint default 0")
    private Integer brix;


    //정격전압
    @Column(name="base_volt" , length = 20)
    private String baseVolt;

    //소재코드
    @Column(name="madeby" )
    private Long madeby;


    //유효기간
    @Column(name="valid_term", columnDefinition = "numeric" , precision=5, scale=2 )
    private float validTerm;


    //원산지
    @Column(name = "madein"  )
    private Long madein;

    //원산지 (하담사용)
    @Column(name = "made" , length = 100  )
    private String made;

    /*관리단위 : 용량(ml)=1702, 질량(gram)=1701 기본값은 */
    @Column(name="mngr_base", columnDefinition = "numeric default 1701"  )
    private Long mngrBase;

    //비중
    @Column(name = "spga" , columnDefinition = "numeric default 1.0", precision=7, scale=3)
    private Float spga;

    // 부피(ml) :
    @Column(name = "vol" ,nullable = false, columnDefinition = "numeric default 0" )
    private Float vol;

    //질량(gram) : 관리단위가 용량(ml) 인경우  질량(mess) = 용량(vol) * 비중(spge)
    @Column(name = "mess" , columnDefinition = "numeric default 0", precision=7, scale=3)
    private Float mess;

    /*규격*/
    @Column(name="sz", length = 100)
    private String sz;


    /*구매단위*/
    @Column(name="purs_unit" )
    private Long pursUnit;


    /*관리단위(무게,질량)*/
    @Column(name="mngr_unit" )
    private Long mngrUnit;

    /*패킷유닛(업로드시점에 사용됨.. 내부적으로 딱히 사용할 곳은 없음.)*/
    @Column(name="pkgUnit" )
    private Long pkgUnit;

    /*구매단위중량*/
    @Column(name="pursUnitWgt" )
    private Long pursUnitWgt;

    /*구매단가*/
    @Column(name="purs_unit_prc" )
    private Long pursUnitPrc;

    //원료속성(연근,진세노사이드,브릭스,고형분,점도,색도등 : 대동고려삼..)
    @OneToOne (fetch = FetchType.LAZY, optional=true)
    private MatrAttr matrAttr;

    @Column(name="file_no" )
    private Long fileNo;

    @Column(name="reg_id")
    private Long regId;

    @Column(name="mod_id")
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

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="base_cal_yn", length = 1 , columnDefinition = "char default 'N'")
    private String baseCalYn;


}
