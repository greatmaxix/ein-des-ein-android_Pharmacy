package com.pharmacy.myapp.data.remote.rest

import com.pharmacy.myapp.data.remote.rest.request.TokenRefreshRequest
import com.pharmacy.myapp.data.remote.rest.response.AuthResponse
import com.pharmacy.myapp.data.remote.rest.response.TokenRefreshResponse
import com.pharmacy.myapp.data.remote.rest.response.UploadImageResponse
import com.pharmacy.myapp.model.*
import com.pharmacy.myapp.model.category.Category
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfoItem
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.product.model.ProductLite
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("$API_PATH_CUSTOMER/refresh_token")
    suspend fun tokenRefresh(@Body body: TokenRefreshRequest): TokenRefreshResponse

    @POST("$API_PATH_CUSTOMER/registration")
    suspend fun signUp(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @POST("$API_PATH_CUSTOMER/auth")
    suspend fun auth(@Body arguments: Map<String, String>): JSONObject

    @POST("$API_PATH_CUSTOMER/login")
    suspend fun login(@Body arguments: Map<String, String>): BaseDataResponse<AuthResponse>

    @PUT("$API_PATH_CUSTOMER/customer")
    suspend fun updateCustomerInfo(@Body arguments: Map<String, String>): BaseDataResponse<CustomerInfoItem>

    @POST("$API_PATH_CUSTOMER/logout")
    suspend fun logout(@Body arguments: Map<String, String>): Response<JSONObject>

    @Multipart
    @POST("$API_PATH_CUSTOMER/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseDataResponse<UploadImageResponse>

    @GET("$API_PATH_CUSTOMER/customer")
    suspend fun fetchCustomerInfo(): BaseDataResponse<CustomerInfoItem>

    @GET("$API_PATH_PUBLIC/products/search")
    suspend fun productSearch(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("regionId") regionId: Int? = null,
        @Query("barCode") barCode: String? = null,
        @Query("categoryCode") categoryCode: String? = null,
        @Query("name") name: String? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @GET("$API_PATH_PUBLIC/regions")
    suspend fun regions(): BaseDataResponse<ListItemsModel<Region>>

    @GET("$API_PATH_PUBLIC/products/global-product/{id}")
    suspend fun getProductById(@Path("id") globalProductId: Int): BaseDataResponseWithItem<Product>

    @PATCH("$API_PATH_CUSTOMER/customer/region")
    suspend fun updateRegion(@Body arguments: Map<String, Int>): BaseDataResponse<CustomerInfoItem>

    @POST("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun setToWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @DELETE("/api/v1/customer/wishlist/global-product/{id}")
    suspend fun removeFromWishList(@Path("id") globalProductId: Int): BaseDataResponse<Unit>

    @GET("/api/v1/customer/wishlist")
    suspend fun getWishList(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @GET("$API_PATH_PUBLIC/categories")
    suspend fun categories(): BaseDataResponse<ListItemsModel<Category>>

    @GET("/api/v1/public/pharmacies/global-product/{id}/pharmacy-products")
    suspend fun pharmacyList(
        @Path("id") globalProductId: Int,
        @Query("regionId") regionId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<Pharmacy>>

    companion object {

        private const val API_PATH = "/api/v1"
        private const val API_PATH_CUSTOMER = "$API_PATH/customer"
        private const val API_PATH_PUBLIC = "$API_PATH/public"
    }
}