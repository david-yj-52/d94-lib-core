package tsh.d94.lib.core.data.orm.usersAndPermissions.gsPermission;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsPermissionRepo extends BaseJpaRepository<GsPermissionModel, String> {
}
