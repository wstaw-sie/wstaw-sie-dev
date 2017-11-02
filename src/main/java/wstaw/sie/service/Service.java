package wstaw.sie.service;

import java.util.List;

public interface Service<T> {

	public void save(T elem);
	
	public void delete(T elem);
	
	public List<T> list();
}