package java0.conc0303;

import java0.conc0303.tool.CountDownLatchDemo;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03 {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException{
        long start=System.currentTimeMillis();
        // 异步执行 下面方法
//        Integer result = method01();

//        Integer result = method02();

//        Integer result = mothod03();
        // 确保  拿到result 并输出
        Integer result = method04();

        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        // 然后退出main线程
    }

    private static Integer method04() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> call = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        Integer result = call.get();
        executor.shutdown();
        return result;
    }

    private static Integer mothod03() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        executor.submit(task);
        return task.get();
    }

    private static Integer method02() throws InterruptedException, ExecutionException{
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        new Thread(task).start();
        return task.get();
    }


    private static Integer method01(){
        return (Integer) CompletableFuture.supplyAsync(() -> {
                return sum();
            }).join();
    }



    private static int sum() {
        return fibo(35);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
