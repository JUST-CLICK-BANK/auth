package com.click.auth.domain.dao;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    /**
     * UUID로 고객 정보를 가져옵니다.
     * @param id 찾을 고객의 UUID
     * @return 찾은 고객
     * @throws com.click.auth.exception.NotFoundExcetion 찾을 고객이 없을 경우
     */
    User selectUser(UUID id);

    /**
     * 친구코드로 고객 정보를 가져옵니다.
     * @param code 찾을 고객의 친구코드
     * @return 찾은 고객
     * @throws com.click.auth.exception.NotFoundExcetion 찾을 고객이 없을 경우
     */
    Optional<User> selectOptionalUser(String code);

    /**
     * 소셜 계정 고유번호로 고객 정보를 가져옵니다.
     * @param identity 찾을 고객의 소셜 계정 고유번호
     * @param type 소셜 계정 종류
     * @return 찾은 고객
     * @throws com.click.auth.exception.NotFoundExcetion 찾을 고객이 없을 경우
     */
    Optional<User> selectOptionalUser(String identity, UserIdentityType type);

    /**
     * 친구 코드들로 친구 목록을 가져옵니다.
     * @param code 찾을 친구의 친구코드들
     * @return 찾은 친구들
     */
    List<User> selectAllUser(String[] code);

    User insertUser(User user);

    /**
     * 고객의 로그인 토큰 버전을 갱신합니다.
     * @param id 대상 고객의 UUID
     * @return <i>update</i>가 완료된 고객
     */
    User updateUserTokenVersion(UUID id);

    /**
     * 고객의 비밀번호를 변경합니다.
     * @param id 대상 고객의 UUID
     * @param password 새로 바꿀 비밀번호
     * @return <i>update</i>가 완료된 고객
     */
    User updateUserPassword(UUID id, String password);

    /**
     * 고객의 프로필 사진을 변경합니다.
     * @param id 대상 고객의 UUID
     * @param image 새 프로필 사진
     * @return <i>update</i>가 완료된 고객
     */
    User updateUserImage(UUID id, String image);

    /**
     * 고객의 닉네임을 변경합니다.
     * @param id 대상 고객의 UUID
     * @param nickname 새 닉네임
     * @return <i>update</i>가 완료된 고객
     */
    User updateUserNickname(UUID id, String nickname);

    /**
     * 고객의 신용점수를 수정합니다.
     * @param id 대상 고객의 UUID
     * @param rank 적용할 신용점수
     * @return <i>update</i>가 완료된 고객
     */
    User updateUserRank(UUID id, Integer rank);

    void deleteUser(UUID id);
}
