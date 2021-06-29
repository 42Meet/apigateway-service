package kr.meet42.apigatewayservice.client;

import kr.meet42.apigatewayservice.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="member-service", url="42meet.kro.kr:8080")
@Component
public interface MemberServiceClient {
    @PostMapping("/refresh")
    TokenDto verifyToken(@RequestBody TokenDto tokenDto);
}
