//package com.itjoin.pro_netty;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufAllocator;
//import io.netty.channel.*;
//import io.netty.channel.nio.AbstractNioChannel;
//import io.netty.channel.nio.NioEventLoop;
//import io.netty.channel.nio.NioTask;
//import io.netty.util.HashedWheelTimer;
//import io.netty.util.Timeout;
//import io.netty.util.TimerTask;
//import io.netty.util.internal.PlatformDependent;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.nio.channels.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.TimeUnit;
//
//import static io.netty.channel.internal.ChannelUtils.WRITE_STATUS_SNDBUF_FULL;
//
//public class ListTest {
//
//    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
//        /**
//         * 需等待执行的任务数+1
//         * 同时判断是否超过了最大限制
//         */
//        long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();
//        if (maxPendingTimeouts > 0 && pendingTimeoutsCount > maxPendingTimeouts) {
//            pendingTimeouts.decrementAndGet();
//            throw new RejectedExecutionException("Number of pending timeouts ("
//                    + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending "
//                    + "timeouts (" + maxPendingTimeouts + ")");
//        }
//        //假如时间轮Worker线程未启动，则需启动
//        start();
//
//        /**
//         * 根据定时任务延时执行时间，与时间轮启动时间
//         * 获取相对时间轮开始后的，任务执行延时时间
//         * 因为时间轮开始启动时间是不会改变的
//         * 通过这个时间可获取时钟需要跳动的刻度
//         */
//        long deadline = System.nanoTime() + unit.toNanos(delay) - startTime;
//
//        // Guard against overflow.
//        if (delay > 0 && deadline < 0) {
//            deadline = Long.MAX_VALUE;
//        }
//        /**
//         * 构建定时检测任务，并添加到新增定时检测任务队列中
//         * 在Worker线程中会从队列中取出来，放入缓存数组wheel
//         */
//        HashedWheelTimer.HashedWheelTimeout timeout = new HashedWheelTimer.HashedWheelTimeout(this, task, deadline);
//        timeouts.add(timeout);
//        return timeout;
//    }
//
//
//    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
//        final SelectionKey key = selectionKey();
//        //获取key兴趣集
//        final int interestOps = key.interestOps();
//        for (;;) {
//            Object msg = in.current();
//            if (msg == null) {
//                //数据已全部发送完，从兴趣集中移除移除OP_WRITE
//                if ((interestOps & SelectionKey.OP_WRITE) != 0) {
//                    key.interestOps(interestOps & ~SelectionKey.OP_WRITE);
//                }
//                break;
//            }
//            try {
//                boolean done = false;
//                //获取配置中最多写次数，默认16次
//                for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
//                    //调用子类方法，msg写成功了返回true
//                    if (doWriteMessage(msg, in)) {
//                        done = true;
//                        break;
//                    }
//                }
//
//                if (done) {
//                    //发送成功，从缓存链表中移除
//                    //继续发送下一个缓存节点数据
//                    in.remove();
//                } else {
//                    //假如没有写成功，doWriteMessage返回false
//                    if ((interestOps & SelectionKey.OP_WRITE) == 0) {
//                        //添加OP_WRITE写操作事件到兴趣事件集中
//                        key.interestOps(interestOps | SelectionKey.OP_WRITE);
//                    }
//                    break;
//                }
//            } catch (Exception e) {
//                //当出现异常判断是否是继续写
//                if (continueOnWriteError()) {
//                    in.remove(e);
//                } else {
//                    throw e;
//                }
//            }
//        }
//    }
//    @Test
//    public void test01(){
//        List<Integer> sizeTable = new ArrayList<Integer>();
//        for (int i = 16; i < 512; i += 16) {
//            sizeTable.add(i);
//        }
//
//        for (int i = 512; i > 0; i <<= 1) {
//            sizeTable.add(i);
//        }
//
//        System.out.println(sizeTable.get(sizeTable.size()-1));
//
//        System.out.println(validateAndCalculateChunkSize(8*1024,11));
//
//        System.out.println(0x3FFFFFFF);
//
//    }
//    private static int validateAndCalculateChunkSize(int pageSize, int maxOrder) {
//        if (maxOrder > 14) {
//            throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
//        }
//
//        // Ensure the resulting chunkSize does not overflow.
//        int chunkSize = pageSize;
//        int t=1;
//        for (int i = maxOrder; i > 0; i --) {
//            chunkSize <<= 1;
//            t=t*2;
//        }
//        System.out.println(t);
//        return chunkSize;
//    }
//
//
//
//    @Test
//    public void test02(){
////        int d =11;
////        int initial = - (1 << d);
////        System.out.println(initial&2048);
////        for(int i=1;i<2050;i++){
////            if((i & initial)!=0){
////                System.out.println(i);
////            }
////
////        }
////        normalizeCapacity(513);
////        int tableIdx = 0;
////        int elemSize = 1023;
////        elemSize >>>= 10;
////        while (elemSize != 0) {
////            elemSize >>>= 1;
////            tableIdx ++;
////        }
////
////        System.out.println(tableIdx);
////        long[] bitmap = new long[]{0,0};
////
////        bitmap[0] |= (1L << 1);
////        System.out.println(bitmap[0]);
//
//    }
//
//    int normalizeCapacity(int reqCapacity) {
//        if (reqCapacity < 0) {
//            throw new IllegalArgumentException("capacity: " + reqCapacity + " (expected: 0+)");
//        }
//
//        if (true) { // >= 512
//            // Doubled
//
//            int normalizedCapacity = reqCapacity;
//            normalizedCapacity --;
////            System.out.println(normalizedCapacity >>>  1);
////            System.out.println(normalizedCapacity >>>  2);
////            System.out.println(normalizedCapacity >>>  4);
////            System.out.println(normalizedCapacity >>>  8);
////            System.out.println(normalizedCapacity >>>  16);
////            System.out.println(normalizedCapacity |512);
//            normalizedCapacity |= normalizedCapacity >>>  1;
//            System.out.println(normalizedCapacity);
//            normalizedCapacity |= normalizedCapacity >>>  2;
//            System.out.println(normalizedCapacity);
//            normalizedCapacity |= normalizedCapacity >>>  4;
//            System.out.println(normalizedCapacity);
//            normalizedCapacity |= normalizedCapacity >>>  8;
//            System.out.println(normalizedCapacity);
//            normalizedCapacity |= normalizedCapacity >>> 16;
//            System.out.println(normalizedCapacity);
//            normalizedCapacity ++;
//
//            if (normalizedCapacity < 0) {
//                normalizedCapacity >>>= 1;
//            }
//
//            return normalizedCapacity;
//        }
//
//        // Quantum-spaced
//        if ((reqCapacity & 15) == 0) {
//            return reqCapacity;
//        }
//
//        return (reqCapacity & ~15) + 16;
//    }
//}
