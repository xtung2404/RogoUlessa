package Object

import ui.theme.all_devices
import ui.theme.overview

enum class GeneralFunction (
    val label: String,
    val image: String
) {
    OVERVIEW(overview, ""),
    ALLDEVICES(all_devices, "");
    companion object {
        fun getGeneralFuncs() = listOf(
            OVERVIEW,

        )
    }
}