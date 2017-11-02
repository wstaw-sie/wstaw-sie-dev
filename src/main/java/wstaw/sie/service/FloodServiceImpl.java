package wstaw.sie.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import wstaw.sie.config.GeneralConfig;
import wstaw.sie.model.entity.Flood;
import wstaw.sie.repository.FloodRepository;
import wstaw.sie.util.TimeUtil;

@Service
public class FloodServiceImpl implements FloodService {

  @Resource
  FloodRepository floodRepository;

  @Override
  public void save(Flood elem) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void delete(Flood elem) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Flood> list() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addFloodEntry() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isIPBlocked(String ipaddress) {
    Flood flood =  floodRepository.findByIp(ipaddress);
    return !(null == flood 
	    		|| TimeUtil.isDifferenceBiggerThan(flood.getDate(), new Date(), GeneralConfig.FLOOD_TIME_DELAY_IN_SECONDS_TO_REMOVE_BLOCKADE) 
	    		|| flood.getCounter() < GeneralConfig.FLOOD_MAX_COUNT_OF_LOGIN_FAILURES
    		);
  }

  @Override
  public void updateFloodTableInCaseOfBadCredentials(String ipaddress) {
    Flood flood =  floodRepository.findByIp(ipaddress);
    if(null != flood)
    {
    	if(TimeUtil.isDifferenceBiggerThan(flood.getDate(), new Date(), GeneralConfig.FLOOD_TIME_DELAY_IN_SECONDS_TO_REMOVE_BLOCKADE))
    	{//somebody has errors in login in the past
    		flood.setCounter((byte)1);
    	}
    	else
    	{//somebody has errors in login but it happened not so far ago - less than time to remove blockade
    		flood.incrementCounter();
    	}
        flood.setDate(new Date());
        floodRepository.saveAndFlush(flood);

    }
    else
    {
      Flood newEntry = new Flood(ipaddress, new Date());
      newEntry.incrementCounter();
      floodRepository.saveAndFlush(newEntry);
    }
    
  }
  
}
