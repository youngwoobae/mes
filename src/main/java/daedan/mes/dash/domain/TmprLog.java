package daedan.mes.dash.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class TmprLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tmprLogNo",nullable = false)
    private long tmprLogNo;

    /*고객번호*/
    @Column(name="custNo",nullable = false )
    private Long custNo;

    /*작업장소번호*/
    @Column(name="spotNo",nullable = false )
    private Long spotNo;

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
