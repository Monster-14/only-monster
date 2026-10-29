package com.monster.portfolio.module.health;

import com.monster.portfolio.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class HealthController {

    @GetMapping("/health")
    public R<Map<String, Object>> health() {
        return R.ok(Map.of(
                "app", "monster-portfolio",
                "status", "UP",
                "time", Instant.now().toString()
        ));
    }
}
