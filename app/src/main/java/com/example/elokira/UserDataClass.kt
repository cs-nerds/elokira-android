import kotlinx.serialization.Serializable

@Serializable
data class User(
    val idNumber: String,

    val firstName: String,

)