package cz.pokebowl.routes.plugins

import cz.pokebowl.config.AppConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.RouteScopedPlugin
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.response.respond

object RoutePlugins {

    fun createAdminAuthPlugin(appConfig: AppConfig): RouteScopedPlugin<Unit> {
        return createRouteScopedPlugin("AdminAuthPlugin") {
            onCall { call ->
                val secret = call.request.headers["X-Admin-Secret"]
                if (secret != appConfig.admin.secret) {
                    call.respond(HttpStatusCode.Forbidden, "Access forbidden")
                }
            }
        }
    }
}
