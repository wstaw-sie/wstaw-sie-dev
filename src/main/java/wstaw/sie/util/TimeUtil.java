package wstaw.sie.util;

import java.util.Date;

public class TimeUtil {

	  /**
	   * @param differenceLimit time in seconds
	   * @return
	   */
	 public static boolean isDifferenceBiggerThan(Date previous, Date current, int differenceLimit)
	 {
	   return (current.getTime() - previous.getTime())/1000l > differenceLimit;
	 }
}
