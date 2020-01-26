package com.jseixas.randomUser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.gson.Gson
import com.jseixas.randomUser.R
import com.jseixas.randomUser.model.Results
import com.jseixas.randomUser.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.activity_profil.view.*


class ProfilActivity : AppCompatActivity() {

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // Get data from RecyclerView
        val userData = gson.fromJson(intent.getStringExtra("UserData"), Results::class.java)


        // Setup informations
        Picasso.get()
            .load(userData.picture.thumbnail)
            .placeholder(R.drawable.ic_mood_black_24dp)
            .transform(CircleTransform())
            .into(findViewById<AppCompatImageView>(R.id.profilePicture))

        findViewById<AppCompatTextView>(R.id.firstName).setText(userData.name.first)
        findViewById<AppCompatTextView>(R.id.lastName).setText(userData.name.last)
        findViewById<AppCompatTextView>(R.id.username).setText(userData.login.username)
        findViewById<AppCompatTextView>(R.id.email).setText(userData.email)
        findViewById<AppCompatTextView>(R.id.phone).setText(userData.phone)

        findViewById<AppCompatImageButton>(R.id.btn_sendEmail).setOnClickListener{
            sendEmail(userData.email)
        }
        findViewById<AppCompatImageButton>(R.id.btn_call).setOnClickListener{
            call(userData.phone)
            Toast.makeText(this, "Warning this number is only for testing ! \n Do not call this number", Toast.LENGTH_LONG).show()
        }
        findViewById<AppCompatImageButton>(R.id.btn_navigation).setOnClickListener{
            navigate(userData.location.coordinates.latitude, userData.location.coordinates.longitude)
        }
    }


    private fun sendEmail(email: String) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", email, null)
        )


        try {
            startActivity(Intent.createChooser(intent, "Choose Email Client..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
    private fun call(phoneNumber: String) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", phoneNumber, null)
        )


        try {
            startActivity(Intent.createChooser(intent, "Choose a dialer..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
    private fun navigate(latitude: Double, longitude: Double) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:" + latitude + "," + longitude)
        )

        try {
            startActivity(Intent.createChooser(intent, "Choose navigation app..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
