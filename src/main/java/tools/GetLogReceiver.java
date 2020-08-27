package tools;

import com.android.ddmlib.MultiLineReceiver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component("GetLogReceiver")
public class GetLogReceiver extends MultiLineReceiver {

    // 环形缓冲区的容量大小
    private static final int MAX_LOG_COUNT = 3000;
    private List<String> logs;
    private int writePos = 0;
    private int readPos = 0 ;
    public GetLogReceiver() {
        // 环形日志容器，留一条日志为空作为结束位置标识
        logs = new ArrayList<String>(MAX_LOG_COUNT);

        for(int i=0; i<MAX_LOG_COUNT; i++) {
            logs.add(null);
        }
    }

    @Override
    public void processNewLines(String[] lines) {
        synchronized(logs) {
            for(String line : lines) {
                if(!line.trim().equals("")) {
                    logs.set(writePos, line);
                    writePos++;
                    writePos = writePos % MAX_LOG_COUNT;
                }
            }

            logs.set(writePos, null);
        }
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public List<String> getNextBatchLogs(int start, int count) {
        List<String> result = new ArrayList<String>();

        synchronized(logs) {
            int firstReadPos = start % MAX_LOG_COUNT;

            for(int i=0; i<count; i++) {
                readPos = (firstReadPos + i)%MAX_LOG_COUNT;
                if (logs.get(readPos) != null) {
                    result.add(logs.get(readPos));
                } else {
                    break;
                }
            }
        }

        return result;
    }
}
