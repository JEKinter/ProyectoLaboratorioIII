package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

import java.util.List;

import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ClienteService {
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    ClienteDao clienteDao;
    CuentaDao cuentaDao;
    

    public ClienteService(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        logger.info("Ingresa cliente service");
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }   
        logger.info("Valida dni");

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }
        logger.info("Valida edad");

        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta) throws TipoCuentaAlreadyExistsException {
        logger.info("Ingresa agregar cuenta");
        Cliente titular = buscarClientePorDni(cuenta.getTitular());
        logger.info("Encuentra cliente");
        logger.info(cuenta.toString());
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        logger.info("No posee cuenta repetida");
        titular.addCuenta(cuenta);
        logger.info("Agrega cuenta");
        clienteDao.save(titular);
        logger.info("cliente guardado");
    }

    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, true);
        if(cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return cliente;
    }

    
    public List<Cuenta> getCuentasCliente(long dni){
        return cuentaDao.getCuentasByCliente(dni);
    }
}
