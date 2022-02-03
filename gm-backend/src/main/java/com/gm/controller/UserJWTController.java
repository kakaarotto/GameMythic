package com.gm.controller;

import com.gm.model.User;
import com.gm.security.jwt.JWTFilter;
import com.gm.security.jwt.TokenProvider;
import com.gm.service.UserService;
import com.gm.service.mapper.UserMapper;
import com.gm.util.JacksonUtil;
import com.gm.util.ResponseUtil;
import com.gm.vm.LoginVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author pujie
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity authorize(@Valid @RequestBody LoginVM vm, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(vm.getUsername().trim(), vm.getPassword());
        try {
            // Verify user info match
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // Save user context permission status
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, vm.isRememberMe());
            // Response token info
            response.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            Optional<User> loginUser = userService.getCurrentUser();
            // User presence returns data relating to successful login
            if(loginUser.isPresent()) {
                return ResponseEntity.ok().body("{\"code\":1,\"message\":\"login success\"," + "\"id_token\":\"" + jwt + "\"," + "\"content\":" + JacksonUtil.toJSON(UserMapper.INSTANCE.modelToDTO(loginUser.get())) + "}");
            }
        }
        catch (AuthenticationException ae) {
            log.error("Authentication exception trace: {}", ae);
        }
        return ResponseUtil.renderError("Please check the email and password");
    }

}
