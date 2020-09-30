package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.cart.model.CartItem
import com.pharmacy.myapp.data.remote.rest.request.order.CreateOrderRequest
import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import com.pharmacy.myapp.data.remote.rest.response.AuthResponse
import com.pharmacy.myapp.model.order.Order
import com.pharmacy.myapp.data.remote.rest.response.TokenRefreshResponse
import com.pharmacy.myapp.data.remote.rest.response.UploadImageResponse
import com.pharmacy.myapp.model.*
import com.pharmacy.myapp.model.category.Category
import com.pharmacy.myapp.user.model.customerInfo.CustomerItem
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.product.model.ProductLite
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/api/v1/customer/refresh_token")
    suspend fun tokenRefresh(@Body body: TokenRefreshRequest): TokenRefreshResponse

    @POST("/api/v1/customer/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @POST("/api/v1/customer/auth")
    suspend fun auth(@Body arguments: Map<String, String>): JSONObject

    @POST("/api/v1/customer/login")
    suspend fun login(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @PUT("/api/v1/customer/customer")
    suspend fun updateCustomerInfo(@Body arguments: Map<String, String>): BaseDataResponse<CustomerItem>

    @POST("/api/v1/customer/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @Multipart
    @POST("/api/v1/customer/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseDataResponse<UploadImageResponse>

    @GET("/api/v1/customer/customer")
    suspend fun fetchCustomer(): BaseDataResponse<CustomerItem>

    @GET("/api/v1/public/products/search")
    suspend fun productSearch(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("regionId") regionId: Int? = null,
        @Query("barCode") barCode: String? = null,
        @Query("categoryCode") categoryCode: String? = null,
        @Query("name") name: String? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @GET("/api/v1/public/regions")
    suspend fun regions(): BaseDataResponse<ListItemsModel<Region>>

    @GET("/api/v1/public/products/global-product/{id}")
    suspend fun getProductById(@Path("id") globalProductId: Int): BaseDataResponseWithItem<Product>

    @PATCH("/api/v1/customer/customer/region")
    suspend fun updateRegion(@Body arguments: Map<String, Int>): BaseDataResponse<CustomerItem>

    @POST("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun setToWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @DELETE("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun removeFromWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @GET("/api/v1/customer/wishlist")
    suspend fun getWishList(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @GET("/api/v1/public/categories")
    suspend fun categories(): BaseDataResponse<ListItemsModel<Category>>

    @GET("/api/v1/public/pharmacies/global-product/{id}/pharmacy-products")
    suspend fun pharmacyList(
        @Path("id") globalProductId: Int,
        @Query("regionId") regionId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<Pharmacy>>

    @GET("/api/v1/customer/product-cart")
    suspend fun cartProducts(): BaseDataResponse<PaginationModel<CartItem>>

    @POST("/api/v1/customer/product-cart/pharmacy-product/{id}")
    suspend fun addProductToCart(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @DELETE("/api/v1/customer/product-cart/pharmacy-product/{id}")
    suspend fun removeProductFromCart(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @POST("/api/v1/customer/order")
    suspend fun sendOrder(@Body body: CreateOrderRequest): BaseDataResponseWithItem<Order>

    @GET("/api/v1/customer/orders")
    suspend fun fetchOrders(
        @Query("status") query: String? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null): BaseDataResponse<PaginationModel<Order>>
}