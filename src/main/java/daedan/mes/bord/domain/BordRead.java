package daedan.mes.bord.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BordRead {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="read_no",nullable = false)
    private Long readNo;

    /*게시번호*/
    @Column(name="bord_no",nullable = false )
    private Long bordNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    /*사용자 이름*/
    @Column(name="user_id",nullable = false )
    private Long userId;

    /*게시확인일자*/
    @Column(name="cnfm_dt",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date cnfmDt;

}
