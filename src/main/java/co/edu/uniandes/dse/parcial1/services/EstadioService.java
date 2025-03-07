package co.edu.uniandes.dse.parcial1.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    @Transactional
    public EstadioEntity createEstadio(EstadioEntity estadio) throws IllegalOperationException {

        
        if (estadio.getCapacidadMaximaEstadio()<1000){
            throw new IllegalOperationException("La capacidad del estadio debe ser mayor a 1000");
        }
        
        if (estadio.getPrecioAlquiler() < 100000) {
            throw new IllegalOperationException("El precio del alquiler del concierto debe ser superior a 100000 dolares");
        }
        if (estadio.getNombreCiudad().length()<3) {
            throw new IllegalOperationException("El nombre de la ciudad debe ser una cadena de minimo tres caracteres");
        }
        return estadioRepository.save(estadio);
    }

}
