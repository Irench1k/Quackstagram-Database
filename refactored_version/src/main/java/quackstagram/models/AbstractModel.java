package quackstagram.models;

// Child classes should be defined as:
// class User extends AbstractModel<User>
//
// We need to to use 'T' as a parameter in the 'isIdEqualTo' to be able to compare 
// objects of the same type (childclass)
public abstract class AbstractModel <T extends AbstractModel<T>> {
    public abstract String[] serialize();

    public abstract boolean isUpdatable();

    public abstract boolean isIdEqualTo(T object);
}
