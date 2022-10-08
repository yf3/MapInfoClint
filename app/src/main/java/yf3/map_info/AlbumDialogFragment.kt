package yf3.map_info

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.DialogFragment

class AlbumDialogFragment: DialogFragment() {

    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoPath = AlbumDialogFragmentArgs.fromBundle(it).filePath
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_album_dialog, null)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HelloCompose(photoPath)
            }
        }
        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    @Composable
    fun HelloCompose(photo_path: String?) {
//        Text("Photo Path: $photo_path")
        HelloContent()
    }

    @Composable
    @Preview
    fun HelloContent() {
        Column() {
            var name by remember { mutableStateOf("") }
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
        }
    }


}