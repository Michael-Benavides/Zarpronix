package Zarpronix.infrastructure.persistence; // Cambiado a Z mayúscula

import Zarpronix.domain.model.Cliente; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}