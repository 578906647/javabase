package com.example.demo.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/3 0003
 * @see com.example.demo.socket.nio
 */
public class NioClient2 {
    int port;
    ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    ByteBuffer wBuffer = ByteBuffer.allocate(1024);
    Selector selector;

    NioClient2(int port) {
        this.port = port;
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", port);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(inetSocketAddress);
        System.out.println("客户端2已经启动");
    }

    private void listen() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(this::handler);
            selectionKeys.clear();
        }
    }

    private void handler(SelectionKey selectionKey) {
        try {
            if (selectionKey.isConnectable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                if (socketChannel.isConnectionPending()) {
                    socketChannel.finishConnect();
                    new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Scanner scanner = new Scanner(System.in);
                                    wBuffer.clear();
                                    String info = scanner.nextLine();
                                    wBuffer.put(StandardCharsets.UTF_8.encode(info));
                                    wBuffer.flip();
                                    socketChannel.write(wBuffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }

            } else if (selectionKey.isReadable()) {
                SocketChannel client = (SocketChannel) selectionKey.channel();
                rBuffer.clear();
                int size = client.read(rBuffer);
                if (size > 0) {
                    rBuffer.flip();
                    String receiveInfo = StandardCharsets.UTF_8.decode(rBuffer).toString();
                    System.out.println(receiveInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        NioClient2 client = new NioClient2(8080);
        client.listen();
    }
}
