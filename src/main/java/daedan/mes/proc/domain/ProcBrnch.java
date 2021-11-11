package daedan.mes.proc.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

//공정분류관리
@Data
@Entity
@NoArgsConstructor
public class ProcBrnch {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="procBrnchNo",nullable = false)
    private Long procBrnchNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*공정분류번호:*/
    @Column(name="brnchNo",nullable = false, columnDefinition = "bigint default 0")
    private Long brnchNo;


    /*공정그룹코드:*/
    @Column(name="procGrpCd",nullable = false, columnDefinition = "bigint default 0")
    private Long procGrpCd;


    /*공정순서:*/
    @Column(name="procSeq",nullable = false, columnDefinition = "int default 0")
    private Integer procSeq;

    /*CCP구분코드(살균,금속검출등...)*/
    @Column(name="ccpTp" , length = 10)
    private String ccpTp;


    /*공정별 최대 생산량:*/
    @Column(name="maxMakeQty",nullable = false, columnDefinition = "numeric default 0")
    private float maxMakeQty;

    /*소요일:종료일사 계산시 시작일자에 더해야 하는 값으로 사용*/
    @Column(name="needDtVal", nullable = false,  precision=5, scale=2 , columnDefinition = "numeric default 0")
    private Integer needDtVal;

    /*공정 시작일계산시 이전공정마지막일자 + 증가값으로 사용*/
    @Column(name="nextStepVal", nullable = false,  precision=5, scale=2 , columnDefinition = "numeric default 0")
    private Integer nextStepVal;

    /*공정률:*/
    @Column(name="procRt",nullable = false, columnDefinition = "numeric default 0" , precision=3, scale=1)
    private float procRt;

    @Column(name="usedYn" ,nullable = false, length = 1)
    private String usedYn;

//    @OneToOne
//    @JoinColumn(name = "proc_cd")
//    private CodeInfo codeInfo;

    /*공정코드 */
//    @Column(name="proc_cd",nullable = false, columnDefinition = "bigint default 0",  updatable=false, insertable=false)
    @Column(name="procCd",nullable = false, columnDefinition = "bigint default 0")
    private Long procCd;


    @Column(name="regId" )
    private Long regId;

    @Column(name="modId" )
    private Long modId;

    @Column(name="regIp" , length = 20)
    private String regIp;

    @Column(name="modIp" , length = 20)
    private String modIp;

    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}
