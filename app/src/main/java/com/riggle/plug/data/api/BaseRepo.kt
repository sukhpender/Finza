package com.riggle.plug.data.api

import com.riggle.plug.data.model.AssignInventoryResponseModel
import com.riggle.plug.data.model.CheckTagAvailabilityResponseModel
import com.riggle.plug.data.model.CreateCustomerRew
import com.riggle.plug.data.model.FinzaForgotPassResponseModel
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.data.model.HomeInventoryResponseModel
import com.riggle.plug.data.model.InventryResponseModel
import com.riggle.plug.data.model.IssueTagCheckWalletResponseModel
import com.riggle.plug.data.model.IssueTagUserCreateResponseModel
import com.riggle.plug.data.model.PaymentStoreRequest
import com.riggle.plug.data.model.ProjectListResponseModel
import com.riggle.plug.data.model.RegisterTagRequest
import com.riggle.plug.data.model.RegisterTagResponseModel
import com.riggle.plug.data.model.ResetPasswordResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.StorePaymentResponseModel
import com.riggle.plug.data.model.UpdateProjectResponseModel
import com.riggle.plug.data.model.UploadDocumentResponseModel
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.data.model.UsersListResponseModel
import com.riggle.plug.data.model.ValidateOtpRequest
import com.riggle.plug.data.model.VehicleMakersListResponseModel
import com.riggle.plug.data.model.VehicleMakersRequest
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.data.model.WalletCreateCustomerResponseModel
import com.riggle.plug.data.model.WalletTransactionsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface BaseRepo {

    suspend fun finzaLogin(
        email: String, password: String
    ): Response<FinzaLoginResponseModel>

    suspend fun finzaLogout(
        token: String
    ): Response<FinzaLogoutResponseModel>

    suspend fun finzaGetProfile(
        token: String,
    ): Response<FinzaProfileResponseModel>

    suspend fun forgotPass(
        email: String,
    ): Response<FinzaForgotPassResponseModel>

    suspend fun sendOtpTagIssue(
        header: String, reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel>

    suspend fun verifyOtpTagIssue(
        header: String, reqBody: ValidateOtpRequest
    ): Response<VerifyOtpResponseModel>

    suspend fun vehicleMakersList(
        header: String, reqBody: VehicleMakersRequest
    ): Response<VehicleMakersListResponseModel>

    suspend fun createCustomerBajaj(
        header: String, reqBody: CreateCustomerRew
    ): Response<IssueTagUserCreateResponseModel>

    suspend fun createWalletCustomer(
        header: String
    ): Response<WalletCreateCustomerResponseModel>

    suspend fun updateUserProfile(
        header: String, body: RequestBody, body2: RequestBody, profile_image: MultipartBody.Part
    ): Response<FinzaProfileResponseModel>

    suspend fun updateUserWithoutImage(
        header: String,
        f_name: String,
        l_name: String,
    ): Response<FinzaProfileResponseModel>

    suspend fun finzaUserWallet(
        header: String,
    ): Response<UserWalletResponseModel>

    suspend fun getInventoriesList(
        header: String, status: String
    ): Response<InventryResponseModel>

    suspend fun finzaUsersList(
        header: String,
    ): Response<UsersListResponseModel>

    suspend fun assignInventory(
        header: String, inventory_id: String, assigned_to: String
    ): Response<AssignInventoryResponseModel>

    suspend fun resetPasssword(
        user_id: String, new_password: String, confirm_new_password: String
    ): Response<ResetPasswordResponseModel>

    suspend fun getProjectList(
        header: String,
    ): Response<ProjectListResponseModel>

    suspend fun updateProject(
        header: String, project_id: String
    ): Response<UpdateProjectResponseModel>

    suspend fun acceptRejectInventory(
        header: String, inventory_id: String, status: String
    ): Response<AssignInventoryResponseModel>

    suspend fun storePayment(
        header: String, reqBody: PaymentStoreRequest
    ): Response<StorePaymentResponseModel>

    suspend fun getTransactionsList(
        header: String, status: Int
    ): Response<WalletTransactionsResponseModel>

    suspend fun getHomeInventoryList(
        header: String,
    ): Response<HomeInventoryResponseModel>

    suspend fun issueTagCheckWallet(
        header: String,
    ): Response<IssueTagCheckWalletResponseModel>

    suspend fun checkTagAvailable(
        header: String,
        tag_id: String,
    ): Response<CheckTagAvailabilityResponseModel>

    suspend fun bajajUploadDocument(
        header: String,
        requestId: RequestBody,
        sessionId: RequestBody,
        channel: RequestBody,
        agentId: RequestBody,
        reqDateTime: RequestBody,
        imageType: RequestBody,
        image: MultipartBody.Part,
        provider: RequestBody,
    ): Response<UploadDocumentResponseModel>

    suspend fun registerTag(
        header: String, reqBody: RegisterTagRequest
    ): Response<RegisterTagResponseModel>
}