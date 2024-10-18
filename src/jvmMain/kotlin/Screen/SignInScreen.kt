package Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rogo.iot.module.cloudapi.auth.callback.AuthRequestCallback
import rogo.iot.module.rogocore.sdk.SmartSdk
import ui.theme.BLUE
import ui.theme.DARK_BLUE
import ui.theme.Headline4
import ui.theme.Headline6
import ui.theme.LIGHT_BLUE
import ui.theme.Roboto
import ui.theme.RogoButton
import ui.theme.RogoOutlinedTextField
import ui.theme.RogoOutlinedTextFieldWithHintAbove
import ui.theme.RogoSpace
import ui.theme.Subtitile2
import ui.theme.dialogLoading
import ui.theme.password_string
import ui.theme.sign_in
import ui.theme.signing_in
import ui.theme.support_key
import ui.theme.username_string

@Composable
fun signInScreen(onSignInSuccess: () -> Unit) {
//    var email by remember {
//        mutableStateOf("rgworkspace")
//    }
    var email by remember {
        mutableStateOf("tungrogo24@gmail.com")
    }
    var password by remember {
        mutableStateOf("123456")
    }
    var supportKey by remember {
        mutableStateOf("")
    }
    val toShowDialogLoading = remember {
        mutableStateOf(false)
    }
    var loadingMessage by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(LIGHT_BLUE)
                .padding(56.dp, 72.dp)
                .fillMaxHeight()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(27.dp),
                        tint = DARK_BLUE
                    )
                    RogoSpace(7)
                    Text(
                        text = "ROGO solutions",
                        fontSize = Headline6.sp,
                        color = Color.Black,
                        fontFamily = Roboto
                    )
                }
                RogoSpace(72)
                Text(
                    text = "Rogo \nU Lesa",
                    fontSize = Headline4.sp,
                    color = DARK_BLUE,
                    fontFamily = Roboto,
                )
            }
            Text(
                text = "© 2023 - 2024\n" +
                        "https://rogo.com.vn\n" +
                        "All Rights Reserved",
                fontSize = Subtitile2.sp,
                color = BLUE,
                fontFamily = Roboto,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
        Column(
            modifier = Modifier
                .weight(0.8f)
                .align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = sign_in,
                fontSize = Headline4.sp,
                color = Color.Black,
                fontFamily = Roboto,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(34.dp))
            RogoSpace(34)
            Row (
                modifier = Modifier.width(554.dp)
            ) {
                RogoOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    hint = username_string,
                    onValueChange = {
                        email = it
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))

                RogoOutlinedTextField (
                    modifier = Modifier.weight(1f),
                    hint = password_string,
                    onValueChange = {
                        password = it
                    }
                )
            }
            Spacer(modifier = Modifier.size(34.dp))
            RogoOutlinedTextFieldWithHintAbove(
                hint = support_key,
                onValueChange = {
                    supportKey = it
                }
            )
            RogoSpace(34)
            RogoButton(
                text = "Sign in",
                backgroundColor = BLUE,
                textColor = Color.White,
                isUppercase = true,
                cornerRadius = 8,
                onClick = {
                    toShowDialogLoading.value = true
                    loadingMessage = signing_in
                    SmartSdk.signIn(null, email, null, "123456", object :
                        AuthRequestCallback {
                        override fun onSuccess() {
                            onSignInSuccess.invoke()
                            println("sign in success")
                        }

                        override fun onFailure(p0: Int, p1: String?) {
                            println("sign in failure $p0 $p1")
                            loadingMessage = "signed in $p0 $p1"
                        }
                    })
                })
            RogoSpace(8)
            dialogLoading(loadingMessage, toShowDialogLoading)
        }
    }
}