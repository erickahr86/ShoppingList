package com.erick.herrera.shoppinglist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erick.herrera.shoppinglist.adapters.ImageAdapter
import com.erick.herrera.shoppinglist.databinding.FragmentImagePickBinding
import com.erick.herrera.shoppinglist.other.Constants.GRID_SPAN_COUNT
import javax.inject.Inject

class ImagePickFragment : Fragment {

    @Inject
    lateinit var imageAdapter: ImageAdapter

    constructor() : super()

    constructor(imageAdapter: ImageAdapter) : this() {
        this.imageAdapter = imageAdapter
    }

    private lateinit var viewModel: ShoppingViewModel

    private lateinit var binding: FragmentImagePickBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImagePickBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        setupRecyclerView()

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }

    }
}