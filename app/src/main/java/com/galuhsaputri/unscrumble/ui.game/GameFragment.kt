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

package com.galuhsaputri.unscrumble.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.galuhsaputri.unscrumble.R
import com.galuhsaputri.unscrumble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
* Fragmen tempat game dimainkan, berisi logika game.
 */
class GameFragment : Fragment() {

   // Mengikat instance objek dengan akses ke tampilan di tata letak game_fragment.xml
    private lateinit var binding: GameFragmentBinding

    // Buat ViewModel saat pertama kali fragmen dibuat.
    // Jika fragmen dibuat ulang, ia menerima instance GameViewModel yang sama yang dibuat oleh
    // fragmen pertama.
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Mengembang file XML tata letak dan mengembalikan instance objek yang mengikat
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setel viewModel untuk pengikatan data - ini memungkinkan akses tata letak terikat
        // ke semua data di VieWModel
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
     // Tentukan tampilan fragmen sebagai pemilik siklus hidup pengikatan.
        // Ini digunakan agar pengikatan dapat mengamati pembaruan LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // Siapkan pendengar klik untuk tombol Kirim dan Lewati.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
    }

    /*
   * Memeriksa kata pengguna, dan memperbarui skor yang sesuai.
    * Menampilkan kata acak berikutnya.
    * Setelah kata terakhir, pengguna diperlihatkan Dialog dengan skor akhir.
    */
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
     * Melompati kata saat ini tanpa mengubah skor.
     * Meningkatkan jumlah kata.
     * Setelah kata terakhir, pengguna diperlihatkan Dialog dengan skor akhir.
     */
    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    /*
   * Membuat dan menampilkan AlertDialog dengan skor akhir.
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }

    /*
    * Inisialisasi ulang data di ViewModel dan perbarui tampilan dengan data baru, untuk
     * mulai ulang permainan.
     */
    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }

    /*
     * keluar dari game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
   * Mengatur dan mengatur ulang status kesalahan bidang teks.
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
}
