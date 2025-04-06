//Implementacion logica
package co.edu.uniquindio.service.impl;
import co.edu.uniquindio.dto.PersonalBodega.CrearPersonalBodegaDTO;
import co.edu.uniquindio.dto.PersonalBodega.EditarPersonalBodegaDTO;
import co.edu.uniquindio.dto.PersonalBodega.PersonalBodegaDTO;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.PersonalBodegaMapper;
import co.edu.uniquindio.repository.PersonalBodegaRepository;
import co.edu.uniquindio.service.PersonalBodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.model.PersonalBodega;



import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalBodegaServiceImp implements PersonalBodegaService {
    private final PersonalBodegaRepository personalRepo;
    private final PersonalBodegaMapper personalMapper;

// Válida y guarda en bd
    @Override
    public void crearPersonalBodega(CrearPersonalBodegaDTO cuentaPersonal) throws Exception {
        if(existeEmail(cuentaPersonal.email())) {
            throw new Exception("El email ya existe");
        }
        if(existeCedula(cuentaPersonal.id())) {
            throw new Exception("La cedula ya existe");
        }
        //Mappear DTO
        PersonalBodega personalBodega = personalMapper.toEntity(cuentaPersonal);
        //Guardar entidad a bd
        personalRepo.save(personalBodega);
    }

    private boolean existeEmail(String email) {
        return personalRepo.findByEmail(email).isPresent();
    }
    private boolean existeCedula(String cedula){
        return personalRepo.findById(cedula).isPresent();
    }


    private PersonalBodega buscarPersonalBodegaPorId(String cedula) throws ElementoNoEncontradoException {
        return personalRepo.findById(cedula)
                .orElseThrow(() -> new ElementoNoEncontradoException("El cliente con ID " + cedula + " no existe."));
    }

    @Override //Buscar por id
    public PersonalBodegaDTO obtenerPersonalBodegaPoId(String id) throws Exception {
        PersonalBodega personalBodega = buscarPersonalBodegaPorId(id);

        return personalMapper.toDto(personalBodega);
    }

    @Override
    public void actualizarPersonalBodega(EditarPersonalBodegaDTO cuentaPersonal) throws Exception {
        PersonalBodega pb = buscarPersonalBodegaPorId(cuentaPersonal.id());

        personalMapper.toEntity(cuentaPersonal, pb);

        personalRepo.save(pb);
    }

    @Override
    public void eliminarPersonalBodega(String id) throws Exception {
        if(!personalRepo.existsById(id)) {
            throw new Exception("El cliente con ID" + id + "no existe");
        }
        personalRepo.deleteById(id);

    }

    @Override
    public List<PersonalBodegaDTO> listarPersonalBod(int pagina) {
        if(pagina<0) throw new RuntimeException("La página no debe ser menor a 0");
        List<PersonalBodega> personalBodegas = personalRepo.findAll(PageRequest.of(pagina, 5)).getContent();
        return personalBodegas.stream()
                .map(personalMapper::toDto)
                .toList();
    }
}
