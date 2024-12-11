package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ar.edu.utn.frbb.tup.controller.validator.CuentaValidator.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);

    @Autowired
    public CuentaService cuentaService;

    @Autowired
    public CuentaValidator cuentaValidator;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        logger.info("Ingresa cuenta controller");
        validate(cuentaDto);
        logger.info("Valida Cuenta");
        return cuentaService.darDeAltaCuenta(cuentaDto);
    }
}