package daedan.mes.dash.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class EvtMsg {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="msg_no",nullable = false, columnDefinition = "numeric")
    private Long msgNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*메세지유형:SMS,PUSH*/
    @Enumerated(EnumType.STRING)
    @Column(name="msg_tp",nullable = false, length = 10)
    private daedan.mes.dash.domain.MsgType msgTp;

    /*발송일자*/
    @Column(name="unix_hms",nullable = false, length = 12)
    private String unixHms;

    /*발송일자*/
    @Column(name="send_dt" ,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDt;

    /*발송내용*/
    @Column(name="msg_cont",nullable = false, length = 1000)
    private String msgCont;

}
