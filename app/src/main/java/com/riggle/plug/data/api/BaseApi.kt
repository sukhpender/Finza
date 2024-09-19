package com.riggle.plug.data.api

import com.riggle.plug.data.model.ActiveCPResponseModel
import com.riggle.plug.data.model.ActiveSalesmanResponseModel
import com.riggle.plug.data.model.AssignInventoryResponseModel
import com.riggle.plug.data.model.AssignedBeatDetailsResponseModel
import com.riggle.plug.data.model.BeatCityResponseModel
import com.riggle.plug.data.model.BeatInsightsResponseModel
import com.riggle.plug.data.model.BrandListModel
import com.riggle.plug.data.model.BrandNetworkCPCount
import com.riggle.plug.data.model.BrandOfferResponseModel
import com.riggle.plug.data.model.BrandProductResponseModel
import com.riggle.plug.data.model.BrandRateResponseModel
import com.riggle.plug.data.model.BrandResponse
import com.riggle.plug.data.model.CPLeaderBoardResponseModel
import com.riggle.plug.data.model.CheckTagAvailabilityResponseModel
import com.riggle.plug.data.model.CityBeatsResponseModel
import com.riggle.plug.data.model.CoOwnersListResponseModel
import com.riggle.plug.data.model.CpInsightsResponseModel
import com.riggle.plug.data.model.CpRemarksResponseModel
import com.riggle.plug.data.model.CpStockResponseItem
import com.riggle.plug.data.model.DailyAnalysisCalenderResponseModel
import com.riggle.plug.data.model.DeleteCoOwnerResponse
import com.riggle.plug.data.model.DesignationSalesFilterResponseModel
import com.riggle.plug.data.model.FinzaForgotPassResponseModel
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.data.model.GenerateReportResponseModel
import com.riggle.plug.data.model.GetHRResponseList
import com.riggle.plug.data.model.GetLeaveCountData
import com.riggle.plug.data.model.HoldAmountResponseModel
import com.riggle.plug.data.model.HomeInsightRetailersResponseModel
import com.riggle.plug.data.model.HomeInsightsLastDaysResponseModel
import com.riggle.plug.data.model.HomeInsightsOrderPlacedResponseModel
import com.riggle.plug.data.model.HomeInsightsOrderResponseModel
import com.riggle.plug.data.model.HomeInsightsSubCatResponseModel
import com.riggle.plug.data.model.HomeInventoryResponseModel
import com.riggle.plug.data.model.HomeLeaderBoardSPResponseModel
import com.riggle.plug.data.model.HomeOrderSummaryOrdersResponseModel
import com.riggle.plug.data.model.HomeOrderSummaryResponseModel
import com.riggle.plug.data.model.HomePageFiltersResponseModel
import com.riggle.plug.data.model.HomePageSalesPersonListResponsModel
import com.riggle.plug.data.model.HomePageSalesResponseModel
import com.riggle.plug.data.model.HomePageSnapShotResponseModel
import com.riggle.plug.data.model.HomeReachAnalysisResponseModel
import com.riggle.plug.data.model.HomeSKUsItemDetailsResponseModel
import com.riggle.plug.data.model.HomeSKUsResponseModel
import com.riggle.plug.data.model.InventryResponseModel
import com.riggle.plug.data.model.IssueTagCheckWalletResponseModel
import com.riggle.plug.data.model.LeadCPResponseModel
import com.riggle.plug.data.model.LowStockDataResponse
import com.riggle.plug.data.model.NetworkCPCount1Item
import com.riggle.plug.data.model.NetworkCancelledOrdersResponseModel
import com.riggle.plug.data.model.NetworkCompletedOrdersResponseModel
import com.riggle.plug.data.model.NetworkConfirmedResponseModel
import com.riggle.plug.data.model.NetworkOrderDetailsResponseModel
import com.riggle.plug.data.model.NetworkOrdersCountResponseModel
import com.riggle.plug.data.model.NetworkOutstandingOrdersResponseModel
import com.riggle.plug.data.model.NetworkPendingOrdersResponseModel
import com.riggle.plug.data.model.OwnOrderDetailsResponseModel
import com.riggle.plug.data.model.PaymentStoreRequest
import com.riggle.plug.data.model.ProjectListResponseModel
import com.riggle.plug.data.model.ReportingManagerResponseModel
import com.riggle.plug.data.model.ResetPasswordResponseModel
import com.riggle.plug.data.model.SalesBeatCountResponseModel
import com.riggle.plug.data.model.SalesBeatResponseModel
import com.riggle.plug.data.model.SalesBrandAnalysisInsightsResponseModel
import com.riggle.plug.data.model.SalesDailyAnalysisResponseModel
import com.riggle.plug.data.model.SalesLocationResponseModel
import com.riggle.plug.data.model.SalesNetworkResponseModel
import com.riggle.plug.data.model.SalesPerformanceResponseModel
import com.riggle.plug.data.model.SalesSKUsResponseModel
import com.riggle.plug.data.model.SalesTargetAnalysisResponseModel
import com.riggle.plug.data.model.SalesUserListResponseModel
import com.riggle.plug.data.model.SalesmanListingResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.SendOtpResponseModel
import com.riggle.plug.data.model.StorePaymentResponseModel
import com.riggle.plug.data.model.TargetGraphResponse
import com.riggle.plug.data.model.TargetUserData
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.data.model.UnAssignedCountResponseModel
import com.riggle.plug.data.model.UserProfileResponseModel
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.data.model.UsersListResponseModel
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.data.model.WalletCreateCustomerResponseModel
import com.riggle.plug.data.model.WalletTransactionsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface BaseApi {

    @POST(Constants.FINZA_LOGIN)
    @FormUrlEncoded
    suspend fun finzaLogin(
        @Field("phone_number") mobile: String,
        @Field("password") password: String,
    ): Response<FinzaLoginResponseModel>

    @POST(Constants.FORGOT_PASSWORD)
    @FormUrlEncoded
    suspend fun forgotPass(
        @Field("email") email: String,
    ): Response<FinzaForgotPassResponseModel>

    @POST(Constants.FINZA_LOGOUT)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun finzaLogOut(
        @Header("Authorization") header: String,
    ): Response<FinzaLogoutResponseModel>

    @GET(Constants.FINZA_PROFILE)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun finzaGetProfile(
        @Header("Authorization") header: String,
    ): Response<FinzaProfileResponseModel>

    @POST(Constants.SEND_OTP_TAG_ISSUE)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun sendOtpTagIssue(
        @Header("Authorization") header: String, @Body reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel>

    @POST(Constants.WALLET_CREATE_CUSTOMER)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun createWalletCustomer(
        @Header("Authorization") header: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Response<WalletCreateCustomerResponseModel>

    @JvmSuppressWildcards
    @Multipart
    @Headers(Constants.X_APP_ACCEPT)
    @POST(Constants.UPDATE_PROFILE)
    suspend fun updateUserProfile(
        @Header("Authorization") header: String,
        @Part("first_name") body: RequestBody,
        @Part("last_name") body2: RequestBody,
        @Part profile_image: MultipartBody.Part
    ): Response<FinzaProfileResponseModel>

    @GET(Constants.USER_WALLET)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun finzaUserWallet(
        @Header("Authorization") header: String,
    ): Response<UserWalletResponseModel>

    @GET(Constants.USERS_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun finzaUsersList(
        @Header("Authorization") header: String,
    ): Response<UsersListResponseModel>

    @POST(Constants.INVENTORY_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getInventoriesList(
        @Header("Authorization") header: String, @Field("status") status: String
    ): Response<InventryResponseModel>

    @POST(Constants.ASSIGN_INVENTORY)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun assignInventory(
        @Header("Authorization") header: String,
        @Field("inventory_id") inventory_id: String,
        @Field("assigned_to") assigned_to: String
    ): Response<AssignInventoryResponseModel>

    @POST(Constants.CHANGE_INVENTORY_STATUS)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun acceptRejectInventory(
        @Header("Authorization") header: String,
        @Field("inventory_id") inventory_id: String,
        @Field("status") status: String
    ): Response<AssignInventoryResponseModel>

    @POST(Constants.RESET_PASSWORD)
    @FormUrlEncoded
    suspend fun resetPasssword(
        @Field("user_id") user_id: String,
        @Field("new_password") new_password: String,
        @Field("confirm_new_password") confirm_new_password: String
    ): Response<ResetPasswordResponseModel>

    @GET(Constants.PROJECT_LISTING)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun getProjectList(
        @Header("Authorization") header: String,
    ): Response<ProjectListResponseModel>

    @POST(Constants.PAYMENT_STORE)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun storePayment(
        @Header("Authorization") header: String, @Body reqBody: PaymentStoreRequest
    ): Response<StorePaymentResponseModel>

    @POST(Constants.WALLET_TRANSACTIONS)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getTransactionsList(
        @Header("Authorization") header: String,
        @Field("status") status:Int,
    ): Response<WalletTransactionsResponseModel>

    @GET(Constants.HOME_INVENTORY)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun getHomeInventoryList(
        @Header("Authorization") header: String,
    ): Response<HomeInventoryResponseModel>

    @GET(Constants.ISSUE_TAG_CHECK_WALLET)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun issueTagCheckWallet(
        @Header("Authorization") header: String,
    ): Response<IssueTagCheckWalletResponseModel>

    @POST(Constants.CHECK_TAG_AVAILABILITY)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun checkTagAvailable(
        @Header("Authorization") header: String,
        @Field("tag_id") tag_id: String,
        ): Response<CheckTagAvailabilityResponseModel>






    @POST(Constants.SEND_OTP)
    @Headers(Constants.X_APP_NAME)
    @FormUrlEncoded
    suspend fun sendOtp(
        @Field("mobile") mobile: String
    ): Response<SendOtpResponseModel>

    @POST(Constants.VERIFY_OTP)
    @Headers(Constants.X_APP_NAME)
    @FormUrlEncoded
    suspend fun verifyOtp(
        @Field("mobile") mobile: String, @Field("otp") otp: String
    ): Response<VerifyOtpResponseModel>

    @GET(Constants.BRAND_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandList(
        @Header("Authorization") header: String,
        @Query("search") search: String,
        @Query("page_size") page_size: String,
        @Query("page") page: Int,
        @Query("expand") expand: String,
        @Query("fields") fields: String
    ): Response<BrandListModel>

    @GET(Constants.BRAND_PRODUCTS)
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandProductList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<BrandProductResponseModel>

    @GET(Constants.BRAND_OFFERS)
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandOffersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<BrandOfferResponseModel>

    @GET(Constants.BRAND_PRODUCT_RATE)
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandRateList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<BrandRateResponseModel>

    @GET("core/companies/{id}/plug_channel_counts/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandNetworkUserCPCount(
        @Header("Authorization") header: String, @Path("id") id: Int, @Query("brand") brandId: Int
    ): Response<BrandNetworkCPCount>

    @GET("core/companies/{id}/company_buyer_listing/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandNetworkUserCPCount1(
        @Header("Authorization") header: String,
        @Path("id") id: Int,
        @Query("brand") brandId: Int,
        @Query("company") company: Int,
    ): Response<List<NetworkCPCount1Item>>

    @GET(Constants.NETWORK_ORDERS_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkPendingOrdersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<NetworkPendingOrdersResponseModel>>

    @GET(Constants.NETWORK_ORDERS_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkOutstandingOrdersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<NetworkOutstandingOrdersResponseModel>>

    @GET(Constants.NETWORK_ORDERS_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkConfirmedOrdersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<NetworkConfirmedResponseModel>>

    @GET(Constants.NETWORK_ORDERS_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkCompletedOrdersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<NetworkCompletedOrdersResponseModel>>

    @GET(Constants.NETWORK_ORDERS_LIST)
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkCancelledOrdersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<NetworkCancelledOrdersResponseModel>>

    @GET("core/orders/{id}")
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkOrderDetails(
        @Header("Authorization") header: String,
        @Path("id") id: Int,
        @QueryMap query: Map<String, String>
    ): Response<NetworkOrderDetailsResponseModel>

    @GET("core/orders/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getOwnOrderDetails(
        @Header("Authorization") header: String,
        @Path("id") id: Int,
        @QueryMap query: Map<String, String>
    ): Response<OwnOrderDetailsResponseModel>

    /** Beats Module */

    @GET("core/companies/{id}/beat_cities/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getBeatCities(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<List<BeatCityResponseModel>>

    @GET("core/companies/{id}/unassigned_retailers_count/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getUnAssignedCount(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @Query("city") city: String
    ): Response<UnAssignedCountResponseModel>

    @GET("core/beat/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getCityBeatsList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<CityBeatsResponseModel>

    @GET("core/companies/{id}/unassigned_retailers/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getUnAssignedBeatsList(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<UnAssignedBeatResponseModel>

    @GET("core/beat/{id}/beat_retailers_detail/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getAssignedBeatDetails(
        @Header("Authorization") header: String,
        @Path("id") beatId: Int,
        @QueryMap query: Map<String, String>
    ): Response<AssignedBeatDetailsResponseModel>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getBeatInsights(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<BeatInsightsResponseModel>

    @GET("core/companies/{id}/channel_partner_listing/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getActiveCPList(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<ActiveCPResponseModel>

    @GET("core/companies/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getLeadCPList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<LeadCPResponseModel>

    @GET("user/users/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getUserProfile(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<UserProfileResponseModel>

    @GET("user/users/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getUserProfileCoOwner(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<CoOwnersListResponseModel>>

    @DELETE("user/users/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun deleteCoOwner(
        @Header("Authorization") header: String, @Path("id") id: Int
    ): Response<DeleteCoOwnerResponse>

    @POST("user/users/")
    @FormUrlEncoded
    @Headers(Constants.X_APP_NAME)
    suspend fun addCoOwner(
        @Header("Authorization") header: String, @FieldMap query: Map<String, String>
    ): Response<DeleteCoOwnerResponse>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("user/users/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun updateUser(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Part("first_name") body: RequestBody,
        @Part("last_name") body2: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<UserProfileResponseModel>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("user/users/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun updateUserWithOutImage(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Part("first_name") body: RequestBody,
        @Part("last_name") body2: RequestBody,
        @Part("email") email: RequestBody
    ): Response<UserProfileResponseModel>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("core/companies/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun updatePanCard(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Part image: MultipartBody.Part?
    ): Response<UserProfileResponseModel>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("core/companies/{id}/")
    @Headers(Constants.X_APP_NAME)
    suspend fun updatePanAadhaarCard(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Part imagePan: MultipartBody.Part?,
        @Part image: MultipartBody.Part?
    ): Response<UserProfileResponseModel>

    @GET("core/visit_log/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getCpRemarks(
        @Header("Authorization") header: String,
        @Query("company") companyId: String,
    ): Response<CpRemarksResponseModel>

    @GET("core/stocks/low_stock_alert/?")
    @Headers(Constants.X_APP_NAME)
    suspend fun getLowStock(
        @Header("Authorization") header: String,
        @Query("brand") brand: String,
        @Query("company") company: String
    ): Response<LowStockDataResponse>

    @GET("core/stocks/channel_partner_stock/?expand=product,product.brand")
    @Headers(Constants.X_APP_NAME)
    suspend fun getCpStocks(
        @Header("Authorization") header: String, @QueryMap query: HashMap<String, String>
    ): Response<List<CpStockResponseItem>>

    @GET("core/mapped_brands/?")
    @Headers(Constants.X_APP_NAME)
    suspend fun getBrandList1(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<BrandResponse>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getCpInsights(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<CpInsightsResponseModel>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getNetworkOrderCount(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<NetworkOrdersCountResponseModel>

    @GET("user/users/{id}/salesman_listing/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesManList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<SalesmanListingResponseModel>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesUserList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<List<SalesUserListResponseModel>>

    @GET("core/companies/{id}/target_insights/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesTargetsList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<List<SalesTargetAnalysisResponseModel>>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesBrandAnalysisInsights(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<SalesBrandAnalysisInsightsResponseModel>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesSKUsList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<SalesSKUsResponseModel>

    @GET("core/beatday/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesBeatsList(
        @Header("Authorization") header: String, @Query("user") userId: Int
    ): Response<SalesBeatResponseModel>

    @GET("user/users/{id}/salesman_monthly_target")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesCount(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<SalesBeatCountResponseModel>

    @GET("core/companies/{id}/dashboard")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesDailyAnalysis(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap query: Map<String, String>
    ): Response<SalesDailyAnalysisResponseModel>

    @GET("core/companies/{id}/dashboard/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getDailyAnalysisInsights(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<DailyAnalysisCalenderResponseModel>

    @GET("core/companies/{id}/active_inactive_users/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getActiveSalesmanList(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap query: Map<String, String>
    ): Response<List<ActiveSalesmanResponseModel>>

    @GET("core/user_location/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesLocations(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<SalesLocationResponseModel>

    @GET("core/target/get_target_data/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesTarget(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<TargetGraphResponse>

    @GET("core/target/get_user_data/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesTargetUsersList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<TargetUserData>>

    @GET("user/users/salesman/")
    @Headers(Constants.X_APP_NAME)
    suspend fun getSalesNetworkList(
        @Header("Authorization") header: String, @QueryMap query: Map<String, String>
    ): Response<List<SalesNetworkResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/leave/get_leaves_count/")
    suspend fun getLeaveCount(
        @Header("Authorization") header: String,
        @Query("month") month: String,
        @Query("year") year: String
    ): Response<GetLeaveCountData>

    @Headers(Constants.X_APP_NAME)
    @GET("core/leave/")
    suspend fun getLeaveList(
        @Header("Authorization") header: String, @QueryMap request: HashMap<String, String>
    ): Response<GetHRResponseList>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getSnapShotData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomePageSnapShotResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeFiltersData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomePageFiltersResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/user_status_count/")
    suspend fun getHomeSalesCountData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomePageSalesResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/salesman_search/")
    suspend fun getHomeSalesPersonsListData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<List<HomePageSalesPersonListResponsModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeInsightsOrderData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeInsightsOrderResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeReachAnalysisData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeReachAnalysisResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeRetailersData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeInsightRetailersResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeOrdersPlacedData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeInsightsOrderPlacedResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeSubCatData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeInsightsSubCatResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeOrderSummaryData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeOrderSummaryResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeOrderSummaryData1(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeOrderSummaryOrdersResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeSKUsData(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<HomeSKUsResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeSKUsItemDetails(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<List<HomeSKUsItemDetailsResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/target_dashboard/")
    suspend fun getHomeLeaderBoardSalesPersons(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<List<HomeLeaderBoardSPResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getHomeInsightsLastDays(
        @Header("Authorization") header: String,
        @Path("id") companyId: Int,
        @QueryMap request: HashMap<String, String>
    ): Response<List<HomeInsightsLastDaysResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/control-center/")
    suspend fun getSalesPerformanceList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap data: HashMap<String, String>
    ): Response<List<SalesPerformanceResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/companies/{id}/cp_dashboard/")
    suspend fun getCPLeaderBoardList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
        @QueryMap data: HashMap<String, String>
    ): Response<List<CPLeaderBoardResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/generate_reports/")
    suspend fun getLeaderBoardReport(
        @Header("Authorization") header: String
    ): Response<GenerateReportResponseModel>

    @Headers(Constants.X_APP_NAME)
    @GET("user/users/{id}/salesman_search/?is_manager=true")
    suspend fun getReportingManagerList(
        @Header("Authorization") header: String,
        @Path("id") id: String,
    ): Response<List<ReportingManagerResponseModel>>

    @Headers(Constants.X_APP_NAME)
    @GET("core/designations/?no-pagination=true")
    suspend fun getSalesDesignationsList(
        @Header("Authorization") header: String
    ): Response<List<DesignationSalesFilterResponseModel>>
}