package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.AttachmentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Attachment and its DTO AttachmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AttachmentMapper {

    AttachmentDTO attachmentToAttachmentDTO(Attachment attachment);

    List<AttachmentDTO> attachmentsToAttachmentDTOs(List<Attachment> attachments);

    Attachment attachmentDTOToAttachment(AttachmentDTO attachmentDTO);

    List<Attachment> attachmentDTOsToAttachments(List<AttachmentDTO> attachmentDTOs);
}
