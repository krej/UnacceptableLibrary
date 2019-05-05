package com.unacceptable.unacceptablelibrary.Repositories;

public interface ILibraryRepository {
    public void Save(String sUrl, byte[] data, RepositoryCallback callback);
    void Delete(String sUrl, String sIDString, RepositoryCallback callback);
}
