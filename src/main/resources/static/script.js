// ==========================================
// 1. CONFIGURACIÓN Y SEGURIDAD
// ==========================================
const CREDENCIALES = { user: "admin", pass: "123" };

function iniciarSesion() {
    const user = document.getElementById('userInput').value;
    const pass = document.getElementById('passInput').value;

    if(user === CREDENCIALES.user && pass === CREDENCIALES.pass) {
        document.getElementById('pantallaLogin').style.display = 'none';
        document.getElementById('interfazSistema').style.display = 'block';
        document.getElementById('labelUsuario').innerText = user.toUpperCase();
        Swal.fire({ icon: 'success', title: 'Acceso Autorizado', timer: 1500, showConfirmButton: false });
    } else {
        Swal.fire('Error', 'Usuario o Contraseña incorrectos', 'error');
    }
}

// ==========================================
// 2. NAVEGACIÓN
// ==========================================
function verSeccion(id, elemento) {
    document.querySelectorAll('.seccion').forEach(s => s.style.display = 'none');
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    document.getElementById(id).style.display = 'block';
    if(elemento) elemento.classList.add('active');
    if(id === 'secInventario') cargarDatosInventario();
}

// ==========================================
// 3. GESTIÓN DE PRODUCTOS (CRUD)
// ==========================================
async function guardarProducto() {
    const id = document.getElementById('prodId').value;
    const data = {
        sku: document.getElementById('sku').value,
        nombre: document.getElementById('nombre').value,
        categoria: document.getElementById('categoria').value,
        precio: parseFloat(document.getElementById('precio').value) || 0,
        ubicacion: document.getElementById('ubicacion').value,
        cantidad: parseInt(document.getElementById('stockInicial').value) || 0
    };

    if(!data.sku || !data.nombre) return Swal.fire('Aviso', 'Complete SKU y Nombre', 'warning');

    const metodo = id ? 'PUT' : 'POST';
    const url = id ? `/api/productos/${id}` : '/api/productos';

    try {
        const resp = await fetch(url, {
            method: metodo,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        if(resp.ok) {
            Swal.fire('Zarpronix', id ? 'Producto Actualizado' : 'Producto Creado', 'success');
            limpiarCatalogo();
            cargarDatosInventario();
        }
    } catch (e) {
        Swal.fire('Error', 'No hay conexión con el servidor', 'error');
    }
}

async function cargarDatosInventario() {
    const tbody = document.getElementById('tablaInventario');
    try {
        const resp = await fetch('/api/productos');
        const productos = await resp.json();
        tbody.innerHTML = '';

        for (let [index, p] of productos.entries()) {
            const respStock = await fetch(`/api/movimientos/stock/${p.id}`);
            const stockActual = await respStock.text();
            
            const precioValido = (p.precio != null && !isNaN(p.precio)) ? parseFloat(p.precio).toFixed(2) : "0.00";

            tbody.innerHTML += `
                <tr>
                    <td><span class="badge bg-secondary">#${index + 1}</span></td> 
                    <td><small class="fw-bold">${p.sku || 'N/A'}</small></td>
                    <td>${p.nombre}</td>
                    <td><span class="badge bg-light text-dark border">${p.categoria || 'Sin categoría'}</span></td>
                    <td class="fw-bold text-primary">$${precioValido}</td>
                    <td class="fw-bold ${stockActual < 5 ? 'text-danger' : 'text-success'}">${stockActual}</td>
                    <td class="text-center">
                        <button class="btn btn-sm btn-outline-warning me-1" onclick='prepararEdicion(${JSON.stringify(p)}, ${stockActual})'>
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="eliminarProducto(${p.id})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>`;
        }
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Error al conectar con el servidor</td></tr>';
    }
}

function prepararEdicion(p, stockActual) {
    verSeccion('secCatalogo', document.getElementById('linkCat'));
    document.getElementById('prodId').value = p.id;
    document.getElementById('sku').value = p.sku;
    document.getElementById('nombre').value = p.nombre;
    document.getElementById('categoria').value = p.categoria;
    document.getElementById('precio').value = p.precio;
    document.getElementById('ubicacion').value = p.ubicacion;
    
    const campoStock = document.getElementById('stockInicial');
    campoStock.value = stockActual;
    campoStock.readOnly = true; 
    campoStock.style.backgroundColor = "#e9ecef";

    document.getElementById('btnGuardar').innerText = "Actualizar Producto";
    document.getElementById('btnGuardar').className = "btn btn-warning px-5 fw-bold";
    document.getElementById('btnCancelar').style.display = "inline-block";
}

function limpiarCatalogo() {
    document.getElementById('prodId').value = "";
    document.querySelectorAll('#secCatalogo input').forEach(i => i.value = "");
    const campoStock = document.getElementById('stockInicial');
    campoStock.readOnly = false;
    campoStock.style.backgroundColor = "white";
    document.getElementById('btnGuardar').innerText = "Guardar Cambios";
    document.getElementById('btnGuardar').className = "btn btn-primary px-5 fw-bold";
    document.getElementById('btnCancelar').style.display = "none";
}

// ======================================================
// FUNCIÓN ELIMINAR ACTUALIZADA (BORRADO EN CASCADA)
// ======================================================
async function eliminarProducto(id) {
    const res = await Swal.fire({
        title: '¿Eliminar de Zarpronix?',
        text: "Se borrará el producto y todo su historial de movimientos.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar todo',
        cancelButtonText: 'Cancelar'
    });

    if(res.isConfirmed) {
        try {
            const resp = await fetch(`/api/productos/${id}`, { method: 'DELETE' });
            if(resp.ok) {
                Swal.fire('Eliminado', 'Registro y movimientos borrados con éxito', 'success');
                cargarDatosInventario();
            } else {
                Swal.fire('Error', 'El servidor no permitió la eliminación. Reinicia las tablas si es necesario.', 'error');
            }
        } catch (error) {
            Swal.fire('Error', 'Servidor no disponible', 'error');
        }
    }
}

// ==========================================
// 4. KARDEX Y FACTURA
// ==========================================
async function procesarKardex() {
    const id = document.getElementById('movProdId').value;
    const tipo = document.getElementById('movTipo').value;
    const cant = document.getElementById('movCant').value;

    if(!id || !cant) return Swal.fire('Error', 'Faltan datos', 'error');

    try {
        const resp = await fetch(`/api/movimientos/${id}/${tipo}/${cant}`, { method: 'POST' });
        if(resp.ok) {
            const resF = await fetch(`/api/movimientos/factura/${id}/${cant}`);
            const total = await resF.json();
            
            const resP = await fetch(`/api/productos`);
            const productos = await resP.json();
            const p = productos.find(x => x.id == id);

            generarFacturaHTML(p, tipo, cant, total);
            verSeccion('secFactura', document.getElementById('linkFac'));
        } else {
            Swal.fire('Error', 'ID de producto no existe o datos inválidos', 'error');
        }
    } catch (e) {
        Swal.fire('Error', 'Error de comunicación', 'error');
    }
}

function generarFacturaHTML(p, tipo, cant, total) {
    const usuario = document.getElementById('labelUsuario').innerText;
    const precioUnitario = (p.precio != null) ? parseFloat(p.precio).toFixed(2) : "0.00";
    
    document.getElementById('areaFactura').innerHTML = `
        <div class="text-center mb-4">
            <h1 class="fw-bold text-primary">ZARPRONIX S.A.</h1>
            <p class="text-muted">Tulcán, Carchi - Control de Bodega</p>
        </div>
        <hr>
        <div class="row mb-4">
            <div class="col-6"><strong>Operador:</strong> ${usuario}<br><strong>Fecha:</strong> ${new Date().toLocaleString()}</div>
            <div class="col-6 text-end"><strong>Estado:</strong> <span class="badge ${tipo==='ENTRADA'?'bg-success':'bg-danger'}">${tipo}</span></div>
        </div>
        <table class="table table-bordered">
            <thead class="table-dark"><tr><th>Producto / SKU</th><th class="text-center">Cant.</th><th class="text-end">V. Unitario</th><th class="text-end">Total</th></tr></thead>
            <tbody>
                <tr>
                    <td>${p.nombre}<br><small>SKU: ${p.sku}</small></td>
                    <td class="text-center">${cant}</td>
                    <td class="text-end">$${precioUnitario}</td>
                    <td class="text-end fw-bold">$${total.toFixed(2)}</td>
                </tr>
            </tbody>
        </table>
        <h2 class="text-end text-success mt-4">Monto Final: $${total.toFixed(2)}</h2>
    `;
}