/*
4. LINKED LIST - Music Playlist Engine (Spotify-like)
Problem:
Build a playlist system with:
- playNext()
- playPrev()
- addSong()
- removeSong()
- prevent duplicate songs
Requirements:
- All operations must run in O(1) using doubly linked list + HashSet.
- Maintain a current pointer that moves dynamically.
- Support exporting playlist both forward and backward. 
 */

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
        System.out.println(id + " added to Playlist");
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
        System.out.println("Removed "+ id);
        if(head == null) current = null;
        return true;
    }

    public String playNext(){
        if(current == null) return "No Songs in PLaylist";
        if(current.next == null) return "No Songs in PLaylist";
        current = current.next;
        return current.id;
    }

    public String playPrev(){
        if(current == null) return null;
        if(current.prev == null) return null;
        current = current.prev;
        return current.id;
    }


    public String getCurrent(){
        return current == null ? null : current.id;
    }

    public int size(){
        return set.size();
    }

    public static void main(String[] args){
        Q4LinkedList p = new Q4LinkedList();
        System.out.println("4. LINKED LIST - Music Playlist Engine (Spotify-like) \n");
        p.addSong("Channa Meriya");
        p.addSong("Kesariya Thera");
        p.addSong("Kaise Hua");
        System.out.println(p.getCurrent());
        System.out.println(p.playNext());
        System.out.println(p.playNext());
        System.out.println(p.playNext());
        p.playPrev();
        System.out.println(p.getCurrent());
        p.removeSong("Kaise Hua");
    }
}
