package com.riggle.finza_finza.data.api

import com.riggle.finza_finza.data.model.AssignInventoryResponseModel
import com.riggle.finza_finza.data.model.CheckTagAvailabilityResponseModel
import com.riggle.finza_finza.data.model.CreateCustomerRew
import com.riggle.finza_finza.data.model.CreatePaymentLinkResponseModel
import com.riggle.finza_finza.data.model.FinzaForgotPassResponseModel
import com.riggle.finza_finza.data.model.FinzaLoginResponseModel
import com.riggle.finza_finza.data.model.FinzaLogoutResponseModel
import com.riggle.finza_finza.data.model.FinzaProfileResponseModel
import com.riggle.finza_finza.data.model.HomeInventoryResponseModel
import com.riggle.finza_finza.data.model.InventryResponseModel
import com.riggle.finza_finza.data.model.IssueTagCheckWalletResponseModel
import com.riggle.finza_finza.data.model.IssueTagUserCreateResponseModel
import com.riggle.finza_finza.data.model.PaymentStoreRequest
import com.riggle.finza_finza.data.model.ProjectListResponseModel
import com.riggle.finza_finza.data.model.RegisterTagRequest
import com.riggle.finza_finza.data.model.RegisterTagResponseModel
import com.riggle.finza_finza.data.model.ResetPasswordResponseModel
import com.riggle.finza_finza.data.model.SendOtpIssueTagResponseModel
import com.riggle.finza_finza.data.model.SendOtpRequest
import com.riggle.finza_finza.data.model.StorePaymentResponseModel
import com.riggle.finza_finza.data.model.UpdateProjectResponseModel
import com.riggle.finza_finza.data.model.UploadDocumentResponseModel
import com.riggle.finza_finza.data.model.UserWalletResponseModel
import com.riggle.finza_finza.data.model.UsersListResponseModel
import com.riggle.finza_finza.data.model.ValidateOtpRequest
import com.riggle.finza_finza.data.model.VehicleMakersListResponseModel
import com.riggle.finza_finza.data.model.VehicleMakersRequest
import com.riggle.finza_finza.data.model.VerifyOtpResponseModel
import com.riggle.finza_finza.data.model.WalletCreateCustomerResponseModel
import com.riggle.finza_finza.data.model.WalletTransactionsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(private val apiService: BaseApi) : BaseRepo {
    override suspend fun finzaLogin(
        email: String, password: String
    ): Response<FinzaLoginResponseModel> {
        return apiService.finzaLogin(email, password)
    }

    override suspend fun finzaLogout(token: String): Response<FinzaLogoutResponseModel> {
        return apiService.finzaLogOut(token)
    }

    override suspend fun finzaGetProfile(token: String): Response<FinzaProfileResponseModel> {
        return apiService.finzaGetProfile(token)
    }

    override suspend fun forgotPass(email: String): Response<FinzaForgotPassResponseModel> {
        return apiService.forgotPass(email)
    }

    override suspend fun sendOtpTagIssue(
        header: String, reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel> {
        return apiService.sendOtpTagIssue(header, reqBody)
    }

    override suspend fun verifyOtpTagIssue(
        header: String, reqBody: ValidateOtpRequest
    ): Response<VerifyOtpResponseModel> {
        return apiService.verifyOtpTagIssue(header, reqBody)
    }

    override suspend fun vehicleMakersList(
        header: String, reqBody: VehicleMakersRequest
    ): Response<VehicleMakersListResponseModel> {
        return apiService.vehicleMakersList(header, reqBody)
    }

    override suspend fun createCustomerBajaj(
        header: String, reqBody: CreateCustomerRew
    ): Response<IssueTagUserCreateResponseModel> {
        return apiService.createCustomerBajaj(header, reqBody)
    }

    override suspend fun createWalletCustomer(
        header: String
    ): Response<WalletCreateCustomerResponseModel> {
        return apiService.createWalletCustomer(header)
    }

    override suspend fun paymentLinkCreate(
        header: String,
        amount: String
    ): Response<CreatePaymentLinkResponseModel> {
        return apiService.paymentLinkCreate(header,amount)
    }

    override suspend fun updateUserProfile(
        header: String, body: RequestBody, body2: RequestBody, profile_image: MultipartBody.Part
    ): Response<FinzaProfileResponseModel> {
        return apiService.updateUserProfile(header, body, body2, profile_image)
    }

    override suspend fun updateUserWithoutImage(
        header: String, f_name: String, l_name: String
    ): Response<FinzaProfileResponseModel> {
        return apiService.updateUserWithoutImage(header, f_name, l_name)
    }

    override suspend fun finzaUserWallet(header: String): Response<UserWalletResponseModel> {
        return apiService.finzaUserWallet(header)
    }

    override suspend fun getInventoriesList(
        header: String, status: String
    ): Response<InventryResponseModel> {
        return apiService.getInventoriesList(header, status)
    }

    override suspend fun finzaUsersList(header: String): Response<UsersListResponseModel> {
        return apiService.finzaUsersList(header)
    }

    override suspend fun assignInventory(
        header: String, inventory_id: String, assigned_to: String
    ): Response<AssignInventoryResponseModel> {
        return apiService.assignInventory(header, inventory_id, assigned_to)
    }

    override suspend fun resetPasssword(
        user_id: String, new_password: String, confirm_new_password: String
    ): Response<ResetPasswordResponseModel> {
        return apiService.resetPasssword(user_id, new_password, confirm_new_password)
    }

    override suspend fun getProjectList(header: String): Response<ProjectListResponseModel> {
        return apiService.getProjectList(header)
    }

    override suspend fun updateProject(
        header: String, project_id: String
    ): Response<UpdateProjectResponseModel> {
        return apiService.updateProject(header, project_id)
    }

    override suspend fun acceptRejectInventory(
        header: String, inventory_id: String, status: String
    ): Response<AssignInventoryResponseModel> {
        return apiService.acceptRejectInventory(header, inventory_id, status)
    }

    override suspend fun storePayment(
        header: String, reqBody: PaymentStoreRequest
    ): Response<StorePaymentResponseModel> {
        return apiService.storePayment(header, reqBody)
    }

    override suspend fun getTransactionsList(
        header: String,
        status: Int
    ): Response<WalletTransactionsResponseModel> {
        return apiService.getTransactionsList(header, status)
    }

    override suspend fun getHomeInventoryList(header: String): Response<HomeInventoryResponseModel> {
        return apiService.getHomeInventoryList(header)
    }

    override suspend fun issueTagCheckWallet(header: String): Response<IssueTagCheckWalletResponseModel> {
        return apiService.issueTagCheckWallet(header)
    }

    override suspend fun checkTagAvailable(
        header: String, tag_id: String
    ): Response<CheckTagAvailabilityResponseModel> {
        return apiService.checkTagAvailable(header, tag_id)
    }

    override suspend fun bajajUploadDocument(
        header: String,
        requestId: RequestBody,
        sessionId: RequestBody,
        channel: RequestBody,
        agentId: RequestBody,
        reqDateTime: RequestBody,
        imageType: RequestBody,
        image: MultipartBody.Part,
        provider: RequestBody
    ): Response<UploadDocumentResponseModel> {
        return apiService.bajajUploadDocument(
            header, requestId, sessionId, channel, agentId, reqDateTime, imageType, image, provider
        )
    }

    override suspend fun registerTag(
        header: String,
        reqBody: RegisterTagRequest
    ): Response<RegisterTagResponseModel> {
        return apiService.registerTag(header,reqBody)
    }
}