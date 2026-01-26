package co.id.rezha.mycrud.views.payungin.helpers

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import co.id.rezha.mycrud.R

class CustomDialogBuilder(private val context: Context) {

    private var title: String? = null
    private var message: String? = null
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var positiveButtonListener: (() -> Unit)? = null
    private var negativeButtonListener: (() -> Unit)? = null
    private var cancelable: Boolean = true

    fun build(): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(cancelable)

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.custom_dialog_layout, null)
        dialog.setContentView(view)

        // Set window background transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupViews(dialog)

        return dialog
    }

    fun show(): Dialog {
        val dialog = build()
        dialog.show()
        return dialog
    }

    private fun setupViews(dialog: Dialog) {
        val tvTitle = dialog.findViewById<TextView>(R.id.tvDialogTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
        val btnPositive = dialog.findViewById<Button>(R.id.btnPositive)
        val btnNegative = dialog.findViewById<Button>(R.id.btnNegative)

        // Set title
        if (title != null) {
            tvTitle.text = title
            tvTitle.isVisible = true
        } else {
            tvTitle.isVisible = false
        }

        // Set message
        tvMessage.text = message ?: ""

        // Setup positive button
        if (positiveButtonText != null) {
            btnPositive.text = positiveButtonText
            btnPositive.setOnClickListener {
                positiveButtonListener?.invoke()
                dialog.dismiss()
            }
            btnPositive.isVisible = true
        } else {
            btnPositive.isVisible = false
        }

        // Setup negative button
        if (negativeButtonText != null) {
            btnNegative.text = negativeButtonText
            btnNegative.setOnClickListener {
                negativeButtonListener?.invoke()
                dialog.dismiss()
            }
            btnNegative.isVisible = true
        } else {
            btnNegative.isVisible = false
        }

        // Jika hanya ada satu button, atur layout
        if (positiveButtonText != null && negativeButtonText == null) {
            btnPositive.layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT
        } else if (negativeButtonText != null && positiveButtonText == null) {
            btnNegative.layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    fun setTitle(title: String): CustomDialogBuilder {
        this.title = title
        return this
    }

    fun setMessage(message: String): CustomDialogBuilder {
        this.message = message
        return this
    }

    fun setPositiveButton(text: String, listener: (() -> Unit)? = null): CustomDialogBuilder {
        this.positiveButtonText = text
        this.positiveButtonListener = listener
        return this
    }

    fun setNegativeButton(text: String, listener: (() -> Unit)? = null): CustomDialogBuilder {
        this.negativeButtonText = text
        this.negativeButtonListener = listener
        return this
    }

    fun setCancelable(cancelable: Boolean): CustomDialogBuilder {
        this.cancelable = cancelable
        return this
    }
}