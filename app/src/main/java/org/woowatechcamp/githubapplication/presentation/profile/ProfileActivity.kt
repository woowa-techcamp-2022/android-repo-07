package org.woowatechcamp.githubapplication.presentation.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setToolbar()

//        val item = intent.getParcelableExtra<UserResponse>("profile_item")
//        item?.let {
//            binding.user = it
//            binding.tvProfileFollow.text = "${it.followers} Followers ・ ${it.following} Following"
//            val url = it.avatar_url
//            CoroutineScope(Dispatchers.IO).launch {
//                kotlin.runCatching {
//                    val mInputStream = URL(it.avatar_url).openStream()
//                    BitmapFactory.decodeStream(mInputStream)
//                }.onSuccess {
//                    withContext(Dispatchers.Main) {
//                        binding.ivProfileImg.setImageBitmap(it)
//                    }
//                }.onFailure {
//                    showSnackBar(binding.root, "프로필을 불러오는 데 실패했습니다.", this@ProfileActivity)
//                }
//            }
//        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbarProfile)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
