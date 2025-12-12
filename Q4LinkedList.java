// LINKED LIST - Music Playlist Engine (Spotify-like) 
// Problem: 
// Build a playlist system with: 
//  playNext() 
//  playPrev() 
//  addSong() 
//  removeSong() 
//  prevent duplicate songs 
// Requirements: 
//  All operations must run in O(1) using doubly linked list + HashSet. 
//  Maintain a current pointer that moves dynamically. 
//  Support exporting playlist both forward and backward.

import java.util.*;

public class Q4LinkedList {
    private static class Node {
        String id;
        Node prev;
        Node next;
        Node(String id){ this.id = id; }
    }

    private Node head;
    private Node tail;
    private Node current;
    private final Set<String> set = new HashSet<>();
    private final Map<String, Node> map = new HashMap<>();

    public boolean addSong(String id){
        if(id == null) return false;
        if(set.contains(id)) return false;
        Node n = new Node(id);
        if(head == null){
            head = tail = current = n;
        } else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
        set.add(id);
        map.put(id, n);
        return true;
    }

    public boolean removeSong(String id){
        if(id == null) return false;
        if(!set.contains(id)) return false;
        Node n = map.get(id);
        if(n.prev != null) n.prev.next = n.next; else head = n.next;
        if(n.next != null) n.next.prev = n.prev; else tail = n.prev;
        if(current == n){
            if(n.next != null) current = n.next;
            else current = n.prev;
        }
        n.prev = n.next = null;
        set.remove(id);
        map.remove(id);
        if(head == null) current = null;
        return true;
    }

    public String playNext(){
        if(current == null) return null;
        if(current.next == null) return null;
        current = current.next;
        return current.id;
    }

    public String playPrev(){
        if(current == null) return null;
        if(current.prev == null) return null;
        current = current.prev;
        return current.id;
    }

    public List<String> exportForward(){
        List<String> out = new ArrayList<>();
        Node p = head;
        while(p != null){
            out.add(p.id);
            p = p.next;
        }
        return out;
    }

    public List<String> exportBackward(){
        List<String> out = new ArrayList<>();
        Node p = tail;
        while(p != null){
            out.add(p.id);
            p = p.prev;
        }
        return out;
    }

    public String getCurrent(){
        return current == null ? null : current.id;
    }

    public int size(){
        return set.size();
    }

    public static void main(String[] args){
        Q4LinkedList p = new Q4LinkedList();
        p.addSong("s1");
        p.addSong("s2");
        p.addSong("s3");
        System.out.println(p.getCurrent());
        System.out.println(p.playNext());
        System.out.println(p.playNext());
        System.out.println(p.playNext());
        p.playPrev();
        System.out.println(p.getCurrent());
        p.removeSong("s2");
        System.out.println(p.exportForward());
        System.out.println(p.exportBackward());
    }
}
