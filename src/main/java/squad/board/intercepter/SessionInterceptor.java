package squad.board.intercepter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import squad.board.service.MemberService;

@RequiredArgsConstructor
@Slf4j
@Component
@NonNullApi
public class SessionInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        memberService.validateSession(request);
        return true;
    }
}
