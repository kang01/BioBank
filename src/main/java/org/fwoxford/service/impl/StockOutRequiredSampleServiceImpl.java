package org.fwoxford.service.impl;

import org.apache.commons.beanutils.ConvertUtils;
import org.fwoxford.repository.StockOutRequiredSampleRepositories;
import org.fwoxford.service.StockOutRequiredSampleService;
import org.fwoxford.domain.StockOutRequiredSample;
import org.fwoxford.repository.StockOutRequiredSampleRepository;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.fwoxford.service.mapper.StockOutRequiredSampleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutRequiredSample.
 */
@Service
@Transactional
public class StockOutRequiredSampleServiceImpl implements StockOutRequiredSampleService{

    private final Logger log = LoggerFactory.getLogger(StockOutRequiredSampleServiceImpl.class);

    private final StockOutRequiredSampleRepository stockOutRequiredSampleRepository;

    private final StockOutRequiredSampleMapper stockOutRequiredSampleMapper;
    @Autowired
    private StockOutRequiredSampleRepositories stockOutRequiredSampleRepositories;

    public StockOutRequiredSampleServiceImpl(StockOutRequiredSampleRepository stockOutRequiredSampleRepository, StockOutRequiredSampleMapper stockOutRequiredSampleMapper) {
        this.stockOutRequiredSampleRepository = stockOutRequiredSampleRepository;
        this.stockOutRequiredSampleMapper = stockOutRequiredSampleMapper;
    }

    /**
     * Save a stockOutRequiredSample.
     *
     * @param stockOutRequiredSampleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutRequiredSampleDTO save(StockOutRequiredSampleDTO stockOutRequiredSampleDTO) {
        log.debug("Request to save StockOutRequiredSample : {}", stockOutRequiredSampleDTO);
        StockOutRequiredSample stockOutRequiredSample = stockOutRequiredSampleMapper.stockOutRequiredSampleDTOToStockOutRequiredSample(stockOutRequiredSampleDTO);
        stockOutRequiredSample = stockOutRequiredSampleRepository.save(stockOutRequiredSample);
        StockOutRequiredSampleDTO result = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);
        return result;
    }

    /**
     *  Get all the stockOutRequiredSamples.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutRequiredSampleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutRequiredSamples");
        Page<StockOutRequiredSample> result = stockOutRequiredSampleRepository.findAll(pageable);
        return result.map(stockOutRequiredSample -> stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample));
    }

    /**
     *  Get one stockOutRequiredSample by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutRequiredSampleDTO findOne(Long id) {
        log.debug("Request to get StockOutRequiredSample : {}", id);
        StockOutRequiredSample stockOutRequiredSample = stockOutRequiredSampleRepository.findOne(id);
        StockOutRequiredSampleDTO stockOutRequiredSampleDTO = stockOutRequiredSampleMapper.stockOutRequiredSampleToStockOutRequiredSampleDTO(stockOutRequiredSample);
        return stockOutRequiredSampleDTO;
    }

    /**
     *  Delete the  stockOutRequiredSample by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutRequiredSample : {}", id);
        stockOutRequiredSampleRepository.delete(id);
    }

    @Override
    public DataTablesOutput<StockOutRequiredSampleDTO> getPageStockOutRequiredSampleByRequired(DataTablesInput input, Long id) {
        input.addColumn("stockOutRequirement.id",true,true,"+"+id);
        Converter<StockOutRequiredSample, StockOutRequiredSampleDTO> userConverter = new Converter<StockOutRequiredSample, StockOutRequiredSampleDTO>() {
            @Override
            public StockOutRequiredSampleDTO convert(StockOutRequiredSample e) {
                return new StockOutRequiredSampleDTO(e.getId(),e.getSampleCode(),e.getSampleType(),e.getStatus(),e.getMemo(),e.getStockOutRequirement().getId());
            }
        };
        DataTablesOutput<StockOutRequiredSampleDTO> dtoDataTablesOutput = stockOutRequiredSampleRepositories.findAll(input,userConverter);
        return dtoDataTablesOutput;
    }
}
