package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor

public class StkClos {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stk_clos_no", nullable = false, columnDefinition = "numeric")
    private Long stkClosNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*마감일자*/
    @Column(name="clos_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date closDt;

    /*자재번호*/
    @Column(name="matr_no",nullable = false, columnDefinition = "numeric")
    private Long matrNo;

    /*입고수량*/
    @Column(name="matr_iwh_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float matrIwhQty;

    /*출고수량*/
    @Column(name="matr_owh_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float matrOwhQty;

    /*재고수량*/
    @Column(name="matr_stk_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float matrStkQty;



    /*자재번호*/
    @Column(name="prod_no",nullable = false, columnDefinition = "numeric")
    private Long prodNo;

    /*입고수량*/
    @Column(name="prod_iwh_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float prodIwhQty;

    /*출고수량*/
    @Column(name="prod_owh_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float prodOwhQty;

    /*재고수량*/
    @Column(name="prod_stk_qty",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float prodStkQty;




}