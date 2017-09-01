package org.ateam.common.repository;


import java.util.List;

import org.ateam.common.model.AnnuityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("annuityInfoRepository")
public interface AnnuityInfoRepository extends JpaRepository<AnnuityInfo, Long> {

	public AnnuityInfo findByPolicyNo(String policyNo);

	@Query("select new AnnuityInfo(a.policyNo as policyNo, a.nenkinShoshoNo as nenkinShoshoNo, a.holderName as holderName, a.holderKname as holderKname, a.annuitantName as annuitantName, a.annuitantKname as annuitantKname, a.keiyakuStatus as keiyakuStatus) from AnnuityInfo a")
	public List<AnnuityInfo> findSearchData();

}
