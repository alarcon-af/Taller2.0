package com.example.taller20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taller20.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACCESS_FINE_LOCATION = 0
        const val ACCESS_COARSE_LOCATION = 1
        const val GALLERY_REQUEST = 0
        const val CAMERA_REQUEST = 1
        const val PICK_IMAGE = 8
        const val CONTACTS_REQUEST =0
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener{
            val intent1 = Intent(this, CameraActivity::class.java)
            startActivity(intent1)
        }

        binding.imageButton2.setOnClickListener{
            val intent2 = Intent(this, ContactsActivity::class.java)
            startActivity(intent2)
        }

        binding.imageButton3.setOnClickListener{
            val intent3 = Intent(this, MapActivity::class.java)
            startActivity(intent3)
        }

    }
}