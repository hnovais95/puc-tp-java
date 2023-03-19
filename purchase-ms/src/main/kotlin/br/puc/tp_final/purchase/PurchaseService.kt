package br.puc.tp_final.purchase
import org.springframework.stereotype.Service
import java.util.*
import br.puc.tp_final.purchase.model.Product;
import br.puc.tp_final.purchase.model.Response;
import br.puc.tp_final.purchase.model.StatusRetorno;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class PurchaseService {

    val log: Logger = LoggerFactory.getLogger(PurchaseService::class.java)

  	constructor() {}

    // Consulta microserviços de Estoque e Pagamento para confirmar transação
    @Throws(PurchaseException::class)
    suspend fun buy(shoppingCart: List<Product>): UUID {
        val uuid = UUID.randomUUID()
        try {
            validateStock(uuid, shoppingCart)
            validatePayment(uuid, shoppingCart)
            return uuid
        } catch (e: Exception) {
            throw e
        }
    }

    // Publica mensagem na fila do serviço de Estoque e aguarda resposta sincronamente
    private suspend fun validateStock(uuid: UUID, shoppingCart: List<Product>) {
        log.info("Vai verificar estoque")
        val message = MessageQueue.Message(uuid, shoppingCart)
        val result = MessageQueue.publishSync(MessageQueue.Queue.STOCK, message)
        if (!result) throw PurchaseException("Estoque insuficiente")
        log.info("Estoque disponível")
    }

    // Publica mensagem na fila do serviço de Pagamento e aguarda resposta sincronamente
    private suspend fun validatePayment(uuid: UUID, shoppingCart: List<Product>) {
        log.info("Vai confirmar pagamento")
        val message = MessageQueue.Message(uuid, shoppingCart)
        val result = MessageQueue.publishSync(MessageQueue.Queue.PAYMENT, message)
        if (!result) throw PurchaseException("Pagamento recusado")
        log.info("Pagamento autorizado")
    }
}