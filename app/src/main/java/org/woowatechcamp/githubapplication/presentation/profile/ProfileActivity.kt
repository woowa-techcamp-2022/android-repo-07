package org.woowatechcamp.githubapplication.presentation.profile

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivityProfileBinding
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.ext.startAction
import org.woowatechcamp.githubapplication.util.ext.startMail

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        with(binding) {
            toolbarProfile.setNavigationOnClickListener {
                finish()
            }

            val item = intent.getParcelableExtra<UserModel>("profile_item")
            item?.let { data ->
                user = data
                tvProfileLink.setOnClickListener {
                    startAction(Pair(Intent.ACTION_VIEW, Uri.parse(data.blog)))
                }

                tvProfileEmail.setOnClickListener {
                    startMail(
                        Pair(EXTRA_EMAIL, data.email)
                    )
                }
            }
        }
    }
}
