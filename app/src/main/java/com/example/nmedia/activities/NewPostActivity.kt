package com.example.nmedia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nmedia.R
import com.example.nmedia.databinding.ActivityNewPostBinding
import com.example.nmedia.util.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.save.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(it.context, getString(R.string.PostIsBlank), Toast.LENGTH_SHORT)
                    .show()
                setResult(RESULT_CANCELED)
            } else {
                val result = Intent().putExtra(Intent.EXTRA_TEXT, binding.content.text.toString())
                setResult(RESULT_OK, result)
            }
            finish()


        }
    }
}