package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConciertoEstadioService {

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private EstadioRepository estadioRepository;


    @Transactional
    public ConciertoEntity asociarConciertoAEstadio(Long conciertoId, Long estadioId) throws IllegalOperationException, EntityNotFoundException {

        ConciertoEntity concierto = conciertoRepository.findById(conciertoId)
                .orElseThrow(() -> new EntityNotFoundException("El concierto no existe."));
        
        EstadioEntity estadio = estadioRepository.findById(estadioId)
                .orElseThrow(() -> new EntityNotFoundException("El estadio no existe."));

        if (concierto.getCapacidadAforo() > estadio.getCapacidadMaximaEstadio()) {
            throw new IllegalOperationException("La capacidad del concierto no puede superar la capacidad del estadio.");
        }

        if (concierto.getPresupuesto() < estadio.getPrecioAlquiler()) {
            throw new IllegalOperationException("El presupuesto del concierto no es suficiente para alquilar el estadio.");
        }

        List<ConciertoEntity> conciertosEnEstadio = estadio.getConciertos();
        
        for (ConciertoEntity c : conciertosEnEstadio) {
            long diasDiferencia = Duration.between(c.getFechaConcierto(), concierto.getFechaConcierto()).toDays();
            if (Math.abs(diasDiferencia) < 2) {
                throw new IllegalOperationException("Debe haber al menos 2 días entre conciertos en el mismo estadio.");
            }
        }

        concierto.setEstadio(estadio);
        concierto = conciertoRepository.save(concierto);

        estadio.getConciertos().add(concierto);
        estadioRepository.save(estadio);

        return concierto;
    }

}
