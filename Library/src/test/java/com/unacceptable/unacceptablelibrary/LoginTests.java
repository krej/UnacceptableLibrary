package com.unacceptable.unacceptablelibrary;

import com.unacceptable.unacceptablelibrary.Logic.LoginLogic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LoginTests {
    private LoginLogic m_oLoginLogic;
    private LoginLogic.View view;

    @Before
    public void setup() {
        m_oLoginLogic = new LoginLogic();
        view = mock(LoginLogic.View.class);
        m_oLoginLogic.attachView(view);

        //doAnswer(doReturn("")).when(view).getMyString(anyInt());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "";
            }
        }).when(view).getMyString(anyInt());
    }

    @Test
    public void login_emptyUserNamePassword_giveUsernameError() {
        m_oLoginLogic.attemptLogin("", "");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void login_nullCredentials_giveUsernameError() {
        m_oLoginLogic.attemptLogin(null, null);

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void login_usernameFilledEmptyPassword_givePasswordError() {
        m_oLoginLogic.attemptLogin("zak", "");

        verify(view).clearErrors();
        verify(view).setPasswordError(anyString());
        verify(view).sendFocusToPassword();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void log_shortUsername_giveUsernameError() {
        m_oLoginLogic.attemptLogin("z", "111111");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void login_usernameEmptyPasswordFilled_giveUsernameError() {
        m_oLoginLogic.attemptLogin("", "111111");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void login_usernameButShortPassword_givePasswordError() {
        m_oLoginLogic.attemptLogin("zak", "1");

        verify(view).clearErrors();
        verify(view).setPasswordError(anyString());
        verify(view).sendFocusToPassword();
        verify(view, never()).SendLoginAttempt(anyString(), anyString());
    }

    @Test
    public void login_validCredentials_SendLoginAttemptIsCalled() {
        String username = "zak";
        String password = "111111";

        m_oLoginLogic.attemptLogin(username, password);

        verify(view).clearErrors();
        verify(view).showProgress(true);
        verify(view).SendLoginAttempt(eq(username), eq(password));
    }
}
