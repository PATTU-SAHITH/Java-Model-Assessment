/*
5. STACK - Google Docs Undo/Redo Manager
Problem:
Design the undo/redo engine for Google Docs supporting text editing.
Requirements:
- Handle 10,000+ operations.
- Undo stack can store only latest 50 actions (drop oldest automatically).
- Any new action clears the redo stack.
- Support two actions:
o INSERT(x)
o DELETE(k characters)
- Return:
o final document
o total operations performed
 */

import java.util.*;
public class Q5Stack {
    
    static class DocumentAction {
        String actionType;
        String textAdded;
        int charsRemoved;
        String deletedText;
        
        public DocumentAction(String type, String text, int removed, String deleted) {
            this.actionType = type;
            this.textAdded = text;
            this.charsRemoved = removed;
            this.deletedText = deleted;
        }
    }
    
    static class DocumentEditor {
        StringBuilder document;
        Stack<DocumentAction> undoHistory;
        Stack<DocumentAction> redoHistory;
        int maxHistorySize = 50;
        int totalActions = 0;
        
        public DocumentEditor() {
            document = new StringBuilder();
            undoHistory = new Stack<>();
            redoHistory = new Stack<>();
        }
        
        public void insert(String text) {
            document.append(text);
            
            DocumentAction action = new DocumentAction("INSERT", text, 0, "");
            undoHistory.push(action);
            
            if (undoHistory.size() > maxHistorySize) {
                undoHistory.remove(0);
            }
            
            redoHistory.clear();
            
            totalActions++;
        }
        
        public void delete(int k) {
            if (k > document.length()) {
                k = document.length();
            }
            
            if (k == 0) return;
            
            int startPos = document.length() - k;
            String removedText = document.substring(startPos);
            
            document.delete(startPos, document.length());
            
            DocumentAction action = new DocumentAction("DELETE", "", k, removedText);
            undoHistory.push(action);
            
            if (undoHistory.size() > maxHistorySize) {
                undoHistory.remove(0);
            }
            
            redoHistory.clear();
            
            totalActions++;
        }
        
        public void undo() {
            if (undoHistory.isEmpty()) {
                System.out.println("Nothing to undo!");
                return;
            }
            
            DocumentAction lastAction = undoHistory.pop();
            
            if (lastAction.actionType.equals("INSERT")) {
                int removeLength = lastAction.textAdded.length();
                document.delete(document.length() - removeLength, document.length());
                
            } else if (lastAction.actionType.equals("DELETE")) {
                document.append(lastAction.deletedText);
            }
            
            redoHistory.push(lastAction);
            totalActions++;
        }
        
        public void redo() {
            if (redoHistory.isEmpty()) {
                System.out.println("Nothing to redo!");
                return;
            }
            
            DocumentAction actionToRedo = redoHistory.pop();
            
            if (actionToRedo.actionType.equals("INSERT")) {
                document.append(actionToRedo.textAdded);
                
            } else if (actionToRedo.actionType.equals("DELETE")) {
                int removeLength = actionToRedo.charsRemoved;
                document.delete(document.length() - removeLength, document.length());
            }
            
            undoHistory.push(actionToRedo);
            totalActions++;
        }
        
        public String getDocument() {
            return document.toString();
        }
        
        public int getTotalActions() {
            return totalActions;
        }
        
        public void showStatus() {
            System.out.println("Document: \"" + getDocument() + "\"");
            System.out.println("Undo Stack Size: " + undoHistory.size());
            System.out.println("Redo Stack Size: " + redoHistory.size());
            System.out.println("Total Actions: " + totalActions);
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        DocumentEditor editor = new DocumentEditor();
        
        System.out.println("5. STACK - Google Docs Undo/Redo Manager\n");
        
        editor.insert("Hello");
        editor.insert(" World");
        editor.showStatus();
        
        editor.delete(5);
        editor.showStatus();
        
        editor.insert("!");
        editor.showStatus();
        
        editor.undo();
        editor.showStatus();
        
        editor.undo();
        editor.showStatus();
        
        editor.redo();
        editor.showStatus();
        
        editor.insert(" Everyone");
        editor.showStatus();
        
        editor.redo();
        editor.showStatus();
        
        System.out.println("=== Testing 50+ Action Limit ===\n");
        DocumentEditor bigEditor = new DocumentEditor();
        
        for (int i = 1; i <= 55; i++) {
            bigEditor.insert("A");
        }
        
        System.out.println("After 55 insert actions:");
        System.out.println("Document length: " + bigEditor.getDocument().length());
        System.out.println("Undo stack size (max 50): " + bigEditor.undoHistory.size());
        System.out.println("Can only undo 50 times, oldest 5 actions are lost!");
        System.out.println();
        
        System.out.println("=== Final Result ===");
        System.out.println("First Editor:");
        System.out.println("Final Document: \"" + editor.getDocument() + "\"");
        System.out.println("Total Operations: " + editor.getTotalActions());
    }

}
