package com.justjoeking.dmotron

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.core.content.ContextCompat.startActivity
import com.justjoeking.dmotron.model.Monster
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MonsterUtils {

    // CR to XP converter
    fun crXPLookup(): ArrayList<Int> {
        return arrayListOf(
            25, // 1/8
            50, // 1/4
            100, // 1/2
            200, // 1
            450,
            700,
            1100,
            1800, // 5
            2300,
            2900,
            3900,
            5000,
            5900, // 10
            7200,
            8400,
            10000,
            11500,
            13000, // 15
            15000,
            18000,
            20000,
            22000,
            25000, // 20
            33000,
            41000,
            50000,
            62000,
            75000, // 25
            90000,
            105000,
            120000,
            135000,
            155000 // 30
        )
    }

    fun shareMonster(monster: Monster, context: Context) {

        val shareIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.dmotron_monster) + monster.name)
        shareIntent.putExtra(
            Intent.EXTRA_HTML_TEXT,
            Html.fromHtml(
                "<h1>Blah blah blah TODO add monster html email</h1>"
            )
        )
        startActivity(context, Intent.createChooser(shareIntent, context.getString(R.string.send_email)), Bundle());
    }
}