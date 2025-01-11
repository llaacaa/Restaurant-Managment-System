package rs.raf.reservation_service.service;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.Tables;
import rs.raf.reservation_service.repository.TablesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TablesService {
    private final TablesRepository tablesRepository;

    public TablesService(TablesRepository tablesRepository) {
        this.tablesRepository = tablesRepository;
    }

    public List<Tables> getTables() {
        return tablesRepository.findAll();
    }

    public Optional<Tables> getTable(Long id) {
        return tablesRepository.findById(id);
    }

    public void updateTable(Tables table) {
        tablesRepository.save(table);
    }


}
