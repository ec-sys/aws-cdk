package com.myorg.todo;

import com.myorg.todo.tracing.TracingEvent;
import com.myorg.todo.utils.PrincipalUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class IndexController {

    private final ApplicationEventPublisher eventPublisher;

    public IndexController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    @RequestMapping("/")
    public String getIndex(Principal principal) {
        this.eventPublisher.publishEvent(
                new TracingEvent(
                        this,
                        "index",
                        principal != null ? PrincipalUtil.getEmailOfLoginUser(principal) : "anonymous"
                )
        );

        return "index";
    }
}
