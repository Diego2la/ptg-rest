package ptg.rest.common.multithreading;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcProcessor {

    private final ISynchronousCeRequest synchronousCeRequest;
    private final ObjectMapper objectMapper;
    private final int rpcTimeout;

    public RpcProcessor(ISynchronousCeRequest synchronousCeRequest, ObjectMapper objectMapper, int rpcTimeout) {
        this.synchronousCeRequest = synchronousCeRequest;
        this.objectMapper = objectMapper;
        this.rpcTimeout = rpcTimeout;
    }

    public String call(String method, Object objectBody) throws Exception {
        return call(method, objectMapper.writeValueAsString(objectBody));
    }

    public String call(String method, String jsonBody) throws Exception {
        RpcRequestAndResponse request = new RpcRequestAndResponse();
        request.setMethod(method);
        request.setJsonBody(jsonBody);
        return synchronousCeRequest.send(request).get(rpcTimeout, TimeUnit.SECONDS);
    }
}
