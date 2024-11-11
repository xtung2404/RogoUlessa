package Object

import rogo.iot.module.platform.define.IoTCmdConst
import ui.theme.auto_string
import ui.theme.high_string
import ui.theme.low_string
import ui.theme.med_string

enum class AcFanMode (
    val cmd: Int,
    val cmdName: String,
    val cmdImg: String?= null
) {
    AUTO(IoTCmdConst.FAN_SPEED_AUTO, auto_string,"icons/ic_fan_mode.png"),
    LOW(IoTCmdConst.FAN_SPEED_LOW, low_string,"icons/ic_fan_low.png"),
    MED(IoTCmdConst.FAN_SPEED_NORMAL, med_string,"icons/ic_fan_medium.png"),
    HIGH(IoTCmdConst.FAN_SPEED_HIGH, high_string,"icons/ic_fan_high.png");
    companion object {
        fun getAcFanModeList() = listOf(
            AUTO, LOW, MED, HIGH
        )
    }
}