package tsh.d94.lib.core.data.orm.usersAndPermissions.gsRole;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tsh.d94.lib.core.constant.GlobalTableName;
import tsh.d94.lib.core.data.orm.common.model.BaseModel;

@Entity
@Table(
	name = GlobalTableName.GS_ROLE,
	uniqueConstraints = {
		@UniqueConstraint(name = GsRoleModel.UK01, columnNames = {"ROLE_NM"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class GsRoleModel extends BaseModel {

	public static final String UK01 = "uk_gs_role_01";

	@Column(name = "ROLE_NM", length = 50, nullable = false)
	private String roleNm;

	@Column(name = "DESCR", length = 255)
	private String descr;

}
