package ru.geekbrains.client;

import ru.geekbrains.common.*;

import javax.swing.*;

public class Controller {
    private Network network;
    private MainWindow mainWindow;
    private LoginDialog loginDialog;

    public Controller() {
        this.network = new Network();

        new Thread(new Runnable() {
            @Override
            public void run() {
                network.run();
            }
        }).start();

        this.mainWindow = new MainWindow(network);
        this.loginDialog = new LoginDialog(mainWindow, network);

        startEventProcessor();

        loginDialog.setVisible(true);
    }

    private void startEventProcessor() {
        Thread eventsProcessor = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        processMessage(network.getEventQueue().take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        eventsProcessor.start();
    }

    private void processMessage(Object msg) {
        if (msg instanceof DownloadFileResponse) {
            mainWindow.onDownloadFile((DownloadFileResponse) msg);
        } else if (msg instanceof SendFileResponse) {
            mainWindow.onSendFile((SendFileResponse) msg);
        } else if (msg instanceof DeleteFileResponse) {
            mainWindow.onDeleteFile((DeleteFileResponse) msg);
        } else if (msg instanceof RefreshFileListResponse) {
            mainWindow.onRefreshFileList((RefreshFileListResponse) msg);
        } else if (msg instanceof AuthorizeResponse) {
            AuthorizeResponse authorizeResponse = (AuthorizeResponse) msg;
            if (authorizeResponse.isStatus()) {
                loginDialog.dispose();
                mainWindow.setVisible(true);
                mainWindow.initialize(authorizeResponse.getUsername());
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Неверный пароль",
                        "Авторизация", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}
