package minicap;

import common.Constant;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.LogTools;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jiaxin on 2019/5/28.
 * minicap截图数据接收处理实现
 */
public class MiniCapDataHandler {

    private LogTools logTools  = new LogTools();
    private int minicapPort ;
    private String minicapHost;
    private boolean isRunning = true;
    private Queue<byte[]> dataQueue = new ConcurrentLinkedQueue<byte[]>();
    private Banner banner = new Banner();
    private String  picPath = new Constant().PICPATH;
    private byte[] finalBytes;

    public MiniCapDataHandler(String minicapHost,int minicapPort){
        this.minicapHost = minicapHost;
        this.minicapPort = minicapPort;
    }

    public void RunImageConverterThread(WebSocketSession session){
        new Thread(new ImageConverter(session)).start();
    }

    public void RunImageBinaryFrameCollectorThread(){
        new Thread(new ImageBinaryFrameCollector()).start();
    }

    class ImageBinaryFrameCollector implements Runnable {
        private Socket socket;
        @Override
        public void run() {
            logTools.printLog("图片二进制数据收集器已经开启","info");
            // TODO Auto-generated method stub
            InputStream stream = null;
            DataInputStream input = null;
            try {
                socket = new Socket(minicapHost, minicapPort);
                stream = socket.getInputStream();
                input = new DataInputStream(stream);
                while (isRunning) {
                    byte[] buffer;
                    int len = 0;
                    while (len == 0) {
                        len = input.available();
                    }
                    buffer = new byte[len];
                    input.read(buffer);
                    dataQueue.add(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null && socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            logTools.printLog("图片二进制数据收集器已关闭","info");
        }
    }

    class ImageConverter implements Runnable {
        private int readBannerBytes = 0;
        private int bannerLength = 2;
        private int readFrameBytes = 0;
        private int frameBodyLength = 0;
        private byte[] frameBody = new byte[0];
        private WebSocketSession session;

        public ImageConverter(WebSocketSession session){
            this.session = session;
        }


        @Override
        public void run() {
            logTools.printLog("图片生成器已经开启","info");
            long start = System.currentTimeMillis();
            byte [] tempFinalBytes = null;
            while (isRunning) {
                byte[] binaryData = dataQueue.poll();
                if (binaryData == null)
                    continue;
                int len = binaryData.length;
                for (int cursor = 0; cursor < len;) {
                    int byte10 = binaryData[cursor] & 0xff;
                    if (readBannerBytes < bannerLength) {
                        cursor = parserBanner(cursor, byte10);
                    } else if (readFrameBytes < 4) {
                        // 第二次的缓冲区中前4位数字和为frame的缓冲区大小
                        frameBodyLength += (byte10 << (readFrameBytes * 8)) >>> 0;
                        cursor += 1;
                        readFrameBytes += 1;
                    } else {
                        if (len - cursor >= frameBodyLength) {
                            byte[] subByte = subByteArray(binaryData, cursor,
                                    cursor + frameBodyLength);
                            frameBody = byteMerger(frameBody, subByte);
                            if ((frameBody[0] != -1) || frameBody[1] != -40) {
                                logTools.printLog(String
                                        .format("Frame body does not start with JPG header"),"info");
                                return;
                            }
                            finalBytes = subByteArray(frameBody, 0, frameBody.length);
                                    // 转化成bufferImage
                            sendPicture(session,finalBytes);//二进制图片发送前台
                            long current = System.currentTimeMillis();
                            start = current;
                            cursor += frameBodyLength;
                            frameBodyLength = 0;
                            readFrameBytes = 0;
                            frameBody = new byte[0];
                        } else {
                            byte[] subByte = subByteArray(binaryData, cursor,
                                    len);
                            frameBody = byteMerger(frameBody, subByte);
                            frameBodyLength -= (len - cursor);
                            readFrameBytes += (len - cursor);
                            cursor = len;
                        }
                    }
                }
            }
            logTools.printLog("图片生成器已关闭","info");
        }

        private int parserBanner(int cursor, int byte10) {
            switch (readBannerBytes) {
                case 0:
                    // version
                    banner.setVersion(byte10);
                    break;
                case 1:
                    // length
                    bannerLength = byte10;
                    banner.setLength(byte10);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    // pid
                    int pid = banner.getPid();
                    pid += (byte10 << ((readBannerBytes - 2) * 8)) >>> 0;
                    banner.setPid(pid);
                    break;
                case 6:
                case 7:
                case 8:
                case 9:
                    // real width
                    int realWidth = banner.getReadWidth();
                    realWidth += (byte10 << ((readBannerBytes - 6) * 8)) >>> 0;
                    banner.setReadWidth(realWidth);
                    break;
                case 10:
                case 11:
                case 12:
                case 13:
                    // real height
                    int realHeight = banner.getReadHeight();
                    realHeight += (byte10 << ((readBannerBytes - 10) * 8)) >>> 0;
                    banner.setReadHeight(realHeight);
                    break;
                case 14:
                case 15:
                case 16:
                case 17:
                    // virtual width
                    int virtualWidth = banner.getVirtualWidth();
                    virtualWidth += (byte10 << ((readBannerBytes - 14) * 8)) >>> 0;
                    banner.setVirtualWidth(virtualWidth);

                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                    // virtual height
                    int virtualHeight = banner.getVirtualHeight();
                    virtualHeight += (byte10 << ((readBannerBytes - 18) * 8)) >>> 0;
                    banner.setVirtualHeight(virtualHeight);
                    break;
                case 22:
                    // orientation
                    banner.setOrientation(byte10 * 90);
                    break;
                case 23:
                    // quirks
                    banner.setQuirks(byte10);
                    break;
            }

            cursor += 1;
            readBannerBytes += 1;

            if (readBannerBytes == bannerLength) {
                logTools.printLog(banner.toString(),"info");
            }
            return cursor;
        }
    }


    // java合并两个byte数组
    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    private static byte[] subByteArray(byte[] byte1, int start, int end) {
        byte[] byte2 = new byte[end - start];
        System.arraycopy(byte1, start, byte2, 0, end - start);
        return byte2;
    }

    private synchronized void createImageFromByte(byte[] binaryData) {
        InputStream in = new ByteArrayInputStream(binaryData);
        try {
            BufferedImage bufferedImage = ImageIO.read(in);
            ImageIO.write(bufferedImage, "jpg", new File(picPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void sendPicture(WebSocketSession session,byte bytes[]) {
        BinaryMessage byteMessage = new BinaryMessage(bytes);
        try {
            session.sendMessage(byteMessage);
        } catch (IOException e) {
        }
    }
}
