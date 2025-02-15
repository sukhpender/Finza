package com.riggle.finza_finza.data.api

import com.riggle.finza_finza.data.model.AcceptRejectRequest
import com.riggle.finza_finza.data.model.AcceptRejectResponseModel
import com.riggle.finza_finza.data.model.ActivationsResponseModel
import com.riggle.finza_finza.data.model.AssignInventoryResponseModel
import com.riggle.finza_finza.data.model.BadResponseModel
import com.riggle.finza_finza.data.model.CancelRequest
import com.riggle.finza_finza.data.model.CancelledResponseModel
import com.riggle.finza_finza.data.model.CheckTagAvailabilityResponseModel
import com.riggle.finza_finza.data.model.CreateCustomerRew
import com.riggle.finza_finza.data.model.CreatePaymentLinkResponseModel
import com.riggle.finza_finza.data.model.DispatchUsersResponseModel
import com.riggle.finza_finza.data.model.DownloadDocResponseModel
import com.riggle.finza_finza.data.model.FinzaForgotPassResponseModel
import com.riggle.finza_finza.data.model.FinzaLoginResponseModel
import com.riggle.finza_finza.data.model.FinzaLogoutResponseModel
import com.riggle.finza_finza.data.model.FinzaProfileResponseModel
import com.riggle.finza_finza.data.model.ForwardUsersResponseModel
import com.riggle.finza_finza.data.model.GetVehicleDetailsResponseModel
import com.riggle.finza_finza.data.model.HomeInventoryResponseModel
import com.riggle.finza_finza.data.model.InventoryResponseModel1
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.data.model.IssueTagCheckWalletResponseModel
import com.riggle.finza_finza.data.model.IssueTagUserCreateResponseModel
import com.riggle.finza_finza.data.model.MultipleTransferResponseModel
import com.riggle.finza_finza.data.model.NeedFastagResponseModel
import com.riggle.finza_finza.data.model.PaymentStoreRequest
import com.riggle.finza_finza.data.model.ProjectListResponseModel
import com.riggle.finza_finza.data.model.RcDownloadedResponseModel
import com.riggle.finza_finza.data.model.RegisterTagRequest
import com.riggle.finza_finza.data.model.RegisterTagResponseModel
import com.riggle.finza_finza.data.model.ReplacementResponseModel
import com.riggle.finza_finza.data.model.ResendOtpResponseModel
import com.riggle.finza_finza.data.model.ResetPasswordResponseModel
import com.riggle.finza_finza.data.model.SendOtpIssueTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.StorePaymentResponseModel
import com.riggle.finza_finza.data.model.TagReplaceRequest
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
import retrofit2.http.Header
import retrofit2.http.Query

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

    suspend fun resendOtp(
        email: String,
    ): Response<ResendOtpResponseModel>

    suspend fun verifyOtpPass(
       user_id: String,
        otp: String,
    ): Response<VerifyOtpPasswordResponseModel>

    suspend fun sendOtpTagIssue(
        header: String, reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel>

    suspend fun verifyOtpTagIssue(
        header: String, reqBody: ValidateOtpRequest
    ): Response<VerifyOtpResponseModel>

    suspend fun vehicleMakersList(
        header: String, reqBody: VehicleMakersRequest
    ): Response<VehicleMakersListResponseModel>

    suspend fun vehicleModelList(
        header: String,reqBody: VehicleModelRequest
    ): Response<VehicleModelListResponseModel>

    suspend fun createCustomerBajaj(
        header: String, reqBody: CreateCustomerRew
    ): Response<IssueTagUserCreateResponseModel>

    suspend fun createWalletCustomer(
        header: String
    ): Response<WalletCreateCustomerResponseModel>

    suspend fun paymentLinkCreate(
       header: String,
        amount: String
    ): Response<CreatePaymentLinkResponseModel>

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

    suspend fun getForwardUsersList(
        header: String, status: String
    ): Response<DispatchUsersResponseModel>

    suspend fun getDispatchUsersList(
        header: String, status: String
    ): Response<ForwardUsersResponseModel>

    suspend fun getInventoriesOldList(
        header: String, status: String
    ): Response<InventoryResponseModel1>

    suspend fun getInventoriesList1(
        header: String, status: String,page: Int
    ): Response<InventoryResponseModel1>

    suspend fun getInventoriesList2(
        header: String, status: String,page: Int,user_id: Int
    ): Response<InventoryResponseModel1>

    suspend fun cancelInventory(
        header: String,
       reqBody: CancelRequest,
    ): Response<CancelledResponseModel>

    suspend fun acceptReject(
        header: String,
       reqBody: AcceptRejectRequest,
    ): Response<AcceptRejectResponseModel>

    suspend fun urtListing(
        header: String,
        month : String,
        year: String
    ): Response<UrtResponseModel>

    suspend fun badListing(
        header: String,
        month : String,
        year: String
    ): Response<BadResponseModel>

    suspend fun getFilterInventoriesList1(
        header: String,
       status: String,
       from_bar_code: String,
        to_bar_code: String,
        page: Int,
    ): Response<InventoryResponseModel1>

    suspend fun getFilterInventoriesList2(
        header: String,
       status: String,
       from_bar_code: String,
        to_bar_code: String,
        page: Int,
        userId:String
    ): Response<InventoryResponseModel1>

    suspend fun finzaUsersList(
        header: String,
    ): Response<UsersListResponseModel>

    suspend fun assignInventory(
        header: String, inventory_id: String, assigned_to: String
    ): Response<AssignInventoryResponseModel>

    suspend fun assignInventory1(
        header: String,
        reqBody: TransferRequest,
    ): Response<MultipleTransferResponseModel>

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

    suspend fun needFastag(
       header: String,
        provider: String,
    ): Response<NeedFastagResponseModel>

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
        inventory_id: RequestBody,
    ): Response<UploadDocumentResponseModel>

    suspend fun uploadDocument(
        header: String,
        audit_id: RequestBody,
        image: MultipartBody.Part,
        channel: RequestBody,
    ): Response<UploadDocResponseModel>

    suspend fun registerTag(
        header: String, reqBody: RegisterTagRequest
    ): Response<RegisterTagResponseModel>

    suspend fun activations(
        header: String,month: String,
    ): Response<ActivationsResponseModel>

    suspend fun tagReplacement(
        header: String, reqBody: TagReplaceRequest
    ): Response<ReplacementResponseModel>

    suspend fun getVehicleDetails(token: String,rc_no: String, type: Int,
    ): Response<GetVehicleDetailsResponseModel>

    suspend fun downloadPdf(
        header: String,
        rc_no: String,
        type: Int,
        status: Int
    ): Response<DownloadDocResponseModel>

    suspend fun downloadRcList(header: String,page: Int): Response<RcDownloadedResponseModel>
}