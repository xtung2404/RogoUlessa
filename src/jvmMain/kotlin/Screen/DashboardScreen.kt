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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import ui.theme.GRAY
import ui.theme.LIGHT_GRAY
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
            .background(Color.White)
    ) {
        Column (
            modifier = Modifier
                .width(166.dp)
                .fillMaxHeight()
                .background(GRAY)
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
                    .fillMaxWidth()
                    .background(LIGHT_GRAY)
            ) {
                Row (
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .align(Alignment.End)
                        .padding(end = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = account_string,
                        fontSize = Subtitile1.sp,
                        color = Color.Black,
                        fontFamily = Roboto
                    )
                    RogoSpace(4)
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(40.dp)
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(GRAY)
            )
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
            .padding(16.dp)
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
                .background(
                    if (entry.value) Color.Black else Color.White,
                    RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onItemClick.invoke(entry.key)
                }
        ) {
            Text(
                text = entry.key.label,
                fontSize = Subtitile1.sp,
                fontFamily = Roboto,
                color = if (entry.value) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 8.dp)
            )
        }
        RogoSpace(4)
    }
}