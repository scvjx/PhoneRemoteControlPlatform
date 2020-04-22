package tools;

import common.Constant;
import logs.ThreadLogger;
import org.apache.log4j.Logger;

/**
 * Created by jiaxin on 2018/10/23.
 * 封装日志打印工具类
 */

public class LogTools {
    private static Logger logger;
    private boolean debugFlag = Constant.DEBUGFLAG;
    private boolean consoleFlag = Constant.CONSOLEFLAG;
    private boolean logFileFlag = Constant.LOGFILEFLAG;

    public void printLog(String logMessage,String logLevel){
        if(debugFlag){
            switch (logLevel){
                case "info" :
                    if(logFileFlag){
                        logger.info(logMessage);
                    }
                    break;
                case "debug" :
                    if(logFileFlag) {
                        logger.debug(logMessage);
                    }
                    break;
                case "error" :
                    if(logFileFlag) {
                        logger.error(logMessage);
                    }
                    break;
                case "trace" :
                    if(logFileFlag) {
                        logger.trace(logMessage);
                    }
                    break;
                default:
                    if(logFileFlag) {
                        logger.info(logMessage);
                    }
                    break;
            }
            if(consoleFlag) {
                System.out.println(logMessage);
            }
        }
    }

    public void printMainLog(String logMessage,String logLevel,Class object){
        logger  = Logger.getLogger(object);
        printLog(logMessage,logLevel);
    }

    public void printThreadLog(String logMessage,String logLevel,String threadName){
        logger = ThreadLogger.getLogger(threadName);
        printLog(logMessage,logLevel);
    }
}
