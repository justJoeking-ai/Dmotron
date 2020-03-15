package com.justjoeking.dmotron


//import kotlinx.android.synthetic.main.MonsterCardActivity.*
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_monster_card.*
import kotlinx.android.synthetic.main.content_monster_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var recyclerView: RecyclerView
private lateinit var viewAdapter: MonsterAdapter
private lateinit var viewManager: RecyclerView.LayoutManager

class MonsterCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_card)
        setSupportActionBar(toolbar)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        setupFabClick()
    }

    private fun setupFabClick() {
        floatingActionButton.setOnClickListener { view ->
            val retrofit = Retrofit.Builder()
                .baseUrl("http://www.dnd5eapi.co/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            var builder = AlertDialog.Builder(this)
            // todo preferably don't setup box, use xml to layout dialog
            // and add listener for text
            val monsterInput = EditText(this).toString()
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            val monsterIndex = monsterInput.toString()
            val sharedPref = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE)
            sharedPref.getString("Orc", String.toString())
            monsterInput.hint = getText(R.string.Find_your_monster)

            builder.setView(monsterInput)

            builder.setTitle("Search for Your Monster! ${MonsterUtil.DRAGON}")
            builder.setPositiveButton(getText(R.string.Find_your_monster)) { dialog, which ->
                // TODO: Match names to not create duplicates
                if (monsterInput.text.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "What are you a NPC?", Toast.LENGTH_LONG
                    ).show()
                    return@setPositiveButton
//                } else if (Integer.parseInt(monsterInput.text.toString()) == 0) {
//                    Toast.makeText(
//                        applicationContext,
//                        "What are you a commoner", Toast.LENGTH_LONG
//                    ).show()
//                    return@setPositiveButton
                }
                printStats(
                    monsterIndex,
                    view
                )
            }
            builder.setNegativeButton(getString(R.string.no_thanks))
            { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
    }

    // sharedPrefEdit.apply()
    fun printStats(monsterIndex: String, view: View) {


        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DNDService::class.java).getMonster(monsterIndex)
            .enqueue(object : Callback<Monster> {
                override fun onFailure(
                    call: Call<Monster>?,
                    t: Throwable?
                ) {
                    Log.v("retrofit", "call failed")
                    Log.e("Call failure", "Aw shit", t)

                }

                override fun onResponse(
                    call: Call<Monster>?,
                    response: Response<Monster>?
                ) {
                    //todo: here handle the case that the server returned a good returne a good result

                    response.body(!!).armor_class

                }

                val snackbarText = String.format(
                    "\n\nEncounter for Party Level " + monsterIndex.toString()
                )

                Snackbar.make(
                view,
                snackbarText, Snackbar.LENGTH_LONG
                ).show()

                centertext.text = "\n\n" + monsterIndex

                Snackbar.make(
                view,
                snackbarText, Snackbar.LENGTH_LONG
                ).show()


            }
    }



