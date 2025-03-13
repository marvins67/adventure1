package com.marvins.adventure1;

import com.marvins.adventure1.graphics.Background;
import com.marvins.adventure1.graphics.ScreenRenderer;
import com.marvins.adventure1.graphics.WalkBoxSidebarRenderer;
import com.marvins.adventure1.graphics.WalkMap;
import com.marvins.adventure1.tool.FileTools;
import com.marvins.adventure1.tool.JsonTools;
import com.marvins.adventure1.tool.Polygon;
import com.marvins.adventure1.tool.PolygonTools;
import com.marvins.adventure1.walkbox.WbFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.marvins.adventure1.Constants.*;

public class WalkBoxEditor extends MouseAdapter {

    private JFrame frame;
    private ScreenRenderer screenRenderer;
    private JFrame sidebar;
    private WalkBoxSidebarRenderer walkBoxSidebarRenderer;
    private Background background;

    private int mouseX = 0;
    private int mouseY = 0;
    private int cursorX = 0;
    private int cursorY = 0;
    private boolean isMouseClicked = false;
    private boolean isMouseClickedLeft = false;
    private boolean isMouseClickedRight = false;

    private boolean isPressUp = false;
    private boolean isPressDown = false;

    private boolean doAddWalkBox = false;
    private boolean doRemoveWalkBox = false;
    private boolean doSaveWalkBox = false;

    private volatile boolean isRunning = true;

    private List<WbFile> wbFiles;
    private String backgroundName = "";

    public void init() {
        System.out.println("Welcome to the mighty walkboxes editor !!");

        screenRenderer = new ScreenRenderer();
        screenRenderer.addMouseListener(this);

        frame = new JFrame(Constants.NAME);
        frame.add(screenRenderer);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE :
                        isRunning = false;
                        break;
                    case KeyEvent.VK_A:
                        doAddWalkBox = true;
                        break;
                    case KeyEvent.VK_S:
                        doSaveWalkBox = true;
                        break;
                    case KeyEvent.VK_R:
                        doRemoveWalkBox = true;
                        break;
                    default:
                        break;
                }
            }
        });

        walkBoxSidebarRenderer = new WalkBoxSidebarRenderer();
        sidebar = new JFrame("WalkBox");
        sidebar.setLocation(800, 0);
        sidebar.add(walkBoxSidebarRenderer);
        sidebar.setSize(250, 800);
        sidebar.setBackground(Color.BLACK);
        sidebar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sidebar.setResizable(false);
        sidebar.setVisible(true);
        sidebar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        isPressUp = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        isPressDown = true;
                        break;
                    default:
                        break;
                }
            }
        });

        //files = FileTools.getFilesInDirectory("wm");
        wbFiles = new ArrayList<>();
        List<String>files = FileTools.getFilesInResourceDirectory("background").stream().filter(f -> f.matches("^[a-z]*_[a-z]*(\\.png)")).collect(Collectors.toList());

        for (String s : files) {
            WbFile wbf = new WbFile();
            wbf.setFile(s.split("\\.")[0]);
            wbFiles.add(wbf);
            walkBoxSidebarRenderer.addFilename(wbf);
        }

        if(!wbFiles.isEmpty()) {
            wbFiles.get(0).setSelected(true);
        }

        backgroundName = wbFiles.stream().filter(WbFile::isSelected).findFirst().get().getFile();

        background = new Background();
        background.read(backgroundName);
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / Constants.FRAMERATE; // Temps par tick pour n FPS
        double frameRateLimit = 0;

        com.marvins.adventure1.tool.Polygon polygon = new com.marvins.adventure1.tool.Polygon();
        WalkMap wm = JsonTools.readJsonFileToObject("wm\\" + backgroundName + "_wm.json", WalkMap.class);
        if (wm == null) {
            wm = new WalkMap();
        }

        while (isRunning) {
            long now = System.nanoTime(); // Récupérer le temps actuel
            frameRateLimit += (now - lastTime) / nsPerTick;
            lastTime = now;

            if (doAddWalkBox) {
                doAddWalkBox = false;
                wm.addPolygon(polygon);
                polygon = new com.marvins.adventure1.tool.Polygon();
            }

            if (doRemoveWalkBox) {
                doRemoveWalkBox = false;
                wm.removePolygon();
            }

            if (doSaveWalkBox) {
                doSaveWalkBox = false;
                JsonTools.writeObjectToJsonFile(wm, "wm\\" + backgroundName + "_wm.json");
            }

            if (isPressUp) {
                isPressUp = false;
                selectPrevious(wbFiles);
                backgroundName = wbFiles.stream().filter(WbFile::isSelected).findFirst().get().getFile();
                background.read(backgroundName);
                wm = JsonTools.readJsonFileToObject("wm\\" + backgroundName + "_wm.json", WalkMap.class);
                if (wm == null) {
                    wm = new WalkMap();
                }
            }


            if (isPressDown) {
                isPressDown = false;
                selectNext(wbFiles);
                backgroundName = wbFiles.stream().filter(WbFile::isSelected).findFirst().get().getFile();
                background.read(backgroundName);
                wm = JsonTools.readJsonFileToObject("wm\\" + backgroundName + "_wm.json", WalkMap.class);
                if (wm == null) {
                    wm = new WalkMap();
                }
            }

            while (frameRateLimit >= 1) {
                frameRateLimit--;
                screenRenderer.drawBackground(background.getBytes());
                screenRenderer.drawWalkMap(wm);
                screenRenderer.drawWalkBoxes(polygon);
                screenRenderer.addWalkBoxKeys();
                screenRenderer.repaint();

                walkBoxSidebarRenderer.repaint();
                updateEngine(wm, polygon);
            }
        }
    }

    private void selectPrevious(List<WbFile> wbFiles) {
        for (int i=0 ; i < wbFiles.size() ; i++) {
            WbFile wbf = wbFiles.get(i);
            if (wbf.isSelected()) {
                wbf.setSelected(false);
                if (i > 0) {
                    wbFiles.get(i-1).setSelected(true);
                } else {
                    wbFiles.get(wbFiles.size() - 1).setSelected(true);
                }
                break;
            }
        }
    }

    private void selectNext(List<WbFile> wbFiles) {
        for (int i=0 ; i < wbFiles.size() ; i++) {
            WbFile wbf = wbFiles.get(i);
            if (wbf.isSelected()) {
                wbf.setSelected(false);
                if (i < wbFiles.size()-1) {
                    wbFiles.get(i+1).setSelected(true);
                } else {
                    wbFiles.get(0).setSelected(true);
                }
                break;
            }
        }
    }

    private void updateEngine(WalkMap wm, Polygon polygon) {
        if (isMouseClicked){
            isMouseClicked = false;
            cursorX = mouseX / GAME_SCALE;
            cursorY = mouseY / GAME_SCALE;

            if(isMouseClickedLeft) {
                isMouseClickedLeft = false;
                if(!PolygonTools.isPointInPolygons(new Point(cursorX, cursorY), wm.getPolygons())) {
                    polygon.addPosition(new Point(cursorX, cursorY));
                }
            } else if(isMouseClickedRight) {
                isMouseClickedRight = false;
                polygon.removeLastPosition();
            }
            System.out.println(polygon);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isMouseClicked = true;
        mouseX = e.getX();
        mouseY = e.getY();
        switch (e.getButton()){
            case MouseEvent.BUTTON1:
                isMouseClickedLeft = true;
                break;
            case MouseEvent.BUTTON3:
                isMouseClickedRight = true;
                break;
            default:
                isMouseClicked = false;
                break;
        }
    }

    public void stop() {
        frame.dispose();
        System.out.println("That's all folks!");
    }
}