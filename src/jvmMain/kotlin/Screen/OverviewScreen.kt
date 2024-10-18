package Screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2Reader
import rogo.iot.module.platform.ILogR
import rogo.iot.module.platform.define.IoTDeviceType
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTDevice
import rogo.iot.module.rogocore.sdk.entity.IoTGroup
import rogo.iot.module.rogocore.sdk.entity.IoTObjState
import ui.theme.BACKGROUND_COLOR
import ui.theme.GRAY
import ui.theme.Headline6
import ui.theme.Roboto
import ui.theme.RogoSpace
import ui.theme.Subtitile1
import ui.theme.Subtitile2
import ui.theme.ac_control_string
import ui.theme.choose_house_to_control_string
import ui.theme.house_string
import ui.theme.humid_string
import ui.theme.light_control_string
import ui.theme.no_available_device_string
import ui.theme.off_string
import ui.theme.on_string
import ui.theme.overview
import ui.theme.presence_detected
import ui.theme.presence_device_string
import ui.theme.presence_undetected
import ui.theme.temperature_string

@Composable
fun overviewScreen() {
    SmartSdk.setAppLocation(SmartSdk.locationHandler().all.first().uuid)
    val currentGroup = remember {
        mutableStateOf<IoTGroup?>(SmartSdk.groupHandler().all.first())
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.4f)
        ) {
            GroupManagementSection(currentGroup)
        }
        RogoSpace(8)
        Text(
            text = overview,
            fontSize = Headline6.sp,
            fontFamily = Roboto,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(
            modifier = Modifier.weight(0.6f)
        ) {
            GroupDetailSection(currentGroup)
        }
    }
}

@Composable
fun GroupManagementSection(currentGroup: MutableState<IoTGroup?>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(BACKGROUND_COLOR, RoundedCornerShape(8.dp))
            .padding(20.dp)
    ) {
        Text(
            text = house_string,
            fontSize = Headline6.sp,
            color = Color.Black,
            fontFamily = Roboto
        )
        RogoSpace(4)
        Text(
            text = choose_house_to_control_string,
            fontSize = Subtitile2.sp,
            color = Color.Black,
            fontFamily = Roboto
        )
        RogoSpace(8)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            GroupViewHolder(currentGroup)
        }
    }
}

@Composable
fun GroupViewHolder(currentGroup: MutableState<IoTGroup?>) {
    val groupList = remember {
        mutableStateMapOf<IoTGroup, Boolean>().apply {
            SmartSdk.groupHandler().all.toList().forEach { ioTGroup ->
                this[ioTGroup] = currentGroup.value == ioTGroup
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(groupList.entries.sortedBy { it.key.label }.chunked(5)) { rowGroups ->
            Row(modifier = Modifier.fillMaxWidth()) {
                for (ioTGroup in rowGroups) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                currentGroup.value = ioTGroup.key
                                groupList.forEach {
                                    groupList[it.key] = it.key == ioTGroup.key
                                }
                            }
                    ) {
                        groupItem(ioTGroup)
                    }
                }
                if (rowGroups.size < 5) {
                    Spacer(modifier = Modifier.weight((5 - rowGroups.size).toFloat()))
                }
            }
        }
    }
}

@Composable
fun groupItem(ioTGroup: Map.Entry<IoTGroup, Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (ioTGroup.value) Color.Black else BACKGROUND_COLOR,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = 14.dp, horizontal = 24.dp)
    ) {
        Text(
            text = ioTGroup.key.label,
            fontSize = Subtitile1.sp,
            color = if (ioTGroup.value) Color.White else Color.Black,
            fontFamily = Roboto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GroupDetailSection(currentGroup: MutableState<IoTGroup?>) {
    currentGroup.value.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                .padding(20.dp),
        ) {
            Text(
                text = it?.label ?: "",
                color = Color.Black,
                fontFamily = Roboto,
                fontSize = Headline6.sp
            )
            RogoSpace(8)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    ControlDeviceSection(IoTDeviceType.AC, currentGroup.value?.uuid)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    ControlDeviceSection(IoTDeviceType.LIGHT, currentGroup.value?.uuid)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .padding(bottom = 4.dp)
                    ) {
                        TempHumidInfoSection(currentGroup.value?.uuid)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 4.dp)
                    ) {
                        PresenceInfoSection(currentGroup.value?.uuid)
                    }
                }
            }
        }
    }
}

@Composable
fun ControlDeviceSection(deviceType: Int, currentGroupUuid: String?) {
    val currentDeviceList = SmartSdk.deviceHandler().all.filter {
        it.groupId == currentGroupUuid && it.devType == deviceType
    }.toMutableStateList()
    ILogR.D("Overview", "CURRENT_DEVICE_LIST", deviceType, currentDeviceList.size)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GRAY, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (deviceType != IoTDeviceType.LIGHT) ac_control_string else light_control_string,
                color = Color.Black,
                fontFamily = Roboto,
                fontSize = Subtitile2.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = off_string,
                color = Color.Black,
                fontFamily = Roboto,
                fontSize = Subtitile2.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .background(BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        SmartSdk.controlHandler().controlGroupPower(
                            currentGroupUuid,
                            false,
                            deviceType,
                            null
                        )
                    }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
            RogoSpace(4)
            Text(
                text = on_string,
                color = Color.Black,
                fontFamily = Roboto,
                fontSize = Subtitile2.sp,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .background(BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        SmartSdk.controlHandler().controlGroupPower(
                            currentGroupUuid,
                            true,
                            deviceType,
                            null
                        )
                    }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
        RogoSpace(4)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            ControlDeviceViewHolder(currentDeviceList)
        }
    }
}

@Composable
fun ControlDeviceViewHolder(currentDeviceList: List<IoTDevice>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(currentDeviceList) {
            DeviceControlItem(it, onItemClick = {

            })
        }
    }
}

@Composable
fun DeviceControlItem(ioTDevice: IoTDevice, onItemClick: (IoTDevice) -> Unit) {
    val deviceState by remember {
        mutableStateOf(SmartSdk.stateHandler().getObjState(ioTDevice.uuid))
    }
    var isOn by remember {
        mutableStateOf(if (deviceState != null) deviceState.isOn else false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                onItemClick.invoke(ioTDevice)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ioTDevice.label,
            fontSize = Headline6.sp,
            color = Color.Black,
            fontFamily = Roboto,
            modifier = Modifier.weight(1f)
        )
        if (ioTDevice.devType != IoTDeviceType.LIGHT) {
            Icon(
                Icons.Default.Add,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {

                    },
                tint = Color.Black
            )
            RogoSpace(4)
            Icon(
                Icons.Filled.Add,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {

                    },
                tint = Color.Black
            )
            RogoSpace(4)
            deviceState?.let { state ->
                Text(
                    text = state.temp.toString(),
                    fontSize = Subtitile2.sp,
                    color = Color.Black,
                    fontFamily = Roboto
                )
            }
        }
        Switch(
            isOn,
            onCheckedChange = {
                isOn = it
                SmartSdk.controlHandler().controlDevicePower(ioTDevice.uuid, ioTDevice.elementIds, isOn, null)
            }
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TempHumidInfoSection(currentGroupUuid: String?) {
    var currentDevice by remember {
        mutableStateOf<IoTDevice?>(null)
    }
    currentDevice = SmartSdk.deviceHandler().all.filter {
        it.devType == IoTDeviceType.MEDIA_BOX && it.groupId == currentGroupUuid
    }.firstOrNull()
    ILogR.D("OverviewScreen", "CURRENT_BOX_DEVICE", SmartSdk.deviceHandler().all.firstOrNull {
        it.devType == IoTDeviceType.MEDIA_BOX && it.groupId == currentGroupUuid
    }?.label)
    var deviceState by remember {
        mutableStateOf<IoTObjState?>(null)
    }
    CoroutineScope(Dispatchers.Main).launch {
        SmartSdk.stateHandler().pingDeviceState(currentDevice?.uuid)
        delay(500)
        deviceState = SmartSdk.stateHandler().getObjState(currentDevice?.uuid)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GRAY, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = temperature_string,
                fontSize = Headline6.sp,
                color = Color.Black,
                fontFamily = Roboto,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${deviceState?.temp}°C",
                fontSize = Headline6.sp,
                color = Color.Black,
                fontFamily = Roboto,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(GRAY, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp)
            )
        }
        RogoSpace(16)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = humid_string,
                fontSize = Headline6.sp,
                color = Color.Black,
                fontFamily = Roboto,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${deviceState?.humid}%",
                fontSize = Headline6.sp,
                color = Color.Black,
                fontFamily = Roboto,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(GRAY, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun PresenceInfoSection(currentGroupUuid: String?) {
    val presenceDevice by remember {
        mutableStateOf<IoTDevice?>(SmartSdk.deviceHandler().all.filter {
            it.devType == IoTDeviceType.PRESENSCE_SENSOR
        }.firstOrNull())
    }
    val currentState by remember {
        mutableStateOf(SmartSdk.stateHandler().getObjState(presenceDevice?.uuid))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GRAY, RoundedCornerShape(8.dp))
            .padding(20.dp)
    ) {
        Text(
            text = presence_device_string,
            fontSize = Headline6.sp,
            color = Color.Black,
            fontFamily = Roboto
        )
        Text(
            text = if (currentState != null) { if (currentState!!.presenceMultiZoneEvt[1] == 1) presence_detected else presence_undetected } else {
                if (presenceDevice != null) presence_undetected else no_available_device_string
            },
            fontSize = Subtitile2.sp,
            color = Color.Black,
            fontFamily = Roboto
        )
    }
}

@Composable
fun PresenceLogViewHolder() {

}