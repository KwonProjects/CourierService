package co.kr.kwon.courierservice.presentation

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT
}