package wstaw.sie.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class UploadedFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "bytes")
	private byte[] bytes;

	@Column(name = "filenameWithPath")
	private String filenameWithPath;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getFilenameWithPath() {
		return filenameWithPath;
	}

	public void setFilenameWithPath(String filenameWithPath) {
		this.filenameWithPath = filenameWithPath;
	}

}
