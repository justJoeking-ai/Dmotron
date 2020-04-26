package com.justjoeking.dmotron

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justjoeking.dmotron.network.HttpClient
import kotlinx.android.synthetic.main.activity_monster_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private lateinit var recyclerView: RecyclerView
private lateinit var viewAdapter: MonsterAdapter
private lateinit var viewManager: RecyclerView.LayoutManager

class AllMonsterActivity : AppCompatActivity() {

    var monsterDataList: ArrayList<MonsterListing> = ArrayList()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(HttpClient.client)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_all_monster)
        setSupportActionBar(toolbar)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MonsterAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.monster_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        setupMonsterList()
    }

    private fun setupMonsterList() {
        val myArg = object : Callback<MonsterResponse> {
            override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }

            override fun onResponse(
                call: Call<MonsterResponse>?,
                response: Response<MonsterResponse>?
            ) {
                Log.d("AllMonsters", response.toString())
                monsterDataList = response?.body()?.results ?: ArrayList()
                viewAdapter.monsterList = monsterDataList
                viewAdapter.notifyDataSetChanged()
            }
        }
        retrofit.create(DNDService::class.java).listMonsters()
            .enqueue(myArg)
    }
}
