package wstaw.sie.util;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("pathBean")
public class PathBean {
	
	@Autowired
	private ServletContext servletContext;
	
	public String getPath(String path)
	{
		return servletContext.getContextPath()+"/"+path;
	}

}
