package tsh.d94.lib.core.data.orm.common.access;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tsh.d94.lib.core.data.orm.common.model.BaseModel;
import tsh.d94.lib.core.data.orm.common.repo.BaseJpaRepository;

class AbstractCrudServiceTest {

	@SuppressWarnings("unchecked")
	private final BaseJpaRepository<BaseModel, String> repo = mock(BaseJpaRepository.class);

	private final AbstractCrudService<BaseModel, String> service = new AbstractCrudService<>() {
		@Override
		protected BaseJpaRepository<BaseModel, String> getRepository() {
			return repo;
		}
	};

	@Test
	@DisplayName("findByIdOptional(null) 은 리포지토리를 부르지 않고 empty (트랜잭션을 rollback-only 로 만들지 않음)")
	void findByIdOptional_nullId_returnsEmptyWithoutRepositoryCall() {
		assertThat(service.findByIdOptional(null)).isEmpty();
		verifyNoInteractions(repo);
	}

}
