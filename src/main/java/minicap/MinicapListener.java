package minicap;

/**
 * Created by jiaxin on 2019/5/27.
 */
public interface MinicapListener {
    // minicap启动完毕后
    public void onStartup(Minicap minicap, boolean success);
    // minicap关闭
    public void onClose(Minicap minicap);
}

