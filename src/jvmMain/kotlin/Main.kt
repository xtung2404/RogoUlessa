// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import rogo.iot.module.platform.ILogR
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SmartSdkConnectCallback
import rogo.iot.module.rogoplatformmultios.CommonIoTPlatform
import rogo.iot.module.rogoplatformmultios.DesktopIoTPlatform


@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, start connect to Rogo Service!") }
    MaterialTheme {
        Button(onClick = {
            DesktopIoTPlatform(object : CommonIoTPlatform.ConstructorPlatformCallback {
                override fun onSuccess(p0: CommonIoTPlatform?) {
                    try {
                        SmartSdk().initV2(p0)
                        SmartSdk.connectService(object :SmartSdkConnectCallback{
                            override fun onConnected(p0: Boolean) {
                                ILogR.D("TEST","onConnected",p0)
                                text = "Rogo Service on connected!"
                            }

                            override fun onDisconnected() {
                                ILogR.D("TEST","Rogo Service on disconnected")
                                text = "Rogo Service on disconnected!"
                            }
                        })
                    }catch (e: Exception){
                        text = e.message.toString()
                    }

                }
            })


        }) {
            Text(text)
        }
    }
}

fun main() = application {

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
