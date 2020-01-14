package com.example.demo.socket.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/2 0002
 * @see com.example.demo.socket.io
 */
public class IoServerThread extends Thread {
    private Socket socket;

    IoServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String info;
                if ((info = bufferedReader.readLine()) != null) {
                    // 向所有的客户端推送消息
                    for (Socket item : IoChatRootServer.socketList) {
                        if (item.equals(socket)) {
                            continue;
                        }
                        PrintWriter printWriter = new PrintWriter(item.getOutputStream());
                        printWriter.write(socket.getLocalAddress() + ":" + socket.getPort() + "说:" + info + "\n");
                        printWriter.flush();
                    }
                    System.out.println(socket.getLocalAddress() + ":" + socket.getPort() + "说:" + info);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
