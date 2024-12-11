package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.PrestamoConsultaDto;
import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;
import ar.edu.utn.frbb.tup.model.PrestamoConsultaCliente;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import ar.edu.utn.frbb.tup.persistence.PrestamoOutputDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrestamoServiceTest {

    @Mock
    private PrestamoDao prestamoDao;

    
    @Mock
    private Prestamo prestamo;

    @Mock
    private PrestamoOutputDao prestamoOutputDao;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculaIntereses(){
        double calculoManual = 1234.5 * ((double) 5 / 12);
        double calculoMetodo = assertDoesNotThrow( () -> prestamoService.calculaIntereses(1234.5, 5));
        assertEquals(calculoManual, calculoMetodo);
    }

    @Test
    void pedirConsultaPrestamosFailureTest(){
        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();

        when(prestamoDao.getPrestamosByCliente(anyLong())).thenReturn(prestamosCliente);
        assertThrows(IllegalArgumentException.class, () -> { prestamoService.pedirConsultaPrestamos(123); });
    }

}