package co.id.rezha.mycrud.views.setting_permission

// PermissionManager.kt
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionManager private constructor() {

    companion object {
        private const val TAG = "PermissionManager"

        // Permission constants
        const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        const val PERMISSION_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION

        // Gallery permissions based on API level
        fun getGalleryPermissions(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // Instance for easier usage
        val instance = PermissionManager()
    }

    // Check single permission
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Check multiple permissions
    fun hasAllPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            hasPermission(context, permission)
        }
    }

    // Check gallery permission (convenience method)
    fun hasGalleryPermission(context: Context): Boolean {
        return hasAllPermissions(context, getGalleryPermissions())
    }

    // Check camera permission (convenience method)
    fun hasCameraPermission(context: Context): Boolean {
        return hasPermission(context, PERMISSION_CAMERA)
    }

    // Check location permission (convenience method)
    fun hasLocationPermission(context: Context): Boolean {
        return hasPermission(context, PERMISSION_LOCATION)
    }

    // Check if should show rationale
    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    // Check if any permission is permanently denied
    fun isAnyPermissionPermanentlyDenied(activity: Activity, permissions: Array<String>): Boolean {
        return permissions.any { permission ->
            !hasPermission(activity, permission) &&
                    !shouldShowRationale(activity, permission)
        }
    }

    // Show rationale dialog
    fun showPermissionRationale(
        activity: Activity,
        permissions: Array<String>,
        title: String = "Permission Required",
        message: String = "This app needs these permissions to function properly.",
        onGrant: () -> Unit,
        onDeny: () -> Unit = {}
    ) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> onGrant() }
            .setNegativeButton("Cancel") { _, _ -> onDeny() }
            .setCancelable(false)
            .show()
    }

    // Show go to settings dialog
    fun showGoToSettingsDialog(
        activity: Activity,
        title: String = "Permissions Required",
        message: String = "Some permissions are permanently denied. Please enable them in app settings.",
        onSettings: () -> Unit,
        onCancel: () -> Unit = {}
    ) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Go to Settings") { _, _ -> onSettings() }
            .setNegativeButton("Cancel") { _, _ -> onCancel() }
            .setCancelable(false)
            .show()
    }

    // Open app settings
    fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        activity.startActivity(intent)
    }
}