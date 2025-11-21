package TiendaEGVD_HD.ProyectoFinal.Service;


import TiendaEGVD_HD.ProyectoFinal.Model.Categoria;
import TiendaEGVD_HD.ProyectoFinal.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria guardarCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
    }
}