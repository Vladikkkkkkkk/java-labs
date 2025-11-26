package lab9;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class lab9 {
    static class Account {
        final int id;
        private int balance;
        final ReentrantLock lock = new ReentrantLock();

        Account(int id, int initial) {
            this.id = id;
            this.balance = initial;
        }

        int getBalance() {
            return balance;
        }

        boolean withdrawIfPossible(int amount) {
            if (amount <= 0)
                return false;
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }

        void deposit(int amount) {
            if (amount <= 0)
                return;
            balance += amount;
        }
    }

    static class Bank {
        void transfer(Account a, Account b, int amount) {
            if (a == b || amount <= 0)
                return;
            Account first = a.id < b.id ? a : b;
            Account second = a.id < b.id ? b : a;

            first.lock.lock();
            try {
                second.lock.lock();
                try {
                    if (a.withdrawIfPossible(amount)) {
                        b.deposit(amount);
                    }
                } finally {
                    second.lock.unlock();
                }
            } finally {
                first.lock.unlock();
            }
        }
    }

    static class RingBuffer {
        private final String[] items;
        private int head = 0, tail = 0, count = 0;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();

        RingBuffer(int capacity) {
            if (capacity <= 0)
                throw new IllegalArgumentException("capacity>0");
            items = new String[capacity];
        }

        void put(String s) throws InterruptedException {
            lock.lock();
            try {
                while (count == items.length) {
                    notFull.await();
                }
                items[tail] = s;
                tail = (tail + 1) % items.length;
                count++;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        String take() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    notEmpty.await();
                }
                String s = items[head];
                items[head] = null;
                head = (head + 1) % items.length;
                count--;
                notFull.signal();
                return s;
            } finally {
                lock.unlock();
            }
        }
    }

    static void runBankTest() throws InterruptedException {
        final int ACCS = 100;
        final int MAX_INIT = 1000;
        final Random rnd = new Random();
        Account[] ac = new Account[ACCS];
        for (int i = 0; i < ACCS; i++)
            ac[i] = new Account(i, rnd.nextInt(MAX_INIT));
        long initialSum = 0;
        for (Account a : ac)
            initialSum += a.getBalance();
        System.out.println("Initial total: " + initialSum);

        Bank bank = new Bank();
        int TASKS = 20000;
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < TASKS; i++) {
            ex.submit(() -> {
                int from = rnd.nextInt(ACCS);
                int to = rnd.nextInt(ACCS);
                if (from == to)
                    return;
                int amount = rnd.nextInt(200);
                bank.transfer(ac[from], ac[to], amount);
            });
        }
        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.MINUTES);

        long finalSum = 0;
        for (Account a : ac)
            finalSum += a.getBalance();
        System.out.println("Final total: " + finalSum);
        System.out.println("Bank test " + (initialSum == finalSum ? "PASSED" : "FAILED"));
    }

    static void runProducerConsumer() throws InterruptedException {
        final RingBuffer buf1 = new RingBuffer(50);
        final RingBuffer buf2 = new RingBuffer(50);
        final int PRODUCERS = 5;
        final int TRANSFERS = 2;
        final Random rnd = new Random();

        for (int i = 0; i < PRODUCERS; i++) {
            final int idx = i;
            Thread t = new Thread(() -> {
                int msg = 0;
                try {
                    while (true) {
                        String s = "Потік No " + idx + " згенерував повідомлення " + (msg++);
                        buf1.put(s);
                        Thread.sleep(rnd.nextInt(10));
                    }
                } catch (InterruptedException ignored) {
                }
            }, "producer-" + i);
            t.setDaemon(true);
            t.start();
        }

        for (int i = 0; i < TRANSFERS; i++) {
            final int idx = i;
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String s = buf1.take();
                        String out = "Потік No " + idx + " переклав повідомлення " + s;
                        buf2.put(out);
                    }
                } catch (InterruptedException ignored) {
                }
            }, "transfer-" + i);
            t.setDaemon(true);
            t.start();
        }

        for (int i = 0; i < 100; i++) {
            String m = buf2.take();
            System.out.println("MAIN read: " + m);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task 1: Bank transfers test ===");
        runBankTest();

        System.out.println("\n=== Task 2: Producer-Consumer with ring buffers ===");
        runProducerConsumer();
    }
}