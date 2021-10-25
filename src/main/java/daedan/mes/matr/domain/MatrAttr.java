package daedan.mes.matr.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@Entity
@NoArgsConstructor
public class MatrAttr {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="matrAttrNo",nullable = false, columnDefinition = "numeric")
    private Long matrAttrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*원료번호*/
    @Column(name="matrNo",nullable = false , columnDefinition = "numeric")
    private Long matrNo;

    /*원료분류*/
    @Column(name="matrType",nullable = false , columnDefinition = "numeric")
    private Long matrType;

    /*연근(4,5,6,기타)*/
    @Column(name="howOld" , columnDefinition = "numeric")
    private Long howOld;

    /*진세노(사이드)*/
    @Column(name="jinseno" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float jinseno;

    // 당도 : 최저당도
    @Column(name = "minBrix" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float minBrix;

    // 당도 : 최대당도
    @Column(name = "maxBrix" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float maxBrix;

    // 고형분 :
    @Column(name = "solid" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float solid;

    // 타입(정타입:점도가 높아서 병제품으로 사용  ,원료타입: 벌크원료로 고객사 또는 후공저의 원료로…)
    @Column(name = "usedTp" , columnDefinition = "numeric default 0")
    private Long usedTp;

    // 최소점도 :  (단위:CP : 일반적으로 10000 ~ 35000까지이며 사용자 입력을 처리)
    @Column(name = "minVisco" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float minVisco;

    // 최대점도 :  (단위:CP : 일반적으로 10000 ~ 35000까지이며 사용자 입력을 처리)
    @Column(name = "maxVisco" , columnDefinition = "numeric default 0", precision=6, scale=1)
    private Float maxVisco;

    // 색도 : (단위:ABS: 1…9까지로 표현하며 소숫점 1자리까지)
    @Column(name = "chroma" , columnDefinition = "numeric default 0", precision=3, scale=1)
    private Float chroma;

    /*사용여부*/
    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
