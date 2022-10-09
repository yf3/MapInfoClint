package yf3.map_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment

class PureCompose: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply { 
            setContent {
                OpenDialog()
            }
        }
    }
}

@Composable
fun OpenDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Text("Enough")
    }
}

@Composable
fun OpenAlert() {
    val openDialog = remember { mutableStateOf(false)  }
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        confirmButton = {
            Button(

                onClick = {
                    openDialog.value = false
                }) {
                Text("This is the Confirm Button")
            }
        },
        dismissButton = {
            Button(

                onClick = {
                    openDialog.value = false
                }) {
                Text("This is the dismiss Button")
            }
        })
}

@Composable
fun Hello() {
    Text("Hello")
}