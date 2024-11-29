package mks.mws.tool.workingtime.entity;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "wt_plancal")
public class PlanCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id", length = 99)
    private String siteId;

    @Column(name = "user_id", length = 99, nullable = false)
    private String userId;

    /** 1: AM; 2: PM; 3: Night */
    @Column(nullable = false)
    private int section;
    
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "from_date")
    private Date fromDate;

    private Boolean mon;

    private Boolean tue;

    private Boolean wed;

    private Boolean thu;

    private Boolean fri;

    private Boolean sat;

    private Boolean sun;

    private String note;
    
    @CreationTimestamp
    @Column(name="created_date")
    Date createdDate;
    
    @UpdateTimestamp
    @Column(name="modified_date")
    Date modified;
}
