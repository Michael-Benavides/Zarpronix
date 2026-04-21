package Zarpronix.infrastructure.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "*")
public class ChatController {

    private final String MISTRAL_API_KEY = "8lOii9pSOWu4vSUeFHCHHrp1IEVOcNYX";
    private final String MISTRAL_URL = "https://api.mistral.ai/v1/chat/completions";

    @PostMapping("/preguntar")
    public ResponseEntity<Map<String, String>> consultarMistral(@RequestBody Map<String, Object> payload) {
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(MISTRAL_API_KEY);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "mistral-tiny");
            body.put("messages", payload.get("messages"));
            body.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // Se define Map<String, Object> para evitar el aviso de "Raw Type"
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(MISTRAL_URL, entity, (Class<Map<String, Object>>) (Class<?>) Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("choices")) {
                List<?> choices = (List<?>) responseBody.get("choices");
                
                if (choices != null && !choices.isEmpty()) {
                    Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
                    
                    if (message != null && message.containsKey("content")) {
                        String content = (String) message.get("content");
                        return ResponseEntity.ok(Map.of("respuesta", content));
                    }
                }
            }
            
            return ResponseEntity.status(500).body(Map.of("respuesta", "Mistral no devolvió contenido, chele."));

        } catch (Exception e) {
            // Imprime el error para que Michael pueda verlo en la consola de Tulcán
            System.err.println("Error en la petición: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("respuesta", "¡Chuta! Falló la conexión con el cerebro de la IA."));
        }
    }
}