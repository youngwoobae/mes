package daedan.mes.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class WhTmpr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tmpr_no",nullable = false, columnDefinition = "numeric")
    private Long tmprNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*창고번호*/
    @Column(name="wh_no",nullable = false, columnDefinition = "numeric")
    private Long whNo;

    /*측정일시*/
    @Column(name="chk_dt",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date chkDt;

    /*측정온도*/
    @Column(name="curr_tmpr",nullable = false, precision=8, scale=2)
    private  float currTmpr;
    /*최저온도*/
    @Column(name="min_tmpr",nullable = false, precision=8, scale=2)
    private  float minTmpr;

    /*최고온도*/
    @Column(name="avg_tmpr",nullable = false, precision=8, scale=2)
    private  float avgTmpr;

    /*최고온도*/
    @Column(name="max_tmpr",nullable = false, precision=8, scale=2)
    private  float maxTmpr;


}
