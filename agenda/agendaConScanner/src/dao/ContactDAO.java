package dao;

import model.Contact;

import java.util.List;

public interface ContactDAO {

    void createContact(Contact contact);
    List<Contact> showAll();
    List<Contact> getByFirstName(Contact contact);
    List<Contact> getByLastName(Contact contact);
    List<Contact> getByPhoneNumber(Contact contact);
    List<Contact> getByEmail(Contact contact);
    int delete(Contact contact);
}
