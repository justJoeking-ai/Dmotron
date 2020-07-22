package com.justjoeking.dmotron.model

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

    val armor_class = 0f // 17
    val hit_points = 0f // 135
    val hit_dice = "" // "18d10"
    val speed: Speed? = null

    val strength = 0f
    val dexterity = 0f
    val constitution = 0f
    val intelligence = 0f
    val wisdom = 0f
    val charisma = 0f

    var proficiencies = ArrayList<Proficiency>()

    var fl: Float = 1.0f
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
