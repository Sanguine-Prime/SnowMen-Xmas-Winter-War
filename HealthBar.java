package com.cs304.lab9.Example1;

import javax.media.opengl.GL;

public class HealthBar {

    // Health values
    private int health1;
    private int health2;
    private final int MAX_HEALTH = 100;

    // Position and size
    private int barWidth = 80;
    private int barHeight = 10;
    private int padding = 10;

    // Screen dimensions
    private int screenWidth = 100;
    private int screenHeight = 100;

    // Texture indices (set these after loading textures)
    private int healthBarBgTexture = -1;
    private int healthBarFillTexture = -1;

    // Colors for player indicators
    private final float[] PLAYER1_COLOR = {0.0f, 0.5f, 1.0f}; // Blue
    private final float[] PLAYER2_COLOR = {1.0f, 0.3f, 0.3f}; // Red


    public HealthBar() {
        this.health1 = MAX_HEALTH;
        this.health2 = MAX_HEALTH;
    }


    public HealthBar(int barWidth, int barHeight) {
        this();
        this.barWidth = barWidth;
        this.barHeight = barHeight;
    }


    //Set screen dimensions (should match your game's dimensions)

    public void setScreenDimensions(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    //Set texture indices
    public void setTextureIndices(int bgTexture, int fillTexture) {
        this.healthBarBgTexture = bgTexture;
        this.healthBarFillTexture = fillTexture;
    }

    //Draw both health bars
    public void draw(GL gl) {
        if (healthBarBgTexture == -1 || healthBarFillTexture == -1) {
            System.err.println("HealthBar: Textures not set! Call setTextureIndices() first.");
            return;
        }

        // Draw player 1 health bar (top left)
        drawSingleHealthBar(gl, padding, screenHeight - padding - barHeight,
                health1, true, "P1");

        // Draw player 2 health bar (top right)
        drawSingleHealthBar(gl, screenWidth - padding - barWidth,
                screenHeight - padding - barHeight,
                health2, false, "P2");
    }

    //Draw a single health bar
    private void drawSingleHealthBar(GL gl, int x, int y, int health,
                                     boolean isPlayer1, String label) {
        gl.glEnable(GL.GL_BLEND);

        // Convert game coordinates to OpenGL coordinates
        float screenX = convertXToGL(x);
        float screenY = convertYToGL(y);

        // Draw health bar background
        drawBarPart(gl, screenX, screenY, barWidth, barHeight,
                1.0f, healthBarBgTexture);

        // Draw health fill (clamp health between 0-100)
        health = Math.max(0, Math.min(health, MAX_HEALTH));
        float healthPercent = health / 100.0f;
        drawBarPart(gl, screenX, screenY, barWidth * healthPercent, barHeight,
                healthPercent, healthBarFillTexture);

        // Draw player indicator
        drawPlayerIndicator(gl, screenX, screenY, barWidth, barHeight,
                isPlayer1, label);

        // Draw health text
        drawHealthText(gl, screenX, screenY, barWidth, barHeight, health);

        gl.glDisable(GL.GL_BLEND);
    }

    //Draw a single part of the health bar (background or fill)
    private void drawBarPart(GL gl, float screenX, float screenY, float width,
                             float height, float textureCoord, int textureIndex) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex);
        gl.glPushMatrix();

        // Position and scale
        gl.glTranslated(screenX, screenY, 0);
        gl.glScaled(width / 50.0, height / 50.0, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureCoord, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(textureCoord, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
    }

    //Draw player indicator (colored box with label)
    private void drawPlayerIndicator(GL gl, float screenX, float screenY,
                                     float width, float height,
                                     boolean isPlayer1, String label) {
        gl.glDisable(GL.GL_TEXTURE_2D);

        // Set color based on player
        float[] color = isPlayer1 ? PLAYER1_COLOR : PLAYER2_COLOR;
        gl.glColor3f(color[0], color[1], color[2]);

        // Draw colored box to the left of the health bar
        float boxWidth = 0.03f;
        float boxHeight = height / 50.0f;
        float boxX = screenX - boxWidth - 0.01f;

        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(boxX, screenY);
        gl.glVertex2f(boxX + boxWidth, screenY);
        gl.glVertex2f(boxX + boxWidth, screenY + boxHeight);
        gl.glVertex2f(boxX, screenY + boxHeight);
        gl.glEnd();

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Reset color
    }

    //Draw health value as text (simple numeric display)
    private void drawHealthText(GL gl, float screenX, float screenY,
                                float width, float height, int health) {
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f); // White text

        // Draw health number above the bar
        float textY = screenY + height / 50.0f + 0.01f;
        float textX = screenX + (width / 50.0f) / 2 - 0.01f;

        // Simple number drawing using lines
        drawNumber(gl, health, textX, textY, 0.02f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Reset color
    }

    //Simple number drawing using lines (0-100)
    private void drawNumber(GL gl, int number, float x, float y, float size) {
        String numStr = String.valueOf(number);

        for (int i = 0; i < numStr.length(); i++) {
            char digit = numStr.charAt(i);
            drawDigit(gl, digit, x + i * size * 0.6f, y, size);
        }
    }

    //Draw a single digit (0-9)
    private void drawDigit(GL gl, char digit, float x, float y, float size) {
        gl.glLineWidth(2.0f);
        gl.glBegin(GL.GL_LINES);

        switch (digit) {
            case '0':
                drawDigit0(gl, x, y, size);
                break;
            case '1':
                drawDigit1(gl, x, y, size);
                break;
            case '2':
                drawDigit2(gl, x, y, size);
                break;
            case '3':
                drawDigit3(gl, x, y, size);
                break;
            case '4':
                drawDigit4(gl, x, y, size);
                break;
            case '5':
                drawDigit5(gl, x, y, size);
                break;
            case '6':
                drawDigit6(gl, x, y, size);
                break;
            case '7':
                drawDigit7(gl, x, y, size);
                break;
            case '8':
                drawDigit8(gl, x, y, size);
                break;
            case '9':
                drawDigit9(gl, x, y, size);
                break;
        }

        gl.glEnd();
    }

    // Digit drawing methods (simplified)
    private void drawDigit0(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y);
    }

    private void drawDigit1(GL gl, float x, float y, float size) {
        gl.glVertex2f(x + size / 2, y);
        gl.glVertex2f(x + size / 2, y + size);
    }

    private void drawDigit2(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x + size, y + size);
    }

    private void drawDigit3(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
    }

    private void drawDigit4(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
    }

    private void drawDigit5(GL gl, float x, float y, float size) {
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x, y + size);
    }

    private void drawDigit6(GL gl, float x, float y, float size) {
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
    }

    private void drawDigit7(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
    }

    private void drawDigit8(GL gl, float x, float y, float size) {
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y + size);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x + size, y + size / 2);
    }

    private void drawDigit9(GL gl, float x, float y, float size) {
        gl.glVertex2f(x + size, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y + size / 2);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x + size, y + size);
        gl.glVertex2f(x, y + size);
    }

    //Convert X coordinate from game space to OpenGL space
    private float convertXToGL(int x) {
        return (float) (x / (screenWidth / 2.0) - 0.9);
    }

    //Convert Y coordinate from game space to OpenGL space
    private float convertYToGL(int y) {
        return (float) (y / (screenHeight / 2.0) - 0.9);
    }

    // Getters and setters for health values
    public int getHealth1() {
        return health1;
    }

    public void setHealth1(int health) {
        this.health1 = Math.max(0, Math.min(health, MAX_HEALTH));
    }

    public int getHealth2() {
        return health2;
    }

    public void setHealth2(int health) {
        this.health2 = Math.max(0, Math.min(health, MAX_HEALTH));
    }

    //Apply damage to a player
    public void takeDamage(boolean isPlayer1, int damage) {
        if (isPlayer1) {
            health1 -= damage;
            if (health1 < 0) health1 = 0;
        } else {
            health2 -= damage;
            if (health2 < 0) health2 = 0;
        }
    }

    //Heal a player
    public void heal(boolean isPlayer1, int healAmount) {
        if (isPlayer1) {
            health1 += healAmount;
            if (health1 > MAX_HEALTH) health1 = MAX_HEALTH;
        } else {
            health2 += healAmount;
            if (health2 > MAX_HEALTH) health2 = MAX_HEALTH;
        }
    }

    //Check if a player is dead
    public boolean isPlayerDead(boolean isPlayer1) {
        return isPlayer1 ? health1 <= 0 : health2 <= 0;
    }

    //Reset both health bars to full
    public void reset() {
        health1 = MAX_HEALTH;
        health2 = MAX_HEALTH;
    }

    //Check if game is over (one player dead)
    public boolean isGameOver() {
        return health1 <= 0 || health2 <= 0;
    }

    //Get the winner (1 for player 1, 2 for player 2, 0 for tie/no winner)
    public int getWinner() {
        if (health1 <= 0 && health2 <= 0) return 0; // Tie
        if (health1 <= 0) return 2; // Player 2 wins
        if (health2 <= 0) return 1; // Player 1 wins
        return 0; // No winner yet
    }
}