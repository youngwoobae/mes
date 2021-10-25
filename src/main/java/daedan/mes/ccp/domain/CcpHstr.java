package daedan.mes.ccp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class CcpHstr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ccp_hstr_no")
    private Long ccpHstrNo;

    @Column(name="ccp_no")
    private Long ccpNo;

    @Column(name="custNo", columnDefinition = "numeric default 0")
    private Long custNo;

    @Column(name="abn_ctnt" , length = 4096)
    private String abnCtnt;

    @Column(name="react_ctnt" , length = 4096)
    private String reactCtnt;

}
