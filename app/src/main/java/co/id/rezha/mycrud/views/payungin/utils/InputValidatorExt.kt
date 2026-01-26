package co.id.rezha.mycrud.views.payungin.utils


import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.validateRequired(errorMessage: String = "Field ini wajib diisi"): Boolean {
    return InputValidatorUtils.validateRequired(this, errorMessage)
}

fun TextInputLayout.validateMinLength(minLength: Int, errorMessage: String = "Minimal $minLength karakter"): Boolean {
    return InputValidatorUtils.validateMinLength(this, minLength, errorMessage)
}

fun TextInputLayout.validateMaxLength(maxLength: Int, errorMessage: String = "Maksimal $maxLength karakter"): Boolean {
    return InputValidatorUtils.validateMaxLength(this, maxLength, errorMessage)
}

fun TextInputLayout.validateFullName(errorMessage: String = "Hanya boleh huruf, koma(,) dan titik(.)"): Boolean {
    return InputValidatorUtils.validateFullName(this, errorMessage)
}



fun TextInputLayout.validatePhoneNumber(errorMessage: String = "Hanya boleh angka 0-9"): Boolean {
    return InputValidatorUtils.validatePhoneNumber(this, errorMessage)
}

fun TextInputLayout.validateFullNameField(minLength: Int = 3, maxLength: Int = 100): Boolean {
    return InputValidatorUtils.validateFullNameField(this, minLength, maxLength)
}



fun TextInputLayout.validatePhoneField(): Boolean {
    return InputValidatorUtils.validatePhoneField(this)
}


// ✅ TAMBAHAN VALIDASI EMAIL
fun TextInputLayout.validateEmailFormat(errorMessage: String = "Format email tidak valid"): Boolean {
    return InputValidatorUtils.validateEmailFormat(this, errorMessage)
}

fun TextInputLayout.validateEmailField(): Boolean {
    return InputValidatorUtils.validateEmailField(this)
}

fun TextInputLayout.validateEmailWithDomain(
    allowedDomains: List<String> = emptyList(),
    errorMessage: String = "Domain email tidak diizinkan"
): Boolean {
    return InputValidatorUtils.validateEmailWithDomain(this, allowedDomains, errorMessage)
}


fun TextInputLayout.validatePassword(errorMessage: String = "Password minimal 8 karakter, mengandung huruf kapital, angka, dan simbol"): Boolean {
    return InputValidatorUtils.validatePassword(this, errorMessage)
}

fun TextInputLayout.validatePasswordField(minLength: Int = 8): Boolean {
    return InputValidatorUtils.validatePasswordField(this, minLength)
}

fun TextInputLayout.validatePasswordConfirmation(
    confirmPasswordLayout: TextInputLayout,
    errorMessage: String = "Konfirmasi password tidak sesuai"
): Boolean {
    return InputValidatorUtils.validatePasswordConfirmation(
        this,
        confirmPasswordLayout,
        errorMessage
    )
}
