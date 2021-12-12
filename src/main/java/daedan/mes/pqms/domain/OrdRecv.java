package daedan.mes.pqms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OrdRecv {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ord_recv_no",nullable = false, columnDefinition = "numeric")
    private Long ordRecvNo;

    @Column(name="cust_no",nullable = false, columnDefinition = "numeric default 0")
    private Long custNo;

    /*발송고객번호*/
    @Column(name="cmpy_no",nullable = false, columnDefinition = "numeric")
    private Long cmpyNo;

    /*발송처에서 데이터를 전송한 일시*/
    @Column(name = "send_ut",nullable = false, columnDefinition = "numeric")
    private Long sendUt;

    /*제품요청일시*/
    @Column(name = "req_ut",nullable = false, columnDefinition = "numeric")
    private Long reqUt;

    /*번호코드*/
    @Column(name="prod_no",nullable = false , length = 20 )
    private Long prodNo;


    /*요청수량*/
    @Column(name="req_qty",nullable = false, columnDefinition = "numeric default 0")
    private Integer reqQty;

    /*처리상태*/
    @Column(name="proc_sts",nullable = false, columnDefinition = "numeric default 0")
    private Long procSts;

    @Column(name="used_yn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
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
