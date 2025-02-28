package io.positivinh.virtuoso.web.security.autoconfigure.filter

import com.crabshue.commons.kotlin.logging.getLogger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class VirtuosoHeaderAuthorizationFilter : OncePerRequestFilter() {

    private val log = getLogger()

    companion object {
        const val VIRTUOSO_USERNAME_HEADER_NAME = "X-Virtuoso-Username"
        const val VIRTUOSO_AUTHORITIES_HEADER_NAME = "X-Virtuoso-Authorities"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val username = request.getHeader(VIRTUOSO_USERNAME_HEADER_NAME)

        val authorities = request.getHeader(VIRTUOSO_AUTHORITIES_HEADER_NAME)?.split(";")

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