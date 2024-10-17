import Screen.dashboardScreen
import Screen.signInScreen
import Screen.splashScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import rogo.iot.module.platform.callback.RequestCallback
import ui.theme.dashboard_screen
import ui.theme.sign_in_screen
import ui.theme.splash_screen
import java.awt.Dimension
import javax.swing.SwingUtilities
@Composable
fun mainApplication() {
    var currentScreen by remember {
        mutableStateOf(splash_screen)
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .widthIn(min = 400.dp)
            .heightIn(300.dp)
    ) {
        when(currentScreen) {
            splash_screen -> splashScreen(object : RequestCallback<Boolean> {
                override fun onSuccess(isAuthenticated: Boolean) {
                    currentScreen = if (isAuthenticated) dashboard_screen else sign_in_screen
                }

                override fun onFailure(p0: Int, p1: String?) {

                }
            })
            sign_in_screen -> signInScreen (onSignInSuccess = {
                currentScreen = dashboard_screen
            })
            dashboard_screen -> dashboardScreen()
        }
    }
}
fun main() = application {
    Window(
        onCloseRequest = {
            exitApplication()
        },
        resizable = true,

        ) {
        SwingUtilities.invokeLater {
            val frame = this.window
            frame.minimumSize = Dimension(1200, 600) // Set minimum width and height
        }
        mainApplication()
    }
}
