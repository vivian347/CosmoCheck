package kinyua.vivian.domain.model

enum class CheckCategory(val displayName: String, val missionName: String) {
    DISPLAY("Display & Touch", "Visual Systems"),
    TOUCH("Touchscreen", "Crew Interface"),
    CAMERA("Camera", "Optics Array"),
    AUDIO("Audio", "Comms Systems"),
    SENSORS("Sensors", "Navigation Array"),
    CONNECTIVITY("Connectivity", "Antenna Systems"),
    MISC("Miscellaneous", "Auxiliary Systems"),
}