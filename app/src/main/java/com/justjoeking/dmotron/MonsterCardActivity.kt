package com.justjoeking.dmotron


//import android.os.Bundle
//import android.os.Message
//import android.util.Log
//import com.google.android.material.snackbar.Snackbar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_all_Monster.*
//import kotlinx.android.synthetic.main.activity_main.*
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
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.MonsterCardActivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_all_monster.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_monster_card.*
import kotlinx.android.synthetic.main.content_main.*

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
            val monsterInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            monsterInput.layoutParams = lp
            monsterInput.inputType = InputType.TYPE_CLASS_TEXT
            val monsterIndex = monsterInput.toString()
            val sharedPref = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE)
            sharedPref.getString("Orc", String.toString())
            monsterInput.hint = getText(R.string.Find_your_monster)

            var builder = AlertDialog.Builder(this)
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



