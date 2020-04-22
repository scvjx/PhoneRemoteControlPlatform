package logs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
/**
 * Created by jiaxin on 2018/11/6.
 * LOG4J线程日志配置�?
 */
public class ThreadLogger {

    public static Logger getLogger(String logName) {
        Logger logger = null;

        logger = Logger.getLogger(logName);
        PatternLayout layout = new PatternLayout("[%d{MM-dd HH:mm:ss}] %-5p %-8t %m%n");

        // 日志文件按照每天分文件夹存放
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String logPath = "/SdkCollector/" + sdf.format(new Date()) + "/";

        // 文件输出
        ThreadFileAppender fileAppender = null;

        try {
            fileAppender = new ThreadFileAppender(layout, logPath, logName, "yyyy-MM-dd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileAppender.setAppend(false);
        fileAppender.setImmediateFlush(true);
        fileAppender.setThreshold(Level.DEBUG);

        // 绑定到logger
        logger.setLevel(Level.DEBUG);
        logger.addAppender(fileAppender);

        return logger;
    }

    /*
     * 继承了log4j类的内部�?
     */
    public static class ThreadFileAppender extends DailyRollingFileAppender {
        public ThreadFileAppender(Layout layout, String filePath, String fileName, String datePattern)
                throws IOException {
            super(layout, filePath + fileName + ".log", datePattern);
        }
    }
}