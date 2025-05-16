package ord.pumped.configuration

import io.ktor.http.Cookie
import io.ktor.server.sessions.SameSite

const val BB_COOKIE = "bb_session_token"

fun userTokenCookie(token: String, domain: String, isSecure: Boolean): Cookie {
    return Cookie(
        name = BB_COOKIE,
        value = token,
        path = "/",
        httpOnly = true,
        domain = domain,
        secure = isSecure,
        extensions = mapOf("SameSite" to SameSite.Lax)
    )
}