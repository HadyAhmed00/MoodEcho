package io.github.moodEcho.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.moodEcho.ServerHandler.ApiClient
import io.github.moodEcho.databinding.FragmentAudioBinding
import org.json.JSONException

import org.json.JSONObject



class AudioFragment : Fragment() {

    lateinit var binding: FragmentAudioBinding
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionToRecordAccepted = false
    val TAG = "adiou"
    companion object{
        const val REQUEST_RECORD_AUDIO_PERMISSION = 1
        const val PICK_SOUND_REQUEST_CODE = 123

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        binding = FragmentAudioBinding.inflate(layoutInflater)

        binding.audioSelectBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*" // Limit the selection to audio files
            startActivityForResult(intent, PICK_SOUND_REQUEST_CODE)

//            findNavController().navigate(R.id.action_audioFragment_to_resultsFragment)

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_SOUND_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { soundUri ->
                val inputStream = requireContext().contentResolver.openInputStream(soundUri)

                if (inputStream != null) {

                    val apiClient = ApiClient()

                    val soundByteArray = inputStream.readBytes()

                    apiClient.uploadWavFile(soundByteArray) { success, response ->
                        if (success) {
                            // Handle the successful response
                            Log.i(TAG, "Server response: $response")
                            var message =""
                            try {
                                val responseJSON = response?.let { JSONObject(it) }
                                message = responseJSON?.getString("voice class").toString()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            Log.i(TAG, "onActivityResult: $message")
                            val action = AudioFragmentDirections.actionAudioFragmentToResultsFragment(message)
                            findNavController().navigate(action)
//                            findNavController().navigate(R.id.action_audioFragment_to_resultsFragment)
                            println("Server response: $response")
                        } else {
                            // Handle the error
                            Log.i(TAG, "Error: $response")
                        }
                    }
                    Toast.makeText(requireContext(), "Done we read the sound !!", Toast.LENGTH_SHORT).show()

                    inputStream.close()
                } else {

                    Toast.makeText(requireContext(), "fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /*override fun onRequestPermissionsResult(
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
    }*/
}