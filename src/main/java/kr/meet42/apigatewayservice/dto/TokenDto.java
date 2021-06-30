package kr.meet42.apigatewayservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
