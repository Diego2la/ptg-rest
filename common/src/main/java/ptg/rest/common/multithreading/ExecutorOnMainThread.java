package ptg.rest.common.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public interface ExecutorOnMainThread extends AutoCloseable {

    <T> CompletableFuture<T> execute(Callable<T> callable);

    void init(OnMainThreadExecutorLogic l);
}
