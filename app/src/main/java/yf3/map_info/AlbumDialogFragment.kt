package yf3.map_info

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
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
        var builder = AlertDialog.Builder(requireContext()).setView(layoutInflater.inflate(R.layout.fragment_album_dialog, null))
        var view = layoutInflater.inflate(R.layout.fragment_album_dialog, null)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setContent {
                hello_compose(photoPath)
            }
        }
        return builder.setView(view).create();
    }

    @Composable
    fun hello_compose(photo_path: String?) {
        Text("Photo Path: $photo_path")
    }
}