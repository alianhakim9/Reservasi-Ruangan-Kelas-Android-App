package id.alian.reservasikelas.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.alian.reservasikelas.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, PilihanLoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}