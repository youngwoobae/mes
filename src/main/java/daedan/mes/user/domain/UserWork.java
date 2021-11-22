package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class UserWork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "workNo")
    private Long workNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name = "userId" ,nullable = false ,columnDefinition = "numeric default 0")
    private Long userId;

//   근무일자
    @Column(name="workDt" , nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date workDt;

//    출근 시간
    @Column(name="workFrTm",nullable = false,length = 5)
    private String workFrTm;
//    퇴근 시간
    @Column(name="workToTm",nullable = false,length = 5)
    private String workToTm;

    @Column(name="usedYn",nullable = false,  columnDefinition = "char(1) default 'Y'")
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
