package com.example.demo.socket.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/2 0002
 * @see com.example.demo.socket.io
 */
public class IoChatRoomClientReadThread extends Thread {
    private Socket socket;

    IoChatRoomClientReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info;
            while (true) {
                if ((info = bufferedReader.readLine()) != null) {
                    System.out.println(info);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
