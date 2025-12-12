import java.util.*;

public class Q6Queue {
    private static class Patient implements Comparable<Patient> {
        final String id;
        String name;
        int severity;
        long arrival;
        Patient(String id, String name, int severity, long arrival){
            this.id = id;
            this.name = name;
            this.severity = severity;
            this.arrival = arrival;
        }
        public int compareTo(Patient o){
            if(this.severity != o.severity) return Integer.compare(o.severity, this.severity);
            if(this.arrival != o.arrival) return Long.compare(this.arrival, o.arrival);
            return this.id.compareTo(o.id);
        }
        public String toString(){ return id + ":" + name + ":" + severity + ":" + arrival; }
    }

    private final NavigableSet<Patient> critical = new TreeSet<>();
    private final NavigableSet<Patient> normal = new TreeSet<>();
    private final Map<String,Patient> map = new HashMap<>();
    private long lastCriticalTreatedAt = 0;
    private final long CRITICAL_MAX_INTERVAL_MS = 5 * 60 * 1000L;

    public boolean addPatient(String id, String name, int severity, long arrivalMillis){
        if(id == null || name == null) return false;
        if(map.containsKey(id)) return false;
        Patient p = new Patient(id,name,severity,arrivalMillis);
        map.put(id,p);
        if(isCritical(severity)) critical.add(p); else normal.add(p);
        return true;
    }

    public boolean removePatient(String id){
        Patient p = map.remove(id);
        if(p == null) return false;
        if(isCritical(p.severity)) critical.remove(p); else normal.remove(p);
        return true;
    }

    public boolean updateSeverity(String id, int newSeverity){
        Patient p = map.get(id);
        if(p == null) return false;
        if(isCritical(p.severity)) critical.remove(p); else normal.remove(p);
        p.severity = newSeverity;
        if(isCritical(newSeverity)) critical.add(p); else normal.add(p);
        return true;
    }

    public Patient peekNext(){
        long now = System.currentTimeMillis();
        if(!critical.isEmpty()){
            if(now - lastCriticalTreatedAt >= CRITICAL_MAX_INTERVAL_MS) return critical.first();
            return critical.first();
        }
        if(!normal.isEmpty()) return normal.first();
        return null;
    }

    public String treatNext(){
        long now = System.currentTimeMillis();
        Patient chosen = null;
        if(!critical.isEmpty()){
            chosen = critical.pollFirst();
            map.remove(chosen.id);
            lastCriticalTreatedAt = now;
            return chosen.toString();
        }
        if(!normal.isEmpty()){
            chosen = normal.pollFirst();
            map.remove(chosen.id);
            return chosen.toString();
        }
        return null;
    }

    public List<String> listCritical(){
        List<String> r = new ArrayList<>();
        for(Patient p: critical) r.add(p.toString());
        return r;
    }

    public List<String> listNormal(){
        List<String> r = new ArrayList<>();
        for(Patient p: normal) r.add(p.toString());
        return r;
    }

    public int size(){ return map.size(); }

    private boolean isCritical(int severity){ return severity >= 8 && severity <= 10; }

    public static void main(String[] args) throws Exception{
        Q6Queue q = new Q6Queue();
        long now = System.currentTimeMillis();
        q.addPatient("p1","Alice",5, now - 60000);
        q.addPatient("p2","Bob",9, now - 50000);
        q.addPatient("p3","Cara",9, now - 40000);
        q.addPatient("p4","Dan",3, now - 30000);
        System.out.println(q.listCritical());
        System.out.println(q.listNormal());
        System.out.println("peek " + q.peekNext());
        System.out.println("treat " + q.treatNext());
        System.out.println("treat " + q.treatNext());
        q.updateSeverity("p4",8);
        System.out.println(q.listCritical());
        System.out.println(q.listNormal());
    }
}
