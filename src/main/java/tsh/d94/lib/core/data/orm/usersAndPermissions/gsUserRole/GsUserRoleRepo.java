package tsh.d94.lib.core.data.orm.usersAndPermissions.gsUserRole;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsUserRoleRepo extends BaseJpaRepository<GsUserRoleModel, String> {

	/** 사용자의 사용 중인 역할명 (GS_USER_ROLE → GS_ROLE). 삭제된 연결·역할은 빠진다 */
	@Query("SELECT r.roleNm FROM GsUserRoleModel ur, GsRoleModel r"
		+ " WHERE ur.roleId = r.objId AND ur.userId = :userId"
		+ " AND ur.useStatCd = tsh.d94.lib.core.data.constant.UseStatCd.Usable"
		+ " AND r.useStatCd = tsh.d94.lib.core.data.constant.UseStatCd.Usable")
	List<String> findActiveRoleNames(@Param("userId") String userId);
}
