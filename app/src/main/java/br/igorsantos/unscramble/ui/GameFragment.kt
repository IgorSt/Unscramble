package br.igorsantos.unscramble.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.igorsantos.unscramble.MAX_NO_OF_WORDS
import br.igorsantos.unscramble.R
import br.igorsantos.unscramble.SCORE_INCREASE
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
        binding = FragmentGameBinding.inflate(inflater, container, false)

        Log.d("GameFragment", "Word: ${viewModel.currentScrambleWord} " +
        "Score: ${viewModel.score} " + "WordCount: ${viewModel.currenWordCount}")
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup clickListener
        binding.submit.setOnClickListener   { onSubmitWord() }
        binding.skip.setOnClickListener     { onSkipWord()   }

        //update UI
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    private fun onSubmitWord(){
        val playerWord = binding.textInputEditText.text.toString()

        if(viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        }else{
            setErrorTextField(true)
        }
    }

    private fun onSkipWord(){
        if(viewModel.nextWord()){
            setErrorTextField(false)
            updateNextWordOnScreen()
        }else{
            showFinalScoreDialog()
        }
    }

    private fun getNextScrambleWord(): String{
        val tempWord = allWordLists.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    private fun restartGame(){
        viewModel.reinitializeData()
        setErrorTextField(true)
        updateNextWordOnScreen()
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

    private fun updateNextWordOnScreen(){
        binding.textViewUnscrambledWord.text = viewModel.currentScrambleWord
    }

    private fun showFinalScoreDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.score, viewModel.score))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
            .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .show()
    }
}