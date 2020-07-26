package com.justjoeking.dmotron.model

import com.google.gson.annotations.SerializedName
import com.justjoeking.dmotron.Proficiency

class Monster(var name: String, var url: String) {

    fun isTerrestrial(): Boolean {
        if (speed != null) {
            if (speed.walk.isEmpty()) {
                return false
            }
        }
        return true
    }

//    fun isProficient(): Boolean {
//        if (proficiencies != null) {
//            if (proficiencies.name.isEmpty()) {
//                return false
//            }
//        }
//        return true
//    }

    val id = "" // "5e8b834d0b1bb138c54a54f7", doesn't seem to be in wide use
    val index = "" // "aboleth"
    val size = "" // "Large"
    val type = "" // "aberration"
    val subtype: String? = null // null or "Dragon"
    val alignment = "" // "lawful evil"

    @SerializedName("armor_class")
    val armorClass = 0 // 17

    @SerializedName("hit_points")
    val hitPoints = 0 // 135

    @SerializedName("hit_dice")
    val hitDice = "" // "18d10"

    val speed: Speed? = null

    val strength = 0
    val dexterity = 0
    val constitution = 0
    val intelligence = 0
    val wisdom = 0
    val charisma = 0

    var proficiencies = ArrayList<Proficiency>()

    @SerializedName("challenge_rating")
    var challenge_rating = 0f

//    val damage_vulnerabilities = ArrayList<>()
//    val damage_resistances = ""
//    val damage_immunities = ""
//    val condition_immunities = ""
//
//    val senses = ""
//    val languages = ""
//    val List<SpecialAbility> special_abilities = null;
//      val List<Action> actions = null;
//    val List<LegendaryAction> legendary_actions = null;
    
}
