// Usar URL relativa permite funcionar no localhost ou dentro do container Docker sem alterar porta
const API_URL = '/api';

document.addEventListener('DOMContentLoaded', () => {
    carregarProdutos();
    carregarPedidos();
    configurarFormularios();
});

function configurarFormularios() {
    document.getElementById('form-produto').addEventListener('submit', salvarProduto);
    document.getElementById('form-pedido').addEventListener('submit', criarPedido);
}

// --- PRODUTOS ---

async function carregarProdutos() {
    try {
        const res = await fetch(`${API_URL}/produtos`);
        if (!res.ok) throw new Error('Falha ao carregar produtos');
        const produtos = await res.json();

        renderizarTabelaProdutos(produtos);
        renderizarCheckboxesProdutos(produtos);
    } catch (err) {
        console.error("Erro ao carregar produtos:", err);
    }
}

function renderizarTabelaProdutos(produtos) {
    const tbody = document.getElementById('tabela-produtos');
    tbody.innerHTML = '';

    produtos.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td>${p.nome}</td>
            <td>R$ ${Number(p.preco).toFixed(2)}</td>
            <td>${p.descricao || '-'}</td>
            <td><button class="btn-danger" onclick="deletarProduto(${p.id})">Excluir</button></td>
        `;
        tbody.appendChild(tr);
    });
}

function renderizarCheckboxesProdutos(produtos) {
    const container = document.getElementById('lista-checkbox-produtos');
    container.innerHTML = '';

    produtos.forEach(p => {
        const label = document.createElement('label');
        label.innerHTML = `
            <input type="checkbox" value="${p.id}" class="prod-checkbox"> 
            ${p.nome} - R$ ${Number(p.preco).toFixed(2)}
        `;
        container.appendChild(label);
    });
}

async function salvarProduto(e) {
    e.preventDefault();
    const produto = {
        nome: document.getElementById('prod-nome').value,
        preco: parseFloat(document.getElementById('prod-preco').value),
        descricao: document.getElementById('prod-descricao').value
    };

    try {
        const res = await fetch(`${API_URL}/produtos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto)
        });

        if (res.ok) {
            document.getElementById('form-produto').reset();
            carregarProdutos();
        }
    } catch (err) {
        console.error("Erro ao salvar produto:", err);
    }
}

async function deletarProduto(id) {
    if (!confirm("Deseja remover este produto?")) return;

    try {
        const res = await fetch(`${API_URL}/produtos/${id}`, { method: 'DELETE' });
        if (res.ok) {
            carregarProdutos();
        }
    } catch (err) {
        console.error("Erro ao deletar produto:", err);
    }
}

// --- PEDIDOS ---

async function carregarPedidos() {
    try {
        const res = await fetch(`${API_URL}/pedidos`);
        if (!res.ok) throw new Error('Falha ao carregar pedidos');
        const pedidos = await res.json();

        const colRecebido = document.getElementById('coluna-recebido');
        const colPreparando = document.getElementById('coluna-preparando');
        const colPronto = document.getElementById('coluna-pronto');

        colRecebido.innerHTML = '';
        colPreparando.innerHTML = '';
        colPronto.innerHTML = '';

        pedidos.forEach(p => {
            const card = criarCardPedido(p);
            if (p.status === 'RECEBIDO') colRecebido.appendChild(card);
            else if (p.status === 'PREPARANDO') colPreparando.appendChild(card);
            else if (p.status === 'PRONTO') colPronto.appendChild(card);
        });
    } catch (err) {
        console.error("Erro ao carregar pedidos:", err);
    }
}

function criarCardPedido(pedido) {
    const div = document.createElement('div');
    div.className = 'card-pedido';

    const produtosHTML = pedido.produtos.map(item => `<li>${item.nome}</li>`).join('');

    let botoesAcao = '';
    if (pedido.status === 'RECEBIDO') {
        botoesAcao = `<button onclick="atualizarStatus(${pedido.id}, 'PREPARANDO')">Iniciar Preparo</button>`;
    } else if (pedido.status === 'PREPARANDO') {
        botoesAcao = `<button class="btn-success" onclick="atualizarStatus(${pedido.id}, 'PRONTO')">Marcar Pronto</button>`;
    }

    div.innerHTML = `
        <strong>Pedido #${pedido.id} - ${pedido.nomeCliente || 'Cliente N/A'}</strong>
        <ul>${produtosHTML}</ul>
        <div class="actions">
            ${botoesAcao}
            <button class="btn-danger" onclick="deletarPedido(${pedido.id})">Cancelar</button>
        </div>
    `;

    return div;
}

async function criarPedido(e) {
    e.preventDefault();
    const cliente = document.getElementById('ped-cliente').value;

    const checkboxes = document.querySelectorAll('.prod-checkbox:checked');
    const produtoIds = Array.from(checkboxes).map(cb => parseInt(cb.value));

    if (produtoIds.length === 0) {
        alert("Selecione pelo menos um produto!");
        return;
    }

    const pedido = { nomeCliente: cliente, produtoIds: produtoIds };

    try {
        const res = await fetch(`${API_URL}/pedidos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pedido)
        });

        if (res.ok) {
            document.getElementById('form-pedido').reset();
            carregarPedidos();
        }
    } catch (err) {
        console.error("Erro ao criar pedido:", err);
    }
}

async function atualizarStatus(id, status) {
    try {
        const res = await fetch(`${API_URL}/pedidos/${id}/status?status=${status}`, { method: 'PATCH' });
        if (res.ok) {
            carregarPedidos();
        }
    } catch (err) {
        console.error("Erro ao atualizar status:", err);
    }
}

async function deletarPedido(id) {
    if (!confirm("Deseja cancelar este pedido?")) return;

    try {
        const res = await fetch(`${API_URL}/pedidos/${id}`, { method: 'DELETE' });
        if (res.ok) {
            carregarPedidos();
        }
    } catch (err) {
        console.error("Erro ao deletar pedido:", err);
    }
}