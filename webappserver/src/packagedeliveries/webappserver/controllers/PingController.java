package packagedeliveries.webappserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class PingController {
    @GetMapping("/ping")
    public String getHelloMessage() {
        return "System is up!";
    }
}
