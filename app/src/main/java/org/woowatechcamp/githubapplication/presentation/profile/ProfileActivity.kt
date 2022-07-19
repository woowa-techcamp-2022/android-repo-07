package org.woowatechcamp.githubapplication.presentation.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import java.net.URL

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

        val item = intent.getParcelableExtra<UserModel>("profile_item")
        item?.let {
            binding.user = it
            binding.tvPerson.text = "${it.followers} Followers ・ ${it.following} Following"
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    val mInputStream = URL(it.imgUrl).openStream()
                    BitmapFactory.decodeStream(mInputStream)
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        binding.ivProfile.setImageBitmap(it)
                    }
                }
            }
        }
    }
}
