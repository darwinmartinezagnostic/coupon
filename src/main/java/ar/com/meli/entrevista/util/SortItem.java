package ar.com.meli.entrevista.util;

import ar.com.meli.entrevista.dto.Item;

import java.util.Comparator;

public class SortItem implements Comparator<Item>{
    public int compare(Item a, Item b)
    {
        boolean temp = a.getValue() > b.getValue();
        return temp ? -1 : 1;
    }
}
