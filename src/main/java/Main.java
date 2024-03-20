import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        EntityManagerFactory softUni = Persistence.createEntityManagerFactory("soft_uni");
        EntityManager entityManager = softUni.createEntityManager();
        entityManager.getTransaction().begin();

//        task2(entityManager);
//        task3(entityManager);
        //task4(entityManager);
        //task5(entityManager);
        //task6(entityManager);
        //task7(entityManager);
       // task8(entityManager);
        //task9(entityManager);
        //task10(entityManager);
        //task11(entityManager);
        //task12(entityManager);
        String townName = sc.nextLine();
        Town town = entityManager.createQuery("FROM Town WHERE name =:townNa", Town.class).setParameter("townNa", townName).getSingleResult();
        Integer id = town.getId();
        List<Address> addresses = entityManager.createQuery("FROM Address WHERE town.id =:id", Address.class).setParameter("id", id).getResultList();

        int count = 0;
        for (Address address : addresses) {
            Set<Employee> employees = address.getEmployees();
            for (Employee employee : employees) {
                employee.setAddress(null);
            }
            count++;
            entityManager.remove(address);
        }
        entityManager.remove(town);
        System.out.println(townName + " " + count);

        entityManager.getTransaction().commit();
    }

    private static void task12(EntityManager entityManager) {
        List<Department> resultList = entityManager.createQuery("FROM Department ", Department.class).getResultList();
        for (Department department : resultList) {
            Set<Employee> employees = department.getEmployees();
            Double max = employees.stream().map(Employee::getSalary)
                    .map(BigDecimal::doubleValue)
                    .max(Double::compareTo).get();
            if(max > 70000 || max < 30000){
                System.out.println(department.getName() + " " + max);
            }

        }
    }

    private static void task11(EntityManager entityManager) {
        String pattern = sc.nextLine();
        List<Employee> resultList = entityManager.createQuery("FROM Employee WHERE firstName LIKE CONCAT(:pattern,'%')", Employee.class)
                .setParameter("pattern", pattern).getResultList();
        for (Employee employee : resultList) {
            System.out.println(employee.getFirstName());
        }
    }

    private static void task10(EntityManager entityManager) {
        List<Employee> resultList = entityManager.createQuery("FROM Employee WHERE department.name IN ('Engineering','Tool Design','Marketing','Information Services')", Employee.class).getResultList();
        for (Employee employee : resultList) {
            employee.setSalary(employee.getSalary().multiply(new BigDecimal("1.12")));
            System.out.println(employee.getFirstName() + " " + employee.getSalary());
        }
    }

    private static void task9(EntityManager entityManager) {
        entityManager.createQuery("FROM Project ORDER BY startDate DESC", Project.class).setMaxResults(10).getResultList()
                .stream().sorted(Comparator.comparing(Project::getName))
                .forEach(project -> System.out.println(project.getName()));
    }

    private static void task8(EntityManager entityManager) {
        Employee employee = entityManager.find(Employee.class, Integer.parseInt(sc.nextLine()));
        System.out.println(employee.getFirstName() + " " + employee.getLastName() + " - " + employee.getJobTitle());
        employee.getProjects().stream().sorted(Comparator.comparing(Project::getName)).forEach(project -> System.out.println("     " + project.getName()));
    }

    private static void task7(EntityManager entityManager) {
        entityManager.createQuery("FROM Address a ORDER BY a.employees.size DESC",Address.class).setMaxResults(10).getResultList()
                        .forEach(a -> System.out.println(a.getText() + " " + a.getEmployees().size()));
    }

    private static void task6(EntityManager entityManager) {
        Address address = new Address();
        address.setText("Vitoshka 15");
        entityManager.persist(address);
        String lastName = sc.nextLine();
        Employee e = entityManager.createQuery("FROM Employee WHERE lastName =:lname", Employee.class).setParameter("lname", lastName).getSingleResult();
        e.setAddress(address);
    }

    private static void task5(EntityManager entityManager) {
        String departString = "Research and Development";
        List<Employee> resultList = entityManager.createQuery("SELECT e FROM Employee e WHERE e.department.name =:name ORDER BY e.salary,e.id",Employee.class).setParameter("name",departString).getResultList();
        for (Employee employee : resultList) {
            System.out.println(employee.getFirstName());
        }
    }

    private static void task4(EntityManager entityManager) {
        List<String> resultList = entityManager.createQuery("SELECT e.firstName FROM Employee e WHERE e.salary > 50000", String.class).getResultList();
        for (String name : resultList) {
            System.out.println(name);
        }
    }

    private static void task3(EntityManager entityManager) {
        String name = sc.nextLine();
        List<Employee> resultList = entityManager.createQuery("FROM Employee WHERE CONCAT(firstName,' ',lastName) =:name", Employee.class)
                .setParameter("name", name)
                .getResultList();
        System.out.println(!resultList.isEmpty() ? "Yes" : "No");
    }

    private static void task2(EntityManager entityManager) {
        List<Town> towns = entityManager.createQuery("FROM Town", Town.class).getResultList();
        for (Town town : towns) {
            if (town.getName().length() > 5){
                entityManager.detach(town);
            }
        }
        for (Town town : towns) {
            town.setName(town.getName().toUpperCase());
        }
    }
}
