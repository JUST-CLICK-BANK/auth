package com.click.auth.service;

import com.click.auth.TestInitData;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.repository.UserRepository;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.FriendCodeUtils;
import com.click.auth.util.JwtUtils;
import com.click.auth.util.PasswordUtils;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest extends TestInitData {

    @InjectMocks
    @Spy
    private AuthServiceImpl authService;
    @Mock
    private UserRepository userRepository;
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
            doReturn(null).when(authService).findUserByCode(code);
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
            given(userRepository.findByUserIdentityAndUserIdentityType(identity, type))
                .willReturn(Optional.of(user));

            // when
            User response = authService.findUserByIdentity(identity, type);

            // then
            Mockito.verify(userRepository, Mockito.times(1))
                .findByUserIdentityAndUserIdentityType(identity, type);
            assertEquals(user.getUserId(), response.getUserId());
        }

        @Test
        void 성공_아무것도_찾지_못해서_NULL_반환() {
            // give
            String identity = "0000000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            given(userRepository.findByUserIdentityAndUserIdentityType(identity, type))
                .willReturn(Optional.empty());

            // when
            User response = authService.findUserByIdentity(identity, type);

            // then
            Mockito.verify(userRepository, Mockito.times(1))
                .findByUserIdentityAndUserIdentityType(identity, type);
            assertNull(response);
        }
    }

    @Nested
    class findUserByUuid {

        @Test
        void 성공_정상적으로_찾아서_유저_반환() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            given(userRepository.findById(id)).willReturn(Optional.of(user));

            // when
            User response = authService.findUserByUuid(id);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findById(id);
            assertEquals(user.getUserCode(), response.getUserCode());
        }

        @Test
        void 실패_아무것도_찾지_못해서_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            given(userRepository.findById(id)).willReturn(Optional.empty());

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.disableUser(id));

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        }
    }

    @Nested
    class findUserByCode {

        @Test
        void 성공_정상적으로_찾아서_유저_반환() {
            // give
            String code = "AAAAA";
            UserResponse res = new UserResponse(
                user.getUserId(),
                user.getUserCode(),
                user.getUserImg(),
                user.getUserNickName(),
                user.getUserCreatedAt()
            );
            given(userRepository.findByUserCode(code)).willReturn(Optional.of(user));

            // when
            UserResponse response = authService.findUserByCode(code);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findByUserCode(code);
            assertEquals(user.getUserId(), response.id());
        }

        @Test
        void 성공_아무것도_찾지_못해서_NULL_반환() {
            // give
            String code = "BBBBB";
            given(userRepository.findByUserCode(code)).willReturn(Optional.empty());

            // when
            UserResponse response = authService.findUserByCode(code);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findByUserCode(code);
            assertNull(response);
        }
    }

    @Nested
    class findUsersByCodes {

        @Test
        void 성공_정상적으로_유저목록_반환함() {
            // give
            String[] codes = new String[]{"AAAAA"};
            given(userRepository.findAllByUserCodeIn(codes)).willReturn(List.of(user));

            // when
            List<UserListResponse> response = authService.findUsersByCodes(codes);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findAllByUserCodeIn(codes);
            assertEquals(1, response.size());
            assertEquals(user.getUserId(), response.get(0).id());
        }

        @Test
        void 성공_해당하는_유저가_없어서_빈_리스트_반환함() {
            // give
            String[] codes = new String[]{};
            given(userRepository.findAllByUserCodeIn(codes)).willReturn(List.of());

            // when
            List<UserListResponse> response = authService.findUsersByCodes(codes);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).findAllByUserCodeIn(codes);
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
            doReturn(user).when(authService).findUserByUuid(id);

            // when
            authService.updateUserImage(id, image);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            assertEquals(image, user.getUserImg());
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            String image = "changed.png";
            doThrow(NotFoundExcetion.class).when(authService).findUserByUuid(id);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateUserImage(id, image));

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
        }
    }

    @Nested
    class updateUserNickname {

        @Test
        void 성공_닉네임이_업데이트됨() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String name = "진수";
            doReturn(user).when(authService).findUserByUuid(id);

            // when
            authService.updateUserNickname(id, name);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            assertEquals(name, user.getUserNickName());
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            String name = "진수";
            doThrow(NotFoundExcetion.class).when(authService).findUserByUuid(id);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateUserNickname(id, name));

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
        }
    }

    @Nested
    class updateUserPassword {

        @Test
        void 성공_비밀번호가_업데이트됨() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String password = "00000";
            String salt = "21bd4149975f734e";
            String hashedPassword = "8cb5219acebdb0e3453b91d9aa77b466b67dfb79ce08fc6a68730a5847a24bac";
            doReturn(user).when(authService).findUserByUuid(id);
            given(passwordUtils.generateSalt()).willReturn(salt);
            given(passwordUtils.passwordHashing(password, salt)).willReturn(hashedPassword);

            // when
            authService.updateUserPassword(id, password);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            Mockito.verify(passwordUtils, Mockito.times(1)).passwordHashing(password, salt);
            assertEquals(hashedPassword, user.getUserPasswd());
            assertEquals(11, user.getUserTokenVersion());
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            String password = "00000";
            String salt = "21bd4149975f734e";
            doThrow(NotFoundExcetion.class).when(authService).findUserByUuid(id);

            // when
            assertThrows(NotFoundExcetion.class,
                () -> authService.updateUserPassword(id, password));

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            Mockito.verify(passwordUtils, Mockito.times(0)).passwordHashing(password, salt);
            assertEquals(10, user.getUserTokenVersion());
        }
    }

    @Nested
    class updateTokenVersion {

        @Test
        void 성공_토큰_버전이_1_올라감() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            doReturn(user).when(authService).findUserByUuid(id);

            // when
            authService.updateTokenVersion(id);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            assertEquals(11, user.getUserTokenVersion());
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            doThrow(NotFoundExcetion.class).when(authService).findUserByUuid(id);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.updateTokenVersion(id));

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
        }
    }

    @Nested
    class disableUser {

        @Test
        void 성공_유저를_찾아_비활성화함() {
            // give
            UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
            doReturn(user).when(authService).findUserByUuid(id);

            // when
            authService.disableUser(id);

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
            assertEquals(true, user.getIsDisable());
        }

        @Test
        void 실패_유저를_찾지_못해_예외_던짐() {
            // give
            UUID id = UUID.fromString("11111111-0000-0000-0000-000000000000");
            doThrow(NotFoundExcetion.class).when(authService).findUserByUuid(id);

            // when
            assertThrows(NotFoundExcetion.class, () -> authService.disableUser(id));

            // then
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(id);
        }
    }
}