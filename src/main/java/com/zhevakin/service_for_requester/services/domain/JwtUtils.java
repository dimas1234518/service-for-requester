package com.zhevakin.service_for_requester.services.domain;

import com.zhevakin.service_for_requester.models.domain.JwtAuthentication;
import com.zhevakin.service_for_requester.models.domain.Role;
import io.jsonwebtoken.Claims;

import java.util.HashSet;
import java.util.Set;

public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUsername(claims.get("username", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    // TODO: перенести разбор групп сюда, убрать получение User из UserService в контроллерах

    private static Set<Role> getRoles(Claims claims) {

        return new HashSet<>();
    }
}
