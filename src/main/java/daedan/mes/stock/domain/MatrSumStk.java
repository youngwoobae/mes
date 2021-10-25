package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrSumStk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="stk_no",nullable = false)
    private Long stkNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*원부자재번호*/
    @Column(name="matr_no",nullable = false)
    private Long matrNo;

    /*재고수량*/
    @Column(name="stk_qty",nullable = false ,columnDefinition = "int default 0")
    private Integer stkQty;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;

    @Column(name="reg_id")
    private Long regId;

    @Column(name="mod_id")
    private Long modId;

    @Column(name="reg_ip")
    private String regIp;

    @Column(name="mod_ip")
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;

    @Column(name="mod_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

}
