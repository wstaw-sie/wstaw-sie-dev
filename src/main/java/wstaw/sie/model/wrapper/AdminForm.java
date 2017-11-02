package wstaw.sie.model.wrapper;

import javax.validation.constraints.Min;

public class AdminForm {

	@Min(0)
	private Integer id_resetowanego;

	@Min(0)
	private Integer id_resetowanego_od;

	@Min(0)
	private Integer id_resetowanego_do;

	private String reset_ile;

	private Boolean mail_notification = false;

	private String message;

	public Integer getId_resetowanego() {
		return id_resetowanego;
	}

	public void setId_resetowanego(Integer id_resetowanego) {
		this.id_resetowanego = id_resetowanego;
	}

	public Integer getId_resetowanego_od() {
		return id_resetowanego_od;
	}

	public void setId_resetowanego_od(Integer id_resetowanego_od) {
		this.id_resetowanego_od = id_resetowanego_od;
	}

	public Integer getId_resetowanego_do() {
		return id_resetowanego_do;
	}

	public void setId_resetowanego_do(Integer id_resetowanego_do) {
		this.id_resetowanego_do = id_resetowanego_do;
	}

	public Boolean getMail_notification() {
		return mail_notification;
	}

	public void setMail_notification(Boolean mail_notification) {
		this.mail_notification = mail_notification;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReset_ile() {
		return reset_ile;
	}

	public void setReset_ile(String reset_ile) {
		this.reset_ile = reset_ile;
	}

}
