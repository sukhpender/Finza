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
import com.riggle.plug.data.model.FinzaProfileModel
import com.riggle.plug.data.model.FinzaProfileResponseModel
import com.riggle.plug.data.model.FinzaUpdateProfileResponseModel
import com.riggle.plug.data.model.GenerateReportResponseModel
import com.riggle.plug.data.model.GetHRResponseList
import com.riggle.plug.data.model.GetLeaveCountData
import com.riggle.plug.data.model.GetLeaveData
import com.riggle.plug.data.model.HomeInsightRetailersResponseModel
import com.riggle.plug.data.model.HomeInsightsLastDaysResponseModel
import com.riggle.plug.data.model.HomeInsightsOrderPlacedResponseModel
import com.riggle.plug.data.model.HomeInsightsOrderResponseModel
import com.riggle.plug.data.model.HomeInsightsSubCatResponseModel
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
import com.riggle.plug.data.model.TargetGraphResponse
import com.riggle.plug.data.model.TargetUserData
import com.riggle.plug.data.model.SalesUserListResponseModel
import com.riggle.plug.data.model.SalesmanListingResponseModel
import com.riggle.plug.data.model.SendOtpIssueTagResponseModel
import com.riggle.plug.data.model.SendOtpRequest
import com.riggle.plug.data.model.SendOtpResponseModel
import com.riggle.plug.data.model.StorePaymentResponseModel
import com.riggle.plug.data.model.UnAssignedBeatResponseModel
import com.riggle.plug.data.model.UnAssignedCountResponseModel
import com.riggle.plug.data.model.UserProfileResponseModel
import com.riggle.plug.data.model.UserWalletResponseModel
import com.riggle.plug.data.model.UsersListResponseModel
import com.riggle.plug.data.model.VerifyOtpResponseModel
import com.riggle.plug.data.model.WalletCreateCustomerResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface BaseRepo {

    suspend fun finzaLogin(
        email:String,
        password: String
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
        header: String,
        reqBody: SendOtpRequest
    ): Response<SendOtpIssueTagResponseModel>

    suspend fun createWalletCustomer(
       header: String,
        name: String,
        email: String,
        phone: String
    ): Response<WalletCreateCustomerResponseModel>

    suspend fun updateUserProfile(
        header: String,
        body: RequestBody,
        body2: RequestBody,
        profile_image: MultipartBody.Part
    ): Response<FinzaProfileResponseModel>

    suspend fun finzaUserWallet(
        header: String,
    ): Response<UserWalletResponseModel>

    suspend fun getInventoriesList(
        header: String,
        status: String
    ): Response<InventryResponseModel>

    suspend fun finzaUsersList(
        header: String,
    ): Response<UsersListResponseModel>

    suspend fun assignInventory(
        header: String,
        inventory_id: String,
        assigned_to: String
    ): Response<AssignInventoryResponseModel>

    suspend fun resetPasssword(
        user_id: String,
        new_password: String,
        confirm_new_password: String
    ): Response<ResetPasswordResponseModel>

    suspend fun getProjectList(
       header: String,
    ): Response<ProjectListResponseModel>

    suspend fun acceptRejectInventory(
        header: String,
        inventory_id: String,
        status: String
    ): Response<AssignInventoryResponseModel>

    suspend fun storePayment(
       header: String, reqBody: PaymentStoreRequest
    ): Response<StorePaymentResponseModel>








    suspend fun sendOtp(
        mobile: String
    ): Response<SendOtpResponseModel>

    suspend fun verifyOtp(
        mobile: String, otp: String
    ): Response<VerifyOtpResponseModel>


    suspend fun getBrandList(
        header: String, search: String, page_size: String, page: Int, expand: String, fields: String
    ): Response<BrandListModel>

    suspend fun getBrandProductList(
        header: String, query: Map<String, String>
    ): Response<BrandProductResponseModel>

    suspend fun getBrandOffersList(
        header: String, query: Map<String, String>
    ): Response<BrandOfferResponseModel>

    suspend fun getBrandRateList(
        header: String, query: Map<String, String>
    ): Response<BrandRateResponseModel>

    suspend fun getBrandNetworkUserCPCount(
        header: String, brand: Int, id: Int
    ): Response<BrandNetworkCPCount>

    suspend fun getBrandNetworkUserCPCount1(
        header: String, id: Int, brand: Int, company: Int
    ): Response<List<NetworkCPCount1Item>>

    suspend fun getNetworkPendingOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkPendingOrdersResponseModel>>

    suspend fun getNetworkOutstandingOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkOutstandingOrdersResponseModel>>

    suspend fun getNetworkConfirmedOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkConfirmedResponseModel>>

    suspend fun getNetworkCompletedOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkCompletedOrdersResponseModel>>

    suspend fun getNetworkCancelledOrdersList(
        header: String, query: Map<String, String>
    ): Response<List<NetworkCancelledOrdersResponseModel>>

    suspend fun getNetworkOrderDetails(
        header: String, query: Map<String, String>, id: Int
    ): Response<NetworkOrderDetailsResponseModel>

    suspend fun getOwnOrderDetails(
        header: String, query: Map<String, String>, id: Int
    ): Response<OwnOrderDetailsResponseModel>

    suspend fun getBeatCities(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<List<BeatCityResponseModel>>

    suspend fun getUnAssignedCount(
        header: String, companyId: Int, city: String
    ): Response<UnAssignedCountResponseModel>

    suspend fun getCityBeatsList(
        header: String, query: Map<String, String>
    ): Response<CityBeatsResponseModel>

    suspend fun getUnAssignedBeatsList(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<UnAssignedBeatResponseModel>

    suspend fun getAssignedBeatDetails(
        header: String, beatId: Int, query: Map<String, String>
    ): Response<AssignedBeatDetailsResponseModel>

    suspend fun getBeatInsights(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<BeatInsightsResponseModel>

    suspend fun getActiveCPList(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<ActiveCPResponseModel>

    suspend fun getLeadCPList(
        header: String, query: Map<String, String>
    ): Response<LeadCPResponseModel>

    suspend fun getUserProfile(
        header: String, companyId: Int, query: Map<String, String>
    ): Response<UserProfileResponseModel>

    suspend fun getUserProfileCoOwner(
        header: String, query: Map<String, String>
    ): Response<List<CoOwnersListResponseModel>>


    suspend fun deleteCoOwner(
        header: String, id: Int
    ): Response<DeleteCoOwnerResponse>

    suspend fun addCoOwner(
        header: String, query: Map<String, String>
    ): Response<DeleteCoOwnerResponse>

    suspend fun updateUser(
        header: String,
        id: String,
        f_name: RequestBody,
        l_name: RequestBody,
        email: RequestBody,
        query: MultipartBody.Part
    ): Response<UserProfileResponseModel>

    suspend fun updateUserWithOutImage(
        header: String, id: String, f_name: RequestBody, l_name: RequestBody, email: RequestBody
    ): Response<UserProfileResponseModel>

    suspend fun updatePanCard(
        authkey: String, id: String, image: MultipartBody.Part?
    ): Response<UserProfileResponseModel>

    suspend fun updatePanAadhaarCard(
        authkey: String, id: String, image: MultipartBody.Part?, imageAadhaar: MultipartBody.Part?
    ): Response<UserProfileResponseModel>

    suspend fun getCpRemarks(
        header: String,
        companyId: String,
    ): Response<CpRemarksResponseModel>

    suspend fun getLowStock(
        header: String, brand: String, company: String
    ): Response<LowStockDataResponse>

    suspend fun getCpStocks(
        header: String, query: HashMap<String, String>
    ): Response<List<CpStockResponseItem>>

    suspend fun getBrandList1(
        header: String, query: Map<String, String>
    ): Response<BrandResponse>

    suspend fun getCpInsights(
        header: String, id: String, query: Map<String, String>
    ): Response<CpInsightsResponseModel>

    suspend fun getNetworkOrderCount(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<NetworkOrdersCountResponseModel>

    suspend fun getSalesManList(
        header: String,
        companyId: String,
        query: Map<String, String>
    ): Response<SalesmanListingResponseModel>

    suspend fun getSalesUserList(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<List<SalesUserListResponseModel>>

    suspend fun getSalesTargetsList(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<List<SalesTargetAnalysisResponseModel>>

    suspend fun getSalesBrandAnalysisInsights(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<SalesBrandAnalysisInsightsResponseModel>

    suspend fun getSalesSKUsList(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<SalesSKUsResponseModel>

    suspend fun getSalesBeatsList(
        header: String,
        userId: Int
    ): Response<SalesBeatResponseModel>

    suspend fun getSalesCount(
        header: String,
       id: String,
        query: Map<String, String>
    ): Response<SalesBeatCountResponseModel>

    suspend fun getSalesDailyAnalysis(
        header: String,
        id: String,
        query: Map<String, String>
    ): Response<SalesDailyAnalysisResponseModel>

    suspend fun getDailyAnalysisInsights(
       header: String,
        companyId: Int,
        query: Map<String, String>
    ): Response<DailyAnalysisCalenderResponseModel>

    suspend fun getActiveSalesmanList(
        header: String,
        companyId: Int,
        query: Map<String, String>
    ): Response<List<ActiveSalesmanResponseModel>>

    suspend fun getSalesLocations(
        header: String,
        query: Map<String, String>
    ): Response<SalesLocationResponseModel>

    suspend fun getSalesTarget(
       header: String,
       query: Map<String, String>
    ): Response<TargetGraphResponse>

    suspend fun getSalesTargetUsersList(
        header: String,
        query: Map<String, String>
    ): Response<List<TargetUserData>>

    suspend fun getSalesNetworkList(
        header: String,
        query: Map<String, String>
    ): Response<List<SalesNetworkResponseModel>>

    suspend fun getLeaveCount(
        header: String,
        month: String,
        year: String
    ): Response<GetLeaveCountData>

    suspend fun getLeaveList(
        header: String,
        request: HashMap<String, String>
    ): Response<GetHRResponseList>

    suspend fun getSnapShotData(
       header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomePageSnapShotResponseModel>

    suspend fun getHomeFiltersData(
       header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomePageFiltersResponseModel>

    suspend fun getHomeSalesCountData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomePageSalesResponseModel>

    suspend fun getHomeSalesPersonsListData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<List<HomePageSalesPersonListResponsModel>>

    suspend fun getHomeInsightsOrderData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeInsightsOrderResponseModel>

    suspend fun getHomeReachAnalysisData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeReachAnalysisResponseModel>

    suspend fun getHomeRetailersData(
       header: String,
        companyId: Int,
       request: HashMap<String, String>
    ): Response<HomeInsightRetailersResponseModel>

    suspend fun getHomeOrderSummaryData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeOrderSummaryResponseModel>

    suspend fun getHomeOrderSummaryData1(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeOrderSummaryOrdersResponseModel>

    suspend fun getHomeSKUsData(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeSKUsResponseModel>

    suspend fun getHomeSKUsItemDetails(
         header: String,
    companyId: Int,
        request: HashMap<String, String>
    ): Response<List<HomeSKUsItemDetailsResponseModel>>

    suspend fun getHomeLeaderBoardSalesPersons(
        header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<List<HomeLeaderBoardSPResponseModel>>

    suspend fun getHomeInsightsLastDays(
        header: String,
       companyId: Int,
        request: HashMap<String, String>
    ): Response<List<HomeInsightsLastDaysResponseModel>>

    suspend fun getHomeOrdersPlacedData(
        header: String,
       companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeInsightsOrderPlacedResponseModel>

    suspend fun getHomeSubCatData(
       header: String,
        companyId: Int,
        request: HashMap<String, String>
    ): Response<HomeInsightsSubCatResponseModel>

    suspend fun getSalesPerformanceList(
        header: String,
        id: String,
        data: HashMap<String,String>
    ): Response<List<SalesPerformanceResponseModel>>

    suspend fun getCPLeaderBoardList(
        header: String,
        id: String,
        data: HashMap<String,String>
    ): Response<List<CPLeaderBoardResponseModel>>

    suspend fun getLeaderBoardReport(
        header: String
    ): Response<GenerateReportResponseModel>

    suspend fun getReportingManagerList(
        header: String,
        id: String,
    ): Response<List<ReportingManagerResponseModel>>

    suspend fun getSalesDesignationsList(
        header: String
    ): Response<List<DesignationSalesFilterResponseModel>>
}