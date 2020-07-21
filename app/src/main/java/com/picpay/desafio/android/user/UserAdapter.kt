package com.picpay.desafio.android.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.extensions.loadUrl
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserAdapter(
    private val users: List<User>,
    private val listen: (Long) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: User) {
            itemView.name.text = item.name
            itemView.username.text = item.username
            itemView.picture.loadUrl(item.image)
            itemView.setOnClickListener { listen.invoke(item.id) }
        }
    }
}