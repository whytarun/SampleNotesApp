package com.example.samplenotesapp.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.samplenotesapp.R
import com.example.samplenotesapp.api.NotesApi
import com.example.samplenotesapp.databinding.FragmentMainBinding
import com.example.samplenotesapp.models.NoteResponse
import com.example.samplenotesapp.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var notesApi: NotesApi
    val noteViewModel by viewModels<NoteViewModel>()

    private var _binding :FragmentMainBinding? =null
    val binding get() = _binding!!

    private lateinit var noteAdapter : NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentMainBinding.inflate(inflater, container, false)
        noteAdapter = NoteAdapter(::noteClick)
        bindingObserver()
        return  binding.root
    }

    private fun bindingObserver() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner,Observer{
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success ->{
                noteAdapter.submitList(it.data)
                }
                is NetworkResult.Error ->{
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun noteClick(noteResponse: NoteResponse){
            val bundle =Bundle()
             bundle.putString("note",Gson().toJson(noteResponse))
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment,bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.getNotes()
        binding.noteList.layoutManager =StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter =noteAdapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}