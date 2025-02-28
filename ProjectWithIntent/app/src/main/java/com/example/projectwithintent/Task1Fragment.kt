package com.example.projectwithintent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

class Task1Fragment : Fragment() {

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_task1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonPickImage: Button = view.findViewById(R.id.button_pick_image)
        buttonPickImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        ImagePicker.with(this)
            .galleryOnly()
            .crop() // Optional: Crop the image
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri = data.data!!
            imageUri = uri
            shareImageToInstagramStory(uri)
        }
    }

    private fun shareImageToInstagramStory(imageUri: Uri) {
        try {
            Log.d("InstagramURI", "Image URI: $imageUri")

            val shareIntent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage("com.instagram.android")
            }

            // Grant temporary read permission to the content URI
            val resInfoList = requireContext().packageManager.queryIntentActivities(shareIntent, 0)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
                Log.d("InstagramIntent", "Starting Instagram story intent")
                startActivity(shareIntent)
            } else {
                Log.e("InstagramIntent", "Instagram is not installed")
                Toast.makeText(requireContext(), "Instagram is not installed", Toast.LENGTH_SHORT).show()
                // Redirect to Play Store to install Instagram
                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.instagram.android"))
                if (playStoreIntent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(playStoreIntent)
                } else {
                    Toast.makeText(requireContext(), "Play Store is not available", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("InstagramError", "Error sharing to Instagram: ${e.message}")
            Toast.makeText(requireContext(), "Error sharing to Instagram: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}