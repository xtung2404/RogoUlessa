package ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.caverock.androidsvg.SVG
import java.io.InputStream
import java.util.Locale

@Composable
fun RogoOutlinedTextField(
    modifier: Modifier = Modifier.width(600.dp).height(60.dp),
    text: String? = "",
    textColor: Color = Color.Black,
    textSize: Int = 14,
    hint: String = "",
    hintColor: Color = Color.Gray,
    fontFamily: FontFamily = Roboto,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    var value by remember {
        mutableStateOf(text ?: "")
    }
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange.invoke(it)
        },
        placeholder = {
            Text(
                text = hint,
                color = hintColor,
                fontSize = textSize.sp,
                fontFamily = fontFamily
            )
        },
        textStyle = TextStyle(
            color = textColor,
            fontSize = textSize.sp,
            fontFamily = fontFamily
        ),
        singleLine = singleLine,
        modifier = modifier
    )
}

@Composable
fun RogoOutlinedTextFieldWithHintAbove(
    modifier: Modifier = Modifier.width(554.dp).height(60.dp),
    text: String? = "",
    textColor: Color = Color.Black,
    textSize: Int = 14,
    hint: String = "",
    hintSize: Int = Caption,
    cornerRadius: Int = 4,
    hintColor: Color = Color.Gray,
    fontFamily: FontFamily = Roboto,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    var value by remember {
        mutableStateOf(text ?: "")
    }
    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = null,
            shape = RoundedCornerShape(cornerRadius.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Blue
            ),
            textStyle = TextStyle(
                color = textColor,
                fontSize = textSize.sp,
                fontFamily = fontFamily
            ),
            singleLine = singleLine
        )

        Text(
            text = hint,
            style = TextStyle(
                color = hintColor,
                fontSize = hintSize.sp,
                background = Color.White
            ),
            fontFamily = Roboto,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp)
                .background(Color.White)
                .padding(horizontal = 4.dp)
                .offset(y = (-8).dp)
        )
    }
}

@Composable
fun RogoButton(
    modifier: Modifier = Modifier.width(554.dp),
    text: String = "",
    isUppercase: Boolean = false,
    fontFamily: FontFamily = Roboto,
    textColor: Color = Color.Black,
    backgroundColor: Color = BLUE,
    textSize: Int = 14,
    cornerRadius: Int = 8,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        ),
        shape = RoundedCornerShape(cornerRadius.dp)
    ) {
        Text(
            text = if (isUppercase) text.uppercase(Locale.getDefault()) else text,
            fontFamily = fontFamily,
            color = textColor,
            fontSize = textSize.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun RogoImageTextButton(
    modifier: Modifier = Modifier.width(554.dp),
    text: String = "",
    icon: ImageVector = Icons.Filled.Person,
    isUppercase: Boolean = false,
    fontFamily: FontFamily = Roboto,
    textColor: Color = Color.Black,
    backgroundColor: Color = BLUE,
    textAlign: TextAlign = TextAlign.Center,
    textSize: Int = 14,
    cornerRadius: Int = 100,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        ),
        shape = RoundedCornerShape(cornerRadius.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = textColor
            )
            if (textAlign != TextAlign.Center) RogoSpace(8)
            Text(
                text = if (isUppercase) text.uppercase(Locale.getDefault()) else text,
                fontFamily = fontFamily,
                color = textColor,
                fontSize = textSize.sp,
                textAlign = textAlign,
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
            )
        }
    }
}
@Composable
fun RogoBackButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        modifier = Modifier
            .size(40.dp)
            .background(Color.Black, CircleShape)
            .clickable {
                onClick.invoke()
            }
            .padding(8.dp),
        tint = Color.White,
        contentDescription = null
    )
}
@Composable
fun RogoSpace(size: Int) {
    Spacer(modifier = Modifier.size(size.dp))
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dialogLoading(message: String, toShow: MutableState<Boolean>) {
    val clipboardManager = LocalClipboardManager.current
    if (toShow.value) {
        AlertDialog(
            onDismissRequest = { },
            text = {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .widthIn(min = 300.dp)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(message))
                                toShow.value = false
                            },
                        tint = Color.Black
                    )
                    RogoSpace(8)
                    Text(
                        text = message,
                        fontFamily = Roboto,
                        fontSize = Subtitile2.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(500)
                    )
                }
            },
            confirmButton = {

            }
        )
    }
}