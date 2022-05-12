package ptg.rest.common.multithreading;

import org.jetbrains.annotations.NotNull;

public interface OnMainThreadExecutorLogic {

    void post() throws Exception;

    void setProcessLambda(@NotNull Runnable runnable);

    void cleanUp() throws Exception;
}
