package tsh.d94.lib.core.data.orm.usersAndPermissions.gsGroupMember;

import org.springframework.stereotype.Repository;

import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface GsGroupMemberRepo extends BaseJpaRepository<GsGroupMemberModel, String> {
}
