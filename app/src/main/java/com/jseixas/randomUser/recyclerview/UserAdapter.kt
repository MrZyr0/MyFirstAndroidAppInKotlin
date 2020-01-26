package com.jseixas.randomUser.recyclerview;

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jseixas.randomUser.R
import com.jseixas.randomUser.activity.MainActivity
import com.jseixas.randomUser.activity.ProfilActivity
import com.jseixas.randomUser.model.Results
import com.jseixas.randomUser.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter(private val myDataset: MutableList<Results>)  : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var gson = Gson()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Setup element to update on itemView by ID
        val _firstName = itemView.item_user_firstName
        val _lastName = itemView.item_user_lastName
        val _username = itemView.item_user_username
        val _profilPicture = itemView.profilePicture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder( itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_user, parent, false) // Change the name of item layout
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder._firstName.text = myDataset[position].name.first // Setup data on elements
        holder._lastName.text = myDataset[position].name.last // Setup data on elements
        holder._username.text = myDataset[position].login.username // Setup data on elements
        Picasso.get()
            .load(myDataset[position].picture.thumbnail)
            .placeholder(R.drawable.ic_mood_black_24dp)
            .transform(CircleTransform())
            .into(holder._profilPicture)
        holder.itemView.setOnClickListener{
            Log.d("CLICK", "items clicked")
            val intent = Intent(holder.itemView.context,ProfilActivity::class.java)
            var dataJsonStringified = gson.toJson(myDataset[position])
            intent.putExtra("UserData", dataJsonStringified)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = myDataset.size
}
