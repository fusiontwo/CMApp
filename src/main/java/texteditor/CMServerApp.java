package texteditor;

import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class CMServerApp {
    private final CMServerStub serverStub;
    private final CMServerEventHandler eventHandler;

    public CMServerApp() {
        serverStub = new CMServerStub();
        eventHandler = new CMServerEventHandler(serverStub);
    }

    public void start() {
        serverStub.setAppEventHandler(eventHandler);
        serverStub.startCM();
    }

    public static void main(String[] args) {
        CMServerApp serverApp = new CMServerApp();
        serverApp.start();
    }
}
