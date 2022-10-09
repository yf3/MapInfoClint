package yf3.map_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    val openDialog = remember { mutableStateOf(false)  }
    Dialog(onDismissRequest = { /*TODO*/ }, properties = DialogProperties(dismissOnBackPress = true)) {
        Column {
            var name by remember { mutableStateOf("") }
            Text("Enough")
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            Button(onClick = { openDialog.value = false }) {
                Text("Dismiss")
            }
        }
    }
}
