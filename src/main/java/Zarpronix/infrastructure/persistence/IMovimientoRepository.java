package Zarpronix.infrastructure.persistence;

import Zarpronix.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovimientoRepository extends JpaRepository<Movimiento, Long> {
}