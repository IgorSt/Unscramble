package br.igorsantos.unscramble.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.igorsantos.unscramble.MAX_NO_OF_WORDS
import br.igorsantos.unscramble.R
import br.igorsantos.unscramble.allWordLists
import br.igorsantos.unscramble.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by IgorSantos on 7/31/2021.
 */
class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        Log.d("GameFragment", "Word: ${viewModel.currentScrambleWord} " +
        "Score: ${viewModel.score} " + "WordCount: ${viewModel.currenWordCount}")
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize varibles dataBinding
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
        binding.lifecycleOwner = viewLifecycleOwner

        //setup clickListener
        binding.submit.setOnClickListener   { onSubmitWord() }
        binding.skip.setOnClickListener     { onSkipWord()   }

        //update UI
        //Observables
        /*viewModel.score.observe(viewLifecycleOwner, { newScore ->
            binding.score.text = getString(R.string.score, newScore)
        })
        viewModel.currenWordCount.observe(viewLifecycleOwner, { newWordCount ->
            binding.wordCount.text = getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
        })
        viewModel.currentScrambleWord.observe(viewLifecycleOwner, { newWord ->
            binding.textViewUnscrambledWord.text = newWord
        })*/
    }

    private fun onSubmitWord(){
        val playerWord = binding.textInputEditText.text.toString()

        if(viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) showFinalScoreDialog()
        }else{
            setErrorTextField(true)
        }
    }

    private fun onSkipWord(){
        if(viewModel.nextWord()){
            setErrorTextField(false)
        }else{
            showFinalScoreDialog()
        }
    }

    private fun restartGame(){
        viewModel.reinitializeData()
        setErrorTextField(true)
    }

    private fun exitGame(){
        activity?.finish()
    }

    private fun setErrorTextField(error: Boolean){
        if(error){
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        }else{
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    private fun showFinalScoreDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.score, viewModel.score.value))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .show()
    }
}