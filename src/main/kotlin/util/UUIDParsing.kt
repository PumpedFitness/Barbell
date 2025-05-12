package ord.pumped.util

import java.util.UUID

fun String.toUUID() = UUID.fromString(this)

fun String.toUUIDOrNull() = try { UUID.fromString(this) } catch (e: Exception) { null }