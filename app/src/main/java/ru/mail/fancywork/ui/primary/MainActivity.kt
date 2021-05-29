package ru.mail.fancywork.ui.primary

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.mail.fancywork.R
import ru.mail.fancywork.controller.Controller
import ru.mail.fancywork.model.repo.PixelizationRepository
import ru.mail.fancywork.ui.auth.AuthActivity
import ru.mail.fancywork.ui.view.ColorGridView

class MainActivity : AppCompatActivity() {

    private val controller = Controller()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.top_bar)
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        controller.logOut()
                        startActivity(Intent(this, AuthActivity::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val inputStream = this.applicationContext.contentResolver.openInputStream(data.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            Log.v(TAG, "starting analysis")
            val repo = PixelizationRepository()
            val colors = repo.getThreadColors(resources)
            val pair = repo.getPixelsFromImage(bitmap, 10, colors)
            val bitmap2 = pair.first
            Log.v(TAG, "ending analysis")

            Toast.makeText(
                this.applicationContext,
                "вы загрузили битмап ${bitmap2.width}x${bitmap2.height}!",
                Toast.LENGTH_LONG
            ).show()

            findViewById<ColorGridView>(R.id.color_grid_view).setImage(bitmap2)
        }
    }

    fun testButton(view: View) {
//        findViewById<ColorGridView>(R.id.color_grid_view).setImage(
//            Bitmap.createBitmap(
//                intArrayOf(
//                    Color.rgb(0,0,0), Color.rgb(255,0,0),
//                    Color.rgb(255,255,0), Color.rgb(255,255,255)
//                ),
//                2,
//                2,
//                Bitmap.Config.ARGB_8888
//            )
//        )
//
//        return

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE
        )
    }

    // todo google auth
    public fun loginGoogle(view: View) {
        // БОРЕМСЯ С КОДСТАЙЛОМ
    }

    // todo local auth
    public fun loginWithoutGoogle(view: View) {
        // todo open recycle view activity
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PICK_IMAGE = 122
    }
}
