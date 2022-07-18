package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.databinding.ActivitySignInBinding
import org.woowatechcamp.githubapplication.presentation.home.MainActivity
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    private val mViewModel : SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()

        binding.btnSignIn.setOnClickListener {
            val scope = "user+repo"
            val loginUrl =  "${BuildConfig.GITHUB_AUTH}?client_id=${BuildConfig.CLIENT_ID}&scope=$scope"
            val intent = Intent(Intent.ACTION_VIEW, loginUrl.toUri())
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        intent?.data?.let { item ->
            val code = item.getQueryParameter("code")
            code?.let { mViewModel.setCode(it) }
        }
    }

    private fun observeData() {
        // 이 lifecycle 이 최소한 start 된 상태에서만 함. destroy 될 경우 cancel 해줌
        lifecycleScope.launchWhenStarted {
            mViewModel.accessSuccess.collect {
                if (it) {
                    val intent = Intent(this@SignInActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
        }
        mViewModel.code.observe(this) {
            mViewModel.getToken(it)
        }
        // 실패시 동작 - SnackBar 메시지 띄움
        mViewModel.errorMessage.observe(this) {
            showSnackBar(binding.root, it, this)
        }
    }
}