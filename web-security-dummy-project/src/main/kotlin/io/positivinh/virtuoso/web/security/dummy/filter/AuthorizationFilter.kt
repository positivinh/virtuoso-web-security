package io.positivinh.virtuoso.web.security.dummy.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Qualifier("authorizationFilter")
class AuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                "user", "", mutableSetOf(SimpleGrantedAuthority("ROLE_USER"))
            )
        }

        filterChain.doFilter(request, response)
    }
}