package wstaw.sie.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@Entity
//@Table(name = "archive")
public class HistoryDraw {

	/*@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;*/

	//@Column(name = "date")
	private Date drawDate;
	
	//@ManyToOne(targetEntity = User.class)
	//@JoinColumn(name = "first_prayer", referencedColumnName = "id")
	private User firstPrayer;
	
	//@ManyToOne(targetEntity = User.class)
	//@JoinColumn(name = "second_prayer", referencedColumnName = "id")
	private User secondPrayer;

	/*
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}*/

	public Date getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Date drawDate) {
		this.drawDate = drawDate;
	}

	public User getFirstPrayer() {
		return firstPrayer;
	}

	public void setFirstPrayer(User firstPrayer) {
		this.firstPrayer = firstPrayer;
	}

	public User getSecondPrayer() {
		return secondPrayer;
	}

	public void setSecondPrayer(User secondPrayer) {
		this.secondPrayer = secondPrayer;
	}

	@Override
	public String toString() {
		return "HistoryDraw [drawDate=" + drawDate
				+ ", firstPrayer=" + firstPrayer + ", secondPrayer="
				+ secondPrayer + "]";
	}
	
}
