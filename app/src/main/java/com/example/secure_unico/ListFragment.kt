package com.example.secure_unico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.secure_unico.databinding.FragmentListBinding
import com.example.secure_unico.model.UserViewModel


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.lifecycleOwner = viewLifecycleOwner
        userViewModel.getTicketsFromApi()
        binding.recyclerView.adapter = TicketListAdapter(TicketListener { ticket ->
            userViewModel.onTicketClicked(ticket)
            findNavController()
                .navigate(R.id.action_listFragment_to_qrcodeFragment)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //  lifecycleOwner = viewLifecycleOwner
            listFragment = this@ListFragment
            viewModel = userViewModel

            // yourTicketTextView.text = userViewModel.tickets.value.toString()


        }

        // viewModel.authenticate()


    }


}