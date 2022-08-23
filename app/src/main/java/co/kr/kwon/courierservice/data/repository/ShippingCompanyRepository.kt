package co.kr.kwon.courierservice.data.repository

import co.kr.kwon.courierservice.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?

}