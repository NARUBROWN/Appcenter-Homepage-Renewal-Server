package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Group;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Role;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.GroupRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.GroupResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.GroupRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.MemberRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.RoleRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public GroupResponseDto getGroup(Long id) {
        Group found_group = groupRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
        GroupResponseDto groupResponseDto = new GroupResponseDto();
        groupResponseDto.setGroupResponseDto(found_group);
        return groupResponseDto;
    }

    public List<GroupResponseDto> findAllGroup() {
        List<Group> found_Groups = groupRepository.findAll();
        return found_Groups.stream()
                .map(data -> data.toGroupResponseDto(data))
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupResponseDto assignGroup(Long member_id, Long role_id, GroupRequestDto groupRequestDto) {
        Member found_member = memberRepository.findById(member_id).orElseThrow(CustomNotFoundIdException::new);
        Role found_role = roleRepository.findById(role_id).orElseThrow(CustomNotFoundIdException::new);

        Group group = new Group();
        group.setGroup(found_member, found_role, groupRequestDto);
        Group saved_group = groupRepository.save(group);

        GroupResponseDto groupResponseDto = new GroupResponseDto();
        groupResponseDto.setGroupResponseDto(saved_group);
        return groupResponseDto;
    }

    @Transactional
    public GroupResponseDto updateGroup(GroupRequestDto groupRequestDto, Long id) {
        // 여기서 외래키까지 다 변경할 수 있게 하려고 했는데, 과한듯
        // 그룹 객체 찾기
        Group foundGroup = groupRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
        foundGroup.setGroup(id, groupRequestDto);
        Group savedGroup = groupRepository.save(foundGroup);

        GroupResponseDto groupResponseDto = new GroupResponseDto();
        groupResponseDto.setGroupResponseDto(savedGroup);
        return groupResponseDto;
    }

    // 멤버 찾기
    public boolean findMember(Member member) {
        Group found_member = groupRepository.getByMember(member);
        return found_member != null;
    }

    // 역할 찾기
    public boolean findRole(Role role) {
        Group found_role = groupRepository.getByRole(role);
        return found_role != null;
    }

    public String deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return "id: " + id + " has been successfully deleted.";
    }
}
