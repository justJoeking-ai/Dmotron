package com.justjoeking.dmotron

import com.justjoeking.dmotron.model.Monster
import com.justjoeking.dmotron.model.Speed

class EmailUtils {

    fun getSingleMonsterEmail(monster: Monster): String {
        return getHeader() + getMonsterBlock(monster) + getFooter()
    }

    fun getEncounterEmail(monsters: ArrayList<Monster>, nums: ArrayList<Int>): String {
        var monstersText = ""
        var headerText = "Encounter: "

        for (monster in monsters) {
            val index = monsters.indexOf(monster)
            monstersText += getMonsterBlock(monster)
            headerText += nums.get(index).toString() + " " + monster.name
            headerText += plurifyMonster(monster.name)

            when (index) {
                monsters.size - 1 -> {
                    // last item, headerText is just fine
                }
                monsters.size - 2 -> {
                    // second to last
                    if (index == 0) {
                        // no oxcom
                        headerText += " and "
                    } else {
                        headerText += ", and "
                    }
                }
                else -> {
                    // anything prior to that
                    headerText += ", "
                }
            }
        }

        return getHeader() + "<br/>" + headerText + "<br/>" + monstersText + getFooter()
    }

    private fun plurifyMonster(name: String): Any? {
        when {
            name.contains("Incubus") -> {
                // Magic String
                return name.replace("Incubus", "Incubi").replace("Succubus", "Succubi")
            }
            name.contains("Erinyes") || name.contains("Deer") -> {
                // Words that don't change when pl'd
                return name
            }
            name.startsWith("Swarm") -> {
                // Swarms
                return name.replace("Swarm", "Swarms")
            }
            name.startsWith("Rug") -> {
                // Rugs of smothering
                return name.replace("Rug", "Rugs")
            }
            name.contains("Fungus") -> {
                // Fungi
                return name.replace("Fungus", "Fungi")
            }
            name.endsWith("Djinni", true) -> {
                return name.replace("Djinni", "Djinn", true)
            }
            name.endsWith("Wolf", true) -> {
                return name.replace("Wolf", "Wolves", true)
            }
            name.endsWith("Mage", true) -> {
                return name.replace("Mage", "Magi", true)
            }
            name.endsWith("s") -> {
                return name + "es"
            }
            else -> {
                // lol
                return name + "s"
            }
        }
    }

    private fun getMonsterBlock(monster: Monster): Any? {
        return "<stat-block>\n    <creature-heading>\n      <h1>{name}</h1>\n      <h2>{" + monster.size + " " +
                monster.type +
                getFormattedSubtype(monster.subtype) +
                " (" + monster.alignment + ")</h2>\n    </creature-heading>\n\n    <top-stats>\n      <property-line>\n        " +
                "<h4>Armor Class</h4>\n        <p>18 (natural armor)</p>\n      </property-line>\n      <property-line>\n        " +
                "<h4>Hit Points</h4>\n        <p>" + monster.hit_points + " (" + monster.hit_dice + ")</p>\n      </property-line>\n      <property-line>\n        " +
                "<h4>Speed</h4>\n        <p>" + monster.speed?.let { getFormattedSpeed(it) } + "</p>\n      </property-line>\n\n      " +
                "<abilities-block " +
                "data-cha=\"" + 1 + "\" " +
                "data-con=\"" + 2 + "\" " +
                "data-dex=\"" + 3 + "\" " +
                "data-int=\"" + 4 + "\" " +
                "data-str=\"" + 5 + "\" " +
                "data-wis=\"" + 6 + "\" " +
                "></abilities-block>\n\n      <property-line>\n        " +
                "<h4>Damage Immunities</h4>\n        <p>poison, psychic</p>\n      </property-line>\n      <property-line>\n        " +
                "<h4>Condition Immunities</h4>\n        <p>blinded, charmed, deafened, exhaustion, frightened, paralyzed,\n          petrified, poisoned</p>\n      </property-line>\n      <property-line>\n        " +
                "<h4>Senses</h4>\n        <p>blindsight 60 ft. (blind beyond this radius), passive Perception 6</p>\n      </property-line>\n      <property-line>\n        " +
                "<h4>Languages</h4>\n        <p>—</p>\n      </property-line>\n      <property-line>\n        " + "<h4>Challenge</h4>\n        <p>1 (200 XP)</p>\n      </property-line>\n    </top-stats>\n\n    <property-block>\n      \" + \"<h4>Antimagic Susceptibility.</h4>\n      <p>The armor is incapacitated while in the area of an <i>antimagic\n        field</i>.  If targeted by <i>dispel magic</i>, the armor must succeed\n        on a Constitution saving throw against the caster’s spell save DC or\n        fall unconscious for 1 minute.</p>\n    </property-block>\n    <property-block>\n      <h4>False Appearance.</h4>\n      <p>While the armor remains motionless, it is indistinguishable from a\n        normal suit of armor.</p>\n    </property-block>\n\n    <h3>Actions</h3>\n\n    <property-block>\n      <h4>Multiattack.</h4>\n      <p>The armor makes two melee attacks.</p>\n    </property-block>\n\n    <property-block>\n      <h4>Slam.</h4>\n      <p><i>Melee Weapon Attack:</i> +4 to hit, reach 5 ft., one target.\n        <i>Hit:</i> 5 (1d6 + 2) bludgeoning damage.</p>\n    </property-block>\n  </stat-block>"
    }

    private fun getFormattedSpeed(speed: Speed): String {
        var speedText = "\nSpeed: "
        if (speed.walk.isNotEmpty()) {
            speedText += "\nWalk: " + speed.walk
        }

        if (speed.fly.isNotEmpty()) {
            speedText += "\nFly: " + speed.fly
        }

        if (speed.swim.isNotEmpty()) {
            speedText += "\nSwim: " + speed.swim
        }

        if (speed.burrow.isNotEmpty()) {
            speedText += "\nBurrow: " + speed.burrow
        }

        if (speed.climb.isNotEmpty()) {
            speedText += "\nClimb: " + speed.climb
        }

        if (speed.hover.isNotEmpty()) {
            speedText += "\nHover: " + speed.hover
        }

        return speedText
    }

    private fun getFormattedSubtype(subtype: String): String {
        if (subtype.isBlank()) {
            return ""
        }
        return " ($subtype)"
    }

    fun getHeader(): String {
        return "<!DOCTYPE html>\n<html><head><meta charset=\"utf-8\"/><link href=\"//fonts.googleapis.com/css?family=Noto+Sans:400,700,400italic,700italic\" rel=\"stylesheet\" type=\"text/css\"/><link href=\"//fonts.googleapis.com/css?family=Libre+Baskerville:700\" rel=\"stylesheet\" type=\"text/css\"/>\n<style>\n    body {\n      margin: 0;\n    }\n\n    stat-block {\n      /* A bit of margin for presentation purposes, to show off the drop\n      shadow. */\n      margin-left: 20px;\n      margin-top: 20px;\n    }\n  </style><script>function createCustomElement(name, contentNode, elementClass = null) {\n  if(elementClass === null) {\n    customElements.define(name,\n      class extends HTMLElement {\n        constructor() {\n          super();\n          this.attachShadow({mode: 'open'})\n            .appendChild(contentNode.cloneNode(true));\n        }\n      }\n    )\n  } else {\n    customElements.define(name, elementClass(contentNode));\n  }\n}\n</script></head><body><template id=\"stat-block\"><style>\n  .bar {\n    height: 5px;\n    background: #E69A28;\n    border: 1px solid #000;\n    position: relative;\n    z-index: 1;\n  }\n\n  :host {\n    display: inline-block;\n  }\n\n  #content-wrap {\n    font-family: 'Noto Sans', 'Myriad Pro', Calibri, Helvetica, Arial,\n                  sans-serif;\n    font-size: 13.5px;\n    background: #FDF1DC;\n    padding: 0.6em;\n    padding-bottom: 0.5em;\n    border: 1px #DDD solid;\n    box-shadow: 0 0 1.5em #867453;\n\n    /* We don't want the box-shadow in front of the bar divs. */\n    position: relative;\n    z-index: 0;\n\n    /* Leaving room for the two bars to protrude outwards */\n    margin-left: 2px;\n    margin-right: 2px;\n\n    /* This is possibly overriden by next CSS rule. */\n    width: 400px;\n\n    -webkit-columns: 400px;\n       -moz-columns: 400px;\n            columns: 400px;\n    -webkit-column-gap: 40px;\n       -moz-column-gap: 40px;\n            column-gap: 40px;\n\n    /* We can't use CSS3 attr() here because no browser currently supports it,\n       but we can use a CSS custom property instead. */\n    height: var(--data-content-height);\n\n    /* When height is constrained, we want sequential filling of columns. */\n    -webkit-column-fill: auto;\n       -moz-column-fill: auto;\n            column-fill: auto;\n  }\n\n  :host([data-two-column]) #content-wrap {\n    /* One column is 400px and the gap between them is 40px. */\n    width: 840px;\n  }\n\n  ::slotted(h3) {\n    border-bottom: 1px solid #7A200D;\n    color: #7A200D;\n    font-size: 21px;\n    font-variant: small-caps;\n    font-weight: normal;\n    letter-spacing: 1px;\n    margin: 0;\n    margin-bottom: 0.3em;\n\n    break-inside: avoid-column;\n    break-after: avoid-column;\n  }\n\n  /* For user-level p elems. */\n  ::slotted(p) {\n    margin-top: 0.3em;\n    margin-bottom: 0.9em;\n    line-height: 1.5;\n  }\n\n  /* Last child shouldn't have bottom margin, too much white space. */\n  ::slotted(*:last-child) {\n    margin-bottom: 0;\n  }\n</style><div class=\"bar\"></div><div id=\"content-wrap\">\n  <slot></slot>\n</div><div class=\"bar\"></div></template><script>{\n  let templateElement = document.getElementById('stat-block');\n  createCustomElement('stat-block', templateElement.content);\n}</script><template id=\"creature-heading\"><style>\n  ::slotted(h1) {\n    font-family: 'Libre Baskerville', 'Lora', 'Calisto MT',\n                 'Bookman Old Style', Bookman, 'Goudy Old Style',\n                 Garamond, 'Hoefler Text', 'Bitstream Charter',\n                 Georgia, serif;\n    color: #7A200D;\n    font-weight: 700;\n    margin: 0px;\n    font-size: 23px;\n    letter-spacing: 1px;\n    font-variant: small-caps;\n  }\n\n  ::slotted(h2) {\n    font-weight: normal;\n    font-style: italic;\n    font-size: 12px;\n    margin: 0;\n  }\n</style><slot></slot></template><script>{\n  let templateElement = document.getElementById('creature-heading');\n  createCustomElement('creature-heading', templateElement.content);\n}</script><template id=\"tapered-rule\"><style>\n  svg {\n    fill: #922610;\n    /* Stroke is necessary for good antialiasing in Chrome. */\n    stroke: #922610;\n    margin-top: 0.6em;\n    margin-bottom: 0.35em;\n  }\n</style><svg height=\"5\" width=\"400\">\n  <polyline points=\"0,0 400,2.5 0,5\"></polyline>\n</svg></template><script>{\n  let templateElement = document.getElementById('tapered-rule');\n  createCustomElement('tapered-rule', templateElement.content);\n}</script><template id=\"top-stats\"><style>\n  ::slotted(*) {\n    color: #7A200D;\n  }\n</style><hr/><slot></slot><hr/></template><script>{\n  let templateElement = document.getElementById('top-stats');\n  createCustomElement('top-stats', templateElement.content);\n}</script><template id=\"abilities-block\"><style>\n  table {\n    width: 100%;\n    border: 0px;\n    border-collapse: collapse;\n  }\n  th, td {\n    width: 50px;\n    text-align: center;\n  }\n</style><hr/><table>\n  <tbody><tr>\n    <th>STR</th>\n    <th>DEX</th>\n    <th>CON</th>\n    <th>INT</th>\n    <th>WIS</th>\n    <th>CHA</th>\n  </tr>\n  <tr>\n    <td id=\"str\"></td>\n    <td id=\"dex\"></td>\n    <td id=\"con\"></td>\n    <td id=\"int\"></td>\n    <td id=\"wis\"></td>\n    <td id=\"cha\"></td>\n  </tr>\n</tbody></table><hr/></template><script>{\n  function abilityModifier(abilityScore) {\n  let score = parseInt(abilityScore, 10);\n  return Math.floor((score - 10) / 2);\n}\n\nfunction formattedModifier(abilityModifier) {\n  if (abilityModifier >= 0) {\n    return '+' + abilityModifier;\n  }\n  // This is an en dash, NOT a \"normal\" dash. The minus sign needs to be more\n  // visible.\n  return '–' + Math.abs(abilityModifier);\n}\n\nfunction abilityText(abilityScore) {\n  return [String(abilityScore),\n          ' (',\n          formattedModifier(abilityModifier(abilityScore)),\n          ')'].join('');\n}\n\nfunction elementClass(contentNode) {\n  return class extends HTMLElement {\n    constructor() {\n      super();\n      this.attachShadow({mode: 'open'})\n        .appendChild(contentNode.cloneNode(true));\n    }\n    connectedCallback() {\n      let root = this.shadowRoot;\n      for (let i = 0; i < this.attributes.length; i++) {\n        let attribute = this.attributes[i];\n        let abilityShortName = attribute.name.split('-')[1];\n        root.getElementById(abilityShortName).textContent =\n           abilityText(attribute.value);\n      }\n    }\n  }\n}\n\n  let templateElement = document.getElementById('abilities-block');\n  createCustomElement('abilities-block', templateElement.content, elementClass);\n}</script><template id=\"property-line\"><style>\n  :host {\n    line-height: 1.4;\n    display: block;\n    text-indent: -1em;\n    padding-left: 1em;\n  }\n\n  ::slotted(h4) {\n    margin: 0;\n    display: inline;\n    font-weight: bold;\n  }\n\n  ::slotted(p:first-of-type) {\n    display: inline;\n    text-indent: 0;\n  }\n\n  ::slotted(p) {\n    text-indent: 1em;\n    margin: 0;\n  }\n</style><slot></slot></template><script>{\n  let templateElement = document.getElementById('property-line');\n  createCustomElement('property-line', templateElement.content);\n}</script><template id=\"property-block\"><style>\n  :host {\n    margin-top: 0.3em;\n    margin-bottom: 0.9em;\n    line-height: 1.5;\n    display: block;\n  }\n\n  ::slotted(h4) {\n    margin: 0;\n    display: inline;\n    font-weight: bold;\n    font-style: italic;\n  }\n\n  ::slotted(p:first-of-type) {\n    display: inline;\n    text-indent: 0;\n  }\n\n  ::slotted(p) {\n    text-indent: 1em;\n    margin: 0;\n  }\n</style><slot></slot></template><script>{\n  let templateElement = document.getElementById('property-block');\n  createCustomElement('property-block', templateElement.content);\n}</script>"
    }

    fun getFooter(): String {
        return "</body></html>"
    }
}