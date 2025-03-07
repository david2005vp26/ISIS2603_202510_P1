package co.edu.uniandes.dse.parcial1.services;

import java.time.LocalDateTime;
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

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ConciertoService.class)
public class ConciertoServiceTest {

    @Autowired
    private ConciertoService conciertoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ConciertoEntity> conciertoList = new ArrayList<>();

  
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM MedicoEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ConciertoEntity conciertoEntity = factory.manufacturePojo(ConciertoEntity.class);
            LocalDateTime fechaConcierto = LocalDateTime.now().plusDays(5);
            conciertoEntity.setFechaConcierto(fechaConcierto);
            conciertoEntity.setCapacidadAforo(15);
            conciertoEntity.setPresupuesto(10000L);


            entityManager.persist(conciertoEntity);
            conciertoList.add(conciertoEntity);
        }
    }


    @Test
    void testCrearConciertoExitoso() {
        ConciertoEntity nuevoConcierto = factory.manufacturePojo(ConciertoEntity.class);
        nuevoConcierto.setFechaConcierto(LocalDateTime.now().plusDays(10)); 
        nuevoConcierto.setCapacidadAforo(50);
        nuevoConcierto.setPresupuesto(20000L);

        assertDoesNotThrow(() -> {
            ConciertoEntity resultado = conciertoService.createConcierto(nuevoConcierto);
            assertNotNull(resultado);
            assertNotNull(resultado.getId());
            assertEquals(nuevoConcierto.getCapacidadAforo(), resultado.getCapacidadAforo());
            assertEquals(nuevoConcierto.getPresupuesto(), resultado.getPresupuesto());
        });
    }

    @Test
    void testCrearConciertoConAforoInvalido() {
        ConciertoEntity nuevoConcierto = factory.manufacturePojo(ConciertoEntity.class);
        nuevoConcierto.setFechaConcierto(LocalDateTime.now().plusDays(5));
        nuevoConcierto.setCapacidadAforo(0); // Aforo inválido
        nuevoConcierto.setPresupuesto(15000L);

        assertThrows(IllegalOperationException.class, () -> {
            conciertoService.createConcierto(nuevoConcierto);
        });
    }


}
