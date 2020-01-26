package com.jseixas.randomUser.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jseixas.randomUser.R
import com.jseixas.randomUser.model.RandomUsersResults
import com.jseixas.randomUser.model.Results
import com.jseixas.randomUser.network.ApiError
import com.jseixas.randomUser.network.ApiHelpers
import com.jseixas.randomUser.network.ApiRequestCallback
import com.jseixas.randomUser.recyclerview.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //Declare var
    lateinit var apiHelpersInstance: ApiHelpers
    lateinit var userAdapter: UserAdapter
    lateinit var userList : MutableList<Results>
    var isFetching = false  // refreshPull status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get swipe container
        val swipeContainer = findViewById(R.id.swipeContainer) as SwipeRefreshLayout

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener { fetchData() }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )


        //Init empty userList
        userList = mutableListOf()

        //init userAdapter
        userAdapter = UserAdapter(userList)

        //Init recyclerView
        val recyclerView = recycleview_main

        // Setup layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Give his adapter
        recyclerView.adapter = userAdapter

        // Give context to apiHelper
        apiHelpersInstance = ApiHelpers(this)

        // Call apiHelper to do the request
        swipeContainer.setRefreshing(true)
        fetchData()
    }

    fun fetchData() {
        userList.clear()
        callApi()
        isFetching = true
    }

    fun callApi() {
        apiHelpersInstance.apiRequestUsersByCount(30,
            // Callback
            object : ApiRequestCallback<RandomUsersResults>() {
                // Override super onSuccess method to apply changes to UI when get data
                override fun onSuccess(result: RandomUsersResults) {
                    super.onSuccess(result)

                    // Disable refreshPull container
                    if (isFetching) swipeContainer.setRefreshing(false)

                    runOnUiThread {
                        userList.addAll(result.users) // Add data to adapter list
                        userAdapter.notifyDataSetChanged() // refresh the adapter => RecyclerView
                    }
                }

                // Override super onError method to apply changes to UI when get data
                override fun onError(error: ApiError?) {
                    super.onError(error)

                    // Disable refreshPull container
                    if (isFetching) swipeContainer.setRefreshing(false)

                    val show_toast = Runnable {
                        Toast.makeText(this@MainActivity, "An error as occured on the API request", Toast.LENGTH_SHORT)
                            .show()
                    }

                    runOnUiThread(show_toast)
                    runOnUiThread {
                        Log.e(
                            MainActivity::class.java.canonicalName,
                            "An error as occured on the API request : " + error
                        )
                    }
                }
            })
    }
}
