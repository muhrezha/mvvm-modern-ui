package co.id.rezha.mycrud.views.setting_permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val permissionManager = PermissionManager.instance


    companion object {
        private const val TAG = "SettingActivity"
        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val PERMISSION_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION

        // Gallery permissions based on API level
        private fun getGalleryPermissions(): Array<String> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+)
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                // Android 12 and below
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // Activity result launcher for permission requests
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        handlePermissionResult(permissions)
    }

    // Activity result launcher for app settings
    private val appSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Update checkbox states when returning from settings
        updateCheckboxStates()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        updateCheckboxStates()
    }

    // Tambahkan di awal class
    private fun checkPermissionStatus(permission: String) {
        val isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        val shouldShowRationale = shouldShowRequestPermissionRationale(permission)

        Log.d(TAG, "Permission $permission - Granted: $isGranted, ShouldShowRationale: $shouldShowRationale")
    }

    // Panggil ini sebelum request permission untuk debugging
    private fun debugPermissionStatus(permissions: Array<String>) {
        permissions.forEach { permission ->
            checkPermissionStatus(permission)
        }
    }

    private fun setupUI() {
        // Camera permission
        binding.listTileCamera.setOnClickListener {
            requestCameraPermission()
        }

        // Gallery permission
        binding.listTileGallery.setOnClickListener {
            requestGalleryPermission()
        }

        // Location permission
        binding.listTileLocation.setOnClickListener {
            requestLocationPermission()
        }

        // Back button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun requestCameraPermission() {
        if (permissionManager.hasCameraPermission(this)) {
            binding.checkboxCamera.isChecked = true
            Toast.makeText(this, "Camera permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            requestSinglePermission(PermissionManager.PERMISSION_CAMERA, "camera")
        }
    }

    private fun requestGalleryPermission() {
        if (permissionManager.hasGalleryPermission(this)) {
            binding.checkboxGallery.isChecked = true
            Toast.makeText(this, "Gallery permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            val galleryPermissions = PermissionManager.getGalleryPermissions()
            if (galleryPermissions.size == 1) {
                requestSinglePermission(galleryPermissions[0], "gallery")
            } else {
                requestMultiplePermissions(galleryPermissions, "gallery")
            }
        }
    }

    private fun requestLocationPermission() {
        Log.d(TAG, "Requesting location permission")

        debugPermissionStatus(arrayOf(PERMISSION_LOCATION))


        if (hasLocationPermission()) {
            // Already granted
            binding.checkboxLocation.isChecked = true
            Toast.makeText(this, "Location permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            // Request permission
            requestSinglePermission(PERMISSION_LOCATION, "location")
        }
    }

    private fun requestSinglePermission(permission: String, permissionType: String) {
        Log.d(TAG, "Requesting single permission: $permission")

        // Check if we should show rationale
        val shouldShowRationale = shouldShowRequestPermissionRationale(permission)

        Log.d(TAG, "Should show rationale for $permission: $shouldShowRationale")

        if (shouldShowRationale) {
            // Show explanation dialog
            showPermissionRationaleDialog(arrayOf(permission), permissionType)
        } else {
            // Request permission directly
            Log.d(TAG, "Launching permission request for: $permission")
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun requestMultiplePermissions(permissions: Array<String>, permissionType: String) {
        Log.d(TAG, "Requesting multiple permissions: ${permissions.joinToString()}")

        // Check if any permission should show rationale
        val shouldShowRationale = permissions.any { permission ->
            shouldShowRequestPermissionRationale(permission)
        }

        Log.d(TAG, "Should show rationale for $permissionType: $shouldShowRationale")

        if (shouldShowRationale) {
            // Show explanation dialog
            showPermissionRationaleDialog(permissions, permissionType)
        } else {
            // Request permissions directly
            Log.d(TAG, "Launching multiple permission request for: ${permissions.joinToString()}")
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun handlePermissionResult(permissions: Map<String, Boolean>) {
        Log.d(TAG, "Permission result: $permissions")

        var allGranted = true
        var anyPermanentlyDenied = false

        permissions.entries.forEach { (permission, isGranted) ->
            Log.d(TAG, "Permission $permission: granted=$isGranted")

            if (!isGranted) {
                allGranted = false
                // Check if permanently denied
                val isPermanentlyDenied = !shouldShowRequestPermissionRationale(permission)
                Log.d(TAG, "Permission $permission permanently denied: $isPermanentlyDenied")

                if (isPermanentlyDenied) {
                    anyPermanentlyDenied = true
                }
            }
        }

        Log.d(TAG, "All granted: $allGranted, Any permanently denied: $anyPermanentlyDenied")

        if (allGranted) {
            // All permissions granted
            updateCheckboxStates()
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else if (anyPermanentlyDenied) {
            // Some permissions permanently denied
            Log.d(TAG, "Showing go to settings dialog due to permanently denied permissions")
            showGoToSettingsDialog()
        } else {
            // Some permissions denied but not permanently
            updateCheckboxStates()
            Toast.makeText(this, "Some permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Update checkbox states menggunakan PermissionManager
    private fun updateCheckboxStates() {
        binding.checkboxCamera.isChecked = permissionManager.hasCameraPermission(this)
        binding.checkboxGallery.isChecked = permissionManager.hasGalleryPermission(this)
        binding.checkboxLocation.isChecked = permissionManager.hasLocationPermission(this)
    }

  /*  private fun updateCheckboxStates() {
        // Update camera checkbox
        binding.checkboxCamera.isChecked = hasCameraPermission()

        // Update gallery checkbox
        binding.checkboxGallery.isChecked = hasGalleryPermission()

        // Update location checkbox
        binding.checkboxLocation.isChecked = hasLocationPermission()

        Log.d(TAG, "Checkbox states - Camera: ${binding.checkboxCamera.isChecked}, " +
                "Gallery: ${binding.checkboxGallery.isChecked}, " +
                "Location: ${binding.checkboxLocation.isChecked}")
    }*/

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasGalleryPermission(): Boolean {
        val galleryPermissions = getGalleryPermissions()
        return galleryPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRationaleDialog(permissions: Array<String>, permissionType: String) {
        val message = when (permissionType) {
            "camera" -> "This app needs camera permission to take photos and scan barcodes."
            "gallery" -> "This app needs gallery permission to access your photos and media."
            "location" -> "This app needs location permission to provide location-based services."
            else -> "This app needs these permissions to function properly."
        }

        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                Log.d(TAG, "User accepted rationale, requesting permissions: ${permissions.joinToString()}")
                requestPermissionLauncher.launch(permissions)
            }
            .setNegativeButton("Cancel") { _, _ ->
                Log.d(TAG, "User cancelled permission request")
                // Do nothing, user cancelled
            }
            .setCancelable(false)
            .show()
    }

    private fun showGoToSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage("Some permissions are permanently denied. Please enable them in app settings to use all features.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Do nothing, user cancelled
            }
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        appSettingsLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        // Update checkbox states when returning to activity
        Log.d(TAG, "onResume: Updating checkbox states")
        updateCheckboxStates()
    }
}