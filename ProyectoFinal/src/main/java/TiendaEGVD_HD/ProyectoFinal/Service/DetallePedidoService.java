package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.DetallePedido;
import TiendaEGVD_HD.ProyectoFinal.Repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Transactional(readOnly = true)
    public List<DetallePedido> buscarPorPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    @Transactional(readOnly = true)
    public List<Object[]> obtenerProductosMasVendidos() {
        return detallePedidoRepository.findProductosMasVendidos();
    }
}