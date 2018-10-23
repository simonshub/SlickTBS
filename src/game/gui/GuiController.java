/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.utils.Log;
import org.simon.src.main.ResourceManager;
import org.simon.src.main.Settings;

/**
 *
 * @author emil.simon
 */
public final class GuiController {
    
    public static int CLICK_LOCK_DURATION = 200;
    
    public static float FLOATING_TEXT_ELEMENT_WIDTH = 1f;
    public static float FLOATING_TEXT_ELEMENT_HEIGHT = 1f;
    
    public static float FLOATING_TEXT_FADE_SPEED = 0.5f;
    public static float FLOATING_TEXT_FLOAT_SPEED = 0.1f;
    
    public static int FLOATING_TEXT_FONT_SIZE = 48;
    public static String FLOATING_TEXT_FONT = "consolas";
    public static String FLOATING_TEXT_ELEMENT_NAME = "floating_text_label_instance_";
    
    
    
    private BasicGameState parent;
    
    private final Map<String,GuiElement> elements;
    
    private boolean visible = true;
    
    private boolean click_lock = false;
    private int time_since_last_click = 0;
    
    private int tooltip_display_delay_ms = 500;
    private float tooltip_x_margin = 5f;
    private float tooltip_y_margin = 5f;
    private float tooltip_max_width = 250f;
    private Color tooltip_font_color = new Color (1f,1f,1f,0.9f);
    private Color tooltip_background_color = new Color (0f,0f,0f,0.5f);
    private String tooltip_background_image = Settings.default_tooltip_background_image;
    private TrueTypeFont tooltip_font = ResourceManager.getFont(Settings.default_tooltip_font, Settings.default_tooltip_font_size);
    
    private int drag_n_drop_activate_time = 500;
    private float drag_n_drop_element_alpha = 0.5f;
    private boolean drag_n_drop_enabled = false;
    private GuiElement drag_n_drop_element = null;
    
    private float mouse_x, mouse_y;
    
    
    
    public GuiController () {
        this.elements = new HashMap<> ();
    }
    
    
    
    public GuiController addElement (String name, GuiElement el) {
        elements.put(name, el);
        return this;
    }
    
    public void removeElement (String name) {
        // remove reference from controller's list
        elements.remove(name);
    }
    
    public boolean hasElement (String name) {
        return elements.containsKey(name);
    }
    
    public GuiElement getElement (String name) {
        if (!elements.containsKey(name)) {
            Log.err("no element of name '"+name+"'");
            return null;
        }
        return elements.get(name);
    }
    
    public List<GuiElement> getElements (String name_contains) {
        List<GuiElement> result_list = new ArrayList<> ();
        for (String key : elements.keySet()) {
            if (key.contains(name_contains))
                result_list.add(elements.get(key));
        }
        return result_list;
    }
    
    public String addFloatingText (String text, Color col, float x, float y) {
        return addFloatingText (text, col, "", x, y);
    }
    
    public String addFloatingText (String text, Color col, String icon, float x, float y) {
        String el_name;
        long id = System.currentTimeMillis();
        
        el_name = FLOATING_TEXT_ELEMENT_NAME+id;
        GuiElement floating_text = new GuiElement (el_name, this, false, x, y, false, FLOATING_TEXT_ELEMENT_WIDTH, FLOATING_TEXT_ELEMENT_HEIGHT, "")
                .setText(text)
                .setFont(FLOATING_TEXT_FONT, FLOATING_TEXT_FONT_SIZE)
                .setTextColor(col.r, col.g, col.b, col.a)
                .setLayer(10)
                .setProperty("fadeout_callback", "destroy")
                .setProperty("acceleration", 0f)
                .setProperty("float_limit", 10000f)
                .setProperty("fade_speed", FLOATING_TEXT_FADE_SPEED)
                .setProperty("float_speed", FLOATING_TEXT_FLOAT_SPEED)
                .addOnUpdate("floatup")
                .instantCall("fadeout");
        if (ResourceManager.hasGraphics(icon)) floating_text.setImage(icon);
        this.addElement(el_name, floating_text);
        
        return el_name;
    }
    
    
    
    public GuiController setParent (BasicGameState parent) {
        this.parent = parent;
        return this;
    }
    
    public GuiController setVisible (boolean visible) {
        this.visible = visible;
        return this;
    }
    
    public GuiController setDragNDropEnabled (boolean enabled) {
        this.drag_n_drop_enabled = enabled;
        return this;
    }
    
    public GuiController setTooltipDisplayDelay (int delay_ms) {
        this.tooltip_display_delay_ms = delay_ms;
        return this;
    }
    
    public GuiController setTooltipMaxWidth (int max_width) {
        this.tooltip_max_width = max_width;
        return this;
    }
    
    public GuiController setTooltipBackground (String image_name, float r, float g, float b, float a) {
        this.tooltip_background_image = image_name;
        this.tooltip_background_color = new Color (r,g,b,a);
        return this;
    }
    
    public GuiController setTooltipFont (String font, int font_size, float r, float g, float b, float a) {
        this.tooltip_font_color = new Color (r,g,b,a);
        this.tooltip_font = ResourceManager.getFont(font, font_size);
        return this;
    }
    
    public GuiController setTooltipMargins (float x_margin, float y_margin) {
        this.tooltip_x_margin = x_margin;
        this.tooltip_y_margin = y_margin;
        return this;
    }
    
    public GuiController setDraggedElement (final GuiElement element) {
        this.drag_n_drop_element = element;
        return this;
    }
    
    public GuiController setDragNDropAlpha (float alpha) {
        this.drag_n_drop_element_alpha = alpha;
        return this;
    }
    
    public GuiController setDragNDropActivateTime (int time_ms) {
        this.drag_n_drop_activate_time = time_ms;
        return this;
    }
    
    public boolean isVisible () {
        return visible;
    }
    
    public boolean isDragNDropEnabled () {
        return drag_n_drop_enabled;
    }
    
    public float getMouseX () {
        return mouse_x;
    }
    
    public float getMouseY () {
        return mouse_y;
    }
    
    public int getTooltipDisplayDelay () {
        return tooltip_display_delay_ms;
    }
    
    public Color getTooltipBackgroundColor () {
        return tooltip_background_color; // asd
    }
    
    public String getTooltipBackgroundImage () {
        return tooltip_background_image;
    }
    
    public TrueTypeFont getTooltipFont () {
        return tooltip_font;
    }
    
    public Color getTooltipFontColor () {
        return tooltip_font_color;
    }
    
    public float getTooltipXMargin () {
        return tooltip_x_margin;
    }
    
    public float getTooltipYMargin () {
        return tooltip_y_margin;
    }
    
    public float getTooltipMaxWidth () {
        return tooltip_max_width;
    }
    
    public int getDragNDropActivateTime () {
        return drag_n_drop_activate_time;
    }
    
    public final GuiElement getDraggedElement () {
        return drag_n_drop_element;
    }
    
    
    
    public void render (Graphics g) {
        if (!visible) return;
        
        List<GuiElement> el_list = new ArrayList<> (elements.values());
        Collections.sort(el_list, (GuiElement el1, GuiElement el2) -> el1.layer - el2.layer);
        
        // render elements ordered by layer
        for (int i=0;i<el_list.size();i++) {
            el_list.get(i).render(g,0f,0f);
        }
        
        // after all elements are rendered, render tooltips
        el_list.stream().filter( (el) -> el.showTooltip() ).forEach( (el) -> {
            String[] lines = el.getTooltip().split("\n");
            int entire_width = 0;
            int entire_height = 0;
            
            for (String line : lines) {
                entire_width = Math.max(tooltip_font.getWidth(line), entire_width);
                entire_height += tooltip_font.getHeight(line);
            }
            
            float tooltip_width = entire_width;
            float tooltip_height = entire_height;
            float tooltip_text_x_margin = this.tooltip_x_margin;
            float tooltip_text_y_margin = this.tooltip_y_margin;
            
            float tooltip_show_x = mouse_x - tooltip_width - tooltip_text_x_margin*2;
            float tooltip_show_y = mouse_y;
            
            if (tooltip_show_x < 0) {
                tooltip_show_x = mouse_x;
            }
            
            if (tooltip_show_y > Settings.screen_height) {
                tooltip_show_y = mouse_y - tooltip_height - tooltip_text_y_margin*2;
            }

            // show tooltip background
            if (ResourceManager.hasGraphics(tooltip_background_image)) {
                ResourceManager.getGraphics(this.tooltip_background_image).draw(tooltip_show_x, tooltip_show_y,
                        tooltip_width + 2*tooltip_text_x_margin, tooltip_height + 2*tooltip_text_y_margin, this.tooltip_background_color);
            }

            // show tooltip contents
            g.setColor(this.tooltip_font_color);
            g.setFont(this.tooltip_font);
            
            float text_x, text_y = tooltip_show_y;
            
            for (int i=0;i<lines.length;i++) {
                String line = lines[i];
                text_x = tooltip_show_x + tooltip_text_x_margin + entire_width/2f - tooltip_font.getWidth(line)/2f;
                g.drawString(line, text_x, text_y);
                text_y += tooltip_font.getHeight(line);
            }
        });
        
        // after tooltips are rendered, if an element is being dragged then show it using the defined alpha
        if (drag_n_drop_enabled) {
            if (drag_n_drop_element!=null) {
                if (ResourceManager.hasGraphics(drag_n_drop_element.getImageName())) {
                    float w = drag_n_drop_element.width;
                    float h = drag_n_drop_element.height;
                    
                    ResourceManager.getGraphics(drag_n_drop_element.getImageName()).draw(mouse_x - w/2f, mouse_y - h/2f, w, h, new Color (1f,1f,1f,drag_n_drop_element_alpha) );
                }
            }
        }
    }
    
    
    
    public void update (GameContainer gc, StateBasedGame sbg, int dt) {
        update(gc,sbg,dt,0,0);
    }
    
    public void update (GameContainer gc, StateBasedGame sbg, int dt, int x_offset, int y_offset) {
        if (!visible) return;
        
        mouse_x = gc.getInput().getMouseX() + x_offset;
        mouse_y = gc.getInput().getMouseY() + y_offset;
        
        boolean lmb_down = gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        boolean lmb = gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) && !click_lock;
        boolean rmb_down = gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON);
        boolean rmb = gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON) && !click_lock;
        
        List<GuiElement> el_list = new ArrayList<> (elements.values());
        Collections.sort(el_list, (GuiElement el1, GuiElement el2) -> el2.layer - el1.layer);
        boolean clicked = false;
        for (int i=0;i<el_list.size();i++) {
            GuiElement el = el_list.get(i);
            if (el.isVisible()) {
                boolean update = el.update(mouse_x, mouse_y, lmb && !clicked, lmb_down, rmb && !clicked, rmb_down, dt);
                clicked = clicked || update;
            }
        }
        
        if (click_lock) {
            time_since_last_click += dt;
            if (time_since_last_click >= CLICK_LOCK_DURATION) {
                time_since_last_click = 0;
                click_lock = false;
            }
        } else if (lmb) {
            click_lock = true;
            time_since_last_click = 0;
        }
        
//        if (drag_n_drop_element!=null && !lmb_down) {
//            List<GuiElement> elements_under_mouse = elements.values().stream()
//                    .filter( (el) -> el.isPointInBounds(mouse_x, mouse_y) )
//                    .collect(Collectors.toList());
//            
//            for (GuiElement el : elements_under_mouse) {
//                el.onDragDropOnto();
//            }
//            
//            drag_n_drop_element = null;
//        }
    }
    
    public void callForElements (String element_name_containing, String instant_call) {
        List<GuiElement> elements = getElements(element_name_containing);
        for (int i=0;i<elements.size();i++) {
            elements.get(i).instantCall(instant_call);
        }
    }
    
}
