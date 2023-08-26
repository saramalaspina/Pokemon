package com.girlsintech.pokemon.screens.listpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.twotone.FilterList
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.model.Ability
import com.girlsintech.pokemon.data.remote.model.ListOfAbilities
import com.girlsintech.pokemon.data.remote.model.SingletonListOfAbilities
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.TextInfo
import com.girlsintech.pokemon.util.parseTypeIt
import java.util.*

//menu dei filtri applicabili alla ricerca di un Pokémon
@Composable
fun FilterDialog(
    initAbilityEn: String,
    initAbilityIt: String,
    initType: String,
    initGen: String,
    onDismiss: () -> Unit,
    onClickType: (String) -> Unit,
    onClickGeneration: (String) -> Unit,
    onClickAbility: (String, String) -> Unit
) {

    val noneSelection = stringResource(id = R.string.none_m)

    var typeSelection by remember {
        mutableStateOf(initType)
    }

    var generationSelection by remember {
        mutableStateOf(initGen)
    }

    var abilitySelectionEn by remember {
        mutableStateOf(initAbilityEn)
    }

    var abilitySelectionIt by remember {
        mutableStateOf(initAbilityIt)
    }

    val types = listOf(
        stringResource(id = R.string.none_m),
        stringResource(id = R.string.normal),
        stringResource(id = R.string.fire),
        stringResource(id = R.string.water),
        stringResource(id = R.string.grass),
        stringResource(id = R.string.flying),
        stringResource(id = R.string.fighting),
        stringResource(id = R.string.poison),
        stringResource(id = R.string.electric),
        stringResource(id = R.string.ground),
        stringResource(id = R.string.rock),
        stringResource(id = R.string.psychic),
        stringResource(id = R.string.ice),
        stringResource(id = R.string.bug),
        stringResource(id = R.string.ghost),
        stringResource(id = R.string.steel),
        stringResource(id = R.string.dragon),
        stringResource(id = R.string.dark),
        stringResource(id = R.string.fairy)
    )

    val generations = listOf(
        stringResource(id = R.string.none_m),
        "I",
        "II",
        "III",
        "IV",
        "V",
        "VI",
        "VII",
        "VIII",
        "IX"
    )

    //la lista delle abilità viene ricavata dal file abilities.csv mantenuto nel folder assest
    //e ottenuto tramite il metodo getInstance
    val listOfAbilities = SingletonListOfAbilities.getInstance(LocalContext.current)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .height(400.dp)
                .width(330.dp)
                .shadow(10.dp, RoundedCornerShape(10.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.FilterList,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.selection),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = fontBasic()
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                //invocazione filtro per selezionare l'abilità
                AbilitySelection(
                    initAbility = if (Locale.getDefault().language == "en") {
                        abilitySelectionEn
                    } else {
                        abilitySelectionIt
                    },
                    listOfAbilities = listOfAbilities
                ) {
                    abilitySelectionEn = it.en
                    abilitySelectionIt = it.it
                }

                Spacer(modifier = Modifier.height(20.dp))

                //invocazione filtro per selezionare la generazione
                FilterSelection(
                    itemList = generations,
                    currentSelection = generationSelection,
                    selectionString = stringResource(id = R.string.selection_generation)
                ) {
                    generationSelection = if (it == noneSelection) {
                        ""
                    } else {
                        it
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                //invocazione filtro per selezionare il tipo
                FilterSelection(
                    itemList = types,
                    currentSelection = typeSelection,
                    selectionString = stringResource(id = R.string.selection_type)
                ) {
                    typeSelection = if (it == noneSelection) {
                        ""
                    } else {
                        it
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = CardBackground),
                    modifier = Modifier
                        .width(160.dp),
                    onClick = {
                        if (Locale.getDefault().language == "en") {
                            onClickType(typeSelection)
                        } else {
                            onClickType(parseTypeIt(typeSelection))
                        }
                        onClickGeneration(generationSelection)
                        onClickAbility(abilitySelectionEn, abilitySelectionIt)
                        onDismiss()
                    }
                ) {
                    TextInfo(
                        text = stringResource(id = R.string.apply_filters),
                        Color.Black
                    )
                }
            }
        }
    }
}

//filtro per selezionare l'abilità
@Composable
fun AbilitySelection(
    initAbility: String,
    listOfAbilities: ListOfAbilities,
    onClickAbility: (Ability) -> Unit
) {
    val options = listOfAbilities.abilities

    var exp by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var selection by remember {
        mutableStateOf(initAbility)
    }

    val icon = if (exp)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = Modifier.width(205.dp)) {
        //è presente un text field per cercare velocemente un'abilità specifica da voler selezionare
        OutlinedTextField(
            value = selection,
            onValueChange = {
                selection = it
                exp = true
            },
            modifier = Modifier
                .width(205.dp)
                .height(50.dp)
                .shadow(5.dp, spotColor = CardBackground),
            enabled = true,
            keyboardActions = KeyboardActions { },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done   //tasto di spunta
            ),
            singleLine = true,

            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = fontBasic(),
                textAlign = TextAlign.Start,
                fontSize = 15.sp
            ),
            shape = RoundedCornerShape(5.dp),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                disabledBorderColor = Color.LightGray
            ),

            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = BlackLight,
                    modifier = Modifier.clickable {
                        exp = !exp
                    }
                )
            },

            placeholder = {
                Text(
                    text = selection.ifBlank {
                        stringResource(id = R.string.search_ability)
                    },
                    fontSize = 15.sp,
                    fontFamily = fontBasic(),
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        )

        Card(elevation = if (exp) 5.dp else 0.dp) {
            //colonne che non creano tutte le righe ma solo quelle visibili, in base all'input del text field
            LazyColumn(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 5.dp)
            ) {
                val filterOpts = options.filter {
                    if (Locale.getDefault().language == "en") {
                        it.en.startsWith(
                            selection,
                            ignoreCase = true  //evita il problema delle maiuscole e delle minuscole
                        )
                    } else {
                        it.it.startsWith(
                            selection,
                            ignoreCase = true
                        )
                    }
                }

                if (exp) {
                    //lista di LazyColumn a cui passo la lista di oggetti
                    itemsIndexed(filterOpts) { _, item ->
                        Text(
                            text = if (Locale.getDefault().language == "en") {
                                item.en
                            } else {
                                item.it
                            },
                            fontFamily = fontBasic(),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(onClick = {
                                    selection = if (item.id == "0") {
                                        ""
                                    } else {
                                        if (Locale.getDefault().language == "en") {
                                            item.en
                                        } else {
                                            item.it
                                        }
                                    }
                                    onClickAbility(item)
                                    exp = false //una volta cliccato la lista deve sparire
                                    focusManager.clearFocus()
                                })
                        )
                    }
                }
            }
        }
    }
}

//component che viene usata per selezione sia il tipo che la generazione all'interno del menù dei filtri
@Composable
fun FilterSelection(
    itemList: List<String>,
    currentSelection: String,
    selectionString: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var selectedItem by rememberSaveable { mutableStateOf(currentSelection) }

    val noneSelection = stringResource(id = R.string.none_m)

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .onGloballyPositioned { coordinates ->

                    textFieldSize = coordinates.size.toSize()
                }
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(5.dp))
                .height(50.dp)
        ) {
            Text(
                text =
                if (selectedItem == "") {
                    selectionString
                } else {
                    selectedItem
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.width(150.dp),
                fontFamily = fontBasic(),
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
            )
            Icon(icon, contentDescription = null, tint = BlackLight)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                    //il dropdown menu deve avere la stessa larghezza del bottone
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .height(with(LocalDensity.current) { textFieldSize.height.toDp() + 255.dp })
        ) {
            itemList.forEach {
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedItem = if(it == noneSelection){
                        ""
                    } else {
                        it
                    }
                    onItemSelected(it)
                }) {
                    Text(text = it)
                }
            }
        }
    }
}
