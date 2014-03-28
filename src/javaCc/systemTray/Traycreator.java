package javaCc.systemTray;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Traycreator {

    public void createTray() {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        Image image = Toolkit.getDefaultToolkit().getImage("C:/Users/Sten/IdeaProjects/Cc/src/javaCc/systemTray/images/logo.jpg");
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem sisestaUrl = new MenuItem("sisesta tunniplaani url");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(sisestaUrl);
        popup.add(exitItem);
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}