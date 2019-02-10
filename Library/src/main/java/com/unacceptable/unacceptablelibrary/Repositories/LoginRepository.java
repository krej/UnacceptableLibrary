package com.unacceptable.unacceptablelibrary.Repositories;

public interface LoginRepository {
    void SendLoginAttempt(String username, String password, RepositoryCallback callback);
}