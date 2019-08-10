package com.justjoeking.dmotron

import android.content.res.ColorStateList
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.ThreadLocalRandom


class MainActivity : AppCompatActivity() {

    // https://www.reddit.com/r/StrangePlanet/

    // https://fragmentedpodcast.com/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Btncolor)))
        var rollhistory = ArrayList<Int>(0)
        fab.setOnClickListener { view ->
            var score = RandomUtils.randInt(1, 20)
//            Snackbar.make(view, String.format("You rolled a %d",score), Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

            rollhistory.add(score)
            centertext.text = String.format("You rolled a %s \n", rollhistory.joinToString("\n You rolled a "))
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
