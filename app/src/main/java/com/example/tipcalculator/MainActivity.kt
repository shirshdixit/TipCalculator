package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun EditNumberField(
    @StringRes label : Int,
    value : String,
    onvalueChange : (String) -> (Unit),
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions
){
    TextField(
        value = value,
        singleLine = true,
        onValueChange = onvalueChange,
        modifier = modifier,
        label = { Text(stringResource(id = label))},
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun RoundTheTipRow (
    roundUp : Boolean,
    onroundUp : (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(
            checked = roundUp,
            onCheckedChange = onroundUp
        )
    }
}

private fun calculateTip( amount : Double, tipPercent : Double, roundup : Boolean) : String{
    var tip = (amount*tipPercent)/100
    if(roundup){
        tip = kotlin.math.ceil(tip)
    }

    return NumberFormat.getCurrencyInstance().format(tip)
}

@Composable
fun TipTimeLayout(){
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    var amount = amountInput.toDoubleOrNull()?:0.0
    var tipPercent = tipInput.toDoubleOrNull()?:0.0
    var tip = calculateTip(amount,tipPercent,roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(Alignment.Start)
            )
        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            onvalueChange = {amountInput = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.how_was_the_service,
            value = tipInput,
            onvalueChange = {tipInput = it},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(
            roundUp = roundUp,
            onroundUp = {roundUp = it},
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview(){
    TipCalculatorTheme {
        TipTimeLayout()
    }
}