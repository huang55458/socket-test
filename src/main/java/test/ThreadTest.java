package test;


import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chumeng
 */
public class ThreadTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                10,
                20,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                new ThreadPoolExecutor.AbortPolicy()
        );
        executor.execute(() -> System.out.println(" = "));
        executor.shutdown();
//        Collections.synchronizedMap()
    }
}
