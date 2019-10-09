package ru.geekbrains.client;

public class MainClass {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        final Network network = new Network(mainWindow);
        new Thread(new Runnable() {
            @Override
            public void run() {
                network.run();
            }
        }).start();

        mainWindow.setNetwork(network);
        mainWindow.initialize();
        mainWindow.setVisible(true);
    }
}

