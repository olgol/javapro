package Task3;

import java.util.LinkedList;
import java.util.List;

public class SimpleThreadPool {
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private final List<Thread> workers = new LinkedList<>();
    private volatile boolean isShutdown;

    public SimpleThreadPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Размер пула должен быть больше 0");
        }

        for (int i = 0; i < capacity; i++) {
            Thread worker = new Thread(this::runWorker, "simple-pool-worker-" + i);
            worker.start();
            workers.add(worker);
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("Задача не может быть null");
        }
        synchronized (taskQueue) {
            if (isShutdown) {
                throw new IllegalStateException("Пул остановлен и не принимает новые задачи");
            }
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public void shutdown() {
        synchronized (taskQueue) {
            isShutdown = true;
            taskQueue.notifyAll();
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }

    private void runWorker() {
        while (true) {
            Runnable task;
            synchronized (taskQueue) {
                while (taskQueue.isEmpty() && !isShutdown) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (taskQueue.isEmpty() && isShutdown) {
                    return;
                }
                task = taskQueue.removeFirst();
            }

            try {
                task.run();
            } catch (RuntimeException e) {
                System.err.println("Ошибка в задаче: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(3);

        for (int i = 1; i <= 8; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " выполняет задачу " + taskId);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("Все потоки завершили работу.");
    }
}
