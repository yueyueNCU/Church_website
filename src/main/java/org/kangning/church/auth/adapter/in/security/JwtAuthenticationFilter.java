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
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.application.port.out.MembershipRepositoryPort;
import org.kangning.church.membership.domain.Membership;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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
    private final MembershipRepositoryPort membershipRepository;

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
        UserId userId = jwtProvider.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String churchIdHeader = request.getHeader("X-Church-Id");
        Long churchId = (churchIdHeader != null) ? Long.parseLong(churchIdHeader) : null;

        // ✨ 驗證 churchId from path 與 header 是否一致
        Long churchIdFromPath = extractChurchIdFromPath(request.getRequestURI());
        if (churchId != null && churchIdFromPath != null && !churchId.equals(churchIdFromPath)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("X-Church-Id does not match path churchId.");
            return;
        }

        List<String> authorities = new ArrayList<>();

        // Global roles
        if (user.getGlobalRoles() != null) {
            authorities.addAll(user.getGlobalRoles().stream()
                    .map(role -> "ROLE_" + role.name())
                    .toList());
        }

        // Church roles
        Optional<Membership> membership = membershipRepository.findByChurchIdAndUserId(new ChurchId(churchId),userId);

        if (churchId != null && membership.isPresent()) {

            authorities.addAll(membership.get().getRoles().stream()
                    .map(role -> "ROLE_" + role.name())
                    .toList());
        }

        UserPrincipal principal = new UserPrincipal(user.getId(), user.getUsername());

        var auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private Long extractChurchIdFromPath(String uri) {
        String[] parts = uri.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("church")) {
                try {
                    return Long.parseLong(parts[i + 1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
