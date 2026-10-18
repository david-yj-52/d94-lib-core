package tsh.d94.lib.core.data.orm.usersAndPermissions.gsUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tsh.d94.lib.core.data.orm.common.access.AbstractCrudService;
import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

@Slf4j
@Service
public class GsUserRoleAccess extends AbstractCrudService<GsUserRoleModel, String> {

	@Autowired
	GsUserRoleRepo repo;

	@Override
	protected BaseJpaRepository<GsUserRoleModel, String> getRepository() {
		return repo;
	}

}
