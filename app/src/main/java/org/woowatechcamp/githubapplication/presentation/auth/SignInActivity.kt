package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.GithubApplication
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivitySignInBinding
import org.woowatechcamp.githubapplication.presentation.MainActivity

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    private val mViewModel : SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()

        intent?.data?.let {
            val code = it.getQueryParameter("code")
            code?.let {
                mViewModel.setCode(it)
            }
        }

        binding.btnSignIn.setOnClickListener {
            val scope = "user+repo"
            val loginUrl =  "${BuildConfig.GITHUB_AUTH}?client_id=${BuildConfig.CLIENT_ID}&scope=$scope"
            val intent = Intent(Intent.ACTION_VIEW, loginUrl.toUri())
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun observeData() {
        // 성공 시 동작 - Access Token
        mViewModel.code.observe(this) {
            mViewModel.getToken(it)
        }
        // 실패시 동작 - SnackBar 메시지 띄움
        mViewModel.errorMessage.observe(this) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                .setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.white)))
                .setBackgroundTintList(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.navy)))
                .show()
        }
        lifecycleScope.launch {
            mViewModel.loginEvent.collect { getTokened ->
                if (getTokened) {
                    val intent = Intent(this@SignInActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
        }

    }

}