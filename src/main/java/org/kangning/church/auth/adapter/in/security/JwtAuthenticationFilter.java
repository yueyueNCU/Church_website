package org.kangning.church.auth.adapter.in.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 處理 JWT 認證的 Spring Security 過濾器。
 * 它會攔截 HTTP 請求，從 Authorization 頭部提取 JWT，
 * 驗證 JWT 的有效性，查找用戶，並將用戶權限設置到 SecurityContextHolder 中。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProviderPort jwtProvider;

    private final UserRepositoryPort userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtProvider.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        String churchIdHeader = request.getHeader("X-Church-Id");
        Long churchId = (churchIdHeader != null) ? Long.parseLong(churchIdHeader) : null;


        List<String> authorities = new ArrayList<>();

        // 加入 Global Roles
        if (user.getGlobalRoles() != null) {
            authorities.addAll(user.getGlobalRoles().stream()
                    .map(role -> "ROLE_" + role.name())
                    .toList());
        }

        // 加入教會的Roles
//        if (churchId != null) {
//            authorities.addAll(
//                    user.getMemberships().stream()
//                            .filter(m -> m.isApproved() && m.getChurchId().equals(churchId))
//                            .flatMap(m -> m.getRoles().stream())
//                            .map(r -> "ROLE_" + r.name())
//                            .toList()
//            );
//        }

        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
