package an.imation.filmsapp.app

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NzI3M2U1ZWMzYjEzMjdkNmM5N2FhNjJjMDFjNWNkOSIsIm5iZiI6MTc0ODM2NTQ1NC4yNTEwMDAyLCJzdWIiOiI2ODM1ZjA4ZTkyNTNkNWFjZjA3MzA0ZjkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.P1VKgsnlVz1abm4FBto8fmTSePgFzITfRC-v7Qb108s")
            .build()
        return chain.proceed(request)
    }
}