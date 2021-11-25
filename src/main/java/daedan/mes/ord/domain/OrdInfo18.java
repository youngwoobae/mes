package daedan.mes.ord.domain;

import daedan.mes.matr.domain.MatrAttr;
import daedan.mes.ord.domain.OrdInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OrdInfo18  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ordMhNo",nullable = false, columnDefinition = "numeric")
    private Long ordMhNo;

    //주문정보
    @Column(name="ordNo",nullable = false, columnDefinition = "numeric")
    private Long ordNo;

    /*고객사번호*/
    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*주문고객명:상기고객사의 고객사명이 아님에 주의할 것*/
    @Column(name="ordCustNm",nullable = false , length=100)
    private String ordCustNm;

    /*주문고객 전화번호*/
    @Column(name="ordCustCellNo",nullable = false , length=20)
    private String ordCustCellNo;

    /*수령자명*/
    @Column(name="rcvNm",nullable = false , length=100)
    private String rcvNm;

    /*수령자 전화번호*/
    @Column(name="rcvCellNo",nullable = false , length=20)
    private String rcvCellNo;

    /*수령자주소*/
    @Column(name="rcvAddr", nullable = false , length=200)
    private String rcvAddr;

    /*수령방법_코드*/
    @Column(name="rcvTp",nullable = false , columnDefinition = "numeric")
    private Long rcvTp;

    /*배송(수령)시간*/
    @Column(name="rcvHr", nullable = false ,columnDefinition = "numeric default '09'")
    private String rcvHr;

    /*배송(수령)분*/
    @Column(name="rcvMi", nullable = false ,columnDefinition = "numeric default '00'")
    private String rcvMi;

    /*포장방법_코드(보자기)*/
    @Column(name="pkgTp", nullable = false, columnDefinition = "numeric default 0")
    private Long pkgTp;

    /*시트(흑임자)*/
    @Column(name="sheetRmk", nullable = false, length = 100)
    private String sheetRmk;

    /*필링*/
    @Column(name="fillRmk", length = 100)
    private String fillRmk;

    /*문구*/
    @Column(name="txtRmk", length = 255)
    private String txtRmk;

    /*메모*/
    @Column(name="noteRmk", length = 255)
    private String noteRmk;


    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

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


}
