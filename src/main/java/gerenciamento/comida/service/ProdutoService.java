package gerenciamento.comida.service;

import gerenciamento.comida.dto.ProdutoDTO;
import gerenciamento.comida.model.Produto;
import gerenciamento.comida.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        return toDTO(produto);
    }

    public ProdutoDTO salvar(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDescricao(dto.getDescricao());

        Produto salvo = produtoRepository.save(produto);
        return toDTO(salvo);
    }

    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDescricao(dto.getDescricao());

        return toDTO(produtoRepository.save(produto));
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    private ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getPreco(), produto.getDescricao());
    }
}