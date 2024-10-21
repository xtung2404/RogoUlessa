package Screen

import Object.GeneralFunction
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import ui.theme.BACKGROUND_COLOR
import ui.theme.GRAY
import ui.theme.LIGHT_GRAY
import ui.theme.LIGHT_PINK_COLOR
import ui.theme.ORANGE_COLOR
import ui.theme.Roboto
import ui.theme.RogoSpace
import ui.theme.Subtitile1
import ui.theme.account_string
import ui.theme.sign_out_string
@Composable
fun dashboardScreen(onSignOut: () -> Unit) {
    val currentFunc = remember {
        mutableStateOf(GeneralFunction.OVERVIEW)
    }
    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND_COLOR)
    ) {
        Column (
            modifier = Modifier
                .width(72.dp)
                .fillMaxHeight()
                .background(LIGHT_PINK_COLOR)
        ) {
            RogoSpace(87)
            Column (
                modifier = Modifier.weight(1f)
            ) {
                generalFunctionViewHolder(currentFunc)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        SmartSdk.signOut(object : AuthRequestCallback {
                            override fun onSuccess() {
                                onSignOut.invoke()
                            }

                            override fun onFailure(p0: Int, p1: String?) {

                            }
                        })
                    }
            ) {
                Text(
                    text = sign_out_string,
                    fontSize = Subtitile1.sp,
                    fontFamily = Roboto,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            ) {
                when (currentFunc.value) {
                    GeneralFunction.OVERVIEW -> overviewScreen()
                    GeneralFunction.ALLDEVICES -> allDevicesScreen()
                }
            }
        }

    }
}

@Composable
fun generalFunctionViewHolder(currentFunc: MutableState<GeneralFunction>) {
    val generalFunctionList = remember {
        mutableStateMapOf<GeneralFunction, Boolean>().apply {
            GeneralFunction.getGeneralFuncs().forEach { function ->
                this[function] = function == currentFunc.value
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(generalFunctionList.entries.toList()) { entry ->
            generalFunctionItem(entry) { selectedFunc ->
                currentFunc.value = selectedFunc
                generalFunctionList.forEach { (key, _) ->
                    generalFunctionList[key] = (selectedFunc == key)
                }
            }
        }
    }
}
@Composable
fun generalFunctionItem(entry: Map.Entry<GeneralFunction, Boolean>, onItemClick: (GeneralFunction) -> Unit) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        RogoSpace(4)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onItemClick.invoke(entry.key)
                }
        ) {
            Icon(
                Icons.Filled.Home,
                "",
                tint = if (entry.value) ORANGE_COLOR else GRAY,
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .size(18.dp)
                    .align(Alignment.Center)
            )
        }
        RogoSpace(4)
    }
}