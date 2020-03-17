package com.justjoeking.dmotron

class Monster(public var name: String, public var url: String) {

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

    var fl: Float = 1.0f
    val id = ""
    val index = ""
    val size = ""
    val type = ""
    var challenge_rating = 0f
    val subtype = ""
    val alignment = ""
    val armor_class = 0f
    val hit_points = 0f
    val hit_dice = ""
    val speed: Speed? = null
    val strength = 0f
    val dexterity = 0f
    val constitution = 0f
    val intelligence = 0f
    val wisdom = 0f
    val charisma = 0f
//    val proficiencies = arrayListOf<ProficienciesListing>()
//    val dexterity_save = 0f
//    val constitution_save = 0f
//    val wisdom_save = 0f
//    val charisma_save = 0f
//    val perception = 0f
//    val stealth = 0f
//    val damage_vulnerabilities = ""
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
