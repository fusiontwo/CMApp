package texteditor;

import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import texteditor.ui.ClientStatusUI;
import texteditor.ui.ClientStatusUI2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CMClientEventHandler implements CMAppEventHandler {
    private final CMClientStub clientStub;
    private JFrame statusUI;
    private List<String> onlineUsers;
    private String myUserName;

    public CMClientEventHandler(CMClientStub clientStub) {
        this.clientStub = clientStub;
        this.onlineUsers = new ArrayList<>();
    }

    public void setStatusUI(JFrame statusUI) {
        this.statusUI = statusUI;
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
            case CMSessionEvent.LOGIN_ACK:
                if (event.isValidUser() == 1) {
                    myUserName = clientStub.getMyself().getName();
                    System.out.println("로그인 성공, 사용자: " + myUserName);
                } else {
                    System.out.println("로그인 실패");
                }
                break;

            case CMSessionEvent.SESSION_ADD_USER:
                System.out.println(event.getUserName() + " 님이 접속하셨습니다.");
                onlineUsers.add(event.getUserName());

                List<String> filteredUsers = new ArrayList<>();
                boolean includeUser = false;
                for (String user : onlineUsers) {
                    if (user.equals(myUserName)) {
                        includeUser = true;
                    }
                    if (includeUser) {
                        filteredUsers.add(user);
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    if (statusUI instanceof ClientStatusUI) {
                        ((ClientStatusUI) statusUI).updateUserList(filteredUsers, event.getUserName(), true);
                    } else if (statusUI instanceof ClientStatusUI2) {
                        ((ClientStatusUI2) statusUI).updateUserList(filteredUsers, event.getUserName(), true);
                    }
                });
                break;

            case CMSessionEvent.SESSION_REMOVE_USER:
                System.out.println(event.getUserName() + " 님이 퇴장하셨습니다.");
                onlineUsers.remove(event.getUserName());

                List<String> updatedFilteredUsers = new ArrayList<>();
                boolean stillIncludeUser = false;
                for (String user : onlineUsers) {
                    if (user.equals(myUserName)) {
                        stillIncludeUser = true;
                    }
                    if (stillIncludeUser) {
                        updatedFilteredUsers.add(user);
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    if (statusUI instanceof ClientStatusUI) {
                        ((ClientStatusUI) statusUI).updateUserList(updatedFilteredUsers, event.getUserName(), false);
                    } else if (statusUI instanceof ClientStatusUI2) {
                        ((ClientStatusUI2) statusUI).updateUserList(updatedFilteredUsers, event.getUserName(), false);
                    }
                });
                break;
        }
    }
}
