package game.ui;

import acm.graphics.GObject;

/**
 * A GUI element class that's addable to GraphicsProgram display
 */
public interface Addable {

    /**
     * @return the constituent components of the GUI element
     */
    GObject[] getComponents();

}
