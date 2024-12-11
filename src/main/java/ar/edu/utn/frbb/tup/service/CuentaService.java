package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaController;
import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CuentaService {
    private static final Logger logger = LoggerFactory.getLogger(CuentaService.class);

    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        logger.info("Crea cuenta con dto");
        logger.info(cuenta.toString());
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
        logger.info("cuenta no repetida");

        //Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        // if (!tipoCuentaEstaSoportada(cuenta)) {...}

       if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta " + cuenta.getTipoCuenta() + " no esta soportada.");
        }
        logger.info("cuenta soportada");

        clienteService.agregarCuenta(cuenta);
        logger.info("cuenta agregada a cliente");
        cuentaDao.save(cuenta);
        logger.info("cuenta guardada");
        logger.info(cuenta.toString());
        return cuenta;
    }

    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS) || (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES));
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }

    public void actualizarCuentaCliente(Prestamo prestamo){
        List<Cuenta> cuentas = clienteService.getCuentasCliente(prestamo.getNumeroCliente());
        boolean prestamoOtorgado=false;
        for (Cuenta c : cuentas){
            if (c.getMoneda().equals(prestamo.getMoneda())&&!prestamoOtorgado){
                double balanceActualizado = c.getBalance()+ prestamo.getMontoPrestamo();
                c.setBalance(balanceActualizado);
                cuentaDao.save(c);
                prestamoOtorgado=true;
            }
            // if (c.getTipoCuenta().equals(TipoDeCuenta.CAJA_DE_AHORROS) && c.getMoneda().equals(TipoMoneda.fromString(prestamo.getMoneda()))){
            //     double saldoActualizado = c.getSaldo()+ prestamo.getMontoPrestamo();
            //     c.setSaldo(saldoActualizado);
            //     cuentaDao.save(c);
            // }
        }

    }
}
