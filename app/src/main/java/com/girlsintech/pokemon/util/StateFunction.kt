package com.girlsintech.pokemon.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.screens.fontBasic

@Composable
fun ErrorMessage(message: String) {
    ConstraintLayout {
        val msg = createRef()

        Text(
            text = message, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
                .constrainAs(msg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            fontSize = 16.sp
        )
    }
}

@Composable
fun Loading() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pokeball_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    ConstraintLayout {
        val (pi, wp) = createRefs()
        LottieAnimation(modifier = Modifier
            .size(50.dp)
            .constrainAs(pi) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            composition = composition, progress = {progress})
        Text(
            text = stringResource(id = R.string.loading),
            modifier = Modifier
                .constrainAs(wp) {
                    top.linkTo(pi.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            fontFamily = fontBasic()
        )
    }
}

@Composable
fun LoadingGift() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gift_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.5f
    )
    ConstraintLayout {
        val (pi) = createRefs()
        LottieAnimation(modifier = Modifier
            .size(300.dp)
            .constrainAs(pi) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            composition = composition, progress = {progress}
        )
    }
}
