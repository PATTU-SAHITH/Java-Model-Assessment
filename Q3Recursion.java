import java.util.*;

public class Q3Recursion {
    public static class Node {
        String name;
        boolean file;
        long size;
        List<Node> children;
        Node link;
        public Node(String n,long s){name=n;file=true;size=s;}
        public Node(String n,List<Node> c){name=n;children=c;}
        public Node(String n,Node l){name=n;link=l;}
    }

    public static class FileEntry{String path; long size; FileEntry(String p,long s){path=p;size=s;} public String toString(){return path+"("+size+")";}}
    public static class Result{long total; List<FileEntry> top; Result(long t,List<FileEntry>l){total=t;top=l;}}

    public Result compute(Node root){
        Map<Node,Long> memo=new IdentityHashMap<>();
        Set<Node> vis=Collections.newSetFromMap(new IdentityHashMap<>());
        Set<Node> seenFiles=Collections.newSetFromMap(new IdentityHashMap<>());
        PriorityQueue<FileEntry> pq=new PriorityQueue<>(Comparator.comparingLong(a->a.size));
        long total=dfs(root,"/",memo,vis,seenFiles,pq);
        List<FileEntry> top=new ArrayList<>(pq); top.sort((a,b)->Long.compare(b.size,a.size));
        return new Result(total,top);
    }

    private long dfs(Node n,String path,Map<Node,Long> memo,Set<Node> vis,Set<Node> seen,PriorityQueue<FileEntry> pq){
        if(n==null) return 0;
        if(n.link!=null) return dfs(n.link,path,memo,vis,seen,pq);
        if(vis.contains(n)) return 0;
        if(memo.containsKey(n)) return memo.get(n);
        vis.add(n);
        long sum=0;
        if(n.file){
            sum=n.size;
            if(!seen.contains(n)){ seen.add(n); String fp = path.equals("/")?"/"+n.name: path+n.name; addTop(pq,new FileEntry(fp,n.size)); }
        } else if(n.children!=null){
            String base = path.equals("/")?"/":path+n.name+"/";
            if(path.equals("/")) base = "/"+n.name+"/";
            for(Node c: n.children){
                String childPath = n==null?"/": (n==null?"/": (path.equals("/")?"/": path+n.name+"/"));
                if(path.equals("/")) childPath="/";
                sum += dfs(c, childPath, memo, vis, seen, pq);
            }
        }
        vis.remove(n);
        memo.put(n,sum);
        return sum;
    }

    private void addTop(PriorityQueue<FileEntry> pq, FileEntry f){
        if(pq.size()<3){ pq.add(f); return; }
        if(f.size>pq.peek().size){ pq.poll(); pq.add(f); }
    }

    public static void main(String[] args){
        Node a=new Node("a.txt",100);
        Node b=new Node("b.log",500);
        Node c=new Node("c.bin",200);
        Node d=new Node("d.mp4",1200);
        Node e=new Node("e.dat",700);

        Node s1=new Node("sub1",Arrays.asList(a,b));
        Node s2=new Node("sub2",Arrays.asList(c));
        Node s3=new Node("sub3",Arrays.asList(d));

        Node l1=new Node("linkTo3",s3);
        Node l2=new Node("linkTo2",s2);

        Node s2b=new Node("sub2",Arrays.asList(c,l1));
        Node s3b=new Node("sub3",Arrays.asList(d,l2));

        Node root=new Node("/",Arrays.asList(s1,s2b,s3b,e));

        Q3Recursion fs=new Q3Recursion();
        Result r=fs.compute(root);
        System.out.println(r.total);
        r.top.forEach(System.out::println);
    }
}
