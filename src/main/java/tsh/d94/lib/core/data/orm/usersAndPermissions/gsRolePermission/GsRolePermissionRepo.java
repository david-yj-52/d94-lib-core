package tsh.d94.lib.core.data.orm.usersAndPermissions.gsRolePermission;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsRolePermissionRepo extends BaseJpaRepository<GsRolePermissionModel, String> {
}
