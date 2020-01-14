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
public class IoChatRoomClientWriteThread extends Thread {
    private Socket socket;

    IoChatRoomClientWriteThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            while (true) {
                printWriter.write(bufferedReader.readLine() + "\n");
                printWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
