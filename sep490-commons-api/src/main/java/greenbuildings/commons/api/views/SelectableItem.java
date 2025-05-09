package greenbuildings.commons.api.views;

public record SelectableItem<V>(
        String label,
        V value,
        boolean disabled
) {
}
