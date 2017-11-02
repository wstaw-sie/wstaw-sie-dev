package wstaw.sie.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flood")
public class Flood {

  public Flood()
  {
    
  }
  
  public Flood(String ip, Date date) {
    super();
    this.ip = ip;
    this.date = date;
  }
  
  public Flood(Integer id, String ip, Date date) {
	    super();
	    this.id = id;
	    this.ip = ip;
	    this.date = date;
	  }
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column (name ="id")
  private Integer id;

  @Column (name ="ip", unique = true)
  private String ip;
  
  @Column (name ="date")
  private Date date;
  
  @Column (name ="counter")
  private byte counter;


  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public byte getCounter() {
    return counter;
  }
  public void setCounter(byte counter) {
    this.counter = counter;
  }
  
  public void incrementCounter()
  {
    this.counter++;
  }
}
