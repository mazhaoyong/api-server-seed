package com.github.imloama.api.demo.controller;

import com.github.imloama.api.config.jwt.JWT;
import com.github.imloama.api.config.jwt.JWTUser;
import com.github.imloama.api.demo.model.User;
import com.github.imloama.mybatisplus.bootext.base.APIResult;
import com.nimbusds.jose.JOSEException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class JWTController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * JWT登陆
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/api/v1/login", method = RequestMethod.POST)
    @ApiOperation(value = "登陆", notes = "登陆成功返回token,测试账户：admin/admin")
    public APIResult login(@RequestParam("username") String username,
                           @RequestParam("password") String password){
        try {
            //用户验证
            Authentication auth = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            //存储认证信息
            SecurityContextHolder.getContext().setAuthentication(auth);
            //生成token
            final User user = (User) auth.getPrincipal();
            JWTUser jwtUser = new JWTUser(user.getId(), user.getUsername());
            String token = JWT.newToken(jwtUser);
            return APIResult.ok("ok", token);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return APIResult.fail("用户登陆失败，用户名或密码不正确！");
        }
    }




}