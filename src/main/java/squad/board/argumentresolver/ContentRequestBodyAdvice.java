package squad.board.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import squad.board.dto.board.CreateBoardRequest;

import java.io.IOException;
import java.lang.reflect.Type;

@RestControllerAdvice
public class ContentRequestBodyAdvice implements RequestBodyAdvice {
    // S3KeyConvert 어노테이션이 붙고, CreateBoardRequest 객체에대해 동작한다.
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasParameterAnnotation(S3KeyConvert.class) && targetType.equals(CreateBoardRequest.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        CreateBoardRequest request = (CreateBoardRequest) body;
        return request.changeS3ImageKey("tmp", "original");
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
