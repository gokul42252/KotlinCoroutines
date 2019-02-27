package com.ct.kotlincoroutines.network

import com.ct.kotlincoroutines.AppConstants


object ApiFactory{

    val placeholderApi : PlaceholderApi = RetrofitFactory.retrofit(AppConstants.JP_BASE_URL)
                                                .create(PlaceholderApi::class.java)
}