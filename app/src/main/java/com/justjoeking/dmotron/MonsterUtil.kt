package com.justjoeking.dmotron

class MonsterUtil {

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
}