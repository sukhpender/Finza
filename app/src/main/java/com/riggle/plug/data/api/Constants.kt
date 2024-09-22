package com.riggle.plug.data.api

object Constants {

    // Finza
    const val IMAGE_BASE_URL= "https://finzaa.s3.amazonaws.com/"
    const val X_APP_ACCEPT= "Accept:application/json"
    const val X_APP_NAME= "Accept:application/json"
    const val FINZA_LOGIN = "api/user-login"
    const val FINZA_LOGOUT = "api/user-logout"
    const val FINZA_PROFILE = "api/profile"
    const val FORGOT_PASSWORD = "api/forgotPassword"
    const val SEND_OTP_TAG_ISSUE = "api/send-otp"
    const val WALLET_CREATE_CUSTOMER = "api/customer-create"
    const val UPDATE_PROFILE = "api/updateProfile"
    const val USER_WALLET = "api/user-wallet"
    const val HOLD_AMOUNT = "api/hold_amount"
    const val INVENTORY_LIST = "api/assigned-inventory"
    const val USERS_LIST = "api/user-list"
    const val ASSIGN_INVENTORY = "api/inventory-assigned"
    const val RESET_PASSWORD = "api/resetPassword"
    const val PROJECT_LISTING = "api/project-list"
    const val CHANGE_INVENTORY_STATUS = "api/change-inventory-status"
    const val PAYMENT_STORE = "api/payment-store"
    const val WALLET_TRANSACTIONS = "api/transaction-list"
    const val HOME_INVENTORY = "api/inventory_count"
    const val ISSUE_TAG_CHECK_WALLET = "api/check-wallet"
    const val CHECK_TAG_AVAILABILITY = "api/scan-tag"
    const val UPDATE_PROJECT = "api/update-user-project"

















    /* <<---  Brand Module Constants  --->> **/
    const val BRAND = "brand"
    const val FIELDS = "id,brand.id,brand.name,brand.image"
    const val BRAND_RATE_FIELDS = "id,update_url,brand.id,cp_count,is_ss,is_distributor,is_wholesaler,product.id,product.name,banner_image.image,product.base_quantity,product.base_unit,product.mrp,ss_rate,ss_moq,ss_margin,distributor_rate,distributor_moq,distributor_margin,wholesaler_rate,wholesaler_moq,wholesaler_margin,retailer_rate,retailer_moq,retailer_margin,ss_name,distributor_name,wholesaler_name"
    const val BRAND_RATE_EXPAND = "product,brand.company,banner_image"
    const val PAGE_SIZE_BRAND = "30"

    /* <<---  Auth Module Api  --->> **/
    const val SEND_OTP = "user/auth/send_otp/"
    const val VERIFY_OTP = "user/auth/verify_otp/"

    /* <<---  Brand Module Api  --->> **/
    const val BRAND_LIST= "core/mapped_brands/"
    const val BRAND_PRODUCTS = "core/stocks/"
    const val BRAND_OFFERS = "core/offers/"
    const val BRAND_PRODUCT_RATE = "core/stocks/"
    const val BRAND_PRODUCT_NETWORK_USER_CP_COUNTS = "core/companies/6575/plug_channel_counts/"
    const val BRAND_PRODUCT_NETWORK_USER_CP_COUNT1 = "core/companies/6575/company_buyer_listing/"



    /* <<---  Network Module Constants  --->> **/
    const val NETWORK_ORDER_DETAILS_EXPAND = "products.free_product,products.product.banner_image,buyer.admin,assigned_runner,statuses,cart_user,brand,payments"
    const val NETWORK_ORDER_DETAILS_EXPAND1 = "products.free_product,products.product.banner_image,buyer.admin,seller.admin,assigned_runner,statuses,cart_user,brand,payments"
    const val NETWORK_PENDING_ORDERS_EXPAND = "buyer,assigned_runner,statuses,cart_user,brand,payments,seller"
    const val NETWORK_PENDING_ORDERS_FIELDS = "id,code,buyer.name,buyer.full_address,seller.name,created_at,delivery_date,pending_amount,cart_user.first_name,final_amount,assigned_runner.first_name,updated_at,statuses"

    /* <<---  Network Orders & Orders Module Api  --->> **/
    const val NETWORK_ORDERS_LIST= "core/orders/"


    /* <<---  Beat Module Api  --->> **/
    const val BEAT_CITIES= "core/companies/6575/beat_cities"
    const val BEAT_LEAD_EXPAND = "admin,extra,admin.created_by"
    const val BEAT_LEAD_FIELDS = "extra.is_sampling_done,channel_id,name,logo,full_address,admin.full_name,admin.mobile,admin.created_by.full_name,admin.created_by.mobile,admin.created_by.image,admin.created_at,id,extra.working_type,extra.monthly_potential,extra.fmcg_experience,extra.products_category,extra.tempo_quantity,extra.tempo_type,extra.wharehouse_space,extra.delivery_staff,extra.current_brands,extra.sampling_required,extra.sampling_quantity,extra.minimum_investment_plan,extra.expected_closure_date,extra.cp_interested,extra.sample_image,extra.is_sampling_done,extra.dist_visiting_card,update_url"

    var selectedTab = "1"
    var selectedMonth = ""
    var selectedYear = ""
    var SALES_DESIGNATION = 0
    var SALES_DESIGNATION_Name = ""
    var SALES_MANAGER = 0
    var SALES_MANAGER_NAME = ""
    var SALES_VAN_USER = false


}