package com.click.auth.service;

import com.click.auth.TestInitData;
import com.click.auth.domain.dao.UserDao;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.FriendCodeUtils;
import com.click.auth.util.JwtUtils;
import com.click.auth.util.PasswordUtils;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest extends TestInitData {

    @InjectMocks
    @Spy
    private AuthServiceImpl authService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordUtils passwordUtils;
    @Mock
    private JwtUtils jwtUtils;

    @Nested
    class createUser {

        @Test
        void 성공_정상적으로_유저_생성됨() {
            // give
            UserCreateRequest req = new UserCreateRequest(
                "3600000000",
                UserIdentityType.KAKAO,
                "수진",
                "000000",
                "image.png"
            );
            String code = "AAAAA";
            String hash = "7edd3bf09fdc20c7c93d0a74700a31d85486f95ee849ebaf776ea30d3108e24b";
            String salt = "58c2fec6f6204220";
            MockedStatic<FriendCodeUtils> friendCodeUtils = mockStatic(FriendCodeUtils.class);
            given(FriendCodeUtils.generateCode()).willReturn(code);
            given(userDao.selectOptionalUser(code)).willReturn(Optional.empty());
            given(passwordUtils.generateSalt()).willReturn(salt);
            given(jwtUtils.createLoginToken(
                LoginTokenResponse.from(req.toEntity(code, hash, salt)))).willReturn(
                "aaaaaaaaaaaaaaaaa");

            // when
            String response = authService.createUser(req);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByCode(code);
            assertEquals("aaaaaaaaaaaaaaaaa", response);
        }
    }

    @Nested
    class findUserByIdentity {

        @Test
        void 성공_정상적으로_찾아서_유저_반환() {
            // give
            String identity = "3600000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            given(userDao.selectOptionalUser(identity, type))
                .willReturn(Optional.of(user));

            // when
            User response = authService.findUserByIdentity(identity, type);

            // then
            Mockito.verify(userDao, Mockito.times(1))
                .selectOptionalUser(identity, type);
            assertEquals(user.getUserId(), response.getUserId());
        }

        @Test
        void 실패_아무것도_찾지_못해서_예외_던짐() {
            // give
            String identity = "0000000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            given(userDao.selectOptionalUser(identity, type))
                .willReturn(Optional.empty());

            // when
            assertThrows(NotFoundExcetion.class,
                () -> authService.findUserByIdentity(identity, type));

            // then
            Mockito.verify(userDao, Mockito.times(1))
                .selectOptionalUser(identity, type);
        }
    }

    @Nested
    class findUserByUuid {

        @Test
        void 성공_정상적으로_찾아서_유저_반환() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            given(userDao.selectUser(id)).willReturn(user);

            // when
            User response = authService.findUserByUuid(id);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectUser(id);
            assertEquals(user.getUserCode(), response.getUserCode());
        }

        @Test
        void 실패_아무것도_찾지_못해서_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            given(userDao.selectUser(id)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.findUserByUuid(id));

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectUser(id);
        }
    }

    @Nested
    class findUserByCode {

        @Test
        void 성공_정상적으로_찾아서_유저_반환() {
            // give
            String code = "AAAAA";
            given(userDao.selectOptionalUser(code)).willReturn(Optional.of(user));

            // when
            UserResponse response = authService.findUserByCode(code);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(code);
            assertEquals(user.getUserId(), response.id());
        }

        @Test
        void 실패_아무것도_찾지_못해서_예외_던짐() {
            // give
            String code = "BBBBB";
            given(userDao.selectOptionalUser(code)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.findUserByCode(code));

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(code);
        }
    }

    @Nested
    class findUsersByCodes {

        @Test
        void 성공_정상적으로_유저목록_반환함() {
            // give
            String[] codes = new String[]{"AAAAA"};
            given(userDao.selectAllUser(codes)).willReturn(List.of(user));

            // when
            List<UserListResponse> response = authService.findUsersByCodes(codes);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectAllUser(codes);
            assertEquals(1, response.size());
            assertEquals(user.getUserId(), response.get(0).id());
        }

        @Test
        void 성공_해당하는_유저가_없어서_빈_리스트_반환함() {
            // give
            String[] codes = new String[]{};
            given(userDao.selectAllUser(codes)).willReturn(List.of());

            // when
            List<UserListResponse> response = authService.findUsersByCodes(codes);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectAllUser(codes);
            assertEquals(0, response.size());
        }
    }

    @Nested
    class updateUserImage {

        @Test
        void 성공_이미지가_업데이트됨() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String image = "changed.png";
            given(userDao.updateUserImage(id, image)).willReturn(user);

            // when
            authService.updateUserImage(id, image);

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserImage(id, image);
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            String image = "changed.png";
            given(userDao.updateUserImage(id, image)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateUserImage(id, image));

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserImage(id, image);
        }
    }

    @Nested
    class updateUserNickname {

        @Test
        void 성공_닉네임이_업데이트됨() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String name = "진수";
            given(userDao.updateUserNickname(id, name)).willReturn(user);

            // when
            authService.updateUserNickname(id, name);

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserNickname(id, name);
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            String name = "진수";
            given(userDao.updateUserNickname(id, name)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateUserNickname(id, name));

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserNickname(id, name);
        }
    }

    @Nested
    class updateUserPassword {

        @Test
        void 성공_비밀번호가_업데이트됨() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String password = "00000";
            given(userDao.updateUserPassword(id, password)).willReturn(user);

            // when
            authService.updateUserPassword(id, password);

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserPassword(id, password);
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String password = "00000";
            given(userDao.updateUserPassword(id, password)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class,
                () -> authService.updateUserPassword(id, password));

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserPassword(id, password);
        }

    }

    @Nested
    class updateTokenVersion {

        @Test
        void 성공_토큰_버전이_1_올라감() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            given(userDao.updateUserTokenVersion(id)).willReturn(user);

            // when
            authService.updateTokenVersion(id);

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserTokenVersion(id);
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            given(userDao.updateUserTokenVersion(id)).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateTokenVersion(id));

            // then
            Mockito.verify(userDao, Mockito.times(1)).updateUserTokenVersion(id);
        }
    }

    @Nested
    class disableUser {

        @Test
        void 성공_유저를_찾아_비활성화함() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            doNothing().when(userDao).deleteUser(id);

            // when
            authService.disableUser(id);

            // then
            Mockito.verify(userDao, Mockito.times(1)).deleteUser(id);
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            doThrow(NotFoundExcetion.class).when(userDao).deleteUser(id);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.disableUser(id));

            // then
            Mockito.verify(userDao, Mockito.times(1)).deleteUser(id);
        }
    }

}