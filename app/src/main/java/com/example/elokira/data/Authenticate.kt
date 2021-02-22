import kotlinx.serialization.Serializable

@Serializable
data class Authenticate(
    val loginId: String,
    var loginCode: String?
)