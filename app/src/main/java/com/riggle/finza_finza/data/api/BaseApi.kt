package com.riggle.finza_finza.data.api

import com.riggle.finza_finza.data.model.AcceptRejectRequest
import com.riggle.finza_finza.data.model.AcceptRejectResponseModel
import com.riggle.finza_finza.data.model.ActivationsResponseModel
import com.riggle.finza_finza.data.model.AssignInventoryResponseModel
import com.riggle.finza_finza.data.model.BadResponseModel
import com.riggle.finza_finza.data.model.CancelRequest
import com.riggle.finza_finza.data.model.Cancelled1
import com.riggle.finza_finza.data.model.CancelledResponseModel
import com.riggle.finza_finza.data.model.CheckTagAvailabilityResponseModel
import com.riggle.finza_finza.data.model.CreateCustomerRew
import com.riggle.finza_finza.data.model.CreatePaymentLinkResponseModel
import com.riggle.finza_finza.data.model.DispatchUsersResponseModel
import com.riggle.finza_finza.data.model.FinzaForgotPassResponseModel
import com.riggle.finza_finza.data.model.FinzaLoginResponseModel
import com.riggle.finza_finza.data.model.FinzaLogoutResponseModel
import com.riggle.finza_finza.data.model.FinzaProfileResponseModel
import com.riggle.finza_finza.data.model.ForwardUsersResponseModel
import com.riggle.finza_finza.data.model.HomeInventoryResponseModel
import com.riggle.finza_finza.data.model.InventoryResponseModel1
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.data.model.IssueTagCheckWalletResponseModel
import com.riggle.finza_finza.data.model.IssueTagUserCreateResponseModel
import com.riggle.finza_finza.data.model.MultipleTransferResponseModel
import com.riggle.finza_finza.data.model.NeedFastagResponseModel
import com.riggle.finza_finza.data.model.PaymentStoreRequest
import com.riggle.finza_finza.data.model.ProjectListResponseModel
import com.riggle.finza_finza.data.model.RegisterTagRequest
import com.riggle.finza_finza.data.model.RegisterTagResponseModel
import com.riggle.finza_finza.data.model.ReplacementResponseModel
import com.riggle.finza_finza.data.model.ResendOtpResponseModel
import com.riggle.finza_finza.data.model.ResetPasswordResponseModel
import com.riggle.finza_finza.data.model.SendOtpIssueTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.StorePaymentResponseModel
import com.riggle.finza_finza.data.model.TagReplaceRequest
import com.riggle.finza_finza.data.model.Transfer
import com.riggle.finza_finza.data.model.TransferRequest
import com.riggle.finza_finza.data.model.UpdateProjectResponseModel
import com.riggle.finza_finza.data.model.UploadDocResponseModel
import com.riggle.finza_finza.data.model.UploadDocumentResponseModel
import com.riggle.finza_finza.data.model.UrtResponseModel
import com.riggle.finza_finza.data.model.UserWalletResponseModel
import com.riggle.finza_finza.data.model.UsersListResponseModel
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.VehicleMakersListResponseModel
import com.riggle.finza_finza.data.model.VehicleMakersRequest
import com.riggle.finza_finza.data.model.VehicleModelListResponseModel
import com.riggle.finza_finza.data.model.VehicleModelRequest
import com.riggle.finza_finza.data.model.VerifyOtpPasswordResponseModel
import com.riggle.finza_finza.data.model.VerifyOtpResponseModel
import com.riggle.finza_finza.data.model.WalletCreateCustomerResponseModel
import com.riggle.finza_finza.data.model.WalletTransactionsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

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

    @POST(Constants.RESEND_OTP)
    @FormUrlEncoded
    suspend fun resendOtp(
        @Field("email") email: String,
    ): Response<ResendOtpResponseModel>

    @POST(Constants.VERIFY_OTP_PASSWORD)
    @FormUrlEncoded
    suspend fun verifyOtpPass(
        @Field("user_id") user_id: String,
        @Field("otp") otp: String,
    ): Response<VerifyOtpPasswordResponseModel>

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

    @POST(Constants.TAG_REPLACEMENT)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun tagReplacement(
        @Header("Authorization") header: String, @Body reqBody: TagReplaceRequest
    ): Response<ReplacementResponseModel>

    @POST(Constants.VEHICLE_MAKERS_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun vehicleMakersList(
        @Header("Authorization") header: String, @Body reqBody: VehicleMakersRequest
    ): Response<VehicleMakersListResponseModel>


    @POST(Constants.VEHICLE_MODE_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun vehicleModelList(
        @Header("Authorization") header: String, @Body reqBody: VehicleModelRequest
    ): Response<VehicleModelListResponseModel>

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

    @POST(Constants.PAYMENT_LINK_CREATE)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun paymentLinkCreate(
        @Header("Authorization") header: String, @Field("amount") amount: String
    ): Response<CreatePaymentLinkResponseModel>

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

    @POST(Constants.ACTIVATIONS)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun activations(
        @Header("Authorization") header: String,
        @Field("month") month: String,
    ): Response<ActivationsResponseModel>

    @POST(Constants.INVENTORY_LIST)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getInventoriesList(
        @Header("Authorization") header: String, @Field("status") status: String
    ): Response<InventryResponseModel>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getForwardUSerList(
        @Header("Authorization") header: String, @Field("status") status: String
    ): Response<DispatchUsersResponseModel>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getDispatchUSerList(
        @Header("Authorization") header: String, @Field("status") status: String
    ): Response<ForwardUsersResponseModel>

 @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getInventoriesOldList(
        @Header("Authorization") header: String, @Field("status") status: String
    ): Response<InventoryResponseModel1>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getInventoriesList1(
        @Header("Authorization") header: String,
        @Field("status") status: String,
        @Field("page") page: Int
    ): Response<InventoryResponseModel1>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getInventoriesList2(
        @Header("Authorization") header: String,
        @Field("status") status: String,
        @Field("page") page: Int,
        @Field("user_id") user_id: Int
    ): Response<InventoryResponseModel1>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getFilterInventoriesList1(
        @Header("Authorization") header: String,
        @Field("status") status: String,
        @Field("from_bar_code") from_bar_code: String,
        @Field("to_bar_code") to_bar_code: String,
        @Field("page") page: Int,
    ): Response<InventoryResponseModel1>

    @POST(Constants.INVENTORY_LIST1)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getFilterInventoriesList2(
        @Header("Authorization") header: String,
        @Field("status") status: String,
        @Field("from_bar_code") from_bar_code: String,
        @Field("to_bar_code") to_bar_code: String,
        @Field("page") page: Int,
        @Field("user_id") user_id: String
    ): Response<InventoryResponseModel1>

    @POST(Constants.ASSIGN_INVENTORY)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun assignInventory(
        @Header("Authorization") header: String,
        @Field("inventory_id") inventory_id: String,
        @Field("assigned_to") assigned_to: String
    ): Response<AssignInventoryResponseModel>


    @POST(Constants.CANCEL_INVENTORY2)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun cancelInventory(
        @Header("Authorization") header: String,
        @Body reqBody: CancelRequest,
    ): Response<CancelledResponseModel>

    @POST(Constants.ACCEPT_REJECT)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun acceptReject(
        @Header("Authorization") header: String,
        @Body reqBody: AcceptRejectRequest,
    ): Response<AcceptRejectResponseModel>

    @GET(Constants.URT_LISTING)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun urtListing(
        @Header("Authorization") header: String,
        @Query("month ") month : String,
        @Query("year") year: String
    ): Response<UrtResponseModel>

    @GET(Constants.BAD_LISTING)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun badListing(
        @Header("Authorization") header: String,
        @Query("month ") month : String,
        @Query("year") year: String
    ): Response<BadResponseModel>

    @POST(Constants.ASSIGN_INVENTORY1)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun assignInventory1(
        @Header("Authorization") header: String,
        @Body reqBody: TransferRequest,
    ): Response<MultipleTransferResponseModel>

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

    @POST(Constants.NEED_FASTAG)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun needFastag(
        @Header("Authorization") header: String,
        @Field("provider") provider: String,
    ): Response<NeedFastagResponseModel>

    @POST(Constants.UPDATE_PROJECT)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun updateProject(
        @Header("Authorization") header: String, @Field("project_id") project_id: String
    ): Response<UpdateProjectResponseModel>

    @POST(Constants.WALLET_TRANSACTIONS)
    @Headers(Constants.X_APP_ACCEPT)
    @FormUrlEncoded
    suspend fun getTransactionsList(
        @Header("Authorization") header: String,
        @Field("status") status: Int,
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
        @Part("inventory_id") inventory_id: RequestBody,
    ): Response<UploadDocumentResponseModel>

    @JvmSuppressWildcards
    @Multipart
    @POST(Constants.UPLOAD_DOCUMENTS1)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun uploadDocument(
        @Header("Authorization") header: String,
        @Part("audit_id") requestId: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody,
        ): Response<UploadDocResponseModel>

    @POST(Constants.REGISTER_TAG)
    @Headers(Constants.X_APP_ACCEPT)
    suspend fun registerTag(
        @Header("Authorization") header: String, @Body reqBody: RegisterTagRequest
    ): Response<RegisterTagResponseModel>

}