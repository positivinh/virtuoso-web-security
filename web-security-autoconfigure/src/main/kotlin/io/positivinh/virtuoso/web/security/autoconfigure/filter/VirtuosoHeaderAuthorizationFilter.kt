package io.positivinh.virtuoso.web.security.autoconfigure.filter

import com.crabshue.commons.kotlin.logging.getLogger
import io.positivinh.virtuoso.web.security.autoconfigure.configuration.AuthorizationHeadersConfigurationProperties
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
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

        val simpleGrantedAuthorities = request.getHeader(authorizationHeadersConfigurationProperties.authorities)
            ?.split(";")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.map { SimpleGrantedAuthority(it) }
            ?: listOf()

        username?.takeIf { it.isNotBlank() }?.let {
            val contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
            val context = contextHolderStrategy.createEmptyContext()
            context.authentication = PreAuthenticatedAuthenticationToken(
                User(it, "", simpleGrantedAuthorities),
                "",
                simpleGrantedAuthorities
            )
            contextHolderStrategy.context = context

            log.debug("Authenticated [{}] via X-Virtuoso authentication headers", it)
        }

        filterChain.doFilter(request, response)
    }
}