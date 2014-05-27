package in.huohua.peterson.misc;

public class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair<?, ?> pairo = (Pair<?, ?>) o;
        return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }
}
