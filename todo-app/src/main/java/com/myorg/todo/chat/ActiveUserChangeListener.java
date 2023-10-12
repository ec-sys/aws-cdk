package com.myorg.todo.chat;

/**
 * This interface used as an Observer for  ActiveUserManager class
 */
public interface ActiveUserChangeListener {

    /**
     * call when Observable's internal state is changed.
     */
    void notifyActiveUserChange();
}