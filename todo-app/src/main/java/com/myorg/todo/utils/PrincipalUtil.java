package com.myorg.todo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.security.Principal;
import java.util.Objects;

public class PrincipalUtil {

    public static String getEmailOfLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {
            Principal principal = (Principal) authentication.getPrincipal();
            if (Objects.nonNull(principal) && principal instanceof OidcUser) {
                OidcUser user = (OidcUser) principal;
                return user.getEmail();
            }
        }
        return "anonymous";
    }

    public static String getEmailOfLoginUser(Principal principal) {
        OAuth2AuthenticationToken authentication = ((OAuth2AuthenticationToken) principal);
        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof OidcUser) {
            OidcUser user = (OidcUser) authentication.getPrincipal();
            return user.getEmail();
        }
        return "unknown";
    }
}
