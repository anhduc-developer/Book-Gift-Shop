package dev.anhduc.bookgiftshop.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    @Value("${duck.jwt.base64-secret}")
    private String jwtKey;

    @Value("${duck.jwt.token-validity-in-seconds}")
    private long jwtExpiration;

    public String createToken(Authentication authentication) {
        Instant now = Instant.now(); // tính thời gian hết hạn kể từ bây giờ
        Instant validity = now.plus(this.jwtExpiration, ChronoUnit.SECONDS); // thời gian hết hạn
        JwtClaimsSet claims = JwtClaimsSet.builder() // tạo payload
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("duck", authentication)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build(); // Chứa thông tin thuật toán
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue(); // secret bên trong
                                                                                                     // encode
    }
}
