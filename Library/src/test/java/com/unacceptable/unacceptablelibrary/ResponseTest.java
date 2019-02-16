package com.unacceptable.unacceptablelibrary;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Repositories.ILibraryRepository;
import com.unacceptable.unacceptablelibrary.Repositories.LibraryRepository;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class ResponseTest {
    private ILibraryRepository repository;
    private ListableObject m_object;

    @Before
    public void setup() {
        repository = mock(ILibraryRepository.class);
        m_object = new ListableObject();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RepositoryCallback callback = invocation.getArgument(2);
                //this is copied from viewing a DailyLog document in Robo3T
                callback.onSuccess("{Success: \"true\", Message: \"a new ID\"}");
                return null;
            }
        }).when(repository).Save(anyString(), any(byte[].class), any(RepositoryCallback.class));
    }

    @Test
    public void saveNewObject_ReturnsID_ResponseClassConverts() {
        m_object.name = "test";
        Assert.assertEquals(null, m_object.idString);
        m_object.Save(repository);
        Assert.assertEquals("a new ID", m_object.idString);
    }
}
