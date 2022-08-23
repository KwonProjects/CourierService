package co.kr.kwon.courierservice.presentation.addtrackingitem

import android.util.Log
import co.kr.kwon.courierservice.data.entity.ShippingCompany
import co.kr.kwon.courierservice.data.entity.TrackingItem
import co.kr.kwon.courierservice.data.repository.ShippingCompanyRepository
import co.kr.kwon.courierservice.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view: AddTrackingItemContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackerRepository: TrackingItemRepository
) : AddTrackingItemContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }
            Log.i("AddTrackingItemPresenter","shippingCompanies size : ${shippingCompanies!!.size}")
            shippingCompanies?.let { view.showCompanies(it) }
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendCompanyLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let { view.showRecommendCompany(it) }
            view.hideRecommendCompanyLoadingIndicator()
        }
    }


    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()
                trackerRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )
                view.finish()
            } catch (exception: Exception) {
                view.showErrorToast(exception.message ?: "서비스에 문제가 생겨서 운송장을 추가하지 못했어요 😢")
            } finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable() {
        if (!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }
}
