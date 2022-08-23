package co.kr.kwon.courierservice.di

import android.app.Activity
import co.kr.kwon.courierservice.data.api.SweetTrackerApi
import co.kr.kwon.courierservice.data.api.Url.SWEET_TRACKER_API_URL
import co.kr.kwon.courierservice.data.db.AppDatabase
import co.kr.kwon.courierservice.data.entity.TrackingInformation
import co.kr.kwon.courierservice.data.entity.TrackingItem
import co.kr.kwon.courierservice.data.preference.PreferenceManager
import co.kr.kwon.courierservice.data.preference.SharedPreferenceManager
import co.kr.kwon.courierservice.data.repository.*
import co.kr.kwon.courierservice.presentation.addtrackingitem.AddTrackingItemContract
import co.kr.kwon.courierservice.presentation.addtrackingitem.AddTrackingItemFragment
import co.kr.kwon.courierservice.presentation.addtrackingitem.AddTrackingItemPresenter
import co.kr.kwon.courierservice.presentation.trackinghistory.TrackingHistoryContract
import co.kr.kwon.courierservice.presentation.trackinghistory.TrackingHistoryFragment
import co.kr.kwon.courierservice.presentation.trackinghistory.TrackingHistoryPresenter
import co.kr.kwon.courierservice.presentation.trackingitems.TrackingItemsContract
import co.kr.kwon.courierservice.presentation.trackingitems.TrackingItemsFragment
import co.kr.kwon.courierservice.presentation.trackingitems.TrackingItemsPresenter
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    //Database
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    //Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }


    //Repository
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    //single<TrackingItemRepository> { TrackingItemRepositoryStub() }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }

    //Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource(), get()) }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemContract.Presenter> {
            AddTrackingItemPresenter(getSource(), get(), get())
        }
    }
    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (trackingItem: TrackingItem, trackingInformation: TrackingInformation) ->
            TrackingHistoryPresenter(getSource(), get(), trackingItem, trackingInformation)
        }
    }
}