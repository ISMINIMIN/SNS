package minzdev.sns.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minzdev.sns.model.dto.User;
import minzdev.sns.service.UserService;
import minzdev.sns.util.JwtTokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token;

        try {
            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                log.info("[TOKEN_IN_PARAM_URLS] Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            } else {
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if(header == null || !header.startsWith("Bearer ")) {
                    log.error("[ERROR] occurs while getting header - header is null or invalid ({})", request.getRequestURL());
                    filterChain.doFilter(request, response);
                    return;
                }

                token = header.split(" ")[1].trim();
            }

            // 토큰 검증
            if(jwtTokenUtils.isExpired(token)) {
                log.error("[ERROR] key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            // 유저(username) 검증
            String username = jwtTokenUtils.getUsername(token);
            User user = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
              user, null, user.getAuthorities()
            );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch(RuntimeException exception) {
            log.error("[ERROR] occurs while token validating - {}", exception.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
