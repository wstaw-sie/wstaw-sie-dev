package wstaw.sie.service;

import wstaw.sie.model.entity.Flood;

public interface FloodService extends Service<Flood>{

  public void addFloodEntry();

  public boolean isIPBlocked(String ipaddress);

  public void updateFloodTableInCaseOfBadCredentials(String ipaddress);
	
}
