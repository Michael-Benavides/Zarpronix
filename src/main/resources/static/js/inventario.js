// ==========================================
// PRODUCTOS, CLIENTES, PROVEEDORES Y KARDEX
// ==========================================

// --- PRODUCTOS (Sincronizado con ProductoController: /api/productos) ---
async function cargarDatosInventario() {
    const contenedor = document.getElementById('tablaInventario');
    if(!contenedor) return;
    try {
        const resp = await fetch(`${CONFIG.API_BASE_URL}/api/productos`);
        const lista = await resp.json();
        contenedor.innerHTML = lista.map(p => `
            <tr>
                <td>${p.id}</td>
                <td>${p.sku}</td>
                <td>${p.nombre}</td>
                <td><span class="badge bg-light text-dark border">${p.categoria}</span></td>
                <td>$${p.precio.toFixed(2)}</td>
                <td><span class="badge ${p.cantidad < 5 ? 'bg-danger' : 'bg-success'}">${p.cantidad}</span></td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick='prepararEdicionProducto(${JSON.stringify(p)})'>
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarDato('api/productos', ${p.id})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>`).join('');
    } catch (e) { console.error("Error en Inventario:", e); }
}

async function guardarProducto() {
    const id = document.getElementById('prodId').value;
    const data = {
        sku: document.getElementById('sku').value,
        nombre: document.getElementById('nombre').value,
        categoria: document.getElementById('categoria').value,
        precio: parseFloat(document.getElementById('precio').value) || 0,
        cantidad: parseInt(document.getElementById('cantidad').value) || 0
    };
    const url = id ? `${CONFIG.API_BASE_URL}/api/productos/${id}` : `${CONFIG.API_BASE_URL}/api/productos`;
    const metodo = id ? 'PUT' : 'POST';
    const resp = await fetch(url, {
        method: metodo,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    if(resp.ok) {
        Swal.fire('Éxito', id ? 'Producto actualizado' : 'Producto guardado en Zarpronix', 'success');
        limpiarFormularios();
        verSeccion('secDatabase');
        cargarDatosInventario();
    }
}

function prepararEdicionProducto(p) {
    verSeccion('secCatalogo');
    document.getElementById('prodId').value = p.id;
    document.getElementById('sku').value = p.sku;
    document.getElementById('nombre').value = p.nombre;
    document.getElementById('categoria').value = p.categoria;
    document.getElementById('precio').value = p.precio;
    document.getElementById('cantidad').value = p.cantidad;
    const btn = document.querySelector('#secCatalogo button');
    if(btn) {
        btn.innerText = "ACTUALIZAR PRODUCTO";
        btn.classList.replace('btn-primary', 'btn-warning');
    }
}

// --- KARDEX ---
async function procesarKardex() {
    const id = document.getElementById('movProdId').value;
    const tipo = document.getElementById('movTipo').value;
    const cant = parseInt(document.getElementById('movCant').value);
    if(!id || !cant) return Swal.fire('Error', 'Complete todos los campos', 'error');
    
    const respGet = await fetch(`${CONFIG.API_BASE_URL}/api/productos/${id}`);
    if(!respGet.ok) return Swal.fire('Error', 'Producto no encontrado', 'error');
    const producto = await respGet.json();
    
    let nuevaCant = tipo === 'ENTRADA' ? producto.cantidad + cant : producto.cantidad - cant;
    if(nuevaCant < 0) return Swal.fire('Error', 'Stock insuficiente', 'warning');
    
    producto.cantidad = nuevaCant;
    const respPut = await fetch(`${CONFIG.API_BASE_URL}/api/productos/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(producto)
    });
    if(respPut.ok) {
        Swal.fire('Éxito', `Operación: ${tipo} realizada`, 'success');
        if(typeof generarFactura === 'function') generarFactura(producto, cant, tipo);
        document.getElementById('movProdId').value = "";
        document.getElementById('movCant').value = "";
        cargarDatosInventario();
    }
}

// --- CLIENTES (Sincronizado con ClienteController: /clientes) ---
async function cargarClientes() {
    const contenedor = document.getElementById('tablaClientes');
    if(!contenedor) return;
    try {
        // Corregido: Tus Clientes en Java NO usan /api
        const resp = await fetch(`${CONFIG.API_BASE_URL}/clientes/listar`);
        const lista = await resp.json();
        contenedor.innerHTML = lista.map(c => `
            <tr>
                <td>${c.nombre || '---'}</td>
                <td>${c.cedula || '---'}</td>
                <td>${c.telefono || '---'}</td>
                <td>${c.email || '---'}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick='prepararEdicionCliente(${JSON.stringify(c)})'>
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarDato('clientes', ${c.id})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>`).join('');
    } catch (e) { console.warn("Error Clientes:", e); }
}

async function guardarCliente() {
    const id = document.getElementById('cliId').value;
    const data = {
        nombre: document.getElementById('cliNombre').value,
        cedula: document.getElementById('cliCedula').value, 
        telefono: document.getElementById('cliTelefono').value,
        email: document.getElementById('cliEmail').value
    };
    // Corregido: Sincronizado con @RequestMapping("/clientes")
    const url = id ? `${CONFIG.API_BASE_URL}/clientes/actualizar/${id}` : `${CONFIG.API_BASE_URL}/clientes/guardar`;
    const metodo = id ? 'PUT' : 'POST';
    const resp = await fetch(url, {
        method: metodo,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    if(resp.ok) {
        Swal.fire('Éxito', 'Cliente procesado', 'success');
        limpiarFormularios();
        verSeccion('secDatabase');
        cargarClientes();
    }
}

function prepararEdicionCliente(c) {
    verSeccion('secClientes');
    document.getElementById('cliId').value = c.id;
    document.getElementById('cliNombre').value = c.nombre;
    document.getElementById('cliCedula').value = c.cedula;
    document.getElementById('cliTelefono').value = c.telefono;
    document.getElementById('cliEmail').value = c.email;
}

// --- PROVEEDORES (Sincronizado con ProveedorController: /proveedores) ---
async function cargarProveedores() {
    const contenedor = document.getElementById('tablaProveedores');
    if(!contenedor) return;
    try {
        // Corregido: Tus Proveedores en Java NO usan /api
        const resp = await fetch(`${CONFIG.API_BASE_URL}/proveedores/listar`);
        const lista = await resp.json();
        contenedor.innerHTML = lista.map(p => `
            <tr>
                <td>${p.nombreEmpresa || '---'}</td>
                <td>${p.ruc || '---'}</td>
                <td>${p.contacto || '---'}</td>
                <td>${p.direccion || '---'}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick='prepararEdicionProveedor(${JSON.stringify(p)})'>
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="eliminarDato('proveedores', ${p.id})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>`).join('');
    } catch (e) { console.warn("Error Proveedores:", e); }
}

async function guardarProveedor() {
    const id = document.getElementById('provId').value;
    const data = {
        nombreEmpresa: document.getElementById('provEmpresa').value,
        ruc: document.getElementById('provRuc').value,
        contacto: document.getElementById('provContacto').value,
        direccion: document.getElementById('provDireccion').value
    };
    // Corregido: Sincronizado con @RequestMapping("/proveedores")
    const url = id ? `${CONFIG.API_BASE_URL}/proveedores/actualizar/${id}` : `${CONFIG.API_BASE_URL}/proveedores/guardar`;
    const metodo = id ? 'PUT' : 'POST';
    const resp = await fetch(url, {
        method: metodo,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });
    if(resp.ok) {
        Swal.fire('Éxito', 'Proveedor procesado', 'success');
        limpiarFormularios();
        verSeccion('secDatabase');
        cargarProveedores();
    }
}

function prepararEdicionProveedor(p) {
    verSeccion('secProveedores');
    document.getElementById('provId').value = p.id;
    document.getElementById('provEmpresa').value = p.nombreEmpresa;
    document.getElementById('provRuc').value = p.ruc;
    document.getElementById('provContacto').value = p.contacto;
    document.getElementById('provDireccion').value = p.direccion;
}

// --- UTILIDADES (Eliminación Sincronizada) ---
async function eliminarDato(ruta, id) {
    const confirm = await Swal.fire({
        title: '¿Eliminar registro?',
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar'
    });

    if(confirm.isConfirmed) {
        let url;
        // Lógica de rutas según tus controladores Java
        if(ruta === 'api/productos') {
            url = `${CONFIG.API_BASE_URL}/api/productos/${id}`;
        } else {
            // Clientes y proveedores usan: /clientes/eliminar/{id}
            url = `${CONFIG.API_BASE_URL}/${ruta}/eliminar/${id}`;
        }
        
        const resp = await fetch(url, { method: 'DELETE' });
        if(resp.ok) {
            Swal.fire('Eliminado', 'El registro ha sido borrado', 'success');
            cargarDatosInventario();
            cargarClientes();
            cargarProveedores();
        }
    }
}

function limpiarFormularios() {
    const ids = ['prodId', 'sku', 'nombre', 'precio', 'cantidad', 'cliId', 'cliNombre', 'cliCedula', 'cliTelefono', 'cliEmail', 'provId', 'provEmpresa', 'provRuc', 'provContacto', 'provDireccion'];
    ids.forEach(id => {
        const el = document.getElementById(id);
        if(el) el.value = "";
    });
    const btnProd = document.querySelector('#secCatalogo button');
    if(btnProd) {
        btnProd.innerText = "GUARDAR EN INVENTARIO";
        btnProd.classList.replace('btn-warning', 'btn-primary');
    }
}