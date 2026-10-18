package tsh.d94.lib.core.data.orm.usersAndPermissions.gsGroup;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsGroupRepo extends BaseJpaRepository<GsGroupModel, String> {
}
