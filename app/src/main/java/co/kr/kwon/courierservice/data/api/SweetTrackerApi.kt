package co.kr.kwon.courierservice.data.api

import co.kr.kwon.courierservice.BuildConfig
import co.kr.kwon.courierservice.BuildConfig.SWEET_TRACKER_API_KEY
import co.kr.kwon.courierservice.data.entity.ShippingCompanies
import co.kr.kwon.courierservice.data.entity.TrackingInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SweetTrackerApi {

    @GET("api/v1/trackingInfo?t_key=${SWEET_TRACKER_API_KEY}")
    suspend fun getTrackingInformation(
        @Query("t_code") companyCode: String,
        @Query("t_invoice") invoice: String
    ): Response<TrackingInformation>

    @GET("api/v1/companylist?t_key=$SWEET_TRACKER_API_KEY")
    suspend fun getShippingCompanies() : Response<ShippingCompanies>

    suspend fun getRecommendShippingCompanies(
        @Query("t_invoice") invoice: String
    ): Response<ShippingCompanies>

}
