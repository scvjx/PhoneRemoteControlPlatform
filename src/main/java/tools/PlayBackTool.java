package tools;

import adb.AdbDevice;

public class PlayBackTool {
    private AdbDevice adbDevice;

    public PlayBackTool(AdbDevice adbDevice){
        this.adbDevice = adbDevice;
    }

    public void playBackScript(String script){
        String[] scriptList = script.split("\n");
        for(String scriptItem : scriptList){
            String[] scriptItemKey = scriptItem.split(" ");
            switch (scriptItemKey[0]){
                case ("t"):
                    adbDevice.adbTap(scriptItemKey[1],scriptItemKey[2]);
                    break;
                case("s"):
                    adbDevice.adbswipe(scriptItemKey[1],scriptItemKey[2],scriptItemKey[3],scriptItemKey[4],1);
                    break;
                case("w"):
                    int sleeptime = Integer.parseInt(scriptItemKey[1])*1000;
                    try {
                        Thread.sleep(sleeptime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case("i"):
                    adbDevice.adbInput(scriptItemKey[1]);
                    break;
            }
        }
    }
}
