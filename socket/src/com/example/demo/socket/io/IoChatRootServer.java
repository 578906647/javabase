package com.example.demo.socket.io;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/2 0002
 * @see com.example.demo.socket.io
 */
public class IoChatRootServer {
    /**
     * 连接的客户端列表
     */
    public static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) {
        try {

            //服务端
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("****服务器即将启动，等待客户端的连接****");
            //客户端连接进来的数量
            int count = 0;
            while (true) {
                // 连接的客户端
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                IoServerThread serverThread = new IoServerThread(socket);
                serverThread.start();
                count++;
                System.out.println("****" + socket.getLocalAddress() + ":" + socket.getPort() + " 进入聊天室****");
                System.out.println("****当前客户端数量为:" + count + "****");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
