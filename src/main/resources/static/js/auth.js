// ==========================================
// CONFIGURACIÓN Y SEGURIDAD
// ==========================================
const CREDENCIALES = { user: "admin", pass: "1234" };

function iniciarSesion() {
    const user = document.getElementById('userInput').value;
    const pass = document.getElementById('passInput').value;

    if(user === CREDENCIALES.user && pass === CREDENCIALES.pass) {
        document.getElementById('pantallaLogin').style.display = 'none';
        document.getElementById('interfazSistema').style.display = 'block';
        document.getElementById('labelUsuario').innerText = "MICHAEL BENAVIDES";
        Swal.fire({ 
            icon: 'success', 
            title: 'Acceso Autorizado', 
            text: 'Bienvenido al Sistema Zarpronix',
            timer: 1500, 
            showConfirmButton: false 
        });
        cargarDatosInventario();
    } else {
        Swal.fire('Error', 'Usuario o Contraseña incorrectos', 'error');
    }
}