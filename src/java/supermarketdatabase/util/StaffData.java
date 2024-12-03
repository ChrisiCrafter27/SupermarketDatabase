package supermarketdatabase.util;

public record StaffData(int id, Name name, FormattedDate birthday, String city, String street, int weeklyHours, int salary, String taskType, int supervisorId, Name supervisorName) {}
