package com.shulian.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * With LogEventEncoder implemented, we’re ready to bootstrap the server, which includes
 * setting various ChannelOptions and installing the needed ChannelHandlers in the
 * pipeline. This will be done by the main class, LogEventBroadcasterServer, shown next.
 *
 * @author zhangjuwa
 * @apiNote
 * @date 2020-09-29 17:15
 * @since jdk1.8
 */
@Slf4j
public class LogEventBroadcasterServer {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcasterServer(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public static void main(String[] args) {
        LogEventBroadcasterServer broadcaster = new LogEventBroadcasterServer(new InetSocketAddress("255.255.255.255", 9999),
                new File("/Users/zhangjuwa/Desktop/udptest.log"));
        try {
            broadcaster.run();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            broadcaster.stop();
        }
    }

    /**
     * 0号端口：
     * 建立新的TCP和UDP socket连接时，需要给它们指定端口号。 为了避免这种写死端口号的做法或者说为了从本地系统中找到可用端口。
     * 网络编程员们可以以端口号0来作为连接参数。这样的话操作系统就会从动态端口号范围内搜索接下来可以使用的端口号
     *
     * @throws InterruptedException
     * @throws IOException
     */
    private void run() throws InterruptedException, IOException {
        Channel channel = bootstrap.bind(0).sync().channel();
        log.info("LogEventBroadcasterServer 绑定本地端口{}成功", 0);
        long pointer = 0;
        for (; ; ) {
            long length = file.length();
            if (length < pointer) {
                //                把读取指针放在文件末尾
                pointer = length;
            } else if (length > pointer) {

                // Content was added    文件里面进入了新的日志内容
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
                    //把文件指针移动到读取位置
                    randomAccessFile.seek(pointer);
                    String line;
//                    使用 RandomAccessFile对象方法的 readLine() 都会将编码格式转换成 ISO-8859-1 所以 输出显示是还要在进行一次转码
                    while ((line = randomAccessFile.readLine()) != null) {
                        byte[] data = line.getBytes(CharsetUtil.ISO_8859_1);
                        line = new String(data, CharsetUtil.UTF_8);
                        log.info("发送消息={}，字节数据长度={}", line, data.length);
                        channel.writeAndFlush(new LogEvent(null, 1, file.getAbsolutePath(), line));
                    }
                    pointer = randomAccessFile.getFilePointer();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("线程中断异常", e);
                    break;
                }
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }


}
