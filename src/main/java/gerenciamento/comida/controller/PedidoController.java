package gerenciamento.comida.controller;

import gerenciamento.comida.dto.PedidoRequestDTO;
import gerenciamento.comida.dto.PedidoResponseDTO;
import gerenciamento.comida.model.StatusPedido;
import gerenciamento.comida.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos(
            @RequestParam(required = false) StatusPedido status) {
        if (status != null) {
            return ResponseEntity.ok(pedidoService.listarPorStatus(status));
        }
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}