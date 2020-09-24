package app;

import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String... args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new App().launch();
    }


    public static class App {
        private void launch() {
            SystemTray systemTray = SystemTray.get();
            systemTray.getMenu().add(new MenuItem("Quit", e -> systemTray.shutdown()));

            Thread updateThread = new Thread(() -> {
                Image[] frames = loadAnimationFrames();
                while (true) {
                    cycleFrames(systemTray, frames);
                }
            });

            updateThread.setDaemon(true);
            updateThread.start();
        }


        private void cycleFrames(SystemTray systemTray, Image[] frames) {
            for (Image frame : frames) {
                systemTray.setImage(frame);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        private Image[] loadAnimationFrames() {
            Image[] frames = new Image[3];
            frames[0] = loadIcon("/update1.png");
            frames[1] = loadIcon("/update2.png");
            frames[2] = loadIcon("/update3.png");
            return frames;
        }

        private Image loadIcon(String resourceName) {
            try {
                return ImageIO.read(App.class.getResourceAsStream(resourceName));
            } catch (IOException e) {
                throw new IllegalArgumentException("Icon cannot be loaded: " + resourceName, e);
            }
        }
    }
}
