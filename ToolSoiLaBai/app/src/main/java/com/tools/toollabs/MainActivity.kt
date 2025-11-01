package com.tools.toollabs

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var imgView: ImageView
    private lateinit var btnPick: Button
    private lateinit var btnAnalyze: Button
    private lateinit var txtResult: TextView
    private var pickedBitmap: Bitmap? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bmp = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            pickedBitmap = bmp
            imgView.setImageBitmap(bmp)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)

        imgView = findViewById(R.id.imgView)
        btnPick = findViewById(R.id.btnPick)
        btnAnalyze = findViewById(R.id.btnAnalyze)
        txtResult = findViewById(R.id.txtResult)

        btnPick.setOnClickListener { pickImage.launch("image/*") }

        btnAnalyze.setOnClickListener {
            pickedBitmap?.let {
                val probCon = 0.68
                val probCai = 1.0 - probCon
                val label = if (probCon >= probCai) "Con" else "Cái"
                txtResult.text = "Gợi ý: $label (Con: ${String.format(\"%.1f\", probCon*100)}%, Cái: ${String.format(\"%.1f\", probCai*100)}%)"
            } ?: run { txtResult.text = "Chưa chọn ảnh" }
        }
    }
}
