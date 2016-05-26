package com.bob.javatour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Spawn about 10 tasks, each returning a string,
 * and ultimately collect the results into a list.
 */
public class J8CompletableFutureWithRObservable {

    private Logger logger = LoggerFactory.getLogger(J8CompletableFutureWithRObservable.class);

    /**
     * 单线程执行
     *
     * @throws Exception
     */
    public void testSequentialScatterGather() throws Exception {
        List<String> list = IntStream.range(0, 10).boxed().map(this::generateTask).collect(Collectors.toList());
        System.out.println(list.toString());
    }

    private final String generateTask(int i) {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i + " - in sq test - " + Thread.currentThread().getName();
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(8);

    /**
     * 有结果值Future实现
     *
     * @throws Exception
     */
    public void testCompletableFutureScatterGather() throws Exception {
        List<CompletableFuture<String>> futures = IntStream.range(0, 10).boxed()
                .map(i -> this.generateTask(i, executorService).exceptionally(t -> t.getMessage())).collect(Collectors.toList());
        CompletableFuture<List<String>> result = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
        result.thenAccept(l -> System.out.println(l.toString()));
    }

    private CompletableFuture<String> generateTask(int i, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i == 5) {
                throw new RuntimeException("Run, it is a 5!");
            } else {
                return i + " - in cf test - " + Thread.currentThread().getName();
            }
        }, executorService);
    }

    /**
     * 开源框架rx.java实现
     *
     * @throws Exception
     */
    public void testRxScatterGather() throws Exception {
        List<Observable<String>> obs = IntStream.range(0, 10).boxed()
                .map(i -> this.generateTaskByRx(i, executorService)).collect(Collectors.toList());
        Observable<List<String>> merged = Observable.merge(obs).toList();
        merged.subscribe(l -> System.out.println(l.toString()));
    }

    private Observable<String> generateTaskByRx(int i, ExecutorService executorService) {
        return Observable.<String>create(s -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i == 5) {
                throw new RuntimeException("Run, it is a 5!");
            }
            s.onNext(i + " - in rx test - " + Thread.currentThread().getName());
            s.onCompleted();
        }).onErrorReturn(e -> e.getMessage()).subscribeOn(Schedulers.from(executorService));
    }

    public static void main(String[] args) throws Exception {

        J8CompletableFutureWithRObservable obj = new J8CompletableFutureWithRObservable();
        obj.testSequentialScatterGather();
        obj.testCompletableFutureScatterGather();
        obj.testRxScatterGather();
    }
}
