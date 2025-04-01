package seedu.finclient.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.finclient.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.finclient.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.finclient.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.finclient.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.finclient.testutil.Assert.assertThrows;
import static seedu.finclient.testutil.TypicalPersons.ALICE;
import static seedu.finclient.testutil.TypicalPersons.BENSON;
import static seedu.finclient.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.finclient.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void constructor_validInputs_createsPerson() {
        // Construct a valid person object
        Person person = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.getName(), person.getName());
        assertEquals(ALICE.getPhoneList(), person.getPhoneList());
        assertEquals(ALICE.getEmail(), person.getEmail());
        assertEquals(ALICE.getAddress(), person.getAddress());
        assertEquals(ALICE.getTags(), person.getTags());
        assertEquals(ALICE.getCompany(), person.getCompany());
        assertEquals(ALICE.getJob(), person.getJob());
        assertEquals(ALICE.getStockPlatform(), person.getStockPlatform());
        assertEquals(ALICE.getNetworth(), person.getNetworth());
    }

    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        // Check if null values are handled by throwing NullPointerException
        assertThrows(NullPointerException.class, () ->
                new Person(null, null, null, null, null, null,
                null, null, null, null, null));
    }

    @Test
    public void otherConstructor_nullValues_throwsNullPointerException() {
        // Check if null values are handled by throwing NullPointerException
        assertThrows(NullPointerException.class, () ->
                new Person(null, null, null, null, null,
                        null, null, null, null, null));
    }

    @Test
    public void testHiddenBehavior() {
        // Hide the person
        Person hiddenAlice = new PersonBuilder(ALICE).build();
        hiddenAlice.setHidden();
        assertTrue(hiddenAlice.getIsHidden(), "Person should be hidden.");

        // Now verify that the 'hidden' variants are returned
        assertEquals("00000000", hiddenAlice.getPhoneList().toString(),
                "PhoneList should be replaced with 00000000 when hidden.");
        assertEquals("hidden@example.com", hiddenAlice.getEmail().toString(),
                "Email should be replaced with hidden@example.com when hidden.");
        assertEquals("Hidden", hiddenAlice.getAddress().toString(),
                "Address should read 'Hidden' when hidden.");
        assertEquals("Sensitive details are hidden", hiddenAlice.getRemark().toString(),
                "Remark should read 'Sensitive details are hidden' when hidden.");
        assertEquals("Hidden", hiddenAlice.getCompany().toString(),
                "Company should read 'Hidden' when hidden.");
        assertEquals("Hidden", hiddenAlice.getJob().toString(),
                "Job should read 'Hidden' when hidden.");
        assertEquals("Hidden", hiddenAlice.getStockPlatform().toString(),
                "Stock platform should read 'Hidden' when hidden.");
        assertEquals("Hidden", hiddenAlice.getNetworth().toString(),
                "Networth should read 'Hidden' when hidden.");
        assertTrue(hiddenAlice.getTags().isEmpty(), "Tags should be empty when hidden.");
    }

    @Test
    public void testUnhiddenBehavior() {
        // Unhide after hiding the person
        Person hiddenAlice = new PersonBuilder(ALICE).build();
        hiddenAlice.setHidden();
        hiddenAlice.setUnhidden();
        assertFalse(hiddenAlice.getIsHidden(), "Person should be unhidden.");

        // Verify original details are returned after unhidden
        assertEquals(ALICE.getPhoneList(), hiddenAlice.getPhoneList(), "PhoneList should revert to original.");
        assertEquals(ALICE.getEmail(), hiddenAlice.getEmail(), "Email should revert to original.");
        assertEquals(ALICE.getAddress(), hiddenAlice.getAddress(), "Address should revert to original.");
        assertEquals(ALICE.getRemark(), hiddenAlice.getRemark(), "Remark should revert to original.");
        assertEquals(ALICE.getTags(), hiddenAlice.getTags(), "Tags should revert to original.");
        assertEquals(ALICE.getCompany(), hiddenAlice.getCompany(), "Company should revert to original.");
        assertEquals(ALICE.getJob(), hiddenAlice.getJob(), "Job should revert to original.");
        assertEquals(ALICE.getStockPlatform(), hiddenAlice.getStockPlatform(),
                "Stock platform should revert to original.");
        assertEquals(ALICE.getNetworth(), hiddenAlice.getNetworth(), "Networth should revert to original.");
    }

    @Test
    public void equals_samePerson_returnsTrue() {
        // Compare the same person instance
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // Same object should return true
        assertTrue(ALICE.equals(ALICE));

        // Compare with null should return false
        assertFalse(ALICE.equals(null));

        // Compare with a different type should return false
        assertFalse(ALICE.equals(5));

        // Compare with a different person object
        assertFalse(ALICE.equals(BOB));
    }

    @Test
    public void isSamePerson_differentAttributes_returnsTrue() {
        // Same name, different other attributes
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void compareTo_sameName_returnsEqual() {
        // Compare two persons with same name
        assertEquals(0, ALICE.compareTo(ALICE, "name"));
    }

    @Test
    public void compareTo_differentNames_returnsComparison() {
        // Compare two persons with different names
        assertTrue(ALICE.compareTo(BOB, "name") < 0,
                "ALICE should come before BOB in name comparison.");
        assertTrue(BOB.compareTo(ALICE, "name") > 0,
                "BOB should come after ALICE in name comparison.");
    }

    @Test
    public void compareTo_differentNetworth_returnsComparison() {
        // Compare two persons with different networth
        assertTrue(ALICE.compareTo(BENSON, "networth") < 0,
                "ALICE should come before Benson in networth comparison.");
        assertTrue(BENSON.compareTo(ALICE, "networth") > 0,
                "Benson should come after Alice in networth comparison.");
    }

    @Test
    public void compareTo_sameNetworth_returnsEqual() {
        // Compare two persons with same networth
        assertEquals(0, ALICE.compareTo(ALICE, "networth"));
    }

    @Test
    public void compareTo_differentAmounts_returnsComparison() {
        // Compare two persons with different amounts
        assertTrue(ALICE.compareTo(BENSON, "amount") < 0,
                "Alice should come before Benson in amount comparison.");
        assertTrue(BENSON.compareTo(ALICE, "amount") > 0,
                "Benson should come after Alice in amount comparison.");
    }

    @Test
    public void compareTo_sameAmounts_returnsEqual() {
        // Compare two persons with same amounts
        assertEquals(0, ALICE.compareTo(ALICE, "amount"));
    }

    @Test
    public void compareTo_differentPrices_returnsComparison() {
        // Compare two persons with different amounts
        assertTrue(ALICE.compareTo(BENSON, "price") > 0,
                "Alice should come after Benson in price comparison.");
        assertTrue(BENSON.compareTo(ALICE, "price") < 0,
                "Benson should come before Alice in price comparison.");
    }

    @Test
    public void compareTo_samePrices_returnsEqual() {
        // Compare two persons with same amounts
        assertEquals(0, ALICE.compareTo(ALICE, "price"));
    }

    @Test
    public void compareToNull_throwsNullPointerException() {
        // Check if null values are handled by throwing NullPointerException
        assertThrows(NullPointerException.class, () -> ALICE.compareTo(null, "name"));
    }

    @Test
    public void compareTo_deadline() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        Person personYesterday = new PersonBuilder()
                .withRemark("Old Event by/"
                        + yesterday.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .withName("Past Person")
                .build();

        Person personToday = new PersonBuilder()
                .withRemark("Today Event by/"
                        + today.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .withName("Today Person")
                .build();

        Person personTomorrow = new PersonBuilder()
                .withRemark("Tomorrow Event by/"
                        + tomorrow.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .withName("Future Person")
                .build();

        // Compare persons with different deadlines
        assertTrue(personYesterday.compareTo(personToday, "deadline") < 0,
                "Person with yesterday's deadline should come before today's deadline.");
        assertTrue(personToday.compareTo(personTomorrow, "deadline") < 0,
                "Person with today's deadline should come before tomorrow's deadline.");
        assertTrue(personTomorrow.compareTo(personToday, "deadline") > 0,
                "Person with tomorrow's deadline should come after today's deadline.");
        assertTrue(personToday.compareTo(personYesterday, "deadline") > 0,
                "Person with today's deadline should come after yesterday's deadline.");
        assertEquals(0, personToday.compareTo(personToday, "deadline"),
                "Person with same deadline should be equal.");
        assertTrue(personYesterday.compareTo(personTomorrow, "deadline") < 0,
                "Person with yesterday's deadline should come before tomorrow's deadline.");
        assertTrue(personTomorrow.compareTo(personYesterday, "deadline") > 0,
                "Person with tomorrow's deadline should come after yesterday's deadline.");
    }

    @Test
    public void toString_hiddenPerson_displaysHiddenDetails() {
        // Hide the person and check the toString method
        Person hiddenAlice = new PersonBuilder(ALICE).build();
        hiddenAlice.setHidden();
        String hiddenString = hiddenAlice.toString();
        assertTrue(hiddenString.contains("Sensitive details are hidden"),
                "Hidden person's toString() should indicate that details are hidden.");
    }

    @Test
    public void toString_unhiddenPerson_displaysFullDetails() {
        // Check the toString method for unhidden person
        String unhiddenString = ALICE.toString();
        assertTrue(unhiddenString.contains(ALICE.getName().toString()),
                "Unhidden person's toString() should show full details.");
    }

    @Test
    public void testingHashValue() {
        Person hiddenAlice = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), hiddenAlice.hashCode());
    }
}
