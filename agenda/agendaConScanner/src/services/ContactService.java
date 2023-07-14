package services;

import dao.ContactDAO;
import model.Contact;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactService {

    private ContactDAO contactDAO;

    public ContactService(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    Scanner sc = new Scanner(System.in);
    List<Contact> contactList = new ArrayList<>();

    public int repetir = -1;

    public void createContact(Contact contact) {

        while (repetir < 0) {
            //Getting names
            System.out.println("Insert your names:");
            contact.firstName = sc.nextLine();

            //validar en tiempo real si el nombre introducido es el correcto(not null)
            while (!validateNull(contact.firstName)) {
                System.out.println("introduzca el nombre correctamente");
                contact.firstName = sc.nextLine();
            }

            //Getting last names
            System.out.println("Insert your last name:");
            contact.lastName = sc.nextLine();

            //validar en tiempo real si el nombre introducido es el correcto(not null)
            while (!validateNull(contact.lastName)) {
                System.out.println("insert your last name correctly");
                contact.lastName = sc.nextLine();
            }

            //Getting phone number
            System.out.println("Insert the 10 digit phone number:");
            contact.phoneNumber = sc.nextLine();
            while (!validateNull(contact.phoneNumber)) {
                System.out.println("Enter the number correctly");
                contact.phoneNumber = sc.nextLine();
            }

            contactList = contactDAO.getByPhoneNumber(contact);

            //ciclo para validar en tiempo real que el numero telefonico introducido sea el correcto (not null ni repetido)
            while (contactList != null && contactList.size() != 0) {
                System.out.println("Este telefono ya pertenece a un contacto");
                System.out.println("El telefono no puede repetirse");
                System.out.println("Por favor agregar otro numero (o presione 0 para salir)");
                contact.phoneNumber = sc.nextLine();
                contactList = contactDAO.getByPhoneNumber(contact);
            }

            //condicional para romper el flujo en caso de presionar 0
            if (contact.phoneNumber.equals(0)) {
                break;
            }
            // validando longitud, que no sea nulo y que sean numeros del numero telefonico
            while (!validateNumber(contact.phoneNumber) ||
                    !validateLength(contact.phoneNumber) ||
                    !validateNull(contact.phoneNumber)) {

                System.out.println("Debe introducir el numero de telefono de manera correcta:");
                contact.phoneNumber = sc.nextLine();
            }

            //email
            System.out.println("Introduzca el correo electronico:");
            contact.email = sc.nextLine();

            while (!validateNull(contact.email) || !validateEmail(contact.email)) {
                System.out.println("Debe introducir el correo electronico de manera correcta:");
                contact.email = sc.nextLine();
            }

            if (validateNumber(contact.phoneNumber)) {

                if (validateLength(contact.phoneNumber)) {

                    if (validateNull(contact.firstName) ||
                            validateNull(contact.lastName) ||
                            validateNull(contact.phoneNumber) ||
                            validateNull(contact.email)) {
                        if (validateEmail(contact.email)) {
                            contactDAO.createContact(contact);
                        }
                    }
                }
            }
            //mensaje de confirmacion
            System.out.println("contacto agregado satisfactoriamente.");
            repetir = 0;
        }
        repetir = -1;
    }

    public List<Contact> getByEmail(Contact contact) {
        while (repetir < 0) {
            System.out.println("Introduzca el correo que desea consultar:");
            contact.email = sc.nextLine();

            while (!validateNull(contact.email)) {
                System.out.println("Agregue un correo valido");
                contact.email = sc.nextLine();
            }
            contactList = contactDAO.getByEmail(contact);

            while (contactList == null || contactList.size() == 0) {
                System.out.println("email incorrecto o no existe en la agenda");
                System.out.println("Introduzca el correo electronico que desea consultar (escriba 0 para salir):");

                contact.email = sc.nextLine();

                if (contact.email.equals("0")) {
                    break;
                }
                contactList = contactDAO.getByEmail(contact);
            }
            if (contact.email.equals("0")) {
                break;
            }

            for (Contact i : contactList) {
                showContacts(i);
            }
            repetir = 0;
        }
        repetir = -1;
        return contactDAO.getByEmail(contact);
    }

    public List<Contact> getByFirstName(Contact contact) {
        while (repetir < 0) {
            System.out.println("Introduzca el nombre que desea consultar");
            contact.firstName = sc.nextLine();

            while (!validateNull(contact.firstName)) {
                System.out.println("Introduzca un nombre correcto");
                contact.firstName = sc.nextLine();
            }

            contactList = contactDAO.getByFirstName(contact);

            while (contactList == null || contactList.size() == 0) {
                System.out.println("Nombre Incorrecto o no existe en la agenda");
                System.out.println("Introduzca un nombre valido o presione 0 para salir:");

                contact.firstName = sc.nextLine();
                validateNull(contact.firstName);

                if (contact.firstName.equals("0")) {
                    break;
                }

                contactList = contactDAO.getByFirstName(contact);
            }
            if (contact.firstName.equals("0")){
                break;
            }

            for (Contact i : contactList) {
                showContacts(i);
            }
            repetir = 0;
        }
        repetir = -1;
        return contactDAO.getByFirstName(contact);
    }

    public List<Contact> getByLastName(Contact contact) {
        while (repetir < 0 ) {
            System.out.println("Introduzca el apellido que deseas consultar");
            contact.lastName = sc.nextLine();

            while (!validateNull(contact.lastName)) {
                System.out.println("El apellido no es valido o no existe, intente otra vez:");
                contact.lastName = sc.nextLine();
            }

            try{
                contactList = contactDAO.getByLastName(contact);
            } catch (RuntimeException e) {
                System.out.println("Error al obtener el apellido el programa se cerrara");
                System.exit(0);
            }

            while (contactList == null || contactList.size() == 0) {
                System.out.println("Apellido incorrecto o no existe en la agenda");
                System.out.println("Introduzca el apellido que desea consultar o presione 0 para salir");

                contact.lastName = sc.nextLine();

                if (contact.lastName.equals("0")) {
                    break;
                }

                contactList = contactDAO.getByLastName(contact);
            }

            if (contact.lastName.equals("0")){
                break;
            }

            for (Contact i : contactList) {
                showContacts(i);
            }
            repetir = 0;
        }
        repetir = -1;
        return contactDAO.getByLastName(contact);
    }

    public List<Contact> getByPhoneNumber(Contact contact) {
        return contactDAO.getByPhoneNumber(contact);
    }

    public int delete(Contact contact) {
        System.out.println("Ingrese el numero telefonico que desea eliminar:");
        contact.phoneNumber = sc.nextLine();

        while (!validateNull(contact.phoneNumber)){
            System.out.println("Ingrese un numero de telefono valido");
            contact.phoneNumber = sc.nextLine();
        }

        validateNumber(contact.phoneNumber);

        while (contactDAO.delete(contact) == 0 ) {
            System.out.println("Introduzca el numero que desea eliminar o presione 0 para salir");
            contact.phoneNumber = sc.nextLine();

            if (contact.phoneNumber.equals("0")) {
                break;
            }
        }
        int affectedRows = contactDAO.delete(contact);

        if (affectedRows == 0) {
            System.out.println("No hay valor para eliminar, intente nuevamente.");
        } else {
            System.out.println("Contacto eliminado satisfactoriamente");
        }
        return affectedRows;
    }

    public List<Contact> showAll(Contact contact) {
        // a travez de nuestro objeto tipo arraylist, llamamos al metodo showall de nuestro objeto contactService
        contactList = contactDAO.showAll();

        for (Contact i : contactList) {
            showContacts(i);
        }
        return contactDAO.showAll();
    }

    public boolean validateNumber(String contact) {
        try {
            BigInteger.valueOf(Long.parseLong(contact));
        } catch (Exception e) {
            System.out.println("Debe introducir un numero valido");
        }
        return true;
    }

    public boolean validateLength(String contact) {
        if (contact.length() < 10){
            System.out.println("Introduzca 10 digitos:");
            return false;
        } else if (contact.length() > 10) {
            System.out.println("Introduzca menos de 10 digitos");
            return false;
        } else if (contact.length() == 10) {
            return true;
        }
        return true;
    }

    public boolean validateNull(String contact) {
        if (contact == null) {
            System.out.println("no puede tener campos nulos");
            return false;
        } else if (contact.isBlank()) {
            System.out.println("No puede haber campos vacios");
            return false;
        }

        return true;
    }

    public boolean validateEmail(String email) {
        String regex =  "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches() == true){
            return true;
        } else {
            System.out.println("Debe introducir un correo valido.");
        }
        return false;
    }

    //para mostrar todos los contactos consultados
    public Contact showContacts(Contact contact) {
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Nombre: " + contact.firstName);
        System.out.println("Apellido: " + contact.lastName);
        System.out.println("Phone: " + contact.phoneNumber);
        System.out.println("mail: " + contact.email);
        System.out.println("------------------------------------------------------------------------------");
        return null;
    }
}
