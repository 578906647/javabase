package com.example.demo.socket.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/2 0002
 * @see com.example.demo.socket.io
 */
public class IoChatRoomClient2 {
    public static void main(String[] args) {
        try {
            //建立客户端
            Socket socket = new Socket();
            socket.bind(new InetSocketAddress(8082));
            socket.connect(new InetSocketAddress("127.0.0.1", 8080));
            new IoChatRoomClientReadThread(socket).start();
            new IoChatRoomClientWriteThread(socket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
