package com.example.nikestore.feature.main.cart

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.R
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.*
import com.example.nikestore.data.repo.CartRepository
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class CartViewModel(val cartRepository: CartRepository) : NikeViewModel() {

    val cartItemsLiveData = MutableLiveData<List<CartItem>>()
    val purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()

    private fun getCartItems() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            progressBarLiveData.value = true
            emptyStateLiveData.value = EmptyState(false)
            cartRepository.get()
                .asyncNetworkRequest()
                .doFinally { progressBarLiveData.value = false }
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            cartItemsLiveData.value = t.cart_items
                            purchaseDetailLiveData.value =
                                PurchaseDetail(t.total_price, t.shipping_cost, t.payable_price)

                        } else
                            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyState)

                    }
                })
        } else
            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyStateLoginRequired, true)
    }

    fun removeItemFromCart(cartItem: CartItem): Completable =
        cartRepository.remove(cartItem.cart_item_id)
            .doAfterSuccess {
                Timber.i("cart item count after remove-> ${cartItemsLiveData.value?.size}")
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= cartItemCount.count
                    EventBus.getDefault().postSticky(it)
                }
                cartItemsLiveData.value?.let {
                    if (it.isEmpty()) {
                        emptyStateLiveData.postValue(EmptyState(true, R.string.cartEmptyState))
                    }
                }
            }
            .ignoreElement()

    fun increaseCartItemCart(cartItem: CartItem): Completable =
        cartRepository.changeCount(cartItem.cart_item_id, ++cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count += 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()

    fun decreaseCartItemCart(cartItem: CartItem): Completable =
        cartRepository.changeCount(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()

    fun refresh() {
        getCartItems()
    }

    private fun calculateAndPublishPurchaseDetail() {
        cartItemsLiveData.value?.let { cartItems ->
            purchaseDetailLiveData.value?.let { purchaseDtail ->
                var totalPrice = 0
                var payablePrice = 0
                cartItems.forEach {
                    totalPrice += it.product.price * it.count
                    payablePrice += (it.product.price - it.product.discount) * it.count
                }
                purchaseDtail.total_price = totalPrice
                purchaseDtail.payable_price = payablePrice
                purchaseDetailLiveData.postValue(purchaseDtail)
            }
        }
    }
}