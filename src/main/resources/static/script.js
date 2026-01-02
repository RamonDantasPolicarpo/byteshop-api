let API_URL = "http://127.0.0.1:8080";
let EP_USERS = "/api/cliente";
let EP_PRODUCTS = "/api/produto";
let EP_ORDERS = "/api/pedido";
let USE_MOCK = false;

let productPage = 0;
let productSize = 20;
let usersCache = [],
    productsCache = [],
    currentCart = [];
let currentViewingOrderId = null;

(function initSystem() {
    const sUrl = localStorage.getItem('api_url');
    if (!sUrl) {
        localStorage.setItem('api_url', API_URL);
        localStorage.setItem('ep_users', EP_USERS);
        localStorage.setItem('ep_products', EP_PRODUCTS);
        localStorage.setItem('ep_orders', EP_ORDERS);
        localStorage.setItem('use_mock', String(USE_MOCK));
    } else {
        API_URL = sUrl;
        EP_USERS = localStorage.getItem('ep_users') || EP_USERS;
        EP_PRODUCTS = localStorage.getItem('ep_products') || EP_PRODUCTS;
        EP_ORDERS = localStorage.getItem('ep_orders') || EP_ORDERS;
        USE_MOCK = localStorage.getItem('use_mock') === 'true';
    }
})();

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('config-api-url').value = API_URL;
    document.getElementById('config-ep-users').value = EP_USERS;
    document.getElementById('config-ep-products').value = EP_PRODUCTS;
    document.getElementById('config-ep-orders').value = EP_ORDERS;
    document.getElementById('config-mock-mode').checked = USE_MOCK;
    updateStatusUI();
    router('dashboard');
});

function router(view) {
    document.querySelectorAll('.main-content').forEach(el => el.classList.remove('active'));
    document.getElementById(`view-${view}`).classList.add('active');
    if (view === 'dashboard') loadDashboard();
    if (view === 'users') loadUsers();
    if (view === 'products') {
        loadProducts();
    }
    if (view === 'orders') loadOrders();
}

async function apiCall(endpoint, method = 'GET', body = null) {
    if (USE_MOCK) return mockCall(endpoint, method, body);
    showLoading(true);
    document.getElementById('connection-alert').style.display = 'none';
    try {
        const opts = {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            }
        };
        if (body) opts.body = JSON.stringify(body);
        const fullUrl = `${API_URL}${endpoint}`.replace(/([^:]\/)\/+/g, "$1");
        const res = await fetch(fullUrl, opts);
        if (!res.ok) {
            const txt = await res.text();
            try {
                const jsonErr = JSON.parse(txt);
                throw new Error(jsonErr.message || JSON.stringify(jsonErr));
            } catch (e) {
                throw new Error(`Status ${res.status}: ${txt.substring(0, 100)}...`);
            }
        }
        const text = await res.text();
        updateStatusUI('online');
        return text ? JSON.parse(text) : {};
    } catch (err) {
        console.error(err);
        handleError(err, endpoint);
        updateStatusUI('offline');
        return null;
    } finally {
        showLoading(false);
    }
}

function handleError(err, endpoint) {
    const msg = `Erro em ${endpoint}: ${err.message}`;
    Foton.showToast(msg);
    const alertBox = document.getElementById('connection-alert');
    if (alertBox) {
        alertBox.style.display = 'flex';
        document.getElementById('connection-msg').innerText = msg;
    }
}

async function loadDashboard() {
    const [p, u, o] = await Promise.all([
        apiCall(`${EP_PRODUCTS}?size=2000`),
        apiCall(EP_USERS),
        apiCall(EP_ORDERS)
    ]);
    const getCount = (d) => {
        if (!d) return 0;
        if (d.totalElements !== undefined) return d.totalElements;
        if (Array.isArray(d)) return d.length;
        return 0;
    };
    const ordersList = Array.isArray(o) ? o : (o?.content || []);
    if (p) document.getElementById('dash-total-products').innerText = getCount(p);
    if (u) document.getElementById('dash-total-users').innerText = getCount(u);
    if (o) document.getElementById('dash-total-orders').innerText = getCount(o);
    ordersList.sort((a, b) => new Date(b.dataPedido) - new Date(a.dataPedido));
    const recents = ordersList.slice(0, 5);
    const tbody = document.querySelector('#dash-recent-orders tbody');
    tbody.innerHTML = '';
    recents.forEach(r => {
        const id = r.idPedido || r.id;
        const cl = r.id_cliente?.nomeCliente || 'N/A';
        const st = r.statusPedido?.descricao || 'Pendente';
        let badgeClass = 'ft-badge-neutral';
        let statusLower = st.toLowerCase();
        if (statusLower.includes('entregue')) badgeClass = 'ft-badge-success';
        else if (statusLower.includes('enviado')) badgeClass = 'ft-badge-warning';
        else if (statusLower.includes('aprovado') || statusLower.includes('pago')) badgeClass = 'ft-badge-info';
        else if (statusLower.includes('cancelado')) badgeClass = 'ft-badge-danger';
        tbody.innerHTML += `
                <tr>
                    <td>#${id}</td>
                    <td>${cl}</td>
                    <td><span class="ft-badge ft-badge-pill ${badgeClass}">${st}</span></td>
                </tr>`;
    });
    let revenue = 0;
    ordersList.forEach(order => {
        if (order.itens && Array.isArray(order.itens)) {
            order.itens.forEach(i => revenue += (i.quantidade * i.valorUnitario));
        }
    });
    document.getElementById('dash-total-revenue').innerText = `R$ ${fmtMoney(revenue)}`;
    initChart(ordersList);
}

let myChart = null;

function initChart(orders) {
    const ctx = document.getElementById("myAreaChart");
    const salesByMonth = {};
    if (orders) {
        orders.forEach(order => {
            const date = new Date(order.dataPedido);
            const key = date.toLocaleString('default', {
                month: 'short'
            });
            let orderTotal = 0;
            if (order.itens) order.itens.forEach(i => orderTotal += i.quantidade * i.valorUnitario);
            if (!salesByMonth[key]) salesByMonth[key] = 0;
            salesByMonth[key] += orderTotal;
        });
    }
    const labels = Object.keys(salesByMonth);
    const data = Object.values(salesByMonth);
    if (myChart) myChart.destroy();
    myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels.length ? labels : ["Vazio"],
            datasets: [{
                label: "Vendas (R$)",
                data: data.length ? data : [0],
                backgroundColor: 'rgba(64, 170, 84, 0.2)',
                borderColor: '#40aa54',
                borderWidth: 3,
                tension: 0.4,
                pointRadius: 4,
                pointBackgroundColor: '#fff',
                pointBorderColor: '#40aa54'
            }],
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    labels: {
                        color: '#e0e0e0'
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#cccccc'
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    }
                },
                y: {
                    ticks: {
                        color: '#cccccc'
                    },
                    grid: {
                        color: 'rgba(255, 255, 255, 0.1)'
                    }
                }
            }
        }
    });
}

async function loadUsers(search = '') {
    let ep = EP_USERS;
    const raw = await apiCall(ep);
    let data = Array.isArray(raw) ? raw : (raw.content || []);
    if (search) {
        data = data.filter(u => u.nomeCliente.toLowerCase().includes(search.toLowerCase()) || u.cpf.includes(search));
    }
    const tbody = document.querySelector('#users-table tbody');
    tbody.innerHTML = '';
    data.forEach(u => {
        const uid = u.idCliente || u.id;
        tbody.innerHTML += `
                <tr>
                    <td>${uid}</td>
                    <td>${u.nomeCliente}</td>
                    <td>${u.email}</td>
                    <td>${u.cpf}</td>
                    <td>${u.cidade}/${u.estado}</td>
                    <td>
                        <button class="ft-btn ft-btn-sm ft-btn-info" onclick="editUser(${uid})"><i class="fa-solid fa-edit"></i></button>
                        <button class="ft-btn ft-btn-sm ft-btn-danger" onclick="deleteItem('${EP_USERS}', ${uid}, loadUsers)"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>`;
    });
    usersCache = data;
}

async function loadProducts(search = '') {
    let ep = `${EP_PRODUCTS}?page=${productPage}&size=${productSize}`;
    const catFilter = document.getElementById('filter-product-category').value;
    if (catFilter) ep = `${EP_PRODUCTS}/categoria/${catFilter}`;
    const raw = await apiCall(ep);
    let data = raw.content || raw || [];
    if (search) {
        data = data.filter(p => p.nomeProduto.toLowerCase().includes(search.toLowerCase()) || p.codigoProduto.includes(search));
    }
    const tbody = document.querySelector('#products-table tbody');
    tbody.innerHTML = '';
    data.forEach(p => {
        const pid = p.idProduto || p.id;
        tbody.innerHTML += `
                <tr>
                    <td>${pid}</td>
                    <td>${p.codigoProduto}</td>
                    <td>${p.nomeProduto}</td>
                    <td>${p.categoria}</td>
                    <td>${p.estoque}</td>
                    <td>R$ ${fmtMoney(p.preco)}</td>
                    <td>
                        <button class="ft-btn ft-btn-sm ft-btn-info" onclick="editProduct(${pid})"><i class="fa-solid fa-edit"></i></button>
                        <button class="ft-btn ft-btn-sm ft-btn-danger" onclick="deleteItem('${EP_PRODUCTS}', ${pid}, loadProducts)"><i class="fa-solid fa-trash"></i></button>
                    </td>
                </tr>`;
    });
    const isPaginated = !catFilter && !search;
    document.getElementById('btn-next-prod').disabled = !isPaginated || data.length < productSize;
    document.getElementById('btn-prev-prod').disabled = !isPaginated || productPage === 0;
    document.getElementById('product-page-info').innerText = isPaginated ? `Página ${productPage + 1}` : 'Filtro Ativo';
    productsCache = data;
}

async function loadOrders(search = '') {
    let ep = EP_ORDERS;
    const searchType = document.querySelector('input[name="searchType"]:checked').id;
    if (search && !isNaN(search)) {
        if (searchType === 'searchByOrder') {
            const order = await apiCall(`${EP_ORDERS}/${search}`);
            if (order) {
                renderOrdersList([order]);
                return;
            }
        } else {
            ep = `${EP_ORDERS}/cliente/${search}`;
        }
    }
    const raw = await apiCall(ep);
    let data = Array.isArray(raw) ? raw : (raw.content || []);
    renderOrdersList(data);
}

function renderOrdersList(data) {
    data.sort((a, b) => new Date(b.dataPedido) - new Date(a.dataPedido));
    const tbody = document.querySelector('#orders-table tbody');
    tbody.innerHTML = '';
    data.forEach(o => {
        const oid = o.idPedido || o.id;
        const client = o.id_cliente?.nomeCliente || 'N/A';
        const status = o.statusPedido?.descricao || 'Pendente';
        let badgeClass = 'ft-badge-neutral';
        let iconClass = 'fa-circle-question';
        const statusLower = status.toLowerCase();
        if (statusLower.includes('entregue')) {
            badgeClass = 'ft-badge-success';
            iconClass = 'fa-box';
        } else if (statusLower.includes('enviado')) {
            badgeClass = 'ft-badge-warning';
            iconClass = 'fa-truck-fast';
        } else if (statusLower.includes('processando') || statusLower.includes('pendente')) {
            badgeClass = 'ft-badge-neutral';
            iconClass = 'fa-arrows-rotate';
        } else if (statusLower.includes('aprovado') || statusLower.includes('pago')) {
            badgeClass = 'ft-badge-info';
            iconClass = 'fa-credit-card';
        } else if (statusLower.includes('cancelado')) {
            badgeClass = 'ft-badge-danger';
            iconClass = 'fa-xmark';
        }
        let total = 0;
        if (o.itens && Array.isArray(o.itens)) total = o.itens.reduce((acc, i) => acc + (i.quantidade * i.valorUnitario), 0);
        tbody.innerHTML += `
                <tr>
                    <td>${oid}</td>
                    <td>${o.numeroPedido}</td>
                    <td style="text-align: center;">${o.dataPedido}</td>
                    <td style="text-align: center;">${client}</td>
                    <td style="text-align: center;">
                        <span class="ft-badge ft-badge-pill ${badgeClass}">
                            <i class="fa-solid ${iconClass}" style="margin-right: 0.5em;"></i>${status}
                        </span>
                    </td>
                    <td>R$ ${fmtMoney(total)}</td>
                    <td style="text-align: center;"><button class="ft-btn ft-btn-sm ft-btn-white" onclick="viewOrder(${oid})">
                        <i class="fa-solid fa-eye"></i>
                        </button></td>
                </tr>`;
    });
}

async function submitUser() {
    const id = document.getElementById('user-id').value;
    const u = {
        nomeCliente: val('user-name'),
        cpf: val('user-cpf'),
        email: val('user-email'),
        dataNascimento: val('user-birth'),
        cep: val('user-cep'),
        endereco: val('user-address'),
        cidade: val('user-city'),
        estado: val('user-state')
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${EP_USERS}/${id}` : EP_USERS;
    if (await apiCall(url, method, u)) {
        closeModal('userModal');
        loadUsers();
        Foton.showToast("Cliente guardado!");
    }
}

async function submitProduct() {
    const id = document.getElementById('prod-id').value;
    const p = {
        nomeProduto: val('prod-name'),
        codigoProduto: val('prod-code'),
        categoria: val('prod-category'),
        preco: parseFloat(val('prod-price')),
        estoque: parseInt(val('prod-stock'))
    };
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${EP_PRODUCTS}/${id}` : EP_PRODUCTS;
    if (await apiCall(url, method, p)) {
        closeModal('productModal');
        loadProducts();
        Foton.showToast("Produto guardado!");
    }
}

async function submitOrder() {
    const uid = document.getElementById('order-user-select').value;
    if (!uid) return alert("Selecione um cliente.");
    if (currentCart.length === 0) return alert("Adicione itens ao carrinho.");
    const payload = {
        clienteid: parseInt(uid),
        itens: currentCart.map(i => {
            const prodId = parseInt(i.idProduto);
            return {
                produtoid: prodId,
                quantidade: parseInt(i.quantidade)
            };
        })
    };
    if (payload.itens.some(item => isNaN(item.produtoid) || !item.produtoid)) {
        Foton.showToast("Erro: Dados de produto inválidos.");
        return;
    }
    const res = await apiCall(EP_ORDERS, 'POST', payload);
    if (res) {
        closeModal('orderModal');
        loadOrders();
        loadDashboard();
        Foton.showToast("Pedido Finalizado!");
    }
}

async function updateOrderStatus() {
    if (!currentViewingOrderId) return;
    const sel = document.getElementById('order-status-select-update');
    const st = sel.value;
    const res = await apiCall(`${EP_ORDERS}/${currentViewingOrderId}/status/${st}`, 'PUT');
    if (res) {
        Foton.showToast("Status atualizado!");
        closeModal('viewOrderModal');
        loadOrders();
        loadDashboard();
    }
}

function changeProductPage(d) {
    productPage += d;
    if (productPage < 0) productPage = 0;
    loadProducts();
}

function openModal(id) {
    Foton.toggleModal(id, true);
}

function closeModal(id) {
    Foton.toggleModal(id, false);
}

async function editUser(id) {
    let u = await apiCall(`${EP_USERS}/${id}`);
    if (u) {
        document.getElementById('user-id').value = u.idCliente || u.id;
        document.getElementById('user-name').value = u.nomeCliente;
        document.getElementById('user-cpf').value = u.cpf;
        document.getElementById('user-email').value = u.email;
        document.getElementById('user-birth').value = u.dataNascimento;
        document.getElementById('user-cep').value = u.cep;
        document.getElementById('user-address').value = u.endereco;
        document.getElementById('user-city').value = u.cidade;
        document.getElementById('user-state').value = u.estado;
        openModal('userModal');
    }
}

async function editProduct(id) {
    let p = await apiCall(`${EP_PRODUCTS}/${id}`);
    if (p) {
        document.getElementById('prod-id').value = p.idProduto || p.id;
        document.getElementById('prod-name').value = p.nomeProduto;
        document.getElementById('prod-code').value = p.codigoProduto;
        document.getElementById('prod-category').value = p.categoria;
        document.getElementById('prod-price').value = p.preco;
        document.getElementById('prod-stock').value = p.estoque;
        openModal('productModal');
    }
}

async function viewOrder(id) {
    let o = await apiCall(`${EP_ORDERS}/${id}`);
    if (!o) return;
    currentViewingOrderId = o.idPedido || o.id;
    document.getElementById('view-order-id').innerText = `#${currentViewingOrderId}`;
    document.getElementById('view-order-number-txt').innerText = o.numeroPedido || 'N/A';
    document.getElementById('view-order-date-txt').innerText = o.dataPedido || 'N/A';
    document.getElementById('view-order-client').innerText = o.id_cliente?.nomeCliente || '-';
    document.getElementById('view-order-email').innerText = o.id_cliente?.email || '-';
    document.getElementById('view-order-cpf').innerText = o.id_cliente?.cpf || '-';
    if (o.statusPedido?.id) {
        document.getElementById('order-status-select-update').value = o.statusPedido.id;
    }
    const container = document.getElementById('view-order-items-list');
    container.innerHTML = '';
    let total = 0;
    (o.itens || []).forEach(i => {
        const sub = i.quantidade * i.valorUnitario;
        total += sub;
        container.innerHTML += `
                <div class="checkout-item">
                    <div>
                        <strong>${i.idProduto?.nomeProduto || 'Produto'}</strong><br>
                        <small class="ft-text-muted">${i.quantidade}x R$ ${fmtMoney(i.valorUnitario)}</small>
                    </div>
                    <div style="text-align: right;">
                        <strong>R$ ${fmtMoney(sub)}</strong>
                    </div>
                </div>`;
    });
    document.getElementById('view-order-total').innerText = `R$ ${fmtMoney(total)}`;
    openModal('viewOrderModal');
}

function openUserModal() {
    document.getElementById('userForm').reset();
    document.getElementById('user-id').value = '';
    openModal('userModal');
}

function openProductModal() {
    document.getElementById('productForm').reset();
    document.getElementById('prod-id').value = '';
    openModal('productModal');
}

async function openOrderModal() {
    currentCart = [];
    renderCart();
    const uSel = document.getElementById('order-user-select');
    uSel.innerHTML = '<option>Carregando...</option>';
    const pSel = document.getElementById('order-product-select');
    pSel.innerHTML = '<option>Carregando...</option>';
    const users = await apiCall(EP_USERS);
    uSel.innerHTML = '<option value="">Selecione um Cliente</option>';
    (users || []).forEach(u => uSel.innerHTML += `<option value="${u.idCliente || u.id}">${u.nomeCliente} (${u.cpf})</option>`);
    const products = await apiCall(`${EP_PRODUCTS}?size=1000`);
    pSel.innerHTML = '<option value="">Selecione um Produto</option>';
    (products.content || products || []).forEach(p => {
        const pid = p.idProduto || p.id;
        pSel.innerHTML += `<option value="${pid}" data-price="${p.preco}" data-name="${p.nomeProduto}">${p.nomeProduto} - R$ ${fmtMoney(p.preco)}</option>`;
    });
    openModal('orderModal');
}

function addItemToOrder() {
    const sel = document.getElementById('order-product-select');
    const id = sel.value;
    const qtd = parseInt(val('order-qty'));
    if (!id || id === "" || qtd < 1) {
        Foton.showToast("Dados inválidos.");
        return;
    }
    const opt = sel.options[sel.selectedIndex];
    currentCart.push({
        idProduto: parseInt(id),
        nomeProduto: opt.getAttribute('data-name'),
        valorUnitario: parseFloat(opt.getAttribute('data-price')),
        quantidade: qtd
    });
    renderCart();
}

function renderCart() {
    const container = document.getElementById('order-cart-table-body');
    container.innerHTML = '';
    let total = 0;
    currentCart.forEach((i, idx) => {
        const sub = i.quantidade * i.valorUnitario;
        total += sub;
        container.innerHTML += `
                <div class="checkout-item">
                    <div>
                        <strong>${i.nomeProduto}</strong><br>
                        <small class="ft-text-muted">${i.quantidade}x R$ ${fmtMoney(i.valorUnitario)}</small>
                    </div>
                    <div style="text-align: right;">
                        <strong>R$ ${fmtMoney(sub)}</strong><br>
                        <a href="javascript:void(0)" onclick="currentCart.splice(${idx},1);renderCart()" style="color: var(--danger); font-size: 0.8rem;">Remover</a>
                    </div>
                </div>`;
    });
    document.getElementById('order-total-display').innerText = `R$ ${fmtMoney(total)}`;
}

function searchUser() {
    loadUsers(val('search-user-input'));
}

function resetUserSearch() {
    document.getElementById('search-user-input').value = '';
    loadUsers();
}

function searchProduct() {
    productPage = 0;
    loadProducts(val('search-product-input'));
}

function resetProductSearch() {
    document.getElementById('search-product-input').value = '';
    document.getElementById('filter-product-category').value = '';
    productPage = 0;
    loadProducts();
}

function searchOrder() {
    loadOrders(val('search-order-input'));
}

function resetOrderSearch() {
    document.getElementById('search-order-input').value = '';
    loadOrders();
}

function saveSettings() {
    API_URL = val('config-api-url').replace(/\/$/, "");
    EP_USERS = val('config-ep-users');
    EP_PRODUCTS = val('config-ep-products');
    EP_ORDERS = val('config-ep-orders');
    USE_MOCK = document.getElementById('config-mock-mode').checked;
    localStorage.setItem('api_url', API_URL);
    localStorage.setItem('use_mock', String(USE_MOCK));
    closeModal('settingsModal');
    loadDashboard();
    updateStatusUI();
}

function val(id) {
    return document.getElementById(id).value;
}

function showLoading(show) {
    document.getElementById('loading-overlay').style.display = show ? 'flex' : 'none';
}

function fmtMoney(v) {
    return (v || 0).toFixed(2);
}

function deleteItem(ep, id, cb) {
    if (confirm('Confirma exclusão?')) {
        apiCall(`${ep}/${id}`, 'DELETE').then(() => {
            cb();
            Foton.showToast("Excluído!");
        });
    }
}

async function updateStatusUI(forcedStatus = null) {
    const b = document.getElementById('api-status');
    if (USE_MOCK) {
        b.innerText = "Modo Mock";
        b.className = "ft-badge ft-badge-pill ft-badge-warning";
        return;
    }

    if (forcedStatus === 'offline') {
        b.innerText = "API Offline";
        b.className = "ft-badge ft-badge-pill ft-badge-danger";
        return;
    }

    if (forcedStatus === 'online') {
        b.innerText = "API Conectada";
        b.className = "ft-badge ft-badge-pill ft-badge-success";
        return;
    }

    b.innerText = "A testar...";
    b.className = "ft-badge ft-badge-pill ft-badge-neutral";

    try {
        const fullUrl = `${API_URL}${EP_USERS}`.replace(/([^:]\/)\/+/g, "$1");
        const res = await fetch(fullUrl, { method: 'GET' });
        if (res.ok) {
            b.innerText = "API Conectada";
            b.className = "ft-badge ft-badge-pill ft-badge-success";
        } else {
            b.innerText = "Erro API (" + res.status + ")";
            b.className = "ft-badge ft-badge-pill ft-badge-danger";
        }
    } catch (e) {
        b.innerText = "API Offline";
        b.className = "ft-badge ft-badge-pill ft-badge-danger";
    }
}

function mockCall(ep) {
    return new Promise(r => setTimeout(() => {
        if (ep.includes('pedido')) r([]);
        else if (ep.includes('cliente')) r([]);
        else if (ep.includes('produto')) r({
            content: []
        });
        else r({});
    }, 300));
}