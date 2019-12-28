package com.justjoeking.dmotron

class MonsterListing {

    val name = ""
    val url = ""

    public fun getId(): Int {
        var monsterIdString = url.replace("http://www.dnd5eapi.co/api/monsters/", "", true) // "1" or "2"
        var intId = monsterIdString.toInt()
        return intId
    }

}
