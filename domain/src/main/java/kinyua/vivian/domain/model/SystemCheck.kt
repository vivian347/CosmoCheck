package kinyua.vivian.domain.model

enum class SystemCheck(
    val displayName: String,
    val missionName: String,
    val category: CheckCategory
) {

    DISPLAY(
        displayName = "Viewport Screen",
        missionName = "Hull Visual Array",
        category = CheckCategory.DISPLAY
    ),
    TOUCHSCREEN(
        displayName = "Touch Grid",
        missionName = "Crew Interface Panel",
        category = CheckCategory.TOUCH
    ),
    CAMERA_FRONT(
        displayName = "Front Camera",
        missionName = "Docking Camera",
        category = CheckCategory.CAMERA
    ),
    CAMERA_REAR(
        displayName = "Rear Camera",
        missionName = "Thruster Camera",
        category = CheckCategory.CAMERA
    ),
    MICROPHONE(
        "Microphone",
        "Comms Receiver",
        CheckCategory.AUDIO
    ),
    SPEAKER(
        "Speaker",
        "Cabin Broadcast",
        CheckCategory.AUDIO
    ),
    EARPIECE(
        "Earpiece",
        "Crew Headset",
        CheckCategory.AUDIO
    ),
    VIBRATOR(
        "Vibrator",
        "Hull Haptics",
        CheckCategory.MISC
    ),
    ACCELEROMETER(
        "Accelerometer",
        "Inertial Navigator",
        CheckCategory.SENSORS
    ),
    GYROSCOPE(
        "Gyroscope",
        "Attitude Control",
        CheckCategory.SENSORS
    ),
    MAGNETOMETER(
        "Magnetometer",
        "Deep Space Compass",
        CheckCategory.SENSORS
    ),
    PROXIMITY(
        "Proximity Sensor",
        "Proximity Beacon",
        CheckCategory.SENSORS
    ),
    AMBIENT_LIGHT(
        "Light Sensor",
        "Solar Array Sensor",
        CheckCategory.SENSORS
    ),
    GPS(
        displayName = "GPS",
        missionName = "Orbital Positioning",
        category = CheckCategory.CONNECTIVITY
    ),
    WIFI(
        "Wi-Fi",
        "Relay Antenna",
        CheckCategory.CONNECTIVITY
    ),
    BLUETOOTH(
        "Bluetooth",
        "Short-Range Comms",
        CheckCategory.CONNECTIVITY
    ),
    NFC(
        "NFC",
        "Docking Handshake",
        CheckCategory.CONNECTIVITY
    ),
    FINGERPRINT(
        "Fingerprint",
        "Crew Biometrics",
        CheckCategory.MISC
    ),
    BATTERY(
        "Battery",
        "Reactor Core",
        CheckCategory.MISC
    ),
    BAROMETER(
        "Barometer",
        "Atmospheric Gauge",
        CheckCategory.MISC
    ),
    USB_PORT(
        "USB Port",
        "Fuel Port",
        CheckCategory.MISC
    ),
    TORCH(
        "Torch",
        "Emergency Beacon",
        CheckCategory.MISC
    ),

}