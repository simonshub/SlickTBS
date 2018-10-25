/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.simon.utils.Log;

import main.ResourceManager;
import main.Settings;

/**
 *
 * @author emil.simon
 */
public final class GuiElement {
    
    private final GuiController parent;
    private final String name;
    
    private Color color_filter;
    
    private List<Method> on_click;
    private List<Method> on_hover;
    private List<Method> on_unhover;
    private List<Method> on_update;
    private List<Method> while_hovered;
    private List<Method> while_unhovered;
    
    private List<Method> while_dragged;
    private List<Method> on_drag_pickup;
    private List<Method> on_drag_drop_this;
    private List<Method> on_drag_drop_onto;
    
    private String text;
    private Function<GuiElement,String> tooltip;
    private Color text_color;
    private Color tooltip_color;
    private TrueTypeFont font;
    private TrueTypeFont tooltip_font;
    private Image graphics_cache;
    private Function<GuiElement,String> graphics;
    private final Map<String,Object> properties;
    
    private boolean visible=true;
    private boolean is_mouse_over=false;
    private boolean render_image_tiled=false;
    private boolean render_image_scaled=true;
    private boolean is_draggable=false;
    
    public int layer;
    public float width, height;
    public float base_width, base_height;
    public float display_x, display_y;
    public float base_x, base_y;
    public float scale;
    
    private int tooltip_counter = 0;
    private int drag_n_drop_counter = 0;
    private boolean show_tooltip = false;
    
    
    
    public GuiElement (String name, GuiController parent,
                        boolean precentile_loc, float x, float y,
                        boolean precentile_size, float width, float height,
                        String graphics) {
        this(name, parent, precentile_loc, x, y, precentile_size, width, height,
                (GuiElement el) -> {
                    return graphics;
                });
    }
    
    public GuiElement (String name, GuiController parent, boolean precentile_loc, float x, float y, boolean precentile_size, float width, float height) {
        this (name, parent, precentile_loc, x, y, precentile_size, width, height, "");
    }
    
    public GuiElement (String name, GuiController parent,
                        boolean precentile_loc, float x, float y,
                        boolean precentile_size, float width, float height,
                        Function<GuiElement,String> graphics) {
        
        if (precentile_loc) {
            x *= Settings.screen_width;
            y *= Settings.screen_height;
        }
        if (precentile_size) {
            width *= Settings.screen_width;
            height *= Settings.screen_height;
        }
        
        this.name = name;
        this.parent = parent;
        this.properties = new HashMap<> ();
        this.color_filter = new Color (1f,1f,1f,1f);
        
        this.graphics = graphics;
        this.graphics_cache = ResourceManager.getGraphics(graphics.apply(this));
        
        this.text = "";
        this.text_color = Color.white;
        this.font = ResourceManager.getFont(Settings.default_font, Settings.default_font_size);
        
        this.tooltip = (el) -> "";
        this.tooltip_color = Color.white;
        this.tooltip_font = ResourceManager.getFont(Settings.default_tooltip_font, Settings.default_tooltip_font_size);
        
        this.scale = 1.0f;
        this.base_width = width;
        this.base_height = height;
        this.width = width;
        this.height = height;
        this.base_x = x;
        this.base_y = y;
        this.display_x = x;
        this.display_y = y;
        
        this.on_click = new ArrayList<> ();
        this.on_hover = new ArrayList<> ();
        this.on_unhover = new ArrayList<> ();
        this.on_update = new ArrayList<> ();
        this.while_hovered = new ArrayList<> ();
        this.while_unhovered = new ArrayList<> ();
        
        this.on_drag_drop_this = new ArrayList<> ();
        this.on_drag_drop_onto = new ArrayList<> ();
        this.on_drag_pickup = new ArrayList<> ();
        this.while_dragged = new ArrayList<> ();
    }
    
    
    
    public boolean isVisible () {
        return this.visible;
    }
    
    public GuiController getParent () {
        return this.parent;
    }
    
    public String getName () {
        return this.name;
    }
    
    public Image getImage () {
        if (graphics_cache!=null)
            return graphics_cache;
        
        String graphics_name = this.graphics.apply(this);
        
        if (ResourceManager.hasGraphics(graphics_name)) {
            this.graphics_cache = ResourceManager.getGraphics(graphics_name);
            return graphics_cache;
        }
        
        return null;
    }
    
    public String getImageName () {
        return this.graphics.apply(this);
    }
    
    
    
    public GuiElement setProperty (String key, Object value) {
        properties.put(key, value);
        return this;
    }
    
    public Object getProperty (String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        
        Log.err("Unknown property '"+key+"' of element '"+name+"'");
        return null;
    }
    
    public boolean hasProperty (String key) {
        return properties.containsKey(key);
    }
    
    public void removeProperty (String key) {
        if (hasProperty(key))
            properties.remove(key);
    }
    
    
    
    public String getText () {
        return text;
    }
    
    public GuiElement setText (String text) {
        this.text = text;
        return this;
    }
    
    public String getTooltip () {
        return tooltip.apply(this);
    }
    
    public GuiElement setTooltip (String tooltip) {
        this.tooltip = (GuiElement el) -> {
            return tooltip;
        };
        return this;
    }
    
    public GuiElement setTooltip (Function<GuiElement,String> tooltip) {
        this.tooltip = tooltip;
        return this;
    }
    
    public GuiElement setSize (float width, float height) {
        this.base_width = width;
        this.base_height = height;
        this.width = width;
        this.height = height;
        return this;
    }
    
    public GuiElement setColor (float r, float g, float b, float a) {
        this.color_filter = new Color (r,g,b,a);
        return this;
    }
    
    public GuiElement setColor (Color col) {
        this.color_filter = col;
        return this;
    }
    
    public Color getColor () {
        return this.color_filter;
    }
    
    public Color getTextColor () {
        return this.text_color;
    }
    
    public GuiElement setTextColor (float r, float g, float b, float a) {
        this.text_color = new Color (r,g,b,a);
        return this;
    }
    
    public GuiElement setTextColor (Color color) {
        this.text_color = color;
        return this;
    }
    
    public Color getTooltipColor () {
        return tooltip_color;
    }
    
    public GuiElement setTooltipColor (float r, float g, float b, float a) {
        this.tooltip_color = new Color (r,g,b,a);
        return this;
    }
    
    public GuiElement setTooltipColor (Color color) {
        this.tooltip_color = color;
        return this;
    }
    
    public TrueTypeFont getFont () {
        return font;
    }
    
    public GuiElement setFont (String font_name, int size) {
        this.font = ResourceManager.getFont(font_name, size);
        return this;
    }
    
    public TrueTypeFont getTooltipFont () {
        return tooltip_font;
    }
    
    public GuiElement setTooltipFont (String font_name, int size) {
        this.tooltip_font = ResourceManager.getFont(font_name, size);
        return this;
    }
    
    public GuiElement setLayer (int layer) {
        this.layer = layer;
        return this;
    }
    
    public GuiElement setVisible (boolean visible) {
        this.visible = visible;
        return this;
    }
    
    public GuiElement isDraggable (boolean enabled) {
        this.is_draggable = enabled;
        return this;
    }
    
    public GuiElement setImage (String img) {
        this.graphics = (GuiElement el) -> {
            return img;
        };
        
        return this;
    }
    
    public GuiElement setImage (Function<GuiElement,String> img) {
        this.graphics = img;
        return this;
    }
    
    public float getCenterX () {
        float c_x = this.display_x + width/2f;
        return c_x;
    }
    
    public float getCenterY () {
        float c_x = this.display_y + height/2f;
        return c_x;
    }
    
    public GuiElement setRenderTiled (boolean render_image_tiled) {
        this.render_image_tiled = render_image_tiled;
        this.render_image_scaled = !render_image_tiled;
        return this;
    }
    
    public GuiElement setRenderScaled (boolean render_image_scaled) {
        this.render_image_scaled = render_image_scaled;
        this.render_image_tiled = !render_image_scaled;
        return this;
    }
    
    public boolean showTooltip () {
        return show_tooltip && !tooltip.apply(this).isEmpty() && tooltip_font!=null;
    }
    
    public boolean isPointInBounds (float x, float y) {
        return x < display_x+width && x > display_x && y < display_y+height && y > display_y;
    }
    
    
    
    public GuiElement setOnClick (String on_click_str) {
        if (on_click_str.isEmpty()) {
            this.on_click = new ArrayList<> ();
            return this;
        }
        
        String[] on_clicks = on_click_str.split("&");
        
        for (String on_click : on_clicks) {
            try {
                if (!on_click.isEmpty()) {
                    this.on_click.add(GuiActionHandler.class.getDeclaredMethod(on_click, this.getClass()));
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnHover (String on_hover_str) {
        String[] on_hovers = on_hover_str.split("&");
        
        for (String on_hover : on_hovers) {
            try {
                if (!on_hover.isEmpty()) this.on_hover.add(GuiActionHandler.class.getDeclaredMethod(on_hover, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnUnhover (String on_unhover_str) {
        String[] on_unhovers = on_unhover_str.split("&");
        
        for (String on_unhover : on_unhovers) {
            try {
                if (!on_unhover.isEmpty()) this.on_unhover.add(GuiActionHandler.class.getDeclaredMethod(on_unhover, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setWhileHovered (String while_hover_str) {
        String[] while_hovers = while_hover_str.split("&");
        
        for (String while_hover : while_hovers) {
            try {
                if (!while_hover.isEmpty()) this.while_hovered.add(GuiActionHandler.class.getDeclaredMethod(while_hover, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setWhileUnhovered (String while_unhover_str) {
        String[] while_unhovers = while_unhover_str.split("&");
        
        for (String while_unhover : while_unhovers) {
            try {
                if (!while_unhover.isEmpty()) this.while_unhovered.add(GuiActionHandler.class.getDeclaredMethod(while_unhover, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnUpdate (String on_update_str) {
        String[] on_updates = on_update_str.split("&");
        this.on_update = new ArrayList<> ();
        
        for (String on_update : on_updates) {
            try {
                if (!on_update.isEmpty()) this.on_update.add(GuiActionHandler.class.getDeclaredMethod(on_update, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnDragDropThis (String on_drag_drop_str) {
        String[] on_drag_drops = on_drag_drop_str.split("&");
        this.on_drag_drop_this = new ArrayList<> ();
        
        for (String on_drop : on_drag_drops) {
            try {
                if (!on_drop.isEmpty()) this.on_drag_drop_this.add(GuiActionHandler.class.getDeclaredMethod(on_drop, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnDragDropOnto (String on_drag_drop_str) {
        String[] on_drag_drops = on_drag_drop_str.split("&");
        this.on_drag_drop_onto = new ArrayList<> ();
        
        for (String on_drop : on_drag_drops) {
            try {
                if (!on_drop.isEmpty()) this.on_drag_drop_onto.add(GuiActionHandler.class.getDeclaredMethod(on_drop, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnDragPickup (String on_drag_pickup_str) {
        String[] on_drag_pickups = on_drag_pickup_str.split("&");
        this.on_drag_pickup = new ArrayList<> ();
        
        for (String on_pickup : on_drag_pickups) {
            try {
                if (!on_pickup.isEmpty()) this.on_drag_pickup.add(GuiActionHandler.class.getDeclaredMethod(on_pickup, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setWhileDragged (String while_dragged_str) {
        String[] while_draggeds = while_dragged_str.split("&");
        
        for (String while_drag : while_draggeds) {
            try {
                if (!while_drag.isEmpty()) this.while_unhovered.add(GuiActionHandler.class.getDeclaredMethod(while_drag, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement addOnUpdate (String name) {
        try {
            if (!name.isEmpty()) this.on_update.add(GuiActionHandler.class.getDeclaredMethod(name, this.getClass(), float.class));
        } catch (NoSuchMethodException | SecurityException ex) {
            Log.err(ex);
        }
        return this;
    }
    
    public void removeOnUpdate (String name) {
        for (int i=0;i<on_update.size();i++) {
            if (on_update.get(i).getName().equals(name))
                on_update.remove(i);
        }
    }
    
    public boolean hasOnUpdate (String name) {
        for (int i=0;i<on_update.size();i++) {
            Method update = on_update.get(i);
            if (name.equals(update.getName()))
                return true;
        }
        return false;
    }
    
    
    
    private void onClick () {
        try {
            if (this.on_click != null)
                for (int i=0;i<this.on_click.size();i++)
                    this.on_click.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onHover () {
        try {
            if (this.on_hover != null)
                for (int i=0;i<this.on_hover.size();i++)
                    this.on_hover.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onUnhover () {
        try {
            if (this.on_unhover != null)
                for (int i=0;i<this.on_unhover.size();i++)
                    this.on_unhover.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onUpdate (int dt) {
        try {
            if (this.on_update != null)
                for (int i=0;i<this.on_update.size();i++)
                    this.on_update.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    public void onDragDropThis () {
        try {
            if (this.on_drag_drop_this != null)
                for (int i=0;i<this.on_drag_drop_this.size();i++)
                    this.on_drag_drop_this.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    public void onDragDropOnto () {
        try {
            if (this.on_drag_drop_onto != null)
                for (int i=0;i<this.on_drag_drop_onto.size();i++)
                    this.on_drag_drop_onto.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    public void onDragPickup () {
        try {
            if (this.on_drag_pickup != null)
                for (int i=0;i<this.on_drag_pickup.size();i++)
                    this.on_drag_pickup.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void whileDragged (int dt) {
        try {
            if (this.while_dragged != null)
                for (int i=0;i<this.while_dragged.size();i++)
                    this.while_dragged.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void whileHovered (int dt) {
        try {
            if (this.while_hovered != null)
                for (int i=0;i<this.while_hovered.size();i++)
                    this.while_hovered.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void whileUnhovered (int dt) {
        try {
            if (this.while_unhovered != null)
                for (int i=0;i<this.while_unhovered.size();i++)
                    this.while_unhovered.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    public GuiElement instantCall (String callstring) {
        String[] instant_calls = callstring.split("&");
        
        for (String instant_call : instant_calls) {
            try {
                if (!instant_call.isEmpty()) {
                    Method method = GuiActionHandler.class.getDeclaredMethod(instant_call, this.getClass());
                    method.invoke(null, this);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    
    
    public void refreshCache () {
        graphics_cache = ResourceManager.getGraphics(graphics.apply(this));
    }
    
    
    
    public void render (Graphics g, float x_offset, float y_offset) {
        if (!this.visible) return;
        
        if (Settings.debug_gui && Settings.debug_mode) {
            g.setColor(color_filter);
            g.setLineWidth(3f);
            g.drawRect(display_x+x_offset, display_y+y_offset, width, height);
        }
        
        if (graphics_cache==null && ResourceManager.hasGraphics(graphics.apply(this))) {
            graphics_cache = ResourceManager.getGraphics(graphics.apply(this));
        }
        
        if (graphics_cache!=null) {
            if (!render_image_tiled && render_image_scaled) {
                graphics_cache.draw(display_x+x_offset, display_y+y_offset, width, height, color_filter);
            } else if (render_image_tiled) {
                float x_scale = width / graphics_cache.getWidth();
                float y_scale = height / graphics_cache.getHeight();
                g.texture(new Rectangle (display_x+x_offset, display_y+y_offset, width, height), graphics_cache, x_scale, y_scale, true);
            } else if (!render_image_scaled) {
                float centered_x = display_x + width/2f - graphics_cache.getWidth()/2f;
                float centered_y = display_y + height/2f - graphics_cache.getHeight()/2f;
                graphics_cache.draw(centered_x+x_offset, centered_y+y_offset, color_filter);
            }
        }
        
        if (!text.isEmpty()) {
            // TEXT DOESN'T SCALE !!! - it uses font size
            String[] lines = text.split("\n");
            int entire_width = 0;
            int entire_height = 0;
            
            for (String line : lines) {
                entire_width = Math.max(font.getWidth(line), entire_width);
                entire_height += font.getHeight(line);
            }
            
            int top_x = (int)(display_x+x_offset + width/2f - entire_width/2f);
            int top_y = (int)(display_y+y_offset + height/2f - entire_height/2f);
            
            g.setFont(font);
            g.setColor(text_color);
            int text_x, text_y = top_y;
            for (int i=0;i<lines.length;i++) {
                String line = lines[i];
                text_x = (int)(top_x + entire_width/2f - font.getWidth(line)/2f);
                g.drawString(line, text_x, text_y);
                text_y += font.getHeight(line);
            }
        }
    }
    
    
    
    public boolean update (float mouse_x, float mouse_y, boolean lmb_click, boolean lmb_down, boolean rmb_click, boolean rmb_down, int dt) {
        boolean was_clicked = false;
        
        if (is_mouse_over) {
            this.whileHovered(dt);
            
            tooltip_counter += dt;
            if (tooltip_counter > parent.getTooltipDisplayDelay() && parent.getDraggedElement()==null && tooltip!=null && !tooltip.apply(this).isEmpty()) {
                show_tooltip = true;
            }
            
            if (parent.isDragNDropEnabled() && this.is_draggable) {
                if (lmb_down) {
                    drag_n_drop_counter += dt;
                    
                    if (drag_n_drop_counter > parent.getDragNDropActivateTime() && parent.getDraggedElement()==null) {
                        if (main.Settings.debug_gui)
                            Log.log("Picked up element '"+this.getName()+"'.");
                        
                        show_tooltip = false;
                        parent.setDraggedElement(this);
                        
                        this.onDragPickup();
                        tooltip_counter = 0;
                    }
                } else {
                    drag_n_drop_counter = 0;
                }
            } else if (parent.isDragNDropEnabled()) {
                if (!lmb_down && parent.getDraggedElement()!=null) {
                    if (Settings.debug_gui)
                        Log.log("Dragged element '"+parent.getDraggedElement().getName()+"' dropped onto '"+this.getName()+"'.");
                    
                    parent.getDraggedElement().onDragDropThis();
                    this.onDragDropOnto();
                    
                    parent.setDraggedElement(null);
                    tooltip_counter = 0;
                }
                drag_n_drop_counter = 0;
            }
        } else {
            this.whileUnhovered(dt);
            tooltip_counter = 0;
            drag_n_drop_counter = 0;
            show_tooltip = false;
        }
        
        if (on_update!=null && !on_update.isEmpty()) {
            this.onUpdate(dt);
        }
        
        if (isPointInBounds(mouse_x, mouse_y)) {
            if (lmb_click) {
                this.onClick();
                was_clicked = true;
            }
            
            if (!is_mouse_over) {
                this.onHover();
            }
            
            is_mouse_over = true;
        } else {
            if (is_mouse_over) {
                this.onUnhover();
            }
            
            is_mouse_over = false;
        }
        
        if (parent.getDraggedElement() == this) {
            this.whileDragged(dt);
        }
        
        return was_clicked;
    }
    
}
