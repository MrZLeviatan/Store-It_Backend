package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;

import java.util.List;

public interface SedeServicio {


    List<SedeDto> listarSedesMapa();

}
