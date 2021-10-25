package daedan.mes.dept.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class DeptInfo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="dept_no",nullable = false)
    private long deptNo;

    @Column(name="par_dept_no",nullable = false)
    private long parDeptNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="dept_nm",nullable = false, length = 250)
    private String deptNm;

    @Column(name="prt_seq",nullable = false, columnDefinition = "int default 0")
    private long prtSeq;

    @Column(name="reg_id", length = 10)
    private Long regId;

    @Column(name="mod_id",length = 10)
    private Long modId;

    @Column(name="reg_ip", length = 20)
    private String regIp;

    @Column(name="mod_ip", length = 20)
    private String modIp;

    @Column(name="reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDt;


    @Column(name="mod_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modDt;

    @Column(name="used_yn",nullable = false, length = 1)
    private String usedYn;

}
