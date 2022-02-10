package com.github.bourbon.base.lock;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/20 23:23
 */
public class StampedLockTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static class Point {
        // x,y 临界区的共享变量，访问时注意线程安全
        private double x, y;
        private final StampedLock sl = new StampedLock();

        // 对x,y修改，需要写锁
        private void move(double deltaX, double deltaY) {
            // 申请写锁，返回一个邮戳，unlock需要用这个邮戳
            long stamp = sl.writeLock();
            try {
                // 拿到锁，修改数据
                x += deltaX;
                y += deltaY;
            } finally {
                // 使用邮戳释放写锁
                sl.unlockWrite(stamp);
            }
        }

        // 读取，使用乐观锁，如果失败，转为普通读锁
        private double distanceFromOrigin() {
            // 获得一个乐观锁
            long stamp = sl.tryOptimisticRead();
            // 获取x,y值
            double currentX = x, currentY = y;
            // 使用validate()函数，进行冲突检测
            if (!sl.validate(stamp)) {
                // 冲突了，悲观策略
                // 获取读锁
                stamp = sl.readLock();
                try {
                    // 获取x,y值
                    currentX = x;
                    currentY = y;
                } finally {
                    // 释放读锁
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        // 读锁升级写锁
        private void moveIfAtOrigin(double newX, double newY) {
            // 直接使用悲观策略
            long stamp = sl.readLock();
            try {
                // 自旋，如果在原点，不停重试
                while (x == 0.0 && y == 0.0) {
                    // 尝试升级写锁，ws==0 表示失败，否则就是一个可用的邮戳
                    long ws = sl.tryConvertToWriteLock(stamp);
                    // 升级写锁成功
                    if (ws != 0L) {
                        // 修改邮戳
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        // 修改失败，显示释放读锁
                        sl.unlockRead(stamp);
                        // 显示直接获取写锁
                        stamp = sl.writeLock();
                    }
                }
            } finally {
                // 释放读锁或写锁
                sl.unlock(stamp);
            }
        }
    }

    @Test
    public void parallelReadSerialWrite() throws Exception {
        StampedLock lock = new StampedLock();
        Lock readLock = lock.asReadLock();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(6);
        Runnable a = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                readLock.lock();
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info("do something end for read,time->" + System.currentTimeMillis());
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        };
        for (int i = 0; i < 5; i++) {
            new Thread(a).start();
        }
        long start = System.currentTimeMillis();
        cyclicBarrier.await();
        logger.info("read end,time->" + (System.currentTimeMillis() - start));

        Lock writeLock = lock.asWriteLock();
        Runnable b = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                writeLock.lock();
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info("do something end for write,time->" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 5; i++) {
            new Thread(b).start();
        }
        start = System.currentTimeMillis();
        cyclicBarrier.await();
        logger.info("write end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void deadLock() throws Exception {
        StampedLock lock = new StampedLock();
        Lock readLock = lock.asReadLock();
        Lock writeLock = lock.asWriteLock();
        CountDownLatch writeCount = new CountDownLatch(5);
        Runnable a = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                readLock.lock();
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info(Thread.currentThread().getName() + " do something end for read,time->" + System.currentTimeMillis());
                try {
                    // 写锁必须等待所有的读锁释放才能获取锁，此处是读锁获取完成，等待获取写锁
                    // 就形成了死锁
                    writeLock.lock();
                    TimeUnit.MILLISECONDS.sleep(1000);
                    logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.currentTimeMillis());
                } finally {
                    writeLock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
                writeCount.countDown();
            }
        };
        for (int i = 0; i < 5; i++) {
            new Thread(a).start();
        }
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("read end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void notReentrantReadWriteLock() throws Exception {
        StampedLock lock = new StampedLock();
        Lock readLock = lock.asReadLock();
        Lock writeLock = lock.asWriteLock();
        CountDownLatch writeCount = new CountDownLatch(5);
        Runnable a = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                writeLock.lock();
                TimeUnit.MILLISECONDS.sleep(1000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.currentTimeMillis());
                try {
                    // StampedLock下同一线程已经获取了写锁，不能再获取读锁，必须等待写锁释放
                    readLock.lock();
                    TimeUnit.MILLISECONDS.sleep(1000);
                    logger.info(Thread.currentThread().getName() + " do something end for read,time->" + System.currentTimeMillis());
                } finally {
                    readLock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                writeCount.countDown();
            }
        };
        for (int i = 0; i < 5; i++) {
            new Thread(a).start();
        }
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("write end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void writeReadMutex() throws Exception {
        StampedLock lock = new StampedLock();
        Lock readLock = lock.asReadLock();
        Lock writeLock = lock.asWriteLock();
        CountDownLatch writeCount = new CountDownLatch(2);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                writeLock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                writeCount.countDown();
            }
        }).start();
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                // 必须得等待占有写锁的线程释放写锁，才能获取读锁
                readLock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for read,time->" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
                writeCount.countDown();
            }
        }).start();
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("main end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void readWriteMutex() throws Exception {
        StampedLock lock = new StampedLock();
        Lock readLock = lock.asReadLock();
        Lock writeLock = lock.asWriteLock();
        CountDownLatch writeCount = new CountDownLatch(2);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                readLock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for read,time->" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
                writeCount.countDown();
            }

        }).start();
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                //其他线程占有读锁，必须等待读锁释放才能获取写锁
                writeLock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                writeCount.countDown();
            }
        }).start();
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("main end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void notReentrantWriteLock() throws Exception {
        StampedLock stampedLock = new StampedLock();
        Lock lock = stampedLock.asWriteLock();
        CountDownLatch writeCount = new CountDownLatch(1);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.currentTimeMillis());
                logger.info("isWriteLocked->" + stampedLock.isWriteLocked());
                try {
                    // 写锁无法重入，此处形成死锁
                    lock.lock();
                    logger.info(stampedLock.getReadLockCount());
                    TimeUnit.MILLISECONDS.sleep(2000);
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                writeCount.countDown();
            }
        }).start();
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("main end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void reentrantReadLock() throws Exception {
        StampedLock stampedLock = new StampedLock();
        Lock lock = stampedLock.asReadLock();
        CountDownLatch writeCount = new CountDownLatch(1);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.currentTimeMillis());
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(2000);
                logger.info(Thread.currentThread().getName() + " do something end for read,time->" + System.currentTimeMillis());
                try {
                    lock.lock();
                    // 是否获取读锁
                    logger.info("isReadLocked->" + stampedLock.isReadLocked());
                    // 获取读锁重入次数
                    logger.info("getReadLockCount->" + stampedLock.getReadLockCount());
                    TimeUnit.MILLISECONDS.sleep(2000);
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                writeCount.countDown();
            }
        }).start();
        long start = System.currentTimeMillis();
        writeCount.await();
        logger.info("main end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void nonFairStampedLock() throws Exception {
        // StampedLock只支持非公平锁
        ReadWriteLock readWriteLock = new StampedLock().asReadWriteLock();
        Lock lock = readWriteLock.writeLock();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(5000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable2 = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                cyclicBarrier.await();
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable2).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        countDownLatch.await();
        logger.info("main end");
    }

    @Test
    public void nonFairReentrantReadWriteLock() throws Exception {
        // 默认的非公平锁
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock lock = readWriteLock.writeLock();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(5000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable2 = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                cyclicBarrier.await();
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable2).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        countDownLatch.await();
        logger.info("main end");
    }

    @Test
    public void fairReentrantReadWriteLock() throws Exception {
        // 公平锁模式
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        Lock lock = readWriteLock.writeLock();
        CountDownLatch countDownLatch = new CountDownLatch(6);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
        new Thread(() -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(5000);
                logger.info(Thread.currentThread().getName() + " do something end for write,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        Runnable runnable2 = () -> {
            logger.info(Thread.currentThread().getName() + " start,time->" + System.nanoTime());
            try {
                cyclicBarrier.await();
                lock.lock();
                logger.info(Thread.currentThread().getName() + " get lock,time->" + System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                countDownLatch.countDown();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(runnable2).start();
        }
        TimeUnit.MILLISECONDS.sleep(100);
        countDownLatch.await();
        logger.info("main end");
    }

    @Test
    public void reentrantReadWriteLock() throws Exception {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock readLock = readWriteLock.readLock();
        Lock writeLock = readWriteLock.writeLock();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(12);
        int num = 10000000;
        Runnable read = () -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < num; i++) {
                try {
                    readLock.lock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    readLock.unlock();
                }
            }
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(read).start();
        }
        Runnable write = () -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < num; i++) {
                try {
                    writeLock.lock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    writeLock.unlock();
                }
            }
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(write).start();
        cyclicBarrier.await();
        long start = System.currentTimeMillis();
        logger.info("run start");
        cyclicBarrier.await();
        logger.info("run end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void stampedLockOptimisticRead() throws Exception {
        StampedLock stampedLock = new StampedLock();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(12);
        int num = 10000000;
        Runnable read = () -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < num; i++) {
                try {
                    long stamp = stampedLock.tryOptimisticRead();
                    // validate方法返回false表示占有了写锁，共享变量可能变了
                    if (!stampedLock.validate(stamp)) {
                        try {
                            // 获取正常的悲观读锁，会阻塞等待修改完成，写锁释放
                            stamp = stampedLock.readLock();
                        } finally {
                            //释放读锁
                            stampedLock.unlockRead(stamp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(read).start();
        }
        Runnable write = () -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < num; i++) {
                long stamp = 0;
                try {
                    stamp = stampedLock.writeLock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stampedLock.unlockWrite(stamp);
                }
            }
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(write).start();
        cyclicBarrier.await();
        long start = System.currentTimeMillis();
        logger.info("run start");
        cyclicBarrier.await();
        logger.info("run end,time->" + (System.currentTimeMillis() - start));
    }

    @Test
    public void readLockToWriteLock() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.readLock();
            logger.info("get readLock succ");
            // 此时会直接释放读锁并获取写锁
            long result = stampedLock.tryConvertToWriteLock(stamp);
            if (result == 0) {
                logger.info("tryConvertToWriteLock fail");
                stampedLock.unlockRead(stamp);
                stamp = stampedLock.writeLock();
            } else {
                stamp = result;
            }
            logger.info("get writeLock succ");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Test
    public void reentrantReadLockToWriteLock() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.readLock();
            long stamp2 = stampedLock.readLock();
            logger.info("get readLock succ");
            // 此时会直接释放读锁并获取写锁
            long stamp3 = stampedLock.tryConvertToWriteLock(stamp2);
            if (stamp3 == 0) {
                logger.info("tryConvertToWriteLock fail");
                stampedLock.unlockRead(stamp2);
                stampedLock.unlockRead(stamp);
                stamp = stampedLock.writeLock();
            }
            logger.info("get writeLock succ");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Test
    public void optimisticReadToWriteLock() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.tryOptimisticRead();
            logger.info("get OptimisticRead succ");
            // 此时会直接释放读锁并获取写锁
            long result = stampedLock.tryConvertToWriteLock(stamp);
            if (result == 0) {
                logger.info("tryConvertToWriteLock fail");
                stampedLock.unlockRead(stamp);
                stamp = stampedLock.writeLock();
            } else {
                stamp = result;
            }
            logger.info("get writeLock succ");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Test
    public void writeLockToReadLock() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.writeLock();
            logger.info("get writeLock succ");
            // 此时会直接释放写锁，获取读锁，然后唤醒下一个等待写锁的线程
            long result = stampedLock.tryConvertToReadLock(stamp);
            if (result == 0) {
                logger.info("tryConvertToReadLock fail");
                stampedLock.unlockWrite(stamp);
                stamp = stampedLock.readLock();
            } else {
                stamp = result;
            }
            logger.info("get readLock succ");
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    @Test
    public void optimisticReadToReadLock() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.tryOptimisticRead();
            logger.info("get writeLock succ");
            // 此时会直接释放写锁，获取读锁，然后唤醒下一个等待写锁的线程
            long result = stampedLock.tryConvertToReadLock(stamp);
            if (result == 0) {
                logger.info("tryConvertToReadLock fail");
                stamp = stampedLock.readLock();
            } else {
                stamp = result;
            }
            logger.info("get readLock succ");
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    @Test
    public void writeLockToOptimisticRead() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.writeLock();
            logger.info("get writeLock succ");
            // 此时会直接释放写锁，获取读锁，然后唤醒下一个等待写锁的线程
            long result = stampedLock.tryConvertToOptimisticRead(stamp);
            if (result == 0) {
                logger.info("tryConvertToOptimisticRead fail");
                stampedLock.unlockWrite(stamp);
                stamp = stampedLock.tryOptimisticRead();
            } else {
                stamp = result;
            }
            logger.info("get OptimisticRead succ");
        } finally {
            logger.info("" + stampedLock.validate(stamp));
        }
    }

    @Test
    public void readLockToOptimisticRead() {
        StampedLock stampedLock = new StampedLock();
        long stamp = 0;
        try {
            stamp = stampedLock.readLock();
            logger.info("get readLock succ");
            // 此时会直接释放写锁，获取读锁，然后唤醒下一个等待写锁的线程
            long result = stampedLock.tryConvertToOptimisticRead(stamp);
            if (result == 0) {
                logger.info("tryConvertToOptimisticRead fail");
                stampedLock.unlockRead(stamp);
                stamp = stampedLock.tryOptimisticRead();
            } else {
                stamp = result;
            }
            logger.info("get OptimisticRead succ");
        } finally {
            logger.info("" + stampedLock.validate(stamp));
        }
    }
}