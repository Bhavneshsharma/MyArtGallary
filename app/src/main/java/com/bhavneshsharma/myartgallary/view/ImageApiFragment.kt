package com.bhavneshsharma.myartgallary.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bhavneshsharma.myartgallary.R
import com.bhavneshsharma.myartgallary.adapter.ImageRecyclerAdapter
import com.bhavneshsharma.myartgallary.databinding.FragmentImageApiBinding
import com.bhavneshsharma.myartgallary.util.Status
import com.bhavneshsharma.myartgallary.viewModel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
   private val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ArtViewModel
    private var fragmentImageApiBinding: FragmentImageApiBinding ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentImageApiBinding.bind(view)
        fragmentImageApiBinding = binding

        var job : Job? = null

        binding.fragmentImageApiSearchBar.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObserver()

        binding.fragmentImageApiRecyclerView.adapter = imageRecyclerAdapter
        binding.fragmentImageApiRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            3
        )
        imageRecyclerAdapter.setOnItemClickLister {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }

    }

    fun subscribeToObserver(){
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentImageApiBinding?.fragmentImageApiProgressBar?.visibility = View.GONE
                }
                Status.ERROR ->{
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                    fragmentImageApiBinding?.fragmentImageApiProgressBar?.visibility = View.GONE
                }
                Status.LOADING ->{
                    fragmentImageApiBinding?.fragmentImageApiProgressBar?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        fragmentImageApiBinding = null
        super.onDestroy()
    }
}