package gerenciamento.comida.service;

import gerenciamento.comida.dto.PedidoRequestDTO;
import gerenciamento.comida.dto.PedidoResponseDTO;
import gerenciamento.comida.dto.ProdutoDTO;
import gerenciamento.comida.model.Pedido;
import gerenciamento.comida.model.Produto;
import gerenciamento.comida.model.StatusPedido;
import gerenciamento.comida.repository.PedidoRepository;
import gerenciamento.comida.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<PedidoResponseDTO> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        List<Produto> produtos = produtoRepository.findAllById(dto.getProdutoIds());

        if (produtos.isEmpty()) {
            throw new RuntimeException("Nenhum produto válido encontrado para os IDs informados.");
        }

        Pedido pedido = new Pedido();
        pedido.setNomeCliente(dto.getNomeCliente());
        pedido.setProdutos(produtos);

        Pedido salvo = pedidoRepository.save(pedido);
        return toResponseDTO(salvo);
    }

    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        pedido.setStatus(novoStatus);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<ProdutoDTO> produtoDTOs = pedido.getProdutos().stream()
                .map(p -> new ProdutoDTO(p.getId(), p.getNome(), p.getPreco(), p.getDescricao()))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                produtoDTOs
        );
    }
}