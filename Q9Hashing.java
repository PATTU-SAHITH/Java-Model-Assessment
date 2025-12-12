/*
9. HASHING - Real-Time Fraud Detection System
Problem:
Given a continuous stream of transactions, detect duplicates within the last k
seconds.
Requirements:
- A duplicate = same transaction ID + amount.
- Must process 10 million transactions/day.
- Implement using HashMap + Doubly Linked List sliding window.
- Return:
- Whether fraud occurred
- All suspicious transaction IDs
 */
import java.util.*;

public class Q9Hashing {
    
    static class Transaction {
        String transactionId;
        double amount;
        long timestamp;
        
        public Transaction(String id, double amount, long timestamp) {
            this.transactionId = id;
            this.amount = amount;
            this.timestamp = timestamp;
            System.out.print("Transaction Created : ");
            System.out.println("ID= " +id+ ", Amount= " +amount+ ", Time= "+timestamp);
        }
        
        public String getKey() {
            return transactionId + "_" + amount;
        }
    }
    
    static class TransactionNode {
        Transaction transaction;
        TransactionNode previous;
        TransactionNode next;
        
        public TransactionNode(Transaction transaction) {
            this.transaction = transaction;
        }
    }
    
    static class FraudDetector {
        Map<String, TransactionNode> recentTransactions;
        TransactionNode oldestTransaction;
        TransactionNode newestTransaction;
        int timeWindowSeconds;
        List<String> suspiciousTransactions;
        
        public FraudDetector(int timeWindowSeconds) {
            this.recentTransactions = new HashMap<>();
            this.oldestTransaction = null;
            this.newestTransaction = null;
            this.timeWindowSeconds = timeWindowSeconds;
            this.suspiciousTransactions = new ArrayList<>();
        }
        
        public boolean checkTransaction(Transaction newTransaction) {
            removeExpiredTransactions(newTransaction.timestamp);
            
            String key = newTransaction.getKey();
            boolean isFraud = recentTransactions.containsKey(key);
            
            if (isFraud) {
                suspiciousTransactions.add(newTransaction.transactionId);
            }
            
            addTransactionToWindow(newTransaction);
            
            return isFraud;
        }
        
        private void removeExpiredTransactions(long currentTime) {
            long cutoffTime = currentTime - (timeWindowSeconds * 1000);
            
            while (oldestTransaction != null && 
                   oldestTransaction.transaction.timestamp < cutoffTime) {
                
                String key = oldestTransaction.transaction.getKey();
                recentTransactions.remove(key);
                
                TransactionNode nextNode = oldestTransaction.next;
                
                if (nextNode != null) {
                    nextNode.previous = null;
                }
                
                oldestTransaction = nextNode;
                
                if (oldestTransaction == null) {
                    newestTransaction = null;
                }
            }
        }
        
        private void addTransactionToWindow(Transaction transaction) {
            TransactionNode newNode = new TransactionNode(transaction);
            String key = transaction.getKey();
            
            recentTransactions.put(key, newNode);
            
            if (newestTransaction == null) {
                oldestTransaction = newNode;
                newestTransaction = newNode;
            } else {
                newestTransaction.next = newNode;
                newNode.previous = newestTransaction;
                newestTransaction = newNode;
            }
        }
        
        public List<String> getSuspiciousTransactions() {
            return suspiciousTransactions;
        }
        
        public int getActiveWindowSize() {
            return recentTransactions.size();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("9. HASHING - Real-Time Fraud Detection System\n");
        
        FraudDetector detector = new FraudDetector(5);
        
        long baseTime = System.currentTimeMillis();
        
        Transaction t1 = new Transaction("TXN001", 150.00, baseTime);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t1));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t2 = new Transaction("TXN002", 200.00, baseTime + 1000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t2));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t3 = new Transaction("TXN003", 75.50, baseTime + 2000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t3));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t4 = new Transaction("TXN001", 150.00, baseTime + 3000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t4));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t5 = new Transaction("TXN004", 300.00, baseTime + 4000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t5));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t6 = new Transaction("TXN005", 100.00, baseTime + 6000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t6));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t7 = new Transaction("TXN002", 200.00, baseTime + 7000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t7));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        Transaction t8 = new Transaction("TXN001", 150.00, baseTime + 8000);
        System.out.println("Fraud Detectection: " + detector.checkTransaction(t8));
        System.out.println("Window Size: " + detector.getActiveWindowSize() + "\n");
        
        System.out.println("===========================================");
        System.out.println("              FINAL REPORT");
        System.out.println("===========================================");
        System.out.println("Fraudulent Transactions Detected: " + detector.getSuspiciousTransactions().size());
        System.out.println("\nSuspicious Transaction IDs:");
        for (String id : detector.getSuspiciousTransactions()) {
            System.out.println(id);
        }
        System.out.println("\nCurrent Active Window Size: " + detector.getActiveWindowSize());
    }

}
