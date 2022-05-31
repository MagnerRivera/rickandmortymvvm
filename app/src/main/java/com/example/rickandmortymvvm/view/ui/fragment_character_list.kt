package com.example.rickandmortymvvm.view.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.PageCharacters
import com.example.rickandmortymvvm.R
import com.example.rickandmortymvvm.databinding.FragmentCharacterListBinding
import com.example.rickandmortymvvm.view.adapter.GridAdapter
import com.example.rickandmortymvvm.view.parcelables.toParcelable
import com.example.rickandmortymvvm.viewmodel.CharactersViewModel
import com.example.rickandmortymvvm.viewmodel.CharactersViewModel.CharacterListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : Fragment() {

    private val viewModel: CharactersViewModel by viewModels()

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridAdapter: GridAdapter

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        _binding = FragmentCharacterListBinding.inflate(i, c, false).apply {
            /*Inicio de la corrutina en el ciclo de vida*/
            lifecycleScope.launch {
                /*repeatOnLifecycle lanza el bloque en una nueva rutina
                  cada vez que el ciclo de vida está en estado INICIADO (o superior) y lo cancela cuando está DETENIDO.*/
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    /* Se activa el flujo y comience a escuchar los valores.
                    Tener en cuenta que esto sucede cuando se COMIENZA
                     el ciclo de vida y deja de recopilar cuando se DETIENE el ciclo de vida*/
                    viewModel.uiState.collect { uiState ->
                        changeUiState(uiState)
                    }
                }
            }
            gridAdapter = GridAdapter(requireContext()) {

            }
            charactersGrid.adapter = gridAdapter
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.before -> {
                        viewModel.before()
                        true
                    }
                    R.id.next -> {
                        viewModel.next()
                        true
                    }
                    else -> false
                }
            }
        }
        return binding.root
    }

    private fun changeUiState(uiState: CharacterListUiState) {
        when (uiState) {
            is CharacterListUiState.Error -> Unit
            is CharacterListUiState.Loading -> {
                binding.progressCircular.visibility =
                    if (uiState.isLoading) View.VISIBLE else View.GONE
            }
            is CharacterListUiState.Success -> updatePage(uiState.page)
        }
    }

    private fun updatePage(pageCharacters: PageCharacters) {
        // Update grid
        gridAdapter.updateItems(pageCharacters.results.map { it.toParcelable() })

        val info = pageCharacters.info

        // Update top app bar
        binding.topAppBar.apply {
            val pageInfoText =
                resources.getString(R.string.page) + " " + info.currentPage + "-" + info.pages
            (menu[0].actionView as TextView).text = pageInfoText
            menu[1].isEnabled = info.prev != null
            menu[2].isEnabled = info.next != null
        }
    }

}