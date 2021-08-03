package br.igorsantos.unscramble.ui

import androidx.lifecycle.ViewModel
import br.igorsantos.unscramble.MAX_NO_OF_WORDS
import br.igorsantos.unscramble.SCORE_INCREASE
import br.igorsantos.unscramble.allWordLists

/**
 * Created by IgorSantos on 8/1/2021.
 */
class GameViewModel : ViewModel() {

    private lateinit var _currentScrambleWord: String
    val currentScrambleWord: String get() = _currentScrambleWord

    private var _currentWordCount = 0
    val currenWordCount: Int get() = _currentWordCount

    private var _score = 0
    val score: Int get() = _score

    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        getNextWord()
    }
    
    private fun getNextWord(){
        currentWord = allWordLists.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.toString().equals(currentWord, false)) { tempWord.shuffle() }

        if(wordList.contains(currentWord)){
            getNextWord()
        }else{
            _currentScrambleWord = String(tempWord)
            ++_currentWordCount
            wordList.add(currentWord)
        }
    }

    fun nextWord(): Boolean{
        return if(_currentWordCount < MAX_NO_OF_WORDS){
            getNextWord()
            true
        } else false
    }

    private fun increaseScore(){
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playWord: String): Boolean{
        if(playWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData(){
        _score = 0
        _currentWordCount = 0
        wordList.clear()
        getNextWord()
    }
}