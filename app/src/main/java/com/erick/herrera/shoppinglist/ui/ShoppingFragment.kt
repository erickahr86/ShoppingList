package com.erick.herrera.shoppinglist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erick.herrera.shoppinglist.R
import com.erick.herrera.shoppinglist.adapters.ShoppingItemAdapter
import com.erick.herrera.shoppinglist.databinding.FragmentShoppingBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ShoppingFragment: Fragment {

    @Inject
    lateinit var shoppingItemAdapter: ShoppingItemAdapter

    @Inject
    lateinit var viewModel: ShoppingViewModel

    constructor() : super()

    constructor(shoppingItemAdapter: ShoppingItemAdapter, viewModel: ShoppingViewModel? = null) : this() {
        this.shoppingItemAdapter = shoppingItemAdapter
        this.viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
    }

    private lateinit var binding: FragmentShoppingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShoppingBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]

        subscribeToObservers()
        setupRecyclerView()

        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(R.id.addShoppingItemFragment)
        }
    }

    private val itemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition

            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.shoppingItems.observe(viewLifecycleOwner) {
            shoppingItemAdapter.shoppingItems = it
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            val price = it ?: 0f
            val priceText = "Total Price: $ $price"
            binding.tvShoppingItemPrice.text = priceText
        }

    }

    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(this)
        }
    }
}