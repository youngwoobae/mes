package daedan.mes.io.domain;

/*상품출고정보*/

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdOwh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="owh_no",nullable = false, columnDefinition = "numeric")
    private Long owhNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*입고번호 : 제품유효기간관ㄹ에서 사용하기 위함:2021.11.03 추가  */
    @Column(name="iwhNo", columnDefinition = "numeric default 0")
    private Long iwhNo;

    /*주문번호*/
    @Column(name="ord_no",nullable = false, columnDefinition = "numeric")
    private Long ordNo;

    /*회사번호*/
    @Column(name="cmpy_no",nullable = false, columnDefinition = "numeric")
    private Long cmpyNo;

    /*품번*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*검수자id*/
    @Column(name="inspEr", columnDefinition = "numeric default 0")
    private Long inspEr;

    /*파레트코드*/
    @Column(name="paltCd", columnDefinition = "numeric default 0")
    private Long paltCd;

    /*출고일자*/
    @Column(name="owh_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhDt;

    /*출고수량*/
    @Column(name="owh_qty", nullable = false, columnDefinition = "numeric default 0" )
    private Float owhQty;

    /*출고요청수량*/
    @Column(name="owh_req_qty", nullable = false, columnDefinition = "numeric default 0" )
    private Float owhReqQty;

    /*출고단위*/
    @Column(name="owh_unit",nullable = false, columnDefinition = "numeric")
    private Long owhUnit;

    /*출고요청일자*/
    @Column(name="owh_req_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date owhReqDt;

    @Column(name="reg_id", columnDefinition = "numeric")
    private Long regId;

    @Column(name="mod_id", columnDefinition = "numeric")
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

}
