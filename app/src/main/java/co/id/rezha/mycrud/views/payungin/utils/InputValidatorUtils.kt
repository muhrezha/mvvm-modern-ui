package co.id.rezha.mycrud.views.payungin.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class InputValidatorUtils {

    companion object {

        /**
         * Validasi input required (wajib diisi)
         */
        fun validateRequired(
            textInputLayout: TextInputLayout,
            errorMessage: String = "Field ini wajib diisi"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            return if (text.isNullOrEmpty()) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi minimal karakter
         */
        fun validateMinLength(
            textInputLayout: TextInputLayout,
            minLength: Int,
            errorMessage: String = "Minimal $minLength karakter"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            return if (text.isNullOrEmpty() || text.length < minLength) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi maksimal karakter
         */
        fun validateMaxLength(
            textInputLayout: TextInputLayout,
            maxLength: Int,
            errorMessage: String = "Maksimal $maxLength karakter"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            return if (!text.isNullOrEmpty() && text.length > maxLength) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi fullname: huruf, koma, dan titik saja
         */
        fun validateFullName(
            textInputLayout: TextInputLayout,
            errorMessage: String = "Hanya boleh huruf, koma(,) dan titik(.)"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()
            // Pattern: huruf (a-zA-Z), koma, titik, dan spasi
            val pattern = Pattern.compile("^[a-zA-Z\\s,.]*$")

            return if (text.isNullOrEmpty() || !pattern.matcher(text).matches()) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi phone: hanya angka 0-9
         */
        fun validatePhoneNumber(
            textInputLayout: TextInputLayout,
            errorMessage: String = "Hanya boleh angka 0-9"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()
            // Pattern: hanya angka
            val pattern = Pattern.compile("^[0-9]*$")

            return if (text.isNullOrEmpty() || !pattern.matcher(text).matches()) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi kombinasi untuk fullname
         */
        fun validateFullNameField(
            textInputLayout: TextInputLayout,
            minLength: Int = 3,
            maxLength: Int = 100
        ): Boolean {
            // Validasi required
            if (!validateRequired(textInputLayout)) {
                return false
            }

            // Validasi minimal karakter
            if (!validateMinLength(textInputLayout, minLength)) {
                return false
            }

            // Validasi maksimal karakter
            if (!validateMaxLength(textInputLayout, maxLength)) {
                return false
            }

            // Validasi format fullname
            if (!validateFullName(textInputLayout)) {
                return false
            }

            return true
        }



        /**
         * Validasi kombinasi untuk phone number
         */
        fun validatePhoneField(
            textInputLayout: TextInputLayout
        ): Boolean {
            // Validasi required
            if (!validateRequired(textInputLayout)) {
                return false
            }

            // Validasi format phone
            if (!validatePhoneNumber(textInputLayout)) {
                return false
            }

            return true
        }


        fun validateEmailFormat(
            textInputLayout: TextInputLayout,
            errorMessage: String = "Format email tidak valid"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            // Pattern email standard
            val emailPattern = Patterns.EMAIL_ADDRESS

            return if (text.isNullOrEmpty() || !emailPattern.matcher(text).matches()) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi kombinasi untuk email field (required + format)
         */
        fun validateEmailField(
            textInputLayout: TextInputLayout,
        ): Boolean {
            // Validasi required
            if (!validateRequired(textInputLayout)) {
                textInputLayout.error = "Email harus disiini"
                return false
            }

            // Validasi format email
            if (!validateEmailFormat(textInputLayout)) {
                textInputLayout.error = "Email tidak valid"
                return false
            }

            return true
        }


        /**
         * Validasi email dengan domain tertentu (optional)
         */
        fun validateEmailWithDomain(
            textInputLayout: TextInputLayout,
            allowedDomains: List<String> = emptyList(),
            errorMessage: String = "Domain email tidak diizinkan"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            // Validasi format email dulu
            if (!validateEmailFormat(textInputLayout)) {
                return false
            }

            // Jika ada domain restrictions, validasi domain
            if (allowedDomains.isNotEmpty() && !text.isNullOrEmpty()) {
                val domain = text.substringAfterLast("@")
                val isValidDomain = allowedDomains.any { it.equals(domain, ignoreCase = true) }

                return if (!isValidDomain) {
                    textInputLayout.error = errorMessage
                    false
                } else {
                    textInputLayout.error = null
                    true
                }
            }

            return true
        }


        fun validatePassword(
            textInputLayout: TextInputLayout,
            errorMessage: String = "Password minimal 8 karakter, mengandung huruf kapital, angka, dan simbol"
        ): Boolean {
            val text = textInputLayout.editText?.text?.toString()?.trim()

            // Pattern: minimal 8 karakter, mengandung minimal 1 huruf kapital, 1 angka, 1 simbol
            val pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")

            return if (text.isNullOrEmpty() || !pattern.matcher(text).matches()) {
                textInputLayout.error = errorMessage
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        /**
         * Validasi kombinasi untuk password field
         */
        fun validatePasswordField(
            textInputLayout: TextInputLayout,
            minLength: Int = 8
        ): Boolean {
            // Validasi required
            if (!validateRequired(textInputLayout)) {
                return false
            }

            // Validasi minimal karakter
            if (!validateMinLength(textInputLayout, minLength)) {
                return false
            }

            // Validasi format password (kapital, angka, simbol)
            if (!validatePassword(textInputLayout)) {
                return false
            }

            return true
        }

        /**
         * Validasi konfirmasi password harus sama dengan password
         */
        fun validatePasswordConfirmation(
            passwordLayout: TextInputLayout,
            confirmPasswordLayout: TextInputLayout,
            errorMessage: String = "Konfirmasi password tidak sesuai"
        ): Boolean {
            val password = passwordLayout.editText?.text?.toString()?.trim()
            val confirmPassword = confirmPasswordLayout.editText?.text?.toString()?.trim()

            return if (password != confirmPassword) {
                confirmPasswordLayout.error = errorMessage
                false
            } else {
                confirmPasswordLayout.error = null
                true
            }
        }

    }
}