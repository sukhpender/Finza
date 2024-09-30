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
import com.riggle.plug.data.model.CreateCustomerRew
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
import com.riggle.plug.data.model.IssueTagUserCreateResponseModel
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
import com.riggle.plug.data.model.RegisterTagRequest
import com.riggle.plug.data.model.RegisterTagResponseModel
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
import com.riggle.plug.data.model.UpdateProjectResponseModel
import com.riggle.plug.data.model.UploadDocumentResponseModel
import com.riggle.plug.data.model.UserProfileResponseModel
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.data.model.UsersListResponseModel
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersListResponseModel
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpRequest
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

    @POST(Constants.VERIFY_OTP_TAG_ISSUE)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun verifyOtpTagIssue(
        @Header("Authorization") header: String, @Body reqBody: ValidateOtpRequest
    ): Response<VerifyOtpResponseModel>

    @POST(Constants.VEHICLE_MAKERS_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun vehicleMakersList(
        @Header("Authorization") header: String, @Body reqBody: VehicleMakersRequest
    ): Response<VehicleMakersListResponseModel>

    @POST(Constants.CREATE_CUSTOMER_BAJAJ)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun createCustomerBajaj(
        @Header("Authorization") header: String, @Body reqBody: CreateCustomerRew
    ): Response<IssueTagUserCreateResponseModel>

    @POST(Constants.WALLET_CREATE_CUSTOMER)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun createWalletCustomer(
        @Header("Authorization") header: String
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

    @Headers(Constants.X_APP_ACCEPT)
    @POST(Constants.UPDATE_PROFILE)
    @FormUrlEncoded
    suspend fun updateUserWithoutImage(
        @Header("Authorization") header: String,
        @Field("first_name") f_name: String,
        @Field("last_name") l_name: String,
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

    @POST(Constants.UPDATE_PROJECT)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun updateProject(
        @Header("Authorization") header: String,
        @Field("project_id") project_id: String
    ): Response<UpdateProjectResponseModel>

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

    @JvmSuppressWildcards
    @Multipart
    @POST(Constants.UPLOAD_DOCUMENTS)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun bajajUploadDocument(
        @Header("Authorization") header: String,
        @Part("requestId") requestId: RequestBody,
        @Part("sessionId") sessionId: RequestBody,
        @Part("channel") channel: RequestBody,
        @Part("agentId") agentId: RequestBody,
        @Part("reqDateTime") reqDateTime: RequestBody,
        @Part("imageType") imageType: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("provider") provider: RequestBody,
    ): Response<UploadDocumentResponseModel>

    @POST(Constants.REGISTER_TAG)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun registerTag(
        @Header("Authorization") header: String, @Body reqBody: RegisterTagRequest
    ): Response<RegisterTagResponseModel>

}