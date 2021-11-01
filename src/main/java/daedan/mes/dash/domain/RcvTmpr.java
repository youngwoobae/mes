package daedan.mes.dash.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class RcvTmpr {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="rcvTmprNo",nullable = false)
    private long rcvTmprNo;

    /*고객번호*/
    @Column(name="custNo",nullable = false )
    private Long custNo;

    /*설비번호*/
    @Column(name="equipNo",nullable = false )
    private Long equipNo;

    /*수신시간*/
    @Column(name="unixHms",nullable = false)
    private Long unixHms;

    /*수신Msg*/
    @Column(name="rcvMsg", length = 100)
    private String rcvMsg;

    /*수신온도*/
    @Column(name="rcvTmpr",nullable = false, columnDefinition = "numeric default 0", precision=6, scale=2)
    private Float rcvTmpr;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;

}
