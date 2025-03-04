package io.positivinh.virtuoso.web.security.dummy.permission

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component("appCustomPermissionEvaluator")
class AppCustomPermissionEvaluator : PermissionEvaluator {

    override fun hasPermission(
        authentication: Authentication?,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean {

        return targetDomainObject == "ok" && permission == "read"
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        TODO("Not yet implemented")
    }
}