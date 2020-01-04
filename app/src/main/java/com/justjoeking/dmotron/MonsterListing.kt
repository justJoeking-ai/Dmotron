package com.justjoeking.dmotron

class MonsterListing {

    val name = ""
    val url = ""

    public fun getIndex(): String {
        var monsterIndexString =
            url.replace("http://www.dnd5eapi.co/api/monsters/", "", true) // "1" or "2"
        return monsterIndexString
    }

    public fun getId(): String {
        var monsterIdString =
            url.replace("http://www.dnd5eapi.co/api/monsters/", "", true) // "1" or "2"
        return monsterIdString
    }
}
