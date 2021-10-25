package daedan.mes.make.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OperMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="operNo",nullable = false, columnDefinition = "numeric")
    private Long operNo;


    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*작업장설비관번호*/
    @Column(name="spotEquipNo",nullable = false ,columnDefinition = "numeric default 0")
    private Long spotEquipNo;

    /*상품번호*/
    @Column(name="prodNo" ,columnDefinition = "numeric default 0" )
    private Long prodNo;

    /*작업지시번호*/
    @Column(name="indcNo" ,columnDefinition = "numeric default 0" )
    private Long IndcNo;

    /*동작구분 0:정상, 1:(시편)테스트*/
    @Column(name="operTp" ,columnDefinition = "numeric default 0" )
    private Integer operTp;

    /*테스트구분 0:테스트없음, 140:FE 통과, 2:SES통과, 12:FE&SUS통과, 123:FE&SUS&제품통과*/
    @Column(name="test_tp" ,columnDefinition = "numeric default 0" )
    private Integer testTp;

    /*시작시분*/
    @Column(name="frUnixHms" ,columnDefinition = "numeric default 0" )
    private Long frUnixHms;

    /*종료시분*/
    @Column(name="toUnixHms" ,columnDefinition = "numeric default 0" )
    private Long toUnixHms;

    /*정상통과수량*/
    @Column(name="passQty" ,columnDefinition = "numeric default 0" )
    private Float passQty;

    /*사용자보완수량*/
    @Column(name="adjQty" ,columnDefinition = "numeric default 0" )
    private Float adjQty;


    /*금속검출수량*/
    @Column(name="metalQty" ,columnDefinition = "numeric default 0" )
    private Long metalQty;

    /*중량미달수량*/
    @Column(name="weightQty" ,columnDefinition = "numeric default 0" )
    private Long weightQty;

    /*누적시작수량*/
    @Column(name="frQty", columnDefinition = "numeric default 0")
    private Float frQty;

    /*누적종료수량*/
    @Column(name="toQty", columnDefinition = "numeric default 0")
    private Float toQty;

    /*살균온도*/
    @Column(name="sterTmpr", columnDefinition = "numeric default 0")
    private Float sterTmpr;

    /*살균시간*/
    @Column(name="sterTime", columnDefinition = "numeric default 0")
    private Long sterTime;

    //형태
    @Column(name="shape", length = 4000)
    private String shape;

    @Column(name="operText", length = 4000)
    private String operText;

    /*유저식별*/
    @Column(name="userId",nullable = false)
    private Long userId;

    /*장비번호*/
    @Column(name="deviceNo", columnDefinition = "numeric default 0" )
    private Long deviceNo;

    /*장비번호*/
    @Column(name="equipNo", columnDefinition = "numeric default 0")
    private Long equipNo;

    /*시작시간*/
    @Column(name="frHm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frHm;

    /*종료시간*/
    @Column(name="toHm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toHm;

}
