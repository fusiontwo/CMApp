package texteditor;

import kr.ac.konkuk.ccslab.cm.entity.CMMember;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import java.util.ArrayList;
import java.util.List;

class CMServerEventHandler implements CMAppEventHandler {
    private final CMServerStub serverStub;

    public CMServerEventHandler(CMServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    public void processEvent(CMEvent cme) {
        switch (cme.getType()) {
            case CMInfo.CM_SESSION_EVENT:
                handleSessionEvent((CMSessionEvent) cme);
                break;
        }
    }

    private void handleSessionEvent(CMSessionEvent event) {
        switch (event.getID()) {
            case CMSessionEvent.LOGIN:
                System.out.println("[Server Log] " + event.getUserName() + " 님 로그인");

                if (!isUserAlreadyLoggedIn(event.getUserName())) {
                    notifyClients(event.getUserName(), CMSessionEvent.SESSION_ADD_USER);
                }
                break;

            case CMSessionEvent.LOGOUT:
                System.out.println("[Server Log] " + event.getUserName() + " 님 로그아웃");

                if (isUserAlreadyLoggedIn(event.getUserName())) {
                    notifyClients(event.getUserName(), CMSessionEvent.SESSION_REMOVE_USER);
                }
                break;
        }
    }

    private boolean isUserAlreadyLoggedIn(String username) {
        CMInteractionInfo interactionInfo = serverStub.getCMInfo().getInteractionInfo();
        CMMember loginUsers = interactionInfo.getLoginUsers();

        for (CMUser user : loginUsers.getAllMembers()) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void notifyClients(String newUser, int eventType) {
        CMInteractionInfo interactionInfo = serverStub.getCMInfo().getInteractionInfo();
        CMMember loginUsers = interactionInfo.getLoginUsers();

        List<String> orderedUsers = new ArrayList<>();
        for (CMUser user : loginUsers.getAllMembers()) {
            orderedUsers.add(user.getName());
        }

        CMSessionEvent notifyEvent = new CMSessionEvent();
        notifyEvent.setID(eventType);
        notifyEvent.setUserName(newUser);

        boolean notify = false;
        for (String existingUser : orderedUsers) {
            if (existingUser.equals(newUser)) {
                notify = true;
                continue;
            }
            if (notify) {
                System.out.println(existingUser + " 님께 " + newUser + " 접속 이벤트 전송");
                serverStub.send(notifyEvent, existingUser);
            }
        }
    }
}
