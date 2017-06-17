package pt.europeia.path.models;


public final class InsertionModified {

    private InsertionModified() {
        throw new RuntimeException("Attempt to instantiate package-class");
    }

    public static void sort(
            final double[] values, Object[] values2) {
        for (int numberOfSortedItems = 1;
             numberOfSortedItems < values.length;
             numberOfSortedItems++)
            for (int i = numberOfSortedItems; i != 0
                    && isLess(values[i], values[i - 1]); i--)
                swap(values, i - 1, i, values2);

        assert isIncreasing(values) :
            "Array should be increasing after sorting.";
    }

    private static boolean isLess(
            final double first, final double second) {
    	if(first < second) {
    		return true;
    	} else {
        return false;
    	}
    }

    private static void swap(final double[] values, final int firstPosition,
            final int secondPosition, final Object[] values2) {
        final double temporary = values[firstPosition];
        values[firstPosition] = values[secondPosition];
        values[secondPosition] = temporary;
        final Object temporary2 = values2[firstPosition];
        values2[firstPosition] = values2[secondPosition];
        values2[secondPosition] = temporary2;
    }

    private static boolean isIncreasing(
            final double[] values) {
        for (int i = 1; i < values.length; i++)
            if (isLess(values[i], values[i - 1]))
                return false;
        return true;
    }

}