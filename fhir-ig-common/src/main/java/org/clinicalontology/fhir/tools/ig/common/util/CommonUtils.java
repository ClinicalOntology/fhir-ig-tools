package org.clinicalontology.fhir.tools.ig.common.util;

public class CommonUtils {

    /**
     * Method attempts to cast object o into type. A runtime exception
     * is thrown if the cast is invalid.
     *
     * @param type The type used to cast the object.
     * @param o The object one wishes to cast.
     * @param <R> The Class type.
     * @return
     */
    public static <R> R cast(Class<R> type, Object o) {
        try {
            return type.cast(o);
        } catch (ClassCastException e) {
            throw new RuntimeException("Instance is not a " + type.getSimpleName(), e);
        }
    }
}
