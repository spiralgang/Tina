package com.example.advancedapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var themeSpinner: Spinner
    private lateinit var selectFileButton: Button
    private lateinit var outputView: TextView

    private val REQUEST_CODE = 100
    private val FILE_PICK_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeSpinner = findViewById(R.id.theme_spinner)
        selectFileButton = findViewById(R.id.select_file_button)
        outputView = findViewById(R.id.output_view)

        setupThemeSpinner()
        setupFileSelectionButton()
        requestPermissions()
    }

    private fun setupThemeSpinner() {
        val themes = arrayOf("Default", "Dark", "Light", "LineageOS", "Ubuntu", "Linux Mint", "KDE")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, themes)
        themeSpinner.adapter = adapter

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTheme = themes[position]
                when (selectedTheme) {
                    "Default" -> setTheme(android.R.style.Theme_AppCompat_Light)
                    "Dark" -> setTheme(android.R.style.Theme_AppCompat)
                    // Add more theme handling as required
                }
                recreate() // Recreate the activity to apply the new theme
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun setupFileSelectionButton() {
        selectFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICK_CODE)
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICK_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                handleFileUpload(uri)
            }
        }
    }

    private fun handleFileUpload(uri: Uri) {
        val fileName = getFileName(uri)
        outputView.text = "Uploaded File: $fileName"
        // Here you can add logic to process the uploaded AI model
    }

    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return uri.lastPathSegment ?: "Unknown"
    }
}￼Enter
