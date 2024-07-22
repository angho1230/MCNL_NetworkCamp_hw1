package parser.table;

import java.util.ArrayList;

public class IdentifierTable {

    public class Id{
        private String id;
        private int value = 0;
        private Id(String id){
            this.id = id;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    ArrayList<Id> table = new ArrayList<>();

    public void addTable(String newId){
        for(Id id : table){
            if(id.id.compareTo(newId) == 0) return;
        }
        table.add(new Id(newId));
    }

    public boolean searchTable(String newId){
        for(Id id: table){
            if(id.id.compareTo(newId) == 0) return true;
        }
        return false;
    }
    public int getValue(String sid){
        for(Id id: table){
            if(id.id.compareTo(sid) == 0) return id.value;
        }
        System.out.println("failed finding id" + sid);
        return 0;
    }

    public void setValue(String sid, int n){
        for(Id id: table){
            if(id.id.compareTo(sid) == 0) {
                id.value = n;
                return;
            }
        }
        System.out.println("failed finding id" + sid);
        return;
    }

    public Id getIdInstance(String sid){
        for(Id id: table){
            if(id.id.compareTo(sid) == 0) return id;
        }
        return null;
    }
}
