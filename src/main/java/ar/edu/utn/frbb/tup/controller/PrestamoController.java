package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.exception.PrestamoNoOtorgadoException;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {
    private static final Logger logger = LoggerFactory.getLogger(PrestamoController.class);

    @Autowired
    PrestamoService prestamoService;

    @PostMapping
    public PrestamoOutputDto solicitarPrestamo(@Valid @RequestBody PrestamoDto prestamoDto) throws PrestamoNoOtorgadoException {
        logger.info("Ingresa");
        if (prestamoDto == null) {
            logger.error("El objeto PrestamoDto es nulo");
            throw new IllegalArgumentException("El cuerpo de la solicitud no puede ser nulo");
        }
    
        logger.info(String.valueOf(prestamoDto.getNumeroCliente()));
        logger.info(prestamoDto.getMonedaString());
        PrestamoValidator.validate(prestamoDto);
        return prestamoService.pedirPrestamo(prestamoDto);
    }

    @GetMapping("/{clienteDni}")
    public PrestamoConsultaDto retonarPrestamosCliente(@PathVariable long clienteDni){
        return prestamoService.pedirConsultaPrestamos(clienteDni);
    }
}
