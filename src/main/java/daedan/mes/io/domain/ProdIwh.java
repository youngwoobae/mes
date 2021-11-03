package daedan.mes.io.domain;

/*상품입고정보*/

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class ProdIwh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="iwh_no",nullable = false, columnDefinition = "numeric")
    private Long iwhNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*생산결과관리번호*/
    @Column(name="indc_rslt_no",columnDefinition = "numeric default 0")
    private Long indcRsltNo;

    /*품번*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*입고일자*/
    @Column(name="iwh_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date iwhDt;

    /*입고수량*/
    @Column(name="iwh_qty",nullable = false, precision=10, scale=2)
    private Float iwhQty;



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

    /*검수자id*/
    @Column(name="inspEr", columnDefinition = "numeric default 0")
    private Long inspEr;

    /*파레트코드*/
    @Column(name="paltCd", columnDefinition = "numeric default 0")
    private Long paltCd;

}
