// ==========================================
// NAVEGACIÓN Y CONTROL DE VISTAS
// ==========================================
function verSeccion(id, elemento) {
    document.querySelectorAll('.seccion').forEach(s => s.style.display = 'none');
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    
    const seccionActiva = document.getElementById(id);
    if(seccionActiva) seccionActiva.style.display = 'block';
    
    if(elemento) {
        elemento.classList.add('active');
    } else {
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            if(link.getAttribute('onclick') && link.getAttribute('onclick').includes(id)) {
                link.classList.add('active');
            }
        });
    }
    
    if(id === 'secDatabase') {
        cargarDatosInventario();
        cargarClientes();
        cargarProveedores();
    }
}

function limpiarFormularios() {
    document.querySelectorAll('input').forEach(i => i.value = "");
    const btn = document.querySelector('#secCatalogo button');
    if(btn) {
        btn.innerText = "GUARDAR EN INVENTARIO";
        btn.classList.replace('btn-warning', 'btn-primary');
    }
}

function generarFactura(prod, cant, tipo) {
    const area = document.getElementById('areaFactura');
    area.innerHTML = `
        <div class="text-center">
            <h3>ZARPRONIX</h3>
            <p><strong>Comprobante de ${tipo}</strong></p>
            <hr>
            <p>Producto: ${prod.nombre}</p>
            <p>Cantidad: ${cant}</p>
            <p>Total: $${(prod.precio * cant).toFixed(2)}</p>
        </div>
    `;
    verSeccion('secFactura');
}