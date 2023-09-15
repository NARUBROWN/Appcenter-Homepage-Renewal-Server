package server.inuappcenter.kr.data.domain;

import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Role extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    private String role_name;

    public void setRole(RoleRequestDto roleRequestDto) {
        this.role_name = roleRequestDto.getRole_name();
    }

    public void setRole(Long id, RoleRequestDto roleRequestDto) {
        this.role_id = id;
        this.role_name = roleRequestDto.getRole_name();
    }

    public RoleResponseDto toRoleResponseDto(Role role) {
        return RoleResponseDto.builder()
                .role_id(role.getRole_id())
                .role_name(role.getRole_name())
                .build();
    }
}
