package com.unacceptable.unacceptablelibrary.Repositories;

public interface ILibraryRepository {
    public void Save(String sUrl, byte[] data, RepositoryCallback callback);
}
