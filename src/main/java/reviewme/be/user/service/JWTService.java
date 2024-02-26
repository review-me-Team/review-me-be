package reviewme.be.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reviewme.be.user.dto.response.UserProfileResponse;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import reviewme.be.user.exception.NonExistUserException;

@Slf4j
@Service
public class JWTService {

    private static final String USER_PROFILE = "userProfile";

    @Value("${JWT_SERVER_SECRET}")
    private String secret;

    public String createJwt(UserProfileResponse userProfile, Date expiredDate) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("login_user")
                .claim(USER_PROFILE, userProfile)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateJwtIsExpired(String jwt) {

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt);

            return claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            log.info("JWT exception: {}", e);
            return true;
        }
    }

    public boolean validateJwtIsManipulated(String jwt) {

        try {
            byte[] decodedSecret = Base64.getUrlDecoder().decode(secret);
            Key key = new SecretKeySpec(decodedSecret, 0, decodedSecret.length, "HmacSHA256");

            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt);

            return false;
        } catch (JwtException e) {
            return true;
        }
    }

    public <T> T extractUserFromJwt(String jwt, Class<T> clazz) {

        String[] jwtParts = jwt.split("\\.");
        String encodedPayload = jwtParts[1];

        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPayload);
        String decodedPayload = new String(decodedBytes);

        return parseUserFromJwt(decodedPayload, clazz);
    }

    private <T> T parseUserFromJwt(String decodedPayload, Class<T> clazz) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            LinkedHashMap payloadMap = objectMapper.readValue(decodedPayload, LinkedHashMap.class);
            Object userProfile = payloadMap.get(USER_PROFILE);
            String userProfileJson = objectMapper.writeValueAsString(userProfile);

            return objectMapper.readValue(userProfileJson, clazz);
        } catch (JsonProcessingException e) {
            throw new NonExistUserException("사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.");
        }
    }
}
