package org.woowatechcamp.githubapplication.presentation.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.showSnackBar
import java.net.URL

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: org.woowatechcamp.githubapplication.databinding.ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setToolbar()

        binding.toolbarProfile.setNavigationOnClickListener {
            finish()
        }

        val item = intent.getParcelableExtra<UserModel>("profile_item")
        item?.let {
            binding.user = it
//            binding.tvProfileFollow.text = "${it.followers} Followers ・ ${it.following} Following"
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    val mInputStream = URL(it.imgUrl).openStream()
                    BitmapFactory.decodeStream(mInputStream)
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        binding.ivProfile.setImageBitmap(it)
                    }
                }.onFailure {
                    showSnackBar(binding.root, "프로필을 불러오는 데 실패했습니다.", this@ProfileActivity)
                }
            }
        }
    }

    private fun setToolbar() {
//        setSupportActionBar(binding.toolbarProfile)
//        val actionBar = supportActionBar
//        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
