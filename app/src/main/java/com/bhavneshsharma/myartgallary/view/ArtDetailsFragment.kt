package com.bhavneshsharma.myartgallary.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bhavneshsharma.myartgallary.R
import com.bhavneshsharma.myartgallary.databinding.FragmentArtDetailsBinding
import com.bhavneshsharma.myartgallary.util.Status
import com.bhavneshsharma.myartgallary.viewModel.ArtViewModel
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide : RequestManager
) : Fragment(R.layout.fragment_art_details) {

    lateinit var viewModel : ArtViewModel

    private var fragmentBinding : FragmentArtDetailsBinding?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding

        subscribeToObserver()

        binding.fragmentArtImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }

        val callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        binding.fragmentArtDetailsSaveButton.setOnClickListener {
            viewModel.makeArt(
                binding.fragmentArtDetailsImageName.text.toString(),
                binding.fragmentArtDetailsArtistName.text.toString(),
                binding.fragmentArtDetailsImageYear.text.toString())
        }
    }

    private fun subscribeToObserver(){
        viewModel.selectImageUrl.observe(viewLifecycleOwner, Observer { url ->
            fragmentBinding?.let {
                glide.load(url).into(it.fragmentArtImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {}
            }
        })
    }

    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }
}