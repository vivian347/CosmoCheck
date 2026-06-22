package kinyua.vivian.domain.model

import androidx.annotation.Px

data class SpacecraftInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkInt: Int,
    val serialHash: String,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val DensityDpi: Int,
) {
    val fullModelName: String get() = "$manufacturer $model"
    val resolutionLabel: String get() = "${screenWidthPx}x$screenHeightPx"
}