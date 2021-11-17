package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class AccHstrEvnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accEnvtNo")
    private Long accEvntNo;

    /*고객관릭번호*/
    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*접속로그관릭번호*/
    @Column(name = "accNo" ,nullable = false, columnDefinition = "numeric default 0")
    private Long accNo;

    /*접속로그이벤트*/
    @Enumerated(EnumType.STRING)
    @Column(name="evntTp",nullable = false, length = 10)
    private EvntType evntTp;

    /*트랜잭션수*/
    @Column(name="trnsCnt",nullable = false, columnDefinition = "numeric default 1")
    private Integer transCnt;

}
