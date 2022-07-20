package org.woowatechcamp.githubapplication.presentation.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: org.woowatechcamp.githubapplication.databinding.ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.toolbarProfile.setNavigationOnClickListener {
            finish()
        }

        val item = intent.getParcelableExtra<UserModel>(getString(R.string.profile_item))
        item?.let {
            binding.user = it
            binding.tvPerson.text = "${it.followers} Followers ・ ${it.following} Following"
            binding.ivProfile.load(it.imgUrl)
        }
    }
}
