package com.avinash.ytsmoviedownload.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.avinash.ytsmoviedownload.R


val fontFamily = FontFamily(
    listOf(
        Font(R.font.lato_black, weight = FontWeight.W900),
        Font(R.font.lato_bold, weight = FontWeight.W700),
        Font(R.font.lato_bold, weight = FontWeight.W500),
        Font(R.font.lato_regular, weight = FontWeight.W400),
        Font(R.font.lato_regular, weight = FontWeight.W300),
        Font(R.font.lato_light, weight = FontWeight.W200)
    )
)


// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, fontWeight = FontWeight.W900),
    h2 = TextStyle(fontFamily = fontFamily, fontSize = 18.sp, fontWeight = FontWeight.W700, color = Color.White),
    h3 = TextStyle(fontFamily = fontFamily, fontSize = 12.sp, fontWeight = FontWeight.W500),
    subtitle1 = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, fontWeight = FontWeight.W400),
    subtitle2 = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, fontWeight = FontWeight.W300),
    button = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, fontWeight = FontWeight.W300),
)


