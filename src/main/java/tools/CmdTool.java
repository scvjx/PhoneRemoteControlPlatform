package tools;

import org.springframework.stereotype.Component;

import java.io.IOException;
@Component("CmdTool")
public class CmdTool {

    private LogTools logTools = new LogTools();
    public void execCommand(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logTools.printLog(e.getMessage(),"debug");
        }
    }
}
