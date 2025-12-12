/*
8. HEAP - YouTube Real-Time Trending Videos
Problem:
Each video has a dynamic "trending score" updated in real-time.
Return top K trending videos.
Requirements:
- updateScore(videoID, score) must run in O(log n).
- Fetch top-K in O(k log n).
- Videos not watched for 24 hours → lose 10% score automatically.
- If two videos have same score → newest timestamp is higher priority. 
*/
import java.util.*;
public class Q8Heap {

    public static class Video {
        int videoID;
        double score;
        long timestamp; 
        
        Video(int videoID, double score, long timestamp) {
            this.videoID = videoID;
            this.score = score;
            this.timestamp = timestamp;
        }
    }

    public static class TrendingVideos {
        private Map<Integer, Video> videoMap;
        private PriorityQueue<Video> maxHeap;

        public TrendingVideos() {
            videoMap = new HashMap<>();
            maxHeap = new PriorityQueue<>((a, b) -> {
                if (b.score != a.score) {
                    return Double.compare(b.score, a.score); 
                } else {
                    return Long.compare(b.timestamp, a.timestamp); 
                }
            });
        }

        public void updateScore(int videoID, double score) {
            long now = System.currentTimeMillis();
            if (videoMap.containsKey(videoID)) {
                Video old = videoMap.get(videoID);
                maxHeap.remove(old); 
                old.score = score;
                old.timestamp = now;
                maxHeap.offer(old);
            } else {
                Video video = new Video(videoID, score, now);
                videoMap.put(videoID, video);
                maxHeap.offer(video);
            }
        }

        public void applyDecay() {
            long now = System.currentTimeMillis();
            List<Video> temp = new ArrayList<>();
            while (!maxHeap.isEmpty()) {
                Video video = maxHeap.poll();
                if (now - video.timestamp > 24 * 60 * 60 * 1000L) { 
                    video.score *= 0.9;
                }
                temp.add(video);
            }
            maxHeap.addAll(temp);
        }

        public List<Video> getTopK(int k) {
            List<Video> topK = new ArrayList<>();
            PriorityQueue<Video> copy = new PriorityQueue<>(maxHeap);
            for (int i = 0; i < k && !copy.isEmpty(); i++) {
                topK.add(copy.poll());
            }
            return topK;
        }

        public static void main(String[] args) {
            TrendingVideos trending = new TrendingVideos();
            
            trending.updateScore(1, 50);
            trending.updateScore(2, 60);
            trending.updateScore(3, 40);

            int topK = 3;
            List<Video> Videos = trending.getTopK(topK);

            System.out.println("Top "+topK+" trending videos:");

            for (Video v : Videos) {
                System.out.println("VideoID: " + v.videoID + ", Score: " + v.score);
            }
            
            trending.applyDecay();
        }
    }
}
