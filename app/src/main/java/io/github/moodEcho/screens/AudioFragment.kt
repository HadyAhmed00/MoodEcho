package io.github.moodEcho.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import io.github.moodEcho.R
import io.github.moodEcho.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {

    lateinit var binding: FragmentAudioBinding
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionToRecordAccepted = false

    companion object{
        const val REQUEST_RECORD_AUDIO_PERMISSION = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        binding = FragmentAudioBinding.inflate(layoutInflater)

        binding.audioSelectBtn.setOnClickListener{
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*" // Limit the selection to audio files
//            startActivityForResult(intent, REQUEST_RECORD_AUDIO_PERMISSION)

            findNavController().navigate(R.id.action_audioFragment_to_resultsFragment)

        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }
}