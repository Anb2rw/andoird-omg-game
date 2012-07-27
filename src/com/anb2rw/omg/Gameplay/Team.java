/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg.Gameplay;

import com.anb2rw.omg.Gameplay.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author АндрюшкА
 */
public class Team {
    public String Name;
    public int Color;
    public List<Actor> Players;
    public int Score;
    
    public Team(String n, int c) {
        this.Name=n;
        this.Color=c;
        Score=0;
        Players=new ArrayList<Actor>();
    }
}
