package com.example.expenseo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService; // Spring's interface for fetching users

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Check if header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            // Pass the request to the next filter.
            // If the endpoint is secured, Spring will block it automatically later.
            filterChain.doFilter(request,response);
        }

        // 3. Extract the token (Remove "Bearer " which is 7 characters)
        jwt=authHeader.substring(7);

        try {

            // 4. Extract email from the token
            userEmail = jwtUtils.extractUsername(jwt);

            // 5. If we have an email and the user is NOT already authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){

                // Fetch the user from the database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 6. Validate the token
                if (jwtUtils.isTokenValid(jwt , userDetails)){
                    // Create an authentication token (This is Spring's way of marking someone as "logged in")
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    // Attach metadata about the request (like the user's IP address)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. Store the authentication in the Security Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);

        }catch (Exception e){
            // In production, you might want to log this error.
            // If the token is expired or malformed, it throws an exception.
            // We catch it and do nothing, so
            // the filter chain continues and Spring denies access.
        }

    }
}
