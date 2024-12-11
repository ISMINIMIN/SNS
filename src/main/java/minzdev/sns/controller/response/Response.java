package minzdev.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String code;
    private T result;

    public static Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static Response<Void> success() {
        return new Response<>("SUCCESS", null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public String toStream() {
        if(result == null) {
            return "{" +
                    "\"code\":" + "\"" + code + "\"," +
                    "\"result\":" + null +
                    "}";
        }

        return "{" +
                "\"code\":" + "\"" + code + "\"," +
                "\"result\":" + "\"" + result + "\"," +
                "}";
    }

}
