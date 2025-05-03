package io.positivinh.virtuoso.web.security.autoconfigure.filter

import com.crabshue.commons.kotlin.logging.getLogger
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.AuthorizationHeadersConfigurationProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@ConditionalOnWebApplication
class VirtuosoHeaderAuthorizationFilter(val authorizationHeadersConfigurationProperties: AuthorizationHeadersConfigurationProperties) :
    OncePerRequestFilter() {

    private val log = getLogger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val username = request.getHeader(authorizationHeadersConfigurationProperties.username)

        val authorities = request.getHeader(authorizationHeadersConfigurationProperties.authorities)?.split(";")

        val simpleGrantedAuthorities = authorities?.map { SimpleGrantedAuthority(it.trim()) } ?: listOf()

        username?.let {
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                User(username, "", simpleGrantedAuthorities),
                "",
                simpleGrantedAuthorities
            )
            SecurityContextHolder.getContextHolderStrategy().context.authentication =
                PreAuthenticatedAuthenticationToken(
                    User(username, "", simpleGrantedAuthorities),
                    "",
                    simpleGrantedAuthorities
                )

            log.debug(
                "Authenticated [{}] via X-Virtuoso authentication headers",
                SecurityContextHolder.getContext().authentication
            )
        }

        filterChain.doFilter(request, response)
    }
}