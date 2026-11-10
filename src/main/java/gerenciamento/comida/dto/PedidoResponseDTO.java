package gerenciamento.comida.dto;

import gerenciamento.comida.model.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private String nomeCliente;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private List<ProdutoDTO> produtos;
}