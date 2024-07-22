package parser.table;

import java.util.ArrayList;

public class IdentifierTable {
    ArrayList<String> table = new ArrayList<>();

    public void addTable(String newId){
        for(String id : table){
            if(id.compareTo(newId) == 0) return;
        }
        table.add(newId);
    }

    public boolean searchTable(String newId){
        for(String id: table){
            if(id.compareTo(newId) == 0) return true;
        }
        return false;
    }
}
