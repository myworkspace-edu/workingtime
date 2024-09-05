package mks.mws.tool.workingtime.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wt_task", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@NoArgsConstructor
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "site_id", length = 99)
	private String siteId;

	@Column(name = "date")
	private Date date;

	@Column(name = "deadline")
	private Date deadline;

	@Column(length = 32)
	private String cat;

	@Column(length = 255)
	private String name;

	@Column(length = 255)
	private String taskname;

	@Column(length = 255)
	private String product;

	@Column(length = 64)
	private String pic;

	@Column(length = 64)
	private String project;

	/**
	 * 0: Open 1: Doing 2: Done
	 */
	private Integer status;

	@Column(name = "note", length = 1024)
	private String note;

//    @Lob
//    @Type(type = "jsonb") // Use the type definition here
//    @Column(columnDefinition = "jsonb")
//    private Map<String, Object> dynamicColumns;

	public Task(Long id, String cat, Date date, String name, String taskname, String product, String pic,
			Integer status, String note, Date deadline, String project) {
		this.id = id;
		this.date = date;
		this.name = name;
		this.cat = cat;
		this.taskname = taskname;
		this.product = product;
		this.pic = pic;
		this.status = status;
		this.deadline = deadline;
		this.project = project;
		this.note = note;
	}
}

//@Entity
//@Table(name="wt_task", uniqueConstraints=@UniqueConstraint(columnNames = "id"))
//@Getter @Setter
//@NoArgsConstructor
//public class Task implements Serializable {
//
//	@Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//  
//  @Column(name = "site_id", length = 99)
//  private String siteId;
//
//  @Column(length = 32)
//  private String cat;
//  
//  @Column(length = 255)
//  private String name;
//  
//  @Column(length = 255)
//  private String product;
//  
//  @Column(length = 64)
//  private String pic;
//
//  /** 
//   * 0: Open
//   * 1: Doing
//   * 2: Done
//   */
//  private Integer status;
//
//  @Column(name = "note", length = 1024)
//  private String note;    
//
//  public Task(Long id, String cat, String name, String product, String pic, Integer status, String note) {
//  	this.id = id;
//		this.cat = cat;
//		this.name = name;
//		this.product = product;
//		this.pic = pic;
//		this.status = status;
//		this.note = note;
//	}
//
//}
