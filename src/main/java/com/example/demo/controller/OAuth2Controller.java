package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class OAuth2Controller {

    @GetMapping("/oauth2/success")
    public RedirectView oauth2Success(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = (String) request.getAttribute("jwt");
        if (token == null) token = "NO_TOKEN";

        String redirectUrl = "http://localhost:5173/oauth2/success?token=" + token;
        return new RedirectView(redirectUrl);
    }
}
