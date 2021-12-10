package daedan.mes.make.domain.youjin;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class MetalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="metalHstrNo",nullable = false, columnDefinition = "numeric")
    private Long metalHstrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*설치작업장번호*/
    @Column(name="spotNo",nullable = false )
    private Long spotNo;

    /*작업구분 1 : FE, 2 : SUS , 3 : 제품 , 4 : 제품+FE , 5 : 제품+SUS */
    @Column(name="stepNo",nullable = false, columnDefinition = "numeric default 0")
    private Integer stepNo;

    /*제품번호*/
    @Column(name="prodNo",nullable = false)
    private Long prodNo;

    /*작업자*/
    @Column(name="workEr",nullable = false)
    private Long workEr;

    /*수신시간*/
    @Column(name="unixHms",nullable = false)
    private Long unixHms;


    /*수신Msg*/
    @Column(name="rcvMsg", length = 100)
    private String rcvMsg;

    /*통과수량*/
    @Column(name="passQty",nullable = false, columnDefinition = "numeric default 0")
    private Integer passQty;

    /*불량수량*/
    @Column(name="errQty",nullable = false, columnDefinition = "numeric default 0")
    private Integer errQty;

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

    @Column(name="event_yn",nullable = false, length = 1 , columnDefinition = "char default 'N'")
    private String eventYn;
}
