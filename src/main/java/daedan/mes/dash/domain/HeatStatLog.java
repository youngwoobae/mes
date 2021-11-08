package daedan.mes.dash.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class HeatStatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="heatStatLogNo",nullable = false)
    private long heatStatLogNo;

    /*고객번호*/
    @Column(name="custNo",nullable = false )
    private Long custNo;

    /*작업장소번호*/
    @Column(name="spotNo",nullable = false )
    private Long spotNo;

    /*발송일자*/
    @Column(name="unix_hms",nullable = false, length = 12)
    private Long unixHms;

    /*수신Msg*/
    @Column(name="rcvMsg", length = 100)
    private String rcvMsg;

    /*작동상태*/
    @Column(name="operYn",nullable = false, length = 1 , columnDefinition = "char default 'N'")
    private String operYn;

    @Column(name="usedYn",nullable = false, length = 1 , columnDefinition = "char default 'Y'")
    private String usedYn;
}
