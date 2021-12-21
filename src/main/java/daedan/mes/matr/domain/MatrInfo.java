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
    @Column(name="matrNo")
    private Long matrNo;

    /*분류번호*/
    @Column(name="brnchNo",nullable = false )
    private Long brnchNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    //발송일시(UnixTime)
    @Column(name="sendUt" , columnDefinition = "numeric default 0")
    private Long sendUt;


    /*자재유형*/
    @Column(name="matrTp")
    private Long matrTp;

    /*자재명*/
    @Column(name="matrNm",nullable = false, length = 200)
    private String matrNm;

    //품목코드
    @Column(name="itemCd", length = 20)
    private String itemCd;

    //보관온도코드
    @Column(name="saveTmpr" )
    private Long saveTmpr;

    //
    @Column(name="brix", columnDefinition = "smallint default 0")
    private Integer brix;


    //정격전압
    @Column(name="baseVolt" , length = 20)
    private String baseVolt;

    //소재코드
    @Column(name="madeby" )
    private Long madeby;


    //유효기간
    @Column(name="validTerm", columnDefinition = "numeric" , precision=5, scale=2 )
    private float validTerm;

    //유효기간
    @Column(name="strValidTerm" , length = 20  )
    private String strValidTerm;

    //원산지
    @Column(name = "madein"  )
    private Long madein;

    //원산지 (하담사용)
    @Column(name = "made" , length = 100  )
    private String made;

    /*창고번호 : 원료적재창고 : 나중에 사용할 예정임.*/
    @Column(name="whNo", columnDefinition = "numeric default 0")
    private Long whNo;

    /*관리단위 : 용량(ml)=1702, 질량(gram)=1701 기본값은 */
    @Column(name="mngrBase", columnDefinition = "numeric default 1701"  )
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
    @Column(name="pursUnit" )
    private Long pursUnit;


    /*관리단위(무게,질량)*/
    @Column(name="mngrUnit" )
    private Long mngrUnit;

    /*패킷유닛(업로드시점에 사용됨.. 내부적으로 딱히 사용할 곳은 없음.)*/
    @Column(name="pkgUnit" )
    private Long pkgUnit;

    /*구매단위중량*/
    @Column(name="pursUnitWgt" )
    private Long pursUnitWgt;

    /*구매단가*/
    @Column(name="pursUnitPrc" )
    private Long pursUnitPrc;

    //원료속성(연근,진세노사이드,브릭스,고형분,점도,색도등 : 대동고려삼..)
    @OneToOne (fetch = FetchType.LAZY, optional=true)
    private MatrAttr matrAttr;

    @Column(name="fileNo" )
    private Long fileNo;

    @Column(name="regId")
    private Long regId;

    @Column(name="modId")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

    //AddOn by KMJ At 21.05.05 23:00 --하담푸드에서 생산지시량 설정에 필요해서 추가 (지시량이 총량이 아니고 원율이라 계산식이 바뀜))
    @Column(name="baseCalYn", length = 1 , columnDefinition = "char default 'N'")
    private String baseCalYn;


}
