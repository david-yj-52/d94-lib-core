package tsh.d94.lib.core.data.orm.usersAndPermissions.gsUserRole;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsUserRoleRepo extends BaseJpaRepository<GsUserRoleModel, String> {
}
