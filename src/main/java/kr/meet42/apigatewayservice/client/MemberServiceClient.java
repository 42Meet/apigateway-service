package kr.meet42.apigatewayservice.client;

import kr.meet42.apigatewayservice.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="member-service", url="42meet.kro.kr/member", configuration = FeignConfig.class)
//@FeignClient(name="member-service", url="localhost:8080", configuration = FeignConfig.class)
@Component
public interface MemberServiceClient {
    @PostMapping("/refresh")
    TokenDto refreshToken(@RequestParam String accessToken, @RequestParam String refreshToken);

    @GetMapping("/{username}/role")
    String getRole(@PathVariable("username") String username);
}
