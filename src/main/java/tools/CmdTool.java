package tools;

import java.io.BufferedReader;
import java.io.IOException;

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
