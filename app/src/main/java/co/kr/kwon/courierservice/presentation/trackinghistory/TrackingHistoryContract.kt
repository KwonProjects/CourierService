package co.kr.kwon.courierservice.presentation.trackinghistory

import co.kr.kwon.courierservice.data.entity.TrackingInformation
import co.kr.kwon.courierservice.data.entity.TrackingItem
import co.kr.kwon.courierservice.presentation.BasePresenter
import co.kr.kwon.courierservice.presentation.BaseView


class TrackingHistoryContract {

    interface View : BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()
    }

    interface Presenter : BasePresenter {

        fun refresh()

        fun deleteTrackingItem()
    }
}
