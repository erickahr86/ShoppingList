package com.erick.herrera.shoppinglist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.erick.herrera.shoppinglist.R
import com.erick.herrera.shoppinglist.databinding.FragmentAddShoppingItemBinding
import com.erick.herrera.shoppinglist.other.Status
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddShoppingItemFragment : Fragment {

    @Inject
    lateinit var glide: RequestManager

    constructor() : super()

    constructor(glide: RequestManager) : this() {
        this.glide = glide
    }

    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentAddShoppingItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        binding.btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                name = binding.etShoppingItemName.text.toString(),
                amountString = binding.etShoppingItemAmount.text.toString(),
                priceString = binding.etShoppingItemPrice.text.toString()
            )
        }

        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(R.id.imagePickFragment)
        }

        val callback = object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.curImageUrl.observe(viewLifecycleOwner) {
            glide.load(it).into(binding.ivShoppingImage)
        }

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView(),
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()

                        findNavController().popBackStack()
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            result.message ?: "An error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    Status.LOADING -> {
                        /*NO-OP*/
                    }
                }
            }
        }
    }
}