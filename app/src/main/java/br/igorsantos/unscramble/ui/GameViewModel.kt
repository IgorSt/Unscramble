package br.igorsantos.unscramble.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.igorsantos.unscramble.MAX_NO_OF_WORDS
import br.igorsantos.unscramble.SCORE_INCREASE
import br.igorsantos.unscramble.allWordLists

/**
 * Created by IgorSantos on 8/1/2021.
 */
class GameViewModel : ViewModel() {

    private val _currentScrambleWord = MutableLiveData<String>()
    val currentScrambleWord: LiveData<String> get() = _currentScrambleWord

    private val _currentWordCount = MutableLiveData(0)
    val currenWordCount: LiveData<Int> get() = _currentWordCount

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

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
            _currentScrambleWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }
    }

    fun nextWord(): Boolean{
        return if(_currentWordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord()
            true
        } else false
    }

    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playWord: String): Boolean{
        if(playWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }
}