package io.github.moodEcho.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.moodEcho.Tips
import io.github.moodEcho.databinding.FragmentResultsBinding
import io.noties.markwon.Markwon
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader

class ResultsFragment : Fragment() {

    lateinit var binding: FragmentResultsBinding
    private lateinit var inputStream: InputStreamReader
    val lookUp: HashMap<String, Tips?> = hashMapOf("key" to null)
    //    val args: ConfirmationFragmentArgs by navArgs()
    private val args: ResultsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsBinding.inflate(layoutInflater)

        val stat = args.cat
        inputStream = InputStreamReader(requireActivity().assets.open("Emo Classes - Sheet1.csv"))
        val reader = BufferedReader(inputStream)
        val csvPars = CSVParser.parse(
            reader,
            CSVFormat.DEFAULT.withIgnoreHeaderCase().withRecordSeparator(',')
        )

        csvPars.forEach {
            it?.let {
                val tmp = Tips(it[0],it[1],it[2])
                lookUp[it[0]] = tmp

            }
        }

        binding.statTxt.text = stat
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }


        val markwon: Markwon = Markwon.create(requireContext())

        markwon.setMarkdown(binding.tipsTitle, "## ${lookUp[stat]?.title}:")

        val content = lookUp[stat]?.tipss

        if (content != null) {
            markwon.setMarkdown(binding.tipsContent, content)
        }
        return binding.root
    }

}