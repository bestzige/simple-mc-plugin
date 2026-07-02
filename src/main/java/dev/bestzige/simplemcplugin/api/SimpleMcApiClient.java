package dev.bestzige.simplemcplugin.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dev.bestzige.simplemcplugin.api.dto.ApiResponse;
import dev.bestzige.simplemcplugin.config.ApiSettings;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public final class SimpleMcApiClient implements Closeable {

    private static final Gson GSON = new Gson();

    private final OkHttpClient httpClient;
    private final SimpleMcApi api;
    private final String authorization;

    private SimpleMcApiClient(OkHttpClient httpClient, SimpleMcApi api, String token) {
        this.httpClient = httpClient;
        this.api = api;
        this.authorization = "Bearer " + token;
    }

    public static SimpleMcApiClient create(ApiSettings settings, Logger logger) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(settings.connectTimeoutSeconds(), TimeUnit.SECONDS)
                .readTimeout(settings.readTimeoutSeconds(), TimeUnit.SECONDS)
                .writeTimeout(settings.writeTimeoutSeconds(), TimeUnit.SECONDS);

        if (settings.debugLogging()) {
            httpClientBuilder.addInterceptor(loggingInterceptor(logger));
        }

        OkHttpClient httpClient = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(settings.baseUrl())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return new SimpleMcApiClient(httpClient, retrofit.create(SimpleMcApi.class), settings.token());
    }

    private static Interceptor loggingInterceptor(Logger logger) {
        return chain -> {
            Request request = chain.request();
            long started = System.nanoTime();
            okhttp3.Response response = chain.proceed(request);
            long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started);
            ResponseBody body = response.body();
            logger.info(request.method() + " " + request.url() + " -> " + response.code()
                    + " (" + elapsedMillis + "ms"
                    + (body != null ? ", " + body.contentLength() + " bytes" : "")
                    + ")");
            return response;
        };
    }

    public SimpleMcApi api() {
        return api;
    }

    public String authorization() {
        return authorization;
    }

    public <T> T execute(Call<ApiResponse<T>> call) throws ApiClientException {
        try {
            Response<ApiResponse<T>> response = call.execute();
            ApiResponse<T> envelope = response.body();

            if (!response.isSuccessful()) {
                throw new ApiClientException(errorMessage(response));
            }

            if (envelope == null) {
                throw new ApiClientException("API returned an empty response");
            }

            if (!envelope.isSuccess()) {
                throw new ApiClientException(Objects.requireNonNullElse(envelope.getMessage(), envelope.getCode()));
            }

            return envelope.getData();
        } catch (IOException exception) {
            throw new ApiClientException("Cannot reach Simple MC API", exception);
        }
    }

    private static String errorMessage(Response<?> response) {
        ResponseBody errorBody = response.errorBody();
        if (errorBody == null) {
            return "HTTP " + response.code();
        }

        try {
            String body = errorBody.string();
            if (body.isBlank()) {
                return "HTTP " + response.code();
            }

            ApiResponse<?> envelope = GSON.fromJson(body, ApiResponse.class);
            if (envelope == null) {
                return "HTTP " + response.code();
            }

            return Objects.requireNonNullElse(
                    envelope.getMessage(),
                    Objects.requireNonNullElse(envelope.getCode(), "HTTP " + response.code())
            );
        } catch (IOException | JsonSyntaxException exception) {
            return "HTTP " + response.code();
        }
    }

    @Override
    public void close() {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
        try {
            if (httpClient.cache() != null) {
                httpClient.cache().close();
            }
        } catch (Exception ignored) {
        }
        try {
            httpClient.dispatcher().executorService().awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
