package kr.meet42.apigatewayservice.client;

import kr.meet42.apigatewayservice.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FeignClient(name="member")
@Component
public interface MemberServiceClient {
    @PostMapping("/refresh")
    TokenDto verifyToken(@RequestBody TokenDto tokenDto);
}
