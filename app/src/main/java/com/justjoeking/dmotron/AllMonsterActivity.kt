package com.justjoeking.dmotron

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_all_monster.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var recyclerView: RecyclerView
private lateinit var viewAdapter: RecyclerView.Adapter<*>
private lateinit var viewManager: RecyclerView.LayoutManager

class AllMonsterActivity : AppCompatActivity() {

    private var monsterDataset: ArrayList<MonsterListing> = ArrayList()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_monster)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MonsterAdapter(monsterDataset)

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

    fun setupMonsterList() {
        retrofit.create(DNDService::class.java).listMonsters()
            .enqueue(object : Callback<MonsterResponse> {
                override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                }

                override fun onResponse(
                    call: Call<MonsterResponse>?,
                    response: Response<MonsterResponse>?
                ) {
                    Log.d("AllMonsters", response.toString())
                    monsterDataset = response?.body()?.results ?: ArrayList()
                    viewAdapter.my =
                    viewAdapter.notifyDataSetChanged()
                }
            })
    }
}
