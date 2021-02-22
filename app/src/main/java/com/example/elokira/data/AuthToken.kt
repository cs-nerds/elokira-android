import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val authToken: AuthToken
)