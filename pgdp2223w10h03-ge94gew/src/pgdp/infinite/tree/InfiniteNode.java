package pgdp.infinite.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfiniteNode<T> {

    private final InfiniteTree<T> tree;
    private final InfiniteNode<T> parent;
    private final T value;

    private List<InfiniteNode<T>> children;

    private Iterator<T> iterator;

    public InfiniteNode(InfiniteTree<T> tree, T value, InfiniteNode<T> parent) {
        this.parent = parent;
        this.tree = tree;
        this.value = value;

        iterator = tree.children.apply(value);
        children = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public InfiniteNode<T> getParent() {
        return parent;
    }

    /**
     * @return Gibt alle bisher berechneten Kinder des Knotens zurück.
     */
    public List<InfiniteNode<T>> getChildren() {
        return this.children;
    }

    /**
     * Berechnet das nächste Kind des Knotens und gibt es zurück.
     * @return das neue Kind oder null, wenn es keine weiteren Kinder gibt.
     */
    public InfiniteNode<T> calculateNextChild() {
        if (iterator.hasNext()) {
            InfiniteNode<T> n = new InfiniteNode<>(tree, iterator.next(), this);
            children.add(n);
            return n;
        }

        return null;
    }

    /**
     * Berechnet alle Kinder des Knotens.
     */
    public void calculateAllChildren() {
        for (Iterator<T> it = iterator; it.hasNext(); ) {
            InfiniteNode<T> n = new InfiniteNode<>(tree, it.next(), this);
            children.add(n);
        }
    }

    /**
     * @return true, wenn alle Kinder berechnet wurden, false sonst.
     */
    public boolean isFullyCalculated() {
        return !this.iterator.hasNext();
    }

    /**
     * Setzt die gesamte Berechnung des Knotens zurück.
     */
    public void resetChildren() {
        iterator = tree.children.apply(value);
        children = new ArrayList<>();
    }

}
