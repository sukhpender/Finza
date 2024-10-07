
package com.riggle.finza_finza.data.network.intersepter
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(
    /*user: String, password: String*/
) : Interceptor {

   // private var credentials = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("x-app-name", "plug")

        return chain.proceed(builder.build())
    }
}