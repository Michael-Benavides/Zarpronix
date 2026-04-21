package Zarpronix.infrastructure.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Zarpronix.domain.model.Cliente;
import Zarpronix.infrastructure.persistence.ClienteRepository;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/listar")
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @PostMapping("/guardar")
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // NUEVO: Método para eliminar cliente
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }

    // NUEVO: Método para actualizar cliente
    @PutMapping("/actualizar/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente nuevo) {
        return clienteRepository.findById(id).map(c -> {
            c.setNombre(nuevo.getNombre());
            c.setCedula(nuevo.getCedula());
            c.setTelefono(nuevo.getTelefono());
            c.setEmail(nuevo.getEmail());
            return clienteRepository.save(c);
        }).orElseThrow();
    }
}