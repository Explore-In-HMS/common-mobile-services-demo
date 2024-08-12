package huawei.cmsdemo.main.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import huawei.cmsdemo.main.R

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showAlertDialog(title: String, desc: String, versions: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder
        .setMessage(desc + "\n\n" + versions)
        .setTitle(title)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.cancel()
        }

    val dialog: AlertDialog = builder.create()
    dialog.show()
}