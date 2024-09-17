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
import com.riggle.plug.data.model.FinzaLoginData
import com.riggle.plug.data.model.FinzaLoginResponseModel
import com.riggle.plug.data.model.FinzaLogoutResponseModel
import com.riggle.plug.data.model.FinzaProfileModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.data.model.FinzaUpdateProfileResponseModel
import com.riggle.plug.data.model.GenerateReportResponseModel
import com.riggle.plug.data.model.GetHRResponseList
import com.riggle.plug.data.model.GetLeaveCountData
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
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(private val apiService: BaseApi) : BaseRepo {
    override suspend fun finzaLogin(
        email: String,
        password: String
    ): Response<FinzaLoginResponseModel> {
        return apiService.finzaLogin(email,password)
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
        header: String,
        reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel> {
        return apiService.sendOtpTagIssue(header,reqBody)
    }

    override suspend fun createWalletCustomer(
        header: String,
        name: String,
        email: String,
        phone: String
    ): Response<WalletCreateCustomerResponseModel> {
        return apiService.createWalletCustomer(header,name,email,phone)
    }

    override suspend fun updateUserProfile(
        header: String,
        body: RequestBody,
        body2: RequestBody,
        profile_image: MultipartBody.Part
    ): Response<FinzaProfileResponseModel> {
        return apiService.updateUserProfile(header,body,body2,profile_image)
    }

    override suspend fun finzaUserWallet(header: String): Response<UserWalletResponseModel> {
        return apiService.finzaUserWallet(header)
    }

    override suspend fun getInventoriesList(
        header: String,
        status: String
    ): Response<InventryResponseModel> {
        return apiService.getInventoriesList(header,status)
    }

    override suspend fun finzaUsersList(header: String): Response<UsersListResponseModel> {
        return apiService.finzaUsersList(header)
    }

    override suspend fun assignInventory(
        header: String,
        inventory_id: String,
        assigned_to: String
    ): Response<AssignInventoryResponseModel> {
        return apiService.assignInventory(header, inventory_id, assigned_to)
    }

    override suspend fun resetPasssword(
        user_id: String,
        new_password: String,
        confirm_new_password: String
    ): Response<ResetPasswordResponseModel> {
        return apiService.resetPasssword(user_id,new_password,confirm_new_password)
    }

    override suspend fun getProjectList(header: String): Response<ProjectListResponseModel> {
        return apiService.getProjectList(header)
    }

    override suspend fun acceptRejectInventory(
        header: String,
        inventory_id: String,
        status: String
    ): Response<AssignInventoryResponseModel> {
        return apiService.acceptRejectInventory(header,inventory_id,status)
    }

    override suspend fun storePayment(
        header: String,
        reqBody: PaymentStoreRequest
    ): Response<StorePaymentResponseModel> {
        return apiService.storePayment(header,reqBody)
    }

    override suspend fun getTransactionsList(header: String): Response<WalletTransactionsResponseModel> {
        return apiService.getTransactionsList(header)
    }

    override suspend fun getHomeInventoryList(header: String): Response<HomeInventoryResponseModel> {
        return apiService.getHomeInventoryList(header)
    }

    override suspend fun issueTagCheckWallet(header: String): Response<IssueTagCheckWalletResponseModel> {
        return apiService.issueTagCheckWallet(header)
    }

    override suspend fun checkTagAvailable(
        header: String,
        tag_id: String
    ): Response<CheckTagAvailabilityResponseModel> {
        return apiService.checkTagAvailable(header,tag_id)
    }


    override suspend fun sendOtp(mobile: String): Response<SendOtpResponseModel> {
        return apiService.sendOtp(mobile)
    }

    override suspend fun verifyOtp(
        mobile: String, otp: String
    ): Response<VerifyOtpResponseModel> {
        return apiService.verifyOtp(mobile, otp)
    }

    override suspend fun getBrandList(
        header: String, search: String, page_size: String, page: Int, expand: String, fields: String
    ): Response<BrandListModel> {
        return apiService.getBrandList(header, search, page_size, page, expand, fields)
    }

    override suspend fun getBrandProductList(
        header: String, query: Map<String, String>
    ): Response<BrandProductResponseModel> {
        return apiService.getBrandProductList(header, query)
    }

    override suspend fun getBrandOffersList(
        header: String, query: Map<String, String>
    ): Response<BrandOfferResponseModel> {
        return apiService.getBrandOffersList(header, query)
    }

    override suspend fun getBrandRateList(
        header: String, query: Map<String, String>
    ): Response<BrandRateResponseModel> {
        return apiService.getBrandRateList(header, query)
    }

    override suspend fun getBrandNetworkUserCPCount(
        header: String, brand: Int, id: Int
    ): Response<BrandNetworkCPCount> {
        return apiService.getBrandNetworkUserCPCount(header, id, brand)
    }

    override suspend fun getBrandNetworkUserCPCount1(
        header: String, id: Int, brand: Int, company: Int
    ): Response<List<NetworkCPCount1Item>> {
        return apiService.getBrandNetworkUserCPCount1(header, id, brand, company)
    }

    override suspend fun getNetworkPendingOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkPendingOrdersResponseModel>> {
        return apiService.getNetworkPendingOrdersList(header, query)
    }

    override suspend fun getNetworkOutstandingOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkOutstandingOrdersResponseModel>> {
        return apiService.getNetworkOutstandingOrdersList(header, query)
    }

    override suspend fun getNetworkConfirmedOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkConfirmedResponseModel>> {
        return apiService.getNetworkConfirmedOrdersList(header, query)
    }

    override suspend fun getNetworkCompletedOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkCompletedOrdersResponseModel>> {
        return apiService.getNetworkCompletedOrdersList(header, query)
    }

    override suspend fun getNetworkCancelledOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkCancelledOrdersResponseModel>> {
        return apiService.getNetworkCancelledOrdersList(header, query)
    }

    override suspend fun getNetworkOrderDetails(
        header: String, query: Map<String, String>, id: Int
    ): Response<NetworkOrderDetailsResponseModel> {
        return apiService.getNetworkOrderDetails(header, id, query)
    }

    override suspend fun getOwnOrderDetails(
        header: String, query: Map<String, String>, id: Int
    ): Response<OwnOrderDetailsResponseModel> {
        return apiService.getOwnOrderDetails(header, id, query)
    }

    override suspend fun getBeatCities(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<List<BeatCityResponseModel>> {
        return apiService.getBeatCities(header, companyId, query)
    }

    override suspend fun getUnAssignedCount(
        header: String, companyId: Int, city: String
    ): Response<UnAssignedCountResponseModel> {
        return apiService.getUnAssignedCount(header, companyId, city)
    }

    override suspend fun getCityBeatsList(
        header: String, query: Map<String, String>
    ): Response<CityBeatsResponseModel> {
        return apiService.getCityBeatsList(header, query)
    }

    override suspend fun getUnAssignedBeatsList(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<UnAssignedBeatResponseModel> {
        return apiService.getUnAssignedBeatsList(header, companyId, query)
    }

    override suspend fun getAssignedBeatDetails(
        header: String, beatId: Int, query: Map<String, String>
    ): Response<AssignedBeatDetailsResponseModel> {
        return apiService.getAssignedBeatDetails(header, beatId, query)
    }

    override suspend fun getBeatInsights(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<BeatInsightsResponseModel> {
        return apiService.getBeatInsights(header, companyId, query)
    }

    override suspend fun getActiveCPList(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<ActiveCPResponseModel> {
        return apiService.getActiveCPList(header, companyId, query)
    }

    override suspend fun getLeadCPList(
        header: String, query: Map<String, String>
    ): Response<LeadCPResponseModel> {
        return apiService.getLeadCPList(header, query)
    }

    override suspend fun getUserProfile(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<UserProfileResponseModel> {
        return apiService.getUserProfile(header, companyId, query)
    }

    override suspend fun getUserProfileCoOwner(
        header: String, query: Map<String, String>
    ): Response<List<CoOwnersListResponseModel>> {
        return apiService.getUserProfileCoOwner(header, query)
    }

    override suspend fun deleteCoOwner(
        header: String, id: Int
    ): Response<DeleteCoOwnerResponse> {
        return apiService.deleteCoOwner(header, id)
    }

    override suspend fun addCoOwner(
        header: String, query: Map<String, String>
    ): Response<DeleteCoOwnerResponse> {
        return apiService.addCoOwner(header, query)
    }

    override suspend fun updateUser(
        header: String,
        id: String,
        f_name: RequestBody,
        l_name: RequestBody,
        email: RequestBody,
        image: MultipartBody.Part
    ): Response<UserProfileResponseModel> {
        return apiService.updateUser(header, id, f_name, l_name, email, image)
    }

    override suspend fun updateUserWithOutImage(
        header: String, id: String, f_name: RequestBody, l_name: RequestBody, email: RequestBody
    ): Response<UserProfileResponseModel> {
        return apiService.updateUserWithOutImage(header, id, f_name, l_name, email)
    }

    override suspend fun updatePanCard(
        authkey: String, id: String, image: MultipartBody.Part?
    ): Response<UserProfileResponseModel> {
        return apiService.updatePanCard(authkey, id, image)
    }

    override suspend fun updatePanAadhaarCard(
        authkey: String, id: String, imagePan: MultipartBody.Part?, imageAadhar: MultipartBody.Part?
    ): Response<UserProfileResponseModel> {
        return apiService.updatePanAadhaarCard(authkey, id, imagePan, imageAadhar)
    }

    override suspend fun getCpRemarks(
        header: String, companyId: String
    ): Response<CpRemarksResponseModel> {
        return apiService.getCpRemarks(header, companyId)
    }

    override suspend fun getLowStock(
        header: String, brand: String, company: String
    ): Response<LowStockDataResponse> {
        return apiService.getLowStock(header, brand, company)
    }

    override suspend fun getCpStocks(
        header: String, query: HashMap<String, String>
    ): Response<List<CpStockResponseItem>> {
        return apiService.getCpStocks(header, query)
    }

    override suspend fun getBrandList1(
        header: String, query: Map<String, String>
    ): Response<BrandResponse> {
        return apiService.getBrandList1(header, query)
    }

    override suspend fun getCpInsights(
        header: String, id: String, query: Map<String, String>
    ): Response<CpInsightsResponseModel> {
        return apiService.getCpInsights(header, id, query)
    }

    override suspend fun getNetworkOrderCount(
        header: String, id: String, query: Map<String, String>
    ): Response<NetworkOrdersCountResponseModel> {
        return apiService.getNetworkOrderCount(header, id, query)
    }

    override suspend fun getSalesManList(
        header: String, companyId: String, query: Map<String, String>
    ): Response<SalesmanListingResponseModel> {
        return apiService.getSalesManList(header, companyId, query)
    }

    override suspend fun getSalesUserList(
        header: String, companyId: String, query: Map<String, String>
    ): Response<List<SalesUserListResponseModel>> {
        return apiService.getSalesUserList(header, companyId, query)
    }

    override suspend fun getSalesTargetsList(
        header: String, id: String, query: Map<String, String>
    ): Response<List<SalesTargetAnalysisResponseModel>> {
        return apiService.getSalesTargetsList(header, id, query)
    }

    override suspend fun getSalesBrandAnalysisInsights(
        header: String, id: String, query: Map<String, String>
    ): Response<SalesBrandAnalysisInsightsResponseModel> {
        return apiService.getSalesBrandAnalysisInsights(header, id, query)
    }

    override suspend fun getSalesSKUsList(
        header: String, id: String, query: Map<String, String>
    ): Response<SalesSKUsResponseModel> {
        return apiService.getSalesSKUsList(header, id, query)
    }

    override suspend fun getSalesBeatsList(
        header: String, userId: Int
    ): Response<SalesBeatResponseModel> {
        return apiService.getSalesBeatsList(header, userId)
    }

    override suspend fun getSalesCount(
        header: String, id: String, query: Map<String, String>
    ): Response<SalesBeatCountResponseModel> {
        return apiService.getSalesCount(header, id, query)
    }

    override suspend fun getSalesDailyAnalysis(
        header: String, id: String, query: Map<String, String>
    ): Response<SalesDailyAnalysisResponseModel> {
        return apiService.getSalesDailyAnalysis(header, id, query)
    }

    override suspend fun getDailyAnalysisInsights(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<DailyAnalysisCalenderResponseModel> {
        return apiService.getDailyAnalysisInsights(header, companyId, query)
    }

    override suspend fun getActiveSalesmanList(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<List<ActiveSalesmanResponseModel>> {
        return apiService.getActiveSalesmanList(header, companyId, query)
    }

    override suspend fun getSalesLocations(
        header: String, query: Map<String, String>
    ): Response<SalesLocationResponseModel> {
        return apiService.getSalesLocations(header, query)
    }

    override suspend fun getSalesTarget(
        header: String, query: Map<String, String>
    ): Response<TargetGraphResponse> {
        return apiService.getSalesTarget(header, query)
    }

    override suspend fun getSalesTargetUsersList(
        header: String, query: Map<String, String>
    ): Response<List<TargetUserData>> {
        return apiService.getSalesTargetUsersList(header, query)
    }

    override suspend fun getSalesNetworkList(
        header: String, query: Map<String, String>
    ): Response<List<SalesNetworkResponseModel>> {
        return apiService.getSalesNetworkList(header, query)
    }

    override suspend fun getLeaveCount(
        header: String, month: String, year: String
    ): Response<GetLeaveCountData> {
        return apiService.getLeaveCount(header, month, year)
    }

    override suspend fun getLeaveList(
        header: String, request: HashMap<String, String>
    ): Response<GetHRResponseList> {
        return apiService.getLeaveList(header, request)
    }

    override suspend fun getSnapShotData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomePageSnapShotResponseModel> {
        return apiService.getSnapShotData(header, companyId, request)
    }

    override suspend fun getHomeFiltersData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomePageFiltersResponseModel> {
        return apiService.getHomeFiltersData(header, companyId, request)
    }

    override suspend fun getHomeSalesCountData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomePageSalesResponseModel> {
        return apiService.getHomeSalesCountData(header, companyId, request)
    }

    override suspend fun getHomeSalesPersonsListData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<List<HomePageSalesPersonListResponsModel>> {
        return apiService.getHomeSalesPersonsListData(header, companyId, request)
    }

    override suspend fun getHomeInsightsOrderData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeInsightsOrderResponseModel> {
        return apiService.getHomeInsightsOrderData(header, companyId, request)
    }

    override suspend fun getHomeReachAnalysisData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeReachAnalysisResponseModel> {
        return apiService.getHomeReachAnalysisData(header, companyId, request)
    }

    override suspend fun getHomeRetailersData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeInsightRetailersResponseModel> {
        return apiService.getHomeRetailersData(header, companyId, request)
    }

    override suspend fun getHomeOrderSummaryData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeOrderSummaryResponseModel> {
        return apiService.getHomeOrderSummaryData(header, companyId, request)
    }

    override suspend fun getHomeOrderSummaryData1(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeOrderSummaryOrdersResponseModel> {
        return apiService.getHomeOrderSummaryData1(header, companyId, request)
    }

    override suspend fun getHomeSKUsData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeSKUsResponseModel> {
        return apiService.getHomeSKUsData(header, companyId, request)
    }

    override suspend fun getHomeSKUsItemDetails(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<List<HomeSKUsItemDetailsResponseModel>> {
        return apiService.getHomeSKUsItemDetails(header, companyId, request)
    }

    override suspend fun getHomeLeaderBoardSalesPersons(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<List<HomeLeaderBoardSPResponseModel>> {
        return apiService.getHomeLeaderBoardSalesPersons(header, companyId, request)
    }

    override suspend fun getHomeInsightsLastDays(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<List<HomeInsightsLastDaysResponseModel>> {
        return apiService.getHomeInsightsLastDays(header, companyId, request)
    }

    override suspend fun getHomeOrdersPlacedData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeInsightsOrderPlacedResponseModel> {
        return apiService.getHomeOrdersPlacedData(header, companyId, request)
    }

    override suspend fun getHomeSubCatData(
        header: String, companyId: Int, request: HashMap<String, String>
    ): Response<HomeInsightsSubCatResponseModel> {
        return apiService.getHomeSubCatData(header, companyId, request)
    }

    override suspend fun getSalesPerformanceList(
        header: String, id: String, data: HashMap<String, String>
    ): Response<List<SalesPerformanceResponseModel>> {
        return apiService.getSalesPerformanceList(header, id, data)
    }

    override suspend fun getCPLeaderBoardList(
        header: String, id: String, data: HashMap<String, String>
    ): Response<List<CPLeaderBoardResponseModel>> {
        return apiService.getCPLeaderBoardList(header, id, data)
    }

    override suspend fun getLeaderBoardReport(header: String): Response<GenerateReportResponseModel> {
        return apiService.getLeaderBoardReport(header)
    }

    override suspend fun getReportingManagerList(
        header: String, id: String
    ): Response<List<ReportingManagerResponseModel>> {
        return apiService.getReportingManagerList(header, id)
    }

    override suspend fun getSalesDesignationsList(
        header: String
    ): Response<List<DesignationSalesFilterResponseModel>> {
        return apiService.getSalesDesignationsList(header)
    }
}