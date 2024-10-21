package Screen


import Object.AcFanMode
import Object.AcModeItem
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
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
import ui.theme.Button
import ui.theme.Caption
import ui.theme.GRAY
import ui.theme.HINT_COLOR
import ui.theme.Headline4
import ui.theme.Headline5
import ui.theme.Headline6
import ui.theme.LIGHT_PINK_COLOR
import ui.theme.ORANGE_COLOR
import ui.theme.ORANGE_DISABLED_COLOR
import ui.theme.Roboto
import ui.theme.Roboto_Medium
import ui.theme.RogoSpace
import ui.theme.SEAWEED_COLOR
import ui.theme.Subtitile1
import ui.theme.Subtitile2
import ui.theme.WHITE_COLOR
import ui.theme.ac_control_string
import ui.theme.add_group_string
import ui.theme.choose_house_to_control_string
import ui.theme.fan_mode_string
import ui.theme.general_control_string
import ui.theme.group_list_string
import ui.theme.humid_string
import ui.theme.light_control_string
import ui.theme.no_available_device_string
import ui.theme.off_string
import ui.theme.on_string
import ui.theme.overview
import ui.theme.presence_detected
import ui.theme.presence_device_string
import ui.theme.presence_undetected
import ui.theme.temp_and_humid_sensor_string
import ui.theme.temperature_string
import java.util.Locale

@Composable
fun overviewScreen() {
    SmartSdk.setAppLocation(SmartSdk.locationHandler().all.first().uuid)
    val currentGroup = remember {
        mutableStateOf<IoTGroup?>(SmartSdk.groupHandler().all.first())
    }
    val groupList = remember {
        mutableStateMapOf<IoTGroup, Boolean>().apply {
            SmartSdk.groupHandler().all.toList().forEach { ioTGroup ->
                this[ioTGroup] = currentGroup.value == ioTGroup
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND_COLOR)
    ) {
        Text(
            text = general_control_string,
            fontSize = Headline5.sp,
            fontFamily = Roboto,
            color = SEAWEED_COLOR,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        RogoSpace(24)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            GroupManagementSection(groupList)
        }
        RogoSpace(24)
        Column(
            modifier = Modifier.weight(0.6f)
        ) {
            GroupDetailSection(groupList.filter {
                it.value
            }.keys.toMutableStateList())
        }
    }
}

@Composable
fun GroupManagementSection(groupList: MutableMap<IoTGroup, Boolean>) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(20.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = group_list_string,
                    fontSize = Headline6.sp,
                    color = SEAWEED_COLOR,
                    fontFamily = Roboto
                )
                RogoSpace(8)
                Text(
                    text = choose_house_to_control_string,
                    fontSize = Caption.sp,
                    color = HINT_COLOR,
                    fontFamily = Roboto
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {

                }.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    "",
                    tint = ORANGE_COLOR,
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    text = add_group_string,
                    fontSize = Subtitile2.sp,
                    color = ORANGE_COLOR,
                    fontFamily = Roboto
                )
            }

        }
        RogoSpace(24)
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .border(0.5.dp, GRAY, RoundedCornerShape(8.dp))
        ) {
            GroupViewHolder(groupList)
        }
    }
}
@Composable
fun GroupViewHolder(groupList: MutableMap<IoTGroup, Boolean>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(groupList.entries.sortedBy { it.key.label }.chunked(5)) { rowGroups ->
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (ioTGroup in rowGroups) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentHeight()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    groupList[ioTGroup.key] = !ioTGroup.value
                                }
                        ) {
                            groupItem(ioTGroup) { ioTGroup, isChecked ->
                                groupList[ioTGroup] = isChecked
                            }
                        }
                    }
                    if (rowGroups.size < 5) {
                        Spacer(modifier = Modifier.weight((5 - rowGroups.size).toFloat()))
                    }
                }
                Spacer(modifier = Modifier.height(0.5.dp).fillMaxWidth().background(GRAY))
            }
        }
    }
}

@Composable
fun groupItem(ioTGroup: Map.Entry<IoTGroup, Boolean>, onItemChecked: (IoTGroup, Boolean) -> Unit) {
    var isChecked = ioTGroup.value
    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onItemChecked.invoke(ioTGroup.key, isChecked)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = ORANGE_COLOR,
                uncheckedColor = Color.White,
                checkmarkColor = Color.White
            ),
            modifier = Modifier.size(24.dp))
        RogoSpace(8)
        Text(
            text = ioTGroup.key.label.toUpperCase(Locale.getDefault()),
            fontSize = Subtitile2.sp,
            color = Color.Black,
            fontFamily = Roboto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(500)
        )
    }
}

@Composable
fun GroupDetailSection(currentGroupList: MutableList<IoTGroup>) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(currentGroupList) {
            GroupDetailItem(it)
        }
    }
}
@Composable
fun GroupDetailItem(ioTGroup: IoTGroup) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(20.dp),
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = null,
                enabled = false,
                colors = CheckboxDefaults.colors(
                    checkedColor = ORANGE_DISABLED_COLOR,
                    uncheckedColor = Color.White,
                    checkmarkColor = Color.White,
                    disabledColor = ORANGE_DISABLED_COLOR
                ),
                modifier = Modifier.size(24.dp))
            RogoSpace(8)
            Text(
                text = ioTGroup.label ?: "",
                color = SEAWEED_COLOR,
                fontFamily = Roboto,
                fontSize = Headline6.sp
            )
        }
        RogoSpace(24)
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                ControlDeviceSection(IoTDeviceType.AC, ioTGroup.uuid)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                ControlDeviceSection(IoTDeviceType.LIGHT, ioTGroup.uuid)
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
                    TempHumidInfoSection(ioTGroup.uuid)
                }
                RogoSpace(18)
                Box(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(bottom = 4.dp)
                ) {
                    PresenceInfoSection(ioTGroup.uuid)
                }
            }
        }
    }
}
@Composable
fun ControlDeviceSection(deviceType: Int, currentGroupUuid: String?) {
    val currentDeviceList = remember { mutableStateListOf<IoTDevice>() }
    val currentDevice = remember {
        mutableStateOf<IoTDevice?>(null)
    }
    LaunchedEffect(deviceType, currentGroupUuid) {
        val newDeviceList = SmartSdk.deviceHandler().all.filter {
            it.groupId == currentGroupUuid && it.devType == deviceType
        }
        currentDeviceList.clear()
        currentDeviceList.addAll(newDeviceList)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BACKGROUND_COLOR, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (currentDevice.value == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (deviceType != IoTDeviceType.LIGHT) ac_control_string else light_control_string,
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontSize = Subtitile1.sp,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ControlDeviceViewHolder(currentDeviceList, currentDevice)
            }
        } else {
            when (currentDevice.value?.devType) {
                IoTDeviceType.AC -> {
                    ControlACSection(currentDevice.value, onNavBack = {
                        currentDevice.value = null
                    })
                }
                IoTDeviceType.LIGHT -> {
                    ControlLightSection(currentDevice.value, onNavBack = {
                        currentDevice.value = null
                    })
                }
            }
        }
    }
}
@Composable
fun ControlDeviceViewHolder(currentDeviceList: List<IoTDevice>, currentDevice: MutableState<IoTDevice?>) {
    ILogR.D("OverviewScreen", "CURRENT_LIST", currentDeviceList.size)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        for (device in currentDeviceList) {
            DeviceControlItem(device) {
                currentDevice.value = it
            }
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
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onItemClick.invoke(ioTDevice)
            }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ioTDevice.label,
            fontSize = Subtitile2.sp,
            color = Color.Black,
            fontFamily = Roboto,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (ioTDevice.devType != IoTDeviceType.LIGHT) {
            deviceState?.let { state ->
                Text(
                    text = "${state.temp}°C",
                    fontSize = Headline6.sp,
                    color = if (isOn) ORANGE_COLOR else ORANGE_DISABLED_COLOR,
                    fontFamily = Roboto
                )
            }
            RogoSpace(12)
        }
        Switch(
            isOn,
            onCheckedChange = {
                isOn = it
                SmartSdk.controlHandler().controlDevicePower(ioTDevice.uuid, ioTDevice.elementIds, isOn, null)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ORANGE_COLOR,
                checkedTrackColor = ORANGE_DISABLED_COLOR
            )
        )
    }
}
@Composable
fun ControlACSection(currentDevice: IoTDevice?, onNavBack: () -> Unit) {
    val acModeList = remember {
        mutableStateMapOf<AcModeItem, Boolean>().apply {
            AcModeItem.getACModeList().forEach {
                this[it] = false
            }
        }
    }
    val acFanModeList = remember {
        mutableStateMapOf<AcFanMode, Boolean>().apply {
            AcFanMode.getAcFanModeList().forEach {
                this[it] = false
            }
        }
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
           Icon(
               Icons.Default.ArrowBack,
               "",
               tint = Color.Black,
               modifier = Modifier
                   .size(24.dp)
                   .clickable {
                       onNavBack.invoke()
                   }
           )
            RogoSpace(4)
           Text(
               text = currentDevice?.label ?:"",
               color = Color.Black,
               fontFamily = Roboto,
               fontSize = Subtitile1.sp
           )
        }
        RogoSpace(24)
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(0.5.dp, BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Add,
                    "",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                        },
                    tint = Color.Black
                )
                RogoSpace(24)
                Text(
                    text = "24",
                    fontSize = Headline4.sp,
                    fontFamily = Roboto,
                    color = ORANGE_COLOR
                )
                RogoSpace(24)
                Icon(
                    Icons.Default.Add,
                    "",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                        },
                    tint = Color.Black
                )
            }
            RogoSpace(8)
            Icon(
                Icons.Default.Settings,
                "",
                modifier = Modifier
                    .background(ORANGE_COLOR, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 22.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp)),
                tint = Color.White
            )
        }
        RogoSpace(24)
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ACModeViewHolder(acModeList)
        }
        RogoSpace(4)
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Text(
                text = fan_mode_string,
                fontFamily = Roboto,
                color = Color.Black,
                fontSize = Button.sp
            )
            RogoSpace(8)
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ACFanModeViewHolder(acFanModeList)
            }
        }
    }
}
@Composable
fun ACModeViewHolder(acModeList: MutableMap<AcModeItem, Boolean>) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        for (mode in acModeList.entries.sortedBy { it.key.cmd }) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .background(if (mode.value) Color.White else BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {

                    }
            ){
                Screen.AcModeItem(mode)
                RogoSpace(4)
            }
        }
    }
}
@Composable
fun AcModeItem(acModeItem: MutableMap.MutableEntry<AcModeItem, Boolean>) {
    Text(
        text = acModeItem.key.cmdName,
        color = Color.Black,
        fontFamily = Roboto,
        fontSize = Button.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textAlign = TextAlign.Center
    )
}
@Composable
fun ACFanModeViewHolder(acFanModeList: MutableMap<AcFanMode, Boolean>) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        for (fanMode in acFanModeList.entries.sortedBy { it.key.cmd }) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .background(if (fanMode.value) LIGHT_PINK_COLOR else BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {

                    }
            ){
                AcFanModeItem(fanMode)
                RogoSpace(8)
            }
        }
    }
}
@Composable
fun AcFanModeItem(acFanModeItem: MutableMap.MutableEntry<AcFanMode, Boolean>) {
    Text(
        text = acFanModeItem.key.cmdName,
        color = Color.Black,
        fontFamily = Roboto,
        fontSize = Button.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}
@Composable
fun ControlLightSection(currentDevice: IoTDevice?, onNavBack: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ArrowBack,
                "",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onNavBack.invoke()
                    }
            )
            RogoSpace(4)
            Text(
                text = currentDevice?.label ?:"",
                color = Color.Black,
                fontFamily = Roboto,
                fontSize = Subtitile1.sp
            )
        }
        RogoSpace(24)
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(0.5.dp, BACKGROUND_COLOR, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
            RogoSpace(8)
            Icon(
                Icons.Default.Settings,
                "",
                modifier = Modifier
                    .background(ORANGE_COLOR, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 22.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp)),
                tint = Color.White
            )
        }
        RainbowColorSlider()
    }
}
@Composable
fun RainbowColorSlider() {
    var sliderPosition by remember { mutableStateOf(0f) }

    val rainbowColors = listOf(
        Color.Red,
        Color(0xFFFFA500), // Orange
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color(0xFF4B0082), // Indigo
        Color(0xFF8A2BE2)  // Violet
    )
    val selectedColor = Brush.linearGradient(rainbowColors)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = getThumbColor(sliderPosition, rainbowColors),
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(selectedColor, RoundedCornerShape(8.dp)),

        )

    }
}

// Hàm để lấy màu thumb dựa trên vị trí slider
private fun getThumbColor(position: Float, colors: List<Color>): Color {
    val index = (position * (colors.size - 1)).toInt()
    val nextIndex = if (index < colors.size - 1) index + 1 else index
    val fraction = position * (colors.size - 1) - index
    return lerp(colors[index], colors[nextIndex], fraction)
}
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
            .background(BACKGROUND_COLOR, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(
            text = temp_and_humid_sensor_string,
            fontSize = Subtitile1.sp,
            color = Color.Black,
            fontFamily = Roboto,
            modifier = Modifier.weight(1f)
        )
        RogoSpace(24)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = temperature_string,
                fontSize = Subtitile2.sp,
                color = Color.Black,
                fontFamily = Roboto,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${deviceState?.temp ?: 0}°C",
                fontSize = Headline6.sp,
                color = ORANGE_COLOR,
                fontFamily = Roboto,
                textAlign = TextAlign.Center
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
                fontSize = Subtitile2.sp,
                color = Color.Black,
                fontFamily = Roboto,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${deviceState?.humid ?: 0}%",
                fontSize = Headline6.sp,
                color = ORANGE_COLOR,
                fontFamily = Roboto,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PresenceInfoSection(currentGroupUuid: String?) {
    val presenceDevice by remember {
        mutableStateOf<IoTDevice?>(SmartSdk.deviceHandler().all.filter {
            it.devType == IoTDeviceType.PRESENSCE_SENSOR && it.groupId == currentGroupUuid
        }.firstOrNull())
    }
    val currentState by remember {
        mutableStateOf(SmartSdk.stateHandler().getObjState(presenceDevice?.uuid))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND_COLOR, RoundedCornerShape(8.dp))
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