package io.positivinh.virtuoso.web.security.dummy.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component("appAuthorizationFilter")
@ConditionalOnProperty(value = ["dummy.authorization.filter.enabled"], havingValue = "true", matchIfMissing = true)
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