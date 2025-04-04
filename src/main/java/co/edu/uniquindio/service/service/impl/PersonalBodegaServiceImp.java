//Implementacion logica
package co.edu.uniquindio.service.service.impl;
import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.mapper.PersonalBodegaMapper;
import co.edu.uniquindio.repository.PersonalBodegaRepository;
import co.edu.uniquindio.service.service.PersonalBodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.model.mod.PersonalBodega;



import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalBodegaServiceImp implements PersonalBodegaService {
    private final PersonalBodegaRepository personalRepo;
    private final PersonalBodegaMapper personalMapper;

//Valida y guarda en bd
    @Override
    public void crearPersonalBodega(PersonalBodegaDTO cuentaPersonal) throws Exception {
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
        return personalRepo.findByCedula(cedula).isPresent();
    }

    @Override //Buscar por id
    public PersonalBodegaDTO obtenerPersonalBodegaPoId(String id) throws Exception {
        Optional<PersonalBodega> personalBodega = personalRepo.findById(id);
        if (personalBodega.isEmpty()) {
            throw new Exception("No se encontró  personal de bodega con ID: " + id);
        }
        return personalMapper.toDTO(personalBodega.get());
    }

    @Override
    public void actualizarPersonalBodega(PersonalBodegaDTO cuentaPersonal) throws Exception {
        Optional<PersonalBodega> personalExistente = personalRepo.findById(cuentaPersonal.id());
        if (personalExistente.isEmpty()) {
            throw new Exception("No se encontró un personal de bodega con ID: " + cuentaPersonal.id());
        }

        //  Actualizar los datos existentes
        PersonalBodega personalActualizado = personalMapper.toEntity(cuentaPersonal);
        personalRepo.save(personalActualizado);

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

        Pageable pageable = PageRequest.of(pagina, 5);
        List<PersonalBodega> personalBodegas = personalRepo.findAll(pageable).getContent();


        //Pageable=Ordenamiento en consultas a bd

        return personalBodegas.stream()
                .map(personalMapper::toDTO)
                .toList();
    }
}
