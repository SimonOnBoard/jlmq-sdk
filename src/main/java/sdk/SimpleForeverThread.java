package sdk;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

public class SimpleForeverThread extends Thread {
    @Override
    public void run() {
//        Object object = new Object();
//        synchronized (object){
//            try {
//                object.wait();
//            } catch (InterruptedException e) {
//                System.out.println(e.getCause());
//            }
//        }
        while (!currentThread().isInterrupted()) {
            try {
                sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                Thread.currentThread().stop();
            }
        }
    }
}
