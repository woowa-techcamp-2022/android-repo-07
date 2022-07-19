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
import org.woowatechcamp.githubapplication.util.ext.startAction
import org.woowatechcamp.githubapplication.util.onError
import org.woowatechcamp.githubapplication.util.onSuccess
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
        with("${BuildConfig.GITHUB_AUTH}?client_id=${BuildConfig.CLIENT_ID}&scope=$scope") {
            startAction(Pair(Intent.ACTION_VIEW, toUri()))
        }
    }

    private fun observeData() {
        viewModel.signInState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                with(state) {
                    onSuccess {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    onError {
                        showSnackBar(binding.root, it, this@SignInActivity)
                    }
                }
            }.launchIn(lifecycleScope)
    }
}