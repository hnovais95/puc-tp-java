package br.puc.tp_final.purchase
import org.springframework.web.bind.annotation.*
import java.util.*
import br.puc.tp_final.purchase.model.Product;
import br.puc.tp_final.purchase.model.Response;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlinx.coroutines.*

@RestController
@RequestMapping("/purchase-ms/rest/purchase")
class PurchaseController(
    val purchaseService: PurchaseService
) {

    val log: Logger = LoggerFactory.getLogger(PurchaseController::class.java)

    @PostMapping("buy")
    fun buy(@RequestBody shoppingCart: List<Product>): Response {
        try {
            runBlocking { 
                log.info("Vai criar transação")
                val uuid = purchaseService.buy(shoppingCart)
                log.info("Transação efetuada com sucesso! Id: ${uuid}")
                return Response(
                    StatusCode.Success.value(), 
                    "Compra realizada com sucesso."
                    uuid
                )
            }
        } catch (e: PurchaseException) {
            return Response(StatusCode.ClientError.value(), e.message)
        } catch (e: Exception) {
            return Response(StatusCode.ServerError.value(), "Erro inesperado")
        }
    }
}