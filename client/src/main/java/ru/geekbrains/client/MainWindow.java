package ru.geekbrains.client;

import ru.geekbrains.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JFrame{
    private Controller controller;

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

    public MainWindow(Controller controller) {
        this.controller = controller;
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
                controller.sendFileClient(clientFileList.getSelectedValue());
            }
        });

        clientDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.deleteFileClient(clientFileList.getSelectedValue());
            }
        });

        clientRefreshFileListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.refreshFileListClient();
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
                controller.downloadFileServer(serverFileList.getSelectedValue());
            }
        });

        serverDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.deleteFileServer(serverFileList.getSelectedValue());
            }
        });

        serverRefreshFileListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.refreshFileListServer();
            }
        });

        serverButtonsPanel = new JPanel();
        serverButtonsPanel.setLayout(new GridLayout(1, 3, 5, 0));
        serverButtonsPanel.add(serverDownloadFileButton);
        serverButtonsPanel.add(serverDeleteButton);
        serverButtonsPanel.add(serverRefreshFileListButton);
        serverMainPanel.add(serverButtonsPanel, BorderLayout.SOUTH);
    }

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

    public void onDownloadFile(String fileName) {
        clientListModel.addElement(fileName);
    }

    public void refreshClientFileList(ArrayList<String> fileList) {
        clientListModel.clear();
        for (String fileName : fileList) {
            clientListModel.addElement(fileName);
        }
    }
}

