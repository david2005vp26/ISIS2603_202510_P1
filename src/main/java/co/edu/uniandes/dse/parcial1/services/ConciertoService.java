package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConciertoService {

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Transactional
    public ConciertoEntity createConcierto(ConciertoEntity concierto) throws IllegalOperationException {

        LocalDateTime fechaActual = LocalDateTime.now();
        
        if (Duration.between(fechaActual,concierto.getFechaConcierto()).isNegative()){
            throw new IllegalOperationException("La fecha del concierto no debe ser en el pasado");
        }
        
        if (concierto.getCapacidadAforo() < 10) {
            throw new IllegalOperationException("La capacidad del concierto debe ser superior a 10 personas");
        }
        if (concierto.getPresupuesto() < 1000) {
            throw new IllegalOperationException("El presupuesto del concierto debe ser superior a 1000 dolares");
        }
        return conciertoRepository.save(concierto);
    }
}



