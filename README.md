# 🚀 CosmoCheck — System Design Document

**App Name:** CosmoCheck  
**Tagline:** *"Every component, cleared for launch."*  
**Version:** 1.0  
**Stack:** Kotlin · Jetpack Compose · Hilt · Room · Coroutines  
**Architecture:** Clean Architecture · MVVM · Full Multi-Module  
**Status:** Draft

---

## 1. Theme & Branding

CosmoCheck frames your phone as a **spacecraft** and each hardware test as a **pre-launch systems check**. The user is the mission commander. Tests that pass are "systems nominal"; failures are "anomalies detected"; skipped tests are "offline — standing by."

### Naming Conventions

| Concept | Generic Term | CosmoCheck Term |
|---------|-------------|-----------------|
| App | Hardware Tester | CosmoCheck |
| Full test run | Run all tests | Launch Sequence |
| Individual test | Test module | System Check |
| Pass | Pass | ✅ Nominal |
| Fail | Fail | ⚠️ Anomaly |
| Skip | Skip | 🌙 Standby |
| Report | Diagnostic report | Mission Log |
| Device | Phone | Spacecraft |
| Home screen | Dashboard | Mission Control |

### Visual Identity

| Token | Value | Usage |
|-------|-------|-------|
| `CosmoPurple` | `#1A0533` | Background / deep space |
| `NebulaPink` | `#C45FE0` | Primary accent, CTAs |
| `StarWhite` | `#F0EEFF` | Body text |
| `OrbitBlue` | `#4DAAFF` | Secondary accent, progress |
| `NominalGreen` | `#3EE8A0` | Pass / nominal state |
| `AnomalyRed` | `#FF5C77` | Fail / anomaly state |
| `StandbyGold` | `#FFCC44` | Skip / standby state |

**Typography:** Display — `Exo 2` (futuristic, geometric, open-source). Body — `Inter`. Monospace data — `JetBrains Mono` for sensor readouts.

**Signature element:** A circular "launch ring" progress indicator on the Mission Control screen that lights up sector by sector as each System Check completes, resembling a spacecraft instrument panel.

---

## 2. Overview

CosmoCheck is a modular Android hardware diagnostics app. It runs automated and semi-automated checks across all major hardware subsystems and presents results as a shareable Mission Log. Designed for repair technicians, QA teams, and curious users who want to verify every system before "launch" — i.e., daily use.

---

## 3. Goals & Non-Goals

### Goals
- Diagnose all major hardware modules on any Android device
- Present results with delightful space-themed UI
- Run as a full Launch Sequence or cherry-pick individual System Checks
- Generate and share a Mission Log (text/JSON)
- Work fully offline
- Scale cleanly via Gradle module boundaries

### Non-Goals
- Root/system-level access
- Performance benchmarking
- OEM-locked hardware
- Cloud sync or user accounts

---

## 4. Architecture Overview

CosmoCheck uses **Clean Architecture** with **full Gradle module separation** across four layers: `app`, `feature:*`, `domain`, and `data:*`. The presentation pattern is **MVVM** everywhere.

```
┌────────────────────────────────────────────────────────────┐
│                    :app (shell)                            │
│         MainActivity · AppNavGraph · Hilt setup            │
└────────┬───────────────────────────────────────────────────┘
         │ depends on
┌────────▼───────────────────────────────────────────────────┐
│              Feature Modules (:feature:*)                  │
│   mission-control · launch-sequence · system-check         │
│   mission-log · settings                                   │
│   Each owns: Screens · ViewModels · local nav              │
└────────┬───────────────────────────────────────────────────┘
         │ depends on
┌────────▼───────────────────────────────────────────────────┐
│                  :domain                                   │
│   HardwareTester · TestOrchestrator · UseCases             │
│   Pure Kotlin — zero Android framework imports             │
└────────┬───────────────────────────────────────────────────┘
         │ depends on
┌────────▼───────────────────────────────────────────────────┐
│            Data Modules (:data:*)                          │
│   hardware · report · history                              │
│   Tester implementations · Room DB · ReportGenerator       │
└────────────────────────────────────────────────────────────┘
         │ both layers share
┌────────▼───────────────────────────────────────────────────┐
│              Shared Modules (:core:*)                      │
│   ui · designsystem · common · testing                     │
└────────────────────────────────────────────────────────────┘
```

---

## 5. Module Graph

```
:app
 ├── :feature:mission-control
 ├── :feature:launch-sequence
 ├── :feature:system-check
 ├── :feature:mission-log
 ├── :feature:settings
 ├── :domain
 ├── :data:hardware
 ├── :data:report
 ├── :data:history
 ├── :core:ui
 ├── :core:designsystem
 ├── :core:common
 └── :core:testing

:feature:* → :domain, :core:ui, :core:designsystem, :core:common
:domain     → :core:common  (pure Kotlin only)
:data:*     → :domain, :core:common
:core:ui    → :core:designsystem, :core:common
```

**Dependency rule:** Features never depend on other features. Data modules never depend on feature modules. Domain has zero Android dependencies.

---

## 6. Module Descriptions

### :app
The shell module. Contains `MainActivity`, `AppNavGraph`, the top-level `@HiltAndroidApp` Application class, and Hilt binding of all data implementations to domain interfaces.

```
:app/
├── CosmoCheckApp.kt          (@HiltAndroidApp)
├── MainActivity.kt
├── navigation/
│   └── AppNavGraph.kt        (top-level NavHost, feature route delegation)
└── di/
    └── AppBindingsModule.kt  (binds data impls to domain interfaces)
```

---

### :domain
Pure Kotlin module. No Android SDK imports. Defines all contracts that feature and data modules depend on.

```
:domain/
├── model/
│   ├── SystemCheck.kt        (enum: DISPLAY, TOUCHSCREEN, CAMERA_FRONT, ...)
│   ├── CheckResult.kt        (status: NOMINAL, ANOMALY, STANDBY, PENDING, IN_PROGRESS)
│   ├── MissionReport.kt
│   └── SpacecraftInfo.kt     (device info model)
├── tester/
│   └── HardwareTester.kt     (interface)
├── orchestrator/
│   └── LaunchOrchestrator.kt (interface)
├── repository/
│   ├── MissionHistoryRepository.kt  (interface)
│   └── ReportRepository.kt          (interface)
└── usecase/
    ├── RunLaunchSequenceUseCase.kt
    ├── RunSingleCheckUseCase.kt
    ├── GetMissionHistoryUseCase.kt
    └── GenerateMissionLogUseCase.kt
```

**Core domain interfaces:**

```kotlin
// Pure interface — data module provides implementation
interface HardwareTester {
    val systemCheck: SystemCheck
    val displayName: String           // e.g. "Thruster Camera (Rear)"
    val missionBriefing: String       // e.g. "Verifying ion thruster optics..."
    val requiredPermissions: List<String>
    val timeoutMs: Long get() = 15_000L
    val requiresManualConfirm: Boolean get() = false
    suspend fun run(): CheckResult
}

interface LaunchOrchestrator {
    fun launch(checks: List<SystemCheck>): Flow<LaunchEvent>
    fun submitCrewConfirmation(check: SystemCheck, nominal: Boolean)
}

sealed class LaunchEvent {
    data class CheckStarted(val check: SystemCheck, val index: Int, val total: Int) : LaunchEvent()
    data class CheckCompleted(val result: CheckResult) : LaunchEvent()
    data class CheckSkipped(val check: SystemCheck, val reason: String) : LaunchEvent()
    object SequenceComplete : LaunchEvent()
}
```

---

### :data:hardware
Contains all `HardwareTester` implementations. One file per system check.

```
:data:hardware/
├── di/
│   └── HardwareModule.kt         (Hilt: provides Map<SystemCheck, HardwareTester>)
├── permission/
│   └── PermissionChecker.kt
├── testers/
│   ├── display/
│   │   └── DisplayTester.kt
│   ├── touch/
│   │   └── TouchscreenTester.kt
│   ├── camera/
│   │   ├── FrontCameraTester.kt
│   │   └── RearCameraTester.kt
│   ├── audio/
│   │   ├── MicrophoneTester.kt
│   │   ├── SpeakerTester.kt
│   │   └── EarpieceTester.kt
│   ├── sensors/
│   │   ├── AccelerometerTester.kt
│   │   ├── GyroscopeTester.kt
│   │   ├── MagnetometerTester.kt
│   │   ├── ProximityTester.kt
│   │   ├── AmbientLightTester.kt
│   │   └── BarometerTester.kt
│   ├── connectivity/
│   │   ├── WifiTester.kt
│   │   ├── BluetoothTester.kt
│   │   ├── NfcTester.kt
│   │   └── GpsTester.kt
│   └── misc/
│       ├── BatteryTester.kt
│       ├── FingerprintTester.kt
│       ├── VibratorTester.kt
│       ├── TorchTester.kt
│       ├── ButtonsTester.kt
│       └── UsbPortTester.kt
└── orchestrator/
    └── LaunchOrchestratorImpl.kt
```

---

### :data:history
Room database for storing past Mission Reports.

```
:data:history/
├── di/
│   └── HistoryModule.kt
├── db/
│   ├── CosmoDatabase.kt          (@Database)
│   ├── MissionReportDao.kt
│   └── entity/
│       ├── MissionReportEntity.kt
│       └── CheckResultEntity.kt
└── repository/
    └── MissionHistoryRepositoryImpl.kt
```

---

### :data:report
Report generation and export logic.

```
:data:report/
├── di/
│   └── ReportModule.kt
├── generator/
│   ├── TextReportGenerator.kt
│   └── JsonReportGenerator.kt
├── share/
│   └── MissionLogShareHelper.kt  (builds share Intent)
└── repository/
    └── ReportRepositoryImpl.kt
```

---

### :feature:mission-control
The home screen — Mission Control dashboard.

```
:feature:mission-control/
├── MissionControlScreen.kt       (LaunchRing + system overview grid)
├── MissionControlViewModel.kt
└── navigation/
    └── MissionControlNavigation.kt
```

**What it shows:** Animated launch ring, spacecraft info card, last mission summary chip, two CTA buttons ("Begin Launch Sequence" / "Select Systems").

---

### :feature:launch-sequence
Runs all (or selected) system checks sequentially.

```
:feature:launch-sequence/
├── LaunchSequenceScreen.kt       (progress bar, current check card)
├── LaunchSequenceViewModel.kt
├── components/
│   ├── CheckProgressCard.kt
│   └── LaunchCompleteSplash.kt
└── navigation/
    └── LaunchSequenceNavigation.kt
```

---

### :feature:system-check
Hosts the interactive UI for each individual system check. Each `HardwareTester` can optionally provide a `@Composable` probe panel (e.g., touch grid, camera viewfinder).

```
:feature:system-check/
├── SystemCheckScreen.kt
├── SystemCheckViewModel.kt
├── panels/
│   ├── TouchGridPanel.kt         (draw finger traces)
│   ├── CameraPreviewPanel.kt     (CameraX preview surface)
│   ├── SensorReadoutPanel.kt     (live axis graph)
│   ├── ManualConfirmPanel.kt     ("Did you feel the vibration?" Yes/No)
│   └── AudioWavePanel.kt         (mic amplitude bar)
└── navigation/
    └── SystemCheckNavigation.kt
```

---

### :feature:mission-log
Report / results screen.

```
:feature:mission-log/
├── MissionLogScreen.kt           (results list + health score ring)
├── MissionLogViewModel.kt
├── HistoryScreen.kt              (past mission reports list)
├── components/
│   ├── CheckResultRow.kt
│   ├── HealthScoreRing.kt
│   └── ShareLogButton.kt
└── navigation/
    └── MissionLogNavigation.kt
```

---

### :feature:settings
Theme toggles, timeout configuration, about screen.

```
:feature:settings/
├── SettingsScreen.kt
├── SettingsViewModel.kt
└── navigation/
    └── SettingsNavigation.kt
```

---

### :core:designsystem
Single source of truth for all visual tokens and components.

```
:core:designsystem/
├── theme/
│   ├── CosmoTheme.kt             (MaterialTheme wrapper)
│   ├── CosmoColors.kt            (color tokens)
│   ├── CosmoTypography.kt        (Exo2 + Inter + JetBrainsMono)
│   └── CosmoShapes.kt
└── components/
    ├── CosmoButton.kt
    ├── StatusChip.kt             (Nominal / Anomaly / Standby chips)
    ├── LaunchRing.kt             (animated circular progress)
    ├── StarfieldBackground.kt    (parallax starfield canvas)
    └── PulseIcon.kt              (pulsing icon for in-progress state)
```

---

### :core:ui
Shared Compose utilities: permission launchers, keyboard handling, WindowInsets helpers.

### :core:common
Pure Kotlin utilities: `Result` wrappers, `DispatcherProvider` interface, extension functions.

### :core:testing
Shared fakes and test fixtures: `FakeHardwareTester`, `FakeLaunchOrchestrator`, test coroutine rules.

---

## 7. Navigation

Top-level `NavHost` lives in `:app`. Each feature module exposes a `fun NavGraphBuilder.featureNavGraph(navController)` extension.

```
cosmocheck://mission-control          (start destination)
cosmocheck://launch-sequence?checks={ids}
cosmocheck://system-check/{checkId}
cosmocheck://mission-log/{reportId}
cosmocheck://mission-log/history
cosmocheck://settings
```

```kotlin
// AppNavGraph.kt in :app
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "mission-control") {
        missionControlNavGraph(navController)
        launchSequenceNavGraph(navController)
        systemCheckNavGraph(navController)
        missionLogNavGraph(navController)
        settingsNavGraph(navController)
    }
}
```

---

## 8. System Checks Catalogue

| System Check (enum) | Display Name | Space Flavour | Sensor / API |
|---------------------|-------------|---------------|-------------|
| `DISPLAY` | Viewport Screen | Hull Visual Array | Canvas fullscreen |
| `TOUCHSCREEN` | Touch Grid | Crew Interface Panel | `pointerInput` |
| `CAMERA_FRONT` | Front Camera | Docking Camera | CameraX |
| `CAMERA_REAR` | Rear Camera | Thruster Camera | CameraX / Camera2 |
| `MICROPHONE` | Microphone | Comms Receiver | `AudioRecord` |
| `SPEAKER` | Speaker | Cabin Broadcast | `AudioTrack` |
| `EARPIECE` | Earpiece | Crew Headset | `AudioManager` |
| `VIBRATOR` | Vibrator | Hull Haptics | `VibratorManager` |
| `ACCELEROMETER` | Accelerometer | Inertial Navigator | `TYPE_ACCELEROMETER` |
| `GYROSCOPE` | Gyroscope | Attitude Control | `TYPE_GYROSCOPE` |
| `MAGNETOMETER` | Magnetometer | Deep Space Compass | `TYPE_MAGNETIC_FIELD` |
| `PROXIMITY` | Proximity Sensor | Proximity Beacon | `TYPE_PROXIMITY` |
| `AMBIENT_LIGHT` | Light Sensor | Solar Array Sensor | `TYPE_LIGHT` |
| `GPS` | GPS | Orbital Positioning | `FusedLocationProvider` |
| `WIFI` | Wi-Fi | Relay Antenna | `WifiManager` |
| `BLUETOOTH` | Bluetooth | Short-Range Comms | `BluetoothAdapter` |
| `NFC` | NFC | Docking Handshake | `NfcAdapter` |
| `FINGERPRINT` | Fingerprint | Crew Biometrics | `BiometricPrompt` |
| `BATTERY` | Battery | Reactor Core | `BatteryManager` |
| `BAROMETER` | Barometer | Atmospheric Gauge | `TYPE_PRESSURE` |
| `USB_PORT` | USB Port | Fuel Port | `BATTERY_PLUGGED_USB` |
| `BUTTONS` | Physical Buttons | Manual Override Keys | `KeyEvent` |
| `TORCH` | Flash / Torch | Emergency Beacon | `CameraManager` |

---

## 9. ViewModel Design

```kotlin
// In :feature:launch-sequence
@HiltViewModel
class LaunchSequenceViewModel @Inject constructor(
    private val runLaunchSequence: RunLaunchSequenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchSequenceUiState())
    val uiState: StateFlow<LaunchSequenceUiState> = _uiState.asStateFlow()

    fun beginLaunchSequence(checks: List<SystemCheck>) {
        viewModelScope.launch {
            runLaunchSequence(checks).collect { event ->
                _uiState.update { it.reduce(event) }
            }
        }
    }

    fun submitCrewConfirmation(check: SystemCheck, nominal: Boolean) {
        runLaunchSequence.submitConfirmation(check, nominal)
    }
}

data class LaunchSequenceUiState(
    val progress: Float = 0f,
    val currentCheck: SystemCheck? = null,
    val completedResults: List<CheckResult> = emptyList(),
    val nominalCount: Int = 0,
    val anomalyCount: Int = 0,
    val isComplete: Boolean = false
)
```

---

## 10. Launch Orchestrator Implementation

```kotlin
// In :data:hardware
class LaunchOrchestratorImpl @Inject constructor(
    private val testers: Map<SystemCheck, @JvmSuppressWildcards HardwareTester>,
    private val permissionChecker: PermissionChecker
) : LaunchOrchestrator {

    private val confirmationChannel = Channel<Boolean>(Channel.RENDEZVOUS)

    override fun launch(checks: List<SystemCheck>): Flow<LaunchEvent> = flow {
        checks.forEachIndexed { index, check ->
            val tester = testers[check] ?: return@forEachIndexed
            if (!permissionChecker.allGranted(tester.requiredPermissions)) {
                emit(LaunchEvent.CheckSkipped(check, "Crew clearance denied"))
                return@forEachIndexed
            }
            emit(LaunchEvent.CheckStarted(check, index, checks.size))
            val result = withTimeoutOrNull(tester.timeoutMs) {
                runCatching { tester.run() }.getOrElse { e ->
                    CheckResult(check, CheckStatus.ANOMALY, e.message ?: "Unknown anomaly")
                }
            } ?: CheckResult(check, CheckStatus.ANOMALY, "Mission clock expired")
            emit(LaunchEvent.CheckCompleted(result))
        }
        emit(LaunchEvent.SequenceComplete)
    }.flowOn(Dispatchers.IO)

    override fun submitCrewConfirmation(check: SystemCheck, nominal: Boolean) {
        confirmationChannel.trySend(nominal)
    }
}
```

---

## 11. Permissions Strategy

Permissions requested just-in-time, grouped by system check cluster.

| Permission | System Checks |
|------------|--------------|
| `CAMERA` | Front Camera, Rear Camera, Torch |
| `RECORD_AUDIO` | Microphone |
| `ACCESS_FINE_LOCATION` | GPS |
| `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE` | Wi-Fi |
| `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` | Bluetooth |
| `USE_BIOMETRIC` | Fingerprint |
| `VIBRATE` | Vibrator |
| `NFC` | NFC |

A `CrewClearanceDialog` composable explains why each permission is needed in space-flavoured language before the system dialog appears (e.g., *"CosmoCheck needs access to the Docking Camera to run the thruster optics check."*).

---

## 12. Manual Crew Confirmation Checks

Tests that cannot self-evaluate require crew confirmation. These are:

- **Cabin Broadcast (Speaker)** — "Did you hear the tone?"
- **Crew Headset (Earpiece)** — "Did you hear audio near your ear?"
- **Hull Haptics (Vibrator)** — "Did you feel the vibration?"
- **Hull Visual Array (Display)** — "Do the colours and touch zones look correct?"
- **Manual Override Keys (Buttons)** — "Press each key when prompted."

The `ManualConfirmPanel` composable renders two large buttons — **"✅ Nominal"** and **"⚠️ Anomaly"** — and blocks the orchestrator's `Channel` until tapped.

---

## 13. Mission Log (Report)

```kotlin
// In :data:report
class TextReportGenerator @Inject constructor() {
    fun generate(report: MissionReport): String = buildString {
        appendLine("═══════════════════════════════")
        appendLine("  🚀 COSMOCHECK MISSION LOG")
        appendLine("═══════════════════════════════")
        appendLine("Spacecraft : ${report.spacecraftInfo.model}")
        appendLine("Android    : ${report.spacecraftInfo.androidVersion}")
        appendLine("Mission T+ : ${report.formattedTimestamp}")
        appendLine("Health     : ${report.healthScore}% systems nominal")
        appendLine("───────────────────────────────")
        report.results.forEach { result ->
            val icon = when (result.status) {
                CheckStatus.NOMINAL  -> "✅"
                CheckStatus.ANOMALY  -> "⚠️"
                CheckStatus.STANDBY  -> "🌙"
                else -> "•"
            }
            appendLine("$icon ${result.systemCheck.displayName}")
            if (result.details.isNotBlank()) appendLine("   ↳ ${result.details}")
        }
        appendLine("═══════════════════════════════")
    }
}
```

---

## 14. Gradle Structure

```
cosmocheck/
├── build.gradle.kts               (root, version catalog)
├── settings.gradle.kts            (module declarations)
├── gradle/
│   └── libs.versions.toml         (version catalog)
├── app/
├── domain/
├── data/
│   ├── hardware/
│   ├── history/
│   └── report/
├── feature/
│   ├── mission-control/
│   ├── launch-sequence/
│   ├── system-check/
│   ├── mission-log/
│   └── settings/
└── core/
    ├── designsystem/
    ├── ui/
    ├── common/
    └── testing/
```

Each module has its own `build.gradle.kts`. A shared `convention-plugins` Gradle plugin handles common config (compileSdk, Compose flags, Hilt, etc.) to avoid duplication.

```kotlin
// convention-plugins/AndroidFeaturePlugin.kt (applied to all :feature:* modules)
// Automatically applies: Compose, Hilt, navigation-compose, lifecycle-viewmodel
```

---

## 15. Key Libraries

| Library | Module(s) | Purpose |
|---------|-----------|---------|
| `androidx.compose.*` | `:core:ui`, `:feature:*` | All UI |
| `androidx.navigation:navigation-compose` | `:app`, `:feature:*` | Screen routing |
| `androidx.lifecycle:lifecycle-viewmodel-compose` | `:feature:*` | ViewModel |
| `androidx.camera:camera-*` | `:data:hardware` | Camera testers |
| `androidx.biometric:biometric` | `:data:hardware` | Fingerprint |
| `com.google.android.gms:play-services-location` | `:data:hardware` | GPS |
| `com.google.dagger:hilt-android` | all | DI |
| `androidx.hilt:hilt-navigation-compose` | `:feature:*` | Hilt + Compose nav |
| `androidx.room:room-*` | `:data:history` | Mission history DB |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | `:data:report` | JSON export |
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | `:domain`, `:data:*` | Async |
| `androidx.datastore:datastore-preferences` | `:data:*` | Settings persistence |

---

## 16. Error & Edge Cases

| Scenario | Handling |
|----------|----------|
| Sensor hardware absent | `getDefaultSensor()` → null → auto-STANDBY "System offline" |
| Permission permanently denied | Settings deep-link via `CrewClearanceDialog`; mark STANDBY |
| Test timeout | `withTimeoutOrNull` → ANOMALY "Mission clock expired" |
| User back-press during manual confirm | Cancel channel → STANDBY |
| Camera in use by another app | `CameraAccessException` → ANOMALY + reason |
| Bluetooth/Wi-Fi disabled | Check adapter state first → prompt to enable or STANDBY |

---

## 17. Testing Strategy

| Layer | Module | Tool |
|-------|--------|------|
| Domain use cases | `:domain` | JUnit 5 + Turbine |
| Orchestrator | `:data:hardware` | JUnit 5 + MockK + `FakeHardwareTester` |
| ViewModels | `:feature:*` | Turbine + `TestCoroutineRule` |
| Compose UI | `:feature:*` | `createAndroidComposeRule` |
| DB / Room | `:data:history` | `InMemoryDatabase` |
| Integration | `:app` | Robolectric, Espresso |

Shared fakes live in `:core:testing` and are available to all test source sets.

---

## 18. Future Enhancements

- PDF Mission Log export (`:data:report`)
- QR code mission share (`:feature:mission-log`)
- Technician Mode: configurable pass/fail thresholds (`:feature:settings`)
- Batch scan via ADB companion tool
- Animated rocket launch splash on suite completion
- Wear OS companion app for wrist-based System Checks
- Localization / i18n support
- Tablet adaptive layout (two-column Mission Control)

---

*CosmoCheck — Every component, cleared for launch. 🚀*  
*Update version and module list with each architectural change.*