package Screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.platform.ILogR
import rogo.iot.module.platform.callback.RequestCallback
import rogo.iot.module.platform.define.IoTErrorCode
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.callback.SmartSdkConnectCallback
import rogo.iot.module.rogocore.sdk.callback.SuccessStatusCallback
import rogo.iot.module.rogoplatformmultios.DesktopIoTPlatform
import java.io.File

@Composable
fun splashScreen(requestCallback: RequestCallback<Boolean>) {
    val TAG = "SplashScreen"
    val appDataPath = if (System.getProperty("os.name").startsWith("Mac")) {
        System.getProperty("user.home") + "/Library/Application Support/RogoUlesa/"
    } else {
        System.getenv("APPDATA") + "\\RogoUlesa\\"
    }
    var partner by remember {
        mutableStateOf("Rogo")
    }
    val appDataDir = File(appDataPath)
    if (!appDataDir.exists()) {
        appDataDir.mkdirs()
    }
    DesktopIoTPlatform.pathApp = appDataPath
    DesktopIoTPlatform { p0 ->
        try {
            ILogR.D(TAG, "ON_CONNECT_SERVICE", "INIT_DESKTOP")
            SmartSdk.isForceProduction = true
            when (partner) {
                "Rogo" -> {
                    SmartSdk().initV2(
                        p0,
                        "e4b75a6b23fc4f30bd5fab35436c6a90",
                        "964e2c974f001a0468bf2734ce88e96652afff328886"
                    )
                }
                "ThingEdges" -> {
                    SmartSdk().initV2(
                        p0,
                        "f07b9dc8912e44ed8b4c6e895acd02c2",
                        "731eee3c8c8ca3de3b178264b7a6a13e80d42f1f1bc1"
                    )
                }
            }
            SmartSdk.connectService(object : SmartSdkConnectCallback {
                override fun onConnected(p0: Boolean) {
                    if (p0) {
                        ILogR.D(TAG, "ON_CONNECT_SERVICE", "CONNECT_SERVICE_SUCCESS")
                        requestCallback.onSuccess(true)
                    } else {
                        ILogR.D(TAG, "ON_CONNECT_SERVICE", "SERVICE_IS_NOT_CONNECTED")
                        requestCallback.onSuccess(false)
                    }
                }
                override fun onDisconnected() {
                    ILogR.D(TAG, "ON_CONNECT_SERVICE", "ON_DISCONNECTED")
                    requestCallback.onFailure(IoTErrorCode.NOTSUPPORT, "SERVICE_IS_NOT_CONNECTED")
                }
            })
        } catch (e: Exception) {
            ILogR.D(TAG, "ON_CONNECT_SERVICE exception", e.message , e.printStackTrace())
        }
    }
}