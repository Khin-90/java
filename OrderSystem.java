import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Product {
    private final String name;
    private double price;

    protected Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    protected void setPrice(double price) { this.price = price; }
}

abstract class Pizza extends Product {
    private String size;
    private final double basePrice;

    protected Pizza(String name, String size, double basePrice) {
        super(name, calculatePrice(size, basePrice));
        this.size = size;
        this.basePrice = basePrice;
    }

    private static double calculatePrice(String size, double basePrice) {
        return switch (size.toLowerCase()) {
            case "medium" -> basePrice + 200.0;
            case "large" -> basePrice + 400.0;
            default -> basePrice;
        };
    }

    public void setSize(String size) {
        this.size = size;
        super.setPrice(calculatePrice(size, basePrice));
    }

    public String getSize() { return size; }
}

class Margherita extends Pizza {
    public Margherita(String size) {
        super("Chicken Mushroom", size, 200.0);
    }
}

class Pepperoni extends Pizza {
    public Pepperoni(String size) {
        super("Pepperoni", size, 250.0);
    }
}

class ChickenTikka extends Pizza {
    public ChickenTikka(String size) {
        super("Chicken Tikka", size, 280.0);
    }
}

class Veggie extends Pizza {
    public Veggie(String size) {
        super("Veggie", size, 130.0);
    }
}

abstract class Drink extends Product {
    private final int volume;

    protected Drink(String name, double price, int volume) {
        super(name, price);
        this.volume = volume;
    }

    public int getVolume() { return volume; }
}

class Beer extends Drink {
    private final double alcoholContent;

    public Beer() {
        super("Beer", 500.0, 500);
        this.alcoholContent = 4.5;
    }

    public double getAlcoholContent() { return alcoholContent; }
}

class Cola extends Drink {
    public Cola() {
        super("Cola", 300.0, 330);
    }
}

class Pepsi extends Drink {
    public Pepsi() {
        super("Pepsi", 350, 330);
    }
}

class Sprite extends Drink {
    public Sprite() {
        super("Sprite", 350, 330);
    }
}

class OrderSystem {
    private Product currentProduct;
    private final List<Product> order = new ArrayList<>();

    public void createPizza(int choice, String size) {
        currentProduct = switch (choice) {
            case 1 -> new Margherita(size);
            case 2 -> new Pepperoni(size);
            case 3 -> new ChickenTikka(size);
            case 4 -> new Veggie(size);
            default -> throw new IllegalArgumentException("Sorry KhinsPizza doesn't have that option for now,Please try again ");
        };
    }

    public void createDrink(int choice) {
        currentProduct = switch (choice) {
            case 1 -> new Beer();
            case 2 -> new Cola();
            case 3 -> new Pepsi();
            case 4 -> new Sprite();
            default -> throw new IllegalArgumentException("KhinsPizza only has the options on the menu, Please try again");
        };
    }

    public void addCurrentToOrder() {
        if (currentProduct != null) {
            order.add(currentProduct);
            currentProduct = null;
        }
    }

    public double getTotal() {
        return order.stream().mapToDouble(Product::getPrice).sum();
    }

    public void printOrder() {
        System.out.println("\nCurrent Order:");
        for (Product p : order) {
            String details = p.getName() + " - Ksh" + String.format("%.2f", p.getPrice());
            if (p instanceof Pizza pizza) {
                details += " (" + pizza.getSize() + ")";
            } else if (p instanceof Drink drink) {
                details += " (" + drink.getVolume() + "ml)";
            }
            System.out.println(details);
        }
        System.out.println("Total: Ksh" + String.format("%.2f", getTotal()));
    }

    public static void main(String[] args) {
        OrderSystem system = new OrderSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Pizza Ordering System ===");
            System.out.println("1. Order Pizza");
            System.out.println("2. Order Drink");
            System.out.println("3. View Order");
            System.out.println("4. Complete Payment");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.println("\nAvailable Pizzas:");
                    System.out.println("1. Chicken Mushroom (Ksh700.00 small)");
                    System.out.println("2. Pepperoni (Ksh850.00 small)");
                    System.out.println("3. Chicken Tikka (Ksh800.00 small)");
                    System.out.println("4. Veggie (Ksh6805.00 small)");
                    System.out.print("Select pizza (1-4): ");
                    int pizzaChoice = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Size (small/medium/large): ");
                    String size = scanner.nextLine().toLowerCase();

                    System.out.print("Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < quantity; i++) {
                        system.createPizza(pizzaChoice, size);
                        system.addCurrentToOrder();
                    }
                }

                case 2 -> {
                    System.out.println("\nAvailable Drinks:");
                    System.out.println("1. Beer (Ksh300.00)");
                    System.out.println("2. Cola (Ksh150.00)");
                    System.out.println("3. Pepsi (Ksh130.00)");
                    System.out.println("4. Sprite (Ksh150.00)");
                    System.out.print("Select drink (1-4): ");
                    int drinkChoice = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < quantity; i++) {
                        system.createDrink(drinkChoice);
                        system.addCurrentToOrder();
                    }
                }

                case 3 -> system.printOrder();

                case 4 -> {
                    System.out.println("\nTotal Amount: Ksh" + String.format("%.2f", system.getTotal()));
                    System.out.print("Confirm payment (yes/no): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        System.out.println("\n=== Payment Successful ===");
                        System.out.println("Thank you for your order!");
                        System.out.println("Thank You for coming to Khins Pizza. Please Come again next time!!");
                        return;
                    } else {
                        System.out.println("Payment cancelled");
                    }
                }

                case 5 -> {
                    System.out.println("Exiting system...");
                    return;
                }

                default -> System.out.println("Invalid option!");
            }
        }
    }
}