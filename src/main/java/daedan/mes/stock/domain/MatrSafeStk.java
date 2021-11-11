package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MatrSafeStk {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="safe_stk_no",nullable = false)
    private Long safeStkNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="matr_no",nullable = false)
    private Long matrNo;

    @Column(name="jan_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long janSafeStk;

    @Column(name="feb_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long febSafeStk;

    @Column(name="mar_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long marSafeStk;

    @Column(name="apr_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long aprSafeStk;

    @Column(name="may_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long maySafeStk;

    @Column(name="jun_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long junSafeStk;

    @Column(name="jul_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long julSafeStk;

    @Column(name="aug_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long augSafeStk;

    @Column(name="sep_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long sepSafeStk;

    @Column(name="oct_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long octSafeStk;

    @Column(name="nov_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long novSafeStk;

    @Column(name="dec_stk_qty",nullable = false, columnDefinition = "bigint default 0" )
    private Long decSafeStk;

    @Column(name="used_yn",nullable = false, length = 1, columnDefinition = "char default 'Y'")
    private String usedYn;


    @Column(name="regId", columnDefinition = "numeric")
    private Long regId;

    @Column(name="modId", columnDefinition = "numeric")
    private Long modId;

    @Column(name="regIp", length = 20)
    private String regIp;

    @Column(name="modIp", length = 20)
    private String modIp;


    @Column(name="regDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="modDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;
}
