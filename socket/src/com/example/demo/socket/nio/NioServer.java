package com.example.demo.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Description
 *
 * @author bai.wenlong
 * @version 1.0
 * @date 2020/1/3 0003
 * @see com.example.demo.socket.nio
 */
public class NioServer {
    int port;
    ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    ByteBuffer wBuffer = ByteBuffer.allocate(1024);
    Selector selector;
    Map<String, SocketChannel> clientMap = new HashMap<>();

    NioServer(int port) {
        this.port = port;
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        serverSocketChannel.bind(inetSocketAddress);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动，端口为:" + port);
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
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel client = serverSocketChannel.accept();
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_READ);
                clientMap.put(getClinetName(client), client);
            } else if (selectionKey.isReadable()) {
                SocketChannel client = (SocketChannel) selectionKey.channel();
                rBuffer.clear();
                int size = client.read(rBuffer);
                if (size > 0) {
                    rBuffer.flip();
                    String receiveInfo = StandardCharsets.UTF_8.decode(rBuffer).toString();
                    System.out.println(getClinetName(client) + ":" + receiveInfo);
                    //分发到其他客户端
                    dispatch(getClinetName(client), receiveInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(String clinetName, String receiveInfo) throws IOException {
        for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
            if (!clinetName.equals(entry.getKey())) {
                SocketChannel channel = entry.getValue();
                wBuffer.clear();
                wBuffer.put(StandardCharsets.UTF_8.encode(clinetName + ":" + receiveInfo));
                wBuffer.flip();
                channel.write(wBuffer);
            }
        }
    }

    private String getClinetName(SocketChannel client) throws IOException {
        String sb = client.socket().getLocalAddress() +
                ":" +
                client.socket().getPort();
        return sb;
    }

    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer(8080);
        server.listen();
    }
}
