package ord.pumped.configuration

import io.ktor.http.Cookie
import io.ktor.server.sessions.SameSite

const val BB_COOKIE = "bb_session_token"

fun userTokenCookie(token: String): Cookie {
    return Cookie(
        name = BB_COOKIE,
        value = token,
        path = "/",
        httpOnly = true,
        extensions = mapOf("SameSite" to SameSite.Lax)
    )
}