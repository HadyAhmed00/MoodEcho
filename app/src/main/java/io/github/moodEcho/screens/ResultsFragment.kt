package io.github.moodEcho.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.github.moodEcho.R
import io.github.moodEcho.databinding.FragmentAudioBinding
import io.github.moodEcho.databinding.FragmentResultsBinding
import io.noties.markwon.Markwon

class ResultsFragment : Fragment() {

    lateinit var binding: FragmentResultsBinding
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsBinding.inflate(layoutInflater)


         binding.backBtn.setOnClickListener{
             findNavController().navigateUp()
         }

         val markwon:Markwon = Markwon.create(requireContext())

         markwon.setMarkdown(binding.tipsTitle,"## 10 tips for happy people to be happier:")

         val content = "* Practice gratitude.\n" +
                 "* Spend time with loved ones.\n" +
                 "* Give back to others.\n" +
                 "* Take care of your physical health.\n" +
                 "* Challenge yourself.\n" +
                 "* Learn new things.\n" +
                 "* Spend time in nature.\n" +
                 "* Meditate or practice mindfulness.\n" +
                 "* Be optimistic.\n" +
                 "* Seek help if needed.\n" +
                 "* Make happiness a priority."

         markwon.setMarkdown(binding.tipsContent,content )
        return binding.root
    }

}