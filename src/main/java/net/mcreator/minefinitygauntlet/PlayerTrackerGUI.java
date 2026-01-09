package net.mcreator.minefinitygauntlet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerTrackerGUI {
    private static final AtomicBoolean GUI_OPEN = new AtomicBoolean(false);
    private static final AtomicBoolean GUI_INITIALIZED = new AtomicBoolean(false);
    private static volatile long windowId;
    private static Thread guiThread;

    private static int fontTextureId;
    private static int fontBase;
    private static final int CHAR_SIZE = 24; // increased from 12
    private static final int TEXTURE_WIDTH = 16 * CHAR_SIZE;
    private static final int TEXTURE_HEIGHT = 16 * CHAR_SIZE;

    private static final Object dataLock = new Object();
    private static List<PlayerData> trackedPlayers = new ArrayList<>();
    private static int scrollOffset = 0;
    
    private static int windowWidth = 800; // increased
    private static int windowHeight = 900; // increased
    private static final int PADDING = 20;
    private static final int ROW_HEIGHT = 120; // increased

    private static double mouseX = 0;
    private static double mouseY = 0;

    private static SortMode currentSort = SortMode.NAME;
    private static boolean sortAscending = true;

    public static void openGUI() {
        if (GUI_OPEN.compareAndSet(false, true)) {
            guiThread = new Thread(() -> {
                createAndShowGUI();
            }, "PlayerTrackerGUI-Thread");
            guiThread.setDaemon(true);
            guiThread.start();
        } else {
            if (windowId != 0) {
                GLFW.glfwFocusWindow(windowId);
            }
        }
    }

    public static void closeGUI() {
        GUI_OPEN.set(false);
    }

    private static void updatePlayerData() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        
        mc.execute(() -> {
            try {
                ClientLevel level = mc.level;
                Player localPlayer = mc.player;
                
                if (level == null || localPlayer == null) return;
                
                List<PlayerData> tempList = new ArrayList<>();
                
                for (Player player : level.players()) {
                    if (player == null) continue;
                    
                    PlayerData data = new PlayerData();
                    data.name = player.getName().getString();
                    data.x = player.getX();
                    data.y = player.getY();
                    data.z = player.getZ();
                    data.health = player.getHealth();
                    data.maxHealth = player.getMaxHealth();
                    data.armor = player.getArmorValue();
                    data.dimension = level.dimension().location().toString();
                    data.isLocalPlayer = player.getUUID().equals(localPlayer.getUUID());
                    
                    double dx = player.getX() - localPlayer.getX();
                    double dy = player.getY() - localPlayer.getY();
                    double dz = player.getZ() - localPlayer.getZ();
                    data.distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    
                    tempList.add(data);
                }
                
                synchronized (dataLock) {
                    trackedPlayers = tempList;
                    sortPlayers();
                    
                    if (scrollOffset > 0) {
                        int maxScroll = Math.max(0, trackedPlayers.size() - 5);
                        scrollOffset = Math.min(scrollOffset, maxScroll);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error updating player data: " + e.getMessage());
            }
        });
    }

    private static void sortPlayers() {
        if (trackedPlayers.isEmpty()) return;
        
        try {
            switch (currentSort) {
                case NAME:
                    trackedPlayers.sort((a, b) -> sortAscending ? 
                        a.name.compareToIgnoreCase(b.name) : 
                        b.name.compareToIgnoreCase(a.name));
                    break;
                case HEALTH:
                    trackedPlayers.sort((a, b) -> sortAscending ? 
                        Float.compare(a.health, b.health) : 
                        Float.compare(b.health, a.health));
                    break;
                case DISTANCE:
                    trackedPlayers.sort((a, b) -> sortAscending ? 
                        Double.compare(a.distance, b.distance) : 
                        Double.compare(b.distance, a.distance));
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error sorting players: " + e.getMessage());
        }
    }

    private static void createAndShowGUI() {
        try {
            if (GUI_INITIALIZED.compareAndSet(false, true)) {
                GLFWErrorCallback.createPrint(System.err).set();
                
                if (!GLFW.glfwInit()) {
                    System.err.println("Failed to initialize GLFW");
                    GUI_INITIALIZED.set(false);
                    return;
                }
            }

            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // now resizable
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
            
            windowId = GLFW.glfwCreateWindow(windowWidth, windowHeight, "Player Tracker", MemoryUtil.NULL, MemoryUtil.NULL);
            if (windowId == MemoryUtil.NULL) {
                System.err.println("Failed to create window");
                return;
            }

            GLFW.glfwSetWindowCloseCallback(windowId, (window) -> {
                GUI_OPEN.set(false);
            });

            GLFW.glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                    GUI_OPEN.set(false);
                }
                if (action == GLFW.GLFW_PRESS) {
                    if (key == GLFW.GLFW_KEY_N) {
                        toggleSort(SortMode.NAME);
                    } else if (key == GLFW.GLFW_KEY_H) {
                        toggleSort(SortMode.HEALTH);
                    } else if (key == GLFW.GLFW_KEY_D) {
                        toggleSort(SortMode.DISTANCE);
                    }
                }
            });

            GLFW.glfwSetCursorPosCallback(windowId, (window, xpos, ypos) -> {
                mouseX = xpos;
                mouseY = ypos;
            });

            GLFW.glfwSetScrollCallback(windowId, (window, xoffset, yoffset) -> {
                synchronized (dataLock) {
                    scrollOffset = Math.max(0, scrollOffset - (int)yoffset);
                    int maxScroll = Math.max(0, trackedPlayers.size() - 5);
                    scrollOffset = Math.min(scrollOffset, maxScroll);
                }
            });

            // handle window resize
            GLFW.glfwSetFramebufferSizeCallback(windowId, (window, width, height) -> {
                windowWidth = width;
                windowHeight = height;
                GL11.glViewport(0, 0, width, height);
                setupOrthoProjection(width, height);
            });

            long monitor = GLFW.glfwGetPrimaryMonitor();
            if (monitor != MemoryUtil.NULL) {
                int[] width = new int[1];
                int[] height = new int[1];
                GLFW.glfwGetMonitorWorkarea(monitor, null, null, width, height);
                GLFW.glfwSetWindowPos(windowId, (width[0] - windowWidth) / 2, (height[0] - windowHeight) / 2);
            }

            GLFW.glfwShowWindow(windowId);
            GLFW.glfwMakeContextCurrent(windowId);
            GL.createCapabilities();

            GL11.glClearColor(0.05f, 0.05f, 0.08f, 1.0f);
            setupOrthoProjection(windowWidth, windowHeight);
            initTextRendering();

            while (!GLFW.glfwWindowShouldClose(windowId) && GUI_OPEN.get()) {
                try {
                    GLFW.glfwPollEvents();
                    
                    // get current window size
                    int[] currentWidth = new int[1];
                    int[] currentHeight = new int[1];
                    GLFW.glfwGetFramebufferSize(windowId, currentWidth, currentHeight);
                    
                    if (currentWidth[0] != windowWidth || currentHeight[0] != windowHeight) {
                        windowWidth = currentWidth[0];
                        windowHeight = currentHeight[0];
                        setupOrthoProjection(windowWidth, windowHeight);
                    }
                    
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                    updatePlayerData();
                    renderHeader();
                    renderPlayerList();
                    renderFooter();

                    GLFW.glfwSwapBuffers(windowId);
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println("Error in render loop: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            cleanupResources();

        } catch (Exception e) {
            System.err.println("GUI thread error: " + e.getMessage());
            e.printStackTrace();
            GUI_OPEN.set(false);
            windowId = 0;
        }
    }

    private static void renderHeader() {
        GL11.glColor3f(0.3f, 0.8f, 1.0f);
        renderText("PLAYER TRACKER", windowWidth / 2 - 150, 20, 1.5f);

        int playerCount = 0;
        synchronized (dataLock) {
            playerCount = trackedPlayers.size();
        }
        
        GL11.glColor3f(0.7f, 0.7f, 0.7f);
        renderText("Online Players: " + playerCount, PADDING, 65, 1.0f);

        String sortText = "Sort: " + currentSort.name() + (sortAscending ? " ^" : " v");
        GL11.glColor3f(0.6f, 0.6f, 0.9f);
        renderText(sortText, windowWidth - 250, 65, 0.9f);

        GL11.glColor4f(0.3f, 0.3f, 0.4f, 1.0f);
        drawRect(PADDING, 100, windowWidth - 2 * PADDING, 2);
    }

    private static void renderPlayerList() {
        int startY = 120;
        int currentY = startY;
        
        List<PlayerData> playersCopy;
        synchronized (dataLock) {
            if (trackedPlayers.isEmpty()) {
                GL11.glColor3f(0.6f, 0.6f, 0.6f);
                renderText("No players detected...", windowWidth / 2 - 180, windowHeight / 2, 1.2f);
                renderText("Waiting for world data...", windowWidth / 2 - 200, windowHeight / 2 + 40, 1.0f);
                return;
            }
            playersCopy = new ArrayList<>(trackedPlayers);
        }
        
        int visibleRows = Math.max(5, (windowHeight - 250) / (ROW_HEIGHT + 10));
        
        for (int i = scrollOffset; i < playersCopy.size() && i < scrollOffset + visibleRows; i++) {
            if (currentY > windowHeight - 150) break;
            
            PlayerData player = playersCopy.get(i);
            
            if (player.isLocalPlayer) {
                GL11.glColor4f(0.15f, 0.25f, 0.35f, 0.6f);
            } else {
                GL11.glColor4f(0.1f, 0.1f, 0.15f, 0.5f);
            }
            drawRect(PADDING, currentY, windowWidth - 2 * PADDING, ROW_HEIGHT);

            if (player.isLocalPlayer) {
                GL11.glColor4f(0.3f, 0.8f, 1.0f, 0.8f);
            } else {
                GL11.glColor4f(0.3f, 0.3f, 0.4f, 0.6f);
            }
            drawRectOutline(PADDING, currentY, windowWidth - 2 * PADDING, ROW_HEIGHT, 2);

            int textX = PADDING + 15;
            int textY = currentY + 15;

            if (player.isLocalPlayer) {
                GL11.glColor3f(0.3f, 0.8f, 1.0f);
                renderText(player.name + " (You)", textX, textY, 1.2f);
            } else {
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                renderText(player.name, textX, textY, 1.2f);
            }
            textY += 30;

            GL11.glColor3f(0.7f, 0.7f, 0.7f);
            String posText = String.format("X: %.1f  Y: %.1f  Z: %.1f", player.x, player.y, player.z);
            renderText(posText, textX, textY, 0.9f);
            textY += 25;

            if (!player.isLocalPlayer) {
                GL11.glColor3f(0.6f, 0.9f, 0.6f);
                renderText(String.format("Distance: %.1f blocks", player.distance), textX, textY, 0.9f);
            } else {
                GL11.glColor3f(0.5f, 0.5f, 0.5f);
                renderText("Distance: 0.0 blocks", textX, textY, 0.9f);
            }
            textY += 30;

            float healthPercent = player.maxHealth > 0 ? player.health / player.maxHealth : 0;
            int barWidth = windowWidth - 2 * PADDING - 30;
            int barX = textX;
            int barY = textY;

            GL11.glColor4f(0.2f, 0.0f, 0.0f, 0.6f);
            drawRect(barX, barY, barWidth, 15);

            if (healthPercent > 0.6f) {
                GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
            } else if (healthPercent > 0.3f) {
                GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.8f);
            } else {
                GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.8f);
            }
            drawRect(barX, barY, (int)(barWidth * healthPercent), 15);

            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            String healthText = String.format("%.1f / %.1f HP", player.health, player.maxHealth);
            renderText(healthText, barX + 8, barY + 2, 0.8f);

            if (player.armor > 0) {
                GL11.glColor3f(0.7f, 0.7f, 0.9f);
                renderText("Armor: " + (int)player.armor, barX + barWidth - 120, barY + 2, 0.8f);
            }

            currentY += ROW_HEIGHT + 10;
        }

        if (playersCopy.size() > visibleRows) {
            GL11.glColor3f(0.5f, 0.5f, 0.6f);
            String scrollText = String.format("Scroll: %d/%d", Math.min(scrollOffset + 1, playersCopy.size()), playersCopy.size());
            renderText(scrollText, windowWidth - 200, windowHeight - 120, 0.9f);
        }
    }

    private static void renderFooter() {
        int footerY = windowHeight - 90;
        
        GL11.glColor4f(0.3f, 0.3f, 0.4f, 1.0f);
        drawRect(PADDING, footerY, windowWidth - 2 * PADDING, 2);

        GL11.glColor3f(0.6f, 0.6f, 0.6f);
        renderText("N: Sort by Name  |  H: Sort by Health  |  D: Sort by Distance", PADDING, footerY + 15, 0.9f);
        
        GL11.glColor3f(0.5f, 0.5f, 0.5f);
        renderText("Scroll: Navigate  |  ESC: Close", PADDING, footerY + 45, 0.9f);
    }

    private static void toggleSort(SortMode mode) {
        if (currentSort == mode) {
            sortAscending = !sortAscending;
        } else {
            currentSort = mode;
            sortAscending = true;
        }
    }

    private static void drawRect(int x, int y, int width, int height) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private static void drawRectOutline(int x, int y, int width, int height, int thickness) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(thickness);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private static void cleanupResources() {
        try {
            if (GL11.glIsList(fontBase)) {
                GL11.glDeleteLists(fontBase, 256);
            }
            if (GL11.glIsTexture(fontTextureId)) {
                GL11.glDeleteTextures(fontTextureId);
            }
        } catch (Exception e) {
            System.err.println("Error cleaning up OpenGL: " + e.getMessage());
        }

        final long winIdToDestroy = windowId;
        windowId = 0;

        try {
            if (winIdToDestroy != 0) {
                GLFW.glfwDestroyWindow(winIdToDestroy);
            }
        } catch (Exception e) {
            System.err.println("Error destroying window: " + e.getMessage());
        }
    }

    private static void setupOrthoProjection(int width, int height) {
        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void initTextRendering() {
        fontTextureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        BufferedImage fontImage = new BufferedImage(TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fontImage.createGraphics();

        // use bold font for better readability
        Font font = new Font("Arial", Font.BOLD, CHAR_SIZE - 4);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, 
                          java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (int i = 0; i < 256; i++) {
            int x = (i % 16) * CHAR_SIZE;
            int y = (i / 16) * CHAR_SIZE;

            if (i >= 32) {
                String ch = String.valueOf((char) i);
                g.drawString(ch, x + 3, y + CHAR_SIZE - 6);
            }
        }
        g.dispose();

        int[] pixels = new int[TEXTURE_WIDTH * TEXTURE_HEIGHT];
        fontImage.getRGB(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, pixels, 0, TEXTURE_WIDTH);

        ByteBuffer buffer = ByteBuffer.allocateDirect(TEXTURE_WIDTH * TEXTURE_HEIGHT * 4);
        for (int y = 0; y < TEXTURE_HEIGHT; y++) {
            for (int x = 0; x < TEXTURE_WIDTH; x++) {
                int pixel = pixels[y * TEXTURE_WIDTH + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        fontBase = GL11.glGenLists(256);
        float texCharWidth = 1.0f / 16.0f;
        float texCharHeight = 1.0f / 16.0f;

        for (int i = 0; i < 256; i++) {
            float texX = (i % 16) * texCharWidth;
            float texY = (i / 16) * texCharHeight;

            GL11.glNewList(fontBase + i, GL11.GL_COMPILE);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(texX, texY);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(texX + texCharWidth, texY);
            GL11.glVertex2f(CHAR_SIZE, 0);

            GL11.glTexCoord2f(texX + texCharWidth, texY + texCharHeight);
            GL11.glVertex2f(CHAR_SIZE, CHAR_SIZE);

            GL11.glTexCoord2f(texX, texY + texCharHeight);
            GL11.glVertex2f(0, CHAR_SIZE);
            GL11.glEnd();

            GL11.glTranslatef(CHAR_SIZE * 0.6f, 0, 0);
            GL11.glEndList();
        }
    }

    private static void renderText(String text, int x, int y, float scale) {
        if (text == null || text.isEmpty()) return;
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureId);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);

        GL11.glListBase(fontBase);

        for (int i = 0; i < text.length(); i++) {
            GL11.glCallList(fontBase + text.charAt(i));
        }

        GL11.glPopMatrix();
    }

    public static boolean isGuiOpen() {
        return GUI_OPEN.get();
    }

    private static class PlayerData {
        String name = "";
        double x, y, z;
        float health = 0;
        float maxHealth = 20;
        float armor = 0;
        String dimension = "";
        double distance = 0;
        boolean isLocalPlayer = false;
    }

    private enum SortMode {
        NAME, HEALTH, DISTANCE
    }
}