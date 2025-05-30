package ord.pumped.io.websocket.auth.request

import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.isNotBlank
import kotlinx.serialization.Serializable
import ord.pumped.io.websocket.auth.request.validation.accessors.socketID

@Serializable
@Validate
data class UpgradeWebsocketRequest(val socketID: String)

val validateUpgradeWebsocketRequest = Validator<UpgradeWebsocketRequest> {
    socketID.isNotBlank()
}