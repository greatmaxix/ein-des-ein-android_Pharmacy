package com.pulse.data.remote.api

import androidx.annotation.WorkerThread
import com.pulse.chat.model.chat.ChatItem
import com.pulse.chat.model.message.MessageItem
import com.pulse.components.cart.model.CartItem
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.components.recipes.model.Recipe
import com.pulse.data.remote.model.chat.CreateChatRequest
import com.pulse.data.remote.model.chat.SendMessageBody
import com.pulse.data.remote.model.chat.SendReviewRequest
import com.pulse.data.remote.model.order.CreateOrderRequest
import com.pulse.model.*
import com.pulse.model.auth.StartInfo
import com.pulse.model.category.Category
import com.pulse.model.order.Order
import com.pulse.model.region.Region
import com.pulse.product.model.Product
import com.pulse.product.model.ProductLite
import com.pulse.user.model.avatar.Avatar
import com.pulse.user.model.customer.CustomerItem
import com.pulse.util.Constants.Companion.CHAT_LIST_PAGE
import com.pulse.util.Constants.Companion.CHAT_LIST_PAGE_SIZE
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface RestApi {

    @WorkerThread
    @POST("/api/v1/customer/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): BaseDataResponse<Unit>

    @WorkerThread
    @POST("/api/v1/customer/auth")
    suspend fun signIn(@Body arguments: Map<String, String>): BaseDataResponse<Unit>

    @WorkerThread
    @POST("/api/v1/customer/login")
    suspend fun signCode(@Body arguments: Map<String, String>): BaseDataResponse<StartInfo>

    @WorkerThread
    @PUT("/api/v1/customer/customer")
    suspend fun updateCustomerInfo(@Body arguments: Map<String, String>): BaseDataResponse<CustomerItem>

    @WorkerThread
    @POST("/api/v1/customer/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @WorkerThread
    @Multipart
    @POST("/api/v1/customer/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseDataResponse<SingleItemModel<Avatar>>

    @WorkerThread
    @GET("/api/v1/customer/customer")
    suspend fun fetchCustomer(): BaseDataResponse<CustomerItem>

    @WorkerThread
    @GET("/api/v1/public/products/search")
    suspend fun productSearch(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("regionId") regionId: Int? = null,
        @Query("barCode") barCode: String? = null,
        @Query("categoryCode") categoryCode: String? = null,
        @Query("name") name: String? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @WorkerThread
    @GET("/api/v1/public/regions")
    suspend fun regions(): BaseDataResponse<ListItemsModel<Region>>

    @WorkerThread
    @GET("/api/v1/public/products/global-product/{id}")
    suspend fun getProductById(@Path("id") globalProductId: Int): BaseDataResponseWithItem<Product>

    @WorkerThread
    @PATCH("/api/v1/customer/customer/region")
    suspend fun updateRegion(@Body arguments: Map<String, Int>): BaseDataResponse<CustomerItem>

    @WorkerThread
    @POST("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun setToWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @WorkerThread
    @DELETE("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun removeFromWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @WorkerThread
    @GET("/api/v1/customer/wishlist")
    suspend fun getWishList(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @WorkerThread
    @GET("/api/v1/customer/recipes")
    suspend fun getRecipes(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<Recipe>>

    @WorkerThread
    @GET("/api/v1/public/categories")
    suspend fun categories(): BaseDataResponse<ListItemsModel<Category>>

    @WorkerThread
    @GET("/api/v1/public/pharmacies/global-product/{id}/pharmacy-products")
    suspend fun pharmacyList(
        @Path("id") globalProductId: Int,
        @Query("regionId") regionId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<Pharmacy>>

    @WorkerThread
    @GET("/api/v1/customer/product-cart")
    suspend fun cartProducts(): BaseDataResponse<PaginationModel<CartItem>>

    @WorkerThread
    @POST("/api/v1/customer/product-cart/pharmacy-product/{id}")
    suspend fun addProductToCart(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @WorkerThread
    @DELETE("/api/v1/customer/product-cart/pharmacy-product/{id}")
    suspend fun removeProductFromCart(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @WorkerThread
    @POST("/api/v1/customer/order")
    suspend fun sendOrder(@Body body: CreateOrderRequest): BaseDataResponseWithItem<Order>

    @WorkerThread
    @GET("/api/v1/customer/orders")
    suspend fun fetchOrders(
        @Query("status") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<Order>>

    @WorkerThread
    @GET("/api/v1/customer/order/{id}/order-card")
    suspend fun getOrderDetail(@Path("id") id: Int): BaseDataResponseWithItem<Order>

    @WorkerThread
    @PATCH("/api/v1/customer/order/{id}/cancel")
    suspend fun cancelOrder(@Path("id") id: Int): BaseDataResponse<Unit>

    @WorkerThread
    @POST("/api/v1/customer/chat")
    suspend fun createChat(@Body body: CreateChatRequest): BaseDataResponse<SingleItemModel<ChatItem>>

    @WorkerThread
    @PATCH("/api/v1/customer/chat/{chatId}/close")
    suspend fun closeChat(@Path("chatId") chatId: Int): BaseDataResponse<SingleItemModel<ChatItem>>

    @WorkerThread
    @PATCH("/api/v1/customer/chat/{chatId}/continue")
    suspend fun continueChat(@Path("chatId") chatId: Int): BaseDataResponse<SingleItemModel<ChatItem>>

    @WorkerThread
    @GET("/api/v1/chat/chat/{chatId}")
    suspend fun getChat(@Path("chatId") chatId: Int): BaseDataResponse<SingleItemModel<ChatItem>>

    @WorkerThread
    @GET("/api/v1/chat/chats")
    suspend fun chatList(
        @Query("page") page: Int? = CHAT_LIST_PAGE,
        @Query("per_page") pageSize: Int? = CHAT_LIST_PAGE_SIZE,
        @Query("all") all: Boolean = false,
        @Query("active") active: Boolean = true,
        @Query("order") order: String? = "desc"
    ): BaseDataResponse<PaginationModel<ChatItem>>

    @WorkerThread
    @GET("/api/v1/chat/chat/{chatId}/messages")
    suspend fun messagesList(
        @Path("chatId") chatId: Int,
        @Query("per_page") pageSize: Int? = null,
        @Query("afterMessageNumber") afterMessageNumber: Int? = null,
        @Query("beforeMessageNumber") beforeMessageNumber: Int? = null
    ): BaseDataResponse<PaginationModel<MessageItem>>

    @WorkerThread
    @POST("/api/v1/chat/chat/{chatId}/message")
    suspend fun sendMessage(@Path("chatId") chatId: Int, @Body body: SendMessageBody): BaseDataResponse<SingleItemModel<MessageItem>>

    @WorkerThread
    @POST("/api/v1/chat/chat/{chatId}/application/{imageUuid}")
    suspend fun sendImageMessage(@Path("chatId") chatId: Int, @Path("imageUuid") imageUuid: String): BaseDataResponse<SingleItemModel<MessageItem>>

    @WorkerThread
    @PATCH("/api/v1/customer/chat/{chatId}/evaluate")
    suspend fun sendReview(@Path("chatId") chatId: Int, @Body body: SendReviewRequest): BaseDataResponse<Any?>
}