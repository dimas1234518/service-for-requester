package com.zhevakin.service_for_requester.controllers.domain;

import com.zhevakin.service_for_requester.models.domain.*;
import com.zhevakin.service_for_requester.services.domain.AuthService;
import com.zhevakin.service_for_requester.services.domain.GroupService;
import com.zhevakin.service_for_requester.services.domain.RoleService;
import com.zhevakin.service_for_requester.services.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Validated
@RestController
@RequestMapping(value = "api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public long sendTimestamp(HttpServletRequest request, @RequestParam("timestamp") long timestamp) throws AuthException {
        String username = request.getHeader("user");
        if (username == null) {
            throw new AuthException("Invalid request");
        }

        long serverTimestamp = System.currentTimeMillis();
        User user = userService.findByUsername(username);

        if (user == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(makePassword(timestamp, serverTimestamp));
            newUser.setRoles(Collections.singleton(Role.VIEWER));
            newUser.setGroups(Collections.singleton(Group.NO_GROUP));
            userService.saveUser(newUser);
        } else {
            user.setPassword(makePassword(timestamp, serverTimestamp));
            userService.saveUser(user);
        }

        // записать информацию о пользователе в бд и передать его далее
        return serverTimestamp;
    }

    private String makePassword(long timestamp, long serverTimestamp) {
        //TODO: переделать дописать bCryptPasswordEncoder.encode
        return String.valueOf(serverTimestamp - timestamp);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws AuthException {
        final JwtResponse token = authService.login(jwtRequest);
        return ResponseEntity.ok(token);
        // авторизовать пользователя в системе
        // сформировать и передать токен пользователя и сбрасывать его каждые 30 минут
//        SecurityContextHolder.getContext().setAuthentication();
    }

    @RequestMapping(value = "/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "/refresh")
    public  ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
