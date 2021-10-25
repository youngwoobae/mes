package daedan.mes.equip.domain.yyjg;

import javax.persistence.*;
import java.util.Date;

public class InitCounter {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="initCounterNo",nullable = false)
    private long initCounterNo;
    /*처리일자*/
    @Column(name="proc_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date procDt;

    /*시작수량*/
    @Column(name="passQty1",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float passQty;

    @Column(name="passQty2",nullable = false, precision=10, scale=2 ,columnDefinition = "numeric default 0")
    private Float passQt2;

}
