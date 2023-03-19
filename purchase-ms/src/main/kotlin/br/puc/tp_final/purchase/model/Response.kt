package br.puc.tp_final.purchase.model
import java.util.*

data class Response (
    val statusCode: Int,
    val friendlyMessage: String,
    val uuid: UUID? = null
)