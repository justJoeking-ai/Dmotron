package com.justjoeking.dmotron

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.justjoeking.dmotron.network.HttpClient
import kotlinx.android.synthetic.main.activity_all_monster.*
import kotlinx.android.synthetic.main.activity_monster_detail.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

private lateinit var recyclerView: RecyclerView
private lateinit var recyclerViewAdapter: MonsterAdapter
private lateinit var recyclerViewManager: RecyclerView.LayoutManager

class AllMonsterActivity : AppCompatActivity() {

    var monsterDataList: ArrayList<MonsterListing> = ArrayList()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(HttpClient.client)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_monster)
        setSupportActionBar(toolbar)

        recyclerViewManager = LinearLayoutManager(this)
        recyclerViewAdapter = MonsterAdapter()

        recyclerView = monster_list.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = recyclerViewManager

            // specify an viewAdapter (see also next example)
            adapter = recyclerViewAdapter
        }
        setupMonsterList()
    }

    private fun setupMonsterList() {
        retrofit.create(DNDService::class.java).listMonsters()
            .enqueue(object : Callback<MonsterResponse> {
                override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                    Timber.v("setupMonsterList call failed")
                }

                override fun onResponse(
                    call: Call<MonsterResponse>?,
                    response: Response<MonsterResponse>?
                ) {
                    Timber.d(response.toString())
                    monsterDataList = response?.body()?.results ?: ArrayList()
                    recyclerViewAdapter.monsterList = monsterDataList
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            })
    }
}
