package TiendaEGVD_HD.ProyectoFinal.Config;


import TiendaEGVD_HD.ProyectoFinal.Model.Role;
import TiendaEGVD_HD.ProyectoFinal.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String id){
        return roleRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
