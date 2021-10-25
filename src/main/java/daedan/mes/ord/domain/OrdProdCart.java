package daedan.mes.ord.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class OrdProdCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_no",nullable = false, columnDefinition = "numeric")
    private Long cartNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="userId",nullable = false, columnDefinition = "numeric")
    private Long userId;

    @Column(name="ord_no",nullable = false,columnDefinition = "numeric default 0" )
    private Long ordNo;

     @Column(name="prod_no",nullable = false,columnDefinition = "numeric default 0" )
    private Long prodNo;

    @Column(name="ord_qty",nullable = false, precision=10, scale=2 )
    private Float ordQty;

    /*주문규격*/
    @Column(name="ord_sz",nullable = false , length = 100 )
    private String ordSz;

    /*주문모델명*/
    @Column(name="ord_modl_nm", length = 100 )
    private String ordModlNm;

    /*판매단위*/
    @Column(name="sale_unit",nullable = false , columnDefinition = "numeric" )
    private Long saleUnit;

    /*포장단위수량*/
    @Column(name="qty_per_pkg",nullable = false ,  columnDefinition = "int default 0" )
    private Integer qtyPerPkg;

    @Column(name="dlv_qty",nullable = true,columnDefinition = "int default 0" )
    private int dlvQty;

    @Column(name="dlv_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dlvDt;


}
