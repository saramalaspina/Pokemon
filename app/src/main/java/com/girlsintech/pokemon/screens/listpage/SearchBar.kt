package com.girlsintech.pokemon.screens.listpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.ui.theme.CardBackground

//component che permette di effettuare la ricerca in base al nome del Pokemon digitato
@Composable
fun SearchBar(
    filter: String,
    onValueChange: (String) -> Unit
){
    var isHintDisplayed by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .background(CardBackground, CircleShape)
            .clip(RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        //in un text field si inserisce il nome
        BasicTextField(
            value = filter,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 45.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )

        Icon(
            imageVector = Icons.TwoTone.Search,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        if (isHintDisplayed) {
            Text(
                text = stringResource(id = R.string.search),
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 12.dp),
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = fontBasic()
            )
        }
    }
}