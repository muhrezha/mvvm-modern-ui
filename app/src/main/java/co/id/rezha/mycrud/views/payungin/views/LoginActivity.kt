package co.id.rezha.mycrud.views.payungin.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivityLoginBinding
import co.id.rezha.mycrud.views.payungin.data.local.LocalRepository
import co.id.rezha.mycrud.views.payungin.data.models.requests.AuthRequest
import co.id.rezha.mycrud.views.payungin.data.models.requests.VerifyEmailRequest
import co.id.rezha.mycrud.views.payungin.data.networks.BaseResponsePayungin
import co.id.rezha.mycrud.views.payungin.helpers.CustomDialogBuilder
import co.id.rezha.mycrud.views.payungin.helpers.ErrorsHelper
import co.id.rezha.mycrud.views.payungin.helpers.LoadingHelper
import co.id.rezha.mycrud.views.payungin.utils.hideKeyboard
import co.id.rezha.mycrud.views.payungin.utils.validateEmailField
import co.id.rezha.mycrud.views.payungin.utils.validatePassword
import co.id.rezha.mycrud.views.payungin.viewmodels.LoginViewModel
import co.id.rezha.mycrud.views.payungin.views.dashboard.DashboardPayunginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginViewModel by viewModels()
    private var navigated = false
    private lateinit var localRepository: LocalRepository

    private var isLoadingLogged = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            localRepository = LocalRepository(application)
            enableEdgeToEdge()
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            binding.btnLogin.visibility = View.GONE
            binding.linearLoading.visibility = View.VISIBLE

            checkSession();

            observeState(this)
            setupRealTimeValidation();

            binding.btnLogin.setOnClickListener({
                binding.inputEmail.editText?.inputType = android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
                binding.inputPassword.editText?.inputType = android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS

                val isEmailValid = binding.inputEmail.validateEmailField()

                when {
                    isEmailValid -> {
                        val email = binding.inputEmail.editText?.text?.toString()?.trim().orEmpty()
                        if(binding.btnLogin.text=="Berikutnya"){
                            vm.login(VerifyEmailRequest(email = email))
                        }else{
                            val isPasswordValid = binding.inputPassword.validatePassword()
                            when {
                                isPasswordValid -> {
                                    val pasword = binding.inputPassword.editText?.text?.toString()?.trim().orEmpty()
                                    val authReq = AuthRequest(
                                        email,
                                        pasword,
                                        "mobile",
                                        "password",
                                        "",
                                        Consts.FCM_TOKEN_WANDA4DES
                                        )
                                    vm.authLogin(authReq)
                                }
                            }
                        }
                    }
                }
            })


        }catch (e: Exception){
            Log.e(Consts.TAG, "${e.message?.let {"-"}}")
        }
    }

    private fun checkSession(){
        val isLogged = localRepository.isLoggedIn()
        if(isLogged){
            Toast.makeText(this@LoginActivity, "Already logged!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, DashboardPayunginActivity::class.java))
            finish()
        }else{
            binding.btnLogin.visibility = View.VISIBLE
            binding.linearLoading.visibility = View.GONE
        }
    }

    private fun observeState(loginActivity: LoginActivity) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // verifyEmailState
                launch {
                    vm.verifyEmailState.collectLatest { state ->
                        when (state) {
                            is BaseResponsePayungin.Idle -> Unit
                            is BaseResponsePayungin.Loading -> {
                                loginActivity.hideKeyboard()
                                LoadingHelper.showLoadingInActivity(loginActivity)
                            }
                            is BaseResponsePayungin.Success -> {
                                LoadingHelper.hideLoading()
                                if (!navigated) {
                                    navigated = true
                                    binding.btnLogin.text = "Login"
                                    binding.inputEmail.editText?.isEnabled = false
                                    binding.inputPassword.visibility = View.VISIBLE
                                }
                            }
                            is BaseResponsePayungin.Error -> {
                                LoadingHelper.hideLoading()
                                state.throwable?.let {
                                    ErrorsHelper(loginActivity, it, "login")
                                        .convertError(
                                            onBadRequestException = { errorResponse ->
                                                CustomDialogBuilder(loginActivity)
                                                    .setTitle("Informasi")
                                                    .setMessage(errorResponse?.message ?: "Request tidak valid")
                                                    .setPositiveButton("Tutup") {}
                                                    .show()
                                            },
                                        )
                                }
                                Log.e(Consts.TAG, state.message ?: "-")
                            }
                        }
                    }
                }
                // authState
                launch {
                    vm.authState.collectLatest { state ->
                        when (state) {
                            is BaseResponsePayungin.Idle -> Unit
                            is BaseResponsePayungin.Loading -> {
                                loginActivity.hideKeyboard()
                                LoadingHelper.showLoadingInActivity(loginActivity)
                            }
                            is BaseResponsePayungin.Success -> {
                                LoadingHelper.hideLoading()
                                Toast.makeText(loginActivity, "Login Success!", Toast.LENGTH_LONG).show();
                                Log.w(Consts.TAG, "${state.data}")
                                try {
                                    val saved = localRepository.saveAuthData(state.data)
                                    if (saved) {
                                        Log.d("LOGIN", "Auth data saved successfully")
                                        Toast.makeText(this@LoginActivity, "Already logged!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, DashboardPayunginActivity::class.java))
                                        finish()
                                    } else {
                                        Log.e("LOGIN", "Failed to save auth data")
                                        Toast.makeText(this@LoginActivity, "Failed to save login data", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("LOGIN", "Error saving auth data: ${e.message}", e)
                                    Toast.makeText(this@LoginActivity, "Error saving login data", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is BaseResponsePayungin.Error -> {
                                LoadingHelper.hideLoading()
                                state.throwable?.let {
                                    ErrorsHelper(loginActivity, it, "login")
                                        .convertError(
                                            onBadRequestException = { errorResponse ->
                                                CustomDialogBuilder(loginActivity)
                                                    .setTitle("Informasi")
                                                    .setMessage(errorResponse?.message ?: "Request tidak valid")
                                                    .setPositiveButton("Tutup") {}
                                                    .show()
                                            },
                                        )
                                }
                                Log.e(Consts.TAG, state.message ?: "-")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRealTimeValidation() {
        binding.inputEmail.editText?.addTextChangedListener {
            if (binding.inputEmail.error != null) {
                binding.inputEmail.error = null
            }
        }

        binding.inputPassword.editText?.addTextChangedListener {
            if (binding.inputPassword.error != null) {
                binding.inputPassword.error = null
            }
        }
    }
}