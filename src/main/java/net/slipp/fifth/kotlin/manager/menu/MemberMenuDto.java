package net.slipp.fifth.kotlin.manager.menu;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberMenuDto implements Serializable {

	private static final long serialVersionUID = -2152040898156299920L;

	private Long id;
    private Long memberId;
	private Long menuId;
	private String menuName;
	private String subMenuName;
	private boolean readYn;
	private boolean printYn;
	private boolean createYn;
	private boolean updateYn;
    private Date createdDate;
    
}
