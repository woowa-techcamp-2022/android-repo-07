package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivitySignInBinding
import org.woowatechcamp.githubapplication.presentation.home.MainActivity
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        observeData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { item ->
            val code = item.getQueryParameter(getString(R.string.code))
            code?.let { viewModel.getToken(it) }
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.activity = this
        binding.lifecycleOwner = this
    }

    fun getCode() {
        val scope = "user+repo"
        val uri = "${BuildConfig.GITHUB_AUTH}?client_id=${BuildConfig.CLIENT_ID}&scope=$scope"
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        startActivity(intent)
    }

    private fun observeData() {
        viewModel.signInState.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is UiState.Error -> {
                        showSnackBar(binding.root, it.msg, this@SignInActivity)
                    }
                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }
}