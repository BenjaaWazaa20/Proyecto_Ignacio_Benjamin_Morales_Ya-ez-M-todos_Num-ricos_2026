package mx.edu.itses.ibmy.metodosnumericos.service;

import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.DeterminantesRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.EliminacionGaussianaRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussJordanRespuesta;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.GaussSeidelResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.JacobiResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.NewtonSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistema;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.PuntoFijoSistemaResultado;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaIterativo;
import mx.edu.itses.ibmy.metodosnumericos.dto.request.sistemas.SistemaLineal;

public interface SistemasEcuaciones {
    DeterminantesRespuesta determinante(SistemaLineal sistemaLineal);
    EliminacionGaussianaRespuesta eliminacionGaussiana(SistemaLineal sistema);
    GaussJordanRespuesta gaussJordan(SistemaLineal request);
    JacobiResultado jacobi(SistemaIterativo request);
    GaussSeidelResultado gaussSeidel(SistemaIterativo request); //[cite: 3]
    PuntoFijoSistemaResultado puntoFijoSistema(PuntoFijoSistema request); //[cite: 3]
    NewtonSistemaResultado newtonSistema(NewtonSistema request);
}