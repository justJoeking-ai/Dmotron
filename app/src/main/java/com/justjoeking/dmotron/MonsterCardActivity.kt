package com.justjoeking.dmotron


//import android.os.Bundle
//import android.os.Message
//import android.util.Log
//import com.google.android.material.snackbar.Snackbar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_all_Monster.*
//import kotlinx.android.synthetic.main.activity_Monster_card.*
//import kotlinx.android.synthetic.main.Monster_layout.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.core.app.ComponentActivity.ExtraData
//import androidx.core.content.ContextCompat.getSystemService
//import android.icu.lang.UCharacter.GraphemeClusterBreak.T
//import kotlinx.android.synthetic.main.MonsterCardActivity.*
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.justjoeking.dmotron.model.Monster
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_monster_card.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MonsterCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_card)
        setSupportActionBar(toolbar)
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://www.dnd5eapi.co/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val monsterInput = findViewById<TextView>(android.R.id.content)
////        val lp = LinearLayout.LayoutParams(
////            LinearLayout.LayoutParams.MATCH_PARENT,
////            LinearLayout.LayoutParams.MATCH_PARENT
////        )
////        monsterInput.layoutParams = lp
//        val `in` = intent
//        val monsterIndex = `in`.extras
//        // check null
//        // check null
//        if (monsterIndex == null) {
//            val content: String = "orc"
//            printStats(monsterIndex.toString(), monsterInput)
//        } else {
//            val content: String = "orc"
//            printStats(monsterIndex.toString(), monsterInput)
//
//        }
        setupFabClick()

    }


    private fun setupFabClick() {
        floatingActionButton.setOnClickListener { view ->
            val retrofit = Retrofit.Builder()
                .baseUrl("http://www.dnd5eapi.co/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val monsterInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            monsterInput.layoutParams = lp
            monsterInput.inputType = InputType.TYPE_CLASS_TEXT
            val monsterIndex = monsterInput.text
            val sharedPref = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE)
            sharedPref.getString("Orc", String.toString())
            monsterInput.hint = getText(R.string.Find_your_monster)

            var builder = AlertDialog.Builder(this)
            builder.setView(monsterInput)

            builder.setTitle("Search for Your Monster! ${MonsterUtil.DRAGON}")
            builder.setPositiveButton(getText(R.string.Find_your_monster)) { dialog, which ->
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
                    monsterIndex.toString(),
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
    fun fetchMonster(monsterIndex: String) {


    }

    private fun printStats(monsterIndex: String, view: View) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val monsterId = monsterIndex.replace("\\s".toRegex(), "-").toLowerCase()

        retrofit.create(DNDService::class.java).getMonster(monsterId)
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
                    val monster: Monster? = response?.body()



                    val snackbarText = String.format(
                        "\n\nEncounter for Party Level " + "\n" + monsterIndex.toString()
                    )


                    Snackbar.make(
                        view,
                        snackbarText, Snackbar.LENGTH_LONG
                    ).show()

                    centertext.text =
                        "\n\n" + monster?.name +
                                "\n" + "Hit Points: " + monster?.hit_points.toString() +
                                "\n" + "AC: " + monster?.armor_class?.toLong() +
//                              "\n"+ " id: " +  monster?.
//                              "\n"+ " index: " +  monster?.
                                "\n" + " Size: " + monster?.size +
                                "\n" + " Type: " + monster?.type +
                                "\n" + " Challenge_rating: " + monster?.challenge_rating +
                                "\n" + " Subtype: " + monster?.subtype +
                                "\n" + " Alignment: " + monster?.alignment +
                                "\n" + " Armor_class: " + monster?.armor_class +
                                "\n" + " Hit_points: " + monster?.hit_points +
                                "\n" + " Hit_dice: " + monster?.hit_dice +
                                "\n" + " Speed:" +
                                "\n" + "Walk: " + monster?.speed?.walk +
//                                "\n" + "Fly: " + monster?.speed?.fly +
//                                "\n" + "Swim: " + monster?.speed?.swim +
//                                "\n" + "Burrow: " + monster?.speed?.burrow

                                "\n" + " Strength: " + monster?.strength +
                                "\n" + " Dexterity: " + monster?.dexterity +
                                "\n" + " Constitution: " + monster?.constitution +
                                "\n" + " Intelligence: " + monster?.intelligence +
                                "\n" + " Wisdom: " + monster?.wisdom
//                    "\n" + " Wisdom: " + monster?.proficiencies
//                    "\n"+ " Charisma: " +  monster?.charisma + monster?.proficiencies?.name
//                    "\n"+ " Dexterity_save: " +  monster?.dexterity_save+
//                    "\n"+ " Constitution_save: " +  monster?.constitution_save+
//                    "\n"+ " Wisdom_save: " +  monster?.wisdom_save+
//                    "\n"+ " Charisma_save: " +  monster?.charisma_save+
//                    "\n"+ " Perception: " +  monster?.perception+
//                    "\n"+ " Stealth: " +  monster?.stealth+
//                    "\n"+ " Damage_vulnerabilities: " +  monster?.damage_vulnerabilities+
//                    "\n"+ " Damage_resistances: " +  monster?.damage_resistances+
//                    "\n"+ " Damage_immunities: " +  monster?.damage_immunities+
//                    "\n"+ " Condition_immunities: " +  monster?.condition_immunities


                    Snackbar.make(
                        view,
                        snackbarText, Snackbar.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun printStat(monsterIndex: String, view: View) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

//        val monsterId = monsterIndex.replace("\\s".toRegex(), "-").toLowerCase()

//        retrofit.create(DNDService::class.java).getMonster(monsterId)
//                        .enqueue(object : Callback<Monster> {
//                            override fun onFailure(
//                                call: Call<Monster>?,
//                                t: Throwable?
//                            ) {
//                                Log.v("retrofit", "call failed")
//                                Log.e("Call failure", "Aw shit", t)
//                            }
//
//                            override fun onResponse(
//                                call: Call<Monster>?,
//                                response: Response<Monster>?
//                            ) {
//                                val monster: Monster? = response?.body()


        val snackbarText = String.format(
            "\n\nEncounter for Party Level " + "\n" + monsterIndex.toString()
        )


        Snackbar.make(
            view,
            snackbarText, Snackbar.LENGTH_LONG
        ).show()

        Snackbar.make(
            view,
            snackbarText, Snackbar.LENGTH_LONG
        ).show()
    }
}
//            })
//    }
//
//}







