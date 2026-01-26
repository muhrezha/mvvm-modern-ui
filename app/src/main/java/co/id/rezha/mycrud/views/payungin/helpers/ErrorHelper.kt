package co.id.rezha.mycrud.views.payungin.helpers

import android.content.Context
import android.util.Log
import co.id.rezha.mycrud.views.payungin.data.models.responses.ErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorsHelper(
    private val context: Context,
    private val error: Throwable,
    private val funcName: String? = null
) {

    suspend fun convertError(
        onForbiddenException: ((error: ErrorResponse?) -> Unit)? = null,
        onTimeoutException: (() -> Unit)? = null,
        onNotModifiedException: (() -> Unit)? = null,
        onBadRequestException: ((error: ErrorResponse?) -> Unit)? = null,
        onSocketException: (() -> Unit)? = null,
        onUnauthenticateException: ((response: String?) -> Unit)? = null,
        onNotFoundException: ((error: ErrorResponse?) -> Unit)? = null,
        onUnreachableException: (() -> Unit)? = null,
        onPayloadTooLargeException: (() -> Unit)? = null,
        onPayloadException: ((error: ErrorResponse?) -> Unit)? = null,
        onHandledException: ((error: ErrorResponse?) -> Unit)? = null,
        onUncaughtException: ((error: Any?) -> Unit)? = null
    ) = withContext(Dispatchers.Main) {

        when (error) {
            is SocketException, is UnknownHostException -> {
                onSocketException?.invoke() ?: showDefaultSocketError()
            }

            is SocketTimeoutException, is IOException -> {
                onTimeoutException?.invoke() ?: showDefaultTimeoutError()
            }

            is HttpException -> {
                handleHttpException(error, onForbiddenException, onTimeoutException,
                    onNotModifiedException, onBadRequestException, onUnauthenticateException,
                    onNotFoundException, onUnreachableException, onPayloadTooLargeException, onPayloadException,
                    onHandledException, onUncaughtException)
            }

            else -> {
                onUncaughtException?.invoke(error) ?: showDefaultUncaughtError(error)
            }
        }
    }

    private fun handleHttpException(
        exception: HttpException,
        onForbiddenException: ((ErrorResponse?) -> Unit)?,
        onTimeoutException: (() -> Unit)?,
        onNotModifiedException: (() -> Unit)?,
        onBadRequestException: ((ErrorResponse?) -> Unit)?,
        onUnauthenticateException: ((String?) -> Unit)?,
        onNotFoundException: ((ErrorResponse?) -> Unit)?,
        onUnreachableException: (() -> Unit)?,
        onPayloadTooLargeException: (() -> Unit)?,
        onPayloadException: ((ErrorResponse?) -> Unit)?,
        onHandledException: ((ErrorResponse?) -> Unit)?,
        onUncaughtException: ((Any?) -> Unit)?
    ) {
        val errorResponse = parseErrorResponse(exception)
        val contentMessage = parseErrorContentMessage(exception)

        when (exception.code()) {
            304 -> {
                onNotModifiedException?.invoke() ?: showDefaultNotModifiedError()
            }

            400 -> {
                onBadRequestException?.invoke(errorResponse) ?: showDefaultBadRequestError(contentMessage)
            }

            401 -> {
                onUnauthenticateException?.invoke(exception.message()) ?: handleUnauthenticated()
            }

            403 -> {
                onForbiddenException?.invoke(errorResponse) ?: showDefaultForbiddenError()
            }

            404 -> {
                onNotFoundException?.invoke(errorResponse) ?: showDefaultNotFoundError(contentMessage)
            }

            413 -> {
                onPayloadTooLargeException?.invoke() ?: showDefaultPayloadTooLargeError()
            }

            422 -> {
                onPayloadException?.invoke(errorResponse) ?: showDefaultHttpError(contentMessage)
            }

            500 -> {
                onHandledException?.invoke(errorResponse) ?: showDefaultInternalServerError()
            }

            501 -> {
                onHandledException?.invoke(errorResponse) ?: showDefaultNotImplementedError()
            }

            503 -> {
                onHandledException?.invoke(errorResponse) ?: showDefaultServiceUnavailableError()
            }

            504 -> {
                onUnreachableException?.invoke() ?: showDefaultGatewayTimeoutError()
            }

            else -> {
                onUncaughtException?.invoke(exception) ?: showDefaultHttpError(contentMessage)
            }
        }
    }

    private fun parseErrorResponse(exception: HttpException): ErrorResponse? {
        return try {
            val errorBody = exception.response()?.errorBody()?.string()
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun parseErrorContentMessage(exception: HttpException): String {
        return try {
//            val errorBody = exception.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
//            errorResponse.message ?: "Terjadi kesalahan"
            // Ambil status code
            val statusCode = exception.code()
            Log.e("ErrorHelper", "Status Code: $statusCode")
            Log.e("ErrorHelper", "Exception: $exception")
            Log.e("ErrorHelper", "Response: ${exception.response()}")
            Log.e("ErrorHelper", "ErrorBody: ${exception.response()?.errorBody()}")

            val errorBody = exception.response()?.errorBody()?.string()
            Log.e("ErrorHelper", "ErrorBody string: $errorBody")

            if (errorBody.isNullOrEmpty()) {
                // Return dengan info status code
                return "${exception.message() ?: "Terjadi kesalahan"} (Code: $statusCode)"
            }

            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            Log.e("ErrorHelper", "Parsed errorResponse: $errorResponse")

            // Return message dengan status code info
            "${errorResponse?.message ?: "Terjadi kesalahan"} (Code: $statusCode)"

        } catch (e: Exception) {
            exception.message ?: "Terjadi kesalahan"
        }
    }

    // Default Error Handlers
    private fun showDefaultSocketError() {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("Kami mengalami kesulitan memuat aplikasi sepenuhnya. Kemungkinan koneksi internet sedang lambat atau ada kendala jaringan. Coba sambungkan ulang dan pastikan koneksi internet stabil, ya.")
            .setPositiveButton("Coba lagi") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultTimeoutError() {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("Waktu koneksi habis. Silakan coba lagi.")
            .setPositiveButton("Coba lagi") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultNotModifiedError() {
        CustomDialogBuilder(context)
            .setTitle("Informasi")
            .setMessage("Tidak ada perubahan data")
            .setPositiveButton("Oke") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultBadRequestError(message: String) {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage(message)
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun handleUnauthenticated() {
        // Handle logout atau redirect ke login
        // context.signingOutApp() // Uncomment jika ada fungsi logout
        CustomDialogBuilder(context)
            .setTitle("Sesi Berakhir")
            .setMessage("Sesi Anda telah berakhir, silakan masuk kembali")
            .setPositiveButton("Masuk") {
                // Redirect to login
            }
            .setCancelable(false)
            .show()
    }

    private fun showDefaultForbiddenError() {
        CustomDialogBuilder(context)
            .setTitle("Akses Ditolak")
            .setMessage("Anda tidak memiliki izin untuk mengakses resource ini")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultNotFoundError(message: String) {
        CustomDialogBuilder(context)
            .setTitle("Perhatian")
            .setMessage(message)
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultPayloadTooLargeError() {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("File yang diunggah terlalu besar. Maksimal 2MB")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultInternalServerError() {
        CustomDialogBuilder(context)
            .setTitle("Informasi")
            .setMessage("Terjadi kesalahan pada server. Silakan coba lagi nanti.")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultNotImplementedError() {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("Fitur belum dapat diakses")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultServiceUnavailableError() {
        CustomDialogBuilder(context)
            .setTitle("Under Maintenance")
            .setMessage("Saat ini server kami sedang dalam perbaikan. Silakan ulangi beberapa saat lagi.")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultGatewayTimeoutError() {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("Server sedang sibuk. Silakan coba beberapa saat lagi")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultHttpError(message: String) {
        CustomDialogBuilder(context)
            .setTitle("Perhatian")
            .setMessage(message)
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }

    private fun showDefaultUncaughtError(error: Throwable) {
        CustomDialogBuilder(context)
            .setTitle("Terjadi Kesalahan")
            .setMessage("${funcName ?: "Error"}:\n${error.message}")
            .setPositiveButton("Tutup") {}
            .setCancelable(false)
            .show()
    }
}