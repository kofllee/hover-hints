package net.kofllee.hoverhints.client.hint.input;

import org.lwjgl.glfw.GLFW;

public final class HintKeybinds {

    private HintKeybinds() {}

    public static boolean isHintModeActive(long windowHandle){
        return GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;
    }
}
