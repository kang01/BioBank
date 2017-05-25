package org.fwoxford.service;

import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service Interface for managing StockOutFiles.
 */
public interface StockOutFilesService {

    /**
     * Save a stockOutFiles.
     *
     * @param stockOutFilesDTO the entity to save
     * @return the persisted entity
     */
    StockOutFilesDTO save(StockOutFilesDTO stockOutFilesDTO);

    /**
     *  Get all the stockOutFiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutFilesDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutFiles.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutFilesDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutFiles.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    StockOutFiles saveFiles(MultipartFile file, HttpServletRequest request);
}
