package aoc.utils;

import java.util.Arrays;

public class Pair<A, B> {

    private final A firstElement;
    private final B secondElement;

    public Pair(A firstElement, B secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public A getFirstElement() {
        return firstElement;
    }

    public B getSecondElement() {
        return secondElement;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pair<?,?> p){
            return firstElement == p.firstElement && secondElement == p.secondElement;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.firstElement, this.secondElement});
    }

    @Override
    public String toString() {
        return "(" + this.firstElement.toString() + "," + this.secondElement.toString() + ")";
    }
}
