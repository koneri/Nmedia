package com.example.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nmedia.R
import com.example.nmedia.activities.MainActivity
import com.example.nmedia.databinding.CardPostBinding
import com.example.nmedia.dto.Post

interface PostEventListener{
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onPlay(post: Post)
}
//typealias  OnLikeListener = (post: Post) -> Unit
//typealias  OnShareListener = (post: Post) -> Unit
//typealias   OnRemoveListener = (post: Post) -> Unit

class PostAdapter(
    private val listener: PostEventListener,

) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostEventListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            videoGroup.isVisible = !post.videoLink.isNullOrBlank()
            author.text = post.author
            if (!post.content.isNullOrEmpty()) content.text = post.content else content.text = ""
            published.text = post.published
            like.text = countFormat(post.countOfLikes)
            share.text = countFormat(post.countOfShares)

            like.isChecked = post.likedByMe
            like.setOnClickListener {
                listener.onLike(post)
            }

            share.setOnClickListener {
                listener.onShare(post)
            }

            playBtn.setOnClickListener{
               listener.onPlay(post)
            }

            menu.setOnClickListener { it ->
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)

                    setOnMenuItemClickListener {menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                        }

                        false
                    }
                    show()
                }
            }
        }
    }
}

//?????????????? ?????? ?????????????????????? 1000 ?? 1?? ?? ????:
fun countFormat(counter: Long): String {
    val char = counter.toString().toCharArray()
    val firstSymbol = char[0]
    var secondSymbol: Char = '_'
    var thirdSymbol: Char = '_'
    if (counter > 9) secondSymbol = char[1] //?????????? ???? ???????????????? ??????????????, ???????? ???????????????? ???????????? 2??
    if (counter > 99) thirdSymbol = char[2] //?????????? ???? ???????????????? ??????????????, ???????? ???????????????? ???????????? 3??

    return when (counter) {
        in 0..999 -> counter.toString()
        in 1000..9999 -> {
            //???????? 2?? ?????????? = 0, ???? ???????????? ?????????????????? ?? - 1??:
            if (secondSymbol == '0') "${firstSymbol}K" else {
                // ?? ?????????????????? ??????????????, ?????????????????? ?????????????? - 1.1??:
                "${firstSymbol}.${secondSymbol}K"
            }
        }
        //?????? ???????????????? ?????????? - ?????? ?????????? ?? ?? - 10??:
        in 10_000..99_999 -> "${firstSymbol}${secondSymbol}K"
        //?????? ?????????? - ?????? ?????????? ?? ??:
        in 100_000..999_999 -> "${firstSymbol}${secondSymbol}${thirdSymbol}K"
        //?????? ???????????????? ???? ????????????????:
        in 1000_000..9999_999 -> {
            if (secondSymbol == '0') "${firstSymbol}M"
            else {
                "${firstSymbol}.${secondSymbol}M"
            }
        }
        in 10_000_000..99_999_999 -> "${firstSymbol}${secondSymbol}M"
        in 100_000_000..999_999_999 -> "${firstSymbol}${secondSymbol}${thirdSymbol}M"
        else -> "infinity"
    }
}


