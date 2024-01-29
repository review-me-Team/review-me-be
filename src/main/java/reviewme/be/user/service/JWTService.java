package reviewme.be.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reviewme.be.user.dto.UserGitHubProfile;
import reviewme.be.user.dto.response.UserProfileResponse;

import java.util.Date;
import java.util.LinkedHashMap;

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

        } catch (Exception e) {
            return true;
        }
    }

    public UserGitHubProfile getUserProfileFromJwt(String jwt) {

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt);

        return claimsJws.getBody().get(USER_PROFILE, UserGitHubProfile.class);
    }

    public <T> T extractUserFromJwt(String jwt, Class<T> type) {

        String[] jwtParts = jwt.split("\\.");
        String encodedPayload = jwtParts[1];

        byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(encodedPayload);
        String decodedPayload = new String(decodedBytes);

        ObjectMapper objectMapper = new ObjectMapper();

        return parseUserFromJwt(decodedPayload, objectMapper, type);
    }

    private <T> T parseUserFromJwt(String decodedPayload, ObjectMapper objectMapper, Class<T> type) {

        try {
            LinkedHashMap payloadMap = objectMapper.readValue(decodedPayload, LinkedHashMap.class);
            Object userProfile = payloadMap.get(USER_PROFILE);
            String userProfileJson = objectMapper.writeValueAsString(userProfile);

            return objectMapper.readValue(decodedPayload, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다.");
        }
    }
}
