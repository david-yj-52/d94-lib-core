package tsh.d94.lib.core.data.orm.usersAndPermissions.gsRole;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsRoleRepo extends BaseJpaRepository<GsRoleModel, String> {
}
