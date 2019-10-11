package ru.geekbrains.client;

import ru.geekbrains.common.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Controller {
    private String PATH;
    private final static String BASE_DIRECTORY = "clientDir" + File.separator;
    private String username;

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

        this.mainWindow = new MainWindow(this);
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
            DownloadFileResponse downloadFileResponse = (DownloadFileResponse) msg;
            String fullPath = PATH + downloadFileResponse.getFileName();
            if (!Files.exists(Paths.get(fullPath))) {
                try {
                    Files.write(Paths.get(fullPath), downloadFileResponse.getData(), StandardOpenOption.CREATE);
                    mainWindow.onDownloadFile(downloadFileResponse.getFileName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(mainWindow,
                        "Файл с таким именем уже существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
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
                initialize(authorizeResponse.getUsername());
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Неверный пароль",
                        "Авторизация", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void sendFileClient(String fileName) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(PATH + fileName));
            network.sendRequestAsync(new SendFileRequest(username, fileName, data));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void deleteFileClient(String fileName) {
        Path selectedPath = Paths.get(PATH + fileName);
        if (Files.exists(selectedPath)) {
            try {
                Files.delete(selectedPath);
                refreshFileListClient();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Файл был удалён или изменён.", "Невозможно удалить файл", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void downloadFileServer(String fileName) {
        network.sendRequestAsync(new DownloadFileRequest(username, fileName));
    }

    public void deleteFileServer(String fileName) {
        network.sendRequestAsync(new DeleteFileRequest(username, fileName));
    }

    public void refreshFileListServer() {
        network.sendRequestAsync(new RefreshFileListRequest(username));
    }

    public void refreshFileListClient() {
        try {
            final ArrayList<String> fileList = new ArrayList<>();
            Files.walkFileTree(Paths.get(PATH), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    fileList.add(file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            mainWindow.refreshClientFileList(fileList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize(String username) {
        this.username = username;
        PATH = BASE_DIRECTORY + username + File.separator;
        if (!Files.exists(Paths.get(PATH))) {
            try {
                Files.createDirectory(Paths.get(PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        refreshFileListClient();
        network.sendRequestSync(new RefreshFileListRequest(username));
    }
}
