package mmt.core.exceptions;

public class NonUniqueServiceIdException extends Exception {
    private static final long serialVersionUID = -406449929624769494L;
    private int _id;

    public NonUniqueServiceIdException(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }
}
