package gerenciamento.comida.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {

    private String nomeCliente;

    @NotEmpty(message = "O pedido deve conter pelo menos um ID de produto")
    private List<Long> produtoIds;
}