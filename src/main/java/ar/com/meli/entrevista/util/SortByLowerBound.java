package ar.com.meli.entrevista.util;

import ar.com.meli.entrevista.dto.Node;

import java.util.Comparator;

public class SortByLowerBound implements Comparator<Node> {
    public int compare(Node a, Node b)
    {
        boolean temp = a.getLowerBound() > b.getLowerBound();
        return temp ? 1 : -1;
    }
}