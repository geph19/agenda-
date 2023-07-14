package menu;

import dao.impl.ContactDAOImplementation;
import model.Contact;
import services.ContactService;

import java.util.Scanner;

public class ContactMenu {

    public void Menu() {
        //primero se declaran las variables
        int repetir = -1;
        String entrada;

        //se crean los objetos
        ContactService contactService = new ContactService(new ContactDAOImplementation());
        Contact contact = new Contact();

        // se crea el scanner
        Scanner sc = new Scanner(System.in);

        //ciclo para repetir tod el menu
        while (repetir < 0) {
            //se imprime el menu en pantalla
            System.out.println("---------- Agenda Telefonica ----------");
            System.out.println("Seleccione la opcion que desea realizar:");
            System.out.println("1. Crear Contacto.");
            System.out.println("2. Buscar contacto por nombre.");
            System.out.println("3. Buscar Contacto por apellido.");
            System.out.println("4. Buscar contacto por email.");
            System.out.println("5. Eliminar contacto.");
            System.out.println("6. Mostrar todos los contactos.");
            System.out.println("0. Salir");

            int option = validate(sc);

            //validamos que el numero introducido
            if (option < 0 || option >6) {
                while (option < 0 || option > 6) {
                    System.out.println("Introduzca un numero del 0 al 6");
                    option = validate(sc);
                }
            }
            //Switch para las diferentes opciones
            switch (option){
                case 1:
                    contactService.createContact(contact);
                    break;
                case 2:
                    contactService.getByFirstName(contact);
                    break;
                case 3:
                    contactService.getByLastName(contact);
                    break;
                case 4:
                    contactService.getByEmail(contact);
                    break;
                case 5:
                    contactService.delete(contact);
                    break;
                case 6:
                    contactService.showAll(contact);
                    break;
            }

            //Preguntas de confirmacion
            System.out.println("");
            System.out.println("Desea salir");
            System.out.println("Si(y) [Cierra el programa]");
            System.out.println("no(n) [Vuelve al menu principal]");

            //do-while para validar la entrada de y o n
            do {
                entrada = sc.nextLine();
                entrada = entrada.toUpperCase();

                if (entrada.equalsIgnoreCase("Y")) {
                    repetir = 1;
                    break;
                } else if (entrada.equalsIgnoreCase("N")) {
                    repetir = -1;
                    break;
                } else {
                    System.out.println(" Debe ser Y o N");
                }
            } while (true);
        }

    }

    //metodo para validar las opciones del menu (del 0 al 6)
    public int validate(Scanner sc){
        int i = -1;

        while (i < 0) {
            String optionMenu = sc.nextLine();

            try {
                i = Integer.valueOf(optionMenu);
            } catch (Exception e) {
                System.out.println("Debe introducir una opcion valida");
                i = -1;
            }
        }
        return i;
    }
}
