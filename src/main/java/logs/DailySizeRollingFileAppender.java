package logs;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by jiaxin on 2018/11/6.
 * LOG4J日志打印配置类
 */
public class DailySizeRollingFileAppender
		extends FileAppender {
	static final int TOP_OF_TROUBLE = -1;
	static final int TOP_OF_MINUTE = 0;
	static final int TOP_OF_HOUR = 1;
	static final int HALF_DAY = 2;
	static final int TOP_OF_DAY = 3;
	static final int TOP_OF_WEEK = 4;
	static final int TOP_OF_MONTH = 5;
	private String datePattern = "'.'yyyy-MM-dd";
	private String scheduledFilename;
	private long nextCheck = System.currentTimeMillis() - 1L;
	Date now = new Date();
	SimpleDateFormat sdf;
	RollingCalendar rc = new RollingCalendar();
	int checkPeriod = -1;
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
	String fileSuffix;
	protected long maxFileSize = 67108864L;
	private int fileNmDateIndex = -1;
	private int fileNmDateLen = -1;

	public long getMaximumFileSize()
	{
		return this.maxFileSize;
	}

	public void setMaximumFileSize(long paramLong)
	{
		this.maxFileSize = paramLong;
	}

	public void setMaxFileSize(String paramString)
	{
		this.maxFileSize = OptionConverter.toFileSize(paramString, this.maxFileSize + 1L);
	}

	public void setDatePattern(String paramString)
	{
		this.datePattern = paramString;
	}

	public String getDatePattern()
	{
		return this.datePattern;
	}

	public DailySizeRollingFileAppender() {}

	public DailySizeRollingFileAppender(Layout paramLayout, String paramString1, String paramString2)
			throws IOException
	{
		super(paramLayout, paramString1, true);
		this.datePattern = paramString2;
		activateOptions();
	}

	public void activateOptions()
	{
		super.activateOptions();
		if ((this.datePattern != null) && (this.fileName != null))
		{
			this.now.setTime(System.currentTimeMillis());
			int i = computeCheckPeriod();
			printPeriodicity(i);
			this.rc.setType(i);
			File localFile = new File(this.fileName);
			this.scheduledFilename = (this.fileName + getRollingFileSuffix(new Date(localFile.lastModified())));
		}
		else
		{
			LogLog.error("Either File or DatePattern options are not set for appender [" + this.name + "].");
		}
	}

	void printPeriodicity(int paramInt)
	{
		switch (paramInt)
		{
			case 0:
				LogLog.debug("Appender [" + this.name + "] to be rolled every minute.");
				break;
			case 1:
				LogLog.debug("Appender [" + this.name + "] to be rolled on top of every hour.");
				break;
			case 2:
				LogLog.debug("Appender [" + this.name + "] to be rolled at midday and midnight.");
				break;
			case 3:
				LogLog.debug("Appender [" + this.name + "] to be rolled at midnight.");
				break;
			case 4:
				LogLog.debug("Appender [" + this.name + "] to be rolled at start of week.");
				break;
			case 5:
				LogLog.debug("Appender [" + this.name + "] to be rolled at start of every month.");
				break;
			default:
				LogLog.warn("Unknown periodicity for appender [" + this.name + "].");
		}
	}

	int computeCheckPeriod()
	{
		RollingCalendar localRollingCalendar = new RollingCalendar(gmtTimeZone, Locale.ENGLISH);

		Date localDate1 = new Date(0L);
		if (this.datePattern != null) {
			for (int i = 0; i <= 5; i++)
			{
				SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(this.datePattern);
				localSimpleDateFormat.setTimeZone(gmtTimeZone);
				String str1 = localSimpleDateFormat.format(localDate1);
				localRollingCalendar.setType(i);
				Date localDate2 = new Date(localRollingCalendar.getNextCheckMillis(localDate1));
				String str2 = localSimpleDateFormat.format(localDate2);
				if ((str1 != null) && (str2 != null) && (!str1.equals(str2))) {
					return i;
				}
			}
		}
		return -1;
	}

	void rollOver()
			throws IOException
	{
		if (this.datePattern == null)
		{
			this.errorHandler.error("Missing DatePattern option in rollOver().");
			return;
		}
		String str1 = getRollingFileSuffix(this.now);
		String str2 = this.fileName + str1;
		if (this.scheduledFilename.equals(str2)) {
			return;
		}
		closeFile();

		File localFile1 = new File(this.scheduledFilename);
		if (localFile1.exists()) {
			localFile1.delete();
		}
		File localFile2 = new File(this.fileName);
		boolean bool = localFile2.renameTo(localFile1);
		if (bool) {
			LogLog.debug(this.fileName + " -> " + this.scheduledFilename);
		} else {
			LogLog.error("Failed to rename [" + this.fileName + "] to [" + this.scheduledFilename + "].");
		}
		try
		{
			setFile(this.fileName, false, this.bufferedIO, this.bufferSize);
		}
		catch (IOException localIOException)
		{
			this.errorHandler.error("setFile(" + this.fileName + ", false) call failed.");
		}
		this.scheduledFilename = (this.fileName + str1);
	}

	public synchronized void setFile(String paramString, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
			throws IOException
	{
		Date localDate = new Date();
		String str;
		Object localObject;
		if (paramString.contains("YYYY-MM-DD"))
		{
			this.fileNmDateIndex = paramString.indexOf("YYYY-MM-DD");
			this.fileNmDateLen = "YYYY-MM-DD".length();
			str = new SimpleDateFormat("yyyy-MM-dd").format(localDate);
			paramString = paramString.replaceAll("YYYY-MM-DD", str);
		}
		else if (paramString.contains("YYYYMMDD"))
		{
			this.fileNmDateIndex = paramString.indexOf("YYYYMMDD");
			this.fileNmDateLen = "YYYYMMDD".length();
			str = new SimpleDateFormat("yyyyMMdd").format(localDate);
			paramString = paramString.replaceAll("YYYYMMDD", str);
		}
		else if (this.fileNmDateLen == "YYYY-MM-DD".length())
		{
			str = new SimpleDateFormat("yyyy-MM-dd").format(localDate);
			localObject = paramString.substring(this.fileNmDateIndex, this.fileNmDateIndex + this.fileNmDateLen);
			if (!((String)localObject).equals(str)) {
				paramString = paramString.replaceAll((String)localObject, str);
			}
		}
		else if (this.fileNmDateLen == "YYYYMMDD".length())
		{
			str = new SimpleDateFormat("yyyyMMdd").format(localDate);
			localObject = paramString.substring(this.fileNmDateIndex, this.fileNmDateIndex + this.fileNmDateLen);
			if (!((String)localObject).equals(str)) {
				paramString = paramString.replaceAll((String)localObject, str);
			}
		}
		int i = paramString.lastIndexOf("/");
		if (-1 != i)
		{
			localObject = new File(paramString.substring(0, i));
			if (!((File)localObject).exists()) {
				((File)localObject).mkdirs();
			}
		}
		super.setFile(paramString, paramBoolean1, this.bufferedIO, this.bufferSize);
		if (paramBoolean1)
		{
			localObject = new File(paramString);
			((CountingQuietWriter)this.qw).setCount(((File)localObject).length());
		}
	}

	protected void setQWForFiles(Writer paramWriter)
	{
		this.qw = new CountingQuietWriter(paramWriter, this.errorHandler);
	}

	private synchronized void switchFileName()
	{
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("'.'yyyy-MM-dd-HHmmss");
		String str = this.fileName + localSimpleDateFormat.format(new Date()) + this.fileSuffix;
		File localFile1 = new File(str);
		if (localFile1.exists()) {
			localFile1.delete();
		}
		closeFile();

		File localFile2 = new File(this.fileName);
		boolean bool = localFile2.renameTo(localFile1);
		if (bool) {
			LogLog.debug(this.fileName + " -> " + str);
		} else {
			LogLog.error("Failed to rename [" + this.fileName + "] to [" + str + "].");
		}
		try
		{
			setFile(this.fileName, false, this.bufferedIO, this.bufferSize);
		}
		catch (IOException localIOException)
		{
			this.errorHandler.error("setFile(" + this.fileName + ", false) call failed.");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String getRollingFileSuffix(Date paramDate)
	{
		if (null == this.sdf)
		{
			Object localObject = new ArrayList<Object>();
			String str = this.datePattern;
			int i = str.lastIndexOf('\'');
			while (i == str.length() - 1)
			{
				str = str.substring(0, i);
				int j = str.lastIndexOf('\'');
				if (j == str.length() - 1) {
					((ArrayList)localObject).add(str.substring(j));
				} else {
					((ArrayList)localObject).add(str.substring(j + 1));
				}
				str = str.substring(0, j);
				i = str.lastIndexOf('\'');
			}
			this.sdf = new SimpleDateFormat(str);
			StringBuilder localStringBuilder = new StringBuilder();
			for (int k = ((ArrayList)localObject).size() - 1; k >= 0; k--) {
				localStringBuilder.append((String)((ArrayList)localObject).get(k));
			}
			this.fileSuffix = localStringBuilder.toString();
		}
		Object localObject = this.sdf.format(paramDate);
		switch (this.rc.getType())
		{
			case 0:
				break;
			case 1:
				localObject = (String)localObject + "5959";
				break;
			case 2:
				break;
			case 3:
				localObject = (String)localObject + "-235959";
				break;
			case 4:
				break;
			case 5:
				break;
			default:
				throw new IllegalStateException("Unknown periodicity type.");
		}
		localObject = (String)localObject + this.fileSuffix;
		return (String)localObject;
	}

	protected void subAppend(LoggingEvent paramLoggingEvent)
	{
		long l = System.currentTimeMillis();
		if (l >= this.nextCheck)
		{
			this.now.setTime(l);
			this.nextCheck = this.rc.getNextCheckMillis(this.now);
			try
			{
				rollOver();
			}
			catch (IOException localIOException)
			{
				LogLog.error("rollOver() failed.", localIOException);
			}
		}
		super.subAppend(paramLoggingEvent);
		if ((this.fileName != null) && (((CountingQuietWriter)this.qw).getCount() >= this.maxFileSize)) {
			switchFileName();
		}
	}
}
