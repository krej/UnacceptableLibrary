package com.unacceptable.unacceptablelibrary.Repositories;

public interface ILoginRepository {
    void SendLoginAttempt(String username, String password, RepositoryCallback callback);
}