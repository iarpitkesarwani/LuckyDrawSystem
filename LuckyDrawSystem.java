package Game;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 This game is developed by Arpit Kesarwani. 
 LinkedIn: https://www.linkedin.com/in/arpit-kesarwani17/
*/

public class LuckyDrawSystem {
    
    // Participant class to store participant information
    static class Participant {
        String name;
        String email;
        String phone;
        int ticketNumber;
        LocalDateTime registrationTime;
        
        public Participant(String name, String email, String phone, int ticketNumber) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.ticketNumber = ticketNumber;
            this.registrationTime = LocalDateTime.now();
        }
        
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("Ticket #%d: %s | Email: %s | Phone: %s | Registered: %s", 
                ticketNumber, name, email, phone, registrationTime.format(formatter));
        }
    }
    
    private List<Participant> participants;
    private List<Participant> winners;
    private Set<Integer> usedTicketNumbers;
    private int nextTicketNumber;
    private Scanner scanner;
    
    public LuckyDrawSystem() {
        participants = new ArrayList<>();
        winners = new ArrayList<>();
        usedTicketNumbers = new HashSet<>();
        nextTicketNumber = 1001;
        scanner = new Scanner(System.in);
    }
    
    // Register new participants
    public void registerParticipant() {
        System.out.println("\n=== REGISTER PARTICIPANT ===");
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        
        // Generate unique ticket number
        int ticketNumber = generateTicketNumber();
        Participant participant = new Participant(name, email, phone, ticketNumber);
        participants.add(participant);
        
        System.out.println("\n✅ Registration Successful!");
        System.out.println("Participant: " + name);
        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println("Total Participants: " + participants.size());
    }
    
    // Generate unique ticket number
    private int generateTicketNumber() {
        while (usedTicketNumbers.contains(nextTicketNumber)) {
            nextTicketNumber++;
        }
        usedTicketNumbers.add(nextTicketNumber);
        return nextTicketNumber++;
    }
    
    // Conduct lucky draw
    public void conductDraw() {
        System.out.println("\n=== CONDUCT LUCKY DRAW ===");
        
        if (participants.isEmpty()) {
            System.out.println("❌ No participants registered yet!");
            return;
        }
        
        System.out.print("Enter number of winners to draw: ");
        int numberOfWinners;
        
        try {
            numberOfWinners = Integer.parseInt(scanner.nextLine());
            
            if (numberOfWinners <= 0) {
                System.out.println("❌ Please enter a positive number!");
                return;
            }
            
            if (numberOfWinners > participants.size()) {
                System.out.println("❌ Not enough participants! Available: " + participants.size());
                return;
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number.");
            return;
        }
        
        System.out.println("\n🎯 Drawing " + numberOfWinners + " winner(s)...");
        
        // Create a copy of participants to draw from
        List<Participant> drawPool = new ArrayList<>(participants);
        Random random = new Random();
        
        System.out.println("\n🏆 WINNERS:");
        for (int i = 1; i <= numberOfWinners; i++) {
            if (drawPool.isEmpty()) break;
            
            int winnerIndex = random.nextInt(drawPool.size());
            Participant winner = drawPool.remove(winnerIndex);
            winners.add(winner);
            
            System.out.println("\n🥇 Winner #" + i + ":");
            System.out.println(winner);
            System.out.println("Prize: " + getPrizeForRank(i));
        }
        
        System.out.println("\n✨ Draw completed successfully!");
    }
    
    // Assign prizes based on rank
    private String getPrizeForRank(int rank) {
        switch (rank) {
            case 1: return "Grand Prize - $1000";
            case 2: return "Second Prize - $500";
            case 3: return "Third Prize - $250";
            default: return "Consolation Prize - $50";
        }
    }
    
    // View all participants
    public void viewParticipants() {
        System.out.println("\n=== ALL PARTICIPANTS ===");
        
        if (participants.isEmpty()) {
            System.out.println("No participants registered yet!");
            return;
        }
        
        System.out.println("Total Participants: " + participants.size());
        System.out.println("----------------------------");
        
        for (int i = 0; i < participants.size(); i++) {
            System.out.println((i + 1) + ". " + participants.get(i));
        }
    }
    
    // View winners
    public void viewWinners() {
        System.out.println("\n=== PREVIOUS WINNERS ===");
        
        if (winners.isEmpty()) {
            System.out.println("No winners drawn yet!");
            return;
        }
        
        System.out.println("Total Winners: " + winners.size());
        System.out.println("----------------------------");
        
        for (int i = 0; i < winners.size(); i++) {
            System.out.println("Winner #" + (i + 1) + ":");
            System.out.println(winners.get(i));
            System.out.println();
        }
    }
    
    // Search participant by ticket number
    public void searchParticipant() {
        System.out.println("\n=== SEARCH PARTICIPANT ===");
        
        System.out.print("Enter Ticket Number to search: ");
        try {
            int ticketNumber = Integer.parseInt(scanner.nextLine());
            
            for (Participant p : participants) {
                if (p.ticketNumber == ticketNumber) {
                    System.out.println("\n✅ Participant Found:");
                    System.out.println(p);
                    return;
                }
            }
            
            // Also check in winners
            for (Participant w : winners) {
                if (w.ticketNumber == ticketNumber) {
                    System.out.println("\n✅ Participant Found (Winner):");
                    System.out.println(w);
                    return;
                }
            }
            
            System.out.println("❌ No participant found with ticket number: " + ticketNumber);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ticket number format!");
        }
    }
    
    // Reset draw (clear all data)
    public void resetDraw() {
        System.out.println("\n=== RESET DRAW ===");
        System.out.print("Are you sure you want to reset? All data will be lost! (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            participants.clear();
            winners.clear();
            usedTicketNumbers.clear();
            nextTicketNumber = 1001;
            System.out.println("✅ All data has been reset!");
        } else {
            System.out.println("Reset cancelled.");
        }
    }
    
    // Display statistics
    public void showStatistics() {
        System.out.println("\n=== STATISTICS ===");
        System.out.println("Total Participants: " + participants.size());
        System.out.println("Total Winners: " + winners.size());
        System.out.println("Next Ticket Number: " + nextTicketNumber);
        
        if (!participants.isEmpty()) {
            System.out.println("\nRecent Registrations:");
            int count = Math.min(5, participants.size());
            for (int i = participants.size() - count; i < participants.size(); i++) {
                System.out.println("  • " + participants.get(i).name + 
                                 " (Ticket: " + participants.get(i).ticketNumber + ")");
            }
        }
    }
    
    // Display menu
    public void displayMenu() {
        System.out.println("\n🎰 COMPUTERIZED LUCKY DRAW SYSTEM 🎰");
        System.out.println("====================================");
        System.out.println("1. Register New Participant");
        System.out.println("2. Conduct Lucky Draw");
        System.out.println("3. View All Participants");
        System.out.println("4. View Winners");
        System.out.println("5. Search Participant");
        System.out.println("6. Show Statistics");
        System.out.println("7. Reset Draw");
        System.out.println("8. Exit");
        System.out.println("====================================");
    }
    
    // Main program loop
    public void run() {
        System.out.println("🎉 Welcome to Computerized Lucky Draw System!");
        System.out.println("=============================================\n");
        
        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice (1-8): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    registerParticipant();
                    break;
                case "2":
                    conductDraw();
                    break;
                case "3":
                    viewParticipants();
                    break;
                case "4":
                    viewWinners();
                    break;
                case "5":
                    searchParticipant();
                    break;
                case "6":
                    showStatistics();
                    break;
                case "7":
                    resetDraw();
                    break;
                case "8":
                    System.out.println("\n🎊 Thank you for using Lucky Draw System!");
                    System.out.println("Goodbye! 👋");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please enter 1-8.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    // Main method
    public static void main(String[] args) {
        LuckyDrawSystem system = new LuckyDrawSystem();
        system.run();
    }
}