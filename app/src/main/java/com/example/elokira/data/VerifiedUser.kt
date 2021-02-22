import kotlinx.serialization.Serializable

@Serializable
data class VerifiedUser(
    val firstName: String,
    val lastName:String,
    val idNumber:String,
    val phoneNumber: String
)