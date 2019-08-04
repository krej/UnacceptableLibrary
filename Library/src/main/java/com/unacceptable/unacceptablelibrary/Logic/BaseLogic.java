package com.unacceptable.unacceptablelibrary.Logic;

import com.unacceptable.unacceptablelibrary.Repositories.ILibraryRepository;

//import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class BaseLogic<V> implements Serializable {

    protected V view;
    protected ILibraryRepository m_LibraryRepo;


    public interface IShowMessageEvent {
        void ShowMessage(String sMessage);
    }


    private /*@NotNull*/ ArrayList<IShowMessageEvent> m_evtShowMessageListeners;

    protected BaseLogic() {
        m_evtShowMessageListeners = new ArrayList<>();
    }

    public void attachView(V v) {
        view = v;
    }

    public void detachView() {
        view = null;
    }

    public void addShowMessageEventListener(IShowMessageEvent listener) {
        m_evtShowMessageListeners.add(listener);
    }

    protected void fireShowMessageEvent(String sMessage) {
        for (IShowMessageEvent listener : m_evtShowMessageListeners) {
            listener.ShowMessage(sMessage);
        }
    }
}
