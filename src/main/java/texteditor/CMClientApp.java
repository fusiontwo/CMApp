package texteditor;

import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import java.util.Scanner;

public class CMClientApp {
    private final CMClientStub clientStub;
    private final CMClientEventHandler eventHandler;

    public CMClientApp() {
        clientStub = new CMClientStub();
        eventHandler = new CMClientEventHandler(clientStub);
    }

    public void start() {
        clientStub.setAppEventHandler(eventHandler);
        clientStub.startCM();
    }

    public void login(String username, String password) {
        clientStub.loginCM(username, password);
    }

    public void logout() {
        clientStub.logoutCM();
    }

    public CMClientEventHandler getEventHandler() {
        return eventHandler;
    }

    public static void main(String[] args) {
        CMClientApp clientApp = new CMClientApp();
        clientApp.start();

        Scanner scanner = new Scanner(System.in);
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("패스워드: ");
        String password = scanner.nextLine();

        clientApp.login(username, password);

        System.out.println("종료하려면 'exit' 입력:");
        while (!scanner.nextLine().equals("exit")) {
            System.out.println("종료하려면 'exit' 입력:");
        }

        clientApp.logout();
    }
}
