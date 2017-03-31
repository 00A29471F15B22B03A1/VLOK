package ui.login;

import java.util.EventListener;

public interface LoginListener extends EventListener {

    void onLogin(String sessionKey);

}
