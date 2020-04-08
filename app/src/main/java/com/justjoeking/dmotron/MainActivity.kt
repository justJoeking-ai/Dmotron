package com.justjoeking.dmotron

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.justjoeking.dmotron.MonsterUtil.Companion.DRAGON
import com.justjoeking.dmotron.model.Monster
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    // remove as read:
    // https://www.reddit.com/r/StrangePlanet/
    // https://www.stilldrinking.org/programming-sucks

    var rollhistory = ArrayList<Int>(0)
    var clickcount = 0


//    https://play.google.com/apps/publish/?account=9031693838262703476#AdminPlace


    val retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    enum class status {
        // just for science
        EASY,
        NORMAL,
        HARD
//    }
//    fun test():String {
//        return "\n\n\n\nHello World"
//
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        main_fab.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                this,
                R.color.Btncolor


            )
        )

        setupMonsterList()
        setupRightFabClick()
//        setupTest()

        // To Monster Listing
        toListedMonsters.setOnClickListener {
            val intent = Intent(this, AllMonsterActivity::class.java)
            // start your next activity
            startActivity(intent)
        }

        //Monster Card
        toAttribute.setOnClickListener {
            val intent = Intent(this, MonsterDetailActivity::class.java)

            // start your next activity
            startActivity(intent)
        }

    }


    private fun setupMonsterList() {
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
                }
            })
    }

    private fun setupRightFabClick() {
        main_fab.setOnClickListener { view ->


            val encounterCRInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            encounterCRInput.layoutParams = lp
            encounterCRInput.inputType = InputType.TYPE_CLASS_NUMBER

            // TODO: spinner for EL instead of text?
            val sharedPref = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE)
            sharedPref.getInt("Party CR", 5)
            encounterCRInput.setText(sharedPref.getInt("Party CR", 5).toString())
            encounterCRInput.hint = getString(R.string.party_level_hint)

            var builder = AlertDialog.Builder(this)
            builder.setView(encounterCRInput)

            builder.setTitle("Create an Encounter! $DRAGON")
            builder.setPositiveButton(getString(R.string.let_roll)) { dialog, which ->
                // TODO: Match names to not create duplicates
                if (encounterCRInput.text.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "What are you a NPC?", Toast.LENGTH_LONG
                    ).show()
                    return@setPositiveButton
                } else if (Integer.parseInt(encounterCRInput.text.toString()) == 0) {
                    Toast.makeText(
                        applicationContext,
                        "What are you a commoner", Toast.LENGTH_LONG
                    ).show()
                    return@setPositiveButton
                }

                clickcount = clickcount + 1
                if (clickcount == 1) {
                } else {
                }

                // Store Party CR
                val sharedPrefEdit = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE).edit()
                sharedPrefEdit.putInt(
                    "Party CR",
                    Integer.parseInt(encounterCRInput.text.toString())
                )
                sharedPrefEdit.apply()


                // Fetch monsters
                retrofit.create(DNDService::class.java).listMonsters()
                    .enqueue(object : Callback<MonsterResponse> {
                        override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                            Log.v("retrofit", "call failed")
                        }

                        override fun onResponse(
                            call: Call<MonsterResponse>?,
                            response: Response<MonsterResponse>?


                        ) {
                            var allMonsters = response!!.body()!!.results
                            Log.v("Monster", allMonsters.get(0).name)
                            Log.v("Monster", allMonsters.get(1).name)
                            Log.v("Monster", allMonsters.get(2).name)
                            Log.v("Monster", allMonsters.get(3).name)

                            // do this until we get a monster whose CR is not above the party level
                            fetchIndividualMonster(
                                encounterCRInput.text.toString().toInt(),
                                allMonsters,
                                view

                            )
                            val sharedPrefMonster =
                                getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE).edit()
                            sharedPrefMonster.putString(
                                "Saved Monster",
                                sharedPrefMonster.toString()
                            )
                            sharedPrefMonster.apply()
                        }

                    })
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

    private fun fetchIndividualMonster(
        encounterCR: Int,
        allMonsters: java.util.ArrayList<MonsterListing>,
        view: View
    ) {
        // get a random monster
        val randomMonster = allMonsters.get(RandomUtils.randInt(0, allMonsters.size - 1))
        val monsterIndex = randomMonster.index
        Log.v("retrofit", monsterIndex)

        // Fetch individual monster
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
                    var encountercount = 1
                    if (response?.body() == null) {
                        // @todo: throw error
                        return
                    } else {
                        val monster = response.body()

                        if (monster!!.challenge_rating == 0f) {
                            monster.challenge_rating = 1f
                        }

                        if (monster.challenge_rating > encounterCR) {
                            // we need to check again
                            return fetchIndividualMonster(
                                encounterCR,
                                allMonsters,
                                view
                            )

                        }
                        if (!monster.isTerrestrial()) {

                            return fetchIndividualMonster(
                                encounterCR,
                                allMonsters,
                                view
                            )
                        }

                        Log.d("Chosen Monster", monster.name)
                        var currentMonster = randomMonster.name
                        val numberOfMonsters = (encounterCR / monster.challenge_rating)
                        val i = randomMonster.name.toString()
                        val snackbarText = String.format(
                            "\n\nEncounter for Party Level " + encounterCR + ":\n" + numberOfMonsters.toInt() + " " + randomMonster.name + "s \n" + "Size = " + response.body()!!.size + "\n" + "AC = " + monster.armor_class.toInt() + "\n" + "HP = " + monster.hit_points.toInt() + "\n" + "You have made " + clickcount + " encounters"


                        )





                        Snackbar.make(
                            view,
                            snackbarText, Snackbar.LENGTH_LONG
                        ).show()


                        val experience = getEncounterXP(numberOfMonsters.toLong() * 2)
                        if (experience < 0) {
                            centertext.text =
                                "\n ${centertext.text}${"$snackbarText (${getString(R.string.MAX)}) \n"}"
                        } else {
                            centertext.text =
                                "${centertext.text}${"$snackbarText ($experience) \n"}"
                        }

                        Snackbar.make(
                            view,
                            snackbarText, Snackbar.LENGTH_LONG
                        ).show()
                    }


                }
            })
    }


    private fun getEncounterXP(cr: Long): Int {

        when (cr) {
            .125.toLong() -> return MonsterUtil().crXPLookup().get(0)
            .25.toLong() -> return MonsterUtil().crXPLookup().get(1)
            .5.toLong() -> return MonsterUtil().crXPLookup().get(2)
            1.toLong() -> return MonsterUtil().crXPLookup().get(3)
            2.toLong() -> return MonsterUtil().crXPLookup().get(4)
            else ->
                if (cr + 2 > MonsterUtil().crXPLookup().size) {
                    return -1
                } else {
                    return MonsterUtil().crXPLookup().get((cr + 2).toInt())
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
