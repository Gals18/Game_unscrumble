/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.galuhsaputri.android.unscramble.ui.game
/*kode diatas adalah nama paket*/

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList
import com.galuhsaputri.unscrumble.R
import com.galuhsaputri.unscrumble.databinding.GameFragmentBinding

/*kode diats merupakan Sebuah Source Code
Untuk Memanggil dan Menampilkan Sebuah Widget pada Program Android
 */

/**
 * Fragmen tempat game dimainkan, berisi logika game, dan saya menggunakan nama nama teman kelas saya.
 */
class GameFragment : Fragment() {

    private var score = 0
    private var currentWordCount = 0
    private var currentScrambledWord = "test"


    // Binding objek instance dengan akses ke tampilan di tata letak game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // Buat ViewModel saat pertama kali fragmen dibuat.
    // Jika fragmen dibuat ulang, ia menerima instance GameViewModel yang sama yang dibuat oleh
    // fragmen pertama

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Mengembang file XML tata letak dan mengembalikan instance objek yang mengikat
        binding = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Siapkan pendengar klik untuk tombol Kirim dan Lewati.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // pembaruan dari user interface
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
            R.string.word_count, 0, MAX_NO_OF_WORDS)
    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() {
        currentScrambledWord = getNextScrambledWord()
        currentWordCount++
        score += SCORE_INCREASE
        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
        binding.score.text = getString(R.string.score, score)
        setErrorTextField(false)
        updateNextWordOnScreen()
    }
/*kode diatas merupakan kode untuk mengaktifkan kegunaan
tombol submit apabila kita sudah memasukan kata kedalam tag input*/
    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    private fun onSkipWord() {
        currentScrambledWord = getNextScrambledWord()
        currentWordCount++
        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
        setErrorTextField(false)
        updateNextWordOnScreen()
    }
    /*kode diatas merupakan kode untuk mengaktifkan kegunaan
    tombol skip apabila kita tidak tau urutan kata yang benar dan mengganti kata acak selanjutnya*/
    /*
     * Gets a random word for the list of words and shuffles the letters in it.
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    /*
     * Inisialisasi ulang data di ViewModel dan perbarui tampilan dengan data baru, untuk
     * mulai ulang permainan.
     */
    private fun restartGame() {
        setErrorTextField(false)
        updateNextWordOnScreen()
    }
    /*digunakan untuk memulai ulang game */
    private fun exitGame() {
        activity?.finish()
    }
    /*
       kode diatas digunakan untuk keluar dari game
        */

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }
    /*
        * Mengatur dan mengatur ulang status kesalahan bidang teks.
        */

    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = currentScrambledWord
    }
/*
    * Menampilkan kata acak berikutnya di layar.
     */
}