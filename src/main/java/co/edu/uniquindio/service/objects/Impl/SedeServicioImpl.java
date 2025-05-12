package co.edu.uniquindio.service.objects.Impl;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.repository.objects.SedeRepo;
import co.edu.uniquindio.service.objects.SedeServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SedeServicioImpl implements SedeServicio {

    private final SedeRepo sedeRepo;
    private final SedeMapper sedeMapper;










    @Override
    public List<SedeDto> listarSedesMapa() {
        List<Sede> sedes =  sedeRepo.findAll();
        return sedes.stream()
                .map(sedeMapper::toDto)
                .collect(Collectors.toList());

    }




}
