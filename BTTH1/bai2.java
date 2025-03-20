import java.util.Random;

public class bai2 {
    public static void main(String[] args) {
        // Số điểm để xấp xỉ (càng lớn càng chính xác)
        int totalPoints = 10000000;
        int pointsInsideCircle = 0;

        Random random = new Random();

        System.out.println("Dang tinh gia tri cua xap xi cua pi " + totalPoints + " diem...");

        // Sử dụng phương pháp Monte Carlo
        for (int i = 0; i < totalPoints; i++) {
            // Tạo điểm ngẫu nhiên trong hình vuông [-1, 1] × [-1, 1]
            double x = 2.0 * random.nextDouble() - 1.0;
            double y = 2.0 * random.nextDouble() - 1.0;

            // Kiểm tra xem điểm có nằm trong đường tròn đơn vị
            if (x * x + y * y <= 1.0) {
                pointsInsideCircle++;
            }
        }

        // Tính xấp xỉ π
        double piApproximation = 4.0 * pointsInsideCircle / totalPoints;

        System.out.println("Gia tri xap xi cua pi " + piApproximation);
        System.out.println("Gia tri thuc cua pi:    " + Math.PI);
        System.out.println("Sai so: " + Math.abs(piApproximation - Math.PI));
    }
}