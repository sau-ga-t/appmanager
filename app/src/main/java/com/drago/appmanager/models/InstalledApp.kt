import android.graphics.drawable.Drawable

data class InstalledApp(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable,
    val version: String,
    val appSize: Long
)
