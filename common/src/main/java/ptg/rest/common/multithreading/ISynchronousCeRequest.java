package ptg.rest.common.multithreading;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface ISynchronousCeRequest {

    CompletableFuture<String> send(@NotNull RpcRequestAndResponse request) throws Exception;
}
