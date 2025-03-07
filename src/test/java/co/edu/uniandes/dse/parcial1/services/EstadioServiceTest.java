package co.edu.uniandes.dse.parcial1.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EstadioService.class)
public class EstadioServiceTest {

    @Autowired
    private EstadioService estadioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EstadioEntity> estadioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM EstadioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EstadioEntity estadioEntity = factory.manufacturePojo(EstadioEntity.class);
            estadioEntity.setCapacidadMaximaEstadio(5000);
            estadioEntity.setPrecioAlquiler(150000L);
            estadioEntity.setNombreCiudad("Cali");
            entityManager.persist(estadioEntity);
            estadioList.add(estadioEntity);
        }
    }

    @Test
    void testCrearEstadioExitoso() {
        EstadioEntity nuevoEstadio = factory.manufacturePojo(EstadioEntity.class);
        nuevoEstadio.setCapacidadMaximaEstadio(5000);
        nuevoEstadio.setPrecioAlquiler(150000L);
        nuevoEstadio.setNombreCiudad("Bogotá");

        assertDoesNotThrow(() -> {
            EstadioEntity resultado = estadioService.createEstadio(nuevoEstadio);
            assertNotNull(resultado);
            assertNotNull(resultado.getId());
            assertEquals(nuevoEstadio.getCapacidadMaximaEstadio(), resultado.getCapacidadMaximaEstadio());
            assertEquals(nuevoEstadio.getPrecioAlquiler(), resultado.getPrecioAlquiler());
            assertEquals(nuevoEstadio.getNombreCiudad(), resultado.getNombreCiudad());
        });
    }

   
    @Test
    void testCrearEstadioConCapacidadInvalida() {
        EstadioEntity estadio = factory.manufacturePojo(EstadioEntity.class);
        estadio.setCapacidadMaximaEstadio(800); 
        estadio.setPrecioAlquiler(150000L);
        estadio.setNombreCiudad("Medellín");

        assertThrows(IllegalOperationException.class, () -> {
            estadioService.createEstadio(estadio);
        });
    }

}

