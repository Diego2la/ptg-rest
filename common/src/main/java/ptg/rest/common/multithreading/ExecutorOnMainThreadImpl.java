package ptg.rest.common.multithreading;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
public class ExecutorOnMainThreadImpl implements ExecutorOnMainThread {

    private final static int AUXILIARY_SCHEDULER_PERIOD = 1; // minutes

    private final Queue<Job<?>> jobs = new ConcurrentLinkedQueue<>();

    protected volatile OnMainThreadExecutorLogic logic = null;

    private final ScheduledExecutorService auxiliaryScheduler = Executors.newScheduledThreadPool(1);

    public ExecutorOnMainThreadImpl() {
        auxiliaryScheduler.scheduleAtFixedRate(() -> {
            if (logic != null) {
                try {
                    logic.post();
                } catch (Exception e) {
                    log.warn("Failed to post to mem link during auxiliary scheduler run", e);
                }
            }
        }, AUXILIARY_SCHEDULER_PERIOD, AUXILIARY_SCHEDULER_PERIOD, TimeUnit.MINUTES);
    }

    @Override
    public <T> CompletableFuture<T> execute(Callable<T> callable) {
        log.debug("Submitting task for execution on main thread");
        if (logic == null) {
            throw new RuntimeException("Logic is not initialized");
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        Job<T> job = new Job<>(future, callable);
        jobs.add(job);
        try {
            logic.post();
        } catch (Exception e) {
            job.getFuture().cancel(true);
            throw new RuntimeException("Exception when posting to mem link", e);
        }
        return future;
    }

    protected void process() {
        log.debug("Processing job notification");
        Job job = jobs.poll();
        if (job == null) {
            log.warn("Jobs queue is empty");
            return;
        }
        if (job.getFuture().isDone()) {
            log.info("Job was canceled, skip");
            return;
        }
        try {
            log.debug("Processing job");
            job.getFuture().complete(job.getCallable().call());
        } catch (Exception e) {
            log.error("Failed to execute on Main Thread task", e);
            job.getFuture().completeExceptionally(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (logic != null) {
            logic.cleanUp();
        }
        auxiliaryScheduler.shutdown();
    }

    @Override
    public void init(OnMainThreadExecutorLogic l) {
        l.setProcessLambda(this::process);
        logic = l;
    }

    @Value
    static class Job<T> {
        CompletableFuture<T> future;
        Callable<T> callable;
    }
}
