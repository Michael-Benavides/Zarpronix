// ==========================================
// ZARPRO-BOT (IA) - Corregido
// ==========================================
let historialChat = []; 

function toggleChat() {
    const win = document.getElementById('chatbot-window');
    win.style.display = win.style.display === 'none' ? 'flex' : 'none';
}

function agregarMensaje(tipo, texto, id = null) {
    const box = document.getElementById('chatbot-messages');
    if(!box) return;
    const div = document.createElement('div');
    div.className = `msg ${tipo}-msg`;
    if(id) div.id = id;
    try {
        div.innerHTML = (tipo === 'bot' && typeof marked !== 'undefined') ? marked.parse(texto) : texto; 
    } catch (e) { div.innerText = texto; }
    box.appendChild(div);
    box.scrollTo({ top: box.scrollHeight, behavior: 'smooth' });
}

async function enviarMensajeBot() {
    const input = document.getElementById('botInput');
    const userText = input.value.trim();
    if (!userText) return;

    agregarMensaje('user', userText);
    input.value = "";
    const tempId = "bot-" + Date.now();
    agregarMensaje('bot', "...", tempId);

    try {
        // Uso de CONFIG.API_BASE_URL
        const base = CONFIG.API_BASE_URL;
        let ctx = { productos: [], clientes: [], proveedores: [] };
        
        try {
            const [rP, rC, rPr] = await Promise.all([
                fetch(`${base}/api/productos`).then(r => r.ok ? r.json() : []),
                fetch(`${base}/clientes/listar`).then(r => r.ok ? r.json() : []),
                fetch(`${base}/proveedores/listar`).then(r => r.ok ? r.json() : [])
            ]);
            ctx.productos = rP;
            ctx.clientes = rC;
            ctx.proveedores = rPr;
        } catch (err) { console.warn("Error cargando contexto IA"); }

        const promptSistema = { 
            role: "system", 
            content: `Eres Zarpro-Bot, asistente de Zarpronix (Carchi). Creado por Michael Benavides.
            REGLA: Usa tablas Markdown. Usa modismos como "chele" o "chuta".
            SISTEMA: ${JSON.stringify(ctx).substring(0, 2500)}` 
        };

        historialChat.push({ role: "user", content: userText });
        const mensajesParaEnviar = [promptSistema, ...historialChat.slice(-4)];

        const resp = await fetch(`${base}/api/bot/preguntar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ messages: mensajesParaEnviar })
        });

        const resData = await resp.json();
        const textoIA = resData.respuesta;
        
        historialChat.push({ role: "assistant", content: textoIA });
        const elementoBot = document.getElementById(tempId);
        if(elementoBot) elementoBot.innerHTML = marked.parse(textoIA);

    } catch (e) {
        const elementoBot = document.getElementById(tempId);
        if(elementoBot) elementoBot.innerText = "¡Chuta, chele! No hay conexión con el servidor.";
    }
}