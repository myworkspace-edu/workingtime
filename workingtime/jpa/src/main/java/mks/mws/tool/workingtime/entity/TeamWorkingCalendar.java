package mks.mws.tool.workingtime.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "wt_teamcal")
public class TeamWorkingCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String account;

    @Column(length = 2, nullable = false)
    private String section;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(length = 1, nullable = false)
    private String mon;

    @Column(length = 1, nullable = false)
    private String tue;

    @Column(length = 1, nullable = false)
    private String wed;

    @Column(length = 1, nullable = false)
    private String thur;

    @Column(length = 1, nullable = false)
    private String fri;

    @Column(length = 1, nullable = false)
    private String sat;

    @Column(length = 1, nullable = false)
    private String sun;

    private String note;

}
