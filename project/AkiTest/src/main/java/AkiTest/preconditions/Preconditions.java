package AkiTest.preconditions;

import java.util.Collection;

public class Preconditions {
	public static <T> void checkCollectionNotNullOrEmpty(Collection<T> testCollection) {
		com.google.common.base.Preconditions.checkNotNull(testCollection);
		com.google.common.base.Preconditions.checkArgument(testCollection.size() != 0);
	}
}
