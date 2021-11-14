package daedan.mes.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class UserHstr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hstrNo")
    private Long hstrNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name = "userId" ,nullable = false ,columnDefinition = "numeric default 0")
    private Long userId;

//    출근 일자
    @Column(name="hstrDt" , nullable = false,length = 50)
    private String hstrDt;
//    출근 시간
    @Column(name="hstrFrDt",nullable = false,length = 50)
    private String hstrFrDt;
//    퇴근 시간
    @Column(name="hstrToDt",nullable = false,length = 50)
    private String hstrToDt;

    @Column(name="used_yn",nullable = false,  columnDefinition = "char(1) default 'Y'")
    private String usedYn;

}
