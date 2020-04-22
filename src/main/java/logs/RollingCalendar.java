package logs;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Created by jiaxin on 2018/11/6.
 * LOG4J日志打印配置类
 */
public class RollingCalendar extends GregorianCalendar
{
	  private static final long serialVersionUID = 3220591301939207891L;
	  private int type = -1;
	  
	  RollingCalendar() {}
	  
	  RollingCalendar(TimeZone paramTimeZone, Locale paramLocale)
	  {
	    super(paramTimeZone, paramLocale);
	  }
	  
	  void setType(int paramInt)
	  {
	    this.type = paramInt;
	  }
	  
	  public int getType()
	  {
	    return this.type;
	  }
	  
	  long getNextCheckMillis(Date paramDate)
	  {
	    return getNextCheckDate(paramDate).getTime();
	  }
	  
	  private Date getNextCheckDate(Date paramDate)
	  {
	    setTime(paramDate);
	    switch (this.type)
	    {
	    case 0: 
	      set(13, 0);
	      set(14, 0);
	      add(12, 1);
	      break;
	    case 1: 
	      set(12, 0);
	      set(13, 0);
	      set(14, 0);
	      add(11, 1);
	      break;
	    case 2: 
	      set(12, 0);
	      set(13, 0);
	      set(14, 0);
	      int i = get(11);
	      if (i < 12)
	      {
	        set(11, 12);
	      }
	      else
	      {
	        set(11, 0);
	        add(5, 1);
	      }
	      break;
	    case 3: 
	      set(11, 0);
	      set(12, 0);
	      set(13, 0);
	      set(14, 0);
	      add(5, 1);
	      break;
	    case 4: 
	      set(7, getFirstDayOfWeek());
	      set(11, 0);
	      set(13, 0);
	      set(14, 0);
	      add(3, 1);
	      break;
	    case 5: 
	      set(5, 1);
	      set(11, 0);
	      set(13, 0);
	      set(14, 0);
	      add(2, 1);
	      break;
	    default: 
	      throw new IllegalStateException("Unknown periodicity type.");
	    }
	    return getTime();
	  }
	}
