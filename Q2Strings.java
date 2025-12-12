/*
2.  STRINGS - Gmail Autocorrect Suggestion Engine 
Problem: 
Given a typed incorrect word and a dictionary of valid words, 
find the closest valid suggestions using minimum edit distance. 
Requirements: 
- Dictionary size can be up to 100,000 words → must optimize. 
- Return the top 3 closest words, not just one. 
- Ties: return lexicographically smallest. 
- Total computation must be < 100 ms per query. 
*/
import java.util.*;

public class Q2Strings {
    public static int levenshtein(String a, String b) {
        a = a == null ? "" : a;
        b = b == null ? "" : b;
        int n = a.length();
        int m = b.length();
        int[] prev = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            int[] cur = new int[m + 1];
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                cur[j] = Math.min(Math.min(prev[j] + 1, cur[j - 1] + 1), prev[j - 1] + cost);
            }
            prev = cur;
        }
        return prev[m];
    }

    public static List<String> suggest(String query, List<String> dict, int k) {
        String q = query.toLowerCase();
        List<String> copy = new ArrayList<>(dict);
        copy.sort(Comparator.naturalOrder());
        List<String> sorted = new ArrayList<>(copy);
        sorted.sort(Comparator.comparingInt((String w) -> levenshtein(q, w.toLowerCase()))
                .thenComparing(Comparator.naturalOrder()));
        return sorted.subList(0, Math.min(k, sorted.size()));
    }

    public static void main(String[] args) {
        System.out.println("2.  STRINGS - Gmail Autocorrect Suggestion Engine ");
        List<String> dict = Arrays.asList(
                "example","sample","simple","gmail","auto","autocorrect","correct","testing","apple","orange",
                "banana","triangle","rectangle","examine","exemplar","exempt","temple","ripple","pineapple","ample"
        );

        String[] queries = {"exampel", "aplpe", "gamil"};
        for (String q : queries) {
            List<String> s = suggest(q, dict, 3);
            System.out.println("Query: " + q + " -> " + s);
        }
    }
}
