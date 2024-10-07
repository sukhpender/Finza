
package com.riggle.finza_finza.data.network
class NetworkError(val errorCode: Int, override val message: String?) : Throwable(message)
