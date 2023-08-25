package squad.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    // 홈 화면
    @GetMapping("/")
    public String home() {
        log.info("home 화면 호출");
        return "home";
    }
}
