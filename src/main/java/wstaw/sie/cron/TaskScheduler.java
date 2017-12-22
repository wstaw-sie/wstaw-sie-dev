package wstaw.sie.cron;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import wstaw.sie.model.entity.Parameter;
import wstaw.sie.repository.ParameterRepository;
import drawer.com.logic.Draw;

@Component
public class TaskScheduler {

	@Autowired
	private Draw draw;

	@Resource
	ParameterRepository parameterRepository;

	private static Logger logger = Logger.getLogger(TaskScheduler.class);

	@Scheduled(cron = "0 0 22 ? * SAT")
	public void play() {
		
		Parameter parameter = parameterRepository.findByName("IS_RUNNING");
		if(parameter != null && parameter.getParamValue() != null && parameter.getParamValue().equalsIgnoreCase("y"))
		{
			logger.info("Start draw scheduler");
			draw.play();
			logger.info("Finish draw scheduler");
		}
		else
		{
			logger.info("Parameter IS_RUNNING is not set or has other value than 'y' - drawer is turned off...");
		}
	}
}
