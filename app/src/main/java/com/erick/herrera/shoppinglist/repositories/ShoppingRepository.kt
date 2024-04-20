package com.erick.herrera.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.erick.herrera.shoppinglist.data.local.ShoppingItem
import com.erick.herrera.shoppinglist.data.remote.responses.ImageResponse
import com.erick.herrera.shoppinglist.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}