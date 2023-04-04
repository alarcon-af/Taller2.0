package com.example.taller20

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.taller20.MainActivity.Companion.CAMERA_REQUEST
import com.example.taller20.MainActivity.Companion.GALLERY_REQUEST
import com.example.taller20.MainActivity.Companion.PICK_IMAGE
import com.example.taller20.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding

    private val img : ImageView by lazy {
        binding.imageView
    }
    private var tempImageUri: Uri? = null
    private var tempImageFilePath = ""
    private val albumLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        img.setImageURI(it)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            success ->
        if (success){
            img.setImageURI(tempImageUri)
        }
    }

    private fun createImageFile() : File{
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("temp-image", ".jpg", storageDir)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            GALLERY_REQUEST -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    startGallery()
                }else{
                    showInContextUI()
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)
                }
                return
            }
            CAMERA_REQUEST -> {
                if((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    startCamera()

                }else{
                    showInContextUI()
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST)
                }
                return
            }
        }
    }

    private fun showInContextUI(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permiso necesario")
        builder.setMessage("Esta función requiere acceso. Si deniegas el permiso, algunas funciones estarán deshabilitadas.")
        builder.setNegativeButton("Volver") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun askCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }else{
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission(){
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            showInContextUI()
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)
        }
    }

    private fun startCamera(){
        tempImageUri = FileProvider.getUriForFile(this, "com.example.taller20.provider", createImageFile().also {
            tempImageFilePath = it.absolutePath
        })
        cameraLauncher.launch(tempImageUri)
    }

    private fun askGalleryPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            startGallery()
        }else{
            requestGalleryPermission()
        }
    }

    private fun requestGalleryPermission(){
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showInContextUI()
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)
        }
    }

    private fun startGallery(){
        albumLauncher.launch("image/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val camara = binding.camara
        val galeria = binding.galeria

        camara.setOnClickListener{
            askCameraPermission()
        }

        galeria.setOnClickListener{
            askGalleryPermission()
        }
    }

}