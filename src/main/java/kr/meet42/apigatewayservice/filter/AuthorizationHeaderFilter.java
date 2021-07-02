package kr.meet42.apigatewayservice.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import kr.meet42.apigatewayservice.client.MemberServiceClient;
import kr.meet42.apigatewayservice.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>
{
    private final Environment env;
    private final MemberServiceClient memberServiceClient;

    private static String ACCESS_TOKEN = "access-token";
    private static String REFRESH_TOKEN = "refresh-token";

    public AuthorizationHeaderFilter(Environment env, @Lazy MemberServiceClient memberServiceClient) {
        super(Config.class);
        this.memberServiceClient = memberServiceClient;
        this.env = env;
    }

    public static class Config {
        // Put configuration properties here
    }

    // login -> token -> users (with token) -> header(include token)
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (request.getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }
            // refresh token이 있는 경우
            if (request.getHeaders().containsKey(REFRESH_TOKEN)){
                String accessToken = request.getHeaders().get(ACCESS_TOKEN).get(0);
                String refreshToken = request.getHeaders().get(REFRESH_TOKEN).get(0);
                TokenDto tokenDto =
                            memberServiceClient.refreshToken(accessToken, refreshToken);
                if (tokenDto != null){
                    try {
                        ServerHttpRequest m_request = exchange.getRequest().mutate()
                                .header(ACCESS_TOKEN, tokenDto.getAccessToken())
                                .build();
                        return chain.filter(exchange.mutate().request(m_request).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        ServerHttpResponse m_response = exchange.getResponse();
                        URI uri = new URI(env.getProperty("42meet.server.login"));
                        m_response.getHeaders().setLocation(uri);
                        m_response.setStatusCode(HttpStatus.FOUND);
                        m_response.setComplete();
                        return chain.filter(exchange.mutate().response(m_response).build());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

            if (!request.getHeaders().containsKey(ACCESS_TOKEN)){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(ACCESS_TOKEN).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            log.info(jwt);
            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = false;
        String subject = null;
        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(jwt).getBody().getSubject();
            if (subject == null || subject.isEmpty()) {
                returnValue = false;
            } else {
                returnValue = true;
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT Signature입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }
}
