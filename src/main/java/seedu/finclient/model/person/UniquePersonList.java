package seedu.finclient.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.finclient.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.finclient.model.order.CallAuctionCalculator;
import seedu.finclient.model.order.Order;
import seedu.finclient.model.person.exceptions.DuplicatePersonException;
import seedu.finclient.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}. As such, adding and updating of
 * persons uses Person#isSamePerson(Person) for equality so as to ensure that the person being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a person uses Person#equals(Object) so
 * as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     */
    public void add(Person toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(persons);
    }

    /**
     * Hides details of the persons that satisfy the predicate.
     */
    public void hidePerson(Predicate<Person> predicate) {
        requireNonNull(predicate);
        internalList.filtered(predicate)
                .forEach(Person::setHidden);
    }

    /**
     * Hides details of the person.
     */
    public void hidePerson(Person person) {
        requireNonNull(person);
        internalList.filtered(person::isSamePerson)
                .forEach(Person::setHidden);
    }

    /**
     * Reveals details of the persons that satisfy the predicate.
     */
    public void revealPerson(Predicate<Person> predicate) {
        requireNonNull(predicate);
        internalList.filtered(predicate)
                .forEach(Person::setUnhidden);
    }

    /**
     * Reveals details of the person.
     */
    public void revealPerson(Person person) {
        requireNonNull(person);
        internalList.filtered(person::isSamePerson)
                .forEach(Person::setUnhidden);
    }

    /**
     * Returns the clearing price based on current orders.
     */
    public Optional<Double> calculateClearingPrice() {
        List<Order> orders = internalList.stream()
                .map(p -> p.getOrder())
                .toList();

        return CallAuctionCalculator.calculateClearingPrice(orders);
    }

    public void sortPersons(String criteria) {
        internalList.setAll(internalList.sorted((p1, p2) -> p1.compareTo(p2, criteria)));
    }
    /**
     * Returns a list of persons who have remarks with upcoming timestamps.
     */
    public List<Person> upcomingPersons(int count) {
        LocalDate today = LocalDate.now();
        return internalList.stream()
                .filter(p -> p.getRemark().getTimestamp().isPresent())
                .filter(p -> !p.getRemark().getTimestamp().get().toLocalDate().isBefore(today))
                .sorted(Comparator.comparing(a -> a.getRemark().getTimestamp().get()))
                .limit(count)
                .toList();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePersonList)) {
            return false;
        }

        UniquePersonList otherUniquePersonList = (UniquePersonList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
