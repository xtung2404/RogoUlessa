package Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.theme.RogoSpace

@Composable
fun dashboardScreen() {
    Row (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .width(166.dp)
                .fillMaxHeight()
        ) {
            RogoSpace(87)

        }
    }
}

@Composable
fun generalFunctionViewHolder() {
    LazyColumn (

    ) {

    }
}

@Composable
fun generalFunctionItem() {
    Column {

    }
}