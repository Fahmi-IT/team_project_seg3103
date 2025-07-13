package src.main.java.main;

public interface MemberRegistry {
    boolean register(String name);
    boolean isRegistered(String name);
}