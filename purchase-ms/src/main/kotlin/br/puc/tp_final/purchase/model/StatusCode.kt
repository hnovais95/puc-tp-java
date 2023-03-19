package br.puc.tp_final.purchase.model
import java.util.*

enum class StatusCode {
    Success {
        override fun value() = 200
    },
    ClientError {
        override fun value() = 200
    },
    ServerError {
        override fun value() = 200
    };

    abstract fun value(): Int
}
