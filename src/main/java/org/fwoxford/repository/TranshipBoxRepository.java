package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.TranshipBox;

import org.springframework.data.jpa.repository.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBox entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxRepository extends JpaRepository<TranshipBox,Long> {

    TranshipBox findByTranshipIdAndFrozenBoxId(Long transhipId, Long frozenBoxId);

    @Modifying
    @Query("update TranshipBox t set t.status=?3 where t.tranship.id=?1 and t.frozenBoxCode=?2")
    void updateStatusByTranshipIdAndFrozenBoxCode(Long id, String frozenBoxCode, String status);

    @Query(value = "select t.* from tranship_box t where t.tranship_id =?1 and t.status not in ('"+Constants.FROZEN_BOX_INVALID+"','"+Constants.INVALID+"')" ,nativeQuery = true)
    List<TranshipBox> findByTranshipId(Long id);

    @Query(value = "select t.* from tranship_box t where t.frozen_box_code =?1 and t.status not in ('"+Constants.FROZEN_BOX_INVALID+"','"+Constants.INVALID+"')",nativeQuery = true)
    TranshipBox findByFrozenBoxCode(String frozenBoxCode);

    @Modifying
    @Query("update TranshipBox t set t.status=?1 where t.tranship.id=?2 and t.status not in ('"+Constants.FROZEN_BOX_INVALID+"','"+Constants.INVALID+"')")
    void updateStatusByTranshipId(String status, Long transhipId);

    @Query("select count(t) from TranshipBox t where t.tranship.transhipCode in ?1 and t.status = '"+Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"'")
    Long countByTranshipCodes(List<String> transhipCodeList);

    @Query("select t from TranshipBox t where t.tranship.transhipCode in ?1 and t.status = '"+Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"'")
    List<TranshipBox> findByTranshipCodesAndStatus(List<String> transhipCodeList);

    @Query("select t from TranshipBox t where t.frozenBoxCode in ?1 and t.status = '"+Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"'")
    List<TranshipBox> findByFrozenBoxCodesAndStatus(List<String> transhipCodeList);

    List<TranshipBox> findByFrozenBoxCode1DIn(List<String> boxCode1Str);

    @Query("select t from TranshipBox t where t.status = ?2 and ( t.frozenBoxCode in ?1 or t.frozenBoxCode1D in ?1 )")
    List<TranshipBox> findByFrozenBoxCodeInAndStatus(List<String> frozenBoxCodeStr, String status);

    TranshipBox findByIdAndStatusNot(Long boxId, String status);

    Long countByTranshipIdAndStatusNotIn(Long id, ArrayList<String> status);

    @Query("select t from TranshipBox t where t.tranship.transhipCode = ?1 and t.frozenBoxCode =?2 and t.status!='"+Constants.INVALID+"'")
    TranshipBox findByTranshipCodeAndFrozenBoxCode(String transhipCode, String frozenBoxCode);
}








































