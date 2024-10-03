package bank.security.jwt;



import bank.entities.Client;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class JwtService {
    @Value("${app.jwt.secret}")

    private String secretKey;

    public String createToken(Client client){
        return JWT.create()
                .withClaim("email", client.getUsername())
                .withClaim("id", client.getId())
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusHours(1).toInstant())
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String verifyToken(String token){
        JWTVerifier jwtVerify = JWT.require(Algorithm.HMAC512(secretKey)).build();
        DecodedJWT decodedJWT = jwtVerify.verify(token);
        return decodedJWT.getClaim("email").asString();
    }
}