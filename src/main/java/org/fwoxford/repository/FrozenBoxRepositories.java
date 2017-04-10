package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Created by gengluying on 2017/4/9.
 */
public interface FrozenBoxRepositories  extends DataTablesRepository<FrozenBox,Long> {
    List<FrozenBox> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);
}
