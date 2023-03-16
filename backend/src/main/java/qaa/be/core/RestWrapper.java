package qaa.be.core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import qaa.be.core.exceptions.ConversionJsonToModelException;
import qaa.be.util.Properties;

import javax.annotation.PostConstruct;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Slf4j
public class RestWrapper<T> {

    @Autowired
    private Properties properties;

    private final RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    private String parameters = "";
    @Setter
    protected String errorField = "error";

    private int statusCode;
    Response returnedResponse;

    @PostConstruct
    public void initializeRequestSpecBuilder() {
        String url = properties.getURI();
        RestAssured.baseURI = url;

        configureRequestSpec().setBaseUri(url);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    protected RequestSpecification onRequest() {
        return given().spec(configureRequestSpec().setContentType(ContentType.JSON).build());
    }

    public <T> T processModel(Class<T> modelClass, RestRequest restRequest) {
        return sendRequestAndCreateModel(modelClass, restRequest);
    }

    public <T> T sendRequestAndCreateModel(Class<T> modelClass, RestRequest restRequest) {
        returnedResponse = sendRequest(restRequest);
        logResponse(returnedResponse);
        setStatusCode(returnedResponse.getStatusCode());
        T model = null;

        boolean hasError = checkForErrorInResponse(returnedResponse);
        if (!hasError) {

            try {
                model = returnedResponse.getBody().as(modelClass);
            } catch (Exception error) {
                error.printStackTrace();
                throw new ConversionJsonToModelException(modelClass, error);
            }
        }
        return model;
    }

    public <T> T processLastError(Class<T> modelClass) {
        return returnedResponse.getBody().as(modelClass);
    }

    private boolean checkForErrorInResponse(Response response) {
        Object error = response.jsonPath().get(errorField);
        return error != null;
    }

    public void logResponse(Response response) {
        response.getBody().prettyPrint();
        log.info("Status code: " + response.getStatusCode());
        log.info("Header: " + response.getHeader("content-type"));
        log.info("Response time: " + response.getTime());
    }

    public RequestSpecBuilder configureRequestSpec() {
        return this.requestSpecBuilder;
    }

    public RestWrapper addRequestHeader(String headerKey, String headerValue) {
        configureRequestSpec().addHeader(headerKey, headerValue);
        return this;
    }

    public Response sendRequest(RestRequest restRequest) {
        RequestSpecification requestSpecification = onRequest();
        switch (restRequest.getHttpMethod()) {
            case GET:
                returnedResponse = onRequest().get(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                break;
            case DELETE:
                returnedResponse = onRequest().delete(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                break;
            case POST:
                if (restRequest.getBody() != null) {
                    returnedResponse = requestSpecification.body(restRequest.getBody()).post(restRequest.getPath(),
                            restRequest.getPathParams()).andReturn();
                } else if (restRequest.getPostMultipartBody() != null) {
                    requestSpecification.contentType(ContentType.MULTIPART);
                    Map<String, Object> postBody = restRequest.getPostMultipartBody();
                    for (String field : postBody.keySet()) {
                        requestSpecification = requestSpecification.multiPart(field, postBody.get(field));
                    }
                    returnedResponse = requestSpecification.post(restRequest.getPath(), restRequest.getPathParams()).andReturn();
                } else {
                    returnedResponse = requestSpecification.post(restRequest.getPath()).andReturn();
                }
                break;
            case PUT:
                returnedResponse = onRequest().body(restRequest.getBody()).put(restRequest.getPath(),
                        restRequest.getPathParams()).andReturn();
                break;
            default:
                throw new RuntimeException("Please provide a valid request method");
        }
        setStatusCode(returnedResponse.getStatusCode());
        return returnedResponse;
    }
}