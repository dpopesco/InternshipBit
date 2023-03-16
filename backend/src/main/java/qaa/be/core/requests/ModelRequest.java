package qaa.be.core.requests;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public abstract class ModelRequest<Request, T> {
    protected T restWrapper;
    private String parameters = "";

    public ModelRequest(T restWrapper) {
        this.restWrapper = restWrapper;
    }

    public Request usingParams(String... parameters) {
        log.info("Entering method where we can use parameters for requests!");
        String paramsStr = Arrays.stream(parameters).collect(Collectors.joining("&"));
        setParameters(paramsStr);
        return (Request) this;
    }

    protected String getParameters() {
        String localParam = parameters;
        clearParameters();
        return localParam;
    }

    protected void setParameters(String parameters) {
        this.parameters = parameters;
    }

    private void clearParameters() {
        log.info("Clearing parameters!");
        setParameters("");
    }
}
