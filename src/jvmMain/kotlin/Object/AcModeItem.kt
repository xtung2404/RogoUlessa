package Object

import rogo.iot.module.platform.define.IoTCmdConst
import rogo.iot.module.platform.define.IoTDeviceType
import ui.theme.auto_string
import ui.theme.cooling_string
import ui.theme.dry_string
import ui.theme.fan_string
import ui.theme.heating_string

enum class AcModeItem (
    val cmd: Int,
    val cmdName: String
) {
    AUTO(IoTCmdConst.AC_MODE_AUTO, auto_string),
    COOLING(IoTCmdConst.AC_MODE_COOLING, cooling_string),
    DRY(IoTCmdConst.AC_MODE_DRY, dry_string),
    HEATING(IoTCmdConst.AC_MODE_HEATING, heating_string),
    FAN(IoTCmdConst.AC_MODE_FAN, fan_string);
    companion object {
        fun getACModeList() = listOf(
            AUTO, COOLING, DRY, HEATING, FAN
        )
    }
}