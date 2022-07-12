package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.GithubApplication
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivitySignInBinding

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 성공 시 동작 - Access Token

        binding.btnSignIn.setOnClickListener {
            val scope = "user_repo+admin:org"
            val loginUrl =  "${GithubApplication.AUTH}?client_id=${BuildConfig.CLIENT_ID}&scope=$scope"
            val intent = Intent(Intent.ACTION_VIEW, loginUrl.toUri())
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

}