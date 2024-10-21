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
    AUTO(IoTCmdConst.FAN_SPEED_AUTO, auto_string, null),
    LOW(IoTCmdConst.FAN_SPEED_LOW, low_string, null),
    MED(IoTCmdConst.FAN_SPEED_NORMAL, med_string, null),
    HIGH(IoTCmdConst.FAN_SPEED_HIGH, high_string, null);
    companion object {
        fun getAcFanModeList() = listOf(
            AUTO, LOW, MED, HIGH
        )
    }
}