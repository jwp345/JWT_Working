package com.likelion.stepstone.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.likelion.stepstone.authentication.PrincipalDetails;
import java.util.Date;


public class JwtTokenProvider {
  public static String provide(PrincipalDetails principalDetails) {
    return JWT.create()
            .withSubject("stepStone")
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
            .withClaim("userid", principalDetails.getUser().getUserId())
            .withClaim("name", principalDetails.getUser().getName())
            .withClaim("role", principalDetails.getUser().getRole())
            .sign(Algorithm.HMAC256(JwtProperties.SECRET));
  }
}
