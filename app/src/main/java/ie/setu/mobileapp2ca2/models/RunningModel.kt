package ie.setu.mobileapp2ca2.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class RunningModel(
    var uid: String? = "",
    var title: String? = "title",
    var description: String? = "desc",
    var email: String? = "joe@bloggs.com",
    var profilepic: String = "",
    var startLatitude: Double = 0.0,
    var startLongitude: Double = 0.0,
    var endLatitude: Double = 0.0,
    var endLongitude: Double = 0.0,
    var difficulty : Int = 0,
    var distance : Float = 0f,
    var weatherCondition : String = "" //enum class CLEAR,SUNNY,CLOUDY,RAINY
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "email" to email,
            "profilepic" to profilepic,
            "startLatitude" to startLatitude,
            "startLongitude" to startLongitude,
            "endLatitude" to endLatitude,
            "endLongitude" to endLongitude,
            "difficulty" to difficulty,
            "distance" to distance,
            "weatherCondition" to weatherCondition
        )
    }
}