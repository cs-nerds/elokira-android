import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val loginId: Int,
    var loginCode: Int?
)