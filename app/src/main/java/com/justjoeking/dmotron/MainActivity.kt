package com.justjoeking.dmotron

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.justjoeking.dmotron.model.Monster
import com.justjoeking.dmotron.network.HttpClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    val retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(HttpClient.client)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())
        Timber.d("Start!")
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        main_fab.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                this,
                R.color.colorButton
            )
        )
        val bundle = Bundle()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        setupMonsterList()
        setupFabClick()
    }

    private fun setupMonsterList() {
        retrofit.create(DNDService::class.java).listMonsters()
            .enqueue(object : Callback<MonsterResponse> {
                override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                    Timber.e("call failed")
                }

                override fun onResponse(
                    call: Call<MonsterResponse>?,
                    response: Response<MonsterResponse>?
                ) {
                    Timber.d("AllMonsters:")
                    Timber.d(response.toString())
                }
            })
    }

    private fun setupFabClick() {
        main_fab.setOnClickListener { view ->
            val encounterCRInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            encounterCRInput.layoutParams = lp
            encounterCRInput.inputType = InputType.TYPE_CLASS_NUMBER

            val sharedPref = getSharedPreferences("DM-o-Tron", Context.MODE_PRIVATE)
            sharedPref.getInt("Party CR", 5)
            encounterCRInput.setText(sharedPref.getInt("Party CR", 5).toString())
            encounterCRInput.hint = getString(R.string.party_level_hint)

            val builder = AlertDialog.Builder(this)
            builder.setView(encounterCRInput)

            builder.setTitle(getString(R.string.create_an_encounter))
            val positiveButton = builder.setPositiveButton(getString(R.string.let_roll),
                fun(_: DialogInterface, _: Int) {
                    if (encounterCRInput.text.toString().isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter a CR level (likely 1-30)", Toast.LENGTH_LONG
                        ).show()
                        return
                    } else if (Integer.parseInt(encounterCRInput.text.toString()) == 0) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter a valid CR level (likely 1-30)", Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    // Store Party CR
                    val sharedPrefEdit =
                        getSharedPreferences("DM-o-Tron", Context.MODE_PRIVATE).edit()
                    sharedPrefEdit.putInt(
                        "Party CR",
                        Integer.parseInt(encounterCRInput.text.toString())
                    )
                    sharedPrefEdit.apply()

                    // Fetch monsters
                    retrofit.create(DNDService::class.java).listMonsters()
                        .enqueue(object : Callback<MonsterResponse> {
                            override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                                Timber.e("call failed")
                            }

                            override fun onResponse(
                                call: Call<MonsterResponse>?,
                                response: Response<MonsterResponse>?
                            ) {
                                val allMonsters = response!!.body()!!.results
                                Timber.v(allMonsters[0].name)
                                Timber.v(allMonsters[1].name)
                                Timber.v(allMonsters[2].name)
                                Timber.v(allMonsters[3].name)

                                // do this until we get a monster whose CR is not above the party level
                                fetchIndividualMonster(
                                    encounterCRInput.text.toString().toInt(),
                                    allMonsters,
                                    view
                                )

                                val sharedPrefMonster =
                                    getSharedPreferences("DM-o-Tron", Context.MODE_PRIVATE).edit()
                                sharedPrefMonster.putString(
                                    "Saved Monster",
                                    sharedPrefMonster.toString()
                                )
                                sharedPrefMonster.apply()
                            }

                        })
                })

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

        // Fetch individual monster
        retrofit.create(DNDService::class.java).getMonster(monsterIndex)
            .enqueue(object : Callback<Monster> {
                override fun onFailure(
                    call: Call<Monster>?,
                    t: Throwable?
                ) {
                    Timber.v("call failed")
                    Timber.e(t)
                }

                override fun onResponse(
                    call: Call<Monster>?,
                    response: Response<Monster>?
                ) {
                    if (response?.body() != null) {
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

                        Timber.d(getString(R.string.chosen_monster), monster.name)
                        val numberOfMonsters = (encounterCR / monster.challenge_rating)
                        val i = randomMonster.name
                        val snackBarText = String.format(
                            "\n\nEncounter for Party Level " + encounterCR + ":\n" + numberOfMonsters.toInt()
                                    + " " + randomMonster.name + "s \nSize = " + response.body()!!.size
                                    + "\nAC = " + monster.armor_class.toInt() + "\nHP = " + monster.hit_points.toInt()
                        )

                        Snackbar.make(
                            view,
                            snackBarText, Snackbar.LENGTH_LONG
                        ).show()

//                        val experience = getEncounterXP(numberOfMonsters.toLong() * 2)

                        Snackbar.make(
                            view,
                            snackBarText, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getEncounterXP(cr: Long): Int {

        return when (cr) {
            .125.toLong() -> MonsterUtils().crXPLookup()[0]
            .25.toLong() -> MonsterUtils().crXPLookup()[1]
            .5.toLong() -> MonsterUtils().crXPLookup()[2]
            1.toLong() -> MonsterUtils().crXPLookup()[3]
            2.toLong() -> MonsterUtils().crXPLookup()[4]
            else -> {
                if (cr + 2 > MonsterUtils().crXPLookup().size) {
                    -1
                } else {
                    MonsterUtils().crXPLookup()[(cr + 2).toInt()]
                }
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
            R.id.search -> {
                // @todo refactor to search provider (https://developer.android.com/training/search/setup) instead
                val intent = Intent(this, AllMonsterActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
