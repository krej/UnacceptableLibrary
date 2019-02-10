package com.unacceptable.unacceptablelibrary;

import android.content.SharedPreferences;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Logic.LoginLogic;
import com.unacceptable.unacceptablelibrary.Repositories.LoginRepository;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginTests {
    private LoginLogic m_oLoginLogic;
    private LoginLogic.View view;
    private LoginRepository repository;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Before
    public void setup() {
        repository = mock(LoginRepository.class);
        prefs = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        m_oLoginLogic = new LoginLogic(repository, prefs);

        view = mock(LoginLogic.View.class);
        m_oLoginLogic.attachView(view);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "";
            }
        }).when(view).getMyString(anyInt());

        when(prefs.edit()).thenReturn(editor);
    }

    @Test
    public void login_emptyUserNamePassword_giveUsernameError() {
        m_oLoginLogic.attemptLogin("", "");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));
    }

    @Test
    public void login_nullCredentials_giveUsernameError() {
        m_oLoginLogic.attemptLogin(null, null);

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));
    }

    @Test
    public void login_usernameFilledEmptyPassword_givePasswordError() {
        m_oLoginLogic.attemptLogin("zak", "");

        verify(view).clearErrors();
        verify(view).setPasswordError(anyString());
        verify(view).sendFocusToPassword();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));
    }

    @Test
    public void log_shortUsername_giveUsernameError() {
        m_oLoginLogic.attemptLogin("z", "111111");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));
    }

    @Test
    public void login_usernameEmptyPasswordFilled_giveUsernameError() {
        m_oLoginLogic.attemptLogin("", "111111");

        verify(view).clearErrors();
        verify(view).setUsernameError(anyString());
        verify(view).sendFocusToUsername();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));
    }

    @Test
    public void login_usernameButShortPassword_givePasswordError() {
        m_oLoginLogic.attemptLogin("zak", "1");

        verify(view).clearErrors();
        verify(view).setPasswordError(anyString());
        verify(view).sendFocusToPassword();
        verify(repository, never()).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));

    }

    @Test
    public void login_validCredentials_SendLoginAttemptIsCalled() {
        String username = "zak";
        String password = "111111";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RepositoryCallback callback = invocation.getArgument(2);
                callback.onSuccess("success");
                return null;
            }
        }).when(repository).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));

        m_oLoginLogic.attemptLogin(username, password);

        verify(view).clearErrors();
        verify(view).showProgress(true);
        verify(repository).SendLoginAttempt(eq(username), eq(password), any(RepositoryCallback.class));
        verify(view).launchNextActivity();
        verify(view).showProgress(false);
    }

    @Test
    public void login_validCredentials_SendLoginAttemptIsCalledButFailed() {
        String username = "zak";
        String password = "111111";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RepositoryCallback callback = invocation.getArgument(2);
                callback.onError(new VolleyError());
                return null;
            }
        }).when(repository).SendLoginAttempt(anyString(), anyString(), any(RepositoryCallback.class));

        m_oLoginLogic.attemptLogin(username, password);

        verify(repository).SendLoginAttempt(eq(username), eq(password), any(RepositoryCallback.class));
        verify(view).showError(anyString());
        verify(view).showProgress(false);
    }
}
