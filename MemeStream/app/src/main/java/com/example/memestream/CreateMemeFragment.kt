package com.example.memestream

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.memestream.DataaClasses.MemePost
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*


class CreateMemeFragment : Fragment() {

    private lateinit var ivMeme: ImageView
    private lateinit var etCaption: EditText
    private lateinit var btnUpload: Button

    private var selectedImageUri: Uri? = null

    // Activity Result Launcher for picking an image
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { ivMeme.setImageURI(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_meme, container, false)

        ivMeme = view.findViewById(R.id.ivMeme)
        etCaption = view.findViewById(R.id.etCaption)
        btnUpload = view.findViewById(R.id.btnUpload)

        btnUpload.setOnClickListener {
            if (selectedImageUri == null) {
                // Pick image if not selected
                val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
                pickImageLauncher.launch(intent)
            } else {
                // Upload meme
                uploadMeme()
            }
        }

        return view
    }

    private fun uploadMeme() {
        val captionText = etCaption.text.toString()
        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        // Check location permission
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        // Convert URI to Bitmap
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
        val bitmapWithCaption = overlayText(bitmap, captionText)

        // Get location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val lat = location?.latitude ?: 0.0
            val lng = location?.longitude ?: 0.0
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val memePost = MemePost(
                userId = "example_user", // Replace with actual userId
                imageUrl = "", // Upload bitmap to get URL
                caption = captionText,
                lat = lat,
                lng = lng,
                timestamp = timestamp
            )

            Toast.makeText(requireContext(), "Meme ready to POST: $memePost", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }

    /** Overlay text on Bitmap using Canvas */
    private fun overlayText(bitmap: Bitmap, text: String): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 60f
            style = Paint.Style.FILL
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            setShadowLayer(5f, 2f, 2f, Color.BLACK)
        }

        val x = 20f
        val y = bitmap.height - 40f
        canvas.drawText(text, x, y, paint)

        return mutableBitmap
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadMeme() // Retry after permission granted
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
