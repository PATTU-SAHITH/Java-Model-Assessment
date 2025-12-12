import java.util.*;

public class Q7Tree {
    static class Node { int id; int parent; String name; List<Integer> children = new ArrayList<>(); boolean isFile = false; }

    List<Node> nodes = new ArrayList<>();
    Map<String, Integer> pathToId = new HashMap<>();
    int[][] up;
    int LOG;

    public Q7Tree() {
        Node root = new Node(); root.id = 0; root.parent = -1; root.name = ""; nodes.add(root); pathToId.put("/", 0);
    }

    public int addPath(String fullPath) {
        if (fullPath.equals("/")) return 0;
        if (pathToId.containsKey(fullPath)) return pathToId.get(fullPath);
        String[] parts = fullPath.split("/");
        int cur = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            sb.append('/').append(part);
            String p = sb.toString();
            if (pathToId.containsKey(p)) { cur = pathToId.get(p); continue; }
            Node n = new Node(); n.id = nodes.size(); n.parent = cur; n.name = part; nodes.add(n); nodes.get(cur).children.add(n.id); pathToId.put(p, n.id); cur = n.id;
        }
        Node last = nodes.get(cur);
        if (parts[parts.length-1].contains(".")) last.isFile = true;
        return cur;
    }

    public void prepareLCA() {
        int n = nodes.size();
        LOG = 1; while ((1<<LOG) <= n) LOG++;
        up = new int[LOG][n];
        for (int v = 0; v < n; v++) up[0][v] = Math.max(0, nodes.get(v).parent);
        for (int k = 1; k < LOG; k++) for (int v = 0; v < n; v++) up[k][v] = up[k-1][up[k-1][v]];
    }

    int depth(int v) { int d = 0; while (v != 0) { v = nodes.get(v).parent; d++; } return d; }

    public int lca(int a, int b) {
        if (a == b) return a;
        int da = depth(a), db = depth(b);
        if (da < db) { int t = a; a = b; b = t; int td = da; da = db; db = td; }
        int diff = da - db;
        for (int k = 0; k < LOG; k++) if (((diff>>k)&1) == 1) a = up[k][a];
        if (a == b) return a;
        for (int k = LOG-1; k >= 0; k--) if (up[k][a] != up[k][b]) { a = up[k][a]; b = up[k][b]; }
        return nodes.get(a).parent;
    }

    public String nodeFullPath(int v) {
        if (v == 0) return "/";
        List<String> parts = new ArrayList<>();
        while (v != 0) { parts.add(nodes.get(v).name); v = nodes.get(v).parent; }
        Collections.reverse(parts);
        StringBuilder sb = new StringBuilder(); for (String p : parts) { sb.append('/').append(p); } return sb.toString();
    }

    public String lowestCommonDirectory(String p1, String p2) {
        int n1 = addPath(p1); int n2 = addPath(p2);
        if (nodes.get(n1).isFile) n1 = nodes.get(n1).parent; if (nodes.get(n2).isFile) n2 = nodes.get(n2).parent;
        prepareLCA();
        int anc = lca(n1, n2);
        return nodeFullPath(anc);
    }

    public static void main(String[] args) {
        Q7Tree fs = new Q7Tree();
        String[] paths = {
            "/usr/local/bin/script.sh",
            "/usr/local/lib/libX.so",
            "/usr/share/doc/readme.txt",
            "/home/alice/docs/resume.pdf",
            "/home/alice/photos/vacation.jpg",
            "/home/bob/docs/todo.txt"
        };
        for (String p : paths) fs.addPath(p);
        fs.prepareLCA();
        System.out.println(fs.lowestCommonDirectory("/usr/local/bin/script.sh", "/usr/local/lib/libX.so"));
        System.out.println(fs.lowestCommonDirectory("/home/alice/docs/resume.pdf", "/home/alice/photos/vacation.jpg"));
        System.out.println(fs.lowestCommonDirectory("/home/alice/docs/resume.pdf", "/usr/share/doc/readme.txt"));
        System.out.println(fs.lowestCommonDirectory("/home/alice/docs/resume.pdf", "/home/bob/docs/todo.txt"));
    }
}
