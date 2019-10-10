package ru.geekbrains.client;

import ru.geekbrains.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MainWindow extends JFrame{
    private Network network;

    private JPanel clientMainPanel;
    private JPanel serverMainPanel;
    private JPanel clientButtonsPanel;
    private JPanel serverButtonsPanel;

    private JButton clientSendFileButton;
    private JButton clientDeleteButton;
    private JButton clientRefreshFileListButton;
    private JButton serverDeleteButton;
    private JButton serverDownloadFileButton;
    private JButton serverRefreshFileListButton;

    private JList<String> clientFileList;
    private JList<String> serverFileList;

    private DefaultListModel<String> clientListModel;
    private DefaultListModel<String> serverListModel;

    private JScrollPane clientScrollPane;
    private JScrollPane serverScrollPane;

    private JLabel clientLabel;
    private JLabel serverLabel;

    private String PATH;
    private final static String BASE_DIRECTORY = "C:\\coding\\cloud-storage\\cloud-storage\\clientDir\\";
    private String username;

    public MainWindow(Network network) {
        this.network = network;
        setTitle("Cloud storage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width / 2 - 300, dimension.height / 2 - 250, 600, 500);
        setLayout(new BorderLayout());
        setClientWindow();
        setServerWindow();
    }

    private void setClientWindow() {
        clientFileList = new JList<>();
        clientListModel = new DefaultListModel<>();
        clientFileList.setModel(clientListModel);
        clientScrollPane = new JScrollPane(clientFileList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        clientMainPanel = new JPanel();
        clientMainPanel.setLayout(new BorderLayout());
        clientMainPanel.add(clientScrollPane, BorderLayout.CENTER);
        add(clientMainPanel, BorderLayout.WEST);

        clientLabel = new JLabel("Локальный список файлов:");
        clientMainPanel.add(clientLabel, BorderLayout.NORTH);

        clientSendFileButton = new JButton("Загрузать");
        clientDeleteButton = new JButton("Удалить");
        clientRefreshFileListButton = new JButton("Обновить");

        clientSendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    byte[] data = Files.readAllBytes(Paths.get(PATH + clientFileList.getSelectedValue()));
                    network.sendRequestAsync(new SendFileRequest(username, clientFileList.getSelectedValue(), data));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        clientDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path selectedPath = Paths.get(PATH + clientFileList.getSelectedValue());
                if (Files.exists(selectedPath)) {
                    try {
                        Files.delete(selectedPath);
                        refreshClientFileList();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Файл был удалён или изменён.", "Невозможно удалить файл", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clientRefreshFileListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshClientFileList();
            }
        });

        clientButtonsPanel = new JPanel();
        clientButtonsPanel.setLayout(new GridLayout(1, 3, 5, 0));
        clientButtonsPanel.add(clientSendFileButton);
        clientButtonsPanel.add(clientDeleteButton);
        clientButtonsPanel.add(clientRefreshFileListButton);
        clientMainPanel.add(clientButtonsPanel, BorderLayout.SOUTH);
    }

    private void setServerWindow() {
        serverFileList = new JList<>();
        serverListModel = new DefaultListModel<>();
        serverFileList.setModel(serverListModel);
        serverScrollPane = new JScrollPane(serverFileList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        serverMainPanel = new JPanel();
        serverMainPanel.setLayout(new BorderLayout());
        serverMainPanel.add(serverScrollPane, BorderLayout.CENTER);
        add(serverMainPanel, BorderLayout.EAST);

        serverLabel = new JLabel("Список файлов в облаке:");
        serverMainPanel.add(serverLabel, BorderLayout.NORTH);

        serverDownloadFileButton = new JButton("Скачать");
        serverDeleteButton = new JButton("Удалить");
        serverRefreshFileListButton = new JButton("Обновить");

        serverDownloadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                network.sendRequestAsync(new DownloadFileRequest(username, serverFileList.getSelectedValue()));
            }
        });

        serverDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                network.sendRequestAsync(new DeleteFileRequest(username, serverFileList.getSelectedValue()));
            }
        });

        serverRefreshFileListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                network.sendRequestAsync(new RefreshFileListRequest(username));
            }
        });

        serverButtonsPanel = new JPanel();
        serverButtonsPanel.setLayout(new GridLayout(1, 3, 5, 0));
        serverButtonsPanel.add(serverDownloadFileButton);
        serverButtonsPanel.add(serverDeleteButton);
        serverButtonsPanel.add(serverRefreshFileListButton);
        serverMainPanel.add(serverButtonsPanel, BorderLayout.SOUTH);
    }


    // To process responses from server
    public void onRefreshFileList(RefreshFileListResponse msg) {
        serverListModel.clear();
        for (String s : msg.getServerFileList()) {
            serverListModel.addElement(s);
        }
    }

    public void onDeleteFile(DeleteFileResponse msg) {
        if (msg.isStatus()) {
            serverListModel.removeElement(msg.getFileName());
            JOptionPane.showMessageDialog(MainWindow.this, "Файл " + msg.getFileName() + " удалён");
        } else {
            JOptionPane.showMessageDialog(MainWindow.this, msg.getStatusDescription(),
                    "Невозможно удалить файл", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onSendFile(SendFileResponse msg) {
        if (msg.isStatus()) {
            serverListModel.addElement(msg.getSendFileName());
            JOptionPane.showMessageDialog(MainWindow.this, "Файл " + msg.getSendFileName() + " загружен");
        } else {
            JOptionPane.showMessageDialog(MainWindow.this, msg.getStatusDescription(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onDownloadFile(DownloadFileResponse msg) {
        String fullPath = PATH + msg.getFileName();
        if (!Files.exists(Paths.get(fullPath))) {
            try {
                Files.write(Paths.get(fullPath), msg.getData(), StandardOpenOption.CREATE);
                clientListModel.addElement(msg.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(MainWindow.this,
                    "Файл с таким именем уже существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
    }

    public void initialize(String username) {
        this.username = username;
        PATH = BASE_DIRECTORY + username + "\\";
        if (!Files.exists(Paths.get(PATH))) {
            try {
                Files.createDirectory(Paths.get(PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        refreshClientFileList();
        network.sendRequestSync(new RefreshFileListRequest(username));
    }

    private void refreshClientFileList() {
        clientListModel.clear();
        try {
            Files.walkFileTree(Paths.get(PATH), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    clientListModel.addElement(file.getFileName().toString());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

