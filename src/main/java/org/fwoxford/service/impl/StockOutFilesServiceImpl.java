package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.repository.StockOutRequirementRepository;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.repository.StockOutFilesRepository;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.fwoxford.service.mapper.StockOutFilesMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutFiles.
 */
@Service
@Transactional
public class StockOutFilesServiceImpl implements StockOutFilesService{

    private final Logger log = LoggerFactory.getLogger(StockOutFilesServiceImpl.class);

    private final StockOutFilesRepository stockOutFilesRepository;

    private final StockOutFilesMapper stockOutFilesMapper;

    @Autowired
    StockOutRequirementRepository stockOutRequirementRepository;

    public StockOutFilesServiceImpl(StockOutFilesRepository stockOutFilesRepository, StockOutFilesMapper stockOutFilesMapper) {
        this.stockOutFilesRepository = stockOutFilesRepository;
        this.stockOutFilesMapper = stockOutFilesMapper;
    }

    /**
     * Save a stockOutFiles.
     *
     * @param stockOutFilesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFilesDTO save(StockOutFilesDTO stockOutFilesDTO) {
        log.debug("Request to save StockOutFiles : {}", stockOutFilesDTO);
        StockOutFiles stockOutFiles = stockOutFilesMapper.stockOutFilesDTOToStockOutFiles(stockOutFilesDTO);
        stockOutFiles = stockOutFilesRepository.save(stockOutFiles);
        StockOutFilesDTO result = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);
        return result;
    }

    /**
     *  Get all the stockOutFiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFiles");
        Page<StockOutFiles> result = stockOutFilesRepository.findAll(pageable);
        return result.map(stockOutFiles -> stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles));
    }

    /**
     *  Get one stockOutFiles by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFilesDTO findOne(Long id) {
        log.debug("Request to get StockOutFiles : {}", id);
        StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(id);
        StockOutFilesDTO stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);
        return stockOutFilesDTO;
    }

    /**
     *  Delete the  stockOutFiles by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFiles : {}", id);
        stockOutFilesRepository.delete(id);
    }

    @Override
    public StockOutFiles saveFiles(MultipartFile file, HttpServletRequest request) {
        StockOutFiles stockOutFiles = new StockOutFiles();
        if (!file.isEmpty()) {
            try {
                stockOutFiles.setFileName(file.getOriginalFilename());
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
                    + file.getOriginalFilename();
                stockOutFiles.setFilePath(filePath);
                stockOutFiles.setFiles(file.getBytes());
                stockOutFiles.setFilesContentType(file.getContentType());
                stockOutFiles.setFileSize((int)file.getSize());
                stockOutFiles.setFileType(file.getContentType());
                stockOutFiles.setStatus(Constants.VALID);
                stockOutFilesRepository.save(stockOutFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stockOutFiles;
    }

    @Override
    public StockOutFilesDTO findByRequirement(Long requirementId) {
        StockOutFilesDTO stockOutFilesDTO = new StockOutFilesDTO();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(requirementId);
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到需求！");
        }

        if(stockOutRequirement.getImportingFileId()!=null) {
            StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(stockOutRequirement.getImportingFileId());
            stockOutFilesDTO = stockOutFilesMapper.stockOutFilesToStockOutFilesDTO(stockOutFiles);
        }
        return stockOutFilesDTO;
    }
}
